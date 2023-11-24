package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.Database.DbHelper;
import com.nhom6.noteapp.model.DTO.Category;
import com.nhom6.noteapp.model.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    DbHelper dbHelper ;
    SQLiteDatabase db ;
    public CategoryDAO(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Category category){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",category.getName());
        contentValues.put("des",category.getDes());
        contentValues.put("date",category.getDate());
        long res = db.insert("Categorys",null,contentValues);
        return  res;
    }

    public long update(Category category){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",category.getName());
        contentValues.put("des",category.getDes());
        contentValues.put("date",category.getDate());
        long res = db.update("Categorys",contentValues,"id=?",new String[]{category.getId()+""});
        return res ;
    }

    public int delete(int id){
        Cursor cursor = db.rawQuery("SELECT * FROM Tasks WHERE id_category = ?",new String[]{String.valueOf(id)});
        if (cursor.getCount()!=0){
            return -1 ;
        }
        long  check = db.delete("Categorys","id=?",new String[]{String.valueOf(id)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<Category> getAll(){
        String sql="SELECT * FROM Users";
        return (ArrayList<Category>) getData(sql);
    }
    public Category getID(String id){
        String sql = "SELECT * FROM Categorys WHERE id=?";
        List<Category> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<Category> getData(String sql, String...selectionArgs) {

        List<Category> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            Category obj = new Category();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id"))));
            obj.setName(c.getString(c.getColumnIndex("name")));
            obj.setDes(c.getString(c.getColumnIndex("des")));
            obj.setDate(c.getString(c.getColumnIndex("date")));

            list.add(obj);
        }
        return list;
    }

}
