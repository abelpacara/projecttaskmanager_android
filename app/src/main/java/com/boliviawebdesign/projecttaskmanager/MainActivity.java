package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.util.Hashtable;



import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    TableLayout tableLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout)findViewById(R.id.commentsTable);


        new ProcessJSON().execute();


    }












    private class ProcessJSON extends AsyncTask<String, Void, String>{
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = getResources().getString(R.string.server_address)+"/view_list_comments";
            //String stringURL = "http://192.168.134.200/ptm/index.php/services/view_list_comments";

            Hashtable hashparams =new Hashtable();

            SenderReceiver sender = new SenderReceiver();

            return sender.getMessage(stringURL, hashparams);
        }





        protected void onPostExecute(String stream) {
            if(stream != null){
                try{
                    //tableLayout.removeAllViews();

                    JSONArray jsarray = new JSONArray(stream);




                    for(int i=0; i<jsarray.length(); i++){


                        JSONObject jsobj = jsarray.getJSONObject(i);

                        String jspostTitle= jsobj.getString("post_title");
                        String jspostContent= jsobj.getString("post_content");
                        final String jspostId= jsobj.getString("id_post");


                        LinearLayout layout1 = new LinearLayout(MainActivity.this);
                        layout1.setBackgroundColor(Color.CYAN);
                        layout1.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout layout2 = new LinearLayout(MainActivity.this);
                        layout2.setBackgroundColor(Color.GRAY);
                        layout2.setOrientation(LinearLayout.HORIZONTAL);





                        TableRow row1 = new TableRow(MainActivity.this);
                        TableRow row2 = new TableRow(MainActivity.this);
                        TableRow row3 = new TableRow(MainActivity.this);

                        TextView projectTitle = new TextView(MainActivity.this);
                        projectTitle.setText("Project Title");
                        projectTitle.setTypeface(null, Typeface.BOLD);
                        projectTitle.setTextSize(getResources().getDimension(R.dimen.textsizeLarge));

                        TextView discussionTitle = new TextView(MainActivity.this);
                        discussionTitle.setText("Discussion Title");
                        discussionTitle.setTypeface(null, Typeface.ITALIC);
                        discussionTitle.setTextSize(getResources().getDimension(R.dimen.textsizeMedium));

                        TextView postTitle = new TextView(MainActivity.this);
                        //postTitle.setText("Post Title");
                        postTitle.setText(jspostTitle);


                        postTitle.setTextSize(getResources().getDimension(R.dimen.textsizeSmall));

                        TextView postContent = new TextView(MainActivity.this);
                        postContent.setText(jspostContent);

                        Button btnRepply = new Button(MainActivity.this);
                        btnRepply.setText("Reply "+i);

                        Button btnComment = new Button(MainActivity.this);
                        btnComment.setText("Comment " + i);

                        //Button btnAddComment = (Button) findViewById(R.id.btnAddComment);

                        //btnAddComment.setOnClickListener(getOnClickDoSomething(btnAddComment, jspostId));



                        btnComment.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("post_id", jspostId);

                                intent.putExtras(myBundle);
                                startActivity(intent);


                            }
                        });




                        tableLayout.addView(row1);
                        tableLayout.addView(row2);
                        tableLayout.addView(row3);

                        row1.addView(layout1);

                        layout1.addView(projectTitle);
                        layout1.addView(discussionTitle);
                        layout1.addView(postTitle);


                        row2.addView(layout2);



                        layout2.addView(btnComment);
                        layout2.addView(btnRepply);



                        row3.addView(postContent);


                        ((TableRow.LayoutParams) postContent.getLayoutParams()).weight = 1;



                    }




                }
                catch(JSONException ex){
                    ex.printStackTrace();
                }
            }

        } // onPostExecute() end
    } // ProcessJSON class end

    View.OnClickListener getOnClickDoSomething(final Button button, final String jspostId)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                button.setText("COMMENT");





                Intent intent = new Intent(MainActivity.this, AddComment.class);

                Bundle myBundle = new Bundle();
                myBundle.putString("discussion_id", jspostId);

                intent.putExtras(myBundle);
                startActivity(intent);




            }
        };
    }
}
