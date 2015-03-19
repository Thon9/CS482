package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.util.Log;



public class Ball extends GameObject {
	private int mWeight, dx=200, dy=200; 
	float temp = (float) 100.5;
	float intx=0, inty=0;
	float acelx=-20, acely=-5;
	private int hP, attackDmg; 

    public Ball(int weight, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.mWeight = weight;
    }
 
    //get weight
    public int getmWeight() {
        return mWeight;
    }
    
    //bounce the ball in the opposite direction
    public void reverseSpeedX(){
    	intx*=-1;
    	
    	
    }
    public void reverseSpeedY(){
    	inty*=-1;
    } 
    
    public void move(){
    	if(Math.abs(this.mPhysicsHandler.getVelocityX())>10 ||Math.abs(this.mPhysicsHandler.getVelocityY())>10){
    		/*if(this.mPhysicsHandler.getVelocityX()>0){
    			this.mPhysicsHandler.setVelocityX(this.mPhysicsHandler.getVelocityX() + acelx);
    		}else if(this.mPhysicsHandler.getVelocityX()<0){
    			this.mPhysicsHandler.setVelocityX(this.mPhysicsHandler.getVelocityX() - acelx);
    		}
    		
    		if(this.mPhysicsHandler.getVelocityY()>0){
    			this.mPhysicsHandler.setVelocityY(this.mPhysicsHandler.getVelocityY() + acelx);
    		}else if(this.mPhysicsHandler.getVelocityY()<0){
    			this.mPhysicsHandler.setVelocityY(this.mPhysicsHandler.getVelocityY() - acelx);
    		}*/
    		//}else if(this.mPhysicsHandler.getAngularVelocity()<0){
    		//	this.mPhysicsHandler.setAcceleration(-acelx);
    		//}
    		//this.mPhysicsHandler.setAngularVelocity(this.mPhysicsHandler.getAngularVelocity() + acelx);
    		//this.mPhysicsHandler.setVelocity(this.mPhysicsHandler.getVelocityX()+acelx, this.mPhysicsHandler.getVelocityY()+acely);
    		//Log.d("Ball", "AngVel: " + Float.toString(this.mPhysicsHandler.getAngularVelocity()));
    	}
    	
    	if(Math.abs(this.mPhysicsHandler.getVelocityX())<=10 && Math.abs(this.mPhysicsHandler.getVelocityY())<=10){
    		this.mPhysicsHandler.setVelocity(0);
    		this.mPhysicsHandler.setAcceleration(0);
    		this.setInMotion(false);
    		Log.d("Ball", "Vel: 0");
    	}
    	
    	
		OutOfScreen(); 
	}
     
    public void update(float beginX, float beginY, float endX, float endY) {
    	//if(!this.isInMotion()){
	    	float angle = (float) Math.atan2(((endY - beginY)), ((endX - beginX)));
	    	intx = (float) (dx * (Math.cos(angle))); 
	    	inty = (float) (dy * (Math.sin(angle)));
	    	
	    	this.mPhysicsHandler.setVelocity(intx,inty);
	    	this.mPhysicsHandler.setAcceleration(acelx);
	    	this.setInMotion(true);
	    	move(); 
    	//}
    	
    }
    
    private void OutOfScreen() {
    	if (mY >= MainActivity.CAMERA_HEIGHT-this.mHeight){
    		if(this.mPhysicsHandler.getVelocityY()>0){
    			inty=-this.mPhysicsHandler.getVelocityY();
    		} 
    	}
    	
    	else if (mY <= 0) {
    		if(this.mPhysicsHandler.getVelocityY()<0){
    			inty=-this.mPhysicsHandler.getVelocityY();
    		}
    		
    	}
    	this.mPhysicsHandler.setVelocityY(inty);
    	if (mX >= MainActivity.CAMERA_WIDTH - this.mWidth){
    		if(this.mPhysicsHandler.getVelocityX()>0){
    			intx=-this.mPhysicsHandler.getVelocityX();
    		}
    		
    	}else if (mX <= 0) {
    		if(this.mPhysicsHandler.getVelocityX()<0){
    			intx=-this.mPhysicsHandler.getVelocityX(); 
    		} 
    		
    	}
    	this.mPhysicsHandler.setVelocityX(intx);
    	
    }
}
