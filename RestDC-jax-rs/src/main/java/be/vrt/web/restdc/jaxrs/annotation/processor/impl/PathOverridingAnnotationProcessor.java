package be.vrt.web.restdc.jaxrs.annotation.processor.impl;

import be.vrt.web.restdc.annotation.RestDescription;
import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.Parameter;
import be.vrt.web.restdc.domain.ParameterLocation;
import be.vrt.web.restdc.domain.RequestMethod;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.jaxrs.annotation.RestParameterName;
import be.vrt.web.restdc.reflection.TypeReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mike Seghers
 */
public class PathOverridingAnnotationProcessor implements OverridingAnnotationProcessor<Path, ResourceDocument, Method, Class<?>> {
    /**
     * SLF4J Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PathOverridingAnnotationProcessor.class);

    @Override
    public ResourceDocument process(Path annotation, Method origin, Path baseAnnotation, Class<?> baseOrigin) {
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
        List<Annotation> httpMethodAnnotations = getAnnotationsOnElementAnnotatedWith(origin, HttpMethod.class);
        Set<RequestMethod> mappedRequestMethods = new HashSet<>(httpMethodAnnotations.size());
        for (Annotation httpMethodAnnotation : httpMethodAnnotations) {
            HttpMethod httpMethod = httpMethodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            mappedRequestMethods.add(RequestMethod.valueOf(httpMethod.value()));
        }
        if (mappedRequestMethods.size() == 0) {
            mappedRequestMethods.add(RequestMethod.GET);
        }


        RequestMethod[] requestMethods = null;


        if (baseAnnotation != null) {
            if (baseAnnotation.value().length() > 0) {
                baseUrl = baseAnnotation.value();
            }
        }

        Consumes baseConsumes = baseOrigin.getAnnotation(Consumes.class);
        if (baseConsumes != null) {
            builder.addConsumingMimeTypesWithStrings(baseConsumes.value());
        }
        Produces baseProduces = baseOrigin.getAnnotation(Produces.class);
        if (baseProduces != null) {
            builder.addProducingMimeTypesWithStrings(baseProduces.value());
        }

        LOGGER.debug("Calculated base URL for base origin {} is \"{}\">", baseOrigin, baseUrl);
        builder.addAllRequestMethods(mappedRequestMethods)
               .withReturnType(TypeReflectionUtil.getTypeFromReflectionType(origin.getGenericReturnType()));

        Consumes consumes = origin.getAnnotation(Consumes.class);
        if (consumes != null) {
            builder.addConsumingMimeTypesWithStrings(consumes.value());
        }
        Produces produces = origin.getAnnotation(Produces.class);
        if (produces != null) {
            builder.addProducingMimeTypesWithStrings(produces.value());
        }
        if (annotation.value().length() == 0) {
            builder.withUrl(baseUrl);
        } else {
            builder.withUrl(new StringBuilder(baseUrl).append(annotation.value()).toString());
        }

        addParametersForMethod(builder, origin);
        ResourceDocument resourceDocument = builder.build();
        LOGGER.debug("Build ResourceDocument {}", resourceDocument);
        return resourceDocument;
    }

    /**
     * Find all annotation on an AnnotatedElement which have on their own also an annotation of the given
     * annotationType.
     *
     * @param element        the element
     * @param annotationType the annotation type.
     * @return a list of annotations found on the given elemen, which are annotated with the given annotationType
     */
    private List<Annotation> getAnnotationsOnElementAnnotatedWith(final AnnotatedElement element, final Class<? extends Annotation> annotationType) {
        Annotation[] annotations = element.getAnnotations();
        List<Annotation> result = new ArrayList<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getAnnotation(annotationType) != null) {
                result.add(annotation);
            }
        }
        return result;
    }

    private void addParametersForMethod(final ResourceDocument.ResourceDocumentBuilder builder, final Method requestMappingMethod) {
        LOGGER.trace("Building parameters for method {}", requestMappingMethod);
        java.lang.reflect.Type[] genericParameterTypes = requestMappingMethod.getGenericParameterTypes();
        if (genericParameterTypes.length > 0) {
            Annotation[][] parameterAnnotations = requestMappingMethod.getParameterAnnotations();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                Parameter parameter = getParameterFromAnnotations(parameterAnnotations[i], genericParameterTypes[i]);
                if (parameter.getName() == null && parameter.getParameterLocation() != ParameterLocation.BODY) {
                    LOGGER.warn("A parameter was build without a name, Spring's auto-discovery apparently was unable to " +
                            "get parameter names, and you didn't specify a specific name in the annotation on the parameter. " +
                            "Your documentation might not be clear to the user!\nTo give you some context, here is the method information: {}", requestMappingMethod);
                }

                builder.addParameter(parameter);
            }
        }
    }

    private Parameter getParameterFromAnnotations(final Annotation[] specificParameterAnnotations, final java.lang.reflect.Type genericParameterType) {
        LOGGER.trace("Building parameter based on annotations:\n{}", specificParameterAnnotations);
        Parameter.ParameterBuilder builder = new Parameter.ParameterBuilder();
        ParameterLocation location = null;
        boolean required = true;
        String name = null;
        if (specificParameterAnnotations.length == 0) {
            //No annotation at all means request body parameter
            location = ParameterLocation.BODY;
        } else if (specificParameterAnnotations.length == 1 && specificParameterAnnotations[0].annotationType().equals(RestParameterName.class)) {
            location = ParameterLocation.BODY;
            name = ((RestParameterName)specificParameterAnnotations[0]).value();
        } else {
            for (Annotation annotation : specificParameterAnnotations) {
                if (annotation instanceof PathParam) {
                    location = ParameterLocation.PATH;
                    name = ((PathParam) annotation).value();
                } else if (annotation instanceof QueryParam) {
                    location = ParameterLocation.PARAMETERS;
                    QueryParam queryParam = (QueryParam) annotation;
                    name = queryParam.value();
                } else if (annotation instanceof HeaderParam) {
                    location = ParameterLocation.HEADER;
                    HeaderParam headerParam = (HeaderParam) annotation;
                    name = headerParam.value();
                } else if (annotation instanceof RestDescription) {
                    builder.withDescription(((RestDescription) annotation).value());
                }
            }
        }

        Parameter parameter = builder.withName(name).withParameterLocation(location)
                                     .withType(TypeReflectionUtil.getTypeFromReflectionType(genericParameterType))
                                     .isRequired(required).build();
        LOGGER.trace("Done filling parameter based on annotations:\n{}", parameter);
        return parameter;
    }
}
