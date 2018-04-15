package com.upsidedown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Bloque extends Rectangle
{
	private ShapeRenderer shape;
	private Color color;
	private float vx;
	private float vy;
	private Fixture fix;
	private boolean hit;
	private boolean Piso;
	private float movey;
	public Bloque(float x, float y, float w, float h, Color color)
	{
		this.x=x;this.y=y;
		width=w;height=h;
		this.color=color;
		shape=new ShapeRenderer();
		vx=0;
		vy=0;
		fix=null;
		hit=false;
		Piso=false;
	}
	public Bloque(Rectangle bloque,Color color)
	{
		this.x=bloque.x;
		this.y=bloque.y;
		this.width=bloque.width;
		this.height=bloque.height;
		this.color=color;
		shape=new ShapeRenderer();
		vx=vy=0;
		fix=null;
		hit=false;
		Piso=false;
	}
	public void dibujar(float angulo)
	{
		final float deg=-0.2f;
		shape.setColor(color);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.rect(x+Config.MARGEN,y+Config.MARGEN,width/2,height/2,
				width-2*Config.MARGEN, height-2*Config.MARGEN,1,1,angulo,
				color,color,new Color(color.r+deg,color.g+deg,color.b+deg,1),
				new Color(color.r+deg,color.g+deg,color.b+deg,1));
		shape.end();
	}
	public void setVariacion(float vx,float vy)
	{
		this.vx=vx;
		this.vy=vy;
	}
	public Vector2 getV()
	{
		return new Vector2(vx,vy);
	}
	public void dispose()
	{
		shape.dispose();
	}
	public void setColor(Color color)
	{
		this.color=color;
	}
	public void setFix(Fixture fixture)
	{
		fix=fixture;
	}
	public Fixture getFix()
	{
		return fix;
	}
	public void sethit(boolean h)
	{
		hit=h;
	}
	public boolean gethit()
	{
		return hit;
	}
	public void setPiso(){ Piso=true;}
	public void setmovey(float ym){ movey=ym;}
	public float getmovey(){return movey;}
	public boolean getPiso(){ return Piso;}
}