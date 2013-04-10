package com.game.zombyte;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.bag.lib.Screen;
import com.bag.lib.impl.GLGame;
import com.game.store.StoreActivity;
import com.game.utilities.Settings;

public class ZombyteActivity extends GLGame {

	boolean firstTimeCreate = true;
	public static Context gameContext;

	public Screen getStartScreen() {
		// return new MainMenuScreen(this);
		gameContext = this.getBaseContext();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		overridePendingTransition(R.anim.slide_in_bot, R.anim.slide_out_top);

		return new MainMenuScreen(this);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if (firstTimeCreate) {
			Settings.load(getFileIO());
			Assets.load(this);
			firstTimeCreate = false;
		} else {
			Assets.reload();
		}
	}

	private SensorManager mSensorManager;
	public static float mAccel; // acceleration apart from gravity
	public static float mAccelCurrent; // current acceleration including gravity
	public static float mAccelLast; // last acceleration including gravity

	/* SensorListener (shake phone) */
	private final SensorEventListener mSensorListener = new SensorEventListener() {

		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter

		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	public static float getCurrentAccel() {
		return mAccelCurrent;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		overridePendingTransition(R.anim.slide_out_bot, R.anim.slide_out_top);

		// if(Settings.soundEnabled)
		// Assets.music.pause();
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		// if(Settings.soundEnabled)
		// Assets.music.pause();
	}

	@Override
	public void onStop() {
		super.onStop();
		/* Save battery by unregistering the sensor listener */
		mSensorManager.unregisterListener(mSensorListener);
		Assets.intro.stop();
		Assets.gamemusic.stop();
	}

	public static void startNewActivity() {
		Intent intent = new Intent(gameContext, StoreActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		gameContext.startActivity(intent);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// overridePendingTransition(R.anim.slide_in_top, R.anim.slide_in_top);
	// }
}
