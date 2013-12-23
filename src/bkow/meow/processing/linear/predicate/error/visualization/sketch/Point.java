package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class Point {
    private double m_X;
    private double m_Y;
    Point(double x, double y){
	m_X = x;
	m_Y = y;
    }
    Point(){
	m_X = 0;
	m_Y = 0;
    }
    double getX()
    {
	return m_X;
    }
    double getY(){
	return m_Y;
    }
    void setX(double x){
	m_X = x;
    }
    void setY(double y){
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
    
    public Point mul(double s){
	return new Point(m_X*s,m_Y*s);
    }
    
    public Point div(double s){
	return new Point(m_X/s,m_Y/s);
    }


}
