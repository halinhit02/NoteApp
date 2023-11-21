package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.Database.DbHelper;
import com.nhom6.noteapp.model.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    DbHelper dbHelper ;
    SQLiteDatabase db ;
    public UserDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.getName());
        contentValues.put("userName",user.getUserName());
        contentValues.put("password",user.getPassword());
        long res = db.insert("Users",null,contentValues);
    return  res;
    }

    public long update(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user.getName());
        contentValues.put("userName",user.getUserName());
        contentValues.put("password",user.getPassword());
        long res = db.update("Users",contentValues,"id=?",new String[]{user.getId()+""});
        return res ;
    }

    public ArrayList<User> getAll(){
        String sql="SELECT * FROM Users";
        return (ArrayList<User>) getData(sql);
    }
    public User getID(String id){
        String sql = "SELECT * FROM Users WHERE id=?";
        List<User> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<User> getData(String sql, String...selectionArgs) {

        List<User> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            User obj = new User();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
            obj.setName(c.getString(c.getColumnIndex("name")));
            obj.setUserName(c.getString(c.getColumnIndex("userName")));
            obj.setPassword(c.getString(c.getColumnIndex("password")));

            list.add(obj);
        }
        return list;
    }
}
