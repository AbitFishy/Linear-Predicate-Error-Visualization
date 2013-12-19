package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import java.util.ArrayList;

public class InConvexHullTest implements OrientTest {

    
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
    
    @Override
    public int testPoint(Point tp){
      int vs = 1;
      int vf = m_pts.size()-1;
      
      return whichSide(vs,vf,tp);
    }
    
    private int whichSide(int vs, int vf, Point pt){
      //println("whichSide loop: "+count++);
      if (vf-vs == 1){
        int r1 = Determinate.DET(m_pts.get(vs),m_pts.get(vf),pt);
        if (r1 != -1){
          if (vs == 1){
            int r2 = Determinate.DET(m_pts.get(0),m_pts.get(vs),pt);
            if(r1 == 1 && r2 == 1){
              return 1;
            }else if ( (r1 == 1 && r2 == 0) || (r1 == 0 && r2 == 1) ){
              return 0;
            }else{
              return -1;
            }
          }else if (vf == m_pts.size()-1){
            int r2 = Determinate.DET(m_pts.get(vf),m_pts.get(0),pt);
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
      left = Determinate.DET(m_pts.get(0),m_pts.get(vm),pt);
      if (left == 1){
        return whichSide(vm,vf,pt);
      }
      else{
        return whichSide(vs,vm,pt);
      }
    }
}
