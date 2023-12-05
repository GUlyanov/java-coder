package ru.innotech.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AllInvocationHandler<T> implements InvocationHandler {

    private T obj;
    private Map<Method, Object> cache = new HashMap<>();
    private ResultType resultType;

    public AllInvocationHandler(T obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object rez;
        // Анализ аннотаций на вызванном методе
        if (method.isAnnotationPresent(Cache.class)) {
            // данный метод кешируется
            if (cache.containsKey(method)) {
                // метод уже вызывался ранее
                //System.out.println("значение взято из кэша");
                resultType = ResultType.FROM_CACHE;
                rez=cache.get(method);
            } else {
                // метод вызывается первый раз после очистки кэша - засунуть значение метода в кэш
                System.out.println("значение вычислено, помещено в кэш");
                rez = method.invoke(obj, args);
                resultType = ResultType.IN_CACHE;
                cache.put(method, rez);
            }
            return rez;
        }
        if (method.isAnnotationPresent(Setter.class)) {
            // данный метод не кешируется но изменяет состояние объекта
            cache.clear(); // очищаем кэш
            System.out.println("очистка кэша");
            resultType = ResultType.CLS_CACHE;
            rez = method.invoke(obj, args);
        } else {
            // данный метод не кешируется и не изменяет состояние объекта
            resultType = ResultType.NO_CACHE;
            rez = method.invoke(obj, args);
        }
        return rez;
    }

    public ResultType getRes() {
        return resultType;
    }
}
