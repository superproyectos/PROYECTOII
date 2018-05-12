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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Creador
{
	/*<----------0. Declaración de variables---------->*/

	private Array<Bloque> figura;
	private Array<Figura> elementos;
	private Rectangle areaCreacion;
	private ShapeRenderer shape;
	private Color color;
	private NumBloques Bloques_permitidos;
	private Bloque ver;
	private boolean Game_Over;
	/*<----------1. Constructor---------->*/

	public Creador(ShapeRenderer shape)
	{
		this.shape=shape;
		areaCreacion=new Rectangle(Config.w/9 * 2, Config.w/9,Config.w/9*4,Config.w/9*2);
		figura = new Array<Bloque>();
		elementos=new Array<Figura>();
		ver=new Bloque(areaCreacion,Config.color(0,0,0,1));
		Game_Over=false;


		crearPiso();
		cambiarColor();
		Bloques_permitidos= new NumBloques();
		Bloques_permitidos.act_Numero_Bloques();
		Bloques_permitidos.setColor(color);

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
		aux.setPiso();
		elementos.add(new Figura(aux,2));
	}
	private void cambiarColor()
	{
		int i=new Random(System.currentTimeMillis()).nextInt(Config.colores.length);
		for(;i==7;)
			i=new Random(System.currentTimeMillis()).nextInt(Config.colores.length);
		color = Config.colores[new Random(System.currentTimeMillis()).nextInt(Config.colores.length)];
	}
/*	private void NuevoNum()
	{
		NumCuadros= new Random(System.currentTimeMillis()).nextInt(10)+1;
	}
*/

	/*<----------2. Detectar toque dentro del lienzo---------->*/

	public void toqueCreador()
	{
		Gdx.app.log("NUmeroooooooooooooooooo","NUm:"+ Bloques_permitidos.getNumero_Bloques());
		Bloques_permitidos.setLabel();
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
			if(figura.size== Bloques_permitidos.getNumero_Bloques())
			{
				cambiarDimensiones();
				elementos.add(new Figura(figura,0));
				cambiarColor();
				Bloques_permitidos.act_Numero_Bloques();
				Bloques_permitidos.setColor(color);
				Bloques_permitidos.setActFondo(true);
				Config.SONIDOS[2].play();
			}
			figura.clear();

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

	public void dibujaFigura(Array<Bloque> figura, Color colors)
	{
		shape.setColor(colors);
		for(Bloque bloque: figura)
		{
			shape.begin(ShapeRenderer.ShapeType.Filled);
			shape.rect(bloque.x+2,bloque.y-2, bloque.width-4, bloque.height-4);
			shape.end();
		}
	}

	public void dibujaCreador()
	{
		Bloques_permitidos.fondo();
	    boolean stop1,stop=false;
		toqueCreador();
		if(figura.size == Bloques_permitidos.getNumero_Bloques())
		{
			dibujaFigura(figura,color);
		}
		else
		{
			dibujaFigura(figura,Config.colores[7]);
		}

		for(Figura a:elementos)
		{
			if(a!=null) {

				stop1=a.actualizar();

               if( (stop1) && (!stop) )
				   stop= stop1;
            }

			else
				elementos.removeIndex(elementos.indexOf(a,false));
		}
		if (stop){
			elementos.get(0).setScrollY(false);
			for(Figura a:elementos) {
				if (a.buscabloque3() > (Config.h + (Config.w / 13) * 3)) {
					a.congelar();
				}
				a.setMovey();
			}
		}

		if(!Game_Over) {
			comprobar_GameOver(elementos);
		}
		else{
			float inx=Gdx.input.getAccelerometerX();
			float iny=Gdx.input.getAccelerometerY();
			Config.mundo.setGravity(new Vector2(-inx, -iny));
		}
	}
	public void congelar()
	{
		for (Figura a:elementos) {
			if(a.getTipo() != 2)
				a.congelar();
		}
	}
	public void salida()
	{
		areaCreacion.y=-1000;
	}
	public void entrada()
	{
		areaCreacion.y=Config.w/9;
	}


	public void BloquesRandom(){
		int i = new Random(System.currentTimeMillis()).nextInt(30)+11;
		for(int j=0; j<=i;j++){
			int a=new Random(System.currentTimeMillis()).nextInt((int)Config.w)+1;
			Bloque aux=new Bloque(a,Config.h,Config.w / 13,Config.w / 13,color);
			elementos.add(new Figura(aux,0));
			cambiarColor();
		}

	}
	public void comprobar_GameOver(Array<Figura> elementos){
		for(Figura a:elementos) {
			if (a.bloque_caido()){
				Game_Over=true;
				break;
			}
		}
		if (Game_Over){
			Bloque aux=new Bloque(0,0,Config.w,0,color);
			elementos.add(new Figura(aux,1));
			aux=new Bloque(0,0,0,Config.h,color);
			elementos.add(new Figura(aux,1));
			aux= new Bloque(Config.w,0,0,Config.h,color);
			elementos.add(new Figura(aux,1));
			BloquesRandom();
		}


	}
}