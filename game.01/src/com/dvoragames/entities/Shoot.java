package com.dvoragames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dvoragame.world.Camera;
import com.dvoragame.world.World;
import com.dvoragames.main.Game;

public class Shoot extends Entity {
	
	private double dx;
	private double dy;
	private double spd = 4; 
	
	private int life = 20, curlife = 0;
	
	public Shoot(int x, int y, int width, int height, BufferedImage sprite,double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}
	
	public void tick() {
		if(World.isFreeDynamic((int)(x+(dx*spd)), (int)(y+(dy*spd)), 3, 3)) {
			x+=dx*spd;
			y+=dy*spd;
		}else {
			Game.shoot.remove(this);
			World.particles(100,(int) x,(int) y);
			return;
		}
		curlife++;
		if(curlife==life) {
			Game.shoot.remove(this);
			return;
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(this.getX() - Camera.x,this.getY() - Camera.y, width, height);
	}
	
}
