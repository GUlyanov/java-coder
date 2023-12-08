package ru.innotech.multi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class AllInvocationHandler<T> implements InvocationHandler {

    private final T obj;

    private final RezCache cache;


    public AllInvocationHandler(T obj, long objLifeTimeOut, long cacheClsRate) throws Exception {
        this.obj = obj;
        cache = new RezCache(objLifeTimeOut, cacheClsRate);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object rez;
        Method clMethod = getClMeth(method, obj); // по методу интерфейса найдем метод класса для obj

        // Анализ аннотаций на вызванном методе
        if (clMethod.isAnnotationPresent(Cache.class)) {
            // данный метод кешируется
            if (cache.containsKey(obj, method)) {
                // метод уже вызывался ранее
                rez = cache.get(obj, method);
            } else {
                // метод вызывается первый раз после очистки кэша - засунуть значение метода в кэш
                rez = method.invoke(obj, args);
                cache.put(obj, method, rez);
            }
            return rez;
        }
        // данный метод не кешируется
        rez = method.invoke(obj, args);
        return rez;
    }

    // по методу интерфейса вернуть аналогичный метод класса объекта obj
    public Method getClMeth(Method method, T obj) {
        Method rez;
        try {
            rez = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e){
            return null;
        }
        return rez;
    }

}
