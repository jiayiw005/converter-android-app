package com.example.cvt;



public class User {

    static boolean loginStatus = false;
    static int id = 000000;
    static String username = "username";
    static String password;

    public User(String name, String pwd) {
        super();
        username = name;
        password = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean status) {
        this.loginStatus = status;
    }

    @Override
    public String toString() {
        return "User{id =" + id + ", name = " + username + ",password =" + password + "}";
    }

}
