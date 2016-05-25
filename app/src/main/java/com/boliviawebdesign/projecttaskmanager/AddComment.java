package com.boliviawebdesign.projecttaskmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class AddComment extends AppCompatActivity {


    private Session session;//global variable

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        //####################################################################
        session = new Session(AddComment.this); //in oncreate
        //and now we set sharedpreference then use this like



        Button btnAddComment = (Button) this.findViewById(R.id.btnAddComment);

        Button btnTakePicture = (Button) this.findViewById(R.id.btnTakePicture);

        imageView = (ImageView) this.findViewById(R.id.imageView);

        final EditText commentContentText = (EditText) findViewById(R.id.commentContentText);

        Intent myIntent = getIntent(); // this is just for example purpose

        Bundle myBundle = myIntent.getExtras();

        String msg =  myBundle.getString("parent_id")+" "+myBundle.getString("project_id")+" "+myBundle.getString("forum_id");

        commentContentText.setText(msg);
        //#################################################################
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = getIntent(); // this is just for example purpose
                Bundle myBundle = myIntent.getExtras();

                String params[] = {commentContentText.getText().toString(), myBundle.getString("parent_id"),
                        myBundle.getString("project_id"),
                        myBundle.getString("forum_id"),
                };


                new ProcessAddComment().execute(params);
            }
        });
        //#################################################################
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                     startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                 }
             }
        });



    }
    /************************************************************************/
    /************************************************************************/
    String mCurrentPhotoPath;
    /************************************************************************/
    /************************************************************************/
    private File createImageFile(String filename) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = filename!=null? filename : "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    /************************************************************************/
    /************************************************************************/
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
    /************************************************************************/
    /************************************************************************/

    private class ProcessAddComment extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = getResources().getString(R.string.server_address)+"/add_comment";

            Hashtable hashparams =new Hashtable();

            hashparams.put("user_id",session.getUserId());
            hashparams.put("post_content",params[0]);
            hashparams.put("parent_id",params[1]);
            hashparams.put("project_id",params[2]);
            hashparams.put("forum_id", params[3]);



            /*File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  *//* prefix *//*
                    ".jpg",         *//* suffix *//*
                    storageDir      *//* directory *//*
            );*/


            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }

        protected void onPostExecute(String stream) {


            startActivity(new Intent(AddComment.this, MainActivity.class));


        } // onPostExecute() end
    } // ProcessJSON class end


}


