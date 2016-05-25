package com.boliviawebdesign.projecttaskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abel on 21-03-16.
 */
public class DatabaseManager{

    SQLiteDatabase myDB;
    ContextWrapper context;


    public DatabaseManager(ContextWrapper contextParam){
        context = contextParam;
        try {
            myDB = context.openOrCreateDatabase("projecttaskmanager", context.MODE_PRIVATE, null);
            myDB.execSQL("CREATE TABLE IF NOT EXISTS TableName (Field1 VARCHAR, Field2 INT(3));");

            myDB.execSQL("CREATE TABLE IF NOT EXISTS posts (id_post INTEGER PRIMARY KEY   AUTOINCREMENT, " +
                                                            "selectable_post_type_id INT, "+
                                                            "parent_id INT, "+
                                                            "project_id INT, "+
                                                            "forum_id INT, "+
                                                            "post_title TEXT  NOT NULL, " +
                                                            "post_content TEXT  NOT NULL);");

            myDB.execSQL("CREATE TABLE IF NOT EXISTS selectables (id_selectable INTEGER PRIMARY KEY   AUTOINCREMENT, " +
                    "selectable_name INT, "+
                    "selectable_vale INT, "+
                    "selectable_table TEXT  NOT NULL);");

        }
        catch (Exception ex) {
        }
    }

    /*public DatabaseManager(Context context) {
        super(context, name, factory, version, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/

    public ArrayList getQuery(String sql){

           /*retrieve data from database */
        Cursor c = myDB.rawQuery(sql, null);

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
         /* Insert data to a Table*/
        //myDB.execSQL("INSERT INTO  TableName (Field1, Field2) VALUES ('Saranga', 22);");
        return myDB.insert(tableName, null, contentValues);
    }
    public String getTestInsert(){
        /*myDB.execSQL("INSERT INTO  TableName (Field1, Field2) VALUES ('Saranga', 22);");
        ArrayList<HashMap> rows= getQuery("SELECT * FROM TableName");
*/

        //myDB.execSQL("INSERT INTO  posts (post_title, post_content) VALUES ('forum 1', 'LOREM IPSUM');");

       /* ContentValues values = new ContentValues();
        values.put("post_title", "forum 2");
        values.put("post_content", "LOREM IPSUM 2");

        insert("posts", values);*/

        ArrayList<HashMap> rows= getQuery("SELECT * FROM posts");


        String msg="";

        for(int i=0; i<rows.size(); i++ ){

            //msg += rows.get(i).get("Field1")+" "+rows.get(i).get("Field2")+"\n";

            msg += rows.get(i).get("id_post")+" "+rows.get(i).get("project_id")+" "+rows.get(i).get("post_title")+" "+rows.get(i).get("post_content")+"\n";


        }
        return msg;

    }

}
