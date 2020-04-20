package com.dvoragames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.dvoragame.world.Camera;
import com.dvoragame.world.Node;
import com.dvoragame.world.Vector2i;
import com.dvoragames.main.Game;

public class Entity{
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	public static BufferedImage GUN_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	public static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	public static BufferedImage Gun_L = Game.spritesheet.getSprite(96, 32, 16, 16);
	public static BufferedImage Gun_R = Game.spritesheet.getSprite(112, 32, 16, 16);
	public static BufferedImage Gun_D = Game.spritesheet.getSprite(128, 32, 16, 16);
	public static BufferedImage ENEMY_FB = Game.spritesheet.getSprite(144, 16, 16, 16);

	
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	public int depth;
	
	protected List<Node> path;
	
	private static double speed = 0.5;
	
	private BufferedImage sprite;
	
	public int maskx,masky,maskw,maskh;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}
	
	public static Comparator<Entity> entitySorter = new Comparator<Entity>() {
		
		
		@Override
		public int compare(Entity n0, Entity n1) {
			if(n1.depth < n0.depth)
				return + 1;
			if(n1.depth > n0.depth)
				return - 1;
			return 0;
		}
	};
	
	public void setMask(int maskx,int masky,int maskw,int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		depth = 0;
	}
	
	public double calcDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				if(x < target.x*16 && !isColliding(this.getX() + 1, this.getY())) {
					x+=speed;
				}else if(x > target.x*16 && !isColliding(this.getX() - 1, this.getY())) {
					x-=speed;
				}
				
				if(y < target.y*16 && !isColliding(this.getX(), this.getY() + 1)) {
					y+=speed;
				}else if(y > target.y*16 && !isColliding(this.getX(), this.getY() - 1)) {
					y-=speed;
				}
				
				if(x == target.x*16 && y == target.y*16) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		for(int i=0; i<Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx,e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX()+e1.maskx,e1.getY()+e1.masky,e1.maskw,e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.getX()+e2.maskx,e2.getY()+e2.masky,e2.maskw,e2.maskh);
		
		return e1Mask.intersects(e2Mask);
				
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y,null);
		//g.setColor(Color.blue);
		//g.fillRect(getX()+maskx-Camera.x, getY()+masky-Camera.y, maskw, maskh);
	}
	

	

}
