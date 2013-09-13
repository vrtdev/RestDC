package be.vrt.web.restdc.reflection;

import be.vrt.web.restdc.domain.Type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mike Seghers
 */
public final class TypeReflectionUtil {
    private TypeReflectionUtil() {

    }

    private static String getActualTypeName(final java.lang.reflect.Type type) {
        String result = null;

        if (type instanceof Class) {
            result = ((Class) type).getSimpleName();
        } else if (type instanceof ParameterizedType) {
            result = getActualTypeName(((ParameterizedType) type).getRawType());
        } else if (type instanceof TypeVariable) {
            TypeVariable typeVar = (TypeVariable) type;
            java.lang.reflect.Type[] bounds = typeVar.getBounds();
            result = typeVar.getName();
            if (!bounds[0].equals(Object.class)) {
                result += " extends " + getActualTypeName(bounds[0]);
            }
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            java.lang.reflect.Type[] upperBounds = wildcardType.getUpperBounds();
            java.lang.reflect.Type[] lowerBounds = wildcardType.getLowerBounds();
            if (lowerBounds.length > 0) {
                result = "super " + getActualTypeName(lowerBounds[0]);
            } else {
                result = "extends " + getActualTypeName(upperBounds[0]);
            }

        }
        return result;
    }

    private static String[] getActualGenericTypeName(final java.lang.reflect.Type type) {
        String[] result = null;
        if (type instanceof ParameterizedType) {
            List<String> strings = new ArrayList<>();
            for (java.lang.reflect.Type actType : ((ParameterizedType) type).getActualTypeArguments()) {
                strings.add(getActualTypeName(actType));
            }
            result = strings.toArray(new String[strings.size()]);
        } else if (type instanceof Class) {
            List<String> strings = new ArrayList<>();
            for (TypeVariable tv : ((Class) type).getTypeParameters()) {
                strings.add(getActualTypeName(tv));
            }
            result = strings.toArray(new String[strings.size()]);
        }
        return result;
    }

    /**
     * Returns a RestDC Type based on a java type.
     *
     * @param reflectionType The java type
     * @return the RestDC type
     */
    public static Type getTypeFromReflectionType(final java.lang.reflect.Type reflectionType) {
        Type.TypeBuilder builder = new Type.TypeBuilder(getActualTypeName(reflectionType));
        if (reflectionType instanceof ParameterizedType || reflectionType instanceof Class) {
            builder.withGenericTypeNames(getActualGenericTypeName(reflectionType));
        }

        return builder.build();
    }


    private interface ReflectiveTypeHandler<T extends java.lang.reflect.Type> {
        String getActualTypeName(T type);
        String[] getActualGenericTypeName(T type);
    }

    private class ClassHandler implements ReflectiveTypeHandler<Class<?>> {
        @Override
        public String getActualTypeName(final Class<?> type) {
            return type.getSimpleName();
        }

        @Override
        public String[] getActualGenericTypeName(final Class<?> type) {
            List<String> strings = new ArrayList<>();
            for (TypeVariable tv : type.getTypeParameters()) {
                strings.add(TypeReflectionUtil.getActualTypeName(tv));
            }
            return strings.toArray(new String[strings.size()]);
        }
    }

    private class ParameterizedTypeHandler implements ReflectiveTypeHandler<ParameterizedType> {
        @Override
        public String getActualTypeName(final ParameterizedType type) {
            return TypeReflectionUtil.getActualTypeName(type.getRawType());
        }

        @Override
        public String[] getActualGenericTypeName(ParameterizedType type) {
            List<String> strings = new ArrayList<>();
            for (java.lang.reflect.Type actType : type.getActualTypeArguments()) {
                strings.add(TypeReflectionUtil.getActualTypeName(actType));
            }
            return strings.toArray(new String[strings.size()]);
        }
    }

}
