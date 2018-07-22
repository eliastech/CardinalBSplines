package src.work2014.java.all;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class kubiskSpline extends Canvas implements ActionListener{

	
	  
	   ArrayList<Point> v = new ArrayList<Point>();
	   int np = 0, centerX, centerY;
	   float pixelSize;
	   boolean ready = false;
	   int spline = 2;
	   
	   kubiskSpline()
	   {  

		   addMouseListener(new MouseAdapter()
	      {  
			   
			public void mousePressed(MouseEvent evt)
	         {  
	    	  float x = fx((int)evt.getX()), y = fy((int)evt.getY());
	            
	    	  	if (ready) 
	            { 
	    	  	   v.clear();
	               np = 0;
	               ready = false;
	            }
	    	  	
	    	  	v.add(new Point((int)x,(int)y));
	            np++;
	            System.out.println("DIM: "+getSize());
	            System.out.println((int)x+" "+" "+(int)y);
	            repaint();
	         }
	      });

	   }

	   void initgr()  
	   {  
		  Dimension d = getSize();
	      int maxX = d.width - 1, maxY = d.height - 1;
	      pixelSize = Math.max(maxX, maxY);
	      centerX = maxX/2; centerY = maxY/2;
	   }

	   int iX(float x){return Math.round(centerX + x/pixelSize);}
	   int iY(float y){return Math.round(centerY - y/pixelSize);}
	   float fx(int X){return (X - centerX) * pixelSize;}
	   float fy(int Y){return (centerY - Y) * pixelSize;}  

	   void bspline(Graphics g, Point[] P)
	   {  
		   int m = 50, n = P.length;
		      float xA, yA, xB, yB, xC, yC, xD, yD, a0, a1, a2, a3, b0, b1, b2, b3, x=0, y=0, x0, y0;
		      boolean first = true;
		      
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
		            if (first) first = false; 
		            else 
		            //g.setColor(Color.black);
		            g.drawLine(iX(x0), iY(y0), iX(x), iY(y));
	         }
	      }
	   }
	   
	   public void paint(Graphics g)
	   {  
		 // super.paintComponent();
		  initgr();
		 
	      Point[] P = new Point[np];
	      v.toArray(P);
	      g.setColor(Color.DARK_GRAY);
	      g.drawString("Kubiske punkter: "+P.length ,25,25);
	     
	      if (!ready){  
	    	  for (int i=0; i<np; i++)
	         {  
	    		g.setColor(Color.red);
	            g.drawOval(iX(P[i].x)-2, iY(P[i].y)-2, 4, 4);
	            g.setColor(Color.LIGHT_GRAY);
	           if (i > 0)
	        	g.drawLine(iX(P[i-1].x), iY(P[i-1].y), iX(P[i].x), iY(P[i].y));   
	      }
	      g.setColor(Color.blue);
	      //if (np >= 4) bspline(g, P);
	      if (np >= 4) bezier(g, P);
	   }
		
	}

private void bezier(Graphics g, Point[] P) {
	int m = 50, n = P.length;
    float xA, yA, xB, yB, xC, yC, xD, yD; 
    double x=0, y=0, x0, y0;
    boolean first = true;
   

    for (int i=1; i<n-2; i+=3)
    { 
		 xA=P[i-1].x; xB=P[i].x; xC=P[i+1].x; xD=P[i+2].x;
		 yA=P[i-1].y; yB=P[i].y; yC=P[i+1].y; yD=P[i+2].y;
			
			
       for (int j=0; j<=m; j++){
    	 
    	 x0 = x; y0 = y;
    	 float t = (float)j/(float)m;
          x = (1.0-t)*(1.0-t)*(1.0-t)*xA + 3.0*t*(1.0-t)*(1.0-t)*xB 
				+ 3.0*t*t*(1.0-t)*xC + t*t*t*xD;
          y = (1.0-t)*(1.0-t)*(1.0-t)*yA + 3.0*t*(1.0-t)*(1.0-t)*yB 
				+ 3.0*t*t*(1.0-t)*yC + t*t*t*yD;
          
          
          if (first) first = false; 
          else 
          //g.setColor(Color.black);
        	  
          g.drawLine(iX((float)x0), iY((float)y0), iX((float)x), iY((float)y));
          x0 = x; y0 = y;
       
       		}
    	}
	}

public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand() == "B-Spline"){
				spline = 1;
				repaint();
				
				System.out.println("act preformed");
				
			}
			if(e.getActionCommand() == "Bezier"){
				spline = 2;
				repaint();
			 } 
		    if(e.getActionCommand() == "Clear"){
		    	ready = true;
		    	np = 0;
		    	
		    }
		    if (e.getActionCommand() == "Rediger"){
		    	
		    	repaint();
		    }
}
		 
public static void main(String[] args)
{  
   JFrame frame = new kubiskspline_jframe();
   //JFrame frame1 = new Oving5_Frame();
  // frame1.show(); 
   frame.setLocation(600, 100);
   frame.setVisible(true);
   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class kubiskspline_jframe extends JFrame
{  

	private static final long serialVersionUID = 1L;

	public kubiskspline_jframe(){  
		setTitle("Kubisk B-Spline");
	    setSize(600, 600);
	    menu m = new menu();
	    Container contentPane = getContentPane();
	    contentPane.add(new kubiskSpline());
	    contentPane.add(m, BorderLayout.NORTH);
	    //addMouseListener(panel);
	}

private class menu extends JMenuBar{
	menu(){
		kubiskSpline a = new kubiskSpline();
		JMenuItem a1 = new JMenuItem("B-Spline");
	    JMenuItem a2 = new JMenuItem("Bezier");
	    JMenuItem a3 = new JMenuItem("Draw");
	    JMenuItem a4 = new JMenuItem("Clear");
	    this.add(a1);this.add(a2);this.add(a3);this.add(a4);
	    a1.addActionListener(a);
		a3.addActionListener(a);
		a2.addActionListener(a);
		a4.addActionListener(a);
		}


	}


}
