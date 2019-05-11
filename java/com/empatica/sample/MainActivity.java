package com.empatica.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.empatica.empalink.EmpaDeviceManager;



public class MainActivity extends AppCompatActivity{ // implements EmpaDataDelegate, EmpaStatusDelegate

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;

    private static final String EMPATICA_API_KEY = "5ae0e6cf0d2a4df4961a98e72c6427c2";

    private EmpaDeviceManager deviceManager;

    private Button toRealtimeCare;

    private LinearLayout sosLayout;

    private LinearLayout toolKit;

    private Button restart;

    private Button sosButton;

    private Button moodJournal;

    private Button powerPool;

    private Button setBLN;

    private Button showHRRecod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sosLayout = findViewById(R.id.sosLayout);
        sosLayout.setVisibility(View.VISIBLE);
        sosButton = findViewById(R.id.sosButton);

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSOS();
            }
        });

        setBLN = findViewById(R.id.setBLN);
        setBLN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }

                enterSeetingHRBLN();
            }
        });


        showHRRecod = findViewById(R.id.showHR);
        showHRRecod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }
                enterHRRecord();
            }
        });


        toolKit = findViewById(R.id.toolKit);
        toolKit.setVisibility(View.VISIBLE);

        restart = findViewById(R.id.restartButton);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }

                Intent restartIntent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(restartIntent);
            }
        });


        toRealtimeCare = findViewById(R.id.toRC);
        toRealtimeCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterRealtimeCare();

            }
        });


        moodJournal = findViewById(R.id.moodJournal);
        moodJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMoodJournal();
            }
        });


        powerPool = findViewById(R.id.powerPool);
        powerPool = findViewById(R.id.powerPool);
        powerPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPowerPool();
            }
        });

    }

    /**
     * Enter activity AllHRRecord
     */
    public void enterHRRecord() {

        Intent intent = new Intent(this, AllHRRecord.class);
        startActivity(intent);

    }

    /**
     * Enter activity HRBaselineSetting
     */
    public void enterSeetingHRBLN() {
        Intent intent = new Intent(this, HRBaselineSetting.class);
        startActivity(intent);


    }

    /**
     * Enter activity RealtimeCare
     */
    public void enterRealtimeCare() {
        Intent intent = new Intent(this, RealtimeCare.class);
        startActivity(intent);
    }

    /**
     * Enter activity sosHandle
     */
    public void startSOS() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);

    }

    /**
     * Enter activity MoodJournalAdd
     */
    public void startMoodJournal() {
        Intent intent = new Intent(this, MoodJournalAdd.class);
        startActivity(intent);
    }

    /**
     * Enter activity PowerPool
     */
    public void startPowerPool() {
        Intent intent = new Intent(this, PowerPool.class);
        startActivity(intent);

    }

}


