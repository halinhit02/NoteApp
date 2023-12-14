package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.model.DbHelper;
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

        contentValues.put("name_user",user.getName());
        contentValues.put("username_user",user.getUserName());
        contentValues.put("password_user",user.getPassword());
        long res = db.insert("Users",null,contentValues);
    return  res;
    }

    public long update(User user){
        ContentValues contentValues = new ContentValues();

        contentValues.put("name_user",user.getName());
        contentValues.put("username_user",user.getUserName());
        contentValues.put("password_user",user.getPassword());
        long res = db.update("Users",contentValues,"id_user=?",new String[]{user.getId()+""});
        return res ;
    }

    public ArrayList<User> getAll(){
        String sql="SELECT * FROM Users";
        return (ArrayList<User>) getData(sql);
    }

    public User getUserByID(String id){
        String sql = "SELECT * FROM Users WHERE id_user=?";
        List<User> list = getData(sql,id);
        return list.get(0);
    }

    public User getUserByUsername(String username){
        String sql = "SELECT * FROM Users WHERE username_user=?";
        List<User> list = getData(sql,username);
        if(list.size()!= 0)
            return list.get(0);
        else
            return new User(-1,"","","");
    }
    @SuppressLint("Range")
    private List<User> getData(String sql, String...selectionArgs) {

        List<User> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            User obj = new User();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id_user"))));
            obj.setName(c.getString(c.getColumnIndex("name_user")));
            obj.setUserName(c.getString(c.getColumnIndex("username_user")));
            obj.setPassword(c.getString(c.getColumnIndex("password_user")));

            list.add(obj);
        }
        return list;
    }
}
