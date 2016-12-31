package me.evrooij.data;

import com.google.gson.annotations.Expose;

/**
 * @author eddy on 31-12-16.
 */
public class Student {
    @Expose
    private String name;
    @Expose
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
