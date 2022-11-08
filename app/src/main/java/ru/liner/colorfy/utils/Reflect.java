package ru.liner.colorfy.utils;

import androidx.annotation.NonNull;
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
    public static Object getFieldSafety(@NonNull Object o, String filedName) {
        Field field = findField(o.getClass(), filedName);
        if (field != null) {
            try {
                return field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean setFieldSafety(@NonNull Object o, String filedName, @Nullable Object value) {
        Field field = findField(o.getClass(), filedName);
        if (field != null) {
            try {
                field.set(o, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Nullable
    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null)
                return findField(superClass, fieldName);
            return null;
        }
    }

    @Nullable
    public static Object invokeSafety(@Nullable Object o, String methodName, Class<?>[] params, Object... objects) {
        if(o == null)
            return null;
        Method method = findMethod(o.getClass(), methodName, params);
        if(method != null){
            try {
                return method.invoke(o, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null)
                return findMethod(superClass, methodName, params);
            return null;
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
