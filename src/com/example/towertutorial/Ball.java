package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class Ball extends GameObject {
	private int mWeight, dx=200, dy=200; 
	float temp = (float) 100.5;
	float intx=0, inty=0;
	float acelx=-5, acely=-5;
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
    	
		//this.mPhysicsHandler.setVelocityX(dx);
		//this.mPhysicsHandler.setVelocityY(dy);
		//this.mPhysicsHandler.setv
    	//this.mPhysicsHandler.setVelocity(intx, inty);
    	//if(this.mPhysicsHandler.)
    	/*if(this.mPhysicsHandler.getVelocityX()!=0){
    		this.mPhysicsHandler.setAccelerationX(acelx);
		}else{
			this.mPhysicsHandler.setAccelerationX((float) 0);
		}
    	
		if(this.mPhysicsHandler.getVelocityY()!=0){
			this.mPhysicsHandler.setAccelerationY(acely);
		}else{
			this.mPhysicsHandler.setAccelerationY((float) 0);
		}*/
    	//this.mPhysicsHandler.setAcceleration((float) -5);
		/*if(this.mPhysicsHandler.getVelocityX()>0){
			intx-=0.0005;
		}else{
			intx=0;
		}
		if(this.mPhysicsHandler.getVelocityY()>0){
			inty-=0.0005;
		}else{
			inty = 0;
		}*/
		OutOfScreen(); 
	}
     
    public void update(float beginX, float beginY, float endX, float endY) {
    	//l
    	float slope = (endY - beginY) / (endX - beginX);
    	float angle = (float) Math.atan(slope);
    	intx = (float) (dx * (Math.cos(angle))); 
    	inty = (float) (dy * (Math.sin(angle)));
    	
    	
    	if (beginX > endX && beginY > endY) {
    		this.mPhysicsHandler.setVelocity(intx,inty);

    	}
    	else if (endX > beginX && endY > beginY) { 
    		this.mPhysicsHandler.setVelocity(intx,inty);
    	}
    	else if (endX > beginX && beginY > endY) {
    		this.mPhysicsHandler.setVelocity(intx,inty);
    	}
    	else if (beginX > endX && endY > beginY) {
    		this.mPhysicsHandler.setVelocity(intx,inty); 
    	}else{
    		this.mPhysicsHandler.setVelocity(intx,inty);
    	}
    	move(); 
    	
    }
    
    private void OutOfScreen() {
    	if (mY >= MainActivity.CAMERA_HEIGHT-this.mHeight){
    		if(this.mPhysicsHandler.getVelocityY()>0){
    			inty*=-1;
    			acely*=5; 
    		} 
    	}
    	
    	else if (mY <= 0) {
    		if(this.mPhysicsHandler.getVelocityY()<0){
    			inty*=-1;
    			acely*=-5;
    		}
    		
    	}
    	this.mPhysicsHandler.setVelocityY(inty);
    	if (mX >= MainActivity.CAMERA_WIDTH - this.mWidth){
    		if(this.mPhysicsHandler.getVelocityX()>0){
    			intx*=-1;
    			acelx*=-5;
    		}
    		
    	}else if (mX <= 0) {
    		if(this.mPhysicsHandler.getVelocityX()<0){
    			intx*=-1; 
    			acelx*=5;
    		} 
    		
    	}
    	this.mPhysicsHandler.setVelocityX(intx);
    	
    }
}
