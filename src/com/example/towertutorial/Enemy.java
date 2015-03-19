package com.example.towertutorial;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;



public class Enemy extends GameObject {
	private int mWeight, mdx, mdy, dx=100, dy=100;
	private int health,maxHealth;

    public Enemy(int health, int weight, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        this.mWeight = weight;
        this.health = health;
        this.maxHealth = health;
        this.mdx=1;
        this.mdy=1;
        //this.dx=0;
        //this.dy=0;
    }
 
    public int getmWeight() {
        return mWeight;
    }
    
    public void takeDamage(int damage) {
    	this.health = this.health - damage;
    }

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	
	//enemy health
	public int getHealth(){
		return health;
	}
	
	public int getMaxHealth(){
		return maxHealth;
	}

}
