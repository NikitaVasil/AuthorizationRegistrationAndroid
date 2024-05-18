package com.example.messengerproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "messenger", factory, 1) {
        val table_Name = "users"
        val login = "login"
        val mail = "email"
        val password = "password"
    override fun onCreate(db: SQLiteDatabase?) {
        val user = "CREATE TABLE $table_Name (UserID INT PRIMARY KEY, $login TEXT, $mail TEXT, $password TEXT)"
        db!!.execSQL(user)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE $table_Name")
        onCreate(db)
    }

    fun addUsers(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("password", user.password)

        val db = this.writableDatabase
        db.insert(table_Name, null, values)

        db.close()
    }

    @SuppressLint("Recycle")
    fun getUser(login: String, password: String) : Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM $table_Name WHERE login = '$login' AND password = '$password'", null)

        return result.moveToFirst()
    }

    @SuppressLint("Range")
    fun getUserName(): Array<String> {
        val db = this.readableDatabase

        val select = db.rawQuery("SELECT * FROM $table_Name", null)
        //select.moveToFirst()
        val users = arrayOf<String>()
        if (select != null) {
            if (select.moveToFirst()) {
                do {
                    val login = select.getString(select.getColumnIndex("login"))

                    users.plus(login)
                } while (select.moveToNext())
            }
        }
        select.close()
        db.close()
        return users
    }

}