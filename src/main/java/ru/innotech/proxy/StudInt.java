package ru.innotech.proxy;

public interface StudInt {
    @Cache
    public String getName();

    @Setter
    public void setName(String name);

    @Cache
    public int getAge();

    @Setter
    public void setAge(int age);
}
