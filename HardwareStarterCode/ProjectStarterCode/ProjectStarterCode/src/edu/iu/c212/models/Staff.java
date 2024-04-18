package edu.iu.c212.models;

public class Staff {
    private String fullName;
    private int age;
    //Their role as a full word or phrase, e.g., "Manager" instead of "M"
    private String role;
    private String availability;
    public Staff(String name, int age, String role, String av){
    this.fullName = name;
    this.age = age;
    this.role = role;
    this.availability = av;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    public String getAvailability() {
        return availability;
    }
}
