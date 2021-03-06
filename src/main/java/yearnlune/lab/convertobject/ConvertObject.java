package yearnlune.lab.convertobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project : convert-object
 * Created by IntelliJ IDEA
 * Author : DONGHWAN, KIM
 * DATE : 2020.02.05
 * DESCRIPTION :
 */
public class ConvertObject {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T string2Object(String string, Class<T> tClass) {
        T t = null;
        try {
            t = objectMapper.readValue(string, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T string2Object(String string, Class<T> tClass, String dateFormat) {
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        T t = null;
        try {
            t = objectMapper.readValue(string, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> string2ObjectList(String string, Class<T[]> tClass) {
        try {
            return Arrays.asList(objectMapper.readValue(string, tClass));

        } catch (IOException e) {
            System.err.println("Object : " + tClass.toString());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String objectClass2String(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T objectClass2Object(Object object, Class<T> tClass) {
        return object.getClass().isAssignableFrom(tClass) ? (T) object : null;
    }

    public static <S, T> T object2Object(S s, Class<T> tClass) {
        T t = getInstance(tClass);
        BeanUtils.copyProperties(s, t);
        return t;
    }

    public static <S, T> List<T> objectList2ObjectList(List<S> objectList, Class<T> tClass) {
        return objectCollection2ObjectList(objectList, tClass);
    }

    public static <S, T> List<T> objectSet2ObjectList(Set<S> objectList, Class<T> tClass) {
        return objectCollection2ObjectList(objectList, tClass);
    }

    public static <T> T map2Object(Map map, Class<T> tClass) {
        T t = getInstance(tClass);

        for (Method method : tClass.getDeclaredMethods()) {
            String methodName = method.getName();
            if (isSetter(methodName, tClass)) {
                String fieldName = extractFieldNameAtMethodName(methodName, 3);
                map.computeIfPresent(fieldName, (key, value) -> {
                    try {
//                        System.out.println("INVOKE !! (METHOD, FIELD_NAME, FIELD_VALUE) : (" + methodName + ", " + fieldName + ", " + value + ")");
                        method.invoke(t, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return true;
                });
            }
        }

        return t;
    }

    public static <T> Map object2Map(T t) {
        return objectMapper.convertValue(t, Map.class);
    }

    private static <T> T getInstance(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Object : " + tClass.toString());
            e.printStackTrace();
        }
        return null;
    }

    private static <S, T> List<T> objectCollection2ObjectList(Collection<S> objectCollection, Class<T> tClass) {
        List<T> tList = new ArrayList<>();
        for (S s : objectCollection) {
            T t = getInstance(tClass);
            BeanUtils.copyProperties(s, t);
            tList.add(t);
        }

        return tList;
    }

    private static <T> boolean isSetter(String methodName, Class<T> tClass) {
        if (methodName.substring(0, 3).equals("set")) {
            String expectedFieldName = extractFieldNameAtMethodName(methodName, 3);

            for (Field field : tClass.getDeclaredFields()) {
                if (field.getName().equals(expectedFieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String extractFieldNameAtMethodName(String methodName, int keywordLength) {
        String fieldName = methodName.substring(keywordLength);
        return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
    }
}
