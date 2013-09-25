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
    private static List<ReflectiveTypeHandler<?>> reflectiveTypeHandlers;

    static {
        reflectiveTypeHandlers = new ArrayList<>();
        reflectiveTypeHandlers.add(new ClassHandler());
        reflectiveTypeHandlers.add(new ParameterizedTypeHandler());
        reflectiveTypeHandlers.add(new TypeVariableTypeHandler());
        reflectiveTypeHandlers.add(new WildcardTypeTypeHandler());
    }

    private TypeReflectionUtil() {
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

    private static <T extends java.lang.reflect.Type> String getActualTypeName(final T type) {
        ReflectiveTypeHandler<T> reflectiveTypeHandler = getReflectiveTypeHandler(type);
        String result = null;
        if (reflectiveTypeHandler != null) {
            result = reflectiveTypeHandler.getActualTypeName(type);
        }
        return result;
    }

    private static <T extends java.lang.reflect.Type> String[] getActualGenericTypeName(final T type) {
        ReflectiveTypeHandler<T> reflectiveTypeHandler = getReflectiveTypeHandler(type);
        String[] result = null;
        if (reflectiveTypeHandler != null) {
            result = reflectiveTypeHandler.getActualGenericTypeName(type);
        }
        return result;
    }

    private static <T extends java.lang.reflect.Type> ReflectiveTypeHandler<T> getReflectiveTypeHandler(final T type) {
        ReflectiveTypeHandler<T> result = null;
        for (ReflectiveTypeHandler<?> handler : reflectiveTypeHandlers) {
            if (handler.accept(type)) {
                result = (ReflectiveTypeHandler<T>) handler;
                break;
            }
        }
        return result;
    }


    /**
     * A reflective type handler is capable of generating REST DC specific type information for a java type.
     * @param <T> the java type
     */
    private interface ReflectiveTypeHandler<T extends java.lang.reflect.Type> {
        String getActualTypeName(T type);

        String[] getActualGenericTypeName(T type);

        boolean accept(java.lang.reflect.Type type);
    }

    /**
     * Class handling type handler.
     */
    private static class ClassHandler implements ReflectiveTypeHandler<Class<?>> {
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

        @Override
        public boolean accept(final java.lang.reflect.Type type) {
            return (type instanceof Class);
        }
    }

    /**
     * ParameterizedType handling type handler.
     */
    private static class ParameterizedTypeHandler implements ReflectiveTypeHandler<ParameterizedType> {
        @Override
        public String getActualTypeName(final ParameterizedType type) {
            return TypeReflectionUtil.getActualTypeName(type.getRawType());
        }

        @Override
        public String[] getActualGenericTypeName(final ParameterizedType type) {
            List<String> strings = new ArrayList<>();
            for (java.lang.reflect.Type actType : type.getActualTypeArguments()) {
                strings.add(TypeReflectionUtil.getActualTypeName(actType));
            }
            return strings.toArray(new String[strings.size()]);
        }

        @Override
        public boolean accept(final java.lang.reflect.Type type) {
            return (type instanceof ParameterizedType);
        }
    }

    /**
     * TypeVariable handling type handler.
     */
    private static class TypeVariableTypeHandler implements ReflectiveTypeHandler<TypeVariable<?>> {
        @Override
        public String getActualTypeName(final TypeVariable<?> type) {
            java.lang.reflect.Type[] bounds = type.getBounds();
            String result = type.getName();
            if (!bounds[0].equals(Object.class)) {
                result += " extends " + TypeReflectionUtil.getActualTypeName(bounds[0]);
            }
            return result;
        }

        @Override
        public String[] getActualGenericTypeName(final TypeVariable<?> type) {
            return new String[0];
        }

        @Override
        public boolean accept(final java.lang.reflect.Type type) {
            return type instanceof TypeVariable;
        }
    }

    /**
     * WildcardType handling type handler.
     */
    private static class WildcardTypeTypeHandler implements ReflectiveTypeHandler<WildcardType> {
        @Override
        public String getActualTypeName(final WildcardType type) {
            java.lang.reflect.Type[] upperBounds = type.getUpperBounds();
            java.lang.reflect.Type[] lowerBounds = type.getLowerBounds();
            String result;
            if (lowerBounds.length > 0) {
                result = "super " + TypeReflectionUtil.getActualTypeName(lowerBounds[0]);
            } else {
                result = "extends " + TypeReflectionUtil.getActualTypeName(upperBounds[0]);
            }
            return result;
        }

        @Override
        public String[] getActualGenericTypeName(final WildcardType type) {
            return new String[0];
        }

        @Override
        public boolean accept(final java.lang.reflect.Type type) {
            return type instanceof WildcardType;
        }
    }

}
