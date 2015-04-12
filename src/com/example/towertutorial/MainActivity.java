package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;

import org.andengine.opengl.texture.ITexture;
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
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import android.util.Log;

public class MainActivity extends SimpleBaseGameActivity {
	static int CAMERA_WIDTH = 480;
	static int CAMERA_HEIGHT = 800;
	private int default_size = 100;
	private ITextureRegion mBackgroundTextureRegion;
	private ITextureRegion mBall1;
	private ITextureRegion mEnemy1;
	private ITextureRegion mBar1, mBar2;
	
	private Player user;
	private Villain enemy;
	
	private Scene mMainScene;
	
	private PhysicsWorld mPhysicsWorld;
	private Camera mCamera;
	
	float initX = 0, initY = 0, endX=0, endY=0;

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
		    
		    barBlank.load();
		    barFull.load();
		    ball1.load();
		    enemy1.load();
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    
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
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,0),false);
		this.mMainScene.registerUpdateHandler(this.mPhysicsWorld);
		
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		this.mMainScene.attachChild(backgroundSprite);
		
		//final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		
		//create the user ball
		//final Ball user_ball = new Ball(1, 240, 600, this.mBall1, getVertexBufferObjectManager());
		//user_ball.setSize(default_size, default_size);
		
		//circle body of the ball
		//Body user_ball_body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, user_ball, BodyType.DynamicBody, objectFixtureDef);
		//mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(user_ball, user_ball_body,true, true));
		user = new Player(1, 240, 600, getVertexBufferObjectManager(), mPhysicsWorld, this.mBall1);
		user.setSize(default_size, default_size); 
		user.createPhysics(mPhysicsWorld);
		this.mMainScene.attachChild(user);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(user, user.getBody(), true, false));
		
		//create enemy
		final int enemyMaxHealth = 100;
		//final Enemy enemy1 = new Enemy(enemyMaxHealth, 3, 240-default_size, 200, this.mEnemy1, getVertexBufferObjectManager());
		enemy = new Villain(3, 240-default_size, 200, getVertexBufferObjectManager(), mPhysicsWorld, this.mEnemy1, enemyMaxHealth);
		enemy.setSize(default_size*2, default_size*2);
		enemy.createPhysics(mPhysicsWorld);
		this.mMainScene.attachChild(enemy);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy, enemy.getBody(), true,false));
		
		//body for enemy
		//Body enemy1_body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, enemy1, BodyType.StaticBody, objectFixtureDef);
		//mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy1, enemy1_body,true, true));
		
		//enemy1.setSize(default_size*2, default_size*2); 
		//this.mMainScene.attachChild(enemy1);
		
		//create the healthbar for enemy
		Sprite enemy_healthEmpty = new Sprite(5, 5, this.mBar1, getVertexBufferObjectManager());
		final Sprite enemy_healthFull = new Sprite(5, 5, this.mBar2, getVertexBufferObjectManager());
		this.mMainScene.attachChild(enemy_healthEmpty);
		this.mMainScene.attachChild(enemy_healthFull);
		
		this.mMainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
				
				/* add touch event for swiping ball for movement...
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
						user.update(initX, initY, endX, endY);
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
				if (user.getmWeight() <= enemy.getmWeight()){
					//userball moves away
					if (user.collidesWith(enemy)){
						//user.getBody().setLinearVelocity(-user.getBody().getLinearVelocity().x,-user.getBody().getLinearVelocity().y );
						//user.reverseSpeedX();
						//user.reverseSpeedY();
						//enemy_healthFull.setWidth(10);
						
						//damage to enemy
						enemy.takeDamage(10);
						//set the width of the size of health bar
						enemy_healthFull.setWidth(enemy_healthFull.getWidth() * enemy.getHealth()/enemy.getMaxHealth());
						
						//rid the enemy if enemy has no more health
						if (enemy.getHealth() <= 0){
							//mMainScene.detachChild(enemy);
							//mMainScene.getChildByTag(pTag);
						}
					}
				}
				
				
			}
		});
		
		createBoundary();
		
		return this.mMainScene;
	}
	
	private void createBoundary() {
		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);

		Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH, 15, this.mEngine.getVertexBufferObjectManager());
		Rectangle ceiling = new Rectangle(0, 0, CAMERA_WIDTH, 15, this.mEngine.getVertexBufferObjectManager());
		
		Rectangle leftWall = new Rectangle(0, 0, 15, CAMERA_HEIGHT, this.mEngine.getVertexBufferObjectManager());
		Rectangle rightWall = new Rectangle(CAMERA_WIDTH-15, 0, 15, CAMERA_HEIGHT, this.mEngine.getVertexBufferObjectManager());

		ground.setColor(new Color(0, 0, 0)); 
		ceiling.setColor(new Color(0, 0, 0));
		leftWall.setColor(new Color(0, 0, 0));
		rightWall.setColor(new Color(0, 0, 0));

		PhysicsFactory.createBoxBody(mPhysicsWorld, ground, BodyType.StaticBody,WALL_FIX);
		PhysicsFactory.createBoxBody(mPhysicsWorld, ceiling, BodyType.StaticBody,WALL_FIX);
		PhysicsFactory.createBoxBody(mPhysicsWorld, leftWall, BodyType.StaticBody,WALL_FIX);
		PhysicsFactory.createBoxBody(mPhysicsWorld, rightWall, BodyType.StaticBody,WALL_FIX);
		
		this.mMainScene.attachChild(ground);
		this.mMainScene.attachChild(ceiling);
		this.mMainScene.attachChild(leftWall);
		this.mMainScene.attachChild(rightWall);

	}

	
    
}
