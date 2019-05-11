package com.empatica.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Query user's mood journal
 */
public class MoodJournalRecordCheck extends AppCompatActivity {

    private EditText histCont;

    private EditText enteredJMP;
    private String jmpStr;

    private Button submitJMP;

    private Button sos;
    private Button back;

    private Button edit;
    private String newMJContent;

    private String userFilename;

    private boolean showingRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_journal_record_check);

        showingRecord = false;
        userFilename = null;



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


        histCont = findViewById(R.id.histContent);

        enteredJMP = findViewById(R.id.enteredJMP);
        submitJMP = findViewById(R.id.submitJMP);
        submitJMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jmpStr = enteredJMP.getText().toString();
                String tryFile = jmpStr + "-MoodJournal";

                FileInputStream inputStream = null;
                tryOpenMJHist(inputStream, tryFile);
                enteredJMP.setText("");
            }
        });


        edit = findViewById(R.id.editBut);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userFilename != null && showingRecord == true) {
                    newMJContent = histCont.getText().toString();

                    FileOutputStream outputStream = null;
                    save(outputStream, userFilename, newMJContent);
                }
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
            outputStream = openFileOutput(filename, MODE_PRIVATE); //rewrite the file
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
     * Read from given source of file input stream
     * @param inputStream given source to read the file
     * @param QrFilename given file name
     */
    public void tryOpenMJHist(FileInputStream inputStream, String QrFilename) {
        try{
            inputStream = openFileInput(QrFilename);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            if(sb.toString().length() > 0) {
                updateLabel(histCont, sb.toString());
                showingRecord = true;
                userFilename = QrFilename;
            }
        } catch(Exception e){
            updateLabel(histCont, "Sorry, no mood journal record found...");
            System.out.println(e);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * Update a label with some text, making sure this is run in the UI thread
     */
    private void updateLabel(final TextView label, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                label.setText(text);
            }
        });
    }


    /**
     * Enter activity sosHandle
     */
    public void startSos() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);
    }



}

