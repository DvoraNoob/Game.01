package com.dvoragames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Help {
	
	public BufferedImage TECLAS_WASD = Game.spritesheet.getSprite(0,128, 48, 32);
	public BufferedImage TECLAS_ARROWS = Game.spritesheet.getSprite(48,128, 48, 32);
	public BufferedImage TECLAS_PAUSE = Game.spritesheet.getSprite(96,128, 16, 16);
	public BufferedImage TECLAS_ESC = Game.spritesheet.getSprite(112,128, 32, 16);
	public BufferedImage TECLAS_SHOOT = Game.spritesheet.getSprite(96,144, 64, 16);
	public BufferedImage TECLAS_MOUSE = Game.spritesheet.getSprite(32,96, 32, 32);
	public BufferedImage TECLAS_ENTER = Game.spritesheet.getSprite(0,96, 32, 32);
	public BufferedImage TECLAS_NAVI = Game.spritesheet.getSprite(64, 128, 16, 32);
	
	public String options = "back";
	
	public boolean back;
	
	public void tick() {
		
		if(back) {
			back = false;
			if(options == "back") {
				if(Game.curMenu == "MENU") {
					Game.gameState = "MENU";
				}else if(Game.curMenu == "PAUSE") {
					Game.gameState = "PAUSE";
				}
			}
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(30,30,30));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		
		g.setColor(Color.yellow);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
		g.drawString("HELP",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE/10);
		
		g.setColor(Color.red);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 30));
		g.drawString("Move:           or",10,Game.HEIGHT*Game.SCALE/3-30);
		g.drawImage(TECLAS_WASD,110,90, 66, 50, null);
		g.drawImage(TECLAS_ARROWS,250,90, 66, 50, null);

		
		g.setColor(Color.red);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 30));
		g.drawString("Shoot:              or",10,Game.HEIGHT*Game.SCALE/2-30);
		g.drawImage(TECLAS_SHOOT,110,185,100,30, null);
		g.drawImage(TECLAS_MOUSE,270,180,70,40, null);

		g.setColor(Color.red);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 30));
		g.drawString("Pause:           or",10,Game.HEIGHT*Game.SCALE/2+55);
		g.drawImage(TECLAS_ESC,115,270,70,30, null);
		g.drawImage(TECLAS_PAUSE,260,265,40,40, null);
		
		g.setColor(Color.red);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 30));
		g.drawString("Navigation:      and",10,Game.HEIGHT*Game.SCALE/2+140);
		g.drawImage(TECLAS_NAVI,180,350,40,45, null);
		g.drawImage(TECLAS_ENTER,280,350,50,40, null);
		
		if(options == "back") {
			g.setColor(Color.blue);
			g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 40));
			g.drawString("Back",Game.WIDTH*Game.SCALE/3+65,Game.HEIGHT*Game.SCALE-20);
			g.drawString(">",Game.WIDTH*Game.SCALE/3+40,Game.HEIGHT*Game.SCALE-20);
		}
	}
}
