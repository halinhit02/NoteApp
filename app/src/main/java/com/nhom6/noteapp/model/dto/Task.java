package com.nhom6.noteapp.model.dto;

import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title;
    private String des;
    private String note;
    private String time;
    private String date;
    private int done;
    private String score;
    private int id_category;

    public Task() {
    }

    public Task(int id, String title, String des, String time, int done, String score) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.time = time;
        this.done = done;
        this.score = score;
    }

    public Task(int id, String title, String des, String note, String time, String date, int done, String score, int id_category) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.note = note;
        this.time = time;
        this.date = date;
        this.done = done;
        this.score = score;
        this.id_category = id_category;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
