package com.upsidedown.punave;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.upsidedown.Config;

public class Bala extends Circle
{
	private ShapeRenderer shape;
	private Body cuerpo;
	boolean hit;
	public Bala(float x,float y,float r)
	{
		this.set(x,y,r);
		shape=new ShapeRenderer();
		crearCuerpo();
		hit=false;
	}
	private void dibujar()
	{
		/*for (Fixture a:cuerpo.getFixtureList())
			if(a.isSensor())
				return;*/
		if(!cuerpo.getFixtureList().get(0).isSensor())
		{
			shape.setColor(Color.RED);
			shape.begin(ShapeRenderer.ShapeType.Filled);
			shape.circle(x,y,radius);
			shape.end();
		}

	}
	private void crearCuerpo()
	{
		BodyDef bodyDef=new BodyDef();
		bodyDef.type=BodyDef.BodyType.DynamicBody;
		bodyDef.position.set((x)/Config.PPM,(y)/Config.PPM);
		cuerpo=Config.mundo.createBody(bodyDef);
		CircleShape shape=new CircleShape();
		shape.setRadius(radius/Config.PPM);
		FixtureDef propiedades=new FixtureDef();
		propiedades.shape=shape;
		propiedades.density=5f;
		Fixture fixture=cuerpo.createFixture(propiedades);
		fixture.setUserData(this);
		shape.dispose();
	}
	public boolean actualizar()
	{
		if(!hit)
		{
			this.setPosition(cuerpo.getPosition().x*Config.PPM,cuerpo.getPosition().y*Config.PPM);
			dibujar();
			return true;
		}
		else
			this.dispose();
		return false;
	}
	private void eliminaCuerpo()
	{
		for(Fixture a:cuerpo.getFixtureList())
			cuerpo.destroyFixture(a);
		Config.mundo.destroyBody(cuerpo);
	}
	public void dispose()
	{
		eliminaCuerpo();
		shape.dispose();
	}
	public void sethit(boolean hit)
	{
		this.hit=hit;
	}
}