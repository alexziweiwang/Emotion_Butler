package com.empatica.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.webkit.*;


/**
 * Lead user to positive thinking method
 */
public class PositiveThinking extends AppCompatActivity {

    private Button back;

    private Button sos;

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive_thinking_cbt);

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

        //webview = new WebView(this);
        webview = findViewById(R.id.web);

        webview.loadUrl("https://www.themindsetapp.com/thinking-traps/");
    }

    /**
     * Enter activity sosHandle
     */
    public void startSos() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);
    }
}