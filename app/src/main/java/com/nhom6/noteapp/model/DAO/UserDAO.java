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
<<<<<<< HEAD
        contentValues.put("name",user.getName());
        contentValues.put("userName",user.getUserName());
        contentValues.put("password",user.getPassword());
=======
        contentValues.put("name_user",user.getName());
        contentValues.put("username_user",user.getUserName());
        contentValues.put("password_user",user.getPassword());
>>>>>>> origin/develop/ui
        long res = db.insert("Users",null,contentValues);
    return  res;
    }

    public long update(User user){
        ContentValues contentValues = new ContentValues();
<<<<<<< HEAD
        contentValues.put("name",user.getName());
        contentValues.put("userName",user.getUserName());
        contentValues.put("password",user.getPassword());
        long res = db.update("Users",contentValues,"id=?",new String[]{user.getId()+""});
=======
        contentValues.put("name_user",user.getName());
        contentValues.put("username_user",user.getUserName());
        contentValues.put("password_user",user.getPassword());
        long res = db.update("Users",contentValues,"id_user=?",new String[]{user.getId()+""});
>>>>>>> origin/develop/ui
        return res ;
    }

    public ArrayList<User> getAll(){
        String sql="SELECT * FROM Users";
        return (ArrayList<User>) getData(sql);
    }
<<<<<<< HEAD
    public User getID(String id){
        String sql = "SELECT * FROM Users WHERE id=?";
=======
    public User getUserByID(String id){
        String sql = "SELECT * FROM Users WHERE id_user=?";
>>>>>>> origin/develop/ui
        List<User> list = getData(sql,id);
        return list.get(0);
    }

<<<<<<< HEAD
=======
    public List<User> getUserByUsername(String username){
        String sql = "SELECT * FROM Users WHERE username_user=?";
        List<User> list = getData(sql,username);
        return list;
    }

>>>>>>> origin/develop/ui
    @SuppressLint("Range")
    private List<User> getData(String sql, String...selectionArgs) {

        List<User> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            User obj = new User();
<<<<<<< HEAD
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
            obj.setName(c.getString(c.getColumnIndex("name")));
            obj.setUserName(c.getString(c.getColumnIndex("userName")));
            obj.setPassword(c.getString(c.getColumnIndex("password")));
=======
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id_user"))));
            obj.setName(c.getString(c.getColumnIndex("name_user")));
            obj.setUserName(c.getString(c.getColumnIndex("username_user")));
            obj.setPassword(c.getString(c.getColumnIndex("password_user")));
>>>>>>> origin/develop/ui

            list.add(obj);
        }
        return list;
    }
}
