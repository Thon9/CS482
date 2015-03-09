package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class Enemy extends GameObject {
	private int mWeight, mdx, mdy, dx=100, dy=100;
	private int health;

    public Enemy(int health, int weight, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.mWeight = weight;
        this.health = health;
        this.mdx=1;
        this.mdy=1;
        //this.dx=0;
        //this.dy=0;
    }
 
    public int getmWeight() {
        return mWeight;
    }

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	//enemy health
	public int getHealth(){
		return health;
	}

}
