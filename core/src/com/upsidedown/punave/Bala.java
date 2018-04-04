package com.upsidedown.punave;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.upsidedown.Config;

public class Bala extends Circle
{
	private ShapeRenderer shape;
	private World mundo;
	private Body cuerpo;
	public Bala(float x,float y,float r)
	{
		this.set(x,y,r);
		shape=new ShapeRenderer();
		this.mundo= Config.mundo;
		crearCuerpo();
	}
	private void dibujar()
	{
		shape.setColor(Color.RED);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.circle(x,y,radius);
		shape.end();
	}
	private void crearCuerpo()
	{
		BodyDef bodyDef=new BodyDef();
		bodyDef.type=BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(x/Config.PPM,y/Config.PPM);
		cuerpo=mundo.createBody(bodyDef);
		CircleShape shape=new CircleShape();
		shape.setPosition(new Vector2(x,y));
		FixtureDef propiedades=new FixtureDef();
		propiedades.shape=shape;
		propiedades.density=5f;
		cuerpo.createFixture(propiedades);
		shape.dispose();
	}
	public void actualizar()
	{
		this.setPosition(cuerpo.getPosition().x*Config.PPM,cuerpo.getPosition().y*Config.PPM);
		dibujar();
	}
	public void dispose()
	{
		shape.dispose();
	}
}