package proyectoGuantes_eclipse_V_01;

import processing.core.PApplet;

public class Marker {
	  private int posX, posY, size;
	  private int id;
	  private PApplet app;
	  public Marker(PApplet app,int posX, int posY, int size) {
		this.app = app;
	    this.posX=posX;
	    this.posY=posY;
	    this.size=size;
	  }

	  public void update() {
	    app.fill(255,90);
	    app.ellipse(posX, posY, size, size);
	    app.textSize(10);
	    app.text(id, posX+(size/2)+2,posY-(size/2)-2); 
	  }
	  
	  public int getPosX(){
	   return posX; 
	  }
	  
	  public int getPosY(){
	   return posY; 
	  }
	  
	  public void setId(int id){
	   this.id=id; 
	  }
	}
