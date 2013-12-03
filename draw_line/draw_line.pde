
Point P1;
Point P2;
Point P3;
Point P4;
Point P5;

DrawLine dl;

color above;
color linear;
color below;

ColorScreen cs;
GraphScreen gs;

PointSpace ps;
Tester test;

ConvexHull ch;
//debuging
int count = 5;

void setup(){
  gs = new GraphScreen(3,255,255);
  
  size(gs.screenSizeH(),gs.screenSizeW()); 
  stroke(0);
  background(128);
  
  ps   = new PointSpace();
  cs   = new ColorScreen(ps);
  test = new Tester();
  ch   = new ConvexHull();
  
  above   = color(0,0,255);
  linear  = color(255,255,0);
  below   = color(255,0,0);
  //end setup
  
  //testing
  P1 = new Point(100,50);
  P2 = new Point(100,100);
  P3 = new Point(75,200);
  P4 = new Point(45,235);
  P5 = new Point(200,210);
  dl = new DrawLine(P1,P2);
  
  ps.addPoint(P1);
  ps.addPoint(P2);
  ps.addPoint(P3);
  ps.addPoint(P4);
  ps.addPoint(P5);
  //println(ps.get(0)+ps.get(1)+ps.get(2)+ps.get(3));
  test.setMode(true,1);
  test.setPoints(ps.getPoints());
  
  int t = 0;
  boolean s;
  cs.colorize();
  
   stroke(0);
  dl.draw();
  stroke(255,255,255);
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
  
  @Override
  public String toString(){
    return new String("("+m_X+","+m_Y+")");
  }
}

class DrawLine{
  private Point m_pt1;
  private Point m_pt2;
  private float m_M;
  private float m_B;
  
  private Point m_pt3;
  private Point m_pt4;
  private boolean isVertical;
  
  public DrawLine(Point pt1, Point pt2){
    m_pt1 = pt1;
    m_pt2 = pt2;
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
    float h = float(height);
    float w = float(width);
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
    line(m_pt1.getX(),m_pt1.getY(),m_pt2.getX(),m_pt2.getY());
    line(m_pt1.getX(),m_pt1.getY(),m_pt3.getX(),m_pt3.getY());
    line(m_pt2.getX(),m_pt2.getY(),m_pt4.getX(),m_pt4.getY());
  }
}

interface OrientTest{
  public int testPoint(Point p);
}

class NullTest implements OrientTest{
  public int testPoint(Point p){
    return -1;
  }
}

public int DET(Point p1, Point p2, Point p3)  {
  return sign( (p1.getX()*(p2.getY()-p3.getY())) + (p2.getX()*(p3.getY()-p1.getY())) + (p3.getX()*(p1.getY()-p2.getY())) );
}
  
public int DET4(Point p1, Point p2, Point p3, Point tp){
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

class LinesideTest implements OrientTest{
  private Point m_pt1;
  private Point m_pt2;
  
  public LinesideTest(Point p1, Point p2){
    m_pt1 = p1;
    m_pt2 = p2;
  }
  
  public int testPoint(Point tp){
    return DET(m_pt1,m_pt2,tp);
  }
}

class InTriangleTest implements OrientTest{
  private Point m_p1;
  private Point m_p2;
  private Point m_p3;
  
  public InTriangleTest(Point p1, Point p2, Point p3){
    m_p1 = p1;
    m_p2 = p2;
    m_p3 = p3;
  }
  
  public int testPoint(Point tp){
    int r1;
    int r2;
    int r3;
    
    r1 = DET(m_p1,m_p2,tp);
    r2 = DET(m_p2,m_p3,tp);
    r3 = DET(m_p3,m_p1,tp);
    
    if (r1 == 1 && r2 == 1 && r3 == 1){
      return 1;
    }
    if ( (r1 == 0 && r2 == 1 && r3 == 1) ||
         (r1 == 1 && r2 == 0 && r3 == 1) ||
         (r1 == 1 && r2 == 1 && r3 == 0) ){
           return 0;
    }
    return -1;
  }
}

class InConvexHullTest implements OrientTest{
  ArrayList<Point> m_pts;
  
  int vm;
  int left;
  
  //debug
  int count;
  
  public InConvexHullTest(ArrayList<Point> pts){
    m_pts = pts;
    vm = 0;
    //left;
    count = 1;
  }
  
  public int testPoint(Point tp){
    int vs = 1;
    int vf = m_pts.size()-1;
    
    return whichSide(vs,vf,tp);
  }
  
  private int whichSide(int vs, int vf, Point pt){
    //println("whichSide loop: "+count++);
    if (vf-vs == 1){
      int r1 = DET(m_pts.get(vs),m_pts.get(vf),pt);
      if (r1 != -1){
        if (vs == 1){
          int r2 = DET(m_pts.get(0),m_pts.get(vs),pt);
          if(r1 == 1 && r2 == 1){
            return 1;
          }else if ( (r1 == 1 && r2 == 0) || (r1 == 0 && r2 == 1) ){
            return 0;
          }else{
            return -1;
          }
        }else if (vf == m_pts.size()-1){
          int r2 = DET(m_pts.get(vf),m_pts.get(0),pt);
          if(r1 == 1 && r2 == 1){
            return 1;
          }else if ( (r1 == 1 && r2 == 0) || (r1 == 0 && r2 == 1) ){
            return 0;
          }else{
            return -1;
          }
        }
      }else return -1;
    }
    
    
    
    //println("vf: "+vf+ m_pts.get(vf)+" vs: "+vs+m_pts.get(vs)+ " vm: "+vm+m_pts.get(vm));
    vm = (vf-vs)/2 +vs;
    left = DET(m_pts.get(0),m_pts.get(vm),pt);
    if (left == 1){
      return whichSide(vm,vf,pt);
    }
    else{
      return whichSide(vs,vm,pt);
    }
  }
}
    
class InCircleTest implements OrientTest{
  private Point m_p1;
  private Point m_p2;
  private Point m_p3;
  private boolean mode; //true: distance test, false raise to the parabola
  
  public InCircleTest(Point p1, Point p2){
    m_p1 = p1;
    m_p2 = p2;
    mode = true;
  }
  
  public InCircleTest(Point p1, Point p2, Point p3){
    m_p1 = p1;
    m_p2 = p2;
    m_p3 = p3;
    mode = false;
  }
  
  public int testPoint(Point tp){
    if (mode){
      
      float x = abs(m_p1.getX()-m_p2.getX());
      float y = abs(m_p1.getY()-m_p2.getY());
      float csq1 = x*x + y*y;
      
      x = abs(m_p1.getX()-tp.getX());
      y = abs(m_p1.getY()-tp.getY());
      float csq2 = x*x + y*y;
      
      if (csq1 > csq2){
        return 1;
      }
      else if (csq1 < csq2){
        return -1;
      }
      else{
        return 0;
      }
    }
    else{
      return DET4(m_p1,m_p2,m_p3,tp);
    }
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
  //private ArrayList<Point> m_pts;
  private PointSpace m_pts;

  public ColorScreen(PointSpace p){
    m_pts = p;  
  }
  
  //public void addPoint(Point p){
  //  m_pts.add(p);
  //}
  
  public boolean colorize(){
    color c;
    Point p;
    for (int i = 0; i < gs.getWidth(); i++)
    {
      for (int j = 0; j < gs.getHeight(); j++)
      {
        //p = new Point(i,j);
        int r = test.testPoint(new Point(i,j));
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
        
        //stroke(0);
        //fill(c);
        paintBox(i,j,c);
      }
    }
    return true;
  }
  
  private void paintBox(int x, int y, color c){     
      Point[] ps = gs.boxToPixels(new Point(x,y));  
      
      for (int i = (int)ps[0].getX(); i <= (int)ps[1].getX(); i++){
        for (int j = (int)ps[0].getY(); j <= (int)ps[1].getY(); j++){
          set(i,j,c);
        }
      }
  }
}
    
class GraphScreen{ //also flips the y-axis
  int m_size; //of box in pixels high and wide
  int m_height; //number of boxes high the screen is
  int m_width; // number of boxes wide the screen is
  
  public GraphScreen(int size, int h, int w){
    m_size = size;
    m_height = h;
    m_width = w;
  }
    
  public int screenSizeH(){
    return m_height*m_size;
  }
    
  public int screenSizeW(){
    return m_width*m_size;
  }
  
  public int getHeight(){
    return m_height;
  }
  public int getWidth(){
    return m_width;
  }
  public int getBoxSize(){
    return m_size;
  }
  
  public Point pixelToBox(Point pixel)
  {
    float s = (float)m_size;
    
    float x = pixel.getX()/s;
    float y = (float)m_height - floor(pixel.getY()/s) -1;
    
    return new Point(x,y);
  }
  
  public Point[] boxToPixels(Point box){
    float s = (float)m_size;
    float h = (float)m_height;
    
    Point[] range = new Point[2];
 
    range[0] = new Point( (box.getX() * s), ( (h-box.getY()-1) * s));
    range[1] = new Point( ((box.getX() * s) + s-1), ( ((h-box.getY()) * s) -1 ));
    
    //debug
    if (count != 0){
      println("range[0](x,y) = ("+ range[0].getX() +","+range[0].getY()+")");
      println("range[1](x,y) = ("+ range[1].getX() +","+range[1].getY()+")\n");
      count--;
    }
    //

    return range;
  }
  
  //public void drawGrid(){
  //}
    
}
  


class PointSpace{
  ArrayList<Point> m_pts;
  
  float m_transX; //translation and scaling from the screen space to point space
  float m_transY;
  float m_scale;
  //distance between boxes?
  
  int m_index;
  
  public PointSpace(){
    m_pts = new ArrayList<Point>();
    m_transX = 0;
    m_transY = 0;
    m_scale  = 1;
    
    m_index  = 0;
  }
  
  public void addPoint(Point p){
    float px = p.getX();
    float py = p.getY();
    
    px += m_transX;
    py += m_transY;
    
    p.setX(px);
    p.setY(py);
    
    m_pts.add(p);
  }
  
  public void screenTrans(float x, float y){
    m_transX += x;
    m_transY += y;
  }
  
  public void screenScale( float s){
    m_scale *= s;
  }
  
  public Point getNextPoint(){ //returns in pointspace coordinates
    return m_pts.get(m_index++);
  }
  
  public Point getNextPointTrans(){ //returns in screen coordinates
    return m_pts.get(m_index++);
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
  
}

class Tester{
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
    
    if (size == 1){
      test = new NullTest();
    }
    
    if (m_mode){
      switch (size){
        case 2: test = new LinesideTest(m_pts.get(0),m_pts.get(1));
                break;
        case 3: test = new InTriangleTest(m_pts.get(0),m_pts.get(1),m_pts.get(2));
                break;
        default: test = new InConvexHullTest(ch.findConvexHull(m_pts));
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
  
  public void setMode(boolean mode, int pivot){
    m_mode = mode;
    m_pivot= pivot;
    if (!m_pts.isEmpty())
    {
      setPoints(m_pts);
    }
  }
  
  public int testPoint(Point p){
    println("Testing point: "+p.getX()+"'"+getY());
    return test.testPoint(p);
  }
}

class ConvexHull{
  
  public ArrayList<Point> findConvexHull(ArrayList<Point> pts){
    pts = sortByAngle(pts);
    
    PointStack stack = new PointStack();
    stack.push(pts.get(0));
    stack.push(pts.get(1));
    for (int i = 2; i <= pts.size()-1; i++){
      while( DET(stack.get2FromTop(),stack.getTop(),pts.get(i)) == 1 ){
        stack.pop();
      }
      stack.push(pts.get(i));
    }
    println("Convex Hull found");
    return stack.toArrayList();
  }
  public ArrayList<Point> sortByAngle(ArrayList<Point> pts){
    int leftmost = 0;
    float leastLeft = pts.get(0).getX();
    float t;
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
        if (DET(pts.get(0),pts.get(min),pts.get(i)) == 1){
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

class PointStack{
  PointNode head;
  
  public PointStack(){
    head = null;
  }
  
  public void push(Point p){
    PointNode n = new PointNode(p);
    if (head == null){
      head = n;
      //debug
      println("First head: "+ head.getPoint().getX() +","+head.getPoint().getY() );
    }else{
      println("Pushing: "+n.getPoint().getX() +","+n.getPoint().getY());
      n.setNext(head);
      //head.setNext(null);
      head = n;
      println("head: "+ head.getPoint().getX() +","+head.getPoint().getY() );
      println("next: "+ head.getNext().getPoint().getX() +","+head.getNext().getPoint().getY() );
    }
  }
  
  public Point pop(){
    if (head == null){
      //burn
      return null;
    }
    
    PointNode ret = head;
    head = head.getNext();
    println("popping: "+ ret.getPoint().getX() +","+ret.getPoint().getY() );
    return ret.getPoint(); 
  }
  
  public Point getTop(){
    return head.getPoint();
  }
  public Point get2FromTop(){
    //debug
    if (head == null){
      println("Null head");
    }
    if (head.getNext() == null){
      println("Null Next");
      println(head.getPoint().getX() +","+head.getPoint().getY());
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
      println("toArrayList loop: "+ count++);
      tp = pn.getPoint();
      println("adding point: "+tp.getX()+"'"+tp.getY());
      ret.add(tp);
      pn = pn.getNext();
    }
    println("Converted to ArrayList");
    return ret;
  }
}

class PointNode{
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
    
