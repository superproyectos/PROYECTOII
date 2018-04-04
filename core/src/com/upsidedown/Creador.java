package com.upsidedown;
/*OBJETO CREADOR. Lienzo donde se dibujan los bloques a crear.
	JERARQUÍA:
		JUEGO>TABLERO>CREADOR.
	0. Declaración de variables.
	I- CREAR UN CREADOR.
		1. Constructor.
	II- DETECTAR PULSACIÓN SOBRE EL LIENZO.
		2. Detectar toque dentro del lienzo.
		3. Verificar si se está pulsando un bloque ya colocado.
	III- DIBUJAR OBJETO QUE SE ESTÁ CREADO.
		4. Dibujar el objeto creado.
*/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Creador
{
	/*<----------0. Declaración de variables---------->*/

	private Array<Bloque> figura;
	private Array<Figura> elementos;
	private Rectangle areaCreacion;
	private ShapeRenderer shape;
	private OrthographicCamera camara;
	private Box2DDebugRenderer camaraBordes;
	private boolean debug;
	private Color color;
	Bloque ver;
	/*<----------1. Constructor---------->*/

	public Creador(ShapeRenderer shape)
	{

		debug=false;




		this.shape=shape;
		areaCreacion=new Rectangle(Config.w/9 * 2, Config.w/9,Config.w/9*4,Config.w/9*2);
		figura = new Array<Bloque>();
		elementos=new Array<Figura>();
		Config.setMundo(new Vector2(0, 5f));

		ver=new Bloque(areaCreacion,Config.color(0,0,0,1));
		camara=new OrthographicCamera();
		camara.setToOrtho(false,Config.w/Config.PPM,Config.h/Config.PPM);
		camara.position.set(Config.w/2,Config.h/2,0);
		camaraBordes=new Box2DDebugRenderer();
		crearPiso();
		cambiarColor();

	}
	private void crearPiso()
	{
		cambiarColor();
		Rectangle base=new Rectangle();
		base.x=Config.w/13*3;
		base.width=Config.w/13*7;
		base.height=Config.w/13*2;
		base.y=(Config.h-base.height);
		Bloque aux=new Bloque(base,color);
		elementos.add(new Figura(aux,1));
	}
	private void cambiarColor()
	{
		color=Config.colores[new Random(System.currentTimeMillis()).nextInt(Config.colores.length)];
	}
	/*<----------2. Detectar toque dentro del lienzo---------->*/

	public void toqueCreador()
	{
		if(Gdx.input.isTouched())
		{
			float ancho = Config.w / 9;
			float x=(int)(Gdx.input.getX()/ancho),y=(int)((Config.h-Gdx.input.getY())/ancho);
			x*=ancho;y*=ancho;
			if(areaCreacion.contains(x,y))
			{
				Bloque bloque=new Bloque(x,y,ancho,ancho,color);
				if(!seRepitebloque(bloque))
				{
					if(figura.size!=0)
					{
						Bloque aux = figura.peek();
						if((aux.x==bloque.x&&((int)Math.abs(aux.y-bloque.y)==(int)ancho))||
								(aux.y==bloque.y&&((int)Math.abs(aux.x-bloque.x)==(int)ancho)))
							figura.add(bloque);
					}
					else
					{
						figura.add(bloque);
						Config.SONIDOS[4].play();
					}

				}
				else
				{
					if(figura.indexOf(bloque,false)==figura.size-2)
						figura.pop();

				}
			}
		}
		else
		{
			if(figura.size>0)
			{
				cambiarDimensiones();
				elementos.add(new Figura(figura,0));
				figura.clear();
				cambiarColor();
				Config.SONIDOS[2].play();
			}


		}
	}
	private void cambiarDimensiones()
	{
		for(Rectangle a: figura)
		{
			//(9*(a.x)/Config.w)-4) obtiene el número del bloque
			a.x=((9*(a.x)/Config.w)-4)*Config.w/13+Config.w/13*6;
			a.y=Config.h/3+(a.y/a.width)*Config.w/13;
			a.width=a.height=Config.w/13;

		}
	}
	/*<----------3. Verificar si se está pulsando un bloque ya colocado---------->*/

	private boolean seRepitebloque(Rectangle bloque)
	{
		for(Rectangle evaluado: figura)
			if(evaluado.x==bloque.x&&evaluado.y==bloque.y)
				return true;
		return false;
	}

	/*<----------4. Dibujar la figura creada---------->*/

	public void dibujaFigura(Array<Bloque> figura)
	{
		shape.setColor(color);
		for(Bloque bloque: figura)
		{
			shape.begin(ShapeRenderer.ShapeType.Filled);
			shape.rect(bloque.x+2,bloque.y-2, bloque.width-4, bloque.height-4);
			shape.end();
		}
	}

	public void dibujaCreador()
	{
		toqueCreador();
		dibujaFigura(figura);
		Config.mundo.step(Gdx.graphics.getDeltaTime(),6,2);
		for(Figura a:elementos)
		{
			a.actualizar();
		}
		if(debug)
			camaraBordes.render(Config.mundo,camara.combined);
		//ver.dibujar(0);
	}
	public void congelar()
	{
		for (Figura a:elementos)
			a.congelar();
	}
	public void salida()
	{
		areaCreacion.y=-1000;
	}
	public void entrada()
	{
		areaCreacion.y=Config.w/9;
	}
}