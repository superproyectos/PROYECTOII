package com.upsidedown;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upsidedown.punave.Nave;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

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
	private Nave nave;
	private float a;
	private float r,g,b;
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

		a=0;
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

		r=181;g=235;b=238;
	}
	private void crearBoton(ImageButton boton,int i)
	{
		boton.setWidth(Config.w*2/13);
		boton.setHeight(Config.w*2/13);
		boton.setX(Config.w*10/13);
		boton.setY(Config.h/3+i*boton.getWidth()*1.2f);
		boton.getImage().setOriginX(boton.getWidth()/2);
	}

	private void limpiar()
	{
		a=(float)((a+0.001)%(float)Math.PI);
		float ax=Math.abs((float)Math.cos(a));
		Gdx.gl.glClearColor(r*ax / 255f, g*ax / 255f, b*ax / 255f, 1);
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
					nave=new Nave(Config.w/2,Config.h/5);
					stage.addActor(nave);
					hayNave=true;
					Config.SONIDOS[1].play();
					tablero.salida();
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
				Config.SONIDOS[1].play();
				return false;
			}
		});
	}
	private void rotar()
	{
		float ax=(float)Math.sin(a*100)*20;
		ponernave.getImage().setRotation(ax);
		congelar.getImage().setRotation(ax);
	}
	private void estados()
	{
		if(nave!=null&&nave.getDisparos()>=10)
		{
			nave.remove();
			nave=null;
			hayNave=false;
			Config.SONIDOS[5].play();
			tablero.entrada();
		}
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
		rotar();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		estados();
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
		for(Sound a:Config.SONIDOS)
			a.dispose();
	}

}