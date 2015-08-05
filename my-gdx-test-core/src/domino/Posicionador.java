package domino;

import com.badlogic.gdx.Gdx;

public class Posicionador {
public enum Tipus {esquerra,dreta};
public Tipus tipusPosicionador;
private int valor;
private int distanciaNivell = 0;
private int nouNivell;
private Joc joc;
float posX = 0,posY = 0;
float hiCapX = 0,hiCapY = 0;
float angle;
boolean esquerra = false,amunt=false;
static float limitXE ,limitXD, limitYD, limitYU;
float lonCostat;

	public Posicionador(Tipus tipus,Joc joc){
		tipusPosicionador = tipus;
		this.joc = joc;
	}
	void calculaLimits(){ // necessitem les mides de les fitxes, ho hem de cridar despres de crearles
		lonCostat = joc.getFitxes().getFitxa(0).getLonCostat();
		limitXE = 2*lonCostat + Constants.MargeJugadorsJoc;
		limitXD = Gdx.graphics.getWidth() - limitXE;
		limitYD = limitXE;
		limitYU = Gdx.graphics.getHeight() - limitYD;
	}
	public boolean posiciona (Fitxa novaFitxa,Fitxa fitxa,int valor){
		
		lliga(novaFitxa,fitxa);
		Orientacio orientacio = novaOrientacio(getOrientacio(fitxa),fitxa,novaFitxa,nouNivell);
		Gdx.app.error("litus", "Fitxa -> orientacio : "+novaFitxa.getlValue()+" : "+novaFitxa.getrValue()+" --> "+orientacio);
		if (posiciona(novaFitxa,orientacio,fitxa,nouNivell,valor)) return true;
		else
			Gdx.app.log("litus", "Error intern, no sap on posar la fitxa");
		return false;
	}
	public boolean posiciona (Fitxa novaFitxa,Orientacio orientacio,Fitxa fitxa, int nivell, int valor){
		
		
		calculaPos(fitxa,orientacio); //,posX,posY,hiCapX,hiCapY,esquerra,amunt);
		
		if (hiCapX>=0 && hiCapY>=0){
			// posicio final
			angle = getAngle(novaFitxa,orientacio,valor);
			novaFitxa.marcaNovaPosicio(posX, posY, angle, orientacio,Constants.TempsMoviment);
			return true;
		}

		if (hiCapY < 0){
			if (!mouTotesY(hiCapY,amunt)) escalaTotes(hiCapY);
			return (posiciona(novaFitxa,orientacio,fitxa,nivell,valor));
		}
		if (hiCapX < 0){ 
			// si encara som a la primera fila , mirem de moure totes les fitxes cap al altre costat
			// si no reposicionem
		 
			if (mouTotesX(hiCapX,esquerra)) return (posiciona(novaFitxa,fitxa,nivell));
			Fitxa aux = novaFitxa;
			Orientacio auxOrientacio;
			while (true){
				auxOrientacio = reposiciona(aux);
				if (auxOrientacio == null) {
					aux = anteriorFitxa(aux);
					continue;
				}
				if (posiciona(aux,auxOrientacio,anteriorFitxa(aux),nivell,getValor(anteriorFitxa(aux)))) break;
				else aux = anteriorFitxa(aux);
			}
			while ((aux = seguentFitxa(aux)) != null) posiciona(aux,anteriorFitxa(aux),getValor(anteriorFitxa(aux)));
 
			}
		return false; // mai ha de passar, es per que no es queixi el compilador
	}
	
	
	private float getAngle(Fitxa novaFitxa, Orientacio orientacio, int valor2) {
		// TODO Auto-generated method stub
		switch (orientacio){
		case V: return 0;
		case H: return 90;
		case HL: if (valor2==novaFitxa.getlValue()) return 90; else return 270;
		case HR: if (valor2==novaFitxa.getlValue()) return 270;else return 90;
		case VUL: 
		case VUR:if (valor2==novaFitxa.getlValue()) return 180;else return 0;
		case VDL:
		case VDR:if (valor2==novaFitxa.getlValue()) return 0;else return 180;
		default: return 45;
		}
	}
	
	Orientacio reposiciona(Fitxa fitxa){
		int nivell = fitxa.getNivell();
		Orientacio aux;
		if (fitxa.esDoble()) return null;
		if (anteriorFitxa(fitxa).esDoble()) return null;
		if (tipusPosicionador == Tipus.esquerra)
			switch (fitxa.getOrientacio()){
			case HL : aux = Orientacio.VUL;
			break;
			case VUL : aux = Orientacio.HR;
			break;
			case HR : aux = Orientacio.VUR;
			break;
			case VUR : aux = Orientacio.HL;
			break;
			default: return null;
		}
		else
			switch (fitxa.getOrientacio()){
			case HR: aux = Orientacio.VDR;
			break;
			case VDR: aux = Orientacio.HL;
			break;
			case HL: aux = Orientacio.VDL;
			break;
			case VDL : aux = Orientacio.HR;
			break;
			default:return null;
			}
		return aux;
	}
	void lliga(Fitxa novaFitxa , Fitxa fitxa){
		if (tipusPosicionador == Tipus.esquerra){
			fitxa.setFitxaEsquerra(novaFitxa);
			novaFitxa.setFitxaDreta(fitxa);
		}
		else{
			fitxa.setFitxaDreta(novaFitxa);
			novaFitxa.setFitxaEsquerra(fitxa);
		}
	}
	Orientacio getOrientacio(Fitxa fitxa){
		if (fitxa.getOrientacio() == Orientacio.V || fitxa.getOrientacio() == Orientacio.H) 
			if (anteriorFitxa(fitxa) != null) return Orientacio.HR;
			else return (Orientacio.HL); // es la primera fitxa i es un doble
		else return fitxa.getOrientacio();
	}
	Orientacio novaOrientacio(Orientacio orientacio,Fitxa fitxa,Fitxa novaFitxa, int nouNivell){// orientacio basada en la de la fitxa anterior
		nouNivell = fitxa.getNivell();
		
		if (novaFitxa.esDoble())
			switch (orientacio) {
			case HL :
			case HR :
				return Orientacio.V;
			case VUL:
			case VUR:
			case VDL:
			case VDR:
				return Orientacio.H;
			}
		else
			switch (orientacio){
			case HL : return Orientacio.HL;
			case VUL : // ha de ser posicionador esquerra
				nouNivell++;
				return Orientacio.HR;
				//if (fitxa.getNivell() % 4 == 1) return Orientacio.HR;
				//else return Orientacio.HL;
			case VUR :
				nouNivell++;
				return Orientacio.HL;
			case VDR : // ha de ser posicionador dret
				nouNivell++;
				return Orientacio.HL;
				//if (fitxa.getNivell() % 4 == 1) return Orientacio.HL;
				//else return Orientacio.HR;
			case VDL:
				nouNivell++;
				return Orientacio.HR;
			case HR : return Orientacio.HR;
			}
		return null;
	}
	Fitxa anteriorFitxa(Fitxa fitxa){
		Fitxa retorn=null;
		if (tipusPosicionador == Tipus.esquerra) return(fitxa.getFitxaDreta()); else return fitxa.getFitxaEsquerra();
		//	if (fitxa == fitxa.getFitxaDreta()) retorn = fitxa.getFitxaDreta();// per  la primera fitxa
		//	else retorn = fitxa.getFitxaEsquerra();
		//else retorn = fitxa.getFitxaDreta();
		
		//if (fitxa== retorn) return null; else return retorn;
	}
	Fitxa seguentFitxa (Fitxa fitxa){
		Fitxa retorn;
		if (tipusPosicionador == Tipus.esquerra) retorn = fitxa.getFitxaEsquerra();
		else retorn = fitxa.getFitxaDreta();
		if (fitxa== retorn) return null; else return retorn;
	}
	
	boolean mouTotesX(float deltaX, boolean esquerra){ 
		
		if (joc.getFitxaL().getXf() != joc.getFitxaR().getXf()) return false; // si no estan totes a la primera fila no podem moureles
		if (esquerra){ // movem capa la esquerra
			float hiCap = joc.getFitxaL().getXf();
			if (joc.getFitxaL().getAngle() == 0 || joc.getFitxaL().getAngle() == 180) 
				hiCap-= joc.getFitxaL().getLonCostat()/2;
			else
				hiCap-=joc.getFitxaL().getLonCostat();
			
			if ( hiCap + deltaX < joc.getFitxaL().getLonCostat()*2) return false; // no hi cap
			Fitxa aux = joc.getFitxaL();
			while (aux!=null){
				aux.marcaNovaPosicio(aux.getXf() + deltaX, aux.getYf(), aux.getAngle(), aux.getOrientacio(), Constants.TempsMoviment);
				aux = aux.getFitxaDreta();
			}
		}
		else { // movem cap a la dreta
			float hiCap = joc.getFitxaR().getXf();
			if (joc.getFitxaL().getAngle() == 0 || joc.getFitxaL().getAngle() == 180) 
				hiCap+= joc.getFitxaL().getLonCostat()/2;
			else
				hiCap+=joc.getFitxaL().getLonCostat();
			
			if ( hiCap + deltaX > Gdx.graphics.getWidth() - joc.getFitxaR().getLonCostat()*2) return false; // no hi cap
			
			for (Fitxa aux = joc.getFitxaR(); aux != null ; aux = aux.getFitxaEsquerra()) 
				aux.marcaNovaPosicio(aux.getXf() - deltaX, aux.getYf(), aux.getAngle(),aux.getOrientacio(), Constants.TempsMoviment);
		}
		return true;
	}
	//private void calculaPos(Fitxa fitxa, Orientacio orientacio, float posX, float posY, float hiCapX, float hiCapY, boolean esquerra, boolean amunt) {
		// TODO Auto-generated method stub
		
	//}
	boolean mouTotesY(float deltaY,boolean amunt){
		float posY;
		Fitxa fitxaPosY=null;
		if (amunt){
			if (joc.getFitxaL().getYf()> joc.getFitxaR().getYf() ) 
				fitxaPosY = joc.getFitxaL();
			else 
				fitxaPosY = joc.getFitxaR();
	
			posY =fitxaPosY.getYf();
			if ((posY - deltaY) > Gdx.graphics.getHeight() - 2*joc.getFitxaL().getYf()- Constants.MargeJugadorsJoc) return false;
			else {
				//movem totes
				if (fitxaPosY.getFitxaDreta() == null)
					for ( Fitxa aux = fitxaPosY ; aux != null; aux = aux.getFitxaEsquerra())
						aux.marcaNovaPosicio(aux.getXf(), aux.getYf() + deltaY, aux.getAngle(), aux.getOrientacio(), Constants.TempsMoviment);
				else
					for ( Fitxa aux = fitxaPosY ; aux != null; aux = aux.getFitxaDreta())
						aux.marcaNovaPosicio(aux.getXf(), aux.getYf() + deltaY, aux.getAngle(), aux.getOrientacio(), Constants.TempsMoviment);
				return true;
				}
		}
		else{
				if (fitxaPosY == joc.getFitxaR()) fitxaPosY = joc.getFitxaL();
				else fitxaPosY = joc.getFitxaR();
				posY = fitxaPosY.getYf();
				if ((posY + deltaY)<= 2* 2*joc.getFitxaL().getYf()- Constants.MargeJugadorsJoc) return false;
				else {
					// movem totes
					if (fitxaPosY.getFitxaDreta() == null)
						for ( Fitxa aux = fitxaPosY ; aux != null; aux = aux.getFitxaEsquerra())
							aux.marcaNovaPosicio(aux.getXf(), aux.getYf() + deltaY, aux.getAngle(), aux.getOrientacio(), Constants.TempsMoviment);
					else
						for ( Fitxa aux = fitxaPosY ; aux != null; aux = aux.getFitxaDreta())
							aux.marcaNovaPosicio(aux.getXf(), aux.getYf() + deltaY, aux.getAngle(), aux.getOrientacio(), Constants.TempsMoviment);
					return true;
				}
			}
	}
		
	boolean escalaTotes(float delta){
		//
		// la veritat es k passo de fer mes calculs
		// reduirem un 10% i suposem que hi cabra, ja ho afinarem mes endavant
		//
		Fitxa aux = joc.getPrimeraFitxa();
		float escala = aux.getEscala()*0.9f;
		int auxValor;
		aux.setEscala(escala);
		for (aux = aux.getFitxaDreta() ; aux !=  null ; aux = aux.getFitxaDreta()) {
			aux.setEscala(escala);
			if (aux.getFitxaEsquerra().getlValue() == aux.getlValue() || aux.getFitxaEsquerra().getlValue() == aux.getrValue()) auxValor = aux.getrValue();
			else auxValor = aux.getlValue();
			posiciona(aux,aux.getFitxaEsquerra(),auxValor);
		}
		for (aux = joc.getPrimeraFitxa().getFitxaEsquerra(); aux != null; aux = aux.getFitxaEsquerra()){
			if (aux.getFitxaDreta().getlValue() == aux.getlValue() || aux.getFitxaDreta().getlValue() == aux.getrValue()) auxValor = aux.getlValue();
			else auxValor = aux.getrValue();
			posiciona(aux,aux.getFitxaDreta(),auxValor);
		}
		return true;
	}
	boolean calculaPos (Fitxa fitxaAnterior,Orientacio orientacioActual){//,float posX, float posY, float noHiCapX, float noHiCapY, boolean esquerra, boolean amunt){
		
		float deltaX,deltaY;
		switch (orientacioActual) {
			case V: // nomes pot anar a la esquerra o a la dreta , perque es doble i esta en vertical
				// X
				deltaX = fitxaAnterior.getLonCostat()*3/2 - Constants.MargeFitxesTirades;
				if (fitxaAnterior.getOrientacio() == Orientacio.HL){ // va a la esquerra 
					posX = fitxaAnterior.getXf() - deltaX;
					hiCapX = posX - lonCostat/2 - limitXE;
					if (hiCapX < 0) esquerra = true;
				}
				else { // va ala dreta
					Gdx.app.debug("litus","va a la dreta, deltax : posX ln fitxaAnterior lnfitxa"+deltaX+" : "+posX+"\n");
					fitxaAnterior.info();
					posX = fitxaAnterior.getXf() + deltaX;
					hiCapX =  limitXD - posX - lonCostat/2;
					if (hiCapX < 0) esquerra = false;
				}
				// Y	
		
				posY = fitxaAnterior.getYf();
				hiCapY = limitYU - posY - lonCostat; 
					
				break;
				
			case H: // va amunt o avall
				deltaY = fitxaAnterior.getLonCostat()*3/2 + Constants.MargeFitxesTirades;
				posX = fitxaAnterior.getXf();
				hiCapX = posX - lonCostat - limitXE;
				if (fitxaAnterior.getOrientacio() == Orientacio.VUL || fitxaAnterior.getOrientacio() == Orientacio.VUR){ // va amunt
					posY = fitxaAnterior.getYf()+deltaY;
					hiCapY = limitYU - posY - lonCostat/2;
				}
				else {// va avall --> VD
					posY = fitxaAnterior.getYf() - deltaY;
					hiCapY = limitYD - posY - lonCostat/2;
				}
				break;
				
			case HL: // H en sentit esquerra
				if (fitxaAnterior.esDoble())
					deltaX = fitxaAnterior.getLonCostat()*3/2 + Constants.MargeFitxesTirades;
				else
					deltaX = fitxaAnterior.getLonCostat()*2 + Constants.MargeFitxesTirades;
				
				posX = fitxaAnterior.getXf() - deltaX;
				hiCapX = posX - lonCostat - limitXE;
				posY = fitxaAnterior.getYf();
				break;
			case HR: //H en sentit dreta
				if (fitxaAnterior.esDoble())
					deltaX = fitxaAnterior.getLonCostat()*3/2 + Constants.MargeFitxesTirades;
				else
					deltaX = fitxaAnterior.getLonCostat()*2 + Constants.MargeFitxesTirades;
				
				posX = fitxaAnterior.getXf() + deltaX;
				hiCapX = limitXD - posX - lonCostat;
				posY = fitxaAnterior.getYf();
				break;
			case VUL: // vertical pujant
				deltaY = fitxaAnterior.getYf()*3/2;
				posY = fitxaAnterior.getYf() + deltaY;
				hiCapY = limitYU - posY;
				switch (fitxaAnterior.getOrientacio()){
				case VUL :
					posX = fitxaAnterior.getXf();
					hiCapX = posX - limitXE;
					break;
				case HL :
					deltaX = fitxaAnterior.getLonCostat()/2;
					posX = fitxaAnterior.getXf() - deltaX;
					hiCapX = posX - limitXE;
				}
				break;
			case VUR :
				deltaY = fitxaAnterior.getYf()*3/2;
				posY = fitxaAnterior.getYf() + deltaY;
				hiCapY = limitYU - posY;
				switch (fitxaAnterior.getOrientacio()){
				case VUR :
					posX = fitxaAnterior.getXf();
					hiCapX = limitXD - posX;
					break;
				case HR :
					deltaX = fitxaAnterior.getLonCostat()/2;
					posX = fitxaAnterior.getXf() + deltaX;
					hiCapX = limitXD - posX;
				}
				break;
			case VDL: // vertical baixant esquerra
				deltaY = fitxaAnterior.getYf()*3/2;
				posY = fitxaAnterior.getYf() - deltaY;
				hiCapY = limitYU - posY;
				switch (fitxaAnterior.getOrientacio()){
				case VDL :
					posX = fitxaAnterior.getXf();
					hiCapX = limitXD - posX;
					break;
				case HL :
					deltaX = fitxaAnterior.getLonCostat()/2;
					posX = fitxaAnterior.getXf() - deltaX;
					hiCapX = limitXD - posX;
					break;
				}
			break;
			}
		Gdx.app.debug("litus", "orientacio , fitxa , posX, posY, hiCapX, hiCapY :: "+
			orientacioActual+","+fitxaAnterior.info2()+","+posX+","+posY+","+
		hiCapX+","+hiCapY);
		if (hiCapX < 0 || hiCapY < 0)
			Gdx.app.error("litus","Entra debug");
		return ( hiCapX >= 0 &&  hiCapY >= 0);
	}
	
	int getValor(Fitxa aux){
		Fitxa seguent = seguentFitxa(aux);
		if (seguent == null)
			if (aux.getlValue() == joc.getlValue()) return aux.getlValue();
			else return aux.getrValue();
		else
			if (aux.getlValue() == seguent.getlValue()) return aux.getlValue();
			else return aux.getrValue();
	}
}

