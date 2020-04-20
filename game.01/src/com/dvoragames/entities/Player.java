package com.dvoragames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dvoragame.world.Camera;
import com.dvoragame.world.World;
import com.dvoragames.main.Game;
import com.dvoragames.main.Sound;

public class Player extends Entity {
	
	public boolean right,left,up,down;
	public int d_dir = 2, u_dir = 3;
	public int r_dir = 0, l_dir = 1;
	public int dir = d_dir;
	public double speed = 0.6;
	
	private int frames = 0, maxFrames = 5,index = 0, maxIndex = 3;
	public  boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] danoPlayer;
	
	public double vida = 100;
	public double maxVida = 100;
	public int ammo = 0;
	
	public int mx, my;
	
	public boolean isDamage = false;
	public boolean isDamageFeedBack = false;	
	private int damageFrames = 0;
	
	public boolean isShoot = false;
	public boolean mouseShoot = false;
	
	public boolean mouseMove = false;
	
	public boolean hasGun = false;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		danoPlayer = new BufferedImage[4];
		
		for(int i = 0; i<4; i++) {
			danoPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 64, 16, 16);
		}
		
		for(int i = 0; i<4 ; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		
		for(int i = 0; i<4 ; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
		for(int i = 0; i<4 ; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
		}
		
		for(int i = 0; i<4 ; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
		}
	}

	public void tick() {
		depth = 1;
		maskx = 4;
		masky = 1;
		maskw = 9;
		maskh = 15;
		moved = false;
		if(right && World.isFree((int)(x+speed),this.getY())) {
			moved = true;
			dir = r_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = l_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = u_dir;
			y-=speed;
		}
		if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = d_dir;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}
		
		this.checkItems();
		this.checkGun();
		
		if(isDamage){
			isDamage = false;
			damageFrames++;
			if(damageFrames == 8) {
				damageFrames = 0;
				if(isDamageFeedBack) {
					isDamageFeedBack = false;
				}else {
					isDamageFeedBack = true;
				}
				
			}
		}else {
			isDamageFeedBack = false;
		}
		
		if(isShoot) {
			isShoot = false;
			if(hasGun && ammo > 0) {
				ammo--;
				double angle = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));
				double dx=Math.cos(angle);
				double dy=Math.sin(angle);
				int px=8;
				int py=8;
				double degrees = Math.toDegrees(angle);
				System.out.println(degrees);
				
				Shoot shoot = new Shoot(this.getX() + px,this.getY() + py,3,3,null,dx,dy);
				Game.shoot.add(shoot);
			}
		}
		
		if(mouseMove) {
			mouseMove = false;
			double angleMove = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));
			double degrees = Math.toDegrees(angleMove);
			
			if(degrees >= -45 && degrees < 45) {
				dir = r_dir;
			}else if(degrees >= 135 && degrees > -135) {
				dir = l_dir;
			}
			
			else if(degrees  > -135 && degrees < -45) {
				dir = u_dir;
			}else if(degrees > 45 && degrees < 135) {
				dir = d_dir;
			}
		}
		 
		if(mouseShoot) {
			mouseShoot = false;
			if(hasGun && ammo > 0) {
				ammo--;
				double angle = Math.atan2(my - (this.getY()+8 - Camera.y), mx - (this.getX()+8 - Camera.x));
				double dx=Math.cos(angle);
				double dy=Math.sin(angle);
				int px=8;
				int py=8;
				double degrees = Math.toDegrees(angle);
				System.out.println(degrees);
				
				Shoot shoot = new Shoot(this.getX() + px,this.getY() + py,3,3,null,dx,dy);
				Game.shoot.add(shoot);
			}
		}
		
		if(Game.player.vida <= 0.5) {
			Game.player.vida = 0;
			Game.gameState = "GAMEOVER";
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	public void checkGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Gun) {
				if(Entity.isColliding(this, atual)) {
					hasGun = true;
					ammo += 50;
					Game.entities.remove(atual);
				}
			}
		}				
	}
	
	public void checkItems() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColliding(this, atual)) {
					vida+=10;
					if(vida>maxVida) {
						vida=maxVida;
					}
					Game.entities.remove(atual);
				}
				
			}else if(atual instanceof Ammo) {
				if(Entity.isColliding(this, atual)) {
					ammo+=50;
					if(ammo > 200) {
						ammo = 200;
					}
					Game.entities.remove(atual);
				}
			}
		}
		
	}
	
	public void render(Graphics g) {
		if(!isDamageFeedBack) {
			if(dir == r_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x ,this.getY() - Camera.y,null);
				if(hasGun) {
					g.drawImage(Gun_L, getX() - 1 - Camera.x, getY() + 1 - Camera.y, null);
				}
			}
			if(dir == l_dir) {
				g.drawImage(leftPlayer[index], this.getX() - Camera.x ,this.getY() - Camera.y ,null);
				if(hasGun) {
					g.drawImage(Gun_R, getX() - 4 - Camera.x, getY() + 1 - Camera.y, null);
				}
			}
			if(dir == u_dir) {
				g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y,null);
			}
			if(dir == d_dir) {
				g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y ,null);
				if(hasGun) {
					g.drawImage(Gun_D, getX() - 2 - Camera.x, getY() + 1 - Camera.y, null);
				}
			}
		}else{
			if(dir == r_dir) {
				g.drawImage(danoPlayer[0], this.getX() - Camera.x ,this.getY() - Camera.y,null);
			}
			if(dir == l_dir) {
				g.drawImage(danoPlayer[1], this.getX() - Camera.x ,this.getY() - Camera.y ,null);
			}
			if(dir == u_dir) {
				g.drawImage(danoPlayer[2], this.getX() - Camera.x, this.getY() - Camera.y,null);
			}
			if(dir == d_dir) {
				g.drawImage(danoPlayer[3], this.getX() - Camera.x, this.getY() - Camera.y ,null);
			}
			
		}
	}

}
