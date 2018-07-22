package src.work2014.java.all;;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;


import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


class window extends JPanel implements MouseListener, MouseMotionListener {
	
	ArrayList<Point> p = new ArrayList<Point>();

	Point curvePoints[]= new Point[100];
	Point controlPoints[] = new Point[0];
	int n = 99, i = 3;
	boolean paint = false; ;
	Graphics g; Point d = null;
	int click = 0;
	
	public void paintCompenent(Graphics g){
		super.paintComponent(g);
		
		
		
	}
	
	public void paint(Graphics g){
		
		super.paint(g);
		Dot(d, g);		
		try {
			
			drawBezier(3, g);

		} catch (Exception e){
			
			g.setColor(Color.red);
			g.drawString("cP: "+e.getMessage(), 50, 50);
			
		}
	}
	
	
	public void Dot(Point d, Graphics g){
		g.setColor(Color.red);
		for (int c=0; c<p.size(); c++)
		g.drawOval(d.x-2, d.y-2, +4, +4);
		//getGraphics();
	}


	public void drawBezier(int i, Graphics g)
	{
			int j;
	    	double t, delta;
	    	
	    	delta = 1.0/n;
	   
	    	
		for (j=0;j<=controlPoints.length;j++)
	    {

	    	t=j*delta;
	    	if (controlPoints.length < 43)
	    		return;
	    	curvePoints[j] = new Point();
	    	Point d = retP(controlPoints,t);
	    	curvePoints[j] = new Point(d);
			

	    }
	    g.setColor(Color.black);
	    for (j=0;j<controlPoints.length;j++)
	    g.drawLine(curvePoints[j].x,curvePoints[j].y,curvePoints[j+1].x,curvePoints[j+1].y);
	  
	   
	} // End drawBezier

	
	

	

	private Point retP(Point[] cp, double t) {
		
		for (int iter = cp.length ; iter > 0 ; iter--) {
			for (int i = 3 ; i < iter ; i++) {
				cp[i-3] = interpol(cp[i-1], cp[i], t); 
				cp[i-2] = interpolc2(cp[i-2],cp[i-1],t);
				cp[i-1] = interpolc3(cp[i-3],cp[i-2], t);
				
				
			}
		}
		return cp[0];
	    

	}

	private Point interpol(Point p1, Point p2, double t){
		int x,y;
		
		x = (int)Math.round(
				p1.x*(1.0-t)*(1.0-t)*(1.0-t) + p2.x*3.0*t*(1.0-t)*(1.0-t));
        y = (int)Math.round(
        		p1.y*(1.0-t)*(1.0-t)*(1.0-t) + p2.y*3.0*t*(1.0-t)*(1.0-t));
		return new Point (x,y);
		
	}
	
	private Point interpolc3(Point p3, Point p4, double t) {
		int x,y;
		
		x = (int)Math.round(
				p3.x*3.0*t*t*(1.0-t) + p4.x*t*t*t);
        y = (int)Math.round(
        		p3.y*3.0*t*t*(1.0-t) + p4.y*t*t*t);
		return new Point(x,y);
		
        
		
	}

	private Point interpolc2(Point p2, Point p3, double t) {
		int x,y;
		
		x = (int)Math.round(
				p2.x*3.0*t*(1.0-t)*(1.0-t) + p3.x*3.0*t*t*(1.0-t));
        y = (int)Math.round(
        		p2.y*3.0*t*(1.0-t)*(1.0-t) + p3.y*3.0*t*t*(1.0-t));

        return new Point(x,y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		paint = true;
		Point c = e.getPoint();
		d = c;
        p.add(c);
        controlPoints = p.toArray(new Point[0]);
        repaint();
        System.out.println(controlPoints.length);
      
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println(p.get(p.size()-1));
		//paint = false;
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}


class drawSpline_frame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public drawSpline_frame(){
		setSize(500,500);
		setTitle("Spline");
		window window  = new window();
		Container cp =  this.getContentPane();
	    cp.add(window, BorderLayout.CENTER );
	    window.addMouseListener(window);

	}


}

class drawSpline{
	public static void main(String[] args) {
		JFrame frame = new drawSpline_frame();
		frame.setVisible(true);
		

		}
	}

