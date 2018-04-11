package com.upsidedown.punave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.upsidedown.Config;
import com.upsidedown.EscenaPrincipal;

public class Nave extends Actor
{
	private boolean entrada;
	private Sprite sprite;
	private float yd;
	private Array<Bala> balas;
	private byte disparos=0;
	public Nave(float x,float y)
	{
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
	private void disparar()
	{
		if(Gdx.input.justTouched()&&disparos<10)
		{
			balas.add(new Bala(this.getX(),this.getY()+50,5));
			Config.SONIDOS[3].play();
			disparos++;
		}
	}
	public int getDisparadas()
	{
		return balas.size;
	}
	public Byte getDisparos()
	{
		return disparos;
	}
	public void eliminaBalas()
	{
		for(Bala a:balas)
			a.dispose();
	}
	private void balasDisparadas()
	{
		if(balas.size>0&&balas.first().y>Gdx.graphics.getHeight())
		{
			balas.get(0).dispose();
			balas.removeIndex(0);
		}

		for(Bala bala:balas)
			if(!bala.actualizar()){
				balas.removeValue(bala, false);
			}
	}
	public void actualizar()
	{
		if(getY()>yd)
			setY(getY()-10);
		else
			mover();
		disparar();
		balasDisparadas();
	}
	public void dispose()
	{
		sprite.getTexture().dispose();
	}
	@Override
	public void draw (Batch batch, float parentAlpha){
		super.draw(batch,parentAlpha);
		batch.end();
		actualizar();
		batch.begin();
		batch.draw(sprite.getTexture(), getX()-40, getY(),80,52);
	}
	@Override
	public void act (float delta) {
		super.act(delta);
		//campoJuego();

	}

	public void isHit(Bala bala)
	{
		if ((bala!=null)&&balas.contains(bala,false))
		{
			balas.get(balas.indexOf(bala,false)).sethit(true);
		}

	}
}