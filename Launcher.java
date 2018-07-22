package src.work2014.java.all;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

import org.w3c.dom.css.Rect;

class Oving5_Panel extends JPanel implements MouseListener, ActionListener
{  
	boolean paint = false, edit = false;
    int figur = 1;
    Point p0,p1;
	Point controlPoints[] = new Point[1];
    ArrayList<Point> points = new ArrayList<Point>();
    Point curvePoints[]= new Point[100];
    int a=0; 
    Graphics g;
    
    int np = 0, centerX, centerY;
    float rWidth = 10.0F, rHeight = 7.5F, eps = rWidth/100F, pixelSize;
 
   Oving5_Panel(){
    addMouseListener(this);
    }
  
  
	public void paintComponent(Graphics g){
		//initgr();
		super.paintComponent(g);
		 g.drawString("Kubiske punkter: "+points.size() ,25,25);
		
		 // draw the bezier spline + polygon
		int np = points.size();
		if (np == 0)
			// draw nothing
			return;
		
		
		controlPoints = points.toArray(new Point[0]); 
		
		bspline(g, controlPoints);
		
		Point p1, p0 = points.get(0);
		
		g.setColor(Color.red);
		g.drawOval(p0.x-2, p0.y-2, +4, +4);
		g.setColor(Color.green);

		for (int i=1 ; i < np; i++) {
			p0 = (Point)points.get(i-1);
			p1 = (Point)points.get(i);
			
			g.setColor(Color.red);
			g.drawOval(p1.x-2, p1.y-2, +4, +4);
			
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(p0.x, p0.y, p1.x, p1.y);
			}
		
		drawBezier(g);
		
			this.requestFocus();
		}
	
		
	
	   void initgr()  
	   {  Dimension d = getSize();
	      int maxX = d.width - 1, maxY = d.height - 1;
	      pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
	      centerX = maxX/2; centerY = maxY/2;
	   }

	private void drawBezier(Graphics g) {
		
		if (points.size() < 4)
			return;
		int n = 99;
		double t, delta = 1.0/n;

		    p0 = (Point)points.get(0);
		for (int i = 0 ; i <=n ; i++) {
			
			t=i*delta;
			controlPoints = points.toArray(new Point[0]); 
			
			p1 = evalBez(controlPoints,t);
		
			
			if (figur == 1){
				g.setColor(Color.blue);
				g.drawLine(p0.x, p0.y, p1.x, p1.y);
				p0 = p1;
				}
			
		}
		
	}

    int iX(float x){return Math.round(centerX + x/pixelSize);}
    int iY(float y){return Math.round(centerY - y/pixelSize);}
    float fx(int X){return (X - centerX) * pixelSize;}
    float fy(int Y){return (centerY - Y) * pixelSize;} 
	
	   void bspline(Graphics g, Point[] P)
	   {  
		  int m = 50, n = P.length;
	      float xA, yA, xB, yB, xC, yC, xD, yD,
	         a0, a1, a2, a3, b0, b1, b2, b3, x=0, y=0, x0, y0;
	     // boolean first = true;
	      for (int i=1; i<n-2; i++)
	      {  
	    	 xA=P[i-1].x; xB=P[i].x; xC=P[i+1].x; xD=P[i+2].x;
	         yA=P[i-1].y; yB=P[i].y; yC=P[i+1].y; yD=P[i+2].y;
	         a3=(-xA+3*(xB-xC)+xD)/6; b3=(-yA+3*(yB-yC)+yD)/6;
	         a2=(xA-2*xB+xC)/2;       b2=(yA-2*yB+yC)/2;
	         a1=(xC-xA)/2;            b1=(yC-yA)/2;
	         a0=(xA+4*xB+xC)/6;       b0=(yA+4*yB+yC)/6;
	         for (int j=0; j<=m; j++)
	         {  
	        	x0 = x; y0 = y;
	            float t = (float)j/(float)m;
	            x = ((a3*t+a2)*t+a1)*t+a0;
	            y = ((b3*t+b2)*t+b1)*t+b0;

	            g.drawLine(iX(x0), iY(y0), iX(x), iY(y));
	         }
	      }
	   }
	
	
	public Point evalBez(Point arr[],double t) {
		for (int iter = arr.length ; iter > 3 ; iter-=3) {
			for (int i = 3 ; i < iter ; i++) {
					arr[i-3] = interpolateBez(arr[i-3],arr[i-2], arr[i-1], arr[i], t);
			
			}
		}
		return arr[0];
	}
	


	public Point interpolateBez(Point p0, Point p1, Point p2, Point p3,  double t) {
		double x, y;
		
		x = (1.0-t)*(1.0-t)*(1.0-t)*p0.x + 3.0*t*(1.0-t)*(1.0-t)*p1.x 
				+ 3.0*t*t*(1.0-t)*p2.x + t*t*t*p3.x;
		y = (1.0-t)*(1.0-t)*(1.0-t)*p0.y + 3.0*t*(1.0-t)*(1.0-t)*p1.y 
				+ 3.0*t*t*(1.0-t)*p2.y + t*t*t*p3.y;

		return new Point((int)(x),(int)(y));
	}
	
	


	@Override
	public void mouseClicked(MouseEvent arg0) {

	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		paint = true;
		fx(arg0.getX()); fy(arg0.getY());
		Point p = arg0.getPoint();
		points.add(p);
		a++;

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		System.out.println(points.size());
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "B-Spline"){
				figur = 2;
				System.out.println("act preformed");
				
			}
			if(e.getActionCommand() == "Bezier"){
				figur = 1;
				repaint();
			 } 
		    if(e.getActionCommand() == "Clear"){
		    	points.clear();
		    	g.dispose();
		    	repaint();
		    }
		    if (e.getActionCommand() == "Rediger"){
		    	paint = true;
		    	edit = true;
		    	repaint();
		    }
		/**	
		 * @return 
		**/	
		
	}

	

	private void clearPoints() {
		for (int z = 0; z<5; z++ ){
			controlPoints[z] = null;
			curvePoints[z] = null;
			paint = false;
			repaint();
		}
		
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// point
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}





class Oving5_Frame extends JFrame
{  public Oving5_Frame()
   {  setTitle("Kubisk B-SPLINE");
      setSize(600, 600);

       Oving5_Panel panel = new Oving5_Panel(); 
      Container contentPane = getContentPane();
      contentPane.add(new Oving5_Panel());
      contentPane.add(b(), BorderLayout.NORTH);
      //addMouseListener(panel);
   }


private Component b() {
	    Oving5_Panel a = new Oving5_Panel();
	    JMenuBar menuBar = new JMenuBar();
		JMenuItem a1 = new JMenuItem("B-Spline");
	    JMenuItem a2 = new JMenuItem("Bezier");
	    JMenuItem a3 = new JMenuItem("Draw");
	    JMenuItem a4 = new JMenuItem("Clear");
	    menuBar.add(a1);
	   // a1.setEnabled(false);
	    menuBar.add(a2);
	    menuBar.add(a3);
	    menuBar.add(a4);
	    a1.addActionListener(a);
		a3.addActionListener(a);
		a2.addActionListener(a);
		a4.addActionListener(a);
		return menuBar;
	}

}

public class Oving5_launch
{  public static void main(String[] args)
   {  
	  JFrame frame = new Oving5_Frame();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }



}
