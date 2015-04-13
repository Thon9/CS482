package com.example.towertutorial;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Villain extends Sprite{
	private Body body;
	private int mWeight;
	private PhysicsHandler mPhysicsHandler;
	
	private int health,maxHealth;
	
	public Villain(int weight, float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld, ITextureRegion txtureReg, int health)//, ITiledTextureRegion txtureReg )
	{
	    super(pX, pY, txtureReg, vbo);
	    
	    this.mWeight = weight;
	    this.health = health;
        this.maxHealth = health;
	    //createPhysics(physicsWorld);
	    //camera.setChaseEntity(this);
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
	    final float radius1 = this.getWidth()/2 ;
	    final float radius2 = circle.getWidth()/2;

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
}
