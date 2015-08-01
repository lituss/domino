package domino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.async.ThreadUtils;
import com.mygdx.test.MyGdxGame;

public class Joc implements Screen{
	private Array <Fitxa> fitxesJugades;
	private Fitxes fitxes;
	private Array <Jugador> jugadors;
	private Jugador tornJugador;
	private int nFitxesInicial,nJugadors;
	private MyGdxGame game;
	private SpriteBatch batch;
	private int maxValor;
	private final int margin = 0;
	private boolean totesQuietes = false;
	private Fitxa primeraFitxa=null;
	private Fitxa fitxaL,fitxaR;
	private int lValue,rValue;
	private Posicionador posicionadorR,posicionadorL;

	public Joc(int nJugadors, int nFitxesInicial , int maxValor,MyGdxGame game){
		this.nFitxesInicial = nFitxesInicial;
		this.nJugadors = nJugadors;
		batch = game.getBatch();
		this.game = game;
		this.maxValor = maxValor;
		posicionadorR = new Posicionador(Posicionador.Tipus.esquerra,this);
		posicionadorL = new Posicionador(Posicionador.Tipus.dreta,this);
	}

private void reparteix(Jugador tornJugador){
	// posem totes les fitxes en un array i les anem repartint 
	Array <Integer> auxFitxes;
	Jugador torn;
	float temps = 2.f; // temps que trigara la fitxa a arribar al lloc

	
	auxFitxes = new Array<Integer>(false,0);
	for (Fitxa aux : fitxes.fitxa){
		auxFitxes.add(aux.getId());
	}
	
	for (int perRepartir = nFitxesInicial ; perRepartir > 0 ; perRepartir --){
		Jugador aux = tornJugador;
		int auxFitxa;
		do{
			auxFitxa = auxFitxes.random();
			auxFitxes.removeIndex(auxFitxes.indexOf(auxFitxa, true));
			aux.rebFitxa(fitxes.fitxa.get(auxFitxa),temps);
			aux = aux.getSeguentJugador();
			temps+=0.05f;
		} while (aux != tornJugador);
	}
	 
}

public void juga(){
	Fitxa fitxa=null;
	//while (!totesQuietes) ;//ThreadUtils.yield(); // esperem que estiguin les fitxes repartides
	
	// mirem qui te el doble mes alt , la primera tirada es especial
	Timer.instance().clear();
	Gdx.app.log("litus", "Ja estan repartides i situades");
	//Gdx.app.exit();
	boolean surt = false;
	for (int dobles = maxValor ; dobles >= 0 && !surt ; dobles --){
		int id = fitxes.indexFitxa.get(dobles).get(dobles);//new Par((float)dobles,(float)dobles));
		for ( Jugador auxJugador :jugadors)
			if ((fitxa = auxJugador.posaFitxa(id)) != null){
				setPrimeraFitxa(fitxa);
				setlValue(dobles);setrValue(dobles);
				setFitxaR(fitxa);setFitxaL(fitxa);
				// lliguem les fitxes del jugador treient la que tirem
				//fitxa.getFitxaEsquerra().setFitxaEsquerra(null);
				//fitxa.getFitxaDreta().setFitxaDreta(null);
				tornJugador = auxJugador;
				//posaFitxa(fitxa);
				surt = true;
				break;
			}
	}
	
	// reste de tirades
	Jugador ultimQueHaTirat = tornJugador;
	Fitxa ultimaFitxa = fitxa,fitxaAnterior = null;
	int valor=0,passa = 0;
	boolean win = false;
	do {
		tornJugador = tornJugador.getSeguentJugador();
		ultimaFitxa = tornJugador.posaFitxa(fitxaL, fitxaR, fitxaAnterior, valor);
		if (ultimaFitxa == null ) passa++;
		else {
			posa (ultimaFitxa,fitxaAnterior,valor);
			passa = 0;
		}
	} while (tornJugador.contaFitxes() == 0 || passa == nJugadors );
	
	
	}

void posa(Fitxa fitxa, Fitxa fitxaAnterior, int valor){
	if (fitxaL == fitxaAnterior){
		posicionadorL.posiciona (fitxa,fitxaAnterior,valor);
		setFitxaL(fitxa);
		setlValue(valor);
	}
	else {
		posicionadorR.posiciona(fitxa, fitxaAnterior, valor);
		setFitxaR(fitxa);
		setrValue(valor);
	}
}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		fitxes = new Fitxes(maxValor); // creem les fitxes
		jugadors = new Array<Jugador>(false,0); //creem Array jugadors
		
		// enllaçem jugadors
		//int g = 1;
		//for (int f = 0 ; f < nJugadors - 1; f++)
		//	jugadors.items[f].setSeguentJugador(jugadors.items[g++]);
		//jugadors.items[nJugadors - 1].setSeguentJugador(jugadors.items[0]);
		// els posicionem a la partida
		int mitatX = Gdx.graphics.getWidth() / 2, mitatY = Gdx.graphics.getHeight()/2;
		int xInicial,yInicial;
		switch (nJugadors){
		case 2 :
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hBaix));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hAlt));
			jugadors.get(0).setSeguentJugador(jugadors.get(1));
			jugadors.get(1).setSeguentJugador(jugadors.get(0));
			break;
		case 3 :
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hBaix));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.vDreta));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hAlt));
			jugadors.get(0).setSeguentJugador(jugadors.get(1));
			jugadors.get(1).setSeguentJugador(jugadors.get(2));
			jugadors.get(2).setSeguentJugador(jugadors.get(0));
			break;
		case 4 : 
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hBaix));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.vDreta));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.hAlt));
			jugadors.add( new Jugador(this,nFitxesInicial,PosicioJugador.vEsquerra));
			jugadors.get(0).setSeguentJugador(jugadors.get(1));
			jugadors.get(1).setSeguentJugador(jugadors.get(2));
			jugadors.get(2).setSeguentJugador(jugadors.get(3));
			jugadors.get(3).setSeguentJugador(jugadors.get(0));
			break;
	}
		tornJugador = jugadors.get(0);
		reparteix(tornJugador);
		Timer.schedule(new Task() {
			@Override
			public void run(){
				if (totesQuietes){
				juga();
				}
			}
		},1, 1);
		
		//if (totesQuietes) // comença el joc
		
	}
	Fitxes getFitxes(){ return fitxes;}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		totesQuietes = fitxes.dinamica(delta);
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		fitxes.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public Fitxa getFitxaL() {
		return fitxaL;
	}

	public void setFitxaL(Fitxa fitxaL) {
		this.fitxaL = fitxaL;
	}

	public int getrValue() {
		return rValue;
	}

	public void setrValue(int rValue) {
		this.rValue = rValue;
	}

	public Fitxa getFitxaR() {
		return fitxaR;
	}

	public void setFitxaR(Fitxa fitxaR) {
		this.fitxaR = fitxaR;
	}

	public int getlValue() {
		return lValue;
	}

	public void setlValue(int lValue) {
		this.lValue = lValue;
	}

	public Fitxa getPrimeraFitxa() {
		return primeraFitxa;
	}

	public void setPrimeraFitxa(Fitxa primeraFitxa) {
		this.primeraFitxa = primeraFitxa;
	}}
