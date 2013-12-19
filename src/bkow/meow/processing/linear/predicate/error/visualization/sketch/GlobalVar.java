package bkow.meow.processing.linear.predicate.error.visualization.sketch;

import processing.core.PApplet;

public class GlobalVar {
    private static PApplet proc;
    private static PointSpace ps;
    private static ColorScreen cs;
    private static GraphScreen gs;
    private static ToolScreen  ts;
    private static KeyboardInput ki;
    private static Tester test;
    private static ConvexHull ch;
    private static DrawLine dl;
    
    private static Color above;
    private static Color linear;
    private static Color below;
    private static Color pointColor;
    private static Color lineColor;
    private static Color textColor;
    private static Color toolTextBGColor;
    private static Color toolTextButtonColor;
    private static Color toolTextButtonPressedColor;
    
    public static void setVars(PApplet pproc, PointSpace pps, GraphScreen pgs, ColorScreen pcs, ToolScreen pts, KeyboardInput pki, Tester ptest,
	    ConvexHull pch, DrawLine pdl){
	if (proc == null){
	    proc = pproc;
	    ps = pps;
	    cs = pcs;
	    gs = pgs;
	    ts = pts;
	    ki = pki;
	    test = ptest;
	    ch = pch;
	    dl = pdl;
	}
    }
    
    public static void setColors(Color a, Color l, Color b, Color pc, Color lc, Color tc, Color ttbgc, Color ttbc, Color ttbpc){
	if (above == null){
        	above = a;
        	linear = l;
        	below = b;
        	pointColor = pc;
        	lineColor = lc;
        	textColor = tc;
        	toolTextBGColor = ttbgc;
        	toolTextButtonColor = ttbc;
        	toolTextButtonPressedColor = ttbpc;
	}
    }
    public static PApplet proc(){
	return proc;
    }
    public static PointSpace ps(){
	return ps;
    }
    public static ColorScreen cs(){
	return cs;
    }
    public static GraphScreen gs(){
	return gs;
    }
    public static ToolScreen ts(){
	return ts;
    }
    public static KeyboardInput ki(){
	return ki;
    }
    public static Tester test(){
	return test;
    }
    public static ConvexHull ch(){
	return ch;
    }
    public static DrawLine dl(){
	return dl;
    }
    
    public static Color above(){
	return above;
    }
    public static Color linear(){
	return linear;
    }
    public static Color below(){
	return below;
    }
    public static Color pointColor(){
	return pointColor;
    }
    public static Color lineColor(){
	return lineColor;
    }
    public static Color textColor(){
	return textColor;
    }
    public static Color toolTextBGColor(){
	return toolTextBGColor;
    }
    public static Color toolTextButtonColor(){
	return toolTextButtonColor;
    }
    public static Color toolTextButtonPressedColor(){
	return toolTextButtonPressedColor;
    }

}
