package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import processing.core.PApplet;

public class KeyboardInput {
    private float m_x;
    private float m_y;
    String inputString;
    Point textPos;
    boolean acceptKeys;
    BoxPoint textBoxPos;
    int doubleClickCount;
    
    public KeyboardInput(){
      acceptKeys = false;
      textBoxPos = new BoxPoint(-1,-1);
      inputString = new String();
      textPos = new Point(-1,-1);
      doubleClickCount = 0;
    }
    
    public void inputKeyboard(boolean yes, float x, float y){
      if (yes){
        acceptKeys = true;
        //textInputPos.setX((int)x);
        //textInputPos.setY((int)y);
        GlobalVar.proc().stroke(0,0,0);
        GlobalVar.proc().fill(255,255,255);
        GlobalVar.proc().rect(x,y, 100, 15);
        textPos.setX(x+3);
        textPos.setY(y+12);
        m_x = x;
        m_y = y;
      }
      else{
        acceptKeys = false;
        //textInputPos.setX(-1);
        //textInputPos.setY(-1);
        textPos.setY(-1);
        textPos.setX(-1);
        inputString = new String();
        GlobalVar.test().setPoints(GlobalVar.ps().getPoints());
        GlobalVar.cs().colorize();
      } //when will the box get over written?
    }
    
    public boolean willAcceptKeys(){
      return acceptKeys;
    }

    public void drawKeyboardInput(){
	GlobalVar.proc().stroke(0,0,0);
	GlobalVar.proc().fill(255,255,255);
	GlobalVar.proc().rect(m_x,m_y, 100, 15);
	GlobalVar.proc().fill(GlobalVar.textColor().color());
	GlobalVar.proc().text(inputString,m_x+3,textPos.getY());
    }
    
    public void inputKey(){
      switch (GlobalVar.proc().key){
        case PApplet.ENTER:
        case PApplet.RETURN:
          GlobalVar.ts().sendInput(inputString);
          inputKeyboard(false,0,0);
          inputString = new String();
          break;
        case PApplet.ESC:
        case PApplet.BACKSPACE:
          GlobalVar.ts().cancelInput();
          break;
        default:
          inputString += GlobalVar.proc().key;
          float x = textPos.getX();
          float y = textPos.getY();
          //text(key,x,y);
          x += 7;
          textPos.setX(x);
          break;
      }
    }
}
