package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class Determinate {
    public static int DET(Point p1, Point p2, Point p3)  {
	  //return sign( (p1.getX()*(p2.getY()-p3.getY())) + (p2.getX()*(p3.getY()-p1.getY())) + (p3.getX()*(p1.getY()-p2.getY())) );
	  return sign ( (p2.getX()-p1.getX()) * (p3.getY()-p1.getY()) - (p2.getY()-p1.getY()) * (p3.getX()-p1.getX()));
    }
	  
    public static int DET4(Point p1, Point p2, Point p3, Point tp){
	  float p1x = p1.getX();
	  float p1y = p1.getY();
	  float p2x = p2.getX();
	  float p2y = p2.getY();
	  float p3x = p3.getX();
	  float p3y = p3.getY();
	  float tpx = tp.getX();
	  float tpy = tp.getY();
	  
	  float p1s = p1x*p1x + p1y*p1y;
	  float p2s = p2x*p2x + p2y*p2y;
	  float p3s = p3x*p3x + p3y*p3y;
	  float tps = tpx*tpx + tpy*tpy;
	  return sign( p1x*p2y*p3s  +  p2x*p3y*tps  +  p3x*tpy*p1s  +  tpx*p1y*p2s
	             -(p1s*p2y*p3x) - (p2s*p3y*tpx) - (p3s*tpy*p1x) - (tps*p1y*p2x) );
    }
    
    public static int sign(float num){
	  if (num > 0)
	  {
	    return 1;
	  }
	  else if (num < 0)
	  {
	    return -1;
	  }
	  else return 0;
	}
}
