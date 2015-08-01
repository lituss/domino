package domino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Jugador {
private PosicioJugador posicio; // 0 horitzontal, baix, 1 vertical dreta , 2 horitzontal alt, 3 vertical esquerra
private float orientacio; //angle de les seves fitxes
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
			orientacio = 0;
			break;
		case hAlt:
			primera = new Par(	(Gdx.graphics.getWidth() + lonCostat*(nFitxesInicial))/2, Gdx.graphics.getHeight() - lonCostat - marge);
			orientacio = 180;
			break;
		case vDreta:
			primera = new Par(Gdx.graphics.getWidth() - lonCostat - marge,  (Gdx.graphics.getHeight() - lonCostat*(nFitxesInicial))/2);
			orientacio = 90;
			break;
		case vEsquerra:
			primera = new Par( lonCostat + marge,  (Gdx.graphics.getHeight() + lonCostat*(nFitxesInicial - 2))/2);
			orientacio = 270;
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
		fitxa.marcaNovaPosicio(primera.getlValue(), primera.getrValue(), orientacio,EnumOrientacio(orientacio), temps);
		fitxaAnterior = fitxa;
	}
	else
		switch (posicio){
		case hBaix : 
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf() + fitxa.getLonCostat(), ultimaFitxa.getYf(), orientacio, EnumOrientacio(orientacio), temps);
			break;
		case hAlt: 
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf() - fitxa.getLonCostat(), ultimaFitxa.getYf(), orientacio,EnumOrientacio(orientacio), temps);
			break;
		case vDreta:
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf(), ultimaFitxa.getYf() + fitxa.getLonCostat(), orientacio,EnumOrientacio(orientacio),temps);
			break;
		case vEsquerra:
			fitxa.marcaNovaPosicio(ultimaFitxa.getXf(), ultimaFitxa.getYf() - fitxa.getLonCostat(), orientacio,EnumOrientacio(orientacio),temps);
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
			aux.setNivell(0);
			aux.marcaNovaPosicio(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, (aux.esDoble() ? 0 : 90) , (aux.esDoble() ? Orientacio.V : Orientacio.HL), 2);
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
public Fitxa posaFitxa(Fitxa lFitxa,Fitxa rFitxa, /* parametres de tornada */ Fitxa fitxaAnterior, int valor){
	int lValue = (lFitxa.getFitxaEsquerra() == null ? lFitxa.getlValue() : lFitxa.getrValue());
	int rValue = (rFitxa.getFitxaEsquerra() == null ? rFitxa.getlValue() : rFitxa.getrValue());
	Fitxa fitxaPerTirar = null;
	
	for (Fitxa aux : fitxes){
		if (aux.getlValue() == lValue) {colocaFitxa(aux);fitxaAnterior = lFitxa; valor = lValue;return aux;}
		else 
			if (aux.getlValue() == rValue){ colocaFitxa(aux); fitxaAnterior = rFitxa; valor = rValue; return aux;}
			else
				if (aux.getrValue() == lValue){ colocaFitxa(aux); fitxaAnterior = lFitxa; valor = lValue;return aux;}
				else
					if (aux.getrValue() == rValue){ colocaFitxa(aux);fitxaAnterior = rFitxa; valor = rValue; return aux;}
	}
	return null;
}

public void colocaFitxa(Fitxa novaFitxa){
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

/*
boolean posiciona(Fitxa novaFitxa, Fitxa fitxa, int value, float espaiAnterior){
	// per posar una fitxa, mirem si hi cap. Si no :
	// si no hi cap per la X
	// 	si hi ha espai al altre costat i estem a la mateixa alçada (el primer "nivell" ), movem totes les fitxes cap al altre costat
	// 	si no hi ha espai o si no estan els dos costats a la mateixa alçada tirem amunt si som a la esquerra o a vall si es a la dreta
	//si no ha cap per la y les baixem o pujem totes, i si no es pot, les reduim de escala
	float espai,posX = 0,posY=0;
	Orientacio oriFitxa = null;
	boolean hiCap = false, noHiCapX = false, noHiCapY = false;
	do{
		switch (fitxa.getOrientacio()){
			case V : if (fitxa.getFitxaEsquerra()!= null) // && (fitxa.getNivell() % 4 == 0)) { // la actual fitxa no pot ser doble, per tant no pot tenir orientacio V ni H
						if (fitxa.getFitxaEsquerra().getOrientacio() == Orientacio.HR){
							posX = fitxa.getXf() + fitxa.getLonCostat()*3/2;
							posY = fitxa.getYf();
							oriFitxa = Orientacio.HR;
							if ((hiCap = hiCap(fitxa, oriFitxa, posX, posY,noHiCapX,noHiCapY) == true)) break;
							else{ 
								if (fitxa.getNivell() == 0 && mouTotesX(-2*fitxa.getLonCostat())) continue;
								else{
									// busquem 2 no dobles seguits i baixem
									Fitxa aux = fitxa.getFitxaEsquerra();
									int noDobles = 1;
									while (noDobles < 2){
										aux = aux.getFitxaEsquerra();
										if (aux.getlValue() == aux.getrValue()) noDobles = 0;
										else noDobles ++;
									}
									posiciona(aux,aux.getFitxaDreta(),Orientacio.HR);
									oriFitxa = Orientacio.VD;
									posX 
							}
							oriFitxa = Orientacio.VD;
							//posX = 
						}
					}
				}
	} while (!hiCap);
	novaFitxa.setOrientacio(oriFitxa);
	novaFitxa.marcaNovaPosicio(posX, posY, oriFitxa, Constants.TempsMoviment);
	
return true;
}

boolean posiciona (Fitxa fitxa,Fitxa fitxaAnterior,Orientacio orientacioNova){
	float posX = 0,posY = 0;
	do{
		switch (orientacioNova){
		case HR: 
			if (fitxaAnterior.getOrientacio()==Orientacio.V)
				posX = fitxaAnterior.getXf() + fitxa.getLonCostat()*3/2;
			else
				posX = fitxaAnterior.getXf() + fitxa.getLonCostat()*2;
			posY = fitxaAnterior.getYf();
			break;
		}
		fitxa.marcaNovaPosicio(posX, posY, orientacioNova, Constants.TempsMoviment);
		if (fitxaAnterior.getFitxaDreta() == fitxa){
			fitxaAnterior = fitxa;
			fitxa = fitxa.getFitxaDreta();
		}
		else{
			fitxaAnterior = fitxa;
			fitxa = fitxa.getFitxaEsquerra();
		}
	} while (fitxa != null);
	return true;
}
*/



public Jugador getSeguentJugador() {
	return seguentJugador;
}

//Orientacio // nivell 0,4,8 ,... HR ->VD ->HL -> VD ->HR
			
//boolean posicionaFitxaAnterior(Fitxa fitxa,float espai){
	
//}
public void setSeguentJugador(Jugador seguentJugador) {
	this.seguentJugador = seguentJugador;
}
public void setPrimera(int x, int y){
	primera.setValue(x, y);
}
public int contaFitxes(){ return fitxes.size;}

Orientacio EnumOrientacio(float graus){
	switch ((int)graus){
	case 0 : 
	case 180:
		return Orientacio.V;
	case 90 :
	case 270: 
		return Orientacio.H;
		default: return null;
	}
}
}
