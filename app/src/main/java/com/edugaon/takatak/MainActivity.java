package com.edugaon.takatak;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView textView, textView2, textView3;
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
    Button button;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner spinner;
    String[] listItem;
    String[] users = {"Guddu", "Chanchal", "ansar", "Arman", "Rohit", "Rahil", "Rahul", "Abhishek", "Arun", "Nitish", "Bholu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spineerClass);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, users);
        aa.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        textView3 = findViewById(R.id.textViewItem);
        listView = findViewById(R.id.listViewitItm);
        listItem = getResources().getStringArray(R.array.array_technology);
        final ArrayAdapter<String>adapter =new  ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1,listItem);
        listView.setAdapter(adapter);
        textView2 = findViewById(R.id.showTimePicker);
        timePicker = findViewById(R.id.timePicker);
        textView = findViewById(R.id.showDate);
        button = findViewById(R.id.button);
        datePicker = findViewById(R.id.datepicker);
        textView.setText("Current Date -:" + getCurrentDate());
        textView2.setText("Current Time" + getCurrentTime());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("Change Date -:" + getCurrentDate());
                textView2.setText("Current Time" + getCurrentTime());
                Intent intent = new Intent(MainActivity.this, RealTimeDatabase.class);
                startActivity(intent);

                progressBar = new ProgressDialog(view.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("File downloading ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                progressBarStatus = 0;
                fileSize = 0;

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (progressBarStatus < 100) {
                            progressBarStatus = doOperation();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        }
                        if (progressBarStatus >= 100) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progressBar.dismiss();
                        }

                    }
                }).start();
            }


        });

    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(datePicker.getMonth() + 1 + "/");
        builder.append(datePicker.getDayOfMonth() + "/");
        builder.append(datePicker.getYear());
        return builder.toString();
    }

    private String getCurrentTime() {
        StringBuilder builder = new StringBuilder();
        builder.append(timePicker.getCurrentHour() + "/");
        builder.append(timePicker.getCurrentMinute() + "/");
        // builder.append(timePicker.getCurrent)
        return builder.toString();
    }

    public int doOperation() {

        while (fileSize >= 10000) {
            fileSize++;
            if (fileSize == 1000) {
                return 10;
            } else if (fileSize == 2000) {
                return 20;

            } else if (fileSize == 3000) {
                return 30;
            } else if (fileSize == 4000) {
                return 40;
            } else if (fileSize == 5000) {
                return 50;
            } else if (fileSize == 6000) {
                return 60;
            } else if (fileSize == 7000) {
                return 70;
            } else if (fileSize == 8000) {
                return 80;
            } else if (fileSize == 9000) {
                return 90;
            }

        }
        return 100;

    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected  void onStop(){
        super.onStop();
    }
    @Override
    protected  void onRestart(){
        super.onRestart();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}