package com.upsidedown;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.upsidedown.punave.Nave;

public class EscenaPrincipal implements Screen
{
	//Variables de escena
	private Game Juego;
	private Stage stage;
	//Variables de renderizado
	private SpriteBatch batch;
	private ShapeRenderer shape;
	//Escenario
	private Tablero tablero;//Campo de juego.
	private ImageButton ponernave;
	private ImageButton congelar;
	//Fondo
	private float a,r,g,b;
	private Music musica;
	//Power Ups
	private Nave nave;

	/*<----------CONSTRUCTOR---------->*/

	public EscenaPrincipal(Game UnJuego)
	{
		//Inicializamos variables
		Juego = UnJuego;
		stage = new Stage(new ScreenViewport());
		batch = new SpriteBatch();
		tablero = new Tablero();
		shape = new ShapeRenderer();
		crearFondo();
		a=0;//Angulo-Hora del día

		//Power ups
		ponernave = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("nave.png"))));
		congelar=new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("nieve.png"))));
		crearBoton(ponernave,0);
		crearBoton(congelar,1);
		powerUps();

		r=181;g=235;b=238;
		generaEscenario();
	}

	/*<----------GENERAR ESCENARIO---------->
	* Añade los objetos al escenario*/

	private void generaEscenario()
	{
		Config.escenario = stage;
		Gdx.input.setInputProcessor(stage);
		//Añadimos al escenario
		stage.addActor(tablero);
		stage.addActor(ponernave);
		stage.addActor(congelar);
	}

	/*<----------CREAR FONDO---------->
	* Se crea un fondo, se configura y se agrega al escenario*/

	private void crearFondo()
	{
		//Creamos el fondo
		Fondo fondo = new Fondo(cargarImagenes());
		//Configuramos el tamaño y la velocidad
		fondo.setSize(Config.w, Config.h / 2);
		fondo.setSpeed(1);
		//Añadimos al escenario
		stage.addActor(fondo);
	}

	/*<----------CARGAR IMÁGENES---------->
	* Devuelve un arreglo con las texturas a utilizar de fondo*/

	private Array<Texture>  cargarImagenes()
	{
		Array<Texture> texturas = new Array<Texture>();
		for (int i = 1; i <= 4; i++)
		{
			texturas.add(new Texture(Gdx.files.internal("Parallax" + i + ".png")));
			texturas.get(texturas.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
		}
		return texturas;
	}

	/*<----------CREAR BOTON---------->
	* Dado un botón establece su posición, dimensiones y eje de rotación*/

	private void crearBoton(ImageButton boton,int i)
	{
		boton.setWidth(Config.w*2/13);
		boton.setHeight(Config.w*2/13);
		boton.setX(Config.w*10/13);
		boton.setY(Config.h/3+i*boton.getWidth()*1.2f);
		boton.getImage().setOriginX(boton.getWidth()/2);
	}

	/*<----------ACCIONES DE LOS POWERUPS---------->
	* Establece los eventos al presionar sobre un PU*/

	private void powerUps()
	{
		//Invocar una nave.
		ponernave.addListener(new EventListener()
		{
			@Override
			public boolean handle(Event event)
			{
				if(nave==null)
				{
					nave=new Nave(Config.w/2,Config.h/5);
					stage.addActor(nave);
					Config.SONIDOS[1].play();
					tablero.salida();
				}

				return false;
			}
		});
		//Congelar escenario.
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

	/*<----------ANIMACIÓN DE ROTAR EN LOS POWERUPS---------->
	* Rota los PU*/

	private void rotar()
	{
		float ax=(float)Math.sin(a*100)*20;
		ponernave.getImage().setRotation(ax);
		congelar.getImage().setRotation(ax);
	}

	/*<----------ESTADOS DE LOS POWERUPS---------->
	* Detecta si se acabó un PU*/

	private void estados()
	{
		if(nave!=null&&nave.getDisparos()>=10)
		{
			nave.remove();
			nave=null;
			Config.SONIDOS[5].play();
			tablero.entrada();
		}
	}

	/*<----------CICLO DÍA NOCHE---------->
	* Transcurso del día - noche*/

	private void cicloDiario()
	{
		//Sumamos al ángulo y se obtiene el residuo para evitar overflow.
		a=(float)((a+0.001)%(float)Math.PI);
		//Calculamos el coseno. cos(0)=1 Día. cos(pi/2)=0 Noche.
		float ax=Math.abs((float)Math.cos(a));
		Gdx.gl.glClearColor(r*ax / 255f, g*ax / 255f, b*ax / 255f, 1);
	}

	/*<----------LIMPIAR ESCENARIO---------->
	* Resetea el escenario*/

	private void limpiar()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	/*<----------MÉTODOS PREDETERMINADOS---------->*/

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
		cicloDiario();
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