package com.cybene.cyposdashboard.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class Db extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "cypos.db";
    private static final String SQL_CREATE_CONFIG_TABLE =  "CREATE TABLE tbl_config (" +
            "id INTEGER PRIMARY KEY NOT NULL," +
            "path varchar(100) NOT NULL);";

    private static final String SQL_CREATE_USERS_TABLE =  "CREATE TABLE tbl_users (" +
            "id INTEGER PRIMARY KEY NOT NULL," +
            "name varchar(100) NOT NULL," +
            "email varchar(65) NOT NULL," +
            "has_pin INTEGER DEFAULT 0," +
            "pin_hash TEXT);";

    private static final String SQL_CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE tbl_notifications (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "server_id TEXT," +
            "title TEXT," +
            "message TEXT," +
            "type TEXT," +
            "is_read INTEGER DEFAULT 0," +
            "is_archived INTEGER DEFAULT 0," +
            "created_at TEXT," +
            "payload TEXT);";

    private static final String SQL_DELETE_CONFIG_TABLE = "DROP TABLE IF EXISTS tbl_config";
    private static final String SQL_DELETE_USERS_TABLE = "DROP TABLE IF EXISTS tbl_users";
    private static final String SQL_DELETE_NOTIFICATIONS_TABLE = "DROP TABLE IF EXISTS tbl_notifications";

    public Db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONFIG_TABLE);
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(SQL_DELETE_CONFIG_TABLE);
            db.execSQL(SQL_DELETE_USERS_TABLE);
            db.execSQL(SQL_CREATE_CONFIG_TABLE);
            db.execSQL(SQL_CREATE_USERS_TABLE);
            db.execSQL(SQL_CREATE_NOTIFICATIONS_TABLE);
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE tbl_users ADD COLUMN has_pin INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE tbl_users ADD COLUMN pin_hash TEXT");
        }
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public boolean storeConfig(String path){
        boolean added;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("path",path);
        long result = db.insert("tbl_config",null,contentValue);
        added= result != -1;
        return added;
    }
    public boolean storeUser(String uid, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put("id", uid);
        contentValue.put("name", name);
        contentValue.put("email", email);
        long result = db.insertWithOnConflict("tbl_users",null,
                contentValue, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }
    public HashMap<String, String> getConfig() {
        HashMap<String, String> path = new HashMap<>();
        String selectQuery = "SELECT * from tbl_config";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            path.put("url", cursor.getString(1));
        }
        cursor.close();
        db.close();
        return path;
    }
    public Cursor getUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * from tbl_users",null);
    }
    public void deleteConfig(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_config");
    }
    public void deleteUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from tbl_users");
    }

    public boolean updateConfig(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if ("url".equals(key)) {
            values.put("path", value);
        } else {
            values.put(key, value);
        }
        int rows = db.update("tbl_config", values, null, null);
        if (rows == 0) {
            return storeConfig(value);
        }
        return rows > 0;
    }

    // Notification CRUD methods
    public boolean insertNotification(String serverId, String title, String message, String type, String createdAt, String payload) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("server_id", serverId);
        values.put("title", title);
        values.put("message", message);
        values.put("type", type);
        values.put("created_at", createdAt);
        values.put("payload", payload);
        long result = db.insert("tbl_notifications", null, values);
        return result != -1;
    }

    public Cursor getAllNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM tbl_notifications WHERE is_archived = 0 ORDER BY created_at DESC", null);
    }

    public int getUnreadCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM tbl_notifications WHERE is_read = 0 AND is_archived = 0", null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        if (cursor != null) cursor.close();
        return count;
    }

    public boolean markAsRead(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_read", 1);
        return db.update("tbl_notifications", values, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean archiveNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_archived", 1);
        return db.update("tbl_notifications", values, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteNotification(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("tbl_notifications", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public String getUserPin(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pin_hash FROM tbl_users WHERE id = ?", new String[]{userId});
        String pin = null;
        if (cursor != null && cursor.moveToFirst()) {
            pin = cursor.getString(0);
        }
        if (cursor != null) cursor.close();
        return pin;
    }

    public boolean updateUserPin(String userId, String hash) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pin_hash", hash);
        values.put("has_pin", 1);
        return db.update("tbl_users", values, "id = ?", new String[]{userId}) > 0;
    }

    public boolean hasUserPin(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT has_pin FROM tbl_users WHERE id = ?", new String[]{userId});
        boolean hasPin = false;
        if (cursor != null && cursor.moveToFirst()) {
            hasPin = cursor.getInt(0) == 1;
        }
        if (cursor != null) cursor.close();
        return hasPin;
    }
}
