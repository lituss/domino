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
private Fitxa fitxaAnterior;

public Jugador (Joc joc,int nFitxesInicial,PosicioJugador posicio){
	this.joc = joc;
	this.posicio = posicio;
	int migX = Gdx.graphics.getWidth() / 2, migY = Gdx.graphics.getHeight();
	int lonCostat = joc.getFitxes().pixelsCostat, marge = 0;
	//primera = new Par(0,0);
	fitxes = new Array <Fitxa> (false,0);
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
		fitxaAnterior = fitxa;
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
	fitxaAnterior.setFitxaEsquerra(fitxa);
	fitxa.setFitxaDreta(fitxaAnterior);
	ultimaFitxa = fitxa;
	fitxes.add(fitxa);
}
public Fitxa posaFitxa (int id){
	 
	for (Fitxa aux : fitxes)
		if (aux.getId() == id) {
			Fitxa esquerra,dreta;
			esquerra = aux.getFitxaEsquerra();
			dreta = aux.getFitxaDreta();
			if (esquerra != null) esquerra.getFitxaDreta().setFitxaDreta(dreta);
			if (dreta != null) dreta.getFitxaEsquerra().setFitxaEsquerra(esquerra);
			aux.marcaNovaPosicio(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Orientacio.Vertical, 2);
			fitxes.removeValue(aux, true);
			return aux;
		}
	return null;
}
/*public Fitxa posaFitxa(int lValue,int rValue, boolean left, boolean win){
	for (Fitxa aux : fitxes)
		if (aux.getlValue() == lValue){
			left = true;
			fitxes.removeValue(aux, true);
			if (fitxes.size == 0) win = true;
			return aux;
		}
			else
				if (aux.getlValue() == rValue){
					left = false;
					fitxes.removeValue(aux, true);
					if (fitxes.size == 0) win = true;
					return aux;
				}
				else
					if (aux.getrValue() == lValue){
						left = true;
						fitxes.removeValue(aux, true);
						if (fitxes.size == 0) win = true;
						return aux;
					}
					else
						if (aux.getrValue() == rValue){
							left = false;
							fitxes.removeValue(aux, true);
							if (fitxes.size == 0) win = true;
							return aux;
						}
		
	return null;
}
*/
public Fitxa posaFitxa(Fitxa lFitxa,Fitxa rFitxa){
	int lValue = (lFitxa.getFitxaEsquerra() == null ? lFitxa.getlValue() : lFitxa.getrValue());
	int rValue = (rFitxa.getFitxaEsquerra() == null ? rFitxa.getlValue() : rFitxa.getrValue());
	Fitxa fitxaPerTirar = null;
	
	for (Fitxa aux : fitxes){
		if (aux.getlValue() == lValue) {colocaFitxa(aux,lFitxa,lValue);return aux;}
		else 
			if (aux.getlValue() == rValue){ colocaFitxa(aux,rFitxa,rValue); return aux;}
			else
				if (aux.getrValue() == lValue){ colocaFitxa(aux,lFitxa,lValue);return aux;}
				else
					if (aux.getrValue() == rValue){ colocaFitxa(aux,rFitxa,rValue); return aux;}
	}
	return null;
}

public void colocaFitxa(Fitxa novaFitxa, Fitxa fitxa, int value){
	// ja sabem la fitxa que tirem i contra quina
	// aqui desconectem la fitxa de les del jugador i calcularem la posicio i orientacio de la fitxa que tirem
	
	Fitxa esquerra,dreta;
	esquerra = novaFitxa.getFitxaEsquerra();
	dreta = novaFitxa.getFitxaDreta();
	if (esquerra != null) esquerra.getFitxaDreta().setFitxaDreta(dreta);
	if (dreta != null) dreta.getFitxaEsquerra().setFitxaEsquerra(esquerra);
	//aux.marcaNovaPosicio(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Orientacio.Vertical, 2);
	fitxes.removeValue(novaFitxa, true);
	
	// posicionament
	
}

boolean posiciona(Fitxa novaFitxa, Fitxa fitxa, int value){
	switch (fitxa.)
	return true;
}

boolean posiciona(Fitxa novaFitxa, Fitxa fitxa, )
public Jugador getSeguentJugador() {
	return seguentJugador;
}
public void setSeguentJugador(Jugador seguentJugador) {
	this.seguentJugador = seguentJugador;
}
public void setPrimera(int x, int y){
	primera.setValue(x, y);
}
public int contaFitxes(){ return fitxes.size;}
}
