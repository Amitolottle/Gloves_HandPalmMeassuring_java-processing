package proyectoGuantes_eclipse_V_01;
import processing.core.*;
import gab.opencv.Contour;
import gab.opencv.OpenCV;
import gab.opencv.Line;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;

import org.opencv.core.Mat;



public class BlobCV {
	
	PApplet app;
	PVector leftUpper,rightUpper,leftLower,rightLower,midPoint,tip;
	Rectangle rec, recROI;
	OpenCV opencv;
	ArrayList<Contour> contoursROI;
	Contour contour;
	double longestDist;
	
	public BlobCV(PApplet app,Contour contour) {
		this.app=app;
		rec=null;
		this.contour=contour;
		rec = contour.getBoundingBox();
		leftUpper = new PVector((float) rec.getX(),(float) rec.getY());
		rightUpper = new PVector((float) rec.getX()+(float) rec.getWidth(),(float) rec.getY());
		leftLower = new PVector((float) rec.getX(),(float) rec.getY()+(float) rec.getHeight());
		rightLower = new PVector((float) rec.getX()+(float) rec.getWidth(),(float) rec.getY()+(float) rec.getHeight());
		midPoint = new PVector((leftUpper.x+rightLower.x)/2,(leftUpper.y+rightLower.y)/2);;
		app.println(midPoint.x + " " + midPoint.y);
	}
	
	public void copy(BlobCV bcv) {
		this.contour=bcv.contour;
		this.rec=bcv.rec;
		this.leftUpper=bcv.leftUpper;
		this.rightUpper=bcv.rightUpper;
		this.leftLower=bcv.leftLower;
		this.rightLower=bcv.rightLower;
		this.midPoint=bcv.midPoint;
		this.longestDist=bcv.longestDist;
		this.contoursROI=bcv.contoursROI;
		
	}
	
	public void update(int index) {
		//app.rectMode(PConstants.CORNER);
        app.noFill();
		app.stroke(255,255, 0);
		
		
		app.line(contour.getPoints().get(0).x,contour.getPoints().get(0).y,contour.getPoints().get(contour.getPoints().size()/2).x,contour.getPoints().get(contour.getPoints().size()/2).y);
		app.ellipse(contour.getPoints().get(0).x,contour.getPoints().get(0).y,10,10);
		//app.ellipse(contourL.getPoints().get(contourL.getPoints().size()/2).x,contourL.getPoints().get(contourL.getPoints().size()/2).y,10,10);
		app.strokeWeight(1);
		app.rect(leftUpper.x,leftUpper.y,rec.width,rec.height);
		app.fill(255,0,0,150);
		app.noStroke();
		app.ellipse(midPoint.x, midPoint.y, 10, 10);
		app.fill(255);
		app.text(index,leftUpper.x,leftUpper.y);
		
		if(contoursROI!=null) {
		
		 app.noFill();
         app.stroke(0, 255, 0);
		 app.strokeWeight(1);
		 contoursROI.get(0).getConvexHull().draw();
		 app.fill(255);
		}
		
		if(tip!=null) {
			app.line(midPoint.x, midPoint.y, tip.x, tip.y);
		}
		
		
	}
	
	public double findSlope() {
		double slope;
		slope= contour.getPoints().get(0).x-contour.getPoints().get(contour.getPoints().size()/2).x;
		return slope;
	}
	
	public void searchByROI(PImage src, double pendiente) {
		recROI=null;
		opencv =new OpenCV(app, src);
		  opencv.gray();
		  PImage tmp;
		  opencv.loadImage(src);
		  if(pendiente>0) {
			  opencv.setROI(0, 0, (int)rightLower.x, (int)rightLower.y);
		  }
		  
		  if(pendiente==0) {
			  PImage img = src.get((int)leftLower.x, 0, rec.width, (int)rightUpper.y);
			  PGraphics pg = app.createGraphics(src.width, src.height);
			  pg.beginDraw();
			  pg.background(0);
			  pg.image(img, (int)leftLower.x, 0);
			  pg.endDraw();
			   tmp = pg.copy();
			  opencv.loadImage(tmp);
		  }
		  
		  if(pendiente<0) {
			  PImage img = src.get((int)leftLower.x, 0, src.width, (int)leftLower.y);
			  PGraphics pg = app.createGraphics(src.width, src.height);
			  pg.beginDraw();
			  pg.background(0);
			  pg.image(img, (int)leftLower.x, 0);
			  pg.endDraw();
			   tmp = pg.copy();
			  opencv.loadImage(tmp);
		  }
		  
		
		 contoursROI = opencv.findContours(false,true);
		  
		 
		 
		 ArrayList<Float> dists = new ArrayList<Float>();
		 
		 for(int i=0;i<contoursROI.get(0).getPoints().size();i++) {
			dists.add(midPoint.dist(contoursROI.get(0).getPoints().get(i)));
			//app.println("DISTANCIA AL PUNTO:" + i +" "+ midPoint.dist(contoursROI.get(0).getPoints().get(i)));
		 }
		 
		 longestDist=Collections.max(dists);
		 app.println("DISTANCIA MÄS LARGA: "+longestDist);
		 
		 for(int i=0;i<contoursROI.get(0).getPoints().size();i++) {
				if(midPoint.dist(contoursROI.get(0).getPoints().get(i))==longestDist) {;
				tip = contoursROI.get(0).getPoints().get(i);
				app.println("PUNTO MAS LARGO: "+i);
				break;
				}
				
			 }
		 app.println(tip);
		 //opencv.releaseROI();
	}
}
