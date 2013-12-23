package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class inSmallestCircleTest implements OrientTest {

    ArrayList<Point> m_pts;
    
    double m_center;
    double m_radius;
    
    public inSmallestCircleTest(ArrayList<Point> pts){
      m_pts = minimumEnclosingBall(pts);
      //center
    }
    
    public ArrayList<Point> minimumEnclosingBall(ArrayList<Point> P){
      ArrayList<Point> Q = new ArrayList<Point>();
      
      Q = MEB(P,Q);
      
      return Q;
    }
    
    private ArrayList<Point> MEB(ArrayList<Point> P, ArrayList<Point> Q){
      if (P.isEmpty()){
        return Q;
      }
      
      ArrayList<Point> B = (ArrayList<Point>)Q.clone();
      int i = P.size();
      P = scramblePoints(P);
      for (int k = 1; k < i; k++){
        if (B.contains( P.get(k) )){
        }
        else{
          ArrayList<Point> subP = (ArrayList<Point>)P.clone();
          //subP.removeRange(k,i);
          Q.add(P.get(k));
          B = MEB(subP,Q);
        }
      }
      return Q;
    }
    
    private ArrayList<Point> scramblePoints(ArrayList<Point> p){
      return p;
    }
    
    public int testPoint(Point tp){
      return -1;
    }

}
