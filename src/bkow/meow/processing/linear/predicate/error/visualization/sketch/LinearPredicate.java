package bkow.meow.processing.linear.predicate.error.visualization.sketch;
import processing.core.*;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class LinearPredicate extends PApplet{
    
    /**
     * 
     */
    private static final long serialVersionUID = 388058512273632187L;
    
   // PApplet proc;

    public static void main(String args[]) {
	PApplet.main(new String[] { "--present", "bkow.meow.processing.linear.predicate.error.visualization.sketch.LinearPredicate" });
    }
    
    private Point downMousePos;
    private Point dragMousePos;
    
    public void setup() {
	//proc = this;
	
	GlobalVar.setVars(this, new PointSpace(), new ColorScreen(), new GraphScreen(3,256,400), new ToolScreen(20), new KeyboardInput(), new Tester(), new ConvexHull(), new DrawLine());

	  
	size(GlobalVar.gs().screenSizeW(),GlobalVar.gs().screenSizeH()+GlobalVar.ts().getHeight()); 
	stroke(0);
	background(128);
	  
	Color above   = new Color(Color.color(128,255,255));
	Color linear  = new Color(Color.color(255,0,0));
	Color below   = new Color(Color.color(255,165,0));
	Color pointColor = new Color(Color.color(0,0,255));
	Color lineColor = new Color(Color.color(0,0,0));
	Color textColor = new Color(Color.color(0,0,0));
	Color toolTextBGColor = new Color(Color.color(0,0,255));
	Color toolTextButtonColor = new Color(0x9E4600FF);
	Color toolTextButtonPressedColor = new Color(0xF4460022);
	
	GlobalVar.setColors(above, linear, below, pointColor, lineColor, textColor, toolTextBGColor, toolTextButtonColor, toolTextButtonPressedColor);
	  
	downMousePos = new Point(-1,-1);
	dragMousePos = new Point(-1,-1);
	  
	GlobalVar.ps().scaleAndTrans(true);
	  
	GlobalVar.test().setPoints(GlobalVar.ps().getPoints());
	GlobalVar.cs().colorize();
	  
    }

    public void draw() {
	GlobalVar.ts().drawLabels();
	  GlobalVar.cs().draw();
	  
	  //drawlines
	  if (GlobalVar.ps().getNumPts() == 2){
	    if (!GlobalVar.dl().needsReset()){
	      //dl.draw();
	    }
	  }
	      
	  //drawpoints
	  GlobalVar.ps().resetQueue();
	  Point point = GlobalVar.ps().getNextPoint();
	  while (point != null){
	    //println("Painting point: "+point);
	    GlobalVar.cs().paintBox(GlobalVar.ps().pointToBox(point),GlobalVar.pointColor());
	    point = GlobalVar.ps().getNextPoint();
	  }
	  if (GlobalVar.ki().willAcceptKeys()){
	    GlobalVar.ki().drawKeyboardInput();
	  }
    }
    
    public void mousePressed(){
	  downMousePos.setX(mouseX);
	  downMousePos.setY(mouseY);
	  if (mouseY < GlobalVar.gs().screenSizeH()){
	    dragMousePos.setX(mouseX);
	    dragMousePos.setY(mouseY);
	  }
	  else{
	    if (GlobalVar.ts().isButtonPressed()){
	      //draw();
	    }
	  }
	}

	public void mouseDragged(){
	  if (mouseButton == LEFT){
	    if (mouseY < GlobalVar.gs().screenSizeH()){//moved and in graph area
	        boolean negX;
	        boolean negY;
	        
	        float transX = -( mouseX - dragMousePos.getX())/ GlobalVar.gs().getBoxSize();
	        float transY = ( mouseY - dragMousePos.getY())/ GlobalVar.gs().getBoxSize();
	        //negX = transX < 0 ? true:false;
	        //negY = transY < 0 ? true:false;
	        //transX= -(transX);
	        //transY = (transY);
	        //transX = negX == true ? transX-1 : transX;
	        //transY = negY == true ? transY+1 : transY;
	        
	        dragMousePos.setX(mouseX);
	        dragMousePos.setY(mouseY);
	        GlobalVar.ps().screenTrans(transX,transY);
	     }
	  }
	}
	     

	public void mouseReleased(){
	  if (GlobalVar.ts().isWaitingForInput()){
	    if (GlobalVar.ki().doubleClickCount++ == 3){
	      GlobalVar.ts().cancelInput();
	      GlobalVar.ki().doubleClickCount = 0;
	    }
	  }
	  else if (downMousePos.getX() == mouseX && downMousePos.getY() == mouseY){
	    //only do this when clicked in graph area.
	    if (mouseY < GlobalVar.gs().screenSizeH()){
	      BoxPoint pt = GlobalVar.gs().pixelToBox(new BoxPoint(mouseX,mouseY));
	      if (mouseButton == LEFT){ 
	        println("Clicked point: "+pt);
	        GlobalVar.ps().addPoint(pt);//here it should convert from screen coordinates to space coordinates
	        println("Number of Points: "+GlobalVar.ps().getNumPts());
	      }
	      else if (mouseButton == RIGHT){
	        boolean rm = GlobalVar.ps().removePoint(pt);
	        if (rm){
	          println("Removed point: "+GlobalVar.ps().boxToPoint(pt));
	        }
	        else{
	          println("Nothing at: "+GlobalVar.ps().boxToPoint(pt));
	        }
	      }
	    
	    
	      GlobalVar.test().setPoints(GlobalVar.ps().getPoints());
	      println("Points set");
	    
	      //color results
	      GlobalVar.cs().colorize();
	      
	      
	      
	    }else{
	      //tool area
	      GlobalVar.ts().release(true);
	      
	    }
	  }else{ //mouse moved
	    GlobalVar.ts().release(false);
	    GlobalVar.test().setPoints(GlobalVar.ps().getPoints());
	    GlobalVar.cs().colorize();
	       
	  }
	  
	  downMousePos.setX(-1);
	  downMousePos.setY(-1);
	  dragMousePos.setX(-1);
	  dragMousePos.setY(-1);
	}

	public void mouseWheel(MouseEvent event){
	  float amount = event.getCount();
	  GlobalVar.ps().screenScale(-amount);
	  
	  GlobalVar.test().setPoints(GlobalVar.ps().getPoints());
	  println("Points set after scroll");
	  GlobalVar.cs().colorize();
	  println("Colorized after scroll");
	  
	}

	public void keyTyped(){
	  if (GlobalVar.ki().willAcceptKeys()){
	    GlobalVar.ki().inputKey();
	  }  
	}
}
