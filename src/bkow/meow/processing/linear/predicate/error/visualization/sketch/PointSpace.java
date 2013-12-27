package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PointSpace {
    ArrayList<Point> m_pts;
    
    BigDecimal m_transX; //translation and scaling from the screen space to point space
    BigDecimal m_transY;
    BigDecimal m_scale;
    BigDecimal m_distBTWBoxes;
    BigDecimal m_scaleFactor;
    
    int m_index;
    boolean scaleAndTrans;
    
    public PointSpace(){
      m_pts = new ArrayList<Point>();
      m_transX =  new BigDecimal(0);
      m_transY = new BigDecimal(0);
      m_scale  = new BigDecimal(1);
      m_distBTWBoxes = new BigDecimal(1);
      m_scaleFactor = new BigDecimal( 2);
      
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
      BigDecimal wx = new BigDecimal(sx).divide(m_scale, BigDecimal.ROUND_DOWN).add( m_transX);
      BigDecimal wy = new BigDecimal(sy).divide(m_scale, BigDecimal.ROUND_DOWN).add(m_transY);
      
      p.setX(wx.doubleValue());
      p.setY(wy.doubleValue());
      
      m_pts.add(p);
    }
    
    public void addPoint(BoxPoint p){
      double sx = p.getX();
      double sy = p.getY();
      BigDecimal wx = new BigDecimal(sx).divide(m_scale, BigDecimal.ROUND_DOWN).add( m_transX);
      BigDecimal wy = new BigDecimal(sy).divide(m_scale, BigDecimal.ROUND_DOWN).add(m_transY);

      m_pts.add(new Point(wx.doubleValue(),wy.doubleValue()));
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
      screenTrans(new BigDecimal(x), new BigDecimal(y));
    }
    
    public void screenTrans(BigDecimal x, BigDecimal y){
	 m_transX = m_transX.add(x.divide (BigDecimal.ONE.divide(m_scale, BigDecimal.ROUND_DOWN)));
	 m_transY = m_transY.add(y.divide (BigDecimal.ONE.divide(m_scale, BigDecimal.ROUND_DOWN)));
    }
    
    public void screenScale( BigDecimal s){
      
      Point A = boxToPoint(new BoxPoint(0,0));
      Point B = boxToPoint(new BoxPoint(GlobalVar.gs().getWidth()-1,GlobalVar.gs().getHeight()-1));
      Point mouse =  boxToPoint(GlobalVar.gs().pixelToBox(new Point (GlobalVar.proc().mouseX,GlobalVar.proc().mouseY)));
      
      if (s.compareTo(BigDecimal.ZERO) > 0){
        s = s.multiply(m_scaleFactor);
      }
      else if(s.compareTo(BigDecimal.ZERO) < 0){
        //s = -1/(s*m_scaleFactor);
	  s = BigDecimal.ZERO.subtract(BigDecimal.ONE).divide(s.multiply(m_scaleFactor));
      }
      else{
        s = BigDecimal.ONE;
      }
      
      m_scale = s.multiply(m_scale);
      m_distBTWBoxes = BigDecimal.ONE.divide(m_scale);
      GlobalVar.proc().println("Scaling by: " + s + ", Scale: " + m_scale);
      
      if (scaleAndTrans){  
	  GlobalVar.proc().println("A: " + A + " B: " +B+" mouse: "+mouse);

        Point oldLen = B.sub(A);
        Point len = oldLen.div( (m_scaleFactor));
        GlobalVar.proc().println("oldlen: "+oldLen+" len: "+len);
        
        BigDecimal divider = new BigDecimal ((s.compareTo(BigDecimal.ONE) < 0) ? .5 : 2);
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
	return (pt == null) ? null : new BoxPoint( (int) (m_scale*(pt.getX()-m_transX)), (int)(m_scale*(pt.getY()-m_transY)));
     
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
