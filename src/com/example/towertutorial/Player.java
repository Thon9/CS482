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

public class Player extends Sprite{
	private Body body;
	private float intx=0, inty=0;
	private int mWeight, dx=10, dy=10;
	private PhysicsHandler mPhysicsHandler;
	private boolean inMotion;
	
	public Player(int weight, float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld, ITextureRegion txtureReg)//, ITiledTextureRegion txtureReg )
	{
	    super(pX, pY, txtureReg, vbo);
	    
	    this.mWeight = weight;
	    //createPhysics(physicsWorld);
	    //camera.setChaseEntity(this);
	}
	
	//get weight
    public int getmWeight() {
        
		return mWeight;
    }
	
	
	public void createPhysics(PhysicsWorld physicsWorld)
	{        
	    //body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
	    //Body user_ball_body = PhysicsFactory.createCircleBody(this.mPhysicsWorld, user_ball, BodyType.DynamicBody, objectFixtureDef);
		body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(10.0f, 1.0f, 0.0f));
	    body.setUserData("player");
	    body.setFixedRotation(true);
	    this.mPhysicsHandler = new PhysicsHandler(this);
	    this.registerUpdateHandler(this.mPhysicsHandler);
	    
	    this.inMotion = false;
	    
	    physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
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
	
	public void update(float beginX, float beginY, float endX, float endY) {
    	//if(!this.isInMotion()){
	    	float angle = (float) Math.atan2(((endY - beginY)), ((endX - beginX)));
	    	intx = (float) (dx * (Math.cos(angle))); 
	    	inty = (float) (dy * (Math.sin(angle)));
	    	
	    	//this.mPhysicsHandler.setVelocity(intx,inty);
	    	body.setLinearVelocity(intx,  inty);
	    	//this.mPhysicsHandler.setAcceleration(acelx);
	    	//this.setInMotion(true);
	    	//move(); 
    	//}
    	
    }
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public boolean isInMotion() {
		return inMotion;
	}

	public void setInMotion(boolean inMotion) {
		this.inMotion = inMotion;
	}
} 
