package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.math.BigDecimal;

public class ExactPoint {
    private BigDecimal m_X;
    private BigDecimal m_Y;
    public ExactPoint(BigDecimal x, BigDecimal y){
	m_X = x;
	m_Y = y;
    }
    public ExactPoint(int x, int y){
      m_X = new BigDecimal(x);
      m_Y = new BigDecimal(y);
    }
    public ExactPoint(){
      m_X = BigDecimal.ZERO;
      m_Y = BigDecimal.ZERO;
    }
    BigDecimal getX()
    {
      return m_X;
    }
    BigDecimal getY(){
      return m_Y;
    }
    void setX(BigDecimal x){
      m_X = x;
    }
    void setY(BigDecimal y){
      m_Y = y;
    }
    void setX(int x){
	m_X = new BigDecimal(x);
    }
    void setY(int y){
	m_Y = new BigDecimal(y);
    }
    @Override
    public String toString(){
      return new String("("+m_X+","+m_Y+")");
    }
    @Override
    public boolean equals(Object tp){
      return m_X.compareTo(((ExactPoint)tp).getX()) == 0 && m_Y.compareTo( ((ExactPoint)tp).getY()) == 0;
    }
    
    public ExactPoint add(ExactPoint p){
      return new ExactPoint(p.getX().add(m_X),p.getY().add(m_Y));
    }
    
    public ExactPoint sub(ExactPoint p){
      return new ExactPoint(m_X.subtract(p.getX()),m_Y.subtract(p.getY()));
    }
    
    public ExactPoint mul(double s){
      return new ExactPoint(m_X.multiply(new BigDecimal(s)),m_Y.multiply(new BigDecimal(s)));
    }
    
    public ExactPoint div(double s){
      return new ExactPoint(m_X.divide(new BigDecimal(s)),m_Y.divide(new BigDecimal(s)));
    }
    
    public ExactPoint mul(BigDecimal s){
	return new ExactPoint(m_X.divide(s),m_Y.divide(s));
    }
    
    public ExactPoint div(BigDecimal s){
	return null;
    }
}
