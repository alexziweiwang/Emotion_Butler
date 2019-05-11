package com.empatica.sample;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.empatica.empalink.ConnectionNotAllowedException;
import com.empatica.empalink.EmpaDeviceManager;
import com.empatica.empalink.EmpaticaDevice;
import com.empatica.empalink.config.EmpaSensorStatus;
import com.empatica.empalink.config.EmpaSensorType;
import com.empatica.empalink.config.EmpaStatus;
import com.empatica.empalink.delegate.EmpaDataDelegate;
import com.empatica.empalink.delegate.EmpaStatusDelegate;

import java.io.IOException;
import java.io.FileOutputStream;

import java.util.ArrayList;

import java.lang.String;


/**
 * Apply breath training function,
 * let user to follow pattern of "4-7-8 breathing trick" to relax
 */
public class BreatheTraining extends AppCompatActivity implements EmpaDataDelegate, EmpaStatusDelegate{

    private static final int REQUEST_ENABLE_BT = 1;

    private static final int REQUEST_PERMISSION_ACCESS_COARSE_LOCATION = 1;

    private static final String EMPATICA_API_KEY = "5ae0e6cf0d2a4df4961a98e72c6427c2";

    private EmpaDeviceManager deviceManager;

    private Button backHome;

    private TextView statusLabel;

    private TextView deviceNameLabel;

    private LinearLayout dataCnt;

    private Button disconnectButton;

    private TextView hrValue;

    private float hr;

    private ArrayList<Float> hrList;

    private float hrSum;

    private float avgHr;

    private Button sos;

    private Button startBTr;

    private TextView startedHR;

    private boolean firstRound;

    private String allHR;

    private boolean sessionStart;

    private long startTime;

    private long prevTime;

    public final String allHRRecord = "allHR.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_breathe_training);

        hr = -1;
        avgHr = -1;
        allHR = "";
        sessionStart = false;
        startTime = -1;
        prevTime = -1;

        firstRound = true;
        startedHR = findViewById(R.id.startHR);

        sos = findViewById(R.id.sosButton);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSos();
            }
        });

        startBTr = findViewById(R.id.startBtr);
        startBTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hr != -1) {

                    updateLabel(startedHR, "Your HR when started: " + hr);
                }
                startBTraining();
            }
        });

        hrList = new ArrayList<Float>();

        backHome = findViewById(R.id.backHome);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }
                finish();

            }
        });
        // Initialize vars that reference UI components
        statusLabel = findViewById(R.id.status);

        dataCnt = findViewById(R.id.dataArea);

        deviceNameLabel = findViewById(R.id.deviceName);

        hrValue = findViewById(R.id.hrValue);

        disconnectButton = findViewById(R.id.disconnectButton);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManager != null) {
                    deviceManager.disconnect();
                }
            }
        });
        disconnectButton.setVisibility(View.INVISIBLE);

        statusLabel.setVisibility(View.INVISIBLE);

        initEmpaticaDeviceManager();
    }

    /**
     * Start Activity to "sosHandle"
     */
    public void startSos() {
        Intent intent = new Intent(this, sosHandle.class);
        startActivity(intent);
    }


    /**
     * Pop a serial of message as the pattern for user to follow, starting step
     */
    public void startBTraining() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Welcome to breath-training!");
        builder.setMessage("Exhale completely through your mouth, making a whoosh sound...");

        builder.setPositiveButton("okay I'm following", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                brIn();
            }
        });

        builder.setNegativeButton("quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


    /**
     * Pop a serial of message as the pattern for user to follow, breath-in step
     */
    public void brIn() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Breath In");
        builder.setMessage("Close your mouth and inhale quietly through your nose to a mental count of 4");

        builder.setPositiveButton("okay I'm following", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                brHold();
            }
        });

        builder.setNegativeButton("quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    /**
     * Pop a serial of message as the pattern for user to follow, hold-breath step
     */
    public void brHold() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Hold your breath");
        builder.setMessage("Hold your breath for a count of 5");

        builder.setPositiveButton("okay I'm following", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                brOut();

            }
        });

        builder.setNegativeButton("quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Pop a serial of message as the pattern for user to follow, breath-out step
     */
    public void brOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Breath Out");
        builder.setMessage("Exhale completely through your mouth, making a whoosh sound to a count of 8");

        builder.setPositiveButton("okay I'm following", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(firstRound == true) {
                    brOneRound();
                } else {
                    brIn();
                }
            }
        });

        builder.setNegativeButton("quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    /**
     * Pop a serial of message as the pattern for user to follow,
     * after the first round, showing a tip for continuing
     */
    public void brOneRound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Continue as you like");
        builder.setMessage("This is 1 breath. You could continue and repeat the cycle 3 more times for a total of 4 breaths :)");

        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                firstRound = false;
                brIn();
            }
        });

        builder.setNegativeButton("quit", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }


    /**
     * Empatica connection permission handling
     * @param requestCode request code
     * @param permissions permission
     * @param grantResults grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_COARSE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay!
                    initEmpaticaDeviceManager();
                } else {
                    // Permission denied, boo!
                    final boolean needRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    new AlertDialog.Builder(this)
                            .setTitle("Permission required")
                            .setMessage("Without this permission bluetooth low energy devices cannot be found, allow it in order to connect to the device.")
                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // try again
                                    if (needRationale) {
                                        // the "never ask again" flash is not set, try again with permission request
                                        initEmpaticaDeviceManager();
                                    } else {
                                        // the "never ask again" flag is set so the permission requests is disabled, try open app settings to enable the permission
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .setNegativeButton("Exit application", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // without permission exit is the only way
                                    finish();
                                }
                            })
                            .show();
                }
                break;
        }
    }

    /**
     * Initialize Empatica device
     */
    private void initEmpaticaDeviceManager() {
        // Android 6 (API level 23) now require ACCESS_COARSE_LOCATION permission to use BLE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, REQUEST_PERMISSION_ACCESS_COARSE_LOCATION);
        } else {

            if (TextUtils.isEmpty(EMPATICA_API_KEY)) {
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Please insert your API KEY")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // without permission exit is the only way
                                finish();
                            }
                        })
                        .show();
                return;
            }

            // Create a new EmpaDeviceManager. MainActivity is both its data and status delegate.
            deviceManager = new EmpaDeviceManager(getApplicationContext(), this, this);

            // Initialize the Device Manager using your API key. You need to have Internet access at this point.
            deviceManager.authenticateWithAPIKey(EMPATICA_API_KEY);

        }
    }

    /**
     * Empatica device pause handling
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (deviceManager != null) {
            deviceManager.stopScanning();
        }
    }

    /**
     * Empatica device destroy handling
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deviceManager != null) {
            deviceManager.cleanUp();
        }
    }

    /**
     * Discover Empatica device
     * @param bluetoothDevice bluetooch device signal
     * @param deviceName name of device
     * @param rssi rssi
     * @param allowed allowed
     */
    @Override
    public void didDiscoverDevice(EmpaticaDevice bluetoothDevice, String deviceName, int rssi, boolean allowed) {
        // Check if the discovered device can be used with your API key. If allowed is always false,
        // the device is not linked with your API key. Please check your developer area at
        // https://www.empatica.com/connect/developer.php
        if (allowed) {
            // Stop scanning. The first allowed device will do.
            deviceManager.stopScanning();

            disconnectButton.setVisibility(View.VISIBLE);
            try {
                // Connect to the device
                deviceManager.connectDevice(bluetoothDevice);
                updateLabel(deviceNameLabel, "Device Name: " + deviceName);

            } catch (ConnectionNotAllowedException e) {
                // This should happen only if you try to connect when allowed == false.
                Toast.makeText(BreatheTraining.this, "Sorry, you can't connect to this device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Check if bluetooth enabled
     */
    @Override
    public void didRequestEnableBluetooth() {
        // Request the user to enable Bluetooth
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }


    /**
     * Empatica activity
     * @param requestCode request code
     * @param resultCode result code
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The user chose not to enable Bluetooth
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            // You should deal with this
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * React to sensor and wrist status
     * @param status status
     * @param type sensor type
     */
    @Override
    public void didUpdateSensorStatus(@EmpaSensorStatus int status, EmpaSensorType type) {
        didUpdateOnWristStatus(status);
    }


    /**
     * Scan status of device
     * @param status
     */
    @Override
    public void didUpdateStatus(EmpaStatus status) {
        // Update the UI
        updateLabel(statusLabel, status.name());

        // The device manager is ready for use
        if (status == EmpaStatus.READY) {

            statusLabel.setVisibility(View.VISIBLE);
            updateLabel(statusLabel, "Status: " + status.name() + " - You could turn on your Empatica wristband XDD");

            // Start scanning
            deviceManager.startScanning();
            // The device manager has established a connection

            hide();

        } else if (status == EmpaStatus.CONNECTED) {

            show();
            // The device manager disconnected from a device
        } else if (status == EmpaStatus.DISCONNECTED) {

            updateLabel(deviceNameLabel, "");

            hide();
        }
    }

    @Override
    public void didReceiveAcceleration(int x, int y, int z, double timestamp) {
    }

    @Override
    public void didReceiveBVP(float bvp, double timestamp) {
    }


    @Override
    public void didReceiveBatteryLevel(float battery, double timestamp) {
    }

    @Override
    public void didReceiveGSR(float gsr, double timestamp) {
    }


    /**
     * React for each ibi value
     * @param ibi ibi value
     * @param timestamp time stamp
     */
    @Override
    public void didReceiveIBI(float ibi, double timestamp) {

        Float ibiValue = new Float(ibi);
        Long currTime = System.currentTimeMillis()/1000;
        if(ibiValue != null ) {
            if(sessionStart == false) {
                //this is the first time getting HR value
                startTime = System.currentTimeMillis()/1000;

                prevTime = startTime;
                sessionStart = true;
            }
            hr = 60/ibi;

            if(prevTime > 0 && startTime > 0 && (currTime - prevTime) > 2) { //record heart rate every 2 second
                String formattedHR = String.format("%.2f", hr);

                formattedHR = formattedHR + ",";
                FileOutputStream outputStream = null;
                save(outputStream, allHRRecord, formattedHR);
                prevTime = currTime;
            }

            hrList.add(hr);
            hrSum = hrSum + hr;

            if(hrList.size() > 15) {
                hrSum = hrSum - hrList.get(0);
                hrList.remove(0);
            }

            avgHr = hrSum / hrList.size();
            updateLabel(hrValue, "Realtime HR: "+hr + "  (Avg HR: " + avgHr + ")");
        }
    }


    @Override
    public void didReceiveTemperature(float temp, double timestamp) {
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

    @Override
    public void didReceiveTag(double timestamp) {
    }


    /**
     * Show data layout
     */
    @Override
    public void didEstablishConnection() {
        show();
    }


    /**
     * Demonstrate Empatica's status
     * @param status
     */
    @Override
    public void didUpdateOnWristStatus(@EmpaSensorStatus final int status) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (status == EmpaSensorStatus.ON_WRIST) {

                    ((TextView) findViewById(R.id.wrist_status_label)).setText("ON WRIST");
                }
                else {

                    ((TextView) findViewById(R.id.wrist_status_label)).setText("NOT ON WRIST");
                }
            }
        });
    }


    /**
     * Show data layout
     */
    void show() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                dataCnt.setVisibility(View.VISIBLE);
            }
        });
    }


    /**
     * Hide data layout
     */
    void hide() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dataCnt.setVisibility(View.INVISIBLE);
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

}
