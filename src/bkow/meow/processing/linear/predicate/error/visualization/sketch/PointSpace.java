package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class PointSpace {
    ArrayList<Point> m_pts;
    
    double m_transX; //translation and scaling from the screen space to point space
    double m_transY;
    double m_scale;
    double m_distBTWBoxes;
    double m_scaleFactor;
    
    int m_index;
    boolean scaleAndTrans;
    
    public PointSpace(){
      m_pts = new ArrayList<Point>();
      m_transX = 0;
      m_transY = 0;
      m_scale  = 1;
      m_distBTWBoxes = 1;
      m_scaleFactor = 2;
      
      m_index  = 0;
      scaleAndTrans = false;
    }
    
    public void addPoint(Point p){//Screen to World
      double sx = p.getX();
      double sy = p.getY();
      
      if (Double.isInfinite(sx)){
	  GlobalVar.proc().println("Infinite Point added X");
      }
      if (Double.isInfinite(sy)){
	  GlobalVar.proc().println("Infinite Point added Y");
      }
       if (Double.isNaN(sx)){
	  GlobalVar.proc().println("NAN Point added X");
      }
      if (Double.isNaN(sy)){
	  GlobalVar.proc().println("NAN Point added Y");
      }
      
      //px += m_transX;
      //py += m_transY;
      double wx = sx/m_scale + m_transX;
      double wy = sy/m_scale + m_transY;
      
      p.setX(wx);
      p.setY(wy);
      
      m_pts.add(p);
    }
    
    public void addPoint(BoxPoint p){
      double sx = p.getX();
      double sy = p.getY();
      double wx = sx/m_scale + m_transX;
      double wy = sy/m_scale + m_transY;

      m_pts.add(new Point(wx,wy));
    }
    
    public boolean removePoint(Point p){ //world coordinates
      
      return m_pts.remove(p);
    }
    public boolean removePoint(BoxPoint rp){//screen to world conversion
      //return removePoint(boxToPoint(p));
      BoxPoint sp;
      GlobalVar.proc().println("Trying to remove " +rp);
      
      for (int i = 0; i < m_pts.size();i++){
        sp = GlobalVar.ps().pointToBox(m_pts.get(i));
        GlobalVar.proc().println("Testing pt " + sp + " / " +m_pts.get(i));
        if (sp.equals(rp)){
          m_pts.remove(i);
          return true;
        }
      }
      return false;
    }
      
    
    public void screenTrans(double x, double y){
      m_transX += x * (1/m_scale);
      m_transY += y * (1/m_scale); 
    }
    
    public void screenScale( double s){
      
      Point A = boxToPoint(new BoxPoint(0,0));
      Point B = boxToPoint(new BoxPoint(GlobalVar.gs().getWidth()-1,GlobalVar.gs().getHeight()-1));
      Point mouse =  boxToPoint(GlobalVar.gs().pixelToBox(new Point (GlobalVar.proc().mouseX,GlobalVar.proc().mouseY)));
      
      if (s >0){
        s *= m_scaleFactor;
      }
      else if(s<0){
        s = -1/(s*m_scaleFactor);
      }
      else{
        s = 1;
      }
      
      m_scale *= s;
      m_distBTWBoxes = 1/m_scale;
      GlobalVar.proc().println("Scaling by: " + s + ", Scale: " + m_scale);
      
      if (scaleAndTrans){  
	  GlobalVar.proc().println("A: " + A + " B: " +B+" mouse: "+mouse);

        Point oldLen = B.sub(A);
        Point len = oldLen.div( (m_scaleFactor));
        GlobalVar.proc().println("oldlen: "+oldLen+" len: "+len);
        
        double divider = (double) ((s < 1) ? .5 : 2);
        Point botLeft= mouse.sub(len.div(divider));
        GlobalVar.proc().println("len.div: " + len.div(divider) + " botLeft: " + botLeft);
        
        m_transX = botLeft.getX();
        m_transY = botLeft.getY();
        
        GlobalVar.proc().println("Translate zoom: "+m_transX+" "+m_transY);
      }
    }
    
    public void scaleAndTrans(boolean yes){
      if (yes){
        scaleAndTrans = true;
      }
      else{
        scaleAndTrans = false;
      }
    }
    
    public void setScaleFactor(double sf){
      m_scaleFactor = sf;
    }
    
    public double getScaleFactor(){
      return m_scaleFactor;
    }
    
    public Point getNextPoint(){ //returns in pointspace (world) coordinates
      return (m_index < m_pts.size()) ? m_pts.get(m_index++) : null;
    }
    
    public Point getNextBox(){ //returns in screen coordinates
      Point wp = (m_index < m_pts.size()) ? m_pts.get(m_index++) : null;
      Point sp = wp == null ? null : new Point(m_scale * (wp.getX()-m_transX), m_scale * (wp.getY()-m_transY) );
      return sp;
    }
    
    public void resetQueue(){
      m_index = 0;
    }
    
    public ArrayList<Point> getPoints(){
      return m_pts;
    }
    
    public int getNumPts(){
      return m_pts.size();
    }
    
    public double getDistBTWBoxes(){
      return m_distBTWBoxes;
    }
    
    public void setDistBTWBoxes(double dist){
      //float transX = m_transX;
      //float transY = m_transY;
      
      m_distBTWBoxes = dist;
      m_scale = 1/dist;
      
      //m_transX = transX*m_scale;
      //m_transY = transY*m_scale;
      
      GlobalVar.proc().println("Changed distace to: " + dist);
    }
    
    public void setX(double x){
      m_transX = x;
    }
    public void setY(double y){
      m_transY = y;
    }
    
    public void setXRange(double x1, double x2){
      if (x2 <= x1){
	  GlobalVar.proc().println("Negative x range: " + x1 + " : " +x2);
	  return;
      }
      else{
        //float transY = m_transY;
        double len = x2-x1;
        double w = GlobalVar.gs().getWidth();
        double dist = len/w;
        m_distBTWBoxes = dist;
        m_scale = 1/dist;
        m_transX = x1;
        //m_transY = transY*m_scale;
      }
        
    }
    public void setYRange(double y1,double y2){
      if (y2 <= y1){
	  GlobalVar.proc().println("Negative y range: " + y1 + " : " +y2);
        return;
      }
      else{
        //double transX = m_transX;
        double len = y2-y1;
        double w = GlobalVar.gs().getHeight();
        double dist = len/w;
        m_distBTWBoxes = dist;
        m_scale = 1/dist;
        m_transY = y1;
       // m_transX = transX*m_scale;
      }
    }
    
    public BoxPoint pointToBox(Point pt){ //world to screen conversion
	//GlobalVar.proc().println("Point to box " + pt + " scale " + m_scale + " transX " + m_transX + " transY " + m_scale);
	return (pt == null) ? null : new BoxPoint( (long)(m_scale*(pt.getX()-m_transX)), (long)(m_scale*(pt.getY()-m_transY)));    }
    }
    
    public Point boxToPoint(BoxPoint pt){ //screen to world conversion
      //return new Point(double(pt.getX()), double( pt.getY()));
      return (pt == null) ? null : new Point( (pt.getX()/m_scale) + m_transX, (pt.getY()/m_scale) +m_transY);
    }
    
    public void scalePoints(double s){
      for (int i = 0; i <m_pts.size();i++){
        m_pts.set(i,m_pts.get(i).mul(s));
      }
    }
    public void translatePoints(double x, double y){
      for (int i = 0; i <m_pts.size();i++){
        m_pts.set(i,m_pts.get(i).add(new Point(x,y)));
      }
    }
    
}
