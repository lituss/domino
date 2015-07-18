package domino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class Fitxes {

	Array<Fitxa> fitxa;
	ArrayMap < Par,Integer> indexFitxa; 
	
	//Fitxa fitxa[] = new Fitxa[29 ];
	
	int pixelsCostat;
	public Fitxes(int maxValor){
		calculaPixelsCostat();
		int conta = 0,marge = 0;
		float posx = marge + pixelsCostat /2, posy = Gdx.graphics.getHeight()/2;
		fitxa = new Array<Fitxa>(false,0);
		indexFitxa = new ArrayMap< Par,Integer>(0);
		for (int pos = 0 ; pos <= maxValor ; pos++)
			for (int pos2 = pos ; pos2 <= maxValor ; pos2++){
				fitxa.add(new Fitxa(pos,pos2,pixelsCostat,conta));
				fitxa.get(conta).setPosition(posx,posy);
				indexFitxa.put(new Par(pos,pos2),conta);
				indexFitxa.put(new Par(pos2,pos),conta);
				conta++;
				posx+=pixelsCostat*1.1;
				
				if (posx + pixelsCostat > Gdx.graphics.getWidth()){
					posx = marge + pixelsCostat/2;
					posy+= 2.2*pixelsCostat;
				}
			}
		new Fitxa (pixelsCostat); // creem textura negra per quan es mostri tapada
		fitxa.get(9).setPosition(pixelsCostat/2, pixelsCostat);
		fitxa.get(9).info();
	}
	public void calculaPixelsCostat(){
		float Max = 2.f; //cms peu
		float Min = 0.2f; // cms peu
		//double densitatPix = Gdx.graphics.getDensity()*160/0.3937; // punts per centimetre
		double densitatPix = Gdx.graphics.getPpcX(); // punts per centimetre
		
		float width = Gdx.graphics.getWidth();
		
		//busquem la mida en cms perque hi capiguen 10 fitxes en orientacio vertical una al costat de l'altre en horitzontal
		
		double lon = (width / densitatPix) / 20;
		Gdx.app.log("litus","native density : "+Gdx.graphics.getDensity());
		Gdx.app.log("litus","densitatPix : "+densitatPix);
		Gdx.app.log("litus","width : "+width);
		Gdx.app.log("litus","lon : "+lon);
		if (lon > Max) lon = Max; else if (lon < Min) lon = Min;
		pixelsCostat = ((int)(lon*densitatPix));
	}
	public void resize(){
		calculaPixelsCostat();
		// regenera fitxes
	}
	public void draw( SpriteBatch batch){
		for ( Fitxa  aux : fitxa) aux.draw(batch);
	}
	public void dinamica(float delta){
		for ( Fitxa  aux : fitxa) aux.novaPosicio(delta);
	}
	public Fitxa getFitxa(int index){
		return fitxa.get(index);
	}
	
}