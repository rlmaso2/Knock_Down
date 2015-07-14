package com.mason.ricky.hw5;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class MainActivity extends Activity {
	
    private Display display;
	private GameView gameView;
	private SensorManager sensorManager;

	SensorEventListener accelListener = new SensorEventListener() {
		@Override public void onSensorChanged(final SensorEvent event) {
			runOnUiThread(new Runnable() {
				@Override public void run() {
					float x = 0;
					switch (display.getRotation()) {
						case Surface.ROTATION_0:
							x = event.values[0];
							break;
						case Surface.ROTATION_90:
							x = -event.values[1];
							break;
						case Surface.ROTATION_180:
							x = -event.values[0];
							break;
						case Surface.ROTATION_270:
							x = event.values[1];
							break;
					}
					gameView.change(-x);
				}});
		}
		@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {
			Log.d("ACCURACY CHANGE", sensor.getName() + ": " + accuracy);
		}
	};
	
	private Sensor accelerometer;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

         WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        gameView = (GameView) findViewById(R.id.game_view);
        
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		gameView.start();
	}
    @Override
    protected void onPause() {
    	gameView.stop();
    	sensorManager.unregisterListener(accelListener);
    	super.onPause();
    }

}
