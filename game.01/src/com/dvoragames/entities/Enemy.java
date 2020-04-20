package com.dvoragames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;

import com.dvoragame.world.AStar;
import com.dvoragame.world.Camera;
import com.dvoragame.world.Vector2i;
import com.dvoragame.world.World;
import com.dvoragames.main.Game;

public class Enemy extends Entity{
	
	private static double speed = 0.5;
	
	private int enemyLife = 10;
	
	public boolean enemyDamage = false;
	private int damageCur = 0;
	private int damageFrames = 8;
	
	private boolean toRight = true;
	private boolean toLeft = false;
	
	public int[] pixelsP;
		
	private int frames = 0, maxFrames = 10,index = 0, maxIndex = 0;
	private BufferedImage[] spriteE;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		spriteE =  new BufferedImage[2];
		spriteE[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		spriteE[1] = Game.spritesheet.getSprite(128, 16, 16, 16);		
	}
	
	public void tick() {
		
		maskx = 4;
		masky = 7;
		maskw = 7;
		maskh = 9;
		if(this.calcDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 80) {
			if(isCollidingPlayer() == false) {
				if(path == null || path.size() == 0) {
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
					path = AStar.findPath(Game.world, start, end);
				}
				if(new Random().nextInt(100) < 90)
					followPath(path);
				if(new Random().nextInt(100) < 5) {
					Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
					Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16));
					path = AStar.findPath(Game.world, start, end);
				}
			}else {
				Game.player.vida -=0.2;
				Game.player.isDamage = true;
			}
		}else{
			isPatrolling();
		}
		
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
			
			collidingShot();
			
			if(enemyLife <= 0) {
				destroySelf();
				return;
			}
			
			if(enemyDamage) {
				damageCur++;
				if(damageCur == damageFrames) {
					damageCur = 0;
					enemyDamage = false;
				}
			}
	}


	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingShot(){
		for(int i = 0; i < Game.shoot.size(); i++) {
			Entity e = Game.shoot.get(i);
				if(Entity.isColliding(this, e)) {
					enemyLife--;
					enemyDamage = true;
					Game.shoot.remove(i);
					return;
				}
			
		}
	}
	
	public void isPatrolling() {
		if(toRight == true && World.isFree((int)(x+speed),  this.getY())) {
			x+=speed;
			if(!World.isFree((int)(x+speed),  this.getY())) {
				toRight = false;
				toLeft = true;
			}
		}
		else if(toLeft == true && World.isFree((int)(x-speed),  this.getY())) {
			x-=speed;
			if(!World.isFree((int)(x-speed),  this.getY())) {
				toRight = true;
				toLeft = false;
			}
		}
	}
	
	public boolean isCollidingPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16,16);
		
		return enemyCurrent.intersects(player);
	}
	
	
	public void render(Graphics g) {
		if(!enemyDamage) {
			g.drawImage(spriteE[index], this.getX() - Camera.x,this.getY() - Camera.y ,null);
		}else {
			g.drawImage(Entity.ENEMY_FB, this.getX() - Camera.x, this.getY() - Camera.y , null);
		}
		//g.setColor(Color.CYAN);
		//g.fillRect(this.getX() + maskx - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
	}

}
