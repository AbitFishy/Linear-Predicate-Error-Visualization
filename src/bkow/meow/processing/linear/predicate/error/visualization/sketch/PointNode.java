package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class PointNode {
    private PointNode m_next;
    private Point m_p;
      
    public PointNode(Point p){
      m_p = p;
      m_next=null;
    }
      
    public void setNext(PointNode pn){
      m_next = pn;
    }
      
    public Point getPoint(){
      return m_p;
    }
    public PointNode getNext(){
      return m_next;
    }
}
