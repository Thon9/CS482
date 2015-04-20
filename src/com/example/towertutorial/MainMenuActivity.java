package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;
import java.security.spec.PSSParameterSpec;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.modifier.ColorBackgroundModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;


import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification.Action;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Entity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MainMenuActivity extends SimpleBaseGameActivity {
	static int CAMERA_WIDTH = 480;
	static int CAMERA_HEIGHT = 800;
	private int default_size = 100;
	private ITextureRegion mBackgroundTextureRegion;
	
	private ITextureRegion mPlayButton;
	private Player user;
	private Villain enemy;
	
	private Scene mMainScene;
	
	//private PhysicsWorld mPhysicsWorld;
	private FixedStepPhysicsWorld mPhysicsWorld;
	private Camera mCamera;
	
	private float initX = 0, initY = 0, endX=0, endY=0;
	
	private boolean hit = false;

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
	    
		    //play button
		    ITexture playButton = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/playgame-button.png");
		        }
		    });
		    
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    
		    
		    playButton.load();
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		
		    this.mPlayButton = TextureRegionFactory.extractFromTexture(playButton);
		    
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		
	}


	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		
		this.mMainScene = new Scene();
		this.mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0,0),false);
		this.mMainScene.registerUpdateHandler(this.mPhysicsWorld);
		
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		this.mMainScene.attachChild(backgroundSprite);
		
		//create the play button
		final ButtonSprite menu_playbutton = new ButtonSprite(240 - mPlayButton.getWidth()/2, 200, this.mPlayButton, getVertexBufferObjectManager()){
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
                    float pTouchAreaLocalX, float pTouchAreaLocalY) {
	                    if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
	                    	
	                    	//switch activities to main game
	                    	Intent gameIntent = new Intent(MainMenuActivity.this, MainActivity.class);
	    	            	startActivity(gameIntent);
	    	            	finish();
	                    }

                return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
            }
        };
        
        this.mMainScene.registerTouchArea(menu_playbutton);
		
		this.mMainScene.attachChild(menu_playbutton);		
		
		
		this.mMainScene.registerUpdateHandler(new IUpdateHandler(){
			@Override
			public void reset() { }
			 
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
			}
		});
		
		return this.mMainScene;
	}
	
	
	//display messages
	public void gameToast(final String msg) {
	    this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	           Toast.makeText(MainMenuActivity.this, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
}
