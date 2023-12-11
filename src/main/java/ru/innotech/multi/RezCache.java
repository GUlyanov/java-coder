package ru.innotech.multi;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.*;

public class RezCache {
    private ConcurrentHashMap<Key, Object> globalMap = new ConcurrentHashMap<>();
    private long objLifeTimeOut; // время жизни значений в кэше

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread th = new Thread(r);
        th.setDaemon(true);
        return th;
    });

    public RezCache(long objLifeTimeOut, long cacheClsRate)  throws Exception {
        if (objLifeTimeOut < 100) {
            throw new Exception("Слишком короткий интервал для сохранения в кэше. Интервал следует сделать больше 10 ms");
        }
        this.objLifeTimeOut = objLifeTimeOut;
        scheduler.scheduleAtFixedRate(() -> {
            long current = System.currentTimeMillis();
            int n = 0;
            int m = globalMap.keySet().size();
            for (Key k : globalMap.keySet()) {
                if (!k.isLive(current)) {
                    n++;
                    globalMap.remove(k);
                }
            }
            System.out.println("Очистка кэша. Ключей: " + m +  ". Очищено ключей: " + n);
        }, 1, cacheClsRate, TimeUnit.MILLISECONDS);
    }


     // Метод для вставки обьекта в кеш
     // Время хранения берётся по умолчанию++
    public void put(Object obj, Method method, Object value) {
        globalMap.put(new Key(obj, method, objLifeTimeOut), value);
    }

     // получение значения по ключу
    public Object get(Object obj, Method method) {
        return globalMap.get(new Key(obj, method));
    }

    // Метод проверки существования ключа
    public boolean containsKey(Object obj, Method method) {
        return globalMap.containsKey(new Key(obj, method));
    }

    //--- Внутренний класс ключа для Hashmap -----------------------------
    private static class Key {

        private final Method method;
        private final String state;
        private final long timelife;

        public Key(Object obj, Method method, long timeout) {
            this.method = method;
            this.state = obj.toString();
            this.timelife = System.currentTimeMillis() + timeout;
        }

        public Key(Object obj, Method method) {
            this(obj, method, 0);
        }

        public Method getMethod() {
            return method;
        }

        public String getState() {
            return state;
        }

        public boolean isLive(long currentTimeMillis) {
            return currentTimeMillis < timelife;
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


}
