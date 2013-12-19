package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class BoxPoint {
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
    @Override
    public boolean equals(Object tp){
      return (m_X == ((BoxPoint)tp).getX() && m_Y == ((BoxPoint)tp).getY());
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
