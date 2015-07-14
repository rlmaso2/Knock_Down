package com.mason.ricky.hw5;


import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {
	private float x;
	private float y;
	private float vx;
	private float vy;
	private float ax;
	private Paint paint;
	private float levelHeight;
	private static final int size = 150;
	int newWidth = 175;
	int newHeight = 175;
	int spaceWidth = 250;
	int strokeWidth = 10;
	int space = 0;
	private float posY;
	private float posX=0;
	private float level= 1;
	boolean collisionX;
	boolean collisionY;
	boolean skip = true;
	boolean newLine = true;
	Context context;
	Bitmap b=BitmapFactory.decodeResource(getResources(), R.drawable.inspector_gadget);
	Bitmap resized = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
	Random r = new Random();
	Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
	
	private Handler handler = new Handler();
	private Runnable invalidator = new Runnable() {
		@Override public void run() {
			invalidate();
		}
	};
	private AnimationThread animationThread;

	public GameView(Context context) {
		super(context);
		this.context = context;
	}
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void start() {
		stop();
		animationThread = new AnimationThread();
		animationThread.start();
	}
	public void stop() {
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}
	}
	public void change(float ax) {
		this.ax = ax;
		
	}
	public float getX() {
		return this.x;
		
	}
	public void myVib(){
		this.vib.vibrate(100);
	}
	private class AnimationThread extends Thread {
		@Override public void run() {
			while (!isInterrupted()) {
				
				// accelerate
				vx += ax;
				y+= getHeight()/100;
				// move
				x += vx;
				//Set Character Position
				levelHeight = getHeight()/10;
				posY = level * levelHeight;
				posX = getX();
				//Collision Detection
				switch(space){
					case 0:
						collisionX = !(posX < spaceWidth);
						break;
					case 1:
						collisionX = !((posX < (getWidth()/4)+spaceWidth/2) && (posX > (getWidth()/4)-spaceWidth/2));
						break;
					case 2:
						collisionX = !((posX < (getWidth()/2)+spaceWidth/2) && (posX > (getWidth()/2)-spaceWidth/2));
						break;
					case 3:
						collisionX = !((posX < (3*getWidth()/4)+spaceWidth/2-100) && (posX > (3*getWidth()/4)-spaceWidth/2));
						break;
					case 4:
						collisionX = !(posX > (getWidth()-spaceWidth));
						break;
				}
				
				collisionY = (y == level*levelHeight);
				
				if(collisionX && collisionY && !skip){
					myVib();
					level++;
					skip = true;
				}
				else{
					skip = false;
				}
					

				// check bounds
				if(level > 9){
					
					try {
						Thread.sleep(10000);
						} catch (InterruptedException e) {
						}
						
				level = 0;
				}
				if (x < 0) {
					x = 0;
					ax = 0;
					vx = -vx/2;
				}
				if (x > getWidth()-size) {
					x = getWidth()-size;
					ax = 0;
					vx = -vx/2;
				}
				if (y < 0) {
					y = 0;
					vy = -vy/2;
				}
				if (y > getHeight()-strokeWidth) {
					y = 0;
					vy = -vy/2;
					newLine = true;
				}
				
				handler.post(invalidator);
				try {
					sleep(50);
				} catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}
	
	@Override protected void onDraw(Canvas canvas) {
		if (paint == null) {
			paint = new Paint();
			paint.setColor(Color.YELLOW);
			paint.setStrokeWidth(strokeWidth);
		}
		
		int max = 5;

		canvas.drawBitmap(resized, x, posY, paint);
		
		if(newLine){
			space = r.nextInt(max + 1);
		}
		
		switch(space){
			case 0:
				canvas.drawLine(spaceWidth, y, getWidth(), y, this.paint);
				Log.d("Case", String.valueOf(0000));
				newLine = false;
				break;
			case 1:
				canvas.drawLine(0, y, (getWidth()/4)-spaceWidth/2, y, paint);
				canvas.drawLine((getWidth()/4)+spaceWidth/2, y, getWidth(), y, paint);
				Log.d("Case", String.valueOf(1111));
				newLine = false;
				break;
			case 2:
				canvas.drawLine(0, y, (getWidth()/2)-spaceWidth/2, y, paint);
				canvas.drawLine((getWidth()/2)+spaceWidth/2, y, getWidth(), y, paint);
				Log.d("Case", String.valueOf(2222));
				newLine = false;
				break;
			case 3:
				canvas.drawLine(0, y, (3*getWidth()/4)-spaceWidth/2, y, paint);
				canvas.drawLine((3*getWidth()/4)+spaceWidth/2, y, getWidth(), y, paint);
				Log.d("Case", String.valueOf(3333));
				newLine = false;
				break;
			case 4:
				canvas.drawLine(0, y, getWidth()-spaceWidth, y, paint);
				Log.d("Case", String.valueOf(4444));
				newLine = false;
				break;
		}
	}
}