package com.boliviawebdesign.projecttaskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abel on 22-03-16.
 */

public class PostsDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "postsDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_USERS = "users";

    // Post Table Columns
    private static final String KEY_POST_ID = "id";
    private static final String KEY_POST_USER_ID_FK = "userId";
    private static final String KEY_POST_TEXT = "text";

    // User Table Columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PROFILE_PICTURE_URL = "profilePictureUrl";


    private String MSG_TAG = "DB";


    private static PostsDatabaseHelper sInstance;


    public static synchronized PostsDatabaseHelper getInstance(ContextWrapper context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new PostsDatabaseHelper(context);
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private PostsDatabaseHelper(ContextWrapper context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {




        try {

            db.execSQL("CREATE TABLE IF NOT EXISTS TableName (Field1 VARCHAR, Field2 INT(3));");

            db.execSQL("CREATE TABLE IF NOT EXISTS posts (id_post INTEGER PRIMARY KEY   AUTOINCREMENT, " +
                    "selectable_post_type_id INT, "+
                    "parent_id INT, "+
                    "project_id INT, "+
                    "forum_id INT, "+
                    "post_title TEXT  NOT NULL, " +
                    "post_content TEXT  NOT NULL);");

            db.execSQL("CREATE TABLE IF NOT EXISTS selectables (id_selectable INTEGER PRIMARY KEY   AUTOINCREMENT, " +
                    "selectable_name INT, "+
                    "selectable_vale INT, "+
                    "selectable_table TEXT  NOT NULL);");



            ContentValues selectableContentValues1 = new ContentValues();
            selectableContentValues1.put("selectable_name", "post_type");
            selectableContentValues1.put("selectable_value", "project");
            selectableContentValues1.put("selectable_table", "posts");
            db.insert("selectables", null, selectableContentValues1);

            ContentValues selectableContentValues2 = new ContentValues();
            selectableContentValues2.put("selectable_name","post_type");
            selectableContentValues2.put("selectable_value","forum");
            selectableContentValues2.put("selectable_table","posts");
            db.insert("selectables", null, selectableContentValues2);

            ContentValues selectableContentValues3 = new ContentValues();
            selectableContentValues3.put("selectable_name","post_type");
            selectableContentValues3.put("selectable_value","comment");
            selectableContentValues3.put("selectable_table","posts");
            db.insert("selectables", null, selectableContentValues3);

        }
        catch (Exception ex) {
        }
    }
    //###############pending test #######
    public long getSelectableId(SQLiteDatabase db){
        ContentValues selectableContentValues1 = new ContentValues();
        selectableContentValues1.put("selectable_name", "post_type");
        selectableContentValues1.put("selectable_value", "project");
        selectableContentValues1.put("selectable_table", "posts");
        return db.insert("selectables", null, selectableContentValues1);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }




    public ArrayList getQuery(String sql){

        SQLiteDatabase db = getReadableDatabase();
        //SQLiteDatabase db = getWritableDatabase();

           /*retrieve data from database */
        Cursor c = db.rawQuery(sql, null);

        String columnsNames[] = c.getColumnNames();

        ArrayList<HashMap> rows = new ArrayList<HashMap>();


        // Check if our result was valid.
        c.moveToFirst();
        if (c != null) {
            // Loop through all Results
            do {
                HashMap row = new HashMap();
                for(int i=0; i<columnsNames.length;i++){
                    row.put(columnsNames[i], c.getString(c.getColumnIndex(columnsNames[i])) );
                }
                rows.add(row);

            }while(c.moveToNext());
        }
        return rows;
    }


    public long insert(String tableName, ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(tableName, null, contentValues);
    }


    public long addPost(ContentValues contentValues, String postType){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues SelectableContentValues = new ContentValues();

        db.beginTransaction();

        long postId = - 1;

        try{

            long id_selectable = db.insert("selectables", null, SelectableContentValues);
            contentValues.put("selectable_post_type_id", id_selectable);

            postId = db.insert("posts", null, contentValues);

            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(MSG_TAG, "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }

        return postId;
    }



    public String getTestInsert(){

        ArrayList<HashMap> rows= getQuery("SELECT * FROM posts");


        String msg="";

        for(int i=0; i<rows.size(); i++ ){

            //msg += rows.get(i).get("Field1")+" "+rows.get(i).get("Field2")+"\n";

            msg += rows.get(i).get("id_post")+" | "+rows.get(i).get("project_id")+" | "+rows.get(i).get("post_title")+" | "+rows.get(i).get("post_content")+"\n";


        }
        return msg;

    }
}