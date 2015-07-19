package domino;

public class Par {

private	float lValue,rValue;

public Par(int lValue, int rValue){
	this.lValue = lValue;
	this.rValue = rValue;
}
public float getlValue() {
	return lValue;
}
public void setlValue(float lValue) {
	this.lValue = lValue;
}
public float getrValue() {
	return rValue;
}
public void setrValue(float rValue) {
	this.rValue = rValue;
}
public void setValue(float lValue, float rValue){
	this.lValue = lValue;
	this.rValue = rValue;
}
}
