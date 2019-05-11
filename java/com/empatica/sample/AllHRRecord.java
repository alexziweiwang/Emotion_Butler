package com.empatica.sample;

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
 * Shows Heart Rate Record of user for further HR data analyzing
 */
public class AllHRRecord extends AppCompatActivity {

    private EditText HRCont;

    private Button back;

    private boolean showingRecord;

    private final String filename = "allHR.txt";

    private Button update;

    private String newRecordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hrrecord);

        showingRecord = false;

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HRCont = findViewById(R.id.hrRecord);

        FileInputStream inputStream = null;
        load(inputStream, filename);

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRecordStr = HRCont.getText().toString();

                FileOutputStream outputStream = null;
                save(outputStream, filename, newRecordStr);
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
            outputStream = openFileOutput(filename, MODE_PRIVATE); //rewrite file
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
    public void load(FileInputStream inputStream, String QrFilename) {
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
                updateLabel(HRCont, sb.toString());
            }
        } catch(Exception e){
            e.printStackTrace();
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
}
