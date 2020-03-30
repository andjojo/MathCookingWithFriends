package com.andjojo.mathcookingwithfriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andjojo.mathcookingwithfriends.WebAPI.HandlePHPResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    List<Task> taskStack;
    View tile;
    TextView taskTitle, taskDescription;
    LocationManager mLocationManager;
    String urlString = "http://18.194.159.113:5001/api/new_job/?lat=48.8552&lon=9.29231&radius=0.005";
    ProgressDialog dialog;
    Boolean firstGps = true;
    ImageButton dismiss,accept;
    ImageView imageView;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int level = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dialog = ProgressDialog.show(this, "","Blümchen werden erschnuppert...", true);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        sp = getApplicationContext().getSharedPreferences(
                "Biene", Context.MODE_PRIVATE);
        editor = sp.edit();

        WebView mWebview = (WebView)findViewById(R.id.webview);
        mWebview.loadUrl("http://latex.codecogs.com/svg.latex?(-7)-5=%22,%22Solusion");

        dismiss = (ImageButton) findViewById(R.id.imageButton2);
        accept = (ImageButton) findViewById(R.id.imageButton);
        dismiss.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    v.animate()
                            .scaleXBy(-0.04f)
                            .scaleYBy(-0.04f)
                            .setDuration(200)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    v.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(200);
                    onNo(v);
                    return true;
                }
                return false;
            }
        });
        accept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    v.animate()
                            .scaleXBy(-0.04f)
                            .scaleYBy(-0.04f)
                            .setDuration(200)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });
                    return true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){

                    v.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .setDuration(200);
                    onYes(v);
                    return true;
                }
                return false;
            }
        });
        tile = findViewById(R.id.tile);
        imageView = (ImageView) findViewById(R.id.imageView2);
        taskTitle = (TextView) findViewById(R.id.textView);
        taskStack = new ArrayList<Task>();

    }
    public void onYes(View v){
        tile.animate()
                .translationX(1.3f*tile.getWidth())
                .rotation(30)
                .alpha(0f)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        sp.edit().putBoolean("taskActive",true).apply();
                        showNextTask();
                        tile.animate().translationX(0).translationY(0).rotation(0).setDuration(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                tile.animate().alpha(1f).setDuration(200);
                            }
                        });

                    }
                });

    }

    public void onNo(View v){
        tile.animate()
                .translationX(-1.3f*tile.getWidth())
                .alpha(0f)
                .rotation(-30)
                .setDuration(500)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        taskStack.add(taskStack.get(0));
                        taskStack.remove(0);
                        showNextTask();
                        tile.animate().translationX(0).translationY(0).rotation(0).setDuration(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                tile.animate().alpha(1f).setDuration(200);
                            }
                        });
                    }
                });
    }

    public void showNextTask(){
        taskTitle.setText(taskStack.get(0).getTaskHeader());
        taskDescription.setText(taskStack.get(0).getTaskDescription());
        if (taskStack.get(0).getCat().equals("Soziales"))
            imageView.setImageResource(R.drawable.social);
        else if (taskStack.get(0).getCat().equals("Einkaufen"))
            imageView.setImageResource(R.drawable.shopping);
        else
            imageView.setImageResource(R.drawable.others);
    }

    public HandlePHPResult handlePHPResult=(s, url)->{
        dialog.dismiss();
        JSONArray jsonTasks = new JSONArray(s);
        taskStack.clear();
        for (int i=0;i<jsonTasks.length();i++){
            JSONObject task = jsonTasks.getJSONObject(i);
            taskStack.add(new Task(task));
        }
        if (taskStack.size()>0) showNextTask();
        else {
            Toast.makeText(this, "Keine Hilfegesuche gefunden",
                    Toast.LENGTH_LONG).show();
        }
        /*for (int i=0;i<10;i++){
            taskStack.add(new Task("task "+i));
        }*/
    };
}
