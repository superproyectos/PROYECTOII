package com.upsidedown;
/*
CLASE JUEGO

*/
import com.badlogic.gdx.Game;

public class Juego extends Game
{
	@Override
	public void create()
	{
		this.setScreen(new EscenaPrincipal(this));
	}

	@Override
	public void render()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
	}
}