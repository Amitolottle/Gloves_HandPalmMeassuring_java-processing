package proyectoGuantes_eclipse_V_01;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import processing.core.*;
import gab.opencv.OpenCV;
import gab.opencv.Contour;

import java.awt.Color;
import java.awt.Rectangle;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


import org.opencv.core.Size;

public class main extends PApplet {

	PImage src, canny, BNW, original, template, maskedBG, showcaseChromCoor, showcaseCanny, showcaseBNW, showcaseOriginal;
	int xTemp, xTemp2;
	double C3Pix, C12Pix, C15Pix, C16Pix, C17Pix, L1Pix, L2Pix, L3Pix, L4Pix, L5Pix, L8Pix, L11Pix, L12Pix, L13Pix;
	double C3MM, C12MM, C15MM, C16MM, C17MM, L1MM, L2MM, L3MM, L4MM, L5MM, L8MM, L11MM, L12MM, L13MM;
	boolean trigger;
	Marker extremoCorazon, C17PuntoMedio;
	ArrayList<Marker> markers;
	ArrayList<Marker> markersMuneca;
	ArrayList<Marker> markersMenique;
	ArrayList<Marker> markersPulgar;
	ArrayList<Marker> markersMano;
	ArrayList<Marker> markersDist;
	int[] list;
	boolean dibujarLineas, ocultarDatos, mostrarAgentes, mostrarRegiones, ocultarOriginal;
	
	OpenCV opencv;
	ArrayList<Contour> contours;
	ArrayList<BlobCV> blobscv;
	
	float threshold = 10;
	ArrayList<Blob> blobs;


	public void settings() {
		size(993, 700);
	}

	public void setup() {
		
		/*
		  oscP5 = new OscP5(this, 12000);//sending from this port
		  myRemoteLocation = new NetAddress("127.0.0.1", 12001);
		  */

		 
		  original = loadImage("./img/proyectoGuantes_fotoBase_palmaCauchosKammilFIXED.jpg");
		  //original = loadImage("./img/proyectoGuantes_fotoBase_palmaCauchosSenorFIXED.jpg");
		  //original = loadImage("./img/proyectoGuantes_fotoBase_palmaCauchosKammilFLIPED.jpg");
		  //original = loadImage("./img/proyectoGuantes_fotoBase_palmaCauchosSenorFLIPED.jpg");
		  //original = loadImage("./img/p.jpg");
		  src = original.copy();
		  src.resize(src.width/5, src.height/5);
		  template=src.copy();
		  BNW=coordenadasCromaticas(src);
		  showcaseOriginal= src.copy();
		  showcaseOriginal.resize(showcaseOriginal.width/2, showcaseOriginal.height/2);
		  println(src.height);
		  markers = new ArrayList<Marker>();
		  markersMuneca= new ArrayList<Marker>();
		  markersMenique= new ArrayList<Marker>();
		  markersPulgar=new ArrayList<Marker>();
		  markersMano = new ArrayList<Marker>();
		  markersDist = new ArrayList<Marker>();
		  System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		  
		  blobs= new ArrayList<Blob>(); 
		  
		  blobscv= new ArrayList<BlobCV>();
		  
		  measureHand();
	}

	public void draw() {

		  fill(0);
		  rect(0, 0, BNW.width, BNW.height);

		  //image(maskedBG, 0, 0);
		  image(showcaseOriginal, template.width, 0);
		  image(showcaseChromCoor, template.width, showcaseChromCoor.height);
//		  image(showcaseCanny, showcaseChromCoor.width+template.width, 0);
		  image(showcaseBNW, showcaseChromCoor.width+template.width, showcaseChromCoor.height);
		  if (!ocultarOriginal) {
		    image(template, 0, 0);
		  }

		  if (!ocultarDatos) {

		    fill(0);
		    textSize(10);
		    /*
		    text("C12: Circunferencia de la articulación distal interfalángica del dígito 3: "+ C12MM, markers.get(markers.size()-1).getPosX()+20, markers.get(markers.size()-1).getPosY()-10, 200, 600);
		    text("C15: Circunferencia de la mano: "+ mmToPixel(300, reglaDeTres(C15Pix, original.width, src.width)), markersMano.get(0).getPosX()-160, markersMano.get(0).getPosY()-10, 200, 600);
		    text("C17: Circunferencia de la muñeca: "+ mmToPixel(300, reglaDeTres(C17Pix, original.width, src.width)), markersMuneca.get(markersMuneca.size()-1).getPosX()+5, markersMuneca.get(markersMuneca.size()-1).getPosY()+10, 100, 600);
		    text("L8: Largo de la mano: "+ mmToPixel(300, reglaDeTres(L8Pix, original.width, src.width)), markersMuneca.get(0).getPosX()+40, markersMuneca.get(0).getPosY()+10, 50, 600);
		    text("L12: Largo de la base del meñique a la muñeca externa: "+ mmToPixel(300, reglaDeTres(L12Pix, original.width, src.width)), markersMenique.get(0).getPosX()+40, markersMenique.get(0).getPosY()+100, 70, 600);
		    text("L11: Largo de la base del índice a la base del pulgar: "+ mmToPixel(300, reglaDeTres(L11Pix, original.width, src.width)), markersPulgar.get(0).getPosX()+20, markersPulgar.get(0).getPosY()-20, 150, 600);

		    text("L13: Largo de la base del pulgar a la muñeca interna: "+ mmToPixel(300, reglaDeTres(L13Pix, original.width, src.width)), markersMuneca.get(0).getPosX()-160, markersMuneca.get(0).getPosY()-90, 120, 600);
		    text("L3: Largo del dedo del dígito 3: "+ mmToPixel(300, reglaDeTres(L3Pix, original.width, src.width)), extremoCorazon.getPosX()+10, extremoCorazon.getPosY()-30, 200, 600);
*/
		    /*/>-- DIBUJAR LINEAS
		    stroke(0);
		    strokeWeight(2);
		    drawLine(markers.get(0), markers.get(markers.size()-1));
		    drawLine(markersMuneca.get(0), markersMuneca.get(markersMuneca.size()-1));
		    drawLine(markersMano.get(0), markersMano.get(markersMano.size()-1));
		    drawLine(markersMenique.get(markersMenique.size()-1), markersMuneca.get(markersMuneca.size()-1));
		    drawLine(markersPulgar.get(0), markersMuneca.get(0));
		    drawLine(extremoCorazon, C17PuntoMedio);
		    drawLine(markersMano.get(0), markersPulgar.get(0));
		    */
		    stroke(255,255,0);
		    strokeWeight(1);
		    //line(blobscv.get(4).leftUpper.x,blobscv.get(4).midPoint.y,blobscv.get(4).rightUpper.x,blobscv.get(4).midPoint.y);
		    
		    line(blobscv.get(7).tip.x,blobscv.get(7).tip.y,blobscv.get(0).midPoint.x,blobscv.get(0).midPoint.y);
		    line(blobscv.get(1).rightUpper.x,blobscv.get(1).rightUpper.y,blobscv.get(2).leftUpper.x,blobscv.get(2).leftUpper.y);
		    
		    if(blobscv.get(1).midPoint.x>blobscv.get(7).midPoint.x) {
		    line(blobscv.get(1).rightLower.x,blobscv.get(1).rightLower.y,blobscv.get(0).rightUpper.x,blobscv.get(0).rightUpper.y);
		    line(blobscv.get(1).leftUpper.x,blobscv.get(1).leftUpper.y,blobscv.get(6).rightLower.x,blobscv.get(6).rightLower.y);
		    line(blobscv.get(4).leftLower.x,blobscv.get(4).leftLower.y,blobscv.get(0).leftUpper.x,blobscv.get(0).leftUpper.y);
		    }else {
		    line(blobscv.get(1).rightUpper.x,blobscv.get(1).rightUpper.y,blobscv.get(6).leftLower.x,blobscv.get(6).leftLower.y);
		    line(blobscv.get(4).rightLower.x,blobscv.get(4).rightLower.y,blobscv.get(0).rightUpper.x,blobscv.get(0).rightUpper.y);
		    line(blobscv.get(1).leftLower.x,blobscv.get(1).leftLower.y,blobscv.get(0).leftUpper.x,blobscv.get(0).leftUpper.y);
		    }
		    
		  }

		  noStroke();
		  if (mostrarRegiones) {
		    fill(255, 0, 0, 40);
		    rect(0, 0, canny.width, (canny.height/2)+5);
		    fill(0, 255, 0, 40);
		    rect(0, (canny.height)*3/4, canny.width, canny.height);
		    fill(0, 0, 255, 40);
		    rect(((canny.width)*7/10)+10, (canny.height)*5/10, 142, ((canny.height)*6/10)-10);
		    fill(255, 255, 0, 40);
		    rect(0, canny.height/2, ((canny.width)*4/10), canny.height);
		  }

		  fill(0);
		  textSize(20);
		  text("Proyecto Guantes_V0.01a", 20, 40);
		  
		  /*
		  for (int i=0;i<markersDist.size();i++) {
			  markersDist.get(i).update();
		  }
		  */
		  
		  
		  for (int i=0;i<blobscv.size();i++) {
			  blobscv.get(i).update(i);	
		  }
		  
	}
	
	void measureHand() {

		  //>-- ENCONTRAR PUNTA DE CORAZON E IMAGEN BLANCO Y NEGRO
		colorMode(HSB,360,100,100);
		  BNW.loadPixels();
		  for (int y=0; y<BNW.height; y++) {
		    for (int x=0; x<BNW.width; x++) {
		      int loc = x+y*BNW.width;
		      float r = red(BNW.pixels[loc]);
		      float g = green(BNW.pixels[loc]);
		      float b = blue(BNW.pixels[loc]);
		      float h = hue(BNW.pixels[loc]);
		      float s = saturation(BNW.pixels[loc]);
		      float br = brightness(BNW.pixels[loc]);
		     
		      //if (r<96.79) {
		      if(h>0 && h<190 && s>20 && br>20) {
			        BNW.pixels[loc]=color(0,0,100);
			      }
		       else {
		        
		        BNW.pixels[loc]=color(0);
		      }

		     
		      /*if (r<=56 && g>=90 && b<=130) {
		        BNW.pixels[loc]=color(255, 255, 255);
		      }*/
		    }
		  }
		  
		  

		outerloop:
		  for (int y=0; y<BNW.height; y++) {
		    for (int x=0; x<BNW.width; x++) {
		      int loc = x+y*BNW.width;
		      float br = brightness(BNW.pixels[loc]);
		      if (br>=20) {
		        extremoCorazon= new Marker(this,x, y, 2);
		        BNW.pixels[loc]=color(0, 100, 100);
		        break outerloop;
		      }
		    }
		  }

		  BNW.updatePixels();
		  PImage invertTemp = BNW.copy();
		  invertTemp.filter(INVERT);
		  maskedBG=BNW.copy();
		  maskedBG.mask(invertTemp);
		  maskedBG.filter(INVERT);
		  showcaseBNW=BNW.copy();
		  showcaseBNW.resize(showcaseBNW.width/2, showcaseBNW.height/2);

		  //>-- SEPARAR CAUCHOS
		  canny = coordenadasCromaticas(src).copy();
		  colorMode(HSB,360,100,100);
		  canny.loadPixels();
		  for (int y=0; y<canny.height; y++) {
		    for (int x=0; x<canny.width; x++) {
		      int loc = x+y*canny.width;
		      float r = red(canny.pixels[loc]);
		      float g = green(canny.pixels[loc]);
		      float b = blue(canny.pixels[loc]);
		      float h = hue(canny.pixels[loc]);
		      float s = saturation(canny.pixels[loc]);
		      float br = brightness(canny.pixels[loc]);
		      
		      //if (r<=56 && g>=90 && b<=130) {
		      if(h>70 && h<190 && s>50 && br>30) {
		        canny.pixels[loc]=color(0,100,100);
		      } else {
		        canny.pixels[loc]=color(0,0,0);
		      }
		    }
		  }
		  canny.updatePixels();
		  colorMode(RGB,255,255,255);
		  
		  //>-- PROCESSING'S OPENCV BLOBS
		  opencv =new OpenCV(this, canny.width, canny.height);
		  opencv.gray();
		  opencv.loadImage(canny);
		  contours = opencv.findContours(false, false);
		  println("TAMAÑO CONTORNOS: "+contours.size());
		  
		  for(int i=contours.size()-1;i>=0;i--) {
			  if(contours.get(i).area()<300) {
				  contours.remove(i);
			  }
		  }
		  
		  for(int i=0;i<contours.size();i++) {
			  blobscv.add(new BlobCV(this,contours.get(i)));
		  }
		  
		  //ARRANGE THE RING AND INDEX FINGERS
		  if(blobscv.get(6).midPoint.x>blobscv.get(7).midPoint.x && blobscv.get(1).midPoint.x<blobscv.get(7).midPoint.x) {
			  BlobCV blobRing = new BlobCV(this,contours.get(6));
			  blobscv.get(6).copy(blobscv.get(5));
			  blobscv.get(5).copy(blobRing);
		  }
		  
		  if(blobscv.get(6).midPoint.x<blobscv.get(7).midPoint.x && blobscv.get(1).midPoint.x>blobscv.get(7).midPoint.x) {
			  BlobCV blobRing = new BlobCV(this,contours.get(6));
			  blobscv.get(6).copy(blobscv.get(5));
			  blobscv.get(5).copy(blobRing);
		  }
		  
		  println("TAMAÑO CONTORNOS: "+contours.size());
		  
		  /*
		  //>-- PIXELS BLOBS
		  canny.loadPixels();
		  for (int y=0; y<canny.height*3/4; y++) {
			    for (int x=0; x<canny.width; x++) {
			      int loc = x+y*canny.width;
			      float br = brightness(canny.pixels[loc]);
			      if (br>=5) {
			    	  boolean found=false;
			    	  for(Blob b:blobs) {
			    		  if(b.isNear(x,y)) {
			    			   b.add(x, y);
			    			   found=true;
			    			   break;
			    		  }
			    	  }
			    	  if(!found) {
			        Blob b = new Blob(this,x,y);
			        blobs.add(b);
			    	  }
			      }
			    }
			  }
		  println("DONE PIXELS BLOBS");
		  
		  canny.updatePixels();
		  
		  
		  //>-- OPENCV IMAGE PROCESSING 
		  PImage cannyTemp = createImage(canny.width,canny.height, RGB);
		  cannyTemp = canny.get();
		  cannyTemp.save(dataPath("canny.jpg"));
		  
		  Mat srcBNW = Imgcodecs.imread("./data/canny.jpg", CvType.CV_8UC1);
			
			System.out.println("Applying Distance Transform...");
			Mat srcDist = new Mat();
			Imgproc.distanceTransform(srcBNW, srcDist, Imgproc.CV_DIST_L2, Imgproc.CV_DIST_MASK_PRECISE);
			Core.normalize(srcDist, srcDist, 0, 20, Core.NORM_MINMAX);
		  
		  String filename = "./data/srcDist.jpg";
	      System.out.println(String.format("Done. Writing %s", filename));
	      Imgcodecs.imwrite(filename, srcDist);
	      
	    //>-- DETECT BRIGHTEST PIXELS
	      PImage imgDist= loadImage("./data/srcDist.jpg");
	      ArrayList<Float> brValues = new ArrayList<Float>();
	      imgDist.loadPixels();
	      for (int y=0; y<imgDist.height; y++) {
			    for (int x=0; x<imgDist.width; x++) {
			      int loc = x+y*imgDist.width;
			      float br = brightness(imgDist.pixels[loc]);
			      if (br>=5) {
			        brValues.add(br);
			       
			      }
			    }
			  }
	      
	      for (int y=0; y<imgDist.height; y++) {
			    for (int x=0; x<imgDist.width; x++) {
			      int loc = x+y*imgDist.width;
			      float br = brightness(imgDist.pixels[loc]);
			      
			      if (br>=Collections.max(brValues)-1) {
			        markersDist.add(new Marker(this,x,y,10));
			      }
			    }
			  }
	      
	      println("MIN VALUE: "+Collections.min(brValues));
	      println("MAX VALUE: "+Collections.max(brValues));
	      imgDist.updatePixels();*/
		  /*
		  //>-- SEPARAR REGION CORAZON
		  canny.loadPixels();
		  for (int y=0; y<(canny.height/2)+5; y++) {
		    for (int x=0; x<canny.width; x++) {
		      int loc = x+y*canny.width;
		      float br = brightness(canny.pixels[loc]);
		      if (br>=80) {
		        canny.pixels[loc]=color(255, 0, 0);
		        markers.add(new Marker(this,x, y, 1));
		      }
		    }
		  }

		  //>-- SEPARAR REGION MUNECA

		  for (int x=0; x<canny.width; x++) {
		    for (int y=(canny.height)*3/4; y<canny.height; y++) {
		      int loc = x+y*canny.width;
		      float br = brightness(canny.pixels[loc]);
		      if (br>=80) {
		        canny.pixels[loc]=color(0, 255, 0);
		        markersMuneca.add(new Marker(this,x, y, 1));
		      }
		    }
		  }

		  //>-- SEPARAR REGION MENIQUE
		  for (int y=(canny.height)*5/10; y<((canny.height)*6/10)-10; y++) {
		    for (int x=((canny.width)*7/10)+10; x<canny.width; x++) {
		      int loc = x+y*canny.width;
		      float br = brightness(canny.pixels[loc]);
		      if (br>=80) {
		        canny.pixels[loc]=color(0, 0, 255);
		        markersMenique.add(new Marker(this,x, y, 1));
		      }
		    }
		  }

		  //>-- SEPARAR REGION PULGAR
		  for (int y=canny.height/2; y<canny.height; y++) {
		    for (int x=0; x<((canny.width)*4/10); x++) {
		      int loc = x+y*canny.width;
		      float br = brightness(canny.pixels[loc]);
		      if (br>=80) {
		        canny.pixels[loc]=color(255, 255, 0);
		        markersPulgar.add(new Marker(this,x, y, 1));
		      }
		    }
		  }

		  canny.updatePixels();

		  canny.loadPixels();
		  //>-- SEPARAR REGION CIRCUNFERENCIA MANO
		  for (int y=0; y<canny.height; y++) {
		    for (int x=0; x<canny.width; x++) {
		      int loc = x+y*canny.width;
		      float br = brightness(canny.pixels[loc]);
		      float st = saturation(canny.pixels[loc]);
		      if (br>=80 && st <=5) {
		        canny.pixels[loc]=color(0, 255, 255);
		        markersMano.add(new Marker(this,x, y, 1));
		      }
		    }
		  }
		  canny.updatePixels();

*/
		  showcaseChromCoor =coordenadasCromaticas(template);
		  showcaseChromCoor.resize(showcaseChromCoor.width/2, showcaseChromCoor.height/2);
/*
		  showcaseCanny = canny.copy();
		  showcaseCanny.resize(showcaseCanny.width/2, showcaseCanny.height/2);*/

		  //>-- ENCONTRAR MEDIDAS
		  /* If Left Left Hand
		   * 0 >-- Wrist
		   * 1 >-- Thumb's root
		   * 2 >-- Circunference between thumb and palm
		   * 3 >-- Hand Circunference
		   * 4 >-- Little finger root
		   * 5 >-- Ring finger root
		   * 6 >-- Index finger root
		   * 7 >-- Middle finger root
		   * 8 >-- Middle finger interphalangical interception
		   */
		  
		//>-- ENCONTRAR CIRCUNFERENCIAS
		  /*markers.get(0).update();
		  markers.get(markers.size()-1).update(); 
		  double C12=findDistance(markers.get(0), markers.get(markers.size()-1));
		  C12Pix=C12;
		  println("C12: "+ C12);
		  println("original C12: " + reglaDeTres(C12, original.width, src.width));
		  println("Real C12: " + mmToPixel(300, reglaDeTres(C12, original.width, src.width)));
		  println("C12 Circunference: "+measureCircunference(mmToPixel(300, reglaDeTres(C12, original.width, src.width)), 20));
		  C12MM= measureCircunference(mmToPixel(300, reglaDeTres(C12, original.width, src.width)), 20);*/
		  
		  //>-- MIDDLE FINGER INTERPHALANGICAL CIRCUNFERENCE
		  double C3= findPlainDistance(blobscv.get(8));
		  C3Pix=C3;
		  println("MIDDLE FINGER INTERPHALANGICAL CIRCUNFERENCE: C3");
		  println("C3: "+ C3);
		  println("original C3: " + reglaDeTres(C3, original.width, src.width));
		  println("Real C3: " + mmToPixel(300, reglaDeTres(C3, original.width, src.width)));
		  C3MM=measureCircunference(mmToPixel(300, reglaDeTres(C3, original.width, src.width)), 15);
		  println("C3 Circunference 56: " + C3MM);
		  
		  //--------------------------------------
		  println(" ");
		  
		  //>-- MIDDLE FINGER ROOT CIRCUNFERENCE
		  double C12 = findPlainDistance(blobscv.get(7));
		  C12Pix=C12;
		  println("MIDDEL FINGER ROOT CIRCUNFERENCE: C12");
		  println("C12: "+ C12);
		  println("original C12: " + reglaDeTres(C12, original.width, src.width));
		  println("Real C12: " + mmToPixel(300, reglaDeTres(C12, original.width, src.width)));
		  C12MM=measureCircunference(mmToPixel(300, reglaDeTres(C12, original.width, src.width)), 15);
		  println("C12 Circunference 63: "+ C12MM);
		  
		  //--------------------------------------
		  println(" ");
		  /*markersMano.get(0).update();
		  markersMano.get(markersMano.size()-1).update(); 
		  double C15=findDistance(markersMano.get(0), markersMano.get(markersMano.size()-1));
		  C15Pix=C15;
		  println("C15: "+ C15);
		  println("original C15: " + reglaDeTres(C15, original.width, src.width));
		  println("Real C15: " + mmToPixel(300, reglaDeTres(C15, original.width, src.width)));
		  println("C15 Circunference: "+measureCircunference(mmToPixel(300, reglaDeTres(C15, original.width, src.width)), 25));
		  C15MM= measureCircunference(mmToPixel(300, reglaDeTres(C15, original.width, src.width)), 25);*/
		  
		  //>-- HAND CIRCUNFERENCE
		  double C15 = blobscv.get(3).leftUpper.dist(blobscv.get(3).rightLower);
		  C15Pix=C15;
		  println("HAND CIRCUNFERENCE: C15");
		  println("C15: "+ C15);
		  println("original C15: " + reglaDeTres(C15, original.width, src.width));
		  println("Real C15: " + mmToPixel(300, reglaDeTres(C15, original.width, src.width)));
		  C15MM= measureCircunference(mmToPixel(300, reglaDeTres(C15, original.width, src.width)), 25);
		  println("C15 Circunference: "+ C15MM);
		  
		  //--------------------------------------
		  println(" ");
		  /*markersMuneca.get(0).update();
		  markersMuneca.get(markersMuneca.size()-1).update(); 
		  double C17=findDistance(markersMuneca.get(0), markersMuneca.get(markersMuneca.size()-1));
		  C17Pix=C17;
		  println("C17: "+ C17);
		  println("original C17: " + reglaDeTres(C17, original.width, src.width));
		  println("Real C17: " + mmToPixel(300, reglaDeTres(C17, original.width, src.width)));
		  C17MM=mmToPixel(300, reglaDeTres(C17, original.width, src.width));*/
		
		  
		  //>-- CIRCUNFERENCE BETWEEN THUMB AND PALM
		  double C16 = blobscv.get(2).rightUpper.dist(blobscv.get(2).leftLower);
		  C16Pix=C16;
		  println("CIRCUNFERENCE BETWEEN THUMB AND PALM: C16");
		  println("C16: "+ C16);
		  println("original C16: " + reglaDeTres(C16, original.width, src.width));
		  println("Real C16: " + mmToPixel(300, reglaDeTres(C16, original.width, src.width)));
		  C16MM = measureCircunference(mmToPixel(300, reglaDeTres(C16, original.width, src.width)),25);
		  println("C16 Circunference: "+ C16MM);
		  
		 //--------------------------------------
		  println(" ");
		  
		  //>-- WRIST CIRCUNFERENCE
		  double C17 = findPlainDistance(blobscv.get(0));
		  C17Pix=C17;
		  println("WRIST CIRCUNFERENCE: C17");
		  println("C17: "+ C17);
		  println("original C17: " + reglaDeTres(C17, original.width, src.width));
		  println("Real C17: " + mmToPixel(300, reglaDeTres(C17, original.width, src.width)));
		  C17MM = measureCircunference(mmToPixel(300, reglaDeTres(C17, original.width, src.width)),40);
		  println("C17 Circunference: "+ C17MM);
		  
		//--------------------------------------
		 
		  
		//--------------------------------------
		  println(" ");
		  //>-------- ENCONTRAR LARGOS PROCESSING'S OPENCV
		//--------------------------------------  
		  
		//--------------------------------------    
		  blobscv.get(1).searchByROI(BNW, blobscv.get(1).findSlope());
		  blobscv.get(6).searchByROI(BNW, blobscv.get(6).findSlope());
		  blobscv.get(7).searchByROI(BNW, 0);
		  blobscv.get(5).searchByROI(BNW, blobscv.get(5).findSlope());
		  blobscv.get(4).searchByROI(BNW, blobscv.get(4).findSlope());
		 
		  //>-- THUMB LENGTH
		  double L1 = blobscv.get(1).longestDist;
		  L1Pix=L1;
		  println("THUMB LENGTH: L1");
		  println("L1: "+ L1);
		  println("original L1: " + reglaDeTres(L1, original.width, src.width));
		  L1MM=mmToPixel(300, reglaDeTres(L1, original.width, src.width));
		  println("Real L1: " + L1MM);
		  
		//--------------------------------------
		  println(" ");
		  
		  //>-- INDEX LENGTH
		  
		  double L2 = blobscv.get(5).longestDist;
		  L2Pix=L2;
		  println("INDEX LENGTH: L2");
		  println("L2: "+ L2);
		  println("original L2: " + reglaDeTres(L2, original.width, src.width));
		  L2MM=mmToPixel(300, reglaDeTres(L2, original.width, src.width));
		  println("Real L2: " + L2MM);
		  
		//--------------------------------------
		  println(" ");
		 /* Marker C12PuntoMedio=puntoMedio(markers.get(0), markers.get(markers.size()-1));
		  double L3=findDistance(extremoCorazon, C12PuntoMedio);
		  L3Pix=L3;
		  println("L3: "+ L3);
		  println("original L3: " + reglaDeTres(L3, original.width, src.width));
		  println("Real L3: " + mmToPixel(300, reglaDeTres(L3, original.width, src.width)));
		  L3MM=mmToPixel(300, reglaDeTres(L3, original.width, src.width));*/
		  
		  //>-- MIDDLE FINGER LENGTH
		  double L3= blobscv.get(7).tip.dist(blobscv.get(7).midPoint);
		  L3Pix = L3;
		  println("MIDDLE FINGER LENGTH: L3");
		  println("L3: "+ L3);
		  println("original L3: " + reglaDeTres(L3, original.width, src.width));
		  L3MM = mmToPixel(300, reglaDeTres(L3, original.width, src.width));
		  println("Real L3: " + L3MM);
		  
		//--------------------------------------
		  println(" ");
		  
		  //>-- RING FINGER LENGTH
		  
		  double L4 = blobscv.get(6).longestDist;
		  L4Pix=L4;
		  println("RING FINGER LENGTH: L4");
		  println("L4: "+ L4);
		  println("original L4: " + reglaDeTres(L4, original.width, src.width));
		  L4MM=mmToPixel(300, reglaDeTres(L4, original.width, src.width));
		  println("Real L4: " + L4MM);
		  
		//--------------------------------------
		  println(" ");
		  
		//>-- LITTLE FINGER LENGTH
		  double L5 = blobscv.get(4).longestDist;
		  L5Pix=L5;
		  println("LITTLE FINGER LENGTH: L5");
		  println("L5: "+ L5);
		  println("original L5: " + reglaDeTres(L5, original.width, src.width));
		  L5MM= mmToPixel(300, reglaDeTres(L5, original.width, src.width));
		  println("Real L5: " + L5MM);
		  
		  
		//--------------------------------------
		  println(" ");
		  /*C17PuntoMedio=puntoMedio(markersMuneca.get(0), markersMuneca.get(markersMuneca.size()-1));
		  double L8=findDistance(extremoCorazon, C17PuntoMedio);
		  L8Pix=L8;
		  println("L8: "+ L8);
		  println("original L8: " + reglaDeTres(L8, original.width, src.width));
		  println("Real L8: " + mmToPixel(300, reglaDeTres(L8, original.width, src.width)));
		  L8MM= mmToPixel(300, reglaDeTres(L8, original.width, src.width));*/
		  
		  //>-- HAND LENGTH
		  double L8= blobscv.get(7).tip.dist(blobscv.get(0).midPoint);
		  L8Pix = L8;
		  println("HAND LENGTH: L8");
		  println("L8: "+ L8);
		  println("original L8: " + reglaDeTres(L8, original.width, src.width));
		  println("Real L8: " + mmToPixel(300, reglaDeTres(L8, original.width, src.width)));
		  
		  
		//--------------------------------------
		  println(" ");
		  /*double L11=findDistance(markersPulgar.get(0), markersMano.get(0));
		  L11Pix=L11;
		  println("L11: "+ L11);
		  println("original L11: " + reglaDeTres(L11, original.width, src.width));
		  println("Real L11: " + mmToPixel(300, reglaDeTres(L11, original.width, src.width)));
		  L11MM=mmToPixel(300, reglaDeTres(L11, original.width, src.width));*/
		  
		//>-- LENGTH FROM THE ROOT OF THE INDEX TO THE ROOT OF THE THUMB
		  if(blobscv.get(0).midPoint.x>blobscv.get(7).midPoint.x) {
			  double L11= blobscv.get(1).leftUpper.dist(blobscv.get(6).rightLower);
			  L11Pix=L11;
			  println("LENGTH FROM THE ROOT OF THE INDEX TO THE ROOT OF THE THUMB: L11");
			  println("L11: "+ L11);
			  println("original L11E: " + reglaDeTres(L11, original.width, src.width));
			  println("Real L11E: " + mmToPixel(300, reglaDeTres(L11, original.width, src.width)));
		  }else {
		  double L11= blobscv.get(1).rightUpper.dist(blobscv.get(6).leftLower);
		  L11Pix=L11;
		  println("LENGTH FROM THE ROOT OF THE INDEX TO THE ROOT OF THE THUMB: L11");
		  println("L11: "+ L11);
		  println("original L11E: " + reglaDeTres(L11, original.width, src.width));
		  println("Real L11E: " + mmToPixel(300, reglaDeTres(L11, original.width, src.width)));
		  }
  
		//--------------------------------------
		  println(" ");
		 /* double L12=findDistance(markersMenique.get(markersMenique.size()-1), markersMuneca.get(markersMuneca.size()-1));
		  L12Pix=L12;
		  println("L12: "+ L12);
		  println("original L12: " + reglaDeTres(L12, original.width, src.width));
		  println("Real L12: " + mmToPixel(300, reglaDeTres(L12, original.width, src.width)));
		  L12MM=mmToPixel(300, reglaDeTres(L12, original.width, src.width));*/
		  
		//>-- LENGTH FROM THE ROOT OF THE LITTLE FINGER TO THE OUTER WRIST
		  if(blobscv.get(0).midPoint.x>blobscv.get(7).midPoint.x) {
			  double L12= blobscv.get(4).leftLower.dist(blobscv.get(0).leftUpper);
			  L12Pix=L12;
			  println("LENGTH FROM THE ROOT OF THE LITTLE FINGER TO THE OUTER WRIST");
			  println("L12: "+ L12);
			  println("original L12: " + reglaDeTres(L12, original.width, src.width));
			  L12MM = mmToPixel(300, reglaDeTres(L12, original.width, src.width));
			  println("Real L12: " + L12MM);
		  }else {
		  double L12= blobscv.get(4).rightLower.dist(blobscv.get(0).rightUpper);
		  L12Pix=L12;
		  println("LENGTH FROM THE ROOT OF THE LITTLE FINGER TO THE OUTER WRIST");
		  println("L12: "+ L12);
		  println("original L12: " + reglaDeTres(L12, original.width, src.width));
		  L12MM = mmToPixel(300, reglaDeTres(L12, original.width, src.width));
		  println("Real L12: " + L12MM);
		  }
		  

		//--------------------------------------
		  println(" ");
		  /*double L13=findDistance(markersPulgar.get(0), markersMuneca.get(0));
		  L13Pix=L13;
		  println("L13: "+ L13);
		  println("original L13: " + reglaDeTres(L13, original.width, src.width));
		  println("Real L13: " + mmToPixel(300, reglaDeTres(L13, original.width, src.width)));
		  L13MM=mmToPixel(300, reglaDeTres(L13, original.width, src.width));*/
		  
		 //>-- LENGTH FROM THE ROOT OF THE THUMB TO THE INNER WRIST
		  if(blobscv.get(0).midPoint.x>blobscv.get(7).midPoint.x) {
			  double L13= blobscv.get(1).rightLower.dist(blobscv.get(0).rightUpper);
			  L13Pix=L13;
			  println("LENGTH FROM THE ROOT OF THE THUMB TO THE INNER WRIST");
			  println("L13: "+ L13);
			  println("original L13: " + reglaDeTres(L13, original.width, src.width));
			  L13MM = mmToPixel(300, reglaDeTres(L13, original.width, src.width));
			  println("Real L13: " + L13MM);  
		  }else {
		  double L13= blobscv.get(1).leftLower.dist(blobscv.get(0).leftUpper);
		  L13Pix=L13;
		  println("LENGTH FROM THE ROOT OF THE THUMB TO THE INNER WRIST");
		  println("L13: "+ L13);
		  println("original L13: " + reglaDeTres(L13, original.width, src.width));
		  L13MM = mmToPixel(300, reglaDeTres(L13, original.width, src.width));
		  println("Real L13: " + L13MM);
		  }
		  
		  /*
		  OscMessage myMessage = new OscMessage("/Measures");
		  String tempMessage;
		  tempMessage = String.valueOf(C15MM)+";"+String.valueOf(L8MM);
		  myMessage.add(tempMessage);
		  println("Mensaje a enviar C12: "+myMessage);
		  oscP5.send(myMessage, myRemoteLocation); 
		  */
		}
	
	
	PImage coordenadasCromaticas(PImage image) {
		colorMode(RGB,255,255,255);
		  PImage imageB = image.copy();
		  image.loadPixels();
		  imageB.loadPixels();

		  for (int i = 0; i < image.pixels.length; i++) {

		    int colorPx = imageB.pixels[i];

		    float r = red(colorPx);
		    float g = green(colorPx);
		    float b = blue(colorPx);

		    float nR = map(r / (r + g + b), 0, 1, 0, 256);
		    float nG = map(g / (r + g + b), 0, 1, 0, 256);
		    float nB = map(b / (r + g + b), 0, 1, 0, 256);

		    imageB.pixels[i] = color(nR, nG, nB);
		  }

		  imageB.updatePixels();
		  image.updatePixels();

		  return imageB;
		}
	
	double findDistance(Marker m1, Marker m2) {
		  double distance = Math.sqrt(Math.pow((m2.getPosX()-m1.getPosX()), 2)+Math.pow((m2.getPosY()-m1.getPosY()), 2));
		  return distance;
		}
	
	double findPlainDistance(BlobCV bcv) {
		double distance = 0;
			PVector pTemp= new PVector(bcv.midPoint.x + bcv.rec.width/2,bcv.midPoint.y);
			distance = bcv.midPoint.dist(pTemp);
			distance=distance*2;
		return distance;
		}

		void drawLine(Marker m1, Marker m2) {
		  line(m1.getPosX(), m1.getPosY(), m2.getPosX(), m2.getPosY());
		}

		Marker puntoMedio(Marker m1, Marker m2) {
		  Marker temp;
		  int midPointX = (m2.getPosX()+m1.getPosX())/2;
		  int midPointY= (m2.getPosY()+m1.getPosY())/2;
		  return  temp= new Marker(this,midPointX, midPointY, 5);
		}

		double reglaDeTres(double fixDist, int originalSize, int fixSize) {
		  double res;
		  res = (fixDist*originalSize)/fixSize;
		  return res;
		}

		double mmToPixel(int DPI, double originalDist) {
		  int pixVal=1;
		  switch(DPI) {
		  case 200:
		    pixVal=8;
		    break;

		  case 300:
		    pixVal=12;
		    break;
		  }
		  return originalDist/pixVal;
		}

		double measureCircunference(double major, double minor) {
		  double semiMajor=major/2;
		  double semiMinor =minor/2;
		  double upperValue= 3*(Math.pow((semiMajor-semiMinor)/(semiMajor+semiMinor), 2));
		  double lowerValue = 10+(Math.sqrt(4-(3*(Math.pow((semiMajor-semiMinor)/(semiMajor+semiMinor), 2)))));
		  double dividedValue = 1+(upperValue/lowerValue);
		  double PIValue = PI*(semiMajor+semiMinor);
		  double circunference = PIValue*dividedValue;
		  return circunference;
		}



		public void keyReleased() {
		  if (key == 'a') {
		    mostrarAgentes=!mostrarAgentes;
		  }
		  if (key== 's') {
		    dibujarLineas=!dibujarLineas;
		  }
		  if (key== 'd') {
		    ocultarDatos=!ocultarDatos;
		  }
		  if (key== 'r') {
		    mostrarRegiones=!mostrarRegiones;
		  }

		  if (key== 'o') {
		    ocultarOriginal=!ocultarOriginal;
		  }

		  if (key== 'm') {
		    setup();
		  }
		  
		  if(key=='j') {
			  println(mouseX + " " + mouseY);
		  }
		}
		
		/*
		void oscEvent(OscMessage theOscMessage) {
		  print the address pattern and the typetag of the received OscMessage
		  print("### received an osc message.");
		  print(" addrpattern: "+theOscMessage.addrPattern());
		  println(" typetag: "+theOscMessage.typetag());
		  println(" x = "+theOscMessage.get(0).stringValue());
		  println(" Y = "+theOscMessage.get(1).stringValue());
		}*/


	public static void main(String[] args) {
		PApplet.main("proyectoGuantes_eclipse_V_01.main");
	}

}
