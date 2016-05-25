package com.boliviawebdesign.projecttaskmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private Session session;//global variable



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //####################################################################
        session = new Session(MainActivity.this); //in oncreate
        //and now we set sharedpreference then use this like


        Button btnAddProject = (Button) this.findViewById(R.id.btnAddProject);

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddProject.class);
                startActivity(intent);
            }
        });

        Button btnAddForum = (Button) this.findViewById(R.id.btnAddForum);

        btnAddForum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddForum.class);
                startActivity(intent);
            }
        });




        tableLayout = (TableLayout)findViewById(R.id.commentsTable);


        String activitiesParams[] ={session.getUserId()};
        new ProcessLoadActivities().execute(activitiesParams);


    }





    //######################################################################
    private class ProcessLoadActivities extends AsyncTask<String, Void, String>{
        protected String doInBackground(String ... params){

            String text = "";
            BufferedReader reader=null;

            String stringURL = getResources().getString(R.string.server_address)+"/view_list_activities";

            Hashtable hashparams =new Hashtable();

            hashparams.put("user_id", params[0]);

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
                        String jspostType= jsobj.getString("post_type");
                        String jspostContent= jsobj.getString("post_content");
                        final String jsparentId= jsobj.getString("id_post");
                        final String jsprojectId= jsobj.getString("project_id");
                        final String jsforumId= jsobj.getString("forum_id");

                        String jsprojectContent = jsobj.getString("project_content");
                        String jsforumContent = jsobj.getString("forum_content");


                        LinearLayout layoutHeader = new LinearLayout(MainActivity.this);
                        layoutHeader.setBackgroundColor(Color.CYAN);
                        layoutHeader.setOrientation(LinearLayout.HORIZONTAL);

                        LinearLayout layoutHeaderUser = new LinearLayout(MainActivity.this);
                        layoutHeaderUser.setBackgroundColor(Color.CYAN);
                        layoutHeaderUser.setOrientation(LinearLayout.VERTICAL);

                        LinearLayout layoutHeaderTitles = new LinearLayout(MainActivity.this);
                        layoutHeaderTitles.setBackgroundColor(Color.CYAN);
                        layoutHeaderTitles.setOrientation(LinearLayout.VERTICAL);




                        LinearLayout layoutButtons = new LinearLayout(MainActivity.this);
                        layoutButtons.setBackgroundColor(Color.GRAY);
                        layoutButtons.setOrientation(LinearLayout.HORIZONTAL);





                        TableRow rowHeader = new TableRow(MainActivity.this);
                        TableRow rowButtons = new TableRow(MainActivity.this);
                        TableRow rowContent = new TableRow(MainActivity.this);


                        ///////////////////////////////////////////////////////////////////
                        ImageButton btnUser = new ImageButton(MainActivity.this);
                        btnUser.setImageResource(R.drawable.user);

                        int btn_user_width = (int)getResources().getDimension(R.dimen.button_user_width);
                        int btn_user_height = (int)getResources().getDimension(R.dimen.button_user_height);

                        buttonStyling(btnUser, btn_user_width, btn_user_height);

                        layoutHeaderUser.addView(btnUser);


                        TextView projectContent = new TextView(MainActivity.this);
                        projectContent.setText(jsprojectContent);
                        projectContent.setTypeface(null, Typeface.BOLD);
                        projectContent.setTextSize(getResources().getDimension(R.dimen.textsizeLarge));


                        if(jspostType.equalsIgnoreCase("forum"))
                        {
                            jsforumContent = jspostContent;
                        }

                        TextView forumContent = new TextView(MainActivity.this);
                        forumContent.setText(jsforumContent);
                        forumContent.setTypeface(null, Typeface.ITALIC);
                        forumContent.setTextSize(getResources().getDimension(R.dimen.textsizeMedium));



                        TextView postContent = new TextView(MainActivity.this);
                        postContent.setText(jspostContent);


                        tableLayout.addView(rowHeader);
                        tableLayout.addView(rowContent);
                        tableLayout.addView(rowButtons);

                        rowHeader.addView(layoutHeader);

                        layoutHeaderTitles.addView(projectContent);
                        layoutHeaderTitles.addView(forumContent);



                        layoutHeader.addView(layoutHeaderUser);
                        layoutHeader.addView(layoutHeaderTitles);

                        rowButtons.addView(layoutButtons);


                        /***************************************************************/
                        int btn_width = (int)getResources().getDimension(R.dimen.button_width);
                        int btn_height = (int)getResources().getDimension(R.dimen.button_height);

                        /***************************************************************/
                        ImageButton btnComment = new ImageButton(MainActivity.this);
                        btnComment.setImageResource(R.drawable.comment);
                        buttonStyling(btnComment, btn_width, btn_height);

                        btnComment.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("parent_id", jsparentId);
                                myBundle.putString("project_id", jsprojectId);
                                myBundle.putString("forum_id", jsforumId);

                                intent.putExtras(myBundle);
                                startActivity(intent);
                            }
                        });
                        layoutButtons.addView(btnComment);
                        /***************************************************************/
                        ImageButton btnReply = new ImageButton(MainActivity.this);
                        btnReply.setImageResource(R.drawable.reply);
                        buttonStyling(btnReply, btn_width, btn_height);

                        btnReply.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("post_id", jsparentId);

                                intent.putExtras(myBundle);
                                startActivity(intent);
                            }
                        });
                        layoutButtons.addView(btnReply);
                        /***************************************************************/
                        ImageButton btnLike = new ImageButton(MainActivity.this);
                        btnLike.setImageResource(R.drawable.like);
                        buttonStyling(btnLike, btn_width, btn_height);

                        btnLike.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("post_id", jsparentId);

                                intent.putExtras(myBundle);
                                startActivity(intent);
                            }
                        });
                        layoutButtons.addView(btnLike);
                        /***************************************************************/
                        ImageButton btnMore = new ImageButton(MainActivity.this);
                        btnMore.setImageResource(R.drawable.more);
                        buttonStyling(btnMore, btn_width, btn_height);

                        btnMore.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("post_id", jsparentId);

                                intent.putExtras(myBundle);
                                startActivity(intent);


                            }
                        });
                        layoutButtons.addView(btnMore);
                        /***************************************************************/
                        ImageButton btnShare = new ImageButton(MainActivity.this);
                        btnShare.setImageResource(R.drawable.share);
                        buttonStyling(btnShare, btn_width, btn_height);

                        btnShare.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {

                                Intent intent = new Intent(MainActivity.this, AddComment.class);

                                Bundle myBundle = new Bundle();
                                myBundle.putString("post_id", jsparentId);

                                intent.putExtras(myBundle);
                                startActivity(intent);


                            }
                        });
                        layoutButtons.addView(btnShare);
                        /***************************************************************/
                        if( ! jspostType.equalsIgnoreCase("forum")) {
                            rowContent.addView(postContent);
                            ((TableRow.LayoutParams) postContent.getLayoutParams()).weight = 1;
                        }
                    }




                }
                catch(JSONException ex){
                    ex.printStackTrace();
                }
            }

        } // onPostExecute() end
    } // ProcessJSON class end


    private int getDipsToPixels(int dip){
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (dip*scale + 0.5f);
        return dpAsPixels;
    }


    private void buttonStyling(ImageButton imageBtn, int btn_width, int btn_height){
        //-------------------------------------------------


        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(btn_width, btn_height);


        int dpAsPixels = getDipsToPixels( (int)getResources().getDimension(R.dimen.margin_left_dip) );

        layoutParams.setMargins(dpAsPixels, dpAsPixels, 0, dpAsPixels);

        imageBtn.setLayoutParams(layoutParams);
        //-------------------------------------------------


        imageBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);





        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //btnComment.setBackgroundDrawable(null);
            //btnComment.setBackgroundDrawable(Color.BLUE);
        } else {
            //btnComment.setBackground(null);
            imageBtn.setBackgroundColor(Color.BLUE);
        }



        //-------------------------------------------------

    }

}
