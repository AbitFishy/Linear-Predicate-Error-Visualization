color above;
color linear;
color below;
color pointColor;
color lineColor;
color textColor;
color toolTextBGColor;
color toolTextButtonColor;
color toolTextButtonPressedColor;

ColorScreen cs;
GraphScreen gs;
ToolScreen  ts;
KeyboardInput ki;

PointSpace ps;
Tester test;

ConvexHull ch;

Point downMousePos;
Point dragMousePos;



//debuging
int count = 5;



void setup(){
  gs = new GraphScreen(3,256,400);
  ts = new ToolScreen(20);
  ki = new KeyboardInput();
  
  size(gs.screenSizeW(),gs.screenSizeH()+ts.getHeight()); 
  stroke(0);
  background(128);
  
  ps   = new PointSpace();
  cs   = new ColorScreen(ps);
  test = new Tester();
  ch   = new ConvexHull();
  
  above   = color(128,255,255);
  linear  = color(255,0,0);
  below   = color(255,165,0);
  pointColor = color(0,0,255);
  lineColor = color(0,0,0);
  textColor = color(0,0,0);
  toolTextBGColor = color(0,0,255);
  toolTextButtonColor = 0x9E4600FF;
  toolTextButtonPressedColor = 0xF4460022;
  
  downMousePos = new Point(-1,-1);
  dragMousePos = new Point(-1,-1);
  
  ps.scaleAndTrans(true);
  
  test.setPoints(ps.getPoints());
  cs.colorize();
  
  //end setup
}

void draw(){
  ts.drawLabels();
  cs.draw();
  
  //drawlines
      
  //drawpoints
  ps.resetQueue();
  Point point = ps.getNextPoint();
  while (point != null){
    //println("Painting point: "+point);
    cs.paintBox(ps.pointToBox(point),pointColor);
    point = ps.getNextPoint();
  }
  if (ki.willAcceptKeys()){
    ki.drawKeyboardInput();
  }
}

void mousePressed(){
  downMousePos.setX(mouseX);
  downMousePos.setY(mouseY);
  if (mouseY < gs.screenSizeH()){
    dragMousePos.setX(mouseX);
    dragMousePos.setY(mouseY);
  }
  else{
    if (ts.isButtonPressed()){
      //draw();
    }
  }
}

void mouseDragged(){
  if (mouseButton == LEFT){
    if (mouseY < gs.screenSizeH()){//moved and in graph area
        boolean negX;
        boolean negY;
        
        float transX = -( mouseX - dragMousePos.getX())/ gs.getBoxSize();
        float transY = ( mouseY - dragMousePos.getY())/ gs.getBoxSize();
        //negX = transX < 0 ? true:false;
        //negY = transY < 0 ? true:false;
        //transX= -(transX);
        //transY = (transY);
        //transX = negX == true ? transX-1 : transX;
        //transY = negY == true ? transY+1 : transY;
        
        dragMousePos.setX(mouseX);
        dragMousePos.setY(mouseY);
        ps.screenTrans(transX,transY);
     }
  }
}
     

void mouseReleased(){
  if (ts.isWaitingForInput()){
    if (ki.doubleClickCount++ == 3){
      ts.cancelInput();
      ki.doubleClickCount = 0;
    }
  }
  else if (downMousePos.getX() == mouseX && downMousePos.getY() == mouseY){
    //only do this when clicked in graph area.
    if (mouseY < gs.screenSizeH()){
      BoxPoint pt = gs.pixelToBox(new BoxPoint(mouseX,mouseY));
      if (mouseButton == LEFT){ 
        println("Clicked point: "+pt);
        ps.addPoint(pt);//here it should convert from screen coordinates to space coordinates
        println("Number of Points: "+ps.getNumPts());
      }
      else if (mouseButton == RIGHT){
        boolean rm = ps.removePoint(pt);
        if (rm){
          println("Removed point: "+ps.boxToPoint(pt));
        }
        else{
          println("Nothing at: "+ps.boxToPoint(pt));
        }
      }
    
    
      test.setPoints(ps.getPoints());
      println("Points set");
    
      //color results
      cs.colorize();
      
      
      
    }else{
      //tool area
      ts.release(true);
      
    }
  }else{ //mouse moved
    ts.release(false);
    test.setPoints(ps.getPoints());
    cs.colorize();
       
  }
  
  downMousePos.setX(-1);
  downMousePos.setY(-1);
  dragMousePos.setX(-1);
  dragMousePos.setY(-1);
}

void mouseWheel(MouseEvent event){
  float amount = event.getAmount();
  ps.screenScale(-amount);
  
  test.setPoints(ps.getPoints());
  println("Points set after scroll");
  cs.colorize();
  println("Colorized after scroll");
  
}

void keyTyped(){
  if (ki.willAcceptKeys()){
    ki.inputKey();
  }  
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
  @Override
  public boolean equals(Object tp){
    return (m_X== ((Point)tp).getX() && m_Y== ((Point)tp).getY());
  }
  
  public Point add(Point p){
    return new Point(p.getX()+m_X,p.getY()+m_Y);
  }
  
  public Point sub(Point p){
    return new Point(m_X-p.getX(),m_Y-p.getY());
  }
  
  public Point mul(float s){
    return new Point(m_X*s,m_Y*s);
  }
  
  public Point div(float s){
    return new Point(m_X/s,m_Y/s);
  }
}

class BoxPoint{
  private int m_X;
  private int m_Y;
  public BoxPoint(int x, int y){
    m_X = x;
    m_Y = y;
  }
  public BoxPoint(){
    m_X = 0;
    m_Y = 0;
  }
  int getX()
  {
    return m_X;
  }
  int getY(){
    return m_Y;
  }
  void setX(int x){
    m_X = x;
  }
  void setY(int y){
    m_Y = y;
  }
  @Override
  public String toString(){
    return new String("("+m_X+","+m_Y+")");
  }
  
  public BoxPoint add(BoxPoint p){
    return new BoxPoint(p.getX()+m_X,p.getY()+m_Y);
  }
  
  public BoxPoint sub(BoxPoint p){
    return new BoxPoint(m_X-p.getX(),m_Y-p.getY());
  }
  
  public BoxPoint mul(int s){
    return new BoxPoint(m_X*s,m_Y*s);
  }
  
  public BoxPoint div(int s){
    return new BoxPoint(m_X/s,m_Y/s);
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

class inSmallestCircleTest implements OrientTest{
  ArrayList<Point> m_pts;
  
  float m_center;
  float m_radius;
  
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
  private color[][] m_screen;

  public ColorScreen(PointSpace p){
    m_pts = p;
    m_screen = new color[gs.getWidth()][gs.getHeight()];  
  }
  
  //public void addPoint(Point p){
  //  m_pts.add(p);
  //}
  
  public boolean colorize(){
    color c;
    Point p;
    for (int i = 0; i < gs.getWidth(); i++)
    {
      //println(i);
      for (int j = 0; j < gs.getHeight(); j++)
      {
        //println(j);
        //p = new Point(i,j);
        int r = test.testPoint(ps.boxToPoint(new BoxPoint(i,j)));
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
        m_screen[i][j] = c;
        paintBox(i,j,c);
      }
    }
    return true;
  }
  public void paintBox(int x, int y,color c){
    paintBox(new BoxPoint(x,y),c);
  }  
  public void paintBox(BoxPoint bp, color c){     
      BoxPoint[] ps = gs.boxToPixels(bp);  
      
      for (int i = (int)ps[0].getX(); i <= (int)ps[1].getX(); i++){
        for (int j = (int)ps[0].getY(); j <= (int)ps[1].getY(); j++){
          set(i,j,c);
        }
      }
  }
  
  public void draw(){
    color c;
    for (int i = 0; i < gs.getWidth(); i++){
      for (int j = 0; j < gs.getHeight(); j++){
        c = m_screen[i][j];
        BoxPoint p = ps.pointToBox(ps.boxToPoint(new BoxPoint(i,j)));
        paintBox(p.getX(),p.getY(),c);
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
  
  public BoxPoint pixelToBox(Point pixel)
  {
    int s = m_size;
    
    int x = floor(pixel.getX()/s);
    int y = m_height - floor(pixel.getY()/s) -1;
    
    return new BoxPoint(x,y);
  }
  
  public BoxPoint pixelToBox(BoxPoint pixel)
  {
    int s = m_size;
    
    int x = pixel.getX()/s;
    int y = m_height - pixel.getY()/s -1;
    
    return new BoxPoint(x,y);
  }
  
  public BoxPoint[] boxToPixels(BoxPoint box){
    int s = m_size;
    int h = m_height;
    
    BoxPoint[] range = new BoxPoint[2];
 
    range[0] = new BoxPoint( (box.getX() * s), ( (h-box.getY()-1) * s));
    range[1] = new BoxPoint( ((box.getX() * s) + s-1), ( ((h-box.getY()) * s) -1 ));
    
    //debug
    //if (count != 0){
   //   println("range[0](x,y) = ("+ range[0].getX() +","+range[0].getY()+")");
   //   println("range[1](x,y) = ("+ range[1].getX() +","+range[1].getY()+")\n");
    //  count--;
   // }
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
  float m_distBTWBoxes;
  float m_scaleFactor;
  
  int m_index;
  boolean scaleAndTrans;
  
  public PointSpace(){
    m_pts = new ArrayList<Point>();
    m_transX = 0;
    m_transY = 0;
    m_scale  = 1;
    m_distBTWBoxes = 1;
    m_scaleFactor = 2;
    
    m_index  = 0;
    scaleAndTrans = false;
  }
  
  public void addPoint(Point p){//Screen to World
    float sx = p.getX();
    float sy = p.getY();
    
    //px += m_transX;
    //py += m_transY;
    float wx = sx/m_scale + m_transX;
    float wy = sy/m_scale + m_transY;
    
    p.setX(wx);
    p.setY(wy);
    
    m_pts.add(p);
  }
  
  public void addPoint(BoxPoint p){
    float sx = float(p.getX());
    float sy = float(p.getY());
    float wx = sx/m_scale + m_transX;
    float wy = sy/m_scale + m_transY;

    m_pts.add(new Point(wx,wy));
  }
  
  public boolean removePoint(Point p){ //world coordinates
    return m_pts.remove(p);
  }
  public boolean removePoint(BoxPoint p){//screen to world conversion
    return removePoint(boxToPoint(p));
  }
    
  
  public void screenTrans(float x, float y){
    m_transX += x * (1/m_scale);
    m_transY += y * (1/m_scale); 
  }
  
  public void screenScale( float s){
    
    Point A = boxToPoint(new BoxPoint(0,0));
    Point B = boxToPoint(new BoxPoint(gs.getWidth()-1,gs.getHeight()-1));
    Point mouse =  boxToPoint(gs.pixelToBox(new Point (mouseX,mouseY)));
    
    if (s >0){
      s *= m_scaleFactor;
    }
    else if(s<0){
      s = -1/(s*m_scaleFactor);
    }
    else{
      s = 1;
    }
    
    m_scale *= s;
    m_distBTWBoxes = 1/m_scale;
    println("Scaling by: " + s + ", Scale: " + m_scale);
    
    if (scaleAndTrans){  
      //Point mouse =  boxToPoint(gs.pixelToBox(new Point (mouseX,mouseY)));
      println("A: " + A + " B: " +B+" mouse: "+mouse);
      
      //Point A = boxToPoint(new BoxPoint(0,0));
      //Point B = boxToPoint(new BoxPoint(gs.getWidth(),gs.getHeight()));
      
      //Point len (B-A)/m_scale;
      Point oldLen = B.sub(A);
      //Point oldLen = B.add(new Point(1,1));
      Point len = oldLen.div( (m_scaleFactor));
      println("oldlen: "+oldLen+" len: "+len);
      
      float divider = (s < 1) ? .5 : 2;
      Point botLeft= mouse.sub(len.div(divider));
      println("len.div: " + len.div(divider) + " botLeft: " + botLeft);
      
      m_transX = botLeft.getX();
      m_transY = botLeft.getY();
      
      println("Translate zoom: "+m_transX+" "+m_transY);
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
  
  public void setScaleFactor(float sf){
    m_scaleFactor = sf;
  }
  
  public float getScaleFactor(){
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
  
  public float getDistBTWBoxes(){
    return m_distBTWBoxes;
  }
  
  public BoxPoint pointToBox(Point pt){ //world to screen conversion
    return (pt == null) ? null : new BoxPoint( (int)(m_scale*(pt.getX()-m_transX)), (int)(m_scale*(pt.getY()-m_transY)));
    //
    //testing
    //BoxPoint sp = new BoxPoint( (int)(m_scale*(pt.getX()-m_transX)), (int)(m_scale*(pt.getY()-m_transY)));
    
    
  }
  
  public Point boxToPoint(BoxPoint pt){ //screen to world conversion
    //return new Point(float(pt.getX()), float( pt.getY()));
    return (pt == null) ? null : new Point( (pt.getX()/m_scale) + m_transX, (pt.getY()/m_scale) +m_transY);
  }
  
  public void scalePoints(float s){
    for (int i = 0; i <m_pts.size();i++){
      m_pts.set(i,m_pts.get(i).mul(s));
    }
  }
  public void translatePoints(float x, float y){
    for (int i = 0; i <m_pts.size();i++){
      m_pts.set(i,m_pts.get(i).add(new Point(x,y)));
    }
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
    println(size + "(s) in set");
    
    if (size <= 1){
      test = new NullTest();
    }
    else{
      if (m_mode){
        switch (size){
          case 2: test = new LinesideTest(m_pts.get(0),m_pts.get(1));
                  break;
          case 3: test = new InTriangleTest(m_pts.get(0),m_pts.get(1),m_pts.get(2));
                  break;
          case 4: test = new InConvexHullTest(ch.findConvexHull(m_pts));
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

class ToolScreen{
  int m_height;
  int labelOffset;
  int labelHeight;
  int labelStart;
  int mouseLabel;
  int distLabel;
  int scaleLabel;
  int transLabel; 
  int xRangeLabel;
  int yRangeLabel;
  
  String mouseText;
  String distText;
  String scaleText;
  String transText;
  String xRangeText;
  String yRangeText;
  
  int buttonStartY;
  int buttonEndY;
  
  boolean isScaleDown;
  boolean isTransDown;
  
  Point[] scaleButtonArea;
  Point[] transButtonArea;
  int inputOffset;
  boolean waitForScaleInput;
  boolean waitForTransInput;
  
  public ToolScreen(int h){
    m_height = h;
    labelOffset = 15;
    labelHeight = gs.screenSizeH() +labelOffset;
    labelStart = 5;
    mouseLabel = labelStart;
    distLabel  = mouseLabel   + 200;
    scaleLabel = distLabel    + 230;
    transLabel = scaleLabel   + 100;
    xRangeLabel= transLabel   + 125;
    yRangeLabel= xRangeLabel  + 220;
    
    mouseText = "Mouse at: ";
    distText  = "Distance btw squares: ";
    scaleText = "Scale Points";
    transText = "Translate Points";
    xRangeText= "X-Range: ";
    yRangeText= "Y-Range: ";
    
    buttonStartY = labelHeight-labelOffset+3;
    buttonEndY   = m_height-7;
    
    scaleButtonArea = new Point[2];
    transButtonArea = new Point[2];
    scaleButtonArea[0] = new Point(scaleLabel-10,buttonStartY);
    scaleButtonArea[1] = new Point(90,buttonEndY);
    transButtonArea[0] = new Point(transLabel-10,buttonStartY);
    transButtonArea[1] = new Point(transLabel-scaleLabel+10,buttonEndY);
    
    isScaleDown = false;
    isTransDown = false;
    
    inputOffset = 30;
    waitForScaleInput = false;
    waitForTransInput = false;
    
  }
  public ToolScreen(){
    m_height = 20;
  }
  public void setHeight(int h){
    m_height = h;
  }
  public int getHeight(){
    return m_height;
  }
  
  public void drawLabels(){
      //Tool text
    int xDraw = gs.screenSizeW();
    int yDraw = gs.screenSizeH();
    fill(toolTextBGColor);
    stroke(0,0,0);
    rect(0,yDraw,xDraw-1,m_height-1);
    
    fill(toolTextButtonColor);
    rect(scaleButtonArea[0].getX(),scaleButtonArea[0].getY(),scaleButtonArea[1].getX(),scaleButtonArea[1].getY());
    rect(transButtonArea[0].getX(),transButtonArea[0].getY(),transButtonArea[1].getX(),transButtonArea[1].getY());
    if (isScaleDown){
      fill(toolTextButtonPressedColor);
      rect(scaleButtonArea[0].getX(),scaleButtonArea[0].getY(),scaleButtonArea[1].getX(),scaleButtonArea[1].getY());
    }
    else if (isTransDown){
      fill(toolTextButtonPressedColor);
      rect(transButtonArea[0].getX(),transButtonArea[0].getY(),transButtonArea[1].getX(),transButtonArea[1].getY());
    }
    
    Point mouse = ps.boxToPoint(gs.pixelToBox(new BoxPoint(mouseX,mouseY)));
    Point bLeft = ps.boxToPoint(new BoxPoint(0,0));
    Point tRight= ps.boxToPoint(new BoxPoint(gs.getWidth()-1,gs.getHeight()-1));
    fill(textColor);
    text(mouseText + mouse,mouseLabel,labelHeight);//change to point coordinates
    text(distText  + ps.getDistBTWBoxes(), distLabel, labelHeight);
    text(scaleText, scaleLabel,labelHeight);
    text(transText, transLabel,labelHeight);
    text(xRangeText+ bLeft.getX()+" : "+tRight.getX()+"  "+yRangeText+ bLeft.getY()+" : "+tRight.getY(), xRangeLabel,labelHeight);
   // text(yRangeText+ tRight.getX()+"-"+tRight.getY(), yRangeLabel,labelHeight);
    
  }
  
  public boolean isButtonPressed(){
    if (isPointInBox(mouseX,mouseY,(int)scaleButtonArea[0].getX(),(int)scaleButtonArea[0].getY(),(int)scaleButtonArea[1].getX(),(int)scaleButtonArea[1].getY())){
      isScaleDown = true;
      println("Scale Button Down");
      return true;
    }
    else if (isPointInBox(mouseX,mouseY,(int)transButtonArea[0].getX(),(int)transButtonArea[0].getY(),(int)transButtonArea[1].getX(),(int)transButtonArea[1].getY())){
      isTransDown = true;
      println("Translate Button Down");
      return true;
    }
    else{
      return false;
    }
  }
  public void release(boolean clicked){
    if (!clicked){
      isScaleDown = false;
      isTransDown = false;
    }
    else{
      if (isScaleDown){
        scaleButtonInput();
      }
      else if (isTransDown){
        transButtonInput();
      }
      else{
        println("Release function failure");
      }
      
      
      isScaleDown = false;
      isTransDown = false; 
    }
  }
  
  private void scaleButtonInput(){
    //String input = getTextInput(scaleButtonArea[0].getX(),scaleButtonArea[0].getY()-inputOffset);
    ki.inputKeyboard(true,scaleButtonArea[0].getX(),scaleButtonArea[0].getY()-inputOffset);
    waitForScaleInput = true;
  }
  private void transButtonInput(){
    //String input = getTextInput(transButtonArea[0].getX(),transButtonArea[0[].getY()-inputOffset);
    ki.inputKeyboard(true,transButtonArea[0].getX(),transButtonArea[0].getY()-inputOffset);
    waitForTransInput = true;
  }
  
  public boolean isWaitingForInput(){
    if (waitForTransInput || waitForTransInput){
      return true;
    }
    else{
      return false;
    }
  }
  
  public void cancelInput(){
    waitForScaleInput = false;
    waitForTransInput = false;
    ki.inputKeyboard(false,0,0);
  }
  
  public void sendInput(String input){
    println("Input: " +input);
    try{    
      if (waitForScaleInput){
        float scaling = Float.parseFloat(input);
        println("Scaling by: "+scaling);
        ps.scalePoints(scaling);
        println("Input Success");
      }
      else if (waitForTransInput){
        float xTran;
        float yTran;
        int commaLoc = input.indexOf(',');
        if (commaLoc == -1){
          xTran = Float.parseFloat(input);
          println("Translate x: "+xTran);
          ps.translatePoints(xTran,0);
          println("Input Success");
        }
        else{
          xTran = Float.parseFloat(input.substring(0,commaLoc));
          yTran = Float.parseFloat(input.substring(commaLoc+1,input.length()));
          println("Translate x: "+xTran+ "  y: "+yTran);
          ps.translatePoints(xTran,yTran);
          println("Input Success");
        }
          
      }
    }
    catch (java.lang.NumberFormatException e){
      println("Bad input");
    }
    finally {
      
    }
    
    cancelInput();
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

public boolean isPointInBox(int pointX, int pointY, int rectX, int rectY, int w, int h){
  if (pointX >= rectX && pointX <= rectX+w && pointY >= rectY && pointY <= pointY+h){
    return true;
  }
  else{
    return false;
  }
}

class KeyboardInput{ //a sloppily constructed class
  private float m_x;
  private float m_y;
  String inputString;
  Point textPos;
  boolean acceptKeys;
  BoxPoint textBoxPos;
  int doubleClickCount;
  
  public KeyboardInput(){
    acceptKeys = false;
    textBoxPos = new BoxPoint(-1,-1);
    inputString = new String();
    textPos = new Point(-1,-1);
    doubleClickCount = 0;
  }
  
  public void inputKeyboard(boolean yes, float x, float y){
    if (yes){
      acceptKeys = true;
      //textInputPos.setX((int)x);
      //textInputPos.setY((int)y);
      stroke(0,0,0);
      fill(255,255,255);
      rect(x,y, 100, 15);
      textPos.setX(x+3);
      textPos.setY(y+12);
      m_x = x;
      m_y = y;
    }
    else{
      acceptKeys = false;
      //textInputPos.setX(-1);
      //textInputPos.setY(-1);
      textPos.setY(-1);
      textPos.setX(-1);
      inputString = new String();
      test.setPoints(ps.getPoints());
      cs.colorize();
    } //when will the box get over written?
  }
  
  public boolean willAcceptKeys(){
    return acceptKeys;
  }

  public void drawKeyboardInput(){
    stroke(0,0,0);
    fill(255,255,255);
    rect(m_x,m_y, 100, 15);
    fill(textColor);
    text(inputString,m_x+3,textPos.getY());
  }
  
  public void inputKey(){
    switch (key){
      case ENTER:
      case RETURN:
        ts.sendInput(inputString);
        inputKeyboard(false,0,0);
        inputString = new String();
        break;
      case ESC:
      case BACKSPACE:
        ts.cancelInput();
        break;
      default:
        inputString += key;
        float x = textPos.getX();
        float y = textPos.getY();
        text(key,x,y);
        x += 7;
        textPos.setX(x);
        break;
    }
  }
}
  
  
