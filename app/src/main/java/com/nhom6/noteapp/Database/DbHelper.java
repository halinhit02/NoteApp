package com.nhom6.noteapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    static final String dbName = "Noteapp";
    static final int dbVersion = 1;

    public DbHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser = "create table Users(" +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name_user TEXT NOT NULL," +
                "username_user TEXT NOT NULL," +
<<<<<<< HEAD
                "password _userTEXT NOT NULL)" ;
=======
                "password_user TEXT NOT NULL)" ;
>>>>>>> origin/develop/ui
        db.execSQL(createTableUser);

        String createTableCategory = "create table Categorys(" +
                "id_category INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name_category TEXT NOT NULL," +
                "des_category TEXT NOT NULL," +
                "datecreate_category DATE NOT NULL," +
                "id_user INTEGER  REFERENCES Users(id_user))" ;
        db.execSQL(createTableCategory);

        String createTableTask = "create table Tasks(" +
                "id_task INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title_task TEXT NOT NULL," +
                "des_task TEXT NOT NULL," +
                "time_task TEXT NOT NULL," +
                "done_task INTEGER NOT NULL," +
                "score_task TEXT NOT NULL," +
                "id_category INTEGER  REFERENCES Categorys(id_category))" ;
        db.execSQL(createTableTask);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Categorys");
        db.execSQL("DROP TABLE IF EXISTS Tasks");
<<<<<<< HEAD
=======
        onCreate(db);
>>>>>>> origin/develop/ui
    }
}