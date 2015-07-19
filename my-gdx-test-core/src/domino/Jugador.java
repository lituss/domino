package domino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Jugador {
private PosicioJugador posicio; // 0 horitzontal, baix, 1 vertical dreta , 2 horitzontal alt, 3 vertical esquerra
private Orientacio orientacio;
private TipusJugador tipus;
private Jugador seguentJugador;
private Par primera; // on posem la primera fitxa, les demes aniran al costat
private Fitxa primeraFitxa = null;
private Fitxa ultimaFitxa = null;
private Array <Fitxa> fitxes;
private Joc joc;

public Jugador (Joc joc,int nFitxesInicial,PosicioJugador posicio){
	this.joc = joc;
	this.posicio = posicio;
	int migX = Gdx.graphics.getWidth() / 2, migY = Gdx.graphics.getHeight();
	int lonCostat = joc.getFitxes().pixelsCostat, marge = 0;
	//primera = new Par(0,0);
	switch(posicio){
		case hBaix:
			primera = new Par(	(Gdx.graphics.getWidth() - lonCostat*(nFitxesInicial-2))/2, lonCostat + marge);
			orientacio = Orientacio.Vertical;
			break;
		case hAlt:
			primera = new Par(	(Gdx.graphics.getWidth() + lonCostat*(nFitxesInicial))/2, Gdx.graphics.getHeight() - lonCostat - marge);
			orientacio = Orientacio.Vertical;
			break;
		case vDreta:
			primera = new Par(Gdx.graphics.getWidth() - lonCostat - marge,  (Gdx.graphics.getHeight() - lonCostat*(nFitxesInicial))/2);
			orientacio = Orientacio.Horitzontal;
			break;
		case vEsquerra:
			primera = new Par( lonCostat + marge,  (Gdx.graphics.getHeight() + lonCostat*(nFitxesInicial - 2))/2);
			orientacio = Orientacio.Horitzontal;
			break;
	}
Gdx.app.log("litus", "primera : "+primera.getlValue()+" , "+primera.getrValue());
}
public void rebFitxa(Fitxa fitxa){
	
}
public void rebFitxa(Fitxa fitxa , float temps){
	if (primeraFitxa ==null){
		primeraFitxa = fitxa;
		ultimaFitxa = fitxa;
		fitxa.marcaNovaPosicio(primera.getlValue(), primera.getrValue(), orientacio, temps);
	}
	else
		switch (posicio){
		case hBaix : 
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf() + fitxa.getLonCostat(), ultimaFitxa.getYf(), orientacio, temps);
			break;
		case hAlt: 
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf() - fitxa.getLonCostat(), ultimaFitxa.getYf(), orientacio,temps);
			break;
		case vDreta:
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf(), ultimaFitxa.getYf() + fitxa.getLonCostat(), orientacio,temps);
			break;
		case vEsquerra:
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf(), ultimaFitxa.getYf() - fitxa.getLonCostat(), orientacio,temps);
			break;
		}
	ultimaFitxa = fitxa;
}
public void posaFitxa (Fitxa fitxa,int valor){
	
}

public Jugador getSeguentJugador() {
	return seguentJugador;
}
public void setSeguentJugador(Jugador seguentJugador) {
	this.seguentJugador = seguentJugador;
}
public void setPrimera(int x, int y){
	primera.setValue(x, y);
}
}
