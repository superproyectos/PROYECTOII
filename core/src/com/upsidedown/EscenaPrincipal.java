package com.upsidedown;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.upsidedown.punave.Bala;
import com.upsidedown.punave.Nave;

import static com.sun.corba.se.impl.naming.cosnaming.NamingContextImpl.debug;

public class EscenaPrincipal implements Screen,ContactListener
{
	//Variables de escena
	private Game Juego;
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

	private OrthographicCamera camara;
	private Box2DDebugRenderer camaraBordes;
	private boolean debug;
	private boolean dia;

	Fondo fondo;
	/*<----------CONSTRUCTOR---------->*/

	public EscenaPrincipal(Game UnJuego)
	{
		//Inicializamos variables
		Juego = UnJuego;
		Config.setMundo(new Vector2(0, 10f));
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

		Config.mundo.setContactListener(this);
		debug=false;
		camara=new OrthographicCamera();
		camara.setToOrtho(false,Config.w/Config.PPM,Config.h/Config.PPM);
		camara.position.set(Config.w/2,Config.h/2,0);
		camaraBordes=new Box2DDebugRenderer();
		dia=true;
	}

	/*<----------GENERAR ESCENARIO---------->
	* Añade los objetos al escenario*/

	private void generaEscenario()
	{

		Gdx.input.setInputProcessor(Config.escenario);
		//Añadimos al escenario
		Config.escenario.addActor(tablero);
		Config.escenario.addActor(ponernave);
		Config.escenario.addActor(congelar);
	}

	/*<----------CREAR FONDO---------->
	* Se crea un fondo, se configura y se agrega al escenario*/

	private void crearFondo()
	{
		//Creamos el fondo
		fondo = new Fondo(cargarImagenes());
		//Configuramos el tamaño y la velocidad
		fondo.setSize(Config.w, Config.h / 2);
		fondo.setSpeed(1);
		//Añadimos al escenario
		Config.escenario.addActor(fondo);
	}

	/*<----------CARGAR IMÁGENES---------->
	* Devuelve un arreglo con las texturas a utilizar de fondo*/

	private Array<Texture>  cargarImagenes()
	{
		Array<Texture> texturas = new Array<Texture>();
		for (int i = 1; i <= 5; i++)
		{
			texturas.add(new Texture(Gdx.files.internal("Parallax" + i + ".png")));
			//texturas.get(texturas.size - 1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
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
					Config.escenario.addActor(nave);
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
		if(nave!=null&&nave.getDisparos()>=10&&nave.getDisparadas()==0)
		{
			nave.eliminaBalas();
			nave.dispose();
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
		fondo.setAng(a*360/((float)Math.PI));
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
		Config.mundo.step(Gdx.graphics.getDeltaTime(),6,2);
		cicloDiario();
		limpiar();
		rotar();
		Config.escenario.act(Gdx.graphics.getDeltaTime());
		Config.escenario.draw();
		estados();
		if(debug)
			camaraBordes.render(Config.mundo,camara.combined);
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

	@Override
	public void beginContact(Contact contact)
	{

		try
		{
			Fixture a = contact.getFixtureA(), b = contact.getFixtureB();
			if (a.getUserData()instanceof Bala)
			{
				Fixture ax = a;
				a = b;
				b = ax;
			}
			if (b.getUserData() instanceof Bala)
			{
				Bala bala = (Bala) b.getUserData();
				Figura fig = (Figura) a.getUserData();

				if (fig.getTipo() == 0)
					fig.buscabloque(bala.x, bala.y + bala.radius+2);
				if (nave != null)
					nave.isHit(bala);
				Config.SONIDOS[7].play();
			}
			else
			{
				Figura fig1 = (Figura) b.getUserData();
				Figura fig2 = (Figura) a.getUserData();

				if(!fig1.gethit() || !fig2.gethit())
				{

					float d=0,c=0;



					if(!fig1.gethit())
						d = fig1.buscabloque2();
					else
						c = fig2.buscabloque2();

					if(d!=0)
					{
						Gdx.app.log("message", "Entro con D: " + d);
						fig1.setyMax(d);
						fig1.setScrollY(true);
					}
					else {
						if(c!=0) {
							Gdx.app.log("message", "Entro con C: " + c);
							fig2.setyMax(c);
							fig2.setScrollY(true);
						}
					}


					fig1.sethit(true);
					fig2.sethit(true);

				}
			}

		}
		catch (Exception e)
		{
			//La bala se borró pero
		}

	}

	@Override
	public void endContact(Contact contact)
	{

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{

	}
}