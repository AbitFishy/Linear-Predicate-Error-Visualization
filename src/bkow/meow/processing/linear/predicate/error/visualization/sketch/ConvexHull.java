package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class ConvexHull {
    public ArrayList<Point> findConvexHull(ArrayList<Point> pts){
	    pts = sortByAngle(pts);
	    
	    PointStack stack = new PointStack();
	    stack.push(pts.get(0));
	    stack.push(pts.get(1));
	    for (int i = 2; i <= pts.size()-1; i++){
	      while( Determinate.DET(stack.get2FromTop(),stack.getTop(),pts.get(i)) == 1 ){
	        stack.pop();
	      }
	      stack.push(pts.get(i));
	    }
	    GlobalVar.proc().println("Convex Hull found");
	    return stack.toArrayList();
	  }
	  public ArrayList<Point> sortByAngle(ArrayList<Point> pts){
	    int leftmost = 0;
	    double leastLeft = pts.get(0).getX();
	    double t;
	    int len = pts.size();
	    
	    for (int i = 0; i < len; i++){
	      t = pts.get(i).getX();
	      if (t < leastLeft){
	        leftmost  = i;
	        leastLeft = t;
	      }
	    }
	    Point tp = pts.get(leftmost);
	    pts.set(leftmost, pts.get(0));
	    pts.set(0,tp);
	    
	    int min;
	    
	    for (int j = 1; j < len - 1; j++)
	    {
	      min = j;
	      for (int i = j+1; i < len; i++){
	        if (Determinate.DET(pts.get(0),pts.get(min),pts.get(i)) == 1){
	          min = i;
	        }
	      }
	      if (min != j){
	        tp = pts.get(j);
	        pts.set(j,pts.get(min));
	        pts.set(min,tp);
	      }
	    }
	    return pts;
	  }
	    
}
