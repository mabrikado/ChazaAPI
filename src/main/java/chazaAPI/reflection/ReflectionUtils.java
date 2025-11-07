package chazaAPI.reflection;

import chazaAPI.annotations.Chaza;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for performing reflection-based operations on classes,
 * including recursively retrieving field information.
 */
public class ReflectionUtils {

    /**
     * Recursively retrieves a map of field names to their types for the given class,
     * including nested fields for complex types and handling collections with generics.
     *
     * <p>This method avoids infinite recursion by tracking visited classes.</p>
     *
     * @param clazz the class to inspect
     * @param visitedClasses a set of classes already visited to prevent infinite recursion
     * @return a map where keys are field names and values are either:
     *         - a simple type name (for primitives, wrappers, enums, and Strings),
     *         - a nested map of fields for complex types,
     *         - or a list containing nested fields for collections
     */
    public static Map<String, Object> getFieldsRecursive(Class<?> clazz, Set<Class<?>> visitedClasses) {
        Map<String, Object> fieldsMap = new LinkedHashMap<>();

        if (clazz == null || clazz == Object.class || visitedClasses.contains(clazz)) {
            return fieldsMap;
        }

        visitedClasses.add(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (isPrimitiveOrWrapper(fieldType) || fieldType == String.class || fieldType.isEnum()) {
                fieldsMap.put(field.getName(), fieldType.getSimpleName());
            } else if (Collection.class.isAssignableFrom(fieldType)) {
                Type genericType = field.getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    Type[] actualTypeArguments = pt.getActualTypeArguments();

                    if (actualTypeArguments.length == 1) {
                        Type actualType = actualTypeArguments[0];

                        if (actualType instanceof Class<?>) {
                            Class<?> actualClass = (Class<?>) actualType;

                            Map<String, Object> nestedFields = getFieldsRecursive(actualClass, visitedClasses);

                            fieldsMap.put(field.getName(), List.of(nestedFields));
                        } else {
                            fieldsMap.put(field.getName(), "Collection");
                        }
                    } else {
                        fieldsMap.put(field.getName(), "Collection");
                    }
                } else {
                    fieldsMap.put(field.getName(), "Collection");
                }

            } else {
                fieldsMap.put(field.getName(), getFieldsRecursive(fieldType, visitedClasses));
            }
        }

        return fieldsMap;
    }

    public static List<Class<?>> findChazaControllers(String basePackage) {
        Reflections reflections = new Reflections(basePackage, Scanners.TypesAnnotated);

        Set<Class<?>> chazaClasses = reflections.getTypesAnnotatedWith(Chaza.class);

        // Convert Set to List
        return new ArrayList<>(chazaClasses);
    }

    /**
     * Checks whether the provided class is a primitive type or a wrapper of a primitive.
     *
     * @param clazz the class to check
     * @return {@code true} if the class is a primitive type or wrapper class, {@code false} otherwise
     */
    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Integer.class || clazz == Long.class ||
                clazz == Double.class || clazz == Float.class ||
                clazz == Boolean.class || clazz == Byte.class ||
                clazz == Short.class || clazz == Character.class;
    }
}
