package bkow.meow.processing.linear.predicate.error.visualization.sketch;


public class Color {
    
    private int color;
    
    Color (int c){
	color = c;
    }
    
    public int color(){
	return color;
    }
    
    public static int color(int r,int g ,int b ,int a){
	int ret = a >> 24;
	r = r >> 24;
	ret = r << 8;
	g = g >> 24;
	ret += g << 16;
	b = b >> 24;
	ret += b << 24;
	return ret;
	
    }
    public static int color(int r, int g, int b){
	return color(r,g,b,0);
    }
    
    public void color(int c){
	color = c;
    }

}
