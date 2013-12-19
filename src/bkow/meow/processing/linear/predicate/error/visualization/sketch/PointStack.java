package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class PointStack {
    PointNode head;
    
    public PointStack(){
      head = null;
    }
    
    public void push(Point p){
      PointNode n = new PointNode(p);
      if (head == null){
        head = n;
        //debug
        GlobalVar.proc().println("First head: "+ head.getPoint().getX() +","+head.getPoint().getY() );
      }else{
	  GlobalVar.proc().println("Pushing: "+n.getPoint().getX() +","+n.getPoint().getY());
        n.setNext(head);
        //head.setNext(null);
        head = n;
        GlobalVar.proc().println("head: "+ head.getPoint().getX() +","+head.getPoint().getY() );
        GlobalVar.proc().println("next: "+ head.getNext().getPoint().getX() +","+head.getNext().getPoint().getY() );
      }
    }
    
    public Point pop(){
      if (head == null){
        //burn
        return null;
      }
      
      PointNode ret = head;
      head = head.getNext();
      GlobalVar.proc().println("popping: "+ ret.getPoint().getX() +","+ret.getPoint().getY() );
      return ret.getPoint(); 
    }
    
    public Point getTop(){
      return head.getPoint();
    }
    public Point get2FromTop(){
      //debug
      if (head == null){
	  GlobalVar.proc().println("Null head");
      }
      if (head.getNext() == null){
	  GlobalVar.proc().println("Null Next");
	  GlobalVar.proc().println(head.getPoint().getX() +","+head.getPoint().getY());
      }
      //end debug
      return head.getNext().getPoint();
    }
    
    public ArrayList<Point> toArrayList(){
      ArrayList<Point> ret = new ArrayList<Point>();
      Point tp;// = head.getPoint();
      PointNode pn = head;
      
      //debug
      int count = 1;
      
      while (pn != null){
        //println("toArrayList loop: "+ count++);
        tp = pn.getPoint();
        //println("adding point: "+tp.getX()+"'"+tp.getY());
        ret.add(tp);
        pn = pn.getNext();
      }
      GlobalVar.proc().println("Converted to ArrayList");
      return ret;
    }
}
