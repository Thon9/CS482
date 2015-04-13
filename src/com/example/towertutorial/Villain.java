package com.example.towertutorial;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
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
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.StaticBody, PhysicsFactory.createFixtureDef(1, 0.5f, 0.5f));
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
	

	public Body getBody() {
		return body;
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
