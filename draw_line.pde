Point P1;
Point P2;

DrawLine dl;

color above;
color linear;
color below;

ColorScreen cs;

void setup(){
  size(480,480); 
  stroke(0);
  background(255);
  
  P1 = new Point(100,350);
  P2 = new Point(400,300);
  dl = new DrawLine(P1,P2);
  
  above = color(255,0,0);
  linear = color(0,255,0);
  below = color(0,0,0);
  
  cs = new ColorScreen();
  cs.addPoint(P1);
  cs.addPoint(P2);
  
  int t = 0;
  boolean s;
  cs.colorize();
  
   stroke(0);
  dl.draw();
  stroke(0,0,0);
  point(P1.getX(),P1.getY());
  point(P2.getX(),P2.getY());
}

void draw(){

}


class Point{
  private float m_X;
  private float m_Y;
  Point(float x, float y){
    m_X = x;
    m_Y = y;
  }
  Point(){
    m_X = 0;
    m_Y = 0;
  }
  
  float getX()
  {
    return m_X;
  }
  float getY(){
    return m_Y;
  }
  void setX(float x){
    m_X = x;
  }
  
  void setY(float y){
    m_Y = y;
  }
}

class DrawLine{          //todo: find which point the interects are closest too to
                        // avoid overlapping lines
  private Point m_pt1;
  private Point m_pt2;
  private float m_M;
  private float m_B;
  
  private Point m_pt3;
  private Point m_pt4;
  
  public DrawLine(Point pt1, Point pt2){
    m_pt1 = pt1;
    m_pt2 = pt2;
    m_M = (pt2.getY() - pt1.getY())/(pt2.getX()-pt1.getX());
    m_B = m_pt1.getY() - m_M * m_pt1.getX();
    findBorderInter();
  }
  
  private void findBorderInter(){
    float h = float(height);
    float w = float(width);
    float tx = 0;
    float ty  = m_M*w + m_B;
    if (ty <= h){
      tx = w;
    }    
    else{
      ty = h;
      tx = (h-m_B)/m_M;
    }
    m_pt3 = new Point(tx,ty);
    
    if (m_B >= 0)
    {
      ty = m_B;
      tx = 0;
    }
    else{
      ty = 0;
      tx = -m_B/m_M;
    }
    
    m_pt4 = new Point(tx,ty);
  }
  
  public void draw(){
    line(m_pt1.getX(),m_pt1.getY(),m_pt2.getX(),m_pt2.getY());
    line(m_pt1.getX(),m_pt1.getY(),m_pt3.getX(),m_pt3.getY());
    line(m_pt2.getX(),m_pt2.getY(),m_pt4.getX(),m_pt4.getY());
  }
}

class LinesideTest{
  private Point m_pt1;
  private Point m_pt2;
  
  public LinesideTest(Point p1, Point p2){
    m_pt1 = p1;
    m_pt2 = p2;
  }
  
  public int testPoint(Point tp){
    return DET(m_pt1,m_pt2,tp);
  }

  private int DET(Point p1, Point p2, Point p3)
  {
    return sign( (p1.getX()*p2.getY() - p1.getY()*p2.getX()) + (p2.getX()*p3.getY() - p3.getX()*p2.getY()) + (p1.getX()*p3.getY() - p3.getX()*p1.getY()));
  }
}

public int sign(float num){
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

class ColorScreen{
  private ArrayList<Point> m_pts;

  public ColorScreen(){
    m_pts = new ArrayList<Point>();  
  }
  
  public void addPoint(Point p){
    m_pts.add(p);
  }
  
  public boolean colorize(){
    color c;
    Point p;
    if (m_pts.size() == 2)
    {
      LinesideTest ls = new LinesideTest(m_pts.get(0),m_pts.get(1));
      for (int i = 0; i < width; i++)
      {
        for (int j = 0; j < height; j++)
        {
          p = new Point(i,j);
          int r = ls.testPoint(new Point(i,j));
          if (r == 1)
          {
            c = above;
          }
          else if (r == 0)
          {
            c = linear;
          }
          else
          {
            c = below;
          }
          
          stroke(c);
          point(i,j);
        }
      }
      return true;         
      
    }else return false;
  }
}
    

  
 




