package com.upsidedown;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;


public class Fondo extends Actor {

	private int scroll;
	private Texture ciclo;
	private Array<Texture> layers;
	private final int LAYER_SPEED_DIFFERENCE = 2;
	float ang=0;
	private float x,y,width,heigth,escalaX,escalaY;
	private int origenX, origenY,rotacion,srcX,srcY;
	private boolean flipX,flipY;

	private int speed;

	public Fondo (Array<Texture> textures){
		layers = textures;
		for(int i = 0; i <layers.size;i++){
			layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		}
		ciclo=layers.get(4);
		scroll = 0;
		speed = 0;

		x = y = origenX = origenY = rotacion = srcY = 0;
		width =  Config.w;
		heigth = Config.h;
		escalaX = escalaY = 1;
		flipX = flipY = false;
	}

	public void setSpeed(int newSpeed){
		this.speed = newSpeed;
	}



	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
		batch.draw(ciclo,0,-heigth/5,width/2,0,heigth,heigth,1,1,-30*(float)Math.sin(ang),0,0,ciclo.getWidth(),ciclo.getHeight(),false,false);
		scroll+=speed;
		for(int i = 0;i<4;i++) {
			srcX = scroll + i*this.LAYER_SPEED_DIFFERENCE *scroll;
			switch (i) {

				case 0:
					y = (Config.w/4);
					break;
				case 1:
					y = (Config.w/7);
					break;
				case 2:
					y = (Config.w/7);
					break;
				default:
					y = 0;
			}
			batch.draw(layers.get(i), x, y, origenX, origenY, width, heigth/2,escalaX,escalaY,rotacion,srcX,srcY,layers.get(i).getWidth(),layers.get(i).getHeight(),flipX,flipY);
		}


	}
	public void setAng(float g)
	{
		ang=g;
	}
	public void setMomento(boolean dia)
	{
		flipY=dia;
	}
}


