package com.upsidedown;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upsidedown.punave.BotonNave;
import com.upsidedown.punave.Nave;

public class EscenaPrincipal implements Screen
{
	private SpriteBatch batch;
	private Tablero tablero;//Campo de juego.
	private Stage stage;
	private Game Juego;
	private ShapeRenderer shape;
	private ImageButton ponernave;
	private ImageButton congelar;
	private boolean hayNave;
	private Music musica;
	public EscenaPrincipal(Game UnJuego)
	{

		Juego = UnJuego;

		hayNave=false;
		stage = new Stage(new ScreenViewport());
		Config.escenario = stage;
		Gdx.input.setInputProcessor(stage);
		batch = new SpriteBatch();


		Array<Texture> textures = new Array<Texture>();
		for (int i = 1; i <= 4; i++)
		{
			textures.add(new Texture(Gdx.files.internal("Parallax" + i + ".png")));
			textures.get(textures.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		}
		Fondo fondo = new Fondo(textures);
		fondo.setSize(Config.w, Config.h / 2);
		fondo.setSpeed(1);
		stage.addActor(fondo);


		tablero = new Tablero();
		stage.addActor(tablero);
		shape = new ShapeRenderer();
		ponernave = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("nave.png"))));
		congelar=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("nieve.png"))));
		crearBoton(ponernave,0);
		crearBoton(congelar,1);
		stage.addActor(ponernave);
		stage.addActor(congelar);
		powerUps();
	}
	private void crearBoton(ImageButton boton,int i)
	{
		boton.setWidth(Config.w*2/13);
		boton.setHeight(Config.w*2/13);
		boton.setX(Config.w*10/13);
		boton.setY(Config.h/3+i*boton.getWidth()*1.2f);
	}
	private void limpiar()
	{
		Gdx.gl.glClearColor(181 / 255f, 235 / 255f, 238 / 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


	}

	private void powerUps()
	{
		ponernave.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				if(!hayNave)
				{
					stage.addActor(new Nave(Config.w/2,Config.h/4));
					hayNave=true;
				}

				return false;
			}
		});
		congelar.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				tablero.congelar();
				return false;
			}
		});
	}
	@Override
	public void show()
	{
		musica = Gdx.audio.newMusic(Gdx.files.getFileHandle("music/Happy Boy End Theme Trim.mp3", Files.FileType.Internal));
		musica.play();
		musica.setLooping(true);
	}

	@Override
	public void render(float delta)
	{
		limpiar();
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{

	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	@Override
	public void hide()
	{

	}

	@Override
	public void dispose()
	{
		batch.dispose();
		tablero.dispose();
		musica.dispose();
	}
}