package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class Ball extends GameObject {
	private int mWeight, mdx, mdy, dx=100, dy=100;

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
    
    public void move() {
    	
		this.mPhysicsHandler.setVelocityX(dx);
		this.mPhysicsHandler.setVelocityY(dy);
		
		OutOfScreen();
	}
    
    private void OutOfScreen() {
    	
    	if (mX > MainActivity.CAMERA_WIDTH - this.mWidth || mX < 0) {
    		dx*=-1;
    		this.mPhysicsHandler.setVelocityX(dx);
    	}
    	if (mY > MainActivity.CAMERA_HEIGHT-this.mHeight || mY < 0) {
    		dy*=-1;
    		this.mPhysicsHandler.setVelocityY(dy);
    	}
    }
}
