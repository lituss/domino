package domino;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
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

	public Joc(int nJugadors, int nFitxesInicial , int maxValor,MyGdxGame game){
		this.nFitxesInicial = nFitxesInicial;
		this.nJugadors = nJugadors;
		batch = game.getBatch();
		this.game = game;
		this.maxValor = maxValor;
	}

private void reparteix(Jugador tornJugador){
	// posem totes les fitxes en un array i les anem repartint 
	Array <Integer> auxFitxes;
	Jugador torn;
	float temps = 10.f; // temps que trigara la fitxa a arribar al lloc

	
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
			temps+=0.1f;
		} while (aux != tornJugador);
	}
	 
}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		fitxes = new Fitxes(maxValor); // creem les fitxes
		jugadors = new Array<Jugador>(nJugadors); //creem Array jugadors
		
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
		/*case 3 :
			jugadors.add( new Jugador(this,PosicioJugador.hBaix));
			jugadors.add( new Jugador(this,PosicioJugador.vDreta));
			jugadors.add( new Jugador(this,PosicioJugador.hAlt));

			jugadors.get(0).setPrimera(Gdx.graphics.getHeight() - (int)(fitxes.pixelsCostat - margin ), (int)mitatY);
			jugadors.get(1).setPrimera(Gdx.graphics.getWidth() - fitxes.pixelsCostat - margin,(int) mitatY);
			jugadors.get(2).setPrimera((int)(fitxes.pixelsCostat  + margin ), (int)mitatY);
			break;
		case 4 : 
			jugadors.add( new Jugador(this,PosicioJugador.hBaix));
			jugadors.add( new Jugador(this,PosicioJugador.vDreta));
			jugadors.add( new Jugador(this,PosicioJugador.hAlt));
			jugadors.add( new Jugador(this,PosicioJugador.vEsquerra));
			jugadors.get(0).setPrimera(Gdx.graphics.getHeight() - (int)(fitxes.pixelsCostat - margin ), (int)mitatY);
			jugadors.get(1).setPrimera(Gdx.graphics.getWidth() - fitxes.pixelsCostat - margin,(int) mitatY);
			jugadors.get(2).setPrimera((int)(fitxes.pixelsCostat  + margin ), (int)mitatY);
			jugadors.get(3).setPrimera(fitxes.pixelsCostat + margin,(int) mitatY);
			break;*/
	}
		tornJugador = jugadors.get(0);
		reparteix(tornJugador);
		
	}
	Fitxes getFitxes(){ return fitxes;}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		fitxes.dinamica(delta);
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
		
	}}
