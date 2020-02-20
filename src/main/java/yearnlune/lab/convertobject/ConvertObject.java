package yearnlune.lab.convertobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
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

    private static <T> T getInstance(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("Object : " + tClass.toString());
            e.printStackTrace();
        }
        return null;
    }

    private static <T,S> List<T> objectCollection2ObjectList(Collection<S> objectCollection, Class<T> tClass) {
        List<T> tList = new ArrayList<>();
        for (S s : objectCollection) {
            T t = getInstance(tClass);
            BeanUtils.copyProperties(s, t);
            tList.add(t);
        }

        return tList;
    }

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

    public static <T> List<T> stringToObjectList(String string, Class<T[]> tClass) {
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
        return object.getClass().isAssignableFrom(tClass) ? (T) object : null ;
    }

    public static <S, T> T object2Object(S s, Class<T> tClass) {
        T t = getInstance(tClass);
        BeanUtils.copyProperties(s, t);
        return t;
    }

    public static <T, S> List<T> objectListToObjectList(List<S> objectList, Class<T> tClass) {
        return objectCollection2ObjectList(objectList, tClass);
    }

    public static <T, S> List<T> objectSetToObjectList(Set<S> objectList, Class<T> tClass) {
        return objectCollection2ObjectList(objectList, tClass);
    }


}
