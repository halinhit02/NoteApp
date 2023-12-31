package com.nhom6.noteapp.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nhom6.noteapp.model.DbHelper;
import com.nhom6.noteapp.model.dto.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    DbHelper dbHelper;
    SQLiteDatabase db;

    public TaskDAO(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title_task", task.getTitle());
        contentValues.put("des_task", task.getDes());
        contentValues.put("note_task", task.getNote());
        contentValues.put("time_task", task.getTime());
        contentValues.put("date_task", task.getDate());
        contentValues.put("done_task", task.getDone());
        contentValues.put("notified_task", task.getNotified());
        contentValues.put("score_task", task.getScore());
        contentValues.put("id_category", task.getId_category());
        long res = db.insert("Tasks", null, contentValues);
        return res;
    }

    public long update(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title_task", task.getTitle());
        contentValues.put("des_task", task.getDes());
        contentValues.put("note_task", task.getNote());
        contentValues.put("time_task", task.getTime());
        contentValues.put("date_task", task.getDate());
        contentValues.put("done_task", task.getDone());
        contentValues.put("notified_task", task.getNotified());
        contentValues.put("score_task", task.getScore());
        contentValues.put("id_category", task.getId_category());
        long res = db.update("Tasks", contentValues, "id_task=?", new String[]{task.getId() + ""});
        return res;
    }

    public int delete(int id) {
        long check = db.delete("Tasks", "id_task=?", new String[]{String.valueOf(id)});
        if (check == -1) {
            return 0;
        }
        return 1;
    }

    public ArrayList<Task> getAll() {
        ArrayList<Task> result = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Tasks";
            result = (ArrayList<Task>) getDaTa(sql);
        } catch (Throwable e) {
            Log.e("DBHelper", "isAnyInfoAvailable(): Caught - " + e.getClass().getName(), e);
        }
        return result;

    }

    public ArrayList<Task> getAllByCategory(String id_category) {
        String sql = "SELECT * FROM Tasks WHERE id_category=?";
        return (ArrayList<Task>) getDaTa(sql, id_category);
    }

    public Task getID(String id) {
        String sql = "SELECT * FROM Tasks WHERE id_task=?";
        List<Task> list = getDaTa(sql, id);
        return list.get(0);
    }

    public List<Task> getDaTa(String sql, String... selectionArgs) {
        List<Task> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int a = c.getInt(0);
                String b = c.getString(1);
                String d = c.getString(2);
                String e = c.getString(3);
                String f = c.getString(4);
                String g = c.getString(5);
                int h = c.getInt(6);
                int i = c.getInt(7);
                String k = c.getString(8);
                int l = c.getInt(9);
                list.add(new Task(a, b, d, e, f, g, h, i, k, l));
                c.moveToNext();
            }
            c.close();
        }
        return list;
    }

}
