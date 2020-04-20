package com.dvoragames.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dvoragames.main.Game;

public class UI{

	public static BufferedImage AmmoIcon = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	public static BufferedImage LifeIcon = Game.spritesheet.getSprite(6*16, 0, 16, 16);

	public UI(){
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(15, 5,(int)((100/2)*1.5), 10);
		g.setColor(Color.green);
		g.fillRect(15, 5,(int)((Game.player.vida/2)*1.5), 10);
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 8));
		g.drawString((int)Game.player.vida+" / "+(int)Game.player.maxVida,20,13);
		g.drawImage(LifeIcon, 0,-2,null);

		g.drawImage(AmmoIcon, Game.WIDTH-30, -5, null);
		g.setColor(Color.white);
		g.setFont(new Font("ARIAL",Font.CENTER_BASELINE, 8));
		if(Game.player.ammo < 10) {
			g.drawString((int)Game.player.ammo+" / "+200,Game.WIDTH-30,20);
		}else if(Game.player.ammo <= 50 || Game.player.ammo >= 50) {
			g.drawString((int)Game.player.ammo+" / "+200,Game.WIDTH-34,20);
		}else if(Game.player.ammo >= 100) {
			g.drawString((int)Game.player.ammo+" / "+200,Game.WIDTH-38,20);
		}
	}
}
