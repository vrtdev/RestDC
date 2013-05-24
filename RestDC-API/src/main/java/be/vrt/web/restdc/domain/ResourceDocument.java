package be.vrt.web.restdc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A DocumentSet holds all documentation about a REST API call.
 * <p/>
 * To create instances of this class, you'll need to use it's builder: {@link be.vrt.web.restdc.domain.ResourceDocument.ResourceDocumentBuilder}
 *
 * @author Mike Seghers
 */
public class ResourceDocument {
    private Set<RequestMethod> requestMethods;

    private String url;

    private String description;

    private Set<MimeType> consumesMimeTypes;

    private Set<MimeType> producesMimeTypes;

    private List<Parameter> parameters;

    private Type returnType;

    private ResourceDocument(final Set<RequestMethod> requestMethods, final String url, final String description,
                             final Set<MimeType> consumesMimeTypes, final Set<MimeType> producesMimeTypes,
                             final List<Parameter> parameters, final Type returnType) {
        if (requestMethods != null && !requestMethods.isEmpty()) {
            this.requestMethods = Collections.unmodifiableSet(requestMethods);
        }
        this.url = url;
        this.description = description;
        if (consumesMimeTypes != null && !consumesMimeTypes.isEmpty()) {
            this.consumesMimeTypes = Collections.unmodifiableSet(consumesMimeTypes);
        }
        if (producesMimeTypes != null && !producesMimeTypes.isEmpty()) {
            this.producesMimeTypes = Collections.unmodifiableSet(producesMimeTypes);
        }
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters = Collections.unmodifiableList(parameters);
        }
        this.returnType = returnType;
    }

    public Set<RequestMethod> getRequestMethods() {
        return requestMethods;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Set<MimeType> getConsumesMimeTypes() {
        return consumesMimeTypes;
    }

    public Set<MimeType> getProducesMimeTypes() {
        return producesMimeTypes;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "ResourceDocument{" +
                "requestMethods=" + requestMethods +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", consumesMimeTypes=" + consumesMimeTypes +
                ", producesMimeTypes=" + producesMimeTypes +
                ", parameters=" + parameters +
                ", returnType=" + returnType +
                '}';
    }

    /**
     * {@link ResourceDocument} builder
     */
    public static class ResourceDocumentBuilder {

        private Set<RequestMethod> requestMethods = new HashSet<>();
        private String url;
        private String description;
        private Set<MimeType> consumesMimeTypes = new HashSet<>();
        private Set<MimeType> producesMimeTypes = new HashSet<>();
        private List<Parameter> parameters = new ArrayList<>();
        private Type returnType;

        /**
         * Add the given request method to the builder.
         *
         * @param requestMethod the request method
         * @return this builder
         */
        public ResourceDocumentBuilder addRequestMethod(final RequestMethod requestMethod) {
            this.requestMethods.add(requestMethod);
            return this;
        }

        /**
         * Add all given request methods to the builder.
         *
         * @param requestMethods the request methods
         * @return this builder
         */
        public ResourceDocumentBuilder addAllRequestMethods(final Set<RequestMethod> requestMethods) {
            this.requestMethods.addAll(requestMethods);
            return this;
        }

        /**
         * Add the given url to the builder.
         *
         * @param url the url
         * @return this builder
         */
        public ResourceDocumentBuilder withUrl(final String url) {
            this.url = url;
            return this;
        }

        /**
         * Add the given description to the builder.
         *
         * @param description the description
         * @return this builder
         */
        public ResourceDocumentBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        /**
         * Add all given consuming mime types to the builder.
         *
         * @param consumingMimeTypes the consuming mime types
         * @return this builder
         */
        public ResourceDocumentBuilder addAllConsumingMimeTypes(final Set<MimeType> consumingMimeTypes) {
            this.consumesMimeTypes.addAll(consumingMimeTypes);
            return this;
        }

        /**
         * Add the given consuming mime type to the builder.
         *
         * @param consumingMimeType the consuming mime type
         * @return this builder
         */
        public ResourceDocumentBuilder addConsumingMimeType(final MimeType consumingMimeType) {
            this.consumesMimeTypes.add(consumingMimeType);
            return this;
        }

        /**
         * Add all given strings which represent consuming mime types to the builder. The strings are internally
         * transformed to {@link be.vrt.web.restdc.domain.MimeType}s
         *
         * @param strings the strings which represent mime types
         * @return this builder
         */
        public ResourceDocumentBuilder addConsumingMimeTypesWithStrings(final String[] strings) {
            for (String string : strings) {
                this.addConsumingMimeType(MimeType.getMimeType(string));
            }
            return this;
        }

        /**
         * Add all given producing mime types to the builder.
         *
         * @param producingMimeTypes the producing mime types
         * @return this builder
         */
        public ResourceDocumentBuilder addAllProducingMimeTypes(final Set<MimeType> producingMimeTypes) {
            this.producesMimeTypes.addAll(producingMimeTypes);
            return this;
        }

        /**
         * Add the given producing mime type to the builder.
         *
         * @param producingMimeType the producing mime type
         * @return this builder
         */
        public ResourceDocumentBuilder addProducingMimeType(final MimeType producingMimeType) {
            this.producesMimeTypes.add(producingMimeType);
            return this;
        }

        /**
         * Add all given strings which represent producing mime types to the builder. The strings are internally
         * transformed to {@link be.vrt.web.restdc.domain.MimeType}s
         *
         * @param strings the strings which represent mime types
         * @return this builder
         */
        public ResourceDocumentBuilder addProducingMimeTypesWithStrings(final String[] strings) {
            for (String string : strings) {
                this.addProducingMimeType(MimeType.getMimeType(string));
            }
            return this;
        }

        /**
         * Add all given parameters to the builder.
         *
         * @param parameters the parameters
         * @return this builder
         */
        public ResourceDocumentBuilder addAllParameters(final List<Parameter> parameters) {
            this.parameters.addAll(parameters);
            return this;
        }

        /**
         * Add the given parameter to the builder.
         *
         * @param parameter the parameter
         * @return this builder
         */
        public ResourceDocumentBuilder addParameter(final Parameter parameter) {
            this.parameters.add(parameter);
            return this;
        }

        /**
         * Add the given return type to the builder.
         *
         * @param returnType the return type
         * @return this builder
         */
        public ResourceDocumentBuilder withReturnType(final Type returnType) {
            this.returnType = returnType;
            return this;
        }

        /**
         * Build the ResourceDocument.
         *
         * @return the build ResourceDocument
         */
        public ResourceDocument build() {
            return new ResourceDocument(requestMethods, url, description, consumesMimeTypes, producesMimeTypes, parameters, returnType);
        }
    }
}
