package com.nhom6.noteapp.model.DTO;

public class User {
    private int id;
    private  String name;
    private String userName;
    private String password;

    public User() {
    }

<<<<<<< HEAD
    public User(int id, String name, String userName, String password) {
        this.id = id;
=======
    public User(String name, String userName, String password) {
>>>>>>> origin/develop/ui
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
