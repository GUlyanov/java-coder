package ru.innotech.proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Utils {
    public static <T> T cache(T obj) throws ClassNotFoundException {
        ClassLoader objClassLoader = obj.getClass().getClassLoader();
        //Получаем все интерфейсы, которые реализует оригинальный объект
        Class[] interfaces = obj.getClass().getInterfaces();
        interfaces = Arrays.copyOf(interfaces, interfaces.length+1);
        interfaces[interfaces.length-1] = Class.forName("StudIntExt");

        //Создаем прокси нашего объекта obj
        T proxyObj = (T) Proxy.newProxyInstance(objClassLoader, interfaces, new AllInvocationHandler(obj));
        return proxyObj;
    }

}
