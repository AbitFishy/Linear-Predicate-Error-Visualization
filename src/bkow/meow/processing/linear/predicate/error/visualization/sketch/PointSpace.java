package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class PointSpace {
    ArrayList<Point> m_pts;
    
    float m_transX; //translation and scaling from the screen space to point space
    float m_transY;
    float m_scale;
    float m_distBTWBoxes;
    float m_scaleFactor;
    
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
      float sx = p.getX();
      float sy = p.getY();
      
      //px += m_transX;
      //py += m_transY;
      float wx = sx/m_scale + m_transX;
      float wy = sy/m_scale + m_transY;
      
      p.setX(wx);
      p.setY(wy);
      
      m_pts.add(p);
    }
    
    public void addPoint(BoxPoint p){
      float sx = p.getX();
      float sy = p.getY();
      float wx = sx/m_scale + m_transX;
      float wy = sy/m_scale + m_transY;

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
      
    
    public void screenTrans(float x, float y){
      m_transX += x * (1/m_scale);
      m_transY += y * (1/m_scale); 
    }
    
    public void screenScale( float s){
      
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
        
        float divider = (float) ((s < 1) ? .5 : 2);
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
    
    public void setScaleFactor(float sf){
      m_scaleFactor = sf;
    }
    
    public float getScaleFactor(){
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
    
    public float getDistBTWBoxes(){
      return m_distBTWBoxes;
    }
    
    public void setDistBTWBoxes(float dist){
      //float transX = m_transX;
      //float transY = m_transY;
      
      m_distBTWBoxes = dist;
      m_scale = 1/dist;
      
      //m_transX = transX*m_scale;
      //m_transY = transY*m_scale;
      
      GlobalVar.proc().println("Changed distace to: " + dist);
    }
    
    public void setX(float x){
      m_transX = x;
    }
    public void setY(float y){
      m_transY = y;
    }
    
    public void setXRange(float x1, float x2){
      if (x2 <= x1){
	  GlobalVar.proc().println("Negative x range: " + x1 + " : " +x2);
	  return;
      }
      else{
        //float transY = m_transY;
        float len = x2-x1;
        float w = GlobalVar.gs().getWidth();
        float dist = len/w;
        m_distBTWBoxes = dist;
        m_scale = 1/dist;
        m_transX = x1;
        //m_transY = transY*m_scale;
      }
        
    }
    public void setYRange(float y1,float y2){
      if (y2 <= y1){
	  GlobalVar.proc().println("Negative y range: " + y1 + " : " +y2);
        return;
      }
      else{
        //float transX = m_transX;
        float len = y2-y1;
        float w = GlobalVar.gs().getHeight();
        float dist = len/w;
        m_distBTWBoxes = dist;
        m_scale = 1/dist;
        m_transY = y1;
       // m_transX = transX*m_scale;
      }
    }
    
    public BoxPoint pointToBox(Point pt){ //world to screen conversion
      return (pt == null) ? null : new BoxPoint( (int)(m_scale*(pt.getX()-m_transX)), (int)(m_scale*(pt.getY()-m_transY)));
      //
      //testing
      //BoxPoint sp = new BoxPoint( (int)(m_scale*(pt.getX()-m_transX)), (int)(m_scale*(pt.getY()-m_transY)));

    }
    
    public Point boxToPoint(BoxPoint pt){ //screen to world conversion
      //return new Point(float(pt.getX()), float( pt.getY()));
      return (pt == null) ? null : new Point( (pt.getX()/m_scale) + m_transX, (pt.getY()/m_scale) +m_transY);
    }
    
    public void scalePoints(float s){
      for (int i = 0; i <m_pts.size();i++){
        m_pts.set(i,m_pts.get(i).mul(s));
      }
    }
    public void translatePoints(float x, float y){
      for (int i = 0; i <m_pts.size();i++){
        m_pts.set(i,m_pts.get(i).add(new Point(x,y)));
      }
    }
    
}
