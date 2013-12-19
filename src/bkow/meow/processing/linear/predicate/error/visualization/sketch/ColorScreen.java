package bkow.meow.processing.linear.predicate.error.visualization.sketch;

public class ColorScreen {
    //private ArrayList<Point> m_pts;
    //private PointSpace m_pts;
    private Color[][] m_screen;

    public ColorScreen(GraphScreen gs){
      //m_pts = p;
      m_screen = new Color[gs.getWidth()][gs.getHeight()];  
    }
    
    //public void addPoint(Point p){
    //  m_pts.add(p);
    //}
    
    public boolean colorize(){
      Color c;
      Point p;
      
      Point p1 = GlobalVar.ps().boxToPoint(new BoxPoint(2,2));
      Point p2 = GlobalVar.ps().boxToPoint(new BoxPoint(2,3));
      int count = 2;
      
      for (int i = 0; i < GlobalVar.gs().getWidth(); i++)
      {
        //println(i);
        for (int j = 0; j < GlobalVar.gs().getHeight(); j++)
        {
          //println(j);
          //p = new Point(i,j);
          
          int r = GlobalVar.test().testPoint(GlobalVar.ps().boxToPoint(new BoxPoint(i,j)));
          if (r == 1)
          {
            c = GlobalVar.above();
          }
          else if (r == 0)
          {
            c = GlobalVar.linear();
          }
          else
          {
             c = GlobalVar.below();
          }
          
          //stroke(0);
          //fill(c);
          m_screen[i][j] = c;
          paintBox(i,j,c);
        }
      }
      GlobalVar.proc().println("Dist btw box during tests " +p1+p2);
      return true;
    }
    public void paintBox(int x, int y,Color c){
      paintBox(new BoxPoint(x,y),c);
    }  
    public void paintBox(BoxPoint bp, Color c){     
        BoxPoint[] ps = GlobalVar.gs().boxToPixels(bp);  
        
        for (int i = (int)ps[0].getX(); i <= (int)ps[1].getX(); i++){
          for (int j = (int)ps[0].getY(); j <= (int)ps[1].getY(); j++){
            GlobalVar.proc().set(i,j,c.color());
          }
        }
    }
    
    public void draw(){
      Color c;
      for (int i = 0; i < GlobalVar.gs().getWidth(); i++){
        for (int j = 0; j < GlobalVar.gs().getHeight(); j++){
          c = m_screen[i][j];
          BoxPoint p = GlobalVar.ps().pointToBox(GlobalVar.ps().boxToPoint(new BoxPoint(i,j)));
          paintBox(p.getX(),p.getY(),c);
        }
      }
    }
}
