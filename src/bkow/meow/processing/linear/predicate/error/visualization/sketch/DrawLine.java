package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import processing.core.PApplet;

public class DrawLine {
    //private PApplet proc;
    
    private Point m_pt1;
    private Point m_pt2;
    private Point m_rpt1;
    private Point m_rpt2;
    private float m_M;
    private float m_B;
    
    private Point m_pt3;
    private Point m_pt4;
    private boolean isVertical;
    
    private boolean reset;
    
    public DrawLine(Point pt1, Point pt2){
      reset = false;
      setPoints(pt1,pt2);
      //proc = processing;
    }
    
    public DrawLine(){
      reset = true;
      //proc = processing;
    }
    
    public void setReset(boolean r){
      reset = r;
    }
    public boolean needsReset(){
      return reset;
    }
    
    public void setPoints(Point pt1, Point pt2){
      
      reset = false;
      BoxPoint tbp1 = GlobalVar.ps().pointToBox(pt1);
      BoxPoint tbp2 = GlobalVar.ps().pointToBox(pt2);
      m_pt1 = new Point(tbp1.getX(),tbp1.getY());
      m_pt2 = new Point(tbp2.getX(),tbp2.getY());
      
      if (pt2.getX() == pt1.getX()){
        isVertical = true;
      }else {
        isVertical = false;
      }
      m_M = (pt2.getY() - pt1.getY())/(pt2.getX()-pt1.getX());
      m_B = m_pt1.getY() - m_M * m_pt1.getX();
      findBorderInter();
    }
    
    private void findBorderInter(){
      float h = GlobalVar.proc().height;
      float w = GlobalVar.proc().width;
      float tx = 0;
      float ty  = m_M*w + m_B;
      
      if (!isVertical){
        Point tp;
        
        if (ty <= h){
          tx = w;
        }    
        else{
          ty = h;
          tx = (h-m_B)/m_M;
        }
        tp = new Point(tx,ty);
        
        if (m_B >= 0)
        {
          ty = m_B;
          tx = 0;
        }
        else{
          ty = 0;
          tx = -m_B/m_M;
        }
        
        if (m_pt1.getX() <= m_pt2.getX() || m_pt1.getY() <= m_pt2.getY() )
        {
          m_pt4 = tp;
          m_pt3 = new Point(tx,ty);
        }else
        {
          m_pt3 = tp;
          m_pt4 = new Point(tx,ty);
        }
      }else
      {
        if (m_pt1.getY() <= m_pt2.getY())
        {
          m_pt3 = new Point(m_pt1.getX(),0);
          m_pt4 = new Point(m_pt2.getX(),h);
        }else{
          m_pt4 = new Point(m_pt1.getX(),0);
          m_pt3 = new Point(m_pt2.getX(),h);
        }
      }
    }
    
    public void draw(){

      BoxPoint tp = new BoxPoint((int)m_pt1.getX(),(int)m_pt1.getY());    
      BoxPoint p1 = GlobalVar.gs().boxToBoxCenter(tp);
      tp = new BoxPoint((int)m_pt2.getX(),(int)m_pt2.getY());
      BoxPoint p2 = GlobalVar.gs().boxToBoxCenter(tp);
      new BoxPoint((int)m_pt3.getX(),(int)m_pt3.getY());
      BoxPoint p3 = GlobalVar.gs().boxToBoxCenter(tp);
      new BoxPoint((int)m_pt4.getX(),(int)m_pt4.getY());
      BoxPoint p4 = GlobalVar.gs().boxToBoxCenter(tp);
      
      GlobalVar.proc().line(p1.getX(),p1.getY(),p2.getX(),p2.getY());
      GlobalVar.proc().line(p1.getX(),p1.getY(),p3.getX(),p3.getY());
      GlobalVar.proc().line(p2.getX(),p2.getY(),p4.getX(),p4.getY());
    }

}
