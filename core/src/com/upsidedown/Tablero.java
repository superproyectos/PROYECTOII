package com.upsidedown;
/*OBJETO TABLERO. Campo de juego donde se desenvuelve la acción.
	JERARQUÍA:
		JUEGO>TABLERO.
	0. Declaración de variables.
	I- CREAR UN TABLERO.
		1. Constructor.
	II- CREAR EL CAMPO DE JUEGO.
		2. Crear campo de juego.
		3. Dibujar cuadrados del tablero.
	III- DIBUJAR CAMPO DE JUEGO.
		4. Dibujar campo de juego.
	IV- LIMPIEZA
		5. Eliminar residuos.
*/
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Tablero extends Actor
{
	/*<----------0. Declaración de variables---------->*/

	private Creador creador;
	private ShapeRenderer shape;

	/*<----------1. Constructor---------->*/

	public Tablero()
	{
		shape = new ShapeRenderer();
		creador=new Creador(shape);
	}

	@Override
	public void draw (Batch batch, float parentAlpha){
		super.draw(batch,parentAlpha);
		batch.end();
		dibujaTablero();
		batch.begin();

	}
	@Override
	public void act (float delta) {
		super.act(delta);
		//campoJuego();

	}
	/*<----------2. Crear campo de juego---------->*/

	private void campoJuego()
	{
		float ancho = Config.w / 9;
		dibujaCuadros(ancho * 2, ancho, 3, 5, ancho, new Color(129 / 255f, 217 / 255f, 221 / 255f, 0.5f), false);
		ancho = Config.w / 13;
		Config.alfa();
		dibujaCuadros(ancho * 4, Config.h-ancho*12, 10, 5, ancho, new Color(129 / 255f, 217 / 255f, 221 / 255f, 0.1f), true);
	}

	/*<----------3. Dibujar los cuadros en el campo de juego---------->*/
	private void dibujaCuadros(float x, float y, int f, int c, float ancho, Color color, boolean alfa)
	{
		shape.setColor(Color.WHITE);
		shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.rect(x - 5, y - 9, (c*ancho)+  11, (f*ancho) + 11);
		shape.end();
		shape.setColor(color);

		for (int i = 0; i < f; i++)
		{
			if (alfa && color.a < 1)
			{
				color.a += 0.05f;
				shape.setColor(color.r, color.g, color.b, color.a);
			}
			for (int j = 0; j < c; j++)
			{
				shape.begin(ShapeRenderer.ShapeType.Filled);
				shape.rect(x + ancho * j + 2, y + i * ancho - 2, ancho - 4, ancho - 4);
				shape.end();
			}
		}
	}

	/*<----------4. Dibujar campo de juego---------->*/

	public void dibujaTablero()
	{
		campoJuego();
		creador.dibujaCreador();
	}

	/*<----------5. Eliminar residuos---------->*/

	public void dispose()
	{
		shape.dispose();
	}

	public void congelar()
	{
		creador.congelar();
	}
}