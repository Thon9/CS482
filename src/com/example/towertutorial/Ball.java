package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class Ball extends GameObject {
	private int mWeight, mdx, mdy, dx=100, dy=100;
	float temp = (float) 100.5;
	private int hP, attackDmg; 

    public Ball(int weight, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.mWeight = weight;
        this.mdx=1;
        this.mdy=1;
        //this.dx=0;
        //this.dy=0;
    }
 
    public int getmWeight() {
        return mWeight;
    }
    
    public void move(){
    	
		//this.mPhysicsHandler.setVelocityX(dx);
		//this.mPhysicsHandler.setVelocityY(dy);
    	//this.mPhysicsHandler.setVelocity(temp);
		if(this.mPhysicsHandler.getAngularVelocity()>0){
			this.mPhysicsHandler.setAcceleration((float)-5.0);
		}else{
			this.mPhysicsHandler.setAcceleration((float)0.0);
		}
		OutOfScreen();
	}
    
    public void update(float beginX, float beginY, float endX, float endY) {
    	
    	float slope = (endY - beginY) / (endX - beginX);
    	float angle = (float) Math.atan(slope);
    	
    	if (beginX > endX && beginY > endY) {
    		this.mPhysicsHandler.setVelocity(-(float) (dx * (Math.cos(angle))),-(float) (dy * (Math.sin(angle))));

    	}
    	else if (endX > beginX && endY > beginY) {
    		this.mPhysicsHandler.setVelocity((float) (dx * (Math.cos(angle))),(float) (dy * (Math.sin(angle))));
    	}
    	else if (endX > beginX && beginY > endY) {
    		this.mPhysicsHandler.setVelocity((float) (dx * (Math.cos(angle))),-(float) (dy * (Math.sin(angle))));
    	}
    	else if (beginX > endX && endY > beginY) {
    		this.mPhysicsHandler.setVelocity(-(float) (dx * (Math.cos(angle))), (float) (dy * (Math.sin(angle))));
    	}
    	move();
    }
    
    private void OutOfScreen() {
    	if (mX > MainActivity.CAMERA_WIDTH - this.mWidth || mX < 0) {
    		//dx*=-1;
    		this.mPhysicsHandler.setVelocityX(-this.mPhysicsHandler.getVelocityX());
    	}
    	if (mY > MainActivity.CAMERA_HEIGHT-this.mHeight || mY < 0) {
    		//dy*=-1;
    		this.mPhysicsHandler.setVelocityY(-this.mPhysicsHandler.getVelocityY());
    	}
    }
}
