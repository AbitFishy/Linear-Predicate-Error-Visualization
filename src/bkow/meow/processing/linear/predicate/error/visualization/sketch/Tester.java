package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class Tester {
    ArrayList<Point> m_pts;
    boolean m_mode;  //true:        (default)
                     //        2 pts: lineside test
                     //        3 pts: in triangle
                     //      >=4 pts: in convex hull
                     //
                     //false:
                     //        2 pts: circle originating at pt1 and pt2 is pt on the circle
                     //        3 pts: circle passing through pts
                     //      >=4 pts: explode into fire
    
    int m_pivot;     //for lineside only for now, maybe
                     //1: first pt  (default)
                     //2: 2nd
                     //3: 3rd
    OrientTest test;
    
    public Tester(){
      m_pts = new ArrayList<Point>();
      m_mode = true;
      m_pivot= 1;
      
    }
    
    public void setPoints(ArrayList pts){
      m_pts = pts;
      int size = m_pts.size();
      GlobalVar.proc().println(size + "(s) in set");
      
      if (size <= 1){
        test = new NullTest();
      }
      else{
        if (m_mode){
          switch (size){
            case 2: test = new LinesideTest(m_pts.get(0),m_pts.get(1));
                    GlobalVar.dl().setPoints(m_pts.get(0),m_pts.get(1));
                    break;
            case 3: test = new InTriangleTest(m_pts.get(0),m_pts.get(1),m_pts.get(2));
                    break;
            case 4: test = new InConvexHullTest(GlobalVar.ch().findConvexHull(m_pts));
                    break;
            default: test = new NullTest();
          }
        }else{
          switch (size){
            case 2: test = new InCircleTest(m_pts.get(0),m_pts.get(1));
                    break;
            case 3: test = new InCircleTest(m_pts.get(0),m_pts.get(1),m_pts.get(2));
                    break;
            default: test = new NullTest();
          }
        }
      }
    }
    
    public void setMode(boolean mode, int pivot){
      m_mode = mode;
      m_pivot= pivot;
      if (!m_pts.isEmpty())
      {
        setPoints(m_pts);
      }
    }
    
    public int testPoint(Point p){
      //println("Testing point: "+ p);
      return test.testPoint(p);
    }
}
