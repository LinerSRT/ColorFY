package ru.liner.colorfy.utils;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class Reflect {

    @Nullable
    public static Field findField(Class<?> clazz, String fieldName){
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if(superClass != null)
                return findField(superClass, fieldName);
            return null;
        }
    }
    @Nullable
    public static Method findMethod(Class<?> clazz, String methodName, Class<?> ... params){
        try {
            Method method = clazz.getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if(superClass != null)
                return findMethod(superClass, methodName, params);
            return null;
        }
    }

    @Nullable
    public static Object getFieldValue(Object obj, String fieldName) {
        Object result = null;
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Nullable
    public static Object getFieldValue(Class<?> clazz, Object obj, String fieldName) {
        Object result = null;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Nullable
    public static Object getField(Object obj, String fieldName){
        Field field = searchFiled(obj.getClass(), fieldName);
        if(field != null){
            field.setAccessible(true);
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static Field searchFiled(Class<?> clazz, String fieldName) {
        if (clazz == null)
            return null;
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return searchFiled(clazz.getSuperclass(), fieldName);
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Class<?> clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Class<?> superClass;
            while ((superClass = clazz.getSuperclass()) != null) {
                try {
                    Field field = superClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(obj, value);
                    break;
                } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                    noSuchFieldException.printStackTrace();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object... values) {
        Class<?> clazz = obj.getClass();
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, values);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Class<?> superClass;
            while ((superClass = clazz.getSuperclass()) != null) {
                try {
                    Field field = superClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(obj, values);
                    break;
                } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
                    noSuchFieldException.printStackTrace();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static Method getMethod(Object obj, String methodName, Class<?>... params) {
        Class<?> clazz = obj.getClass();
        try {
            Method method = clazz.getMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Class<?> superClass;
            Method method = null;
            while ((superClass = clazz.getSuperclass()) != null) {
                try {
                    method = superClass.getMethod(methodName, params);
                    method.setAccessible(true);
                    break;
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                }
            }
            return method;
        }
    }

    @Nullable
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            Method method = clazz.getMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Class<?> superClass;
            Method method = null;
            while ((superClass = clazz.getSuperclass()) != null) {
                try {
                    method = superClass.getMethod(methodName, params);
                    method.setAccessible(true);
                    break;
                } catch (NoSuchMethodException e2) {
                    e2.printStackTrace();
                }
            }
            return method;
        }
    }

    @Nullable
    public static Method getMethod(Object object, String methodName) {
        return getMethod(object, methodName, new Class[]{});
    }


    public static Object invokeMethod(Object object, @Nullable Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (method != null) {
            method.setAccessible(true);
            return method.invoke(object, args);
        }
        return null;
    }

    public static Object invokeMethod(Object object, @Nullable Method method, Object args) throws InvocationTargetException, IllegalAccessException {
        return invokeMethod(object, method, new Object[]{args});
    }

    public static Object invokeMethod(Object object, @Nullable Method method) throws InvocationTargetException, IllegalAccessException {
        return invokeMethod(object, method, new Object[]{});
    }
}
