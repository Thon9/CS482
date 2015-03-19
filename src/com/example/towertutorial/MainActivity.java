package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;

import java.util.Stack;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;

import org.andengine.input.touch.TouchEvent;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification.Action;
import android.content.Entity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends SimpleBaseGameActivity {
	static int CAMERA_WIDTH = 480;
	static int CAMERA_HEIGHT = 800;
	private int default_size = 100;
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion, mRing1, mRing2, mRing3;
	private ITextureRegion mBall1;
	private ITextureRegion mEnemy1;
	private ITextureRegion mBar1, mBar2;
	private Sprite mTower1, mTower2, mTower3;
	private Stack mStack1, mStack2, mStack3;
	
	private Scene mMainScene;
	private Camera mCamera;
	
	float initX = 0, initY = 0, endX=0, endY=0;
	
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}


	@Override
	protected void onCreateResources() {
		try {
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            //return getAssets().open("gfx/background.png");
		        	//return getAssets().open("gfx/rsz_billardstable.jpg");
		        	return getAssets().open("gfx/greenradargrid.jpg");
		        }
		    });
		    /*
			    ITexture towerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			        @Override
			        public InputStream open() throws IOException {
			            return getAssets().open("gfx/tower.png");
			        }
			    });
			    ITexture ring1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			        @Override
			        public InputStream open() throws IOException {
			            return getAssets().open("gfx/ring1.png");
			        }
			    });
			    ITexture ring2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			        @Override
			        public InputStream open() throws IOException {
			            return getAssets().open("gfx/ring2.png");
			        }
			    });
			    ITexture ring3 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			        @Override
			        public InputStream open() throws IOException {
			            return getAssets().open("gfx/ring3.png");
			        }
			    });
		    */
		    //Empty healthbar
		    ITexture barBlank = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/HealthBarWHITE.png");
		        }
		    });
		    //Full healthbar
		    ITexture barFull = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/HealthBarGREEN.png");
		        }
		    });
		    //player ball
		    ITexture ball1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/dragon_ball.png");
		        }
		    });
		    //enemy ball
		    ITexture enemy1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/monster1.png");
		        }
		    });
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    /*
			    towerTexture.load();
			    ring1.load();
			    ring2.load();
			    ring3.load();
		    */
		    barBlank.load();
		    barFull.load();
		    ball1.load();
		    enemy1.load();
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    /*
			    this.mTowerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
			    this.mRing1 = TextureRegionFactory.extractFromTexture(ring1);
			    this.mRing2 = TextureRegionFactory.extractFromTexture(ring2);
			    this.mRing3 = TextureRegionFactory.extractFromTexture(ring3);
		    */
		    this.mBar1 = TextureRegionFactory.extractFromTexture(barBlank);
		    this.mBar2 = TextureRegionFactory.extractFromTexture(barFull);
		    
		    this.mBall1 = TextureRegionFactory.extractFromTexture(ball1);
		    this.mEnemy1 = TextureRegionFactory.extractFromTexture(enemy1);
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		
	}


	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		this.mMainScene = new Scene();
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		this.mMainScene.attachChild(backgroundSprite);
		
		
		
		// 2 - Add the towers
		/*mTower1 = new Sprite(192, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower2 = new Sprite(400, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		mTower3 = new Sprite(604, 63, this.mTowerTextureRegion, getVertexBufferObjectManager());
		scene.attachChild(mTower1);
		scene.attachChild(mTower2);
		scene.attachChild(mTower3);
		
		// 3 - Create the rings
		Ring ring1 = new Ring(1, 139, 174, this.mRing1, getVertexBufferObjectManager());
		Ring ring2 = new Ring(2, 118, 212, this.mRing2, getVertexBufferObjectManager());
		Ring ring3 = new Ring(3, 97, 255, this.mRing3, getVertexBufferObjectManager());
		scene.attachChild(ring1);
		scene.attachChild(ring2);
		scene.attachChild(ring3); */
		
		//create the healthbar for enemy
		//Sprite enemy_healthEmpty = new Sprite(0, 0, this.mBar1, 100, getVertexBufferObjectManager(), null);
		
		//create the user ball
		final Ball user_ball = new Ball(1, 240-default_size/2, 600, this.mBall1, getVertexBufferObjectManager());
		user_ball.setSize(default_size, default_size);
		this.mMainScene.attachChild(user_ball);
		
		final Enemy enemy1 = new Enemy(100, 3, 240-default_size, 200, this.mEnemy1, getVertexBufferObjectManager());
		enemy1.setSize(default_size*2, default_size*2);
		this.mMainScene.attachChild(enemy1);
		
		
		this.mMainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
				/*
				 * add touch event for swiping ball for movement...
				 * */
				
				
				
				if(pSceneTouchEvent.isActionDown()){
					Log.d("MAct", "DOWN");
					initX = pSceneTouchEvent.getX();
					initY = pSceneTouchEvent.getY();
					
				}else if(pSceneTouchEvent.isActionMove()){
					
				}else if(pSceneTouchEvent.isActionUp()){
					Log.d("MAct", "Up");
					endX = pSceneTouchEvent.getX();
					endY = pSceneTouchEvent.getY();
					
					Log.d("MAct", "init X: " + Float.toString(initX));
					Log.d("MAct", "init Y: " + Float.toString(initY));
					Log.d("MAct", "end X: " + Float.toString(endX));
					Log.d("MAct", "end Y: " + Float.toString(endY));
					
					if(initX != 0 && initY != 0 && endX != 0 && endY != 0){
						user_ball.update(initX, initY, endX, endY);
					}
					initX = 0;
					initY = 0;
					endX=0;
					endY=0;
				}
				
				return false;
			}
		});
		
		//collision
		this.mMainScene.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void reset() { }
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
			//Log.d("USER_BALL", user_ball.getX() + "," + user_ball.getY());				
				//Log.d("MAINACTIVITY", "=======PLAYER HIT ENEMY=======");
				//if user weight is lighter or equal than enemy
				if (user_ball.getmWeight() <= enemy1.getmWeight()){
					//userball moves away
					if (user_ball.collidesWith(enemy1)){
						user_ball.reverseSpeedX();
						user_ball.reverseSpeedY();
					}
				}
			}
		});
		
		return this.mMainScene;
	}
}
