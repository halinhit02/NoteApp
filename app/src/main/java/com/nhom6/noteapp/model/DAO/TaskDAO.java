package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.database.DbHelper;
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
        contentValues.put("title_task",task.getTitle());
        contentValues.put("des_task",task.getDes());
        contentValues.put("note_task",task.getDes());
        contentValues.put("time_task",task.getTime());
        contentValues.put("date_task",task.getTime());
        contentValues.put("done_task",task.getDone());
        contentValues.put("score_task",task.getScore());
        long res = db.insert("Tasks",null,contentValues);
        return  res;
    }

    public long update(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title_task",task.getTitle());
        contentValues.put("des_task",task.getDes());
        contentValues.put("note_task",task.getDes());
        contentValues.put("time_task",task.getTime());
        contentValues.put("date_task",task.getTime());
        contentValues.put("done_task",task.getDone());
        contentValues.put("score_task",task.getScore());
        contentValues.put("id_category",task.getId_category());
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
        String sql="SELECT * FROM Tasks";
        return (ArrayList<Task>) getData(sql);
    }
    public Task getID(String id){
        String sql = "SELECT * FROM Tasks WHERE id=?";
        List<Task> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<Task> getData(String sql, String...selectionArgs) {

        List<Task> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            Task obj = new Task();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id_task"))));
            obj.setTitle(c.getString(c.getColumnIndex("title_task")));
            obj.setDes(c.getString(c.getColumnIndex("des_task")));
            obj.setNote(c.getString(c.getColumnIndex("note_task")));
            obj.setTime(c.getString(c.getColumnIndex("time_task")));
            obj.setDate(c.getString(c.getColumnIndex("date_task")));
            obj.setDone(Integer.parseInt(c.getString(c.getColumnIndex("done_task"))));
            obj.setScore(c.getString(c.getColumnIndex("score_task")));
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id_category"))));
            list.add(obj);
        }
        return list;
    }

}
