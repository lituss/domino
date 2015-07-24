package com.mygdx.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import domino.Fitxes;
import domino.Joc;
import domino.test;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	
	public SpriteBatch getBatch() {
		return batch;
	}

	Joc joc;
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.gl20.glLineWidth(2);
		// treurem una screen gui amb el menu amb les opcions del joc, de moment ho fem fixe per probar 
		//Gdx.graphics.
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		//fitxes = new Fitxes(6);
		test myTest = new test(6);
		joc = new Joc(4, 7, 6, this); // numero jugadors // numero fitxes per jugador // valor mes alt de la fitxa
		setScreen(joc);
	}

	//@Override
	//public void render () {
		//Gdx.gl.glClearColor(1, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 0, 0);
		//joc.render(delta);//fitxes.draw(batch);
		//batch.end();
	//}
}
