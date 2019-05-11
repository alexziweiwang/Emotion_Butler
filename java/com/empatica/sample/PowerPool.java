package com.empatica.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class PowerPool extends AppCompatActivity {

    private Button back;

    private Button sos;

    private EditText onegoodInput;

    private EditText twoproudInput;

    private EditText threeplanInput;

    private EditText nameInput;

    private String onegoodStr;

    private String twoproudStr;

    private String threeplanStr;

    private String nameStr;

    private Button checkPow;

    private EditText checknameInput;

    private String checknameStr;

    private String currPower;

    private Button record;

    private EditText showPP;

    private Button updateBut;

    private String newPPContent;

    private String currFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_pool);

        currPower = "";

        checkPow = findViewById(R.id.checkPower);

        checknameInput = findViewById(R.id.checknameInput);

        onegoodInput = findViewById(R.id.onegoodInput);

        twoproudInput = findViewById(R.id.twoproudInput);

        threeplanInput = findViewById(R.id.threeplanInput);

        nameInput = findViewById(R.id.nameInput);

        onegoodStr = onegoodInput.getText().toString();

        twoproudStr = twoproudInput.getText().toString();

        threeplanStr = threeplanInput.getText().toString();

        nameStr = nameInput.getText().toString();

        checknameStr = checknameInput.getText().toString();

        showPP = findViewById(R.id.showPP);
        showPP.setVisibility(View.INVISIBLE);


        //if checkname's file exists, then open then file, otherwise create a file...
        record = findViewById(R.id.recordBut);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = nameInput.getText().toString();
                onegoodStr = onegoodInput.getText().toString();
                twoproudStr = twoproudInput.getText().toString();
                threeplanStr = threeplanInput.getText().toString();

                currPower = nameStr + ":\na good thing happened on this day: " + onegoodStr
                        + ",\nand you are so proud of yourself with your " + twoproudStr
                        + "!\nAlso you have a small plan to " + threeplanStr + "\n\n";

                //get filename by username
                String filename = nameStr + "'s_Power";
                String fileContents = currPower;
                FileOutputStream outputStream = null;
                saveAppend(outputStream, filename, fileContents);


                currPower = "";
                nameInput.setText("");
                onegoodInput.setText("");
                twoproudInput.setText("");
                threeplanInput.setText("");
            }
        });



        checkPow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checknameStr = checknameInput.getText().toString();
                String filename = checknameStr + "'s_Power";

                showPP.setVisibility(View.VISIBLE);
                FileInputStream inputStream = null;
                tryOpenPP(inputStream, filename);



                checknameInput.setText("");
            }
        });


        updateBut = findViewById(R.id.update2);
        updateBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPPContent = showPP.getText().toString();

                FileOutputStream outputStream = null;
                saveRewrite(outputStream, currFilename, newPPContent);
            }
        });


        back = findViewById(R.id.back2);
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
    }

    /**
     * Enter activity sosHandle
     */
    public void startSos() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);
    }


    /**
     * Write string into given file, overwritting
     * @param outputStream given destination to write the content
     * @param filename given filename
     * @param fileContents given String to write
     */
    public void saveRewrite(FileOutputStream outputStream, String filename, String fileContents) {
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
     * Write string into given file, appending
     * @param outputStream given destination to write the content
     * @param filename given filename
     * @param fileContents given String to write
     */
    public void saveAppend(FileOutputStream outputStream, String filename, String fileContents) {
        try {
            outputStream = openFileOutput(filename, MODE_APPEND); //rewrite the file
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
    public void tryOpenPP(FileInputStream inputStream, String QrFilename) {
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
                currFilename = QrFilename;
                newPPContent = sb.toString();
                updateLabel(showPP, sb.toString());
            }
        } catch(Exception e){
            updateLabel(showPP, "Sorry, no power pool record found...");
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


}
