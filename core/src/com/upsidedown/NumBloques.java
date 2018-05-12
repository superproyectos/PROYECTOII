package com.upsidedown;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Random;

/**
 * Created by Alfonso on 11/05/2018.
 */

public class NumBloques {

    private ShapeRenderer shape,shape1;
    private int MAX=87;
    private int MIN=40;
    private int Numero_Bloques;
    private Color color,color1;
    private Label label;
    private float radio;
    private float x;
    private float y;
    private boolean actFondo;

    public NumBloques(){

        shape = new ShapeRenderer();
        shape1 = new ShapeRenderer();

        Numero_Bloques=0;
        radio=MIN;
        x=Config.w / 2;
        y=Config.h / 3;
        actFondo=false;
        color= new Color();
        color1= new Color();
       // color.add(Config.colores[2]);

        label=new Label(String.valueOf(Numero_Bloques),Config.SKIN,"title");
        label.setColor(Color.WHITE);
        label.setPosition(x/1.05f,y/4);
        label.setSize(Config.w/2,Config.h/2);
        Config.escenario.addActor(label);




    }

    public void fondo() {

        shape1.setColor(color1);
        shape1.begin(ShapeRenderer.ShapeType.Filled);
        shape1.circle( x , y , MIN);
        shape1.end();

        if ((radio<=MAX) && actFondo) {



            radio+=1f;
            Config.alfa();
            color.a=color.a-0.02f;

            shape.setColor(color);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.circle( x , y , radio);
            shape.end();

        }
        else {
            radio = MIN;
            Config.alfa();
            color.a =1;
            setActFondo(false);
        }
    }
    public void setColor(Color color){
        this.color.set(color);
        this.color1.set(color);
    }

    public void setRadio(float radio){
        this.radio=radio;
    }


     public void act_Numero_Bloques(){
        Numero_Bloques = new Random(System.currentTimeMillis()).nextInt(10)+1;
        if(Numero_Bloques == 10){
            label.setPosition(x/1.1f,y/4);
        }
        else{
            label.setPosition(x/1.05f,y/4);
        }
     }

     public int getNumero_Bloques(){
         return Numero_Bloques;
     }

    public void setMAX(int MAX){
         this.MAX = MAX;
    }
    public void setMIN (int MIN){
        this.MIN = MIN;
    }

    public void setLabel(){
        label.setText(String.valueOf(Numero_Bloques));
        label.toFront();

    }

    public void setActFondo(boolean bool){
        this.actFondo=bool;
    }


}
