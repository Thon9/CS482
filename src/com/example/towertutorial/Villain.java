package com.example.towertutorial;

import java.util.Random;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
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
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.graphics.drawable.shapes.OvalShape;
import android.opengl.GLES20;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;



public class Villain extends Sprite{
	private Body body;
	private int mWeight;
	private PhysicsHandler mPhysicsHandler;
	
	private int health,maxHealth;
	
	 
    private RectangleParticleEmitter mCampFireEmitter;
    private ParticleSystem<Sprite> mCampFireParticleSystem;
    
	
	
	private static final float RATE_MIN = 8; 
    private static final float RATE_MAX = 12;
    private static final int PARTICLES_MAX = 200;
    
    private ColorParticleInitializer<Sprite> cI;
    private ColorParticleModifier<Sprite> cM;
	
    private Random r;
    
	public Villain(int weight, float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld, ITextureRegion txtureReg, int health)//, ITiledTextureRegion txtureReg )
	{
	    super(pX, pY, txtureReg, vbo);
	    
	    this.r = new Random();
	    
	    this.mWeight = weight;
	    this.health = health;
        this.maxHealth = health;
	    //createPhysics(physicsWorld);
	    //camera.setChaseEntity(this);
        /*Color attackCol;
        switch(r.nextInt(3)){
			case(0):{
				attackCol = Color.BLUE; 
				break;
			}
			case(1):{
				attackCol = Color.RED;
				break;
			}
			case(2):{
				attackCol = Color.GREEN;
				break;
			}
	        default:{
	        	attackCol = Color.BLUE; 
	        }
        }
        
        cI = new ColorParticleInitializer<Sprite>(attackCol);*/
        //cM = new ColorParticleModifier<Sprite>()
        
        
	}
	
	public ParticleSystem<Sprite> Attack(ITextureRegion mParticleTextureRegion){
		
		int tempy = r.nextInt(MainActivity.CAMERA_HEIGHT - 100);
		int tempx = r.nextInt(MainActivity.CAMERA_WIDTH - 100);
		
	        mCampFireEmitter = new RectangleParticleEmitter(tempx, tempy, 50,0);
	        
	        mCampFireParticleSystem = new SpriteParticleSystem(mCampFireEmitter, RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion, getVertexBufferObjectManager());

	        mCampFireParticleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(7.0f, 8.0f));

	        mCampFireParticleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-2.0f, 2.0f, -10.0f, -19.0f));

	        mCampFireParticleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA));

	        mCampFireParticleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0.0f, 6.99f, 1.0f, 0.5f));

	        mCampFireParticleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.51f, 0.0f, 0.0f));

	        mCampFireParticleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(1.0f, 2.0f, 1.0f, 0.50f, 0.51f, 0.50f, 0.0f, 0.50f));

	        mCampFireParticleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0.0f, 6.99f, 1.0f, 0.50f));

		/*RectangleParticleEmitter pParticleEmitter = new RectangleParticleEmitter(this.getX()+this.mWidth/2, this.getY()+this.mHeight/2, 50, 0);//MainActivity.CAMERA_WIDTH/2, MainActivity.CAMERA_HEIGHT/2, 50,0)
		//particleEmitterRect = new Rectangle(this.getX()+this.mWidth/2, this.getY()+this.mHeight/2,50,50, getVertexBufferObjectManager());
		
		SpriteParticleSystem particleSystem = new SpriteParticleSystem(pParticleEmitter, RATE_MIN, RATE_MAX, PARTICLES_MAX, mParticleTextureRegion, getVertexBufferObjectManager());
		
		
		
		//final ParticleSystem particleSystem = new ParticleSystem(recFact,new PointParticleEmitter(-32, CAMERA_HEIGHT - 32), RATE_MIN, RATE_MAX, PARTICLES_MAX, this.mParticleTextureRegion);
        //particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		particleSystem.addParticleInitializer((IParticleInitializer<Sprite>) new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA));
		
		
		
		switch(r.nextInt(4)){
			case(0):{
				//down to the left
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0, -10, 0, -10 ));
				particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, 11));
		        break;
			}
			case(1):{
				//down to the right
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0, 10, 0, -10 ));
		        particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, 11));
		        break;
			}
			case(2):{
		        //up to the right
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0, 10, 0, 10 ));
				particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(5, -11));
		        break;
			}
			case(3):{
		        //up to the left
				particleSystem.addParticleInitializer(new VelocityParticleInitializer<Sprite>(0, -10, 0, 10 ));
				particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Sprite>(-5, -11));
		        break;
			}
			default:{}
		}
        
		particleSystem.addParticleInitializer(new RotationParticleInitializer<Sprite>(0.0f, 360.0f));
        particleSystem.addParticleInitializer(cI);

        particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0.5f, 2.0f, 0, 5));
        particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6.5f));
        //particleSystem2.addParticleModifier(new ColorParticleModifier<Sprite>(1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.5f, 5.5f));
        //particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        //particleSystem2.addParticleModifier(new ColorParticleModifier<Sprite>(1.0f, 2.0f, 1.0f, 0.50f, 0.51f, 0.50f, 0.0f, 0.50f));
        particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(1.0f, 0.0f, 2.5f, 6.5f));

		 */
        
        return mCampFireParticleSystem;

	}
	 
	public void createPhysics(PhysicsWorld physicsWorld)
	{        
	    //body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
	    //Body user_ball_body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, user_ball, BodyType.DynamicBody, objectFixtureDef);
		FixtureDef area = PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f);
		//area.isSensor=true;
		
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.StaticBody, area);
	    body.setUserData("villain");
	    body.setFixedRotation(true);
	    this.mPhysicsHandler = new PhysicsHandler(this);
	    this.registerUpdateHandler(this.mPhysicsHandler);
	    
	    
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
	     /*{
	        @Override
	        public void onUpdate(float pSecondsElapsed)
	        {
	            //super.onUpdate(pSecondsElapsed);
	            //camera.onUpdate(0.1f);
	            
	            //if (getY() <= 0)
	           //{                    
	                //onDie();
	           // }
	            
	            //if (canRun)
	           // {    
	                //body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y)); 
	            //}
	        }
	    });*/
	}
	
	//collidion with player
	/*boolean collidesWithCircle(Sprite circle){
		float x1 = this.getX();
		float y1 = this.getY();
		float x2 = this.getX();
		float y2 = this.getY();
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dis = (dx*dx) + (dy*dy);
		
		if (dis <= this.getWidth()*this.getWidth()){
			return true;
		}
		
		return false;
	}
	*/
	boolean collidesWithCircle(Player circle){
	    final float x1 = this.getX() + this.getWidth()/2;
	    final float y1 = this.getY() + this.getWidth()/2;
	    final float x2 = circle.getX()  + circle.getWidth()/2;
	    final float y2 = circle.getY()  + circle.getWidth()/2;
	    final float xDifference = x2 - x1;
	    final float yDifference = y2 - y1;

	    // The ideal would be to provide a radius, but as
	    // we assume they are perfect circles, half the
	    // width will be just as good
	    final float radius1 = this.getWidth()/2 +1;
	    final float radius2 = circle.getWidth()/2 +1;

	    // Note we are using inverseSqrt but not normal sqrt,
	    // please look below to see a fast implementation of it.
	    // Using normal sqrt would not need "1.0f/", is more precise
	    // but less efficient
	    final float euclideanDistance = 1.0f/inverseSqrt(
	                                            xDifference*xDifference +
	                                            yDifference*yDifference);
	    return euclideanDistance < (radius1+radius2);
	}

	public Body getBody() {
		return body;
	}
	
	public static float inverseSqrt(float x) {
	    float xhalf = 0.5f*x;
	    int i = Float.floatToIntBits(x);
	    i = 0x5f3759df - (i>>1);
	    x = Float.intBitsToFloat(i);
	    x = x*(1.5f - xhalf*x*x);
	    return x;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public int getmWeight() {
		return mWeight;
	}

	public void setmWeight(int mWeight) {
		this.mWeight = mWeight;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void takeDamage(int damage) {
    	this.health = this.health - damage;
    }

	public RectangleParticleEmitter getmCampFireEmitter() {
		return mCampFireEmitter;
	}

}
