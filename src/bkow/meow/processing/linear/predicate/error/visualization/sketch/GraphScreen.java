package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class GraphScreen {//also flips the y-axis
    int m_size; //of box in pixels high and wide
    int m_height; //number of boxes high the screen is
    int m_width; // number of boxes wide the screen is
    
    public GraphScreen(int size, int h, int w){
      m_size = size;
      m_height = h;
      m_width = w;
    }
      
    public int screenSizeH(){
      return m_height*m_size;
    }
      
    public int screenSizeW(){
      return m_width*m_size;
    }
    
    public int getHeight(){
      return m_height;
    }
    public int getWidth(){
      return m_width;
    }
    public int getBoxSize(){
      return m_size;
    }
    
    public BoxPoint pixelToBox(Point pixel)
    {
      int s = m_size;
      
      int x = GlobalVar.proc().floor((float) (pixel.getX()/s));
      int y = m_height - GlobalVar.proc().floor((float) (pixel.getY()/s)) -1;
      
      return new BoxPoint(x,y);
    }
    
    public BoxPoint pixelToBox(BoxPoint pixel)
    {
      int s = m_size;
      
      long x = pixel.getX()/s;
      long y = m_height - pixel.getY()/s -1;
      
      return new BoxPoint(x,y);
    }
    
    public BoxPoint boxToBoxCenter(BoxPoint box){
      int s = m_size;
      int h = m_height;
      return new BoxPoint((box.getX() * s)+(getBoxSize()/2),(box.getY() * s)+(getBoxSize()/2));
    }
    
    public BoxPoint[] boxToPixels(BoxPoint box){
      long s = m_size;
      long h = m_height;
      
      BoxPoint[] range = new BoxPoint[2];
   
      range[0] = new BoxPoint( (box.getX() * s), ( (h-box.getY()-1) * s));
      range[1] = new BoxPoint( ((box.getX() * s) + s-1), ( ((h-box.getY()) * s) -1 ));
      
      //debug
      //if (count != 0){
     //   println("range[0](x,y) = ("+ range[0].getX() +","+range[0].getY()+")");
     //   println("range[1](x,y) = ("+ range[1].getX() +","+range[1].getY()+")\n");
      //  count--;
     // }
      //
      return range;
    }
    
    //public void drawGrid(){
    //}
    public boolean isPointInBox(int pointX, int pointY, int rectX, int rectY, int w, int h){
	  if (pointX >= rectX && pointX <= rectX+w && pointY >= rectY && pointY <= pointY+h){
	    return true;
	  }
	  else{
	    return false;
	  }
	}

}
