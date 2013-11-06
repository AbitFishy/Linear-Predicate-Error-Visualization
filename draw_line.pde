GraphScreen gs;

Point P1;
Point P2;

DrawLine dl;

void setup(){
  gs = new GraphScreen(5,96,96);
  size(gs.getScreenH(),gs.getScreenW()); //also include the text fields in future
  stroke(0);
  background(255);
  
  P1 = new Point(100,350);
  P2 = new Point(400,300);
  dl = new DrawLine(P1,P2);
}

class Point{
  private float m_X;
  private float m_Y;
  Point(float x, float y){
    m_X = x;
    m_Y = y;
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


class GraphScreen{
  //The boxes here are abstact pixels, all points scale to fit one box
  int m_boxDim; //in number of pixels
  int m_boxesH;
  int m_boxesW;
  
  int m_screenH;
  int m_screenW;
  
  int[][] m_boxes;
  
  public GraphScreen(int bd, int bh, int bw){
    m_boxDim = bd;
    m_boxesH = bh;
    m_boxesW = bw;
    
    m_screenH = m_boxDim * m_boxesH;
    m_screenW = m_boxDim * m_boxesW;
    
    m_boxes = new int[m_boxesH][m_boxesW];
  }
  
  public int getScreenH(){
    return m_screenH;
  }
  
  public int getScreenW(){
    return m_screenW;
  }
  
  
}
  
 

void draw(){
  stroke(0);
  dl.draw();
  stroke(255,0,0);
  point(P1.getX(),P1.getY());
  point(P2.getX(),P2.getY());
}



