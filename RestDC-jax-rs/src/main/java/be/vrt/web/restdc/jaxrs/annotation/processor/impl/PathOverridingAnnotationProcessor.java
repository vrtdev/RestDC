package be.vrt.web.restdc.jaxrs.annotation.processor.impl;

import be.vrt.web.restdc.annotation.RestDescription;
import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.Parameter;
import be.vrt.web.restdc.domain.ParameterLocation;
import be.vrt.web.restdc.domain.RequestMethod;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.domain.Type;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mike Seghers
 */
public class PathOverridingAnnotationProcessor implements OverridingAnnotationProcessor<Path, ResourceDocument, Method, Class<?>> {
    public static final List<Class<? extends Annotation>> ALLOWED_ANNOTATION_TYPES = Arrays.asList(PathParam.class, QueryParam.class, HeaderParam.class, RestDescription.class);
    /**
     * SLF4J Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PathOverridingAnnotationProcessor.class);

    @Override
    public ResourceDocument process(final Path annotation, final Method annotatedElement, final Path baseAnnotation, final Class<?> baseOrigin) {
        LOGGER.trace("Processing RequestMapping {} with base {}", annotation, baseAnnotation);
        ResourceDocument.ResourceDocumentBuilder builder = new ResourceDocument.ResourceDocumentBuilder();
        ResourceDocument resourceDocument = builder.withDescription(getDescription(annotatedElement)).addAllRequestMethods(getRequestMethods(annotatedElement))
                                                   .withReturnType(getReturnType(annotatedElement)).withUrl(getURL(baseAnnotation, annotation))
                                                   .addConsumingMimeTypesWithStrings(getConsumingMimeTypes(baseOrigin.getAnnotation(Consumes.class)))
                                                   .addProducingMimeTypesWithStrings(getProducingMimeTypes(baseOrigin.getAnnotation(Produces.class)))
                                                   .addConsumingMimeTypesWithStrings(getConsumingMimeTypes(annotatedElement.getAnnotation(Consumes.class)))
                                                   .addProducingMimeTypesWithStrings(getProducingMimeTypes(annotatedElement.getAnnotation(Produces.class)))
                                                   .addAllParameters(getParameters(annotatedElement)).build();
        LOGGER.debug("Build ResourceDocument {}", resourceDocument);
        return resourceDocument;
    }

    private String getDescription(final Method annotatedElement) {
        RestDescription descriptionAnnotation = annotatedElement.getAnnotation(RestDescription.class);
        String description = null;
        if (descriptionAnnotation != null) {
            description = descriptionAnnotation.value();
        }
        return description;
    }

    private String getURL(final Path baseAnnotation, final Path annotation) {
        String baseUrl = "";
        if (baseAnnotation != null && baseAnnotation.value().length() > 0) {
            baseUrl = baseAnnotation.value();
        }
        if (annotation != null && annotation.value().length() > 0) {
            baseUrl += annotation.value();
        }
        return baseUrl;
    }

    private Set<RequestMethod> getRequestMethods(final Method annotatedElement) {
        List<Annotation> httpMethodAnnotations = getAnnotationsOnElementAnnotatedWith(annotatedElement, HttpMethod.class);
        Set<RequestMethod> mappedRequestMethods = new HashSet<>(httpMethodAnnotations.size());
        for (Annotation httpMethodAnnotation : httpMethodAnnotations) {
            HttpMethod httpMethod = httpMethodAnnotation.annotationType().getAnnotation(HttpMethod.class);
            mappedRequestMethods.add(RequestMethod.valueOf(httpMethod.value()));
        }
        if (mappedRequestMethods.size() == 0) {
            mappedRequestMethods.add(RequestMethod.GET);
        }

        return mappedRequestMethods;
    }

    private Type getReturnType(final Method annotatedElement) {
        return TypeReflectionUtil.getTypeFromReflectionType(annotatedElement.getGenericReturnType());
    }


    private String[] getProducingMimeTypes(final Produces produces) {
        String[] value = null;
        if (produces != null) {
            value = produces.value();
        }
        return value;
    }

    private String[] getConsumingMimeTypes(final Consumes consumes) {
        String[] value = null;
        if (consumes != null) {
            value = consumes.value();
        }
        return value;
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

    private List<Parameter> getParameters(final Method requestMappingMethod) {
        LOGGER.trace("Building parameters for method {}", requestMappingMethod);
        java.lang.reflect.Type[] genericParameterTypes = requestMappingMethod.getGenericParameterTypes();
        List<Parameter> result = null;
        if (genericParameterTypes.length > 0) {
            result = new ArrayList<>(genericParameterTypes.length);
            Annotation[][] parameterAnnotations = requestMappingMethod.getParameterAnnotations();
            for (int i = 0; i < genericParameterTypes.length; i++) {
                Parameter pfa = getParameterFromAnnotations(parameterAnnotations[i], genericParameterTypes[i]);
                if (pfa != null) {
                    result.add(pfa);
                }
            }
        }
        return result;
    }

    private Parameter getParameterFromAnnotations(final Annotation[] specificParameterAnnotations, final java.lang.reflect.Type genericParameterType) {
        LOGGER.trace("Building parameter based on annotations:\n{}", specificParameterAnnotations);
        Parameter parameter = null;
        if (isDocumentableAnnotationArray(specificParameterAnnotations)) {
            parameter = getDocumentableParameter(specificParameterAnnotations, genericParameterType);
        }
        LOGGER.trace("Done filling parameter based on annotations:\n{}", parameter);
        return parameter;
    }

    private Parameter getDocumentableParameter(Annotation[] specificParameterAnnotations, java.lang.reflect.Type genericParameterType) {
        Parameter parameter;
        Parameter.ParameterBuilder builder = new Parameter.ParameterBuilder(TypeReflectionUtil.getTypeFromReflectionType(genericParameterType));
        ParameterLocation location = null;
        boolean required = true;
        String name = null;
        if (specificParameterAnnotations.length == 0) {
            //No annotation at all means request body parameter
            location = ParameterLocation.BODY;
        } else if (specificParameterAnnotations.length == 1 && specificParameterAnnotations[0].annotationType().equals(RestParameterName.class)) {
            location = ParameterLocation.BODY;
            name = ((RestParameterName) specificParameterAnnotations[0]).value();
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
        parameter = builder.withName(name).withParameterLocation(location).isRequired(required).build();
        return parameter;
    }

    private boolean isDocumentableAnnotationArray(final Annotation[] annotations) {
        boolean result = false;
        if (annotations.length == 0) {
            result = true;
        }
        if (annotations.length == 1 && annotations[0].annotationType().equals(RestParameterName.class)) {
            result = true;
        } else {
            int i = 0;
            while (!result && i < annotations.length) {
                result = ALLOWED_ANNOTATION_TYPES.contains(annotations[i++].annotationType());
            }
        }

        return result;
    }
}
