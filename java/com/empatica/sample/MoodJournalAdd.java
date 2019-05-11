package com.empatica.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Add mood journal content
 */
public class MoodJournalAdd extends AppCompatActivity {

    private Button back;

    private Button sos;

    private EditText moodInput;
    private EditText extInput;
    private EditText situInput;
    private EditText thoughtInput;
    private EditText timeInput;
    private Button record;
    private Button checkHist;
    private EditText nameInput;

    private String mood;
    private String ext;
    private String situ;
    private String thought;
    private String timing;
    private static String histMoodContent = "";
    private String currMood;
    private String name;

    private TextView cmTip;
    private TextView extTip;
    private TextView situTip;
    private TextView mtTip;
    private TextView timingTip;
    private TextView nameTip;

    String temp;

    private HashMap<String, String> personalRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_journal_add);

        cmTip = findViewById(R.id.cmTip);
        extTip = findViewById(R.id.extTip);
        situTip = findViewById(R.id.situTip);
        mtTip = findViewById(R.id.mtTip);
        timingTip = findViewById(R.id.timingTip);
        nameTip = findViewById(R.id.nameTip);

        currMood = "";
        temp = "";

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sos = findViewById(R.id.sosButton3);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSos();
            }
        });

        moodInput = findViewById(R.id.moodInput);
        extInput = findViewById(R.id.extInput);
        situInput = findViewById(R.id.situInput);
        thoughtInput = findViewById(R.id.thoughtInput);
        timeInput = findViewById(R.id.timeInput);
        record = findViewById(R.id.recordBut);
        nameInput = findViewById(R.id.nameinput);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mood = moodInput.getText().toString();
                ext = extInput.getText().toString();
                situ = situInput.getText().toString();
                thought = thoughtInput.getText().toString();
                timing = timeInput.getText().toString();
                name = nameInput.getText().toString();
                currMood = "Mood: "+ mood + ", Extent: " + ext + ", Situation/Location:" + situ
                        + ",\n\tMy thought: " + thought + ", Timing: " + timing + "\n\n";

                //store into file by "name"...
                //if name's file exists, open the file; otherwise create one.
                String filename = name + "-MoodJournal";
                String fileContents = currMood;
                FileOutputStream outputStream = null;
                save(outputStream, filename, fileContents);

                histMoodContent = histMoodContent + currMood;
                currMood = "";
                temp = "";

                moodInput.setText("");
                extInput.setText("");
                situInput.setText("");
                thoughtInput.setText("");
                timeInput.setText("");
                nameInput.setText("");
            }
        });

        checkHist = findViewById(R.id.checkPrev);
        checkHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMJRecord();
            }
        });
    }


    /**
     * Write string into given file
     * @param outputStream given destination to write the content
     * @param filename given filename
     * @param fileContents given String to write
     */
    public void save(FileOutputStream outputStream, String filename, String fileContents) {
        try {
            outputStream = openFileOutput(filename, MODE_APPEND); //append new data to file
            outputStream.write(fileContents.getBytes());
            outputStream.close();

            Toast.makeText(this, "Saved to" + getFilesDir() + "/" + filename, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Enter activity MoodJournalRecordCheck
     */
    public void openMJRecord() {
        Intent intent = new Intent(this, MoodJournalRecordCheck.class);
        startActivity(intent);
    }


    /**
     * Enter activity sosHandle
     */
    public void startSos() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);
    }

}
