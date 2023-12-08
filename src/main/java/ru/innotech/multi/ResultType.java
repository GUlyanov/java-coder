package ru.innotech.multi;

public enum ResultType {
    FROM_CACHE("значение взято из кэша"),
    IN_CACHE("значение вычислено, помещено в кэш"),
    CLS_CACHE("очистка кэша"),
    NO_CACHE("метод не кешируется");

    String name;

    ResultType(String name) {
        this.name = name;
    }
}
