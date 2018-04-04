package com.upsidedown.punave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Nave extends Actor
{
	private SpriteBatch batch;
	private boolean entrada;
	private Sprite sprite;
	private float yd;
	private Array<Bala> balas;
	public Nave(float x,float y)
	{
		batch = new SpriteBatch();
		sprite=new Sprite(new Texture("ship.png"));
		setX(x);
		setY(Gdx.graphics.getHeight());
		yd=y;
		entrada=true;
		balas=new Array<Bala>();
	}
	private void mover()
	{
		float in=Gdx.input.getAccelerometerX();
		float x=getX();
		if(in>1&&x>30)
			setX(getX()-10);
		else if(in<-1&&x+getWidth()+30<Gdx.graphics.getWidth())
			setX(getX()+10);
	}
	private void dibujar()
	{
		batch.begin();
		batch.draw(sprite.getTexture(), getX()-40, getY(),80,52);
		batch.end();
	}
	private void disparar()
	{
		if(Gdx.input.justTouched())
			balas.add(new Bala(this.getX(),this.getY()+50,5));
	}
	private void balasDisparadas()
	{
		if(balas.size>0&&balas.first().y>Gdx.graphics.getHeight())
		{
			balas.get(0).dispose();
			balas.removeIndex(0);
		}

		for(Bala bala:balas)
			bala.actualizar();
	}
	public void actualizar()
	{
		if(getY()>yd)
			setY(getY()-10);
		else
			mover();
		disparar();
		balasDisparadas();
		dibujar();
	}
	public void dispose()
	{
		batch.dispose();
		sprite.getTexture().dispose();
	}
	@Override
	public void draw (Batch batch, float parentAlpha){
		super.draw(batch,parentAlpha);
		batch.end();
		actualizar();
		batch.begin();

	}
	@Override
	public void act (float delta) {
		super.act(delta);
		//campoJuego();

	}
}