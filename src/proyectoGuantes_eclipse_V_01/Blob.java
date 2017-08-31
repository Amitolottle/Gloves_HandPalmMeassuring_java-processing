package proyectoGuantes_eclipse_V_01;

import processing.core.*;

public class Blob {
	float minX,minY,maxX,maxY;
	PApplet app;
	
	public Blob(PApplet app,float x, float y) {
		this.app=app;
		minX=x;
		minY=y;
		maxX=x; 
		maxY=y ;
	}
	
	public void update(int index) {
		app.noStroke();
		app.fill(255,0,0,50);
		app.rectMode(PConstants.CORNERS);
		app.rect(minX, minY, maxX, maxY);
		app.fill(255);
		app.text(index, minX, minY);
	}
	
	public boolean isNear(float px,float py) {
		float centerX= (minX + maxX)/2;
		float centerY= (minY+maxY)/2; 
		float d= distSq(centerX,centerY,px,py);
		if(d<30*30) {
			return true;
		}else return false; 
	}
	
	void add(float px,float py) {
		minX= app.min(minX,px);
		minY=app.min(minY,py);
		maxX=app.max(maxX,px);
		maxY=app.max(maxY,py);
	}
	
	public float distSq(float x1, float y1, float x2, float y2) {
		float d= (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
		return d;
	}
}
