package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class Point {
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
