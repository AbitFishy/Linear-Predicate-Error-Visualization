package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class LinesideTest implements OrientTest {

    private Point m_pt1;
    private Point m_pt2;
    
    public LinesideTest(Point p1, Point p2){
      m_pt1 = p1;
      m_pt2 = p2;
    }
    
    public int testPoint(Point tp){
      return Determinate.DET(m_pt1,m_pt2,tp);
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
      
      r1 = Determinate.DET(m_p1,m_p2,tp);
      r2 = Determinate.DET(m_p2,m_p3,tp);
      r3 = Determinate.DET(m_p3,m_p1,tp);
      
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
