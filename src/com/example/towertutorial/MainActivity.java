package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;

import java.util.Stack;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
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
import android.view.Menu;

public class MainActivity extends SimpleBaseGameActivity {
	static int CAMERA_WIDTH = 480;
	static int CAMERA_HEIGHT = 800;
	private int default_size = 100;
	private ITextureRegion mBackgroundTextureRegion, mTowerTextureRegion, mRing1, mRing2, mRing3;
	private ITextureRegion mBall1;
	private Sprite mTower1, mTower2, mTower3;
	private Stack mStack1, mStack2, mStack3;
	
	private Scene mMainScene;
	private Camera mCamera;
	
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
		    ITexture ball1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/dragon_ball.png");
		        }
		    });
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    towerTexture.load();
		    ring1.load();
		    ring2.load();
		    ring3.load();
		    ball1.load();
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.mTowerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
		    this.mRing1 = TextureRegionFactory.extractFromTexture(ring1);
		    this.mRing2 = TextureRegionFactory.extractFromTexture(ring2);
		    this.mRing3 = TextureRegionFactory.extractFromTexture(ring3);
		    this.mBall1 = TextureRegionFactory.extractFromTexture(ball1);
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
		
		//create the user ball
		Ball user_ball = new Ball(1, 240-default_size/2, 600, this.mBall1, getVertexBufferObjectManager());
		user_ball.setSize(default_size, default_size);
		this.mMainScene.attachChild(user_ball);
		return this.mMainScene;
	}
	
	
    
}