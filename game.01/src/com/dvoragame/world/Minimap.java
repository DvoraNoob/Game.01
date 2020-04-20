package com.dvoragame.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.dvoragames.entities.Ammo;
import com.dvoragames.entities.Enemy;
import com.dvoragames.entities.Entity;
import com.dvoragames.entities.Gun;
import com.dvoragames.entities.Lifepack;
import com.dvoragames.main.Game;

public class Minimap{

	public static BufferedImage minimap;
	public static int[] minimapPixels;
	public static BufferedImage minimapsub;
	
	public Minimap() {
		minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		minimapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
	}
	
	public void render(Graphics g) {
		
		renderMinimap();
		Graphics2D g2d = (Graphics2D) g;
		
		Area a1 = new Area(new Ellipse2D.Double(15,356,119,119));
		Area a2 = new Area(new Ellipse2D.Double(20,360,110,110));
		a1.subtract(a2);
		g2d.fill(a1);
		
		g2d.setClip(new Ellipse2D.Double(20,360,110,110));
		g2d.drawImage(minimapsub, 20, 360, 110, 110, null);
		g2d.dispose();
	}

	public static void renderMinimap() {
		
		for(int i = 0; i < minimapPixels.length;i++) {
			minimapPixels[i] = 0x9f006009;
		}
		for(int  xx = 0; xx < World.WIDTH; xx++) {
			for(int  yy = 0; yy < World.HEIGHT; yy++) {
				if(World.tiles[xx +(yy*World.WIDTH)] instanceof WallTile) {
					minimapPixels[xx +(yy*World.WIDTH)] = 0xff828282;
				}
			}
		}
		
		int xPlayer = Game.player.getX()/16;
		int yPlayer = Game.player.getY()/16;
		
		minimapPixels[xPlayer+(yPlayer*World.WIDTH)] = 0xff0094FF;
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e  = Game.entities.get(i);
			if(e instanceof Ammo) {
				
				int xAmmo = e.getX()/16;
				int yAmmo = e.getY()/16;
				
				minimapPixels[xAmmo +(yAmmo*World.WIDTH)] = 0xffFFD800;
				
			}else if(e instanceof Lifepack) {
				
				int xLife = e.getX()/16;
				int yLife = e.getY()/16;
				
				minimapPixels[xLife +(yLife*World.WIDTH)] = 0xffFF00DC;
				
			}else if(e instanceof Gun) {
				
				int xGun = e.getX()/16;
				int yGun = e.getY()/16;
				
				minimapPixels[xGun +(yGun*World.WIDTH)] = 0xff00BF16;
			}
		}
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy enemy = Game.enemies.get(i);
			
			int xEnemy = enemy.getX()/16;
			int yEnemy = enemy.getY()/16;
			
			minimapPixels[xEnemy +(yEnemy*World.WIDTH)] = 0xffff0000;
		}
		int xstart = Camera.x/16;
		int ystart = Camera.y/16;
		
		int xfinal = xstart + (Game.WIDTH/16);
		int yfinal = ystart + (Game.HEIGHT/16);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal ; yy++) {
				if(xx<0 || yy<0 || xx>=World.WIDTH || yy >= World.HEIGHT) {
					continue;
				}
				minimapsub = minimap.getSubimage(xstart,ystart, Game.WIDTH/16 , Game.HEIGHT/16);
			}
		}
	}
	
}
