package ru.innotech.multi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class AllInvocationHandler<T> implements InvocationHandler {

    private final T obj;

    private final RezCache1 cache;

    public AllInvocationHandler(T obj, long cacheClsRate) throws Exception {
        this.obj = obj;
        cache = new RezCache1(cacheClsRate);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object rez;
        Method clMethod = getClMeth(method, obj); // по методу интерфейса найдем метод класса для obj

        // Анализ аннотаций на вызванном методе
        if (clMethod.isAnnotationPresent(Cache.class)) {
            // данный метод кешируется
            if (cache.containsKey(obj, clMethod)) {
                // метод уже вызывался ранее - вернуть значение из кэша и обновить срок годности ключа
                //rez = cache.get(obj, clMethod);
                rez = cache.getAndUpdate(obj, clMethod);
                Utils.resTp = ResultType.FROM_CACHE;
            } else {
                // метод вызывается первый раз для такого значения ключа - засунуть значение метода в кэш
                rez = clMethod.invoke(obj, args);
                cache.put(obj, clMethod, rez);
                Utils.resTp = ResultType.IN_CACHE;
            }
            Utils.mapCnt = cache.MapCnt();
            return rez;
        }
        // данный метод не кешируется
        rez = clMethod.invoke(obj, args);
        Utils.resTp = ResultType.NO_CACHE;
        Utils.mapCnt = cache.MapCnt();
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
