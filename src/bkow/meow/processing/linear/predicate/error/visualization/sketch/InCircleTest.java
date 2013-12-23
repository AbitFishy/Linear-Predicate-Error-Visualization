package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class InCircleTest implements OrientTest {

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
        
        double x = GlobalVar.proc().abs((float) (m_p1.getX()-m_p2.getX()));
        double y = GlobalVar.proc().abs((float) (m_p1.getY()-m_p2.getY()));
        double csq1 = x*x + y*y;
        
        x = GlobalVar.proc().abs((float) (m_p1.getX()-tp.getX()));
        y = GlobalVar.proc().abs((float) (m_p1.getY()-tp.getY()));
        double csq2 = x*x + y*y;
        
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
        return Determinate.DET4(m_p1,m_p2,m_p3,tp);
      }
    }

}
