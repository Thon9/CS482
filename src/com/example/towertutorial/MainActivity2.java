package com.example.towertutorial;

import java.io.InputStream;
import java.io.IOException;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.particle.ParticleSystem;
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
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.Entity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity2 extends SimpleBaseGameActivity {
	static int CAMERA_WIDTH = 480;
	static int CAMERA_HEIGHT = 800;
	private int default_size = 100;
	private ITextureRegion mBackgroundTextureRegion;
	private ITextureRegion mBall1;
	private ITextureRegion mEnemy1, mEnemy2;
	private ITextureRegion mBar1, mBar2;

    private Player user;
	private Villain enemy,enemy2;
	
	private Rectangle ground;
	private Rectangle ceiling;
	
	private Rectangle leftWall; 
	private Rectangle rightWall;

	
	private Scene mMainScene;
	
	private ParticleSystem<Sprite> enemyAttackAnim;
	private ParticleSystem<Sprite> enemyAttackAnim2;
    private boolean canEnemyAttack = false;
    private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mParticleTextureRegion;
	private boolean hit = false;
	private boolean hit2 = false;
	
	//private PhysicsWorld mPhysicsWorld;
	private FixedStepPhysicsWorld mPhysicsWorld;
	private Camera mCamera;
	
	private float initX = 0, initY = 0, endX=0, endY=0;
	private Sound hitballSFX;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		//return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
		   // new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
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
		    
		  
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    
		    barBlank.load();
		    barFull.load();
		    ball1.load();
		    enemy1.load();
		    

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
		    this.mEnemy2 = mEnemy1;
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
		
		
		//circle body of the ball
		final int playerMaxHealth = 200;
		user = new Player(1, 240, 600, getVertexBufferObjectManager(), mPhysicsWorld, this.mBall1, playerMaxHealth);
		user.setSize(default_size, default_size); 
		user.createPhysics(mPhysicsWorld);
		this.mMainScene.attachChild(user);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(user, user.getBody(), true, false));
		 
		//create enemy
		final int enemyMaxHealth = 100;
		//final Enemy enemy1 = new Enemy(enemyMaxHealth, 3, 240-default_size, 200, this.mEnemy1, getVertexBufferObjectManager());
		enemy = new Villain(3, 100-default_size, 50, getVertexBufferObjectManager(), mPhysicsWorld, this.mEnemy1, enemyMaxHealth);
		enemy.setSize(default_size*2, default_size*2);
		enemy.createPhysics(mPhysicsWorld);
		this.mMainScene.attachChild(enemy);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy, enemy.getBody(), true,false));
		
		//2nd enemy
		enemy2 = new Villain(3, 400-default_size, 50, getVertexBufferObjectManager(), mPhysicsWorld, this.mEnemy2, enemyMaxHealth);
		enemy2.setSize(default_size*2, default_size*2);
		enemy2.createPhysics(mPhysicsWorld);
		this.mMainScene.attachChild(enemy2);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(enemy2, enemy2.getBody(), true,false));
		
		
		enemyAttackAnim = enemy.Attack(this.mParticleTextureRegion);
		enemyAttackAnim.setParticlesSpawnEnabled(false);
		
		enemyAttackAnim2 = enemy2.Attack(this.mParticleTextureRegion);
		enemyAttackAnim2.setParticlesSpawnEnabled(false);
		
		this.mMainScene.attachChild(enemyAttackAnim);
		this.mMainScene.attachChild(enemyAttackAnim2);
		
		
		//create the healthbar for enemy
		final Sprite enemy_healthEmpty = new Sprite(5, 5, this.mBar1, getVertexBufferObjectManager());
		final Sprite enemy_healthFull = new Sprite(5, 5, this.mBar2, getVertexBufferObjectManager());
		this.mMainScene.attachChild(enemy_healthEmpty);
		this.mMainScene.attachChild(enemy_healthFull);
		final float full_width = enemy_healthFull.getWidth();
		
		//health bar for player
		final Sprite player_healthEmpty = new Sprite(-210, 530, this.mBar1, getVertexBufferObjectManager());
		final Sprite player_healthFull = new Sprite(-210, 530, this.mBar2, getVertexBufferObjectManager());
		player_healthEmpty.setRotation(90);
		player_healthFull.setRotation(270); 
		this.mMainScene.attachChild(player_healthEmpty);
		this.mMainScene.attachChild(player_healthFull);
		//final float full_width = player_healthFull.getWidth();
		
		//create the walls 
		createBoundary();
		
		//music and sound
		try {
			this.hitballSFX = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "sfx/hitball.mp3");
		} catch (final IOException e) {
			Debug.e("Error", e);
		}
	    
		
		this.mMainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
				
				/* add touch event for swiping ball for movement...
				 * */
				
				//end level if no enemies
				if (enemy.getHealth() <= 0 && enemy2.getHealth() <= 0){
					endLevel();
				}
				
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
						enemyAttackAnim2.setParticlesSpawnEnabled(true);
						mMainScene.registerUpdateHandler((IUpdateHandler) new TimerHandler(5, new ITimerCallback() {
						    @Override
						    public void onTimePassed(final TimerHandler pTimerHandler) {
						    	enemyAttackAnim.setParticlesSpawnEnabled(false);
						    	enemyAttackAnim2.setParticlesSpawnEnabled(false);
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
		        	   hit2 = false;
		        	   canEnemyAttack = true;
		        }
				 
				//collide with enemy1
				if (enemy.collidesWithCircle(user)){
					enemy.takeDamage(30); 
					
					//set the width of the size of health bar
					if (enemy.getHealth() > 0){
						hitballSFX.play();
						enemy_healthFull.setWidth(enemy_healthFull.getWidth()*(enemy.getHealth()+enemy2.getHealth())/(enemy.getMaxHealth()+enemy2.getHealth()));
					}
					//remove the enemy
					if (enemy.getHealth() <= 0){
						//hitballSFX.play();
                		mPhysicsWorld.unregisterPhysicsConnector(new PhysicsConnector(enemy, enemy.getBody(), true,false));
						enemy.getBody().setActive(false);
						mMainScene.detachChild(enemy);
					}
				}
				 
				if (enemy2.collidesWithCircle(user)){
					enemy2.takeDamage(30); 
					
					//set the width of the size of health bar
					if (enemy2.getHealth() > 0){
						hitballSFX.play();
						enemy_healthFull.setWidth(enemy_healthFull.getWidth()*(enemy.getHealth()+enemy2.getHealth())/(enemy.getMaxHealth()+enemy2.getHealth()));
					}
					//remove the enemy
					if (enemy2.getHealth() <= 0){
						//hitballSFX.play();
                		mPhysicsWorld.unregisterPhysicsConnector(new PhysicsConnector(enemy2, enemy2.getBody(), true,false));
						enemy2.getBody().setActive(false);
						mMainScene.detachChild(enemy2);
					}
				}
				
				//rid the enemy if enemy has no more health
				if (enemy.getHealth() <= 0 && enemy2.getHealth() <= 0){
					//mMainScene.detachChild(enemy);
					//mMainScene.getChildByTag(pTag);
					//enemy.setHealth(enemyMaxHealth);
					//enemy2.setHealth(enemyMaxHealth);
					//enemy_healthFull.setWidth(full_width);
					endLevel();
				}
				
				if (hitWall(user) == true){
					hitballSFX.play();
				}
				
				Rectangle r = new Rectangle(enemy.getmCampFireEmitter().getCenterX()-enemy.getmCampFireEmitter().getWidth()/2, enemy.getmCampFireEmitter().getCenterY() - enemy.getmCampFireEmitter().getHeight()/2, enemy.getmCampFireEmitter().getWidth(), 50, getVertexBufferObjectManager());
				
				Rectangle r2 = new Rectangle(enemy2.getmCampFireEmitter().getCenterX()-enemy2.getmCampFireEmitter().getWidth()/2, enemy2.getmCampFireEmitter().getCenterY() - enemy2.getmCampFireEmitter().getHeight()/2, enemy2.getmCampFireEmitter().getWidth(), 50, getVertexBufferObjectManager());
				
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
				
				if(user.collidesWith(r2) && hit2==false && enemyAttackAnim2.isParticlesSpawnEnabled()==true){
					hit2 = true;
					user.takeDamage(20);
					player_healthFull.setWidth(player_healthFull.getWidth() * user.getHealth()/user.getMaxHealth());
					if (user.getHealth() <= 0){
						user.takeDamage(-playerMaxHealth);
						player_healthFull.setWidth(full_width);
						endLevel();
					}
				}
				
				
			}
		});
		
		//createBoundary();
		
		return this.mMainScene;
	}
	
	private void createBoundary() {
		FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);

		this.ground = new Rectangle(0, CAMERA_HEIGHT - 15, CAMERA_WIDTH, 15, this.mEngine.getVertexBufferObjectManager());
		this.ceiling = new Rectangle(0, 0, CAMERA_WIDTH, 15, this.mEngine.getVertexBufferObjectManager());
		
		this.leftWall = new Rectangle(0, 0, 15, CAMERA_HEIGHT, this.mEngine.getVertexBufferObjectManager());
		this.rightWall = new Rectangle(CAMERA_WIDTH-15, 0, 15, CAMERA_HEIGHT, this.mEngine.getVertexBufferObjectManager());

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
	
	//if user hit wall
	public boolean hitWall(Player user){
		if (user.collidesWith(ceiling) || user.collidesWith(ground) || user.collidesWith(leftWall) || user.collidesWith(rightWall)){
			return true;
		}
		return false;
	}

	//end level
	public void endLevel(){
		//pause the cur rent game
		gameToast("Game Over");

		mMainScene.setIgnoreUpdate(true);
		
		this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity2.this);
                alert.setTitle("GameOver");
                alert.setMessage("Congrats");
                //next level
                alert.setPositiveButton("Next", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    	//next level
                    	gameToast("Coming Soon");
                    }
                });
                //reset level
                alert.setNeutralButton("Reset", new OnClickListener() {
                	@Override
                    public void onClick(DialogInterface arg0, int arg1) {
                		mMainScene.setIgnoreUpdate(false);
                		
                    	Intent gameIntent = new Intent(MainActivity2.this, MainActivity2.class);
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
                    	
                    	
                    	Intent gameIntent = new Intent(MainActivity2.this, MainMenuActivity.class);
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
	           Toast.makeText(MainActivity2.this, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
}
