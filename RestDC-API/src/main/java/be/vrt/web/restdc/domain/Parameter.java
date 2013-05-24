package be.vrt.web.restdc.domain;

/**
 * A parameter represent an argument that can be past to a REST API call. It holds information on the parameter's name,
 * descripition, type, location and whether the parameter is optional or not.
 * <p/>
 * To create instances of this class, you'll need to use it's builder: {@link be.vrt.web.restdc.domain.Parameter.ParameterBuilder}
 *
 * @author Mike Seghers
 */
public class Parameter {
    private static final int HASH_PRIME = 31;
    private String name;

    private String description;

    private Type type;

    private ParameterLocation parameterLocation;

    private boolean required;

    private Parameter(final String name, final String description, final Type type, final ParameterLocation parameterLocation, final boolean required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.parameterLocation = parameterLocation;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public ParameterLocation getParameterLocation() {
        return parameterLocation;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", parameterLocation=" + parameterLocation +
                ", required=" + required +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Parameter parameter = (Parameter) o;

        if (required != parameter.required) {
            return false;
        }
        if (description != null ? !description.equals(parameter.description) : parameter.description != null) {
            return false;
        }
        if (name != null ? !name.equals(parameter.name) : parameter.name != null) {
            return false;
        }
        if (parameterLocation != parameter.parameterLocation) {
            return false;
        }
        if (!type.equals(parameter.type)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = HASH_PRIME * result + (description != null ? description.hashCode() : 0);
        result = HASH_PRIME * result + type.hashCode();
        result = HASH_PRIME * result + parameterLocation.hashCode();
        result = HASH_PRIME * result + (required ? 1 : 0);
        return result;
    }

    /**
     * {@link Parameter} builder
     */
    public static class ParameterBuilder {

        private String name;
        private String description;
        private Type type;
        private ParameterLocation parameterLocation;
        private boolean required;

        /**
         * Add the given name to the builder
         * @param name the name
         * @return this builder
         */
        public ParameterBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Add the given description to the builder
         * @param description the description
         * @return this builder
         */
        public ParameterBuilder withDescription(final String description) {
            this.description = description;
            return this;
        }

        /**
         * Add the given type to the builder
         * @param type the type
         * @return this builder
          */
        public ParameterBuilder withType(final Type type) {
            this.type = type;
            return this;
        }

        /**
         * Add the given parameter location to the builder
         * @param parameterLocation the parameter location
         * @return this builder
         */
        public ParameterBuilder withParameterLocation(final ParameterLocation parameterLocation) {
            this.parameterLocation = parameterLocation;
            return this;
        }

        /**
         * Add the given required flag to the builder
         * @param required the required flag
         * @return this builder
         */
        public ParameterBuilder isRequired(final boolean required) {
            this.required = required;
            return this;
        }

        /**
         * Builds the Parameter
         * @return the build Parameter
         */
        public Parameter build() {
            return new Parameter(name, description, type, parameterLocation, required);
        }
    }
}
