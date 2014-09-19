package com.Rushabh.sensordatarecording;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;



//import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainActivity extends Activity implements SensorEventListener {

    EditText textData;
    Button startButton;
    Button stopButton;
    TextView textview;

    File myFile;
    FileOutputStream fOut;
    OutputStreamWriter myOutWriter;
    BufferedWriter myBufferedWriter;
    PrintWriter myPrintWriter;


    private SensorManager sensorManager;
    private long currentTime;
    private long startTime;
    private long x = 0;
    private long y = 0;

    float[] acceleration = new float[3];
    //float[] magneticField = new float[3];

    boolean stopFlag = false;
    boolean startFlag = false;
    boolean isFirstSet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // file name to be entered
        textData = (EditText) findViewById(R.id.editText);

        // start button
        startButton = (Button) findViewById(R.id.button1);
        startButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // start recording the sensor data
                try {
                	
                	String root = Environment.getExternalStorageDirectory().toString();
                	textview = (TextView) findViewById(R.id.textView);
                	//String root = getFilesDir().toString();
                	textview.setText(root);
                	
                	//File iSdCard = getFilesDir();
                	
                	//File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()+ "/SensorTest/");
                	File sdcard = Environment.getExternalStorageDirectory();
                	myFile = new File(sdcard, textData.getText()+ ".txt");
                
                    myFile.createNewFile();

                    fOut = new FileOutputStream(myFile);
                    myOutWriter = new OutputStreamWriter(fOut);
                    myBufferedWriter = new BufferedWriter(myOutWriter);
                    myPrintWriter = new PrintWriter(myBufferedWriter);

                    Toast.makeText(getBaseContext(), "Start recording the data set", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    startFlag = true;
                }
            }
        });

        // stop button
        stopButton = (Button) findViewById(R.id.button2);
        stopButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // stop recording the sensor data
                try {
                    stopFlag = true;
                    Toast.makeText(getBaseContext(), "Done recording the data set", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);
		sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		
		
		 // register this class as a listener for the sensors
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    	
    	
        if (startFlag) {

        	synchronized (this) {
   			 
    	        // SENSOR SIMULATOR
    	        int sensor = event.sensor.getType();	        
    	 
    	        switch (sensor) {
    	        case Sensor.TYPE_ACCELEROMETER:
    	        	acceleration[0] = event.values[0];
                    acceleration[1] = event.values[1];
                    acceleration[2] = event.values[2];
                    
    	        break;
    	        /**
    	        case Sensor.TYPE_MAGNETIC_FIELD:
    	        	magneticField[0] = event.values[0];
                    magneticField[1] = event.values[1];
                    magneticField[2] = event.values[2];
    	 
    	        break;
    	        */
    	        default:
    	        break;
    	        }
    	    }
        	/**

            if (event.Type() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticField[0] = event.values[0];
                magneticField[1] = event.values[1];
                magneticField[2] = event.values[2];
            }

            */

            
        	if (isFirstSet) {
                startTime = System.currentTimeMillis();
                isFirstSet = false;
            }
        	
        	currentTime = System.currentTimeMillis();

            for (int i = 0; i<1 ; i++) {
                if (!stopFlag) {
                    save();
                }

                else {
                    try {
                        myOutWriter.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        fOut.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    
    private String getTime(long time) {
    	
    	Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String time1 = DateFormat.format("hh:mm:ss", cal).toString();
        return time1; 
    	
    	//String time1 = time.toString();
    	//return time1;
    	
    }
    
    DecimalFormat xFormat = new DecimalFormat("000");
    DecimalFormat yFormat = new DecimalFormat("00.000000000");
    
    private void save() {

            //myPrintWriter.write(currentTime - startTime + " " + acceleration[0] + " " + acceleration[1] + " " + acceleration[2]
                        //+ " " + magneticField[0] + " " + magneticField[1] + " " + magneticField[2] + "\n");
    	
    	
    	y = currentTime - startTime - x;
    	
    	myPrintWriter.write(" \n" + getTime(currentTime) + "  " + xFormat.format(y) + " x:  " + yFormat.format(acceleration[0]) 
    			+ " y:  " + yFormat.format(acceleration[1]) + " z:  " + yFormat.format(acceleration[2])+ "\n");
    	 	
    	x = currentTime - startTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the sensors
        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManagerSimulator.SENSOR_DELAY_NORMAL);
        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManagerSimulator.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub

    }
    
}