package ru.innotech.multi;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.*;

// вариант организации кэша № 2 - кэш на основе ConcurrentHashMap и очистка реальная (не подключен)
public class RezCache {
    private ConcurrentHashMap<Key, Value> globalMap = new ConcurrentHashMap<>();

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread th = new Thread(r);
        th.setDaemon(true);
        return th;
    });

    public RezCache(long cacheClsRate) {
        if (cacheClsRate==0) return; // очистка кеша отключена
        scheduler.scheduleAtFixedRate(() -> {
            long current = System.currentTimeMillis();
            int n = 0;
            int m = globalMap.keySet().size();
            for (Key k : globalMap.keySet()) {
                if (!globalMap.get(k).isLive(current)) {
                    if (globalMap.remove(k)!=null) n++;
                }
            }
            if (n>0)
                System.out.println("Очистка кэша. Очищено ключей: " + n);
        }, 1, cacheClsRate, TimeUnit.MILLISECONDS);
    }


    // Метод для вставки обьекта в кеш
    // Время хранения берётся из аннотации на методе
    public void put(Object obj, Method method, Object val) {
        Cache anno = method.getAnnotation(Cache.class);
        Key key = new Key(obj, method);
        Value value = new Value(val, anno.value());
        globalMap.put(key, value);
    }

    // получение значения по ключу
    public Object get(Object obj, Method method) {
        return globalMap.get(new Key(obj, method)).val;
    }

    // получить значение Обновить ключ(продлить срок годности)
    public Object getAndUpdate(Object obj, Method method){
        Value value = globalMap.get(new Key(obj, method));
        Cache anno = method.getAnnotation(Cache.class);
        value.refreshTimelife(anno.value());
        return value.val;
    }

    // Метод проверки существования ключа
    public boolean containsKey(Object obj, Method method) {
        return globalMap.containsKey(new Key(obj, method));
    }

    // Количество записей в кеше
    public int MapCnt(){ return globalMap.size(); }

    //----------------------- Внутренний класс ключа для Hashmap -----------------------------
    private static class Key {

        private final Method method;
        private final String state;

        public Key(Object obj, Method method) {
            this.method = method;
            this.state = obj.toString();
        }

        public Method getMethod() {
            return method;
        }

        public String getState() {
            return state;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (
                    (this.method != other.method && (this.method == null || !this.method.equals(other.method))) &&
                            (this.state != other.state && (this.state == null || !this.state.equals(other.state)))
            ) { return false; }
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getMethod(), getState());
        }

        @Override
        public String toString() {
            return "Key{" +
                    "method=" + method +
                    ", state='" + state + '\'' +
                    '}';
        }
    }


    //----------------------- Внутренний класс значение для Hashmap -----------------------------
    private static class Value {
        private Object val;
        private long timelife;

        public Value(Object val, long timeout) {
            this.val = val;
            this.timelife = System.currentTimeMillis() + timeout;
        }

        public boolean isLive(long currentTimeMillis) {
            return currentTimeMillis < timelife;
        }

        public void refreshTimelife(long timeout){
            this.timelife = System.currentTimeMillis() + timeout;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Value value)) return false;
            return timelife == value.timelife && Objects.equals(val, value.val);
        }

        @Override
        public int hashCode() {
            return Objects.hash(val, timelife);
        }
    }


}
