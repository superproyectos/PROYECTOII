package com.upsidedown;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Config
{
	public static float w= Gdx.graphics.getWidth();
	public static float h= Gdx.graphics.getHeight();
	public static float mid= Gdx.graphics.getHeight()-(Gdx.graphics.getWidth()/13*2)-((Gdx.graphics.getWidth()/ 13)*6)-10;
	public static World mundo;
	public static final Stage escenario =new Stage(new ScreenViewport());
	public static final int MARGEN=2;
	public static final float PPM= 100;
	public static final Skin SKIN = new Skin(Gdx.files.internal("skin/comic-ui.json"));
	public static final Color []colores={
			Config.color(203, 67, 53,1)/*rojo*/,
			Config.color(136, 78, 160,1)/*morado*/,
			Config.color(52, 152, 219  ,1)/*azul*/,
			Config.color(26, 188, 156  ,1)/*turquesa*/,
			Config.color(30, 132, 73,1)/*verde*/,
			Config.color(244, 208, 63,1)/*amarillo*/,
			Config.color(220, 118, 51,1)/*naranja*/,
			Config.color(131, 145, 146,1)/*gris*/,
			Config.color(46, 64, 83,1)/*azul-gris*/,
	};
	public static final Sound SONIDOS[]={
			/*0*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/lose.mp3", Files.FileType.Internal)),
			/*1*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/pu.wav", Files.FileType.Internal)),
			/*2*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/suelta.mp3", Files.FileType.Internal)),
			/*3*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/shoot.wav", Files.FileType.Internal)),
			/*4*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/toca.mp3", Files.FileType.Internal)),
			/*5*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/explo.wav", Files.FileType.Internal)),
			/*6*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/llego.wav", Files.FileType.Internal)),
			/*7*/Gdx.audio.newSound( Gdx.files.getFileHandle("sounds/destroy.wav", Files.FileType.Internal)),
	};
	public static void setMundo(Vector2 g)
	{
		Config.mundo=new World(g,true);
	}
	public static Color color(float r,float g,float b, float a)
	{
		return new Color(r/255f,g/255f,b/255f,a);
	}
	public static void alfa()
	{
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
}