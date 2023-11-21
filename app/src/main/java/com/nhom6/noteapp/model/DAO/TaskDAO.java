package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.Database.DbHelper;
import com.nhom6.noteapp.model.DTO.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    DbHelper dbHelper ;
    SQLiteDatabase db ;
    public TaskDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put("tiitle",task.getTitle());
        contentValues.put("des",task.getDes());
        contentValues.put("time",task.getTime());
        contentValues.put("done",task.getDone());
        contentValues.put("score",task.getScore());
        long res = db.insert("Tasks",null,contentValues);
        return  res;
    }

    public long update(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put("tiitle",task.getTitle());
        contentValues.put("des",task.getDes());
        contentValues.put("time",task.getTime());
        contentValues.put("done",task.getDone());
        contentValues.put("score",task.getScore());
        long res = db.update("Tasks",contentValues,"id=?",new String[]{task.getId()+""});
        return res ;
    }

    public int delete(int id){
        long  check = db.delete("Tasks","id=?",new String[]{String.valueOf(id)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<Task> getAll(){
        String sql="SELECT * FROM Users";
        return (ArrayList<Task>) getData(sql);
    }
    public Task getID(String id){
        String sql = "SELECT * FROM Categorys WHERE id=?";
        List<Task> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<Task> getData(String sql, String...selectionArgs) {

        List<Task> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            Task obj = new Task();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
            obj.setTitle(c.getString(c.getColumnIndex("title")));
            obj.setDes(c.getString(c.getColumnIndex("des")));
            obj.setTime(c.getString(c.getColumnIndex("time")));
            obj.setDone(Integer.parseInt(c.getString(c.getColumnIndex("done"))));
            obj.setScore(c.getString(c.getColumnIndex("score")));
            list.add(obj);
        }
        return list;
    }

}
