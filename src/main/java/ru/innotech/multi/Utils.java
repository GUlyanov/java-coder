package ru.innotech.multi;

import java.lang.reflect.Proxy;

public class Utils {
    public static <T> T cache(T obj, long objLifeTimeOut, long cacheClsRate) throws Exception {
        ClassLoader objClassLoader = obj.getClass().getClassLoader();
        //Получаем все интерфейсы, которые реализует оригинальный объект
        Class[] interfaces = obj.getClass().getInterfaces();

        //Создаем прокси нашего объекта obj
        T proxyObj = (T) Proxy.newProxyInstance(objClassLoader, interfaces, new AllInvocationHandler(obj, objLifeTimeOut, cacheClsRate));
        return proxyObj;
    }

}
