package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import processing.core.PApplet;

public class ToolScreen {
    int m_height;
    int labelOffset;
    int labelHeight;
    int labelStart;
    int mouseLabel;
    int distLabel;
    int scaleLabel;
    int transLabel; 
    int xRangeLabel;
    int yRangeLabel;
    
    String mouseText;
    String distText;
    String scaleText;
    String transText;
    String xRangeText;
    String yRangeText;
    
    int buttonStartY;
    int buttonEndY;
    
    boolean isScaleDown;
    boolean isTransDown;
    boolean isDist2Down;
    boolean isXRangDown;
    boolean isYRangDown;
    
    Point[] scaleButtonArea;
    Point[] transButtonArea;
    Point[] dist2ButtonArea;
    Point[] xRangButtonArea;
    Point[] yRangButtonArea;
    int inputOffset;
    boolean waitForScaleInput;
    boolean waitForTransInput;
    boolean waitForDist2Input;
    boolean waitForXRangInput;
    boolean waitForYRangInput;
    
    public ToolScreen(int h, GraphScreen gs){
      m_height = h;
      labelOffset = 15;
      labelHeight = gs.screenSizeH() +labelOffset;
      labelStart = 5;
      mouseLabel = labelStart;
      distLabel  = mouseLabel   + 200;
      scaleLabel = distLabel    + 230;
      transLabel = scaleLabel   + 100;
      xRangeLabel= transLabel   + 125;
      yRangeLabel= xRangeLabel  + 220;
      
      mouseText = "Mouse at: ";
      distText  = "Distance btw squares: ";
      scaleText = "Scale Points";
      transText = "Translate Points";
      xRangeText= "X-Range: ";
      yRangeText= "Y-Range: ";
      
      buttonStartY = labelHeight-labelOffset+3;
      buttonEndY   = m_height-7;
      
      scaleButtonArea = new Point[2];
      transButtonArea = new Point[2];
      dist2ButtonArea = new Point[2];
      xRangButtonArea = new Point[2];
      yRangButtonArea = new Point[2];
      
      scaleButtonArea[0] = new Point(scaleLabel-10,buttonStartY);
      scaleButtonArea[1] = new Point(90,buttonEndY);
      transButtonArea[0] = new Point(transLabel-10,buttonStartY);
      transButtonArea[1] = new Point(transLabel-scaleLabel+10,buttonEndY);
      dist2ButtonArea[0] = new Point(distLabel-10,buttonStartY);
      dist2ButtonArea[1] = new Point(220,buttonEndY);
      xRangButtonArea[0] = new Point(xRangeLabel-10,buttonStartY);
      xRangButtonArea[1] = new Point(63,buttonEndY);
      yRangButtonArea[0] = new Point(yRangeLabel + 50,buttonStartY);
      yRangButtonArea[1] = new Point(100,buttonEndY);
      
      isScaleDown = false;
      isTransDown = false;
      
      inputOffset = 30;
      waitForScaleInput = false;
      waitForTransInput = false;
      
    }
    public ToolScreen(){
      m_height = 20;
    }
    public void setHeight(int h){
      m_height = h;
    }
    public int getHeight(){
      return m_height;
    }
    
    public void drawLabels(){
        //Tool text
      int xDraw = GlobalVar.gs().screenSizeW();
      int yDraw = GlobalVar.gs().screenSizeH();
      GlobalVar.proc().fill(GlobalVar.toolTextBGColor().color());
      GlobalVar.proc().stroke(0,0,0);
      GlobalVar.proc().rect(0,yDraw,xDraw-1,m_height-1);
      
      GlobalVar.proc().fill(GlobalVar.toolTextButtonColor().color());
      rect(scaleButtonArea[0].getX(),scaleButtonArea[0].getY(),scaleButtonArea[1].getX(),scaleButtonArea[1].getY());
      rect(transButtonArea[0].getX(),transButtonArea[0].getY(),transButtonArea[1].getX(),transButtonArea[1].getY());
      rect(dist2ButtonArea[0].getX(),dist2ButtonArea[0].getY(),dist2ButtonArea[1].getX(),dist2ButtonArea[1].getY());
      rect(xRangButtonArea[0].getX(),xRangButtonArea[0].getY(),xRangButtonArea[1].getX(),xRangButtonArea[1].getY());
      rect(yRangButtonArea[0].getX(),yRangButtonArea[0].getY(),yRangButtonArea[1].getX(),yRangButtonArea[1].getY());
      if (isScaleDown){
	  GlobalVar.proc().fill(GlobalVar.toolTextButtonPressedColor().color());
        rect(scaleButtonArea[0].getX(),scaleButtonArea[0].getY(),scaleButtonArea[1].getX(),scaleButtonArea[1].getY());
      }
      else if (isTransDown){
	  GlobalVar.proc().fill(GlobalVar.toolTextButtonPressedColor().color());
        rect(transButtonArea[0].getX(),transButtonArea[0].getY(),transButtonArea[1].getX(),transButtonArea[1].getY());
      }
      else if (isDist2Down){
	  GlobalVar.proc().fill(GlobalVar.toolTextButtonPressedColor().color());
        rect(dist2ButtonArea[0].getX(),dist2ButtonArea[0].getY(),dist2ButtonArea[1].getX(),dist2ButtonArea[1].getY());
      }
      else if (isXRangDown){
	  GlobalVar.proc().fill(GlobalVar.toolTextButtonPressedColor().color());
        rect(xRangButtonArea[0].getX(),xRangButtonArea[0].getY(),xRangButtonArea[1].getX(),xRangButtonArea[1].getY());
      }
      else if (isYRangDown){
	  GlobalVar.proc().fill(GlobalVar.toolTextButtonPressedColor().color());
        rect(yRangButtonArea[0].getX(),yRangButtonArea[0].getY(),yRangButtonArea[1].getX(),yRangButtonArea[1].getY());
      }
      
      Point mouse = GlobalVar.ps().boxToPoint(GlobalVar.gs().pixelToBox(new BoxPoint(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY)));
      Point bLeft = GlobalVar.ps().boxToPoint(new BoxPoint(0,0));
      Point tRight= GlobalVar.ps().boxToPoint(new BoxPoint(GlobalVar.gs().getWidth()-1,GlobalVar.gs().getHeight()-1));
      GlobalVar.proc().fill(GlobalVar.textColor().color());
      text(mouseText + mouse,mouseLabel,labelHeight);//change to point coordinates
      text(distText  + GlobalVar.ps().getDistBTWBoxes(), distLabel, labelHeight);
      text(scaleText, scaleLabel,labelHeight);
      text(transText, transLabel,labelHeight);
      text(xRangeText+ bLeft.getX()+" : "+tRight.getX()+"  "+yRangeText+ bLeft.getY()+" : "+tRight.getY(), xRangeLabel,labelHeight);
     // text(yRangeText+ tRight.getX()+"-"+tRight.getY(), yRangeLabel,labelHeight);
      
    }
    
    public boolean isButtonPressed(){
      if (GlobalVar.gs().isPointInBox(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY,(int)scaleButtonArea[0].getX(),(int)scaleButtonArea[0].getY(),(int)scaleButtonArea[1].getX(),(int)scaleButtonArea[1].getY())){
        isScaleDown = true;
        println("Scale Button Down");
        return true;
      }
      else if (GlobalVar.gs().isPointInBox(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY,(int)transButtonArea[0].getX(),(int)transButtonArea[0].getY(),(int)transButtonArea[1].getX(),(int)transButtonArea[1].getY())){
        isTransDown = true;
        println("Translate Button Down");
        return true;
      }
      else if (GlobalVar.gs().isPointInBox(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY,(int)dist2ButtonArea[0].getX(),(int)dist2ButtonArea[0].getY(),(int)dist2ButtonArea[1].getX(),(int)dist2ButtonArea[1].getY())){
        isDist2Down = true;
        println("Distance Button Down");
        return true;
      }
      else if (GlobalVar.gs().isPointInBox(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY,(int)xRangButtonArea[0].getX(),(int)xRangButtonArea[0].getY(),(int)xRangButtonArea[1].getX(),(int)xRangButtonArea[1].getX())){
        isXRangDown = true;
        println("X-Range Button Down");
        return true;
      }
      else if (GlobalVar.gs().isPointInBox(GlobalVar.proc().mouseX,GlobalVar.proc().mouseY,(int)yRangButtonArea[0].getX(),(int)yRangButtonArea[0].getY(),(int)yRangButtonArea[1].getX(),(int)yRangButtonArea[1].getX())){
        isYRangDown = true;
        println("Y-Rang Button Down");
        return true;
      }
      else{
        return false;
      }
    }
    public void release(boolean clicked){
      if (!clicked){
        isScaleDown = false;
        isTransDown = false;
        isDist2Down = false;
        isXRangDown = false;
        isYRangDown = false;
      }
      else{
        if (isScaleDown){
          scaleButtonInput();
        }
        else if (isTransDown){
          transButtonInput();
        }
        else if (isDist2Down){
          dist2ButtonInput();
        }
        else if (isXRangDown){
          xRangButtonInput();
        }
        else if (isYRangDown){
          yRangButtonInput();
        }
        else{
          println("Release function failure");
        }
        
        
        isScaleDown = false;
        isTransDown = false;
        isDist2Down = false;
        isXRangDown = false;
        isYRangDown = false; 
      }
    }
    
    private void scaleButtonInput(){
      //String input = getTextInput(scaleButtonArea[0].getX(),scaleButtonArea[0].getY()-inputOffset);
      GlobalVar.ki().inputKeyboard(true,scaleButtonArea[0].getX(),scaleButtonArea[0].getY()-inputOffset);
      waitForScaleInput = true;
    }
    private void transButtonInput(){
      //String input = getTextInput(transButtonArea[0].getX(),transButtonArea[0[].getY()-inputOffset);
      GlobalVar.ki().inputKeyboard(true,transButtonArea[0].getX(),transButtonArea[0].getY()-inputOffset);
      waitForTransInput = true;
    }
    private void dist2ButtonInput(){
      GlobalVar.ki().inputKeyboard(true,dist2ButtonArea[0].getX(),dist2ButtonArea[0].getY()-inputOffset);
      waitForDist2Input = true;
    }
    private void xRangButtonInput(){
      GlobalVar.ki().inputKeyboard(true,xRangButtonArea[0].getX(),xRangButtonArea[0].getY()-inputOffset);
      waitForXRangInput = true;
    }
    private void yRangButtonInput(){
      GlobalVar.ki().inputKeyboard(true,yRangButtonArea[0].getX(),yRangButtonArea[0].getY()-inputOffset);
      waitForYRangInput = true;
    }
    
    
    public boolean isWaitingForInput(){
      if (waitForTransInput || waitForScaleInput || waitForDist2Input || waitForXRangInput || waitForYRangInput){
        return true;
      }
      else{
        return false;
      }
    }
    
    public void cancelInput(){
      waitForScaleInput = false;
      waitForTransInput = false;
      waitForDist2Input = false;
      waitForXRangInput = false;
      waitForYRangInput = false;
      GlobalVar.ki().inputKeyboard(false,0,0);
    }
    
    public void sendInput(String input){
      println("Input: " +input);
      try{    
        if (waitForScaleInput){
          double scaling = Double.parseDouble(input);
          println("Scaling by: "+scaling);
          GlobalVar.ps().scalePoints(scaling);
          println("Input Success");
        }
        else if (waitForTransInput){
          double xTran;
          double yTran;
          int commaLoc = input.indexOf(',');
          if (commaLoc == -1){
            xTran = Double.parseDouble(input);
            println("Translate x: "+xTran);
            GlobalVar.ps().translatePoints(xTran,0);
            println("Input Success");
          }
          else{
            xTran = Double.parseDouble(input.substring(0,commaLoc));
            yTran = Double.parseDouble(input.substring(commaLoc+1,input.length()));
            println("Translate x: "+xTran+ "  y: "+yTran);
            GlobalVar.ps().translatePoints(xTran,yTran);
            println("Input Success");
          }
            
        }
        else if (waitForDist2Input){
          double distance = Double.parseDouble(input);
          println("Distance btw pts now: "+ distance);
          GlobalVar.ps().setDistBTWBoxes(distance);
          println("Input Success");
        }
        else if (waitForXRangInput){
            double x1;
            double x2;
            int commaLoc = input.indexOf(',');
            if (commaLoc == -1){
              x1 = Double.parseDouble(input);
              println("X: "+x1);
              GlobalVar.ps().setX(x1);
              println("Input Success");
            }
            else{
        	x1 = Double.parseDouble(input.substring(0, commaLoc));
        	x2 = Double.parseDouble(input.substring(commaLoc+1,input.length()));
        	println("X range: " + x1 + " - " + x2);
        	GlobalVar.ps().setXRange(x1,x2);
        	println("Input Success");
        	
            }
        }
        else if (waitForYRangInput){
          double y1;
          double y2;
          int commaLoc = input.indexOf(',');
          if (commaLoc == -1){
              y1 = Double.parseDouble(input);
              println("Y: "+y1);
              GlobalVar.ps().setY(y1);
              println("Input Success");
          }
          else{
                y1 = Double.parseDouble(input.substring(0, commaLoc));
          	y2 = Double.parseDouble(input.substring(commaLoc+1,input.length()));
          	println("Y range: " + y1 + " - " + y2);
          	GlobalVar.ps().setYRange(y1,y2);
          	println("Input Success");
          }
        }
      }
      catch (java.lang.NumberFormatException e){
        println("Bad input");
      }
      finally {
        
      }
      
      cancelInput();
    }
    
    private void rect(double x,double y,double w,double h){
	GlobalVar.proc().rect((float)x,(float)y,(float)w,(float)h);
    }
    private void println(String s){
	GlobalVar.proc().println(s);
    }
    private void text(String s, double x, double y){
	GlobalVar.proc().text(s,(float)x,(float)y);
    }
    
}
