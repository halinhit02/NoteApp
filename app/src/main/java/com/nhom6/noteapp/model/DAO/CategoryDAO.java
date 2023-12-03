package com.nhom6.noteapp.model.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nhom6.noteapp.database.DbHelper;
import com.nhom6.noteapp.model.DTO.Category;

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
        contentValues.put("name_category",category.getName());
        contentValues.put("des_category",category.getDes());
        contentValues.put("datecreate_category",category.getDate());
        contentValues.put("id_user",category.getId_user());
        long res = db.insert("Categorys",null,contentValues);
        return  res;
    }

    public long update(Category category){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name_category",category.getName());
        contentValues.put("des_category",category.getDes());
        contentValues.put("datecreate_category",category.getDate());
        contentValues.put("id_user",category.getId_user());
        long res = db.update("Categorys",contentValues,"id_category=?",new String[]{category.getId()+""});
        return res ;
    }

    public int delete(int id){

        long  check = db.delete("Categorys","id_category=?",new String[]{String.valueOf(id)});
        if(check==-1){
            return  0 ;
        }
        return 1 ;
    }

    public ArrayList<Category> getAll(){
        String sql="SELECT * FROM Categorys";
        return (ArrayList<Category>) getData(sql);
    }
    public Category getID(String id){
        String sql = "SELECT * FROM Categorys WHERE id_category=?";
        List<Category> list = getData(sql,id);
        return list.get(0);
    }

    @SuppressLint("Range")
    private List<Category> getData(String sql, String...selectionArgs) {

        List<Category> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            Category obj = new Category();
            obj.setId(Integer.parseInt(c.getString(c.getColumnIndex("id_category"))));
            obj.setName(c.getString(c.getColumnIndex("name_category")));
            obj.setDes(c.getString(c.getColumnIndex("des_category")));
            obj.setDate(c.getString(c.getColumnIndex("datecreate_category")));
            obj.setId_user(Integer.parseInt(c.getString(c.getColumnIndex("id_user"))));

            list.add(obj);
        }
        return list;
    }

}
