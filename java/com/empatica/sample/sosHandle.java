package com.empatica.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class sosHandle extends AppCompatActivity {

    private Button back;

    private Button powerPool;

    private Button posThink;

    private Button breathTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_handle);

        back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        powerPool = findViewById(R.id.powerPool);
        powerPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPowerPool();
            }
        });

        posThink = findViewById(R.id.posThinking);
        posThink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPostThink();
            }
        });

        breathTraining = findViewById(R.id.breatheTraining);
        breathTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreathTraining();
            }
        });
    }

    /**
     * Enter activity BreatheTraining
     */
    public void startBreathTraining() {
        Intent intent = new Intent(this, BreatheTraining.class);
        startActivity(intent);

    }

    /**
     * Enter activity PositiveThinking
     */
    public void startPostThink() {
        Intent intent = new Intent(this, PositiveThinking.class);
        startActivity(intent);

    }

    /**
     * Enter activity MoodJournalAdd
     */
    public void startMoodJ() {
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
