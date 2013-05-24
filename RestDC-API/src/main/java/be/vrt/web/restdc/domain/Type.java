package be.vrt.web.restdc.domain;

import java.util.Arrays;

/**
 * A type holds type information on return and parameter values in a REST API.
 *
 * @author Mike Seghers
 */
public class Type {
    public static final int HASH_PRIME = 31;
    private String typeName;
    private String[] genericTypeNames;

    private Type(final String typeName, final String[] genericTypeNames) {
        this.typeName = typeName;
        this.genericTypeNames = genericTypeNames;
    }

    public String getTypeName() {
        return typeName;
    }

    public String[] getGenericTypeNames() {
        return genericTypeNames;
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeName='" + typeName + '\'' +
                ", genericTypeNames=" + Arrays.toString(genericTypeNames) +
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

        Type type = (Type) o;

        if (!Arrays.equals(genericTypeNames, type.genericTypeNames)) {
            return false;
        }
        if (!typeName.equals(type.typeName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = typeName.hashCode();
        result = HASH_PRIME * result + (genericTypeNames != null ? Arrays.hashCode(genericTypeNames) : 0);
        return result;
    }

    /**
     * {@link Type} builder.
     */
    public static class TypeBuilder {
        private String typeName;
        private String[] genericTypeNames;

        /**
         * Adds a type name to the builder.
         * @param typeName the type name
         * @return this builder
         */
        public TypeBuilder withTypeName(final String typeName) {
            this.typeName = typeName;
            return this;
        }

        /**
         * Adds generic type names to the builder.
         * @param genericTypeNames the generuc type names
         * @return this builder
         */
        public TypeBuilder withGenericTypeNames(final String... genericTypeNames) {
            this.genericTypeNames = genericTypeNames;
            return this;
        }

        /**
         * Builds the {@link Type}
         * @return the build type
         */
        public Type build() {
            return new Type(typeName, genericTypeNames);
        }
    }
}
