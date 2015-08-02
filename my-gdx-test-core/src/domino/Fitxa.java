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
//private Orientacio posFitxaEsquerra=null,posFitxaDreta=null;
private Orientacio orientacio;
private boolean tapada = false;
private boolean dinamica = false;
private float xf,yf,anglef,vx,vy,w,dt; // x final y final velocidad x velocidad y velocitat de rotacio i temps per arribar al desti dt
private int nivell; // 0 -> primera horitzontal 1-> pujant 2-> segona horitzontal (a sobre) ..-1 -> baixant -2 -> segona horitzontal per vaix
static private final float precisio = 4.f;

	public Fitxa (int lonCostat){
		// negre, fitca tapada
		pixmap = new Pixmap(lonCostat,2*lonCostat,Format.RGBA8888);
		pixmap.setColor( Color.BLACK);
		pixmap.fill();
		negra = new Texture(pixmap);
		
	}
	public Fitxa(int lValue, int rValue, int lonCostat,int id){
		this.lonCostat = lonCostat;
		this.setlValue(lValue);
		this.setrValue(rValue);
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
		//img.setPosition(x - img.getOriginX(), y - img.getOriginY());
		img.setCenter(x, y);
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
	
	public boolean novaPosicio(float delta){
		float x = getX(),y=getY();
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
			//info();
			return true;
		}
		return false;	
	}
	public void marcaNovaPosicio(float x, float y , float anglef, Orientacio orientacio, float temps){
		float angle = img.getRotation();
		//temps*=1000000;
		xf = x;
		yf = y;
		vx = (xf - getX())/temps;
		vy = (yf - getY())/temps;
		this.anglef = anglef;
		this.orientacio = orientacio;
	
		w = (anglef - angle)/temps;
		
		dinamica = true;
		Gdx.app.log("litus", "x,y,xf,yf,vx,vy : "+getX()+","+getY()+","+xf+","+yf+","+vx+","+vy);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getX(){
		return img.getX()+img.getOriginX();
	}
	public float getY(){
		return img.getY() + img.getOriginY();
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
		Gdx.app.log("litus","x,y : xf,yf --> "+getX() + " , "+getY()+" : "+xf+" , "+yf);
	}
	public Fitxa getFitxaEsquerra() {
		return fitxaEsquerra;
	}
	public void setFitxaEsquerra(Fitxa fitxaEsquerra) {
		this.fitxaEsquerra = fitxaEsquerra;
	}
	public Fitxa getFitxaDreta() {
		return fitxaDreta;
	}
	public void setFitxaDreta(Fitxa fitxaDreta) {
		this.fitxaDreta = fitxaDreta;
	}
	public int getlValue() {
		return lValue;
	}
	public void setlValue(int lValue) {
		this.lValue = lValue;
	}
	public int getrValue() {
		return rValue;
	}
	public void setrValue(int rValue) {
		this.rValue = rValue;
	}
	public Orientacio getOrientacio() {
		return orientacio;
	}
	public void setOrientacio(Orientacio orientacio) {
		this.orientacio = orientacio;
	}
	public int getNivell() {
		return nivell;
	}
	public void setNivell(int nivell) {
		this.nivell = nivell;
	}
	public boolean esDoble(){
		return (lValue == rValue ? true : false);
	}
	public float getAngle(){
		return anglef;
	}
	public float getEscala() {
		return img.getScaleX();
	}
	public void setEscala(float escala) {
		img.setScale(escala);
	}
	public String info2(){
		String retorn = " "+lValue+":"+rValue+" ";
		return retorn;
	}
}
