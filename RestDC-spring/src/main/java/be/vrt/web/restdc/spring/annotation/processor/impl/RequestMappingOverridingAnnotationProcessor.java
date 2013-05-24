package be.vrt.web.restdc.spring.annotation.processor.impl;

import be.vrt.web.restdc.annotation.RestDescription;
import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.Parameter;
import be.vrt.web.restdc.domain.ParameterLocation;
import be.vrt.web.restdc.domain.RequestMethod;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.domain.Type;
import be.vrt.web.restdc.reflection.TypeReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Annotation processor which processes RequestMapping annotations on class and method level to generate a {@link
 * be.vrt.web.restdc.domain.ResourceDocument} out of it.
 *
 * @author Mike Seghers
 */
public class RequestMappingOverridingAnnotationProcessor implements OverridingAnnotationProcessor<RequestMapping, ResourceDocument, Method, Class<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMappingOverridingAnnotationProcessor.class);

    private ParameterNameDiscoverer parameterNameDiscoverer;

    /**
     * Constructor, initializing Spring's ParameterNameDiscoverer.
     *
     * @param parameterNameDiscoverer the parameter name discoverer for resolving method parameter names.
     */
    public RequestMappingOverridingAnnotationProcessor(final ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Override
    public ResourceDocument process(final RequestMapping annotation, final Method origin, final RequestMapping baseAnnotation, final Class<?> baseOrigin) {
        LOGGER.trace("Processing RequestMapping {} with base {}", annotation, baseAnnotation);
        RestDescription descriptionAnnotation = origin.getAnnotation(RestDescription.class);

        ResourceDocument.ResourceDocumentBuilder builder = new ResourceDocument.ResourceDocumentBuilder();
        String description = null;
        if (descriptionAnnotation != null) {
            description = descriptionAnnotation.value();
        }
        builder.withDescription(description);
        LOGGER.trace("Calculated description for element {} is \"{}\">", origin, description);
        String baseUrl = "";
        org.springframework.web.bind.annotation.RequestMethod[] defaultMethods = null;
        if (baseAnnotation != null) {
            if (baseAnnotation.value().length > 0) {
                baseUrl = baseAnnotation.value()[0];
            }
            builder.addConsumingMimeTypesWithStrings(baseAnnotation.consumes())
                   .addProducingMimeTypesWithStrings(baseAnnotation.produces());
            defaultMethods = baseAnnotation.method();
        }
        LOGGER.debug("Calculated base URL for base origin {} is \"{}\">", baseOrigin, baseUrl);

        org.springframework.web.bind.annotation.RequestMethod[] methods = annotation.method();
        if (methods.length == 0) {
            if (defaultMethods == null || defaultMethods.length == 0) {
                methods = new org.springframework.web.bind.annotation.RequestMethod[]{org.springframework.web.bind.annotation.RequestMethod.GET};
            } else {
                methods = defaultMethods;
            }
        }

        Set<RequestMethod> mappedRequestMethods = new HashSet<>(methods.length);
        for (org.springframework.web.bind.annotation.RequestMethod requestMethod : methods) {
            mappedRequestMethods.add(RequestMethod.valueOf(requestMethod.name()));
        }

        builder.addAllRequestMethods(mappedRequestMethods)
               .withReturnType(TypeReflectionUtil.getTypeFromReflectionType(origin.getGenericReturnType()))
               .addConsumingMimeTypesWithStrings(annotation.consumes())
               .addProducingMimeTypesWithStrings(annotation.produces());

        if (annotation.value().length == 0) {
            builder.withUrl(baseUrl);
        } else {
            builder.withUrl(new StringBuilder(baseUrl).append(annotation.value()[0]).toString());
        }

        addParametersForMethod(builder, origin);
        ResourceDocument resourceDocument = builder.build();
        LOGGER.debug("Build ResourceDocument {}", resourceDocument);
        return resourceDocument;
    }


    private void addParametersForMethod(final ResourceDocument.ResourceDocumentBuilder builder, final Method requestMappingMethod) {
        LOGGER.trace("Building parameters for method {}", requestMappingMethod);
        java.lang.reflect.Type[] genericParameterTypes = requestMappingMethod.getGenericParameterTypes();
        if (genericParameterTypes.length > 0) {
            Annotation[][] parameterAnnotations = requestMappingMethod.getParameterAnnotations();
            String[] parametersNames = getBoundSafeMethodNames(requestMappingMethod, genericParameterTypes);
            for (int i = 0; i < genericParameterTypes.length; i++) {
                Parameter parameter = getParameterFromAnnotations(parameterAnnotations[i], genericParameterTypes[i], parametersNames[i]);
                if (parameter.getName() == null && parameter.getParameterLocation() != ParameterLocation.BODY) {
                    LOGGER.warn("A parameter was build without a name, Spring's auto-discovery apparently was unable to " +
                            "get parameter names, and you didn't specify a specific name in the annotation on the parameter. " +
                            "Your documentation might not be clear to the user!\nTo give you some context, here is the method information: {}", requestMappingMethod);
                }

                builder.addParameter(parameter);
            }
        }
    }

    private Parameter getParameterFromAnnotations(final Annotation[] specificParameterAnnotations, final java.lang.reflect.Type genericParameterType, final String nameFromDiscovery) {
        LOGGER.trace("Building parameter with discover name \"{}\" based on annotations:\n{}", nameFromDiscovery, specificParameterAnnotations);
        Parameter.ParameterBuilder builder = new Parameter.ParameterBuilder();
        ParameterLocation location = null;
        boolean required = true;
        String name = nameFromDiscovery;
        for (Annotation annotation : specificParameterAnnotations) {
            if (annotation instanceof PathVariable) {
                location = ParameterLocation.PATH;
                name = getParameterName(((PathVariable) annotation).value(), nameFromDiscovery);
            } else if (annotation instanceof RequestParam) {
                location = ParameterLocation.PARAMETERS;
                RequestParam requestParam = (RequestParam) annotation;
                name = getParameterName(requestParam.value(), nameFromDiscovery);
                required = requestParam.required();
            } else if (annotation instanceof RequestHeader) {
                location = ParameterLocation.HEADER;
                RequestHeader requestHeader = (RequestHeader) annotation;
                required = requestHeader.required();
                name = getParameterName(requestHeader.value(), nameFromDiscovery);
            } else if (annotation instanceof RestDescription) {
                builder.withDescription(((RestDescription) annotation).value());
            } else if (annotation instanceof RequestBody) {
                location = ParameterLocation.BODY;
                required = ((RequestBody) annotation).required();
            }
        }

        Parameter parameter = builder.withName(name).withParameterLocation(location)
                                     .withType(TypeReflectionUtil.getTypeFromReflectionType(genericParameterType)).isRequired(required)
                                     .build();
        LOGGER.trace("Done filling parameter based on annotations:\n{}", parameter);
        return parameter;
    }

    private String[] getBoundSafeMethodNames(final Method requestMappingMethod, final java.lang.reflect.Type[] genericParameterTypes) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(requestMappingMethod);
        if (parameterNames == null) {
            LOGGER.trace("Parameter names could not be discoverd, defaulting to NULL parameter names");
            parameterNames = new String[genericParameterTypes.length];
        }
        return parameterNames;
    }

    private String getParameterName(final String annotationValue, final String name) {
        String result;
        if (annotationValue.length() != 0) {
            result = annotationValue;
        } else {
            result = name;
        }
        return result;
    }
}
