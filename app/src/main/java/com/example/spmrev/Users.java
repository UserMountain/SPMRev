package com.example.spmrev;

public class Users {
    String firstName,firstName1, lastName, age, userName;
    public Users() {
    }
    public Users(String firstName,String firstName1, String lastName, String age, String userName) {
        this.firstName = firstName;
        this.firstName1 = firstName1;
        this.lastName = lastName;
        this.age = age;
        this.userName = userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getFirstName1() {
        return firstName1;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
