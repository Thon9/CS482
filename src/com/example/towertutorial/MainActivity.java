package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.modifier.ColorBackgroundModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.shape.IShape;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;


import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification.Action;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.Entity;
import android.util.Log;
import android.widget.Toast;


import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.IParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;

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
	private Sound hitballSFX;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mParticleTextureRegion;
    
    private ParticleSystem<Sprite> enemyAttackAnim;
    private boolean canEnemyAttack = false;
    
   
	
	
	//private PhysicsWorld mPhysicsWorld;
	private FixedStepPhysicsWorld mPhysicsWorld;
	private Camera mCamera;
	
	private float initX = 0, initY = 0, endX=0, endY=0;
	
	private boolean hit = false;

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
		//    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
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
		    
		  //enemy ball
		    ITexture deadenemy1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/monster1dead.png");
		        }
		    });
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    
		    barBlank.load();
		    barFull.load();
		    ball1.load();
		    enemy1.load();
		    deadenemy1.load();
		    
			this.mBitmapTextureAtlas = new BitmapTextureAtlas(null, 32, 32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	        this.mParticleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "particle_fire.png", 0, 0);
	        this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
			
		    
		    
		    
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
		this.mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0,0),false);
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
		final int playerMaxHealth = 200;
		user = new Player(1, 240, 600, getVertexBufferObjectManager(), mPhysicsWorld, this.mBall1, playerMaxHealth);
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
		
		
		enemyAttackAnim = enemy.Attack(this.mParticleTextureRegion);
		enemyAttackAnim.setParticlesSpawnEnabled(false);
		
		this.mMainScene.attachChild(enemyAttackAnim);
		
		//body for enemy
		//Body enemy1_body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, enemy1, BodyType.StaticBody, objectFixtureDef);
		//mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy1, enemy1_body,true, true));
		
		//enemy1.setSize(default_size*2, default_size*2); 
		//this.mMainScene.attachChild(enemy1);
		
		//create the healthbar for enemy
		final Sprite enemy_healthEmpty = new Sprite(5, 5, this.mBar1, getVertexBufferObjectManager());
		final Sprite enemy_healthFull = new Sprite(5, 5, this.mBar2, getVertexBufferObjectManager());
		this.mMainScene.attachChild(enemy_healthEmpty);
		this.mMainScene.attachChild(enemy_healthFull);
		final float full_width = enemy_healthFull.getWidth();
		
		//health player bar
		final Sprite player_healthEmpty = new Sprite(-210, 530, this.mBar1, getVertexBufferObjectManager());
		final Sprite player_healthFull = new Sprite(-210, 530, this.mBar2, getVertexBufferObjectManager());
		player_healthEmpty.setRotation(90);
		player_healthFull.setRotation(270);
		this.mMainScene.attachChild(player_healthEmpty);
		this.mMainScene.attachChild(player_healthFull);
		
		//music and sound
		try {
			this.hitballSFX = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sfx/hitball.mp3");
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
		
		this.mMainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
				
				//end level if no enemies
				if (enemy.getHealth() <= 0){
					endLevel();
				}
				
				/* add touch event for swiping ball for movement...
				 * */

				if(pSceneTouchEvent.isActionDown()){
					initX = pSceneTouchEvent.getX();
					initY = pSceneTouchEvent.getY();
					
				}else if(pSceneTouchEvent.isActionMove()){
					
				}else if(pSceneTouchEvent.isActionUp()){
					endX = pSceneTouchEvent.getX();
					endY = pSceneTouchEvent.getY();
					
					if(initX != 0 && initY != 0 && endX != 0 && endY != 0){
						user.update(initX, initY, endX, endY);
						canEnemyAttack = true;
					}
					
					
					if(canEnemyAttack==true){
						//enemyAttackAnim = enemy.Attack(mParticleTextureRegion);
						//particleBody = PhysicsFactory.createCircleBody(mPhysicsWorld, enemyAttackAnim.getChildByIndex(0).getX(), enemyAttackAnim.getChildByIndex(0).getY(), 100, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0.0f, .0f, 0.0f));
						enemyAttackAnim.setParticlesSpawnEnabled(true);
						mMainScene.registerUpdateHandler((IUpdateHandler) new TimerHandler(5, new ITimerCallback() {
						    @Override
						    public void onTimePassed(final TimerHandler pTimerHandler) {
						    	enemyAttackAnim.setParticlesSpawnEnabled(false);
						    	//enemyAttackAnim = new SpriteParticleSystem(null, 0, 0, 0, null, null);
						    }
						}));
					}
					
					initX = 0;
					initY = 0;
					endX=0;
					endY=0; 
					
					canEnemyAttack = false;
					
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
				
				if(user.isInMotion() == true && Math.abs(user.getBody().getLinearVelocity().x) <=0.4 && Math.abs(user.getBody().getLinearVelocity().y) <=0.4){
		        	   user.setInMotion(false);
		        	   user.getBody().setLinearVelocity(0, 0);
		        	   hit = false;
		        	   canEnemyAttack = true;
		        }
				 
				if (user.getmWeight() <= enemy.getmWeight()){
					//userball moves away
					
					if (enemy.collidesWithCircle(user)){
						enemy.takeDamage(20); 
						hitballSFX.play(); 
						//set the width of the size of health bar
						enemy_healthFull.setWidth(enemy_healthFull.getWidth() * enemy.getHealth()/enemy.getMaxHealth());
						
						//rid the enemy if enemy has no more health
						if (enemy.getHealth() <= 0){
							//mMainScene.detachChild(enemy);
							//mMainScene.getChildByTag(pTag);
							//enemy.setColor(0.69f, 0.69f, 0.69f);
							enemy.takeDamage(-enemyMaxHealth);
							enemy_healthFull.setWidth(full_width);
							endLevel();
						}
					}
					Rectangle r = new Rectangle(enemy.getmCampFireEmitter().getCenterX()-enemy.getmCampFireEmitter().getWidth()/2, enemy.getmCampFireEmitter().getCenterY() - enemy.getmCampFireEmitter().getHeight()/2, enemy.getmCampFireEmitter().getWidth(), 50, getVertexBufferObjectManager());
					
					if(user.collidesWith(r) && hit==false && enemyAttackAnim.isParticlesSpawnEnabled()==true){
						hit = true;
						user.takeDamage(20);
						player_healthFull.setWidth(player_healthFull.getWidth() * user.getHealth()/user.getMaxHealth());
						if (user.getHealth() <= 0){
							user.takeDamage(-playerMaxHealth);
							player_healthFull.setWidth(full_width);
							endLevel();
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

	//end level
	public void endLevel(){
		//pause the cur rent game
		gameToast("Game Over");

		mMainScene.setIgnoreUpdate(true);
		
		this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("GameOver");
                alert.setMessage("Congrats");
                //next level
                alert.setPositiveButton("Next", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    	//next level
                    	Intent gameIntent = new Intent(MainActivity.this, MainActivity2.class);
    	            	startActivity(gameIntent);
                    	finish();
                    	gameToast("Level 2");
                    }
                });
                //reset level
                alert.setNeutralButton("Reset", new OnClickListener() {
                	@Override
                    public void onClick(DialogInterface arg0, int arg1) {
                		/*mMainScene.setIgnoreUpdate(false);
                		mMainScene.detachChild(user);
                		user = new Player(1, 240, 600, getVertexBufferObjectManager(), mPhysicsWorld, mBall1);
                		user.setSize(default_size, default_size); 
                		user.createPhysics(mPhysicsWorld);
                		mMainScene.attachChild(user);
                		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(user, user.getBody(), true, false));
                		*/
                		Intent gameIntent = new Intent(MainActivity.this, MainActivity.class);
    	            	startActivity(gameIntent);
                    	finish();
                		gameToast("Game Reset");
                    }
                });
                
                //end game
                alert.setNegativeButton("Exit", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    	//exit the game
                    	gameToast("Game Exit");
                    	
                    	Intent gameIntent = new Intent(MainActivity.this, MainMenuActivity.class);
    	            	startActivity(gameIntent);
                    	finish();
                    }
                });

                alert.show();
            }
        });
		
	}
	
	//display messages
	public void gameToast(final String msg) {
	    this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	           Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
}
