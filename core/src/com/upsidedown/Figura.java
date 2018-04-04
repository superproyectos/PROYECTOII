package com.upsidedown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Figura
{
	private Array<Bloque>figura;
	private Body cuerpo;
	private World mundo;
	private int tipo;
	public Figura(Array<Bloque>figura,int tipo)
	{
		this.figura=new Array<Bloque>();
		copiar(figura);
		this.mundo=Config.mundo;
		this.tipo=tipo;
		cajaColision();
	}
	public Figura(Bloque figura,int tipo)
	{
		this.figura=new Array<Bloque>();
		this.figura.add(figura);
		this.mundo=Config.mundo;
		this.tipo=tipo;
		cajaColision();
	}
	private void cajaColision()
	{
		boolean creado=false;
		for (Bloque bloque:figura)
		{
			PolygonShape shape=new PolygonShape();
			if(!creado)
			{
				BodyDef bodyDef=new BodyDef();
				setTipo(bodyDef);
				bodyDef.position.set((bloque.x+bloque.width/2)/Config.PPM,(bloque.y+bloque.height/2)/Config.PPM);
				cuerpo=mundo.createBody(bodyDef);
				shape.setAsBox(bloque.width/2/Config.PPM,bloque.height/2/Config.PPM);
			}
			else
			{
				float vx=(bloque.x+bloque.width/2)/Config.PPM-cuerpo.getPosition().x;
				float vy=(bloque.y+bloque.height/2)/Config.PPM-cuerpo.getPosition().y;
				bloque.setVariacion(vx,vy);
				Vector2 v=new Vector2(vx,vy);

				shape.setAsBox(bloque.width/2/Config.PPM,bloque.height/2/Config.PPM,v,cuerpo.getAngle());
			}
			FixtureDef propiedades=new FixtureDef();
			propiedades.shape=shape;
			propiedades.density=5f;
			propiedades.restitution=.1f;
			propiedades.friction=0.3f;
			cuerpo.createFixture(propiedades);
			shape.dispose();
			creado=true;
		}


	}
	private void setTipo(BodyDef bodyDef)
	{
		switch (tipo)
		{
			case 0:
				bodyDef.type=BodyDef.BodyType.DynamicBody;
				break;
			case 1:
				bodyDef.type=BodyDef.BodyType.StaticBody;
				break;
			case 2:
				bodyDef.type=BodyDef.BodyType.KinematicBody;
				break;
		}
	}
	public void actualizar()
	{
		float x,y,vx,vy;
		for (Bloque bloque:figura)
		{
			/*
			 Wow matemáticas en un videojuego.
			 El eje de coordenadas del objeto está inclinado.P(x',y')
			 El eje de dibujo es normal.P(x,y).
			 Lo primero es rotar el eje inclinado hasta que llegue al del dibujo.

			 x=x'cos@-y'sen@
			 y=y'*cos@+x'cos@

			 Una vez obtenido el punto, es necesario trasladarlo al origen relativo del cuerpo.
			 x+=xcuerpo
			 y+=ycuerpo

			 Y se obtiene el punto en el eje normal :)

			 */
			x=bloque.getV().x;
			y=bloque.getV().y;
			vx=x*(float)Math.cos(cuerpo.getAngle())-y*(float)Math.sin(cuerpo.getAngle());
			vy=y*(float)Math.cos(cuerpo.getAngle())+x*(float)Math.sin(cuerpo.getAngle());
			vx+=cuerpo.getPosition().x;
			vy+=cuerpo.getPosition().y;
			bloque.setPosition(vx*Config.PPM-bloque.width/2,vy*Config.PPM-bloque.height/2);
			bloque.dibujar((float)(cuerpo.getAngle()*180/Math.PI));
		}

	}
	private void copiar(Array<Bloque>original)
	{
		for (Bloque bloque:original)
			figura.add(bloque);
	}
	public void fuerza(Vector2 F)
	{
		cuerpo.applyForceToCenter(F.x,F.y,true);
	}
	public void dispose()
	{
		for (Bloque bloque:figura)
			bloque.dispose();
	}
	public void congelar()
	{
		System.out.println("Entro");
		cuerpo.setType(BodyDef.BodyType.StaticBody);
		for (Bloque a:figura)
			a.setColor(Color.CYAN);
	}
}