package domino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fitxa implements Ifitxa {
Sprite img;
private int lonCostat;
private float radius ;
private int lValue,rValue;
private Pixmap pixmap;
private int id;
private Texture texture;
static private Texture negra ;
private Fitxa fitxaEsquerra = null,fitxaDreta=null;
private PosicioFitxaVeina posFitxaEsquerra=null,posFitxaDreta=null;
private Orientacio orientacio = null;
private boolean tapada = false;
private boolean dinamica = false;
private float xf,yf,anglef,vx,vy,w,dt; // x final y final velocidad x velocidad y velocitat de rotacio i temps per arribar al desti dt
static private final float precisio = 2.f;

	public Fitxa (int lonCostat){
		// negre, fitca tapada
		pixmap = new Pixmap(lonCostat,2*lonCostat,Format.RGBA8888);
		pixmap.setColor( Color.BLACK);
		pixmap.fill();
		negra = new Texture(pixmap);
		
	}
	public Fitxa(int lValue, int rValue, int lonCostat,int id){
		this.lonCostat = lonCostat;
		this.lValue = lValue;
		this.rValue = rValue;
		this.setId(id);
		radius = lonCostat / 10;
		pixmap = new Pixmap(lonCostat,2*lonCostat,Format.RGBA8888);
		pixmap.setFilter(Pixmap.Filter.BiLinear);
		pixmap.setColor( Color.WHITE);
		pixmap.fill();
		pixmap.setColor( Color.BLACK);
		pixmap.drawLine(0, lonCostat, lonCostat, lonCostat);
		pixmap.drawRectangle(0, 0, lonCostat  , 2*lonCostat );
		pixmap.setColor( Color.PURPLE);
		pixmap.fillCircle(lonCostat/2, lonCostat, (int) radius);
		pixmap.setColor( Color.BLACK);
		posaPunt(false,lValue);
		posaPunt(true,rValue);
		texture = new Texture(pixmap,Format.RGBA8888,true);
		img = new Sprite(texture);
		//img.setOriginCenter();
		//img.setCenter(lonCostat/2,lonCostat);
		
		
		
		
	}
	public void setPosition(float x, float y){
		img.setPosition(x - img.getOriginX(), y - img.getOriginY());
		//img.setCenter(x, y);
	}
	
	public void draw(SpriteBatch batch){
	
		img.draw(batch);
	}
	
	private void posaPunt(boolean baix,int valor){
		
		switch(valor){
		case 0 : break;
		case 1 : pintaPunt(baix,5);
		break;
		case 2 : pintaPunt(baix,3);
				 pintaPunt(baix,7);
				 break;
		case 3 : pintaPunt(baix,3);pintaPunt(baix,5);pintaPunt(baix,7);break;
		case 4 : pintaPunt(baix,1);pintaPunt(baix,3);pintaPunt(baix,7);pintaPunt(baix,9);break;
		case 5 : pintaPunt(baix,1);pintaPunt(baix,3);pintaPunt(baix,5);pintaPunt(baix,7);pintaPunt(baix,9);break;
		case 6 : pintaPunt(baix,1);pintaPunt(baix,3);pintaPunt(baix,4);pintaPunt(baix,6);pintaPunt(baix,7);pintaPunt(baix,9);break;
		}
	}
	private void pintaPunt( boolean baix,int pos ){
		int posx,posy;
		posx = (((pos - 1 ) % 3) + 1) * lonCostat/4;
		posy = (((pos - 1 ) / 3) + 1) * lonCostat/4;
		if (!baix) posy +=lonCostat;
		pixmap.fillCircle(posx, posy, (int)radius);
	}
	
	public void novaPosicio(float delta){
		float x = img.getX(),y=img.getY();
		if (dinamica){
			if ((Math.abs(x - xf) < precisio) && (Math.abs(y - yf) < precisio)){
				dinamica = false;
				img.setRotation(anglef);
				setPosition(xf, yf);
			}
			else{
				x+=vx*delta;
				y+=vy*delta;
				img.rotate(w*delta);
				setPosition(x, y);
			}
		}
			
	}
	public void marcaNovaPosicio(float x, float y , Orientacio orientacio, float temps){
		float angle = img.getRotation();
		//temps*=1000;
		xf = x;
		yf = y;
		vx = (xf - getX())/temps;
		vy = (yf - getY())/temps;
		if (orientacio == Orientacio.Vertical)
			anglef = 0;
		else
			anglef = 90;
	
		w = (anglef - angle)/temps;
		
		dinamica = true;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getX(){
		return img.getX();
	}
	public float getY(){
		return img.getY();
	}
	public float getXf(){
		return xf;
	}
	public float getYf(){
		return yf;
	}
	public int getLonCostat(){
		return lonCostat;
	}
	public void tapa(boolean tapada){
		if (tapada) img.setTexture(negra);
		else img.setTexture(texture);
	}
	public void info(){
		Gdx.app.log("litus","costat -> origen i centre : "+lonCostat+" --> "+img.getOriginX()+" , "+img.getOriginY()+" :: "+img.getX() + " , "+img.getY());
	}
}