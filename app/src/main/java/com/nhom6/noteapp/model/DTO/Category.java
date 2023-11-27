package com.nhom6.noteapp.model.DTO;

public class Category {
    private int id;
    private String name;
    private  String des;
    private String date;
   public  Category(){}

    public Category(int id, String name, String des, String date) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.date = date;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
