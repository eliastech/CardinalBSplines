
<HTML>
<HEAD>
<TITLE>bezier.java</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<PRE WIDTH="80"><FONT COLOR="#B22222">/*
bezier.java         by Gengbin Zheng
*/</FONT>

<FONT COLOR="#228B22">import java.awt.*;</FONT>
<FONT COLOR="#228B22">import java.applet.*;</FONT>

<STRONG><FONT COLOR="#4169E1">public class bezier extends Applet </FONT></STRONG>{
   Button draw1Button, draw2Button, modifyButton, deleteButton, clearButton;
   myCanvas canvas;
   TextField statusBar;

<STRONG><FONT COLOR="#4169E1">   public void init()</FONT></STRONG> {
      GridBagLayout layout = new GridBagLayout();
      setLayout(layout);

      GridBagConstraints constraints = new GridBagConstraints();

      draw1Button = new Button(<FONT COLOR="#666666">"Draw Bezier"</FONT>);
      draw2Button = new Button(<FONT COLOR="#666666">"Draw B-Spline"</FONT>);
      modifyButton = new Button(<FONT COLOR="#666666">"Modify"</FONT>);
      deleteButton = new Button(<FONT COLOR="#666666">"Delete curve"</FONT>);
      clearButton = new Button(<FONT COLOR="#666666">"Clear All"</FONT>);

      constraints.fill = GridBagConstraints.BOTH;
      constraints.weightx = 1;
      layout.setConstraints(draw1Button, constraints);
      add(draw1Button);

      layout.setConstraints(draw2Button, constraints);
      add(draw2Button);

      layout.setConstraints(modifyButton, constraints);
      add(modifyButton);

      constraints.gridwidth = GridBagConstraints.RELATIVE;
      layout.setConstraints(deleteButton, constraints);
      add(deleteButton);

      constraints.gridwidth = GridBagConstraints.REMAINDER;
      layout.setConstraints(clearButton, constraints);
      add(clearButton);

      canvas = new myCanvas();
      constraints.weighty = 1;
      layout.setConstraints(canvas, constraints);
      add(canvas);

      statusBar = new TextField(<FONT COLOR="#666666">"Draw Bezier: click to add a point, double click to finish drawing"</FONT>, 45);
      statusBar.setEditable(false);

      constraints.weighty = 0;
      layout.setConstraints(statusBar, constraints);
      add(statusBar);

      resize(550,450);              <FONT COLOR="#B22222">//Set window size</FONT>
   }

<STRONG><FONT COLOR="#4169E1">    public boolean action(Event evt, Object arg)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (evt.target instanceof Button)
      {
	HandleButtons(arg);
      }
      <FONT COLOR="#4169E1">return</FONT> true; 
    }

<STRONG><FONT COLOR="#4169E1">    protected void HandleButtons(Object label)</FONT></STRONG>
    {
      String helpMsg;

      <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Clear All"</FONT>)
	helpMsg = <FONT COLOR="#666666">"All curves are cleared."</FONT>;
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Draw Bezier"</FONT>)
	helpMsg = <FONT COLOR="#666666">"Draw Bezier: click to add a point, double click to finish drawing"</FONT>; 
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Draw B-Spline"</FONT>)
	helpMsg = <FONT COLOR="#666666">"Draw B-Spline: click to add a point, double click to finish drawing."</FONT>;
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Modify"</FONT>)
	helpMsg = <FONT COLOR="#666666">"Modify: select a control point, drag mouse to modify and release to finish."</FONT>;
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Delete curve"</FONT>)
	helpMsg = <FONT COLOR="#666666">"Delete: select a curve, click to delete."</FONT>;
      <FONT COLOR="#4169E1">else</FONT>
	helpMsg = <FONT COLOR="#666666">""</FONT>;

      statusBar.setText(helpMsg);

      canvas.HandleButtons(label);
    }
}

<STRONG><FONT COLOR="#4169E1">class myCanvas extends Canvas </FONT></STRONG>{
  PointList pts[];
  int nline;
  int curObj;
  boolean drawing;  
  int action;
  final int DRAW_BEZIER=1, DRAW_BSPLINE=2, MODIFY=3, DELETE=4;
  ErrorFrame  errDlg;
  <FONT COLOR="#B22222">// double buffering</FONT>
  Image img = null;
  Graphics backg;

<STRONG><FONT COLOR="#4169E1">    public myCanvas()</FONT></STRONG> {
      pts = new PointList[200];
      nline = -1;
      drawing = false;
      action = DRAW_BEZIER;

      errDlg = new ErrorFrame(<FONT COLOR="#666666">" Too many points!"</FONT>);
    }

    void setcursor(boolean working)
    {
      Cursor curs;
      <FONT COLOR="#4169E1">if</FONT> (working) 
	 curs = new Cursor(Cursor.HAND_CURSOR);
      <FONT COLOR="#4169E1">else</FONT>
         curs = new Cursor(Cursor.DEFAULT_CURSOR);
      setCursor(curs);
    }

<STRONG><FONT COLOR="#4169E1">    public boolean mouseUp(Event evt, int x, int y)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
       <FONT COLOR="#4169E1">if</FONT> (drawing) {
	  <FONT COLOR="#4169E1">if</FONT> (!pts[nline].addPoint(x,y))
	  {
		<FONT COLOR="#4169E1">if</FONT> (!errDlg.isShowing()) errDlg.show();
		drawing = false;
		nline --;
		setcursor(drawing);
	  }
       }
       repaint();
      }
      <FONT COLOR="#4169E1">if</FONT> (action == MODIFY)
      {
        <FONT COLOR="#4169E1">if</FONT> (drawing) 
	{
	   drawing = false;
	   setcursor(drawing);
        }
      }
      <FONT COLOR="#4169E1">if</FONT> (action == DELETE)
      {
	<FONT COLOR="#4169E1">if</FONT> (curObj != -1)
	{
	   <FONT COLOR="#4169E1">for</FONT> (int i=curObj; i&lt; nline; i++) pts[i] = pts[i+1];
	   nline--;
	   repaint();
	}
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

<STRONG><FONT COLOR="#4169E1">    public boolean mouseDown(Event evt, int x, int y)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
	<FONT COLOR="#4169E1">if</FONT> (drawing == false)
	{
	  nline ++;
	  <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BEZIER) pts[nline] = new bezierLine();
	  <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BSPLINE) pts[nline] = new bspline();
          pts[nline].addPoint(x,y);
          drawing = true;
	  setcursor(drawing);
        }
	<FONT COLOR="#4169E1">else</FONT> {
          <FONT COLOR="#4169E1">if</FONT> (evt.clickCount == 2) {
	    <FONT COLOR="#4169E1">if</FONT> (!pts[nline].done())
	    {
		<FONT COLOR="#4169E1">if</FONT> (!errDlg.isShowing()) errDlg.show();
		nline --;
	    }
	    drawing = false;
	    setcursor(drawing);
          }
	}
      }
      <FONT COLOR="#4169E1">if</FONT> (action == MODIFY)
      {
	<FONT COLOR="#4169E1">if</FONT> (curObj != -1) {
	   drawing = true;
	   setcursor(drawing);
        }
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

<STRONG><FONT COLOR="#4169E1">    public boolean mouseMove(Event evt, int x, int y)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
	<FONT COLOR="#4169E1">if</FONT> (drawing)
	{
	 pts[nline].changePoint(x,y);
	 repaint();
        }
      }
      <FONT COLOR="#4169E1">if</FONT> (action == MODIFY || action == DELETE)
      {
	<FONT COLOR="#4169E1">if</FONT> (drawing == false)
	{
	  int oldObj = curObj;
	  curObj = -1;
	  <FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;=nline; i++)
	  {
	    <FONT COLOR="#4169E1">if</FONT> (pts[i].inRegion(x,y) != -1) 
	    {
	      curObj = i;
	      <FONT COLOR="#4169E1">break</FONT>; 
	    }
          }
	  <FONT COLOR="#4169E1">if</FONT> (oldObj != curObj) repaint();
	}
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

<STRONG><FONT COLOR="#4169E1">    public boolean mouseDrag(Event evt, int x, int y)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (action == MODIFY)
      {
	<FONT COLOR="#4169E1">if</FONT> (drawing == true)  {
	  pts[curObj].changeModPoint(x,y);
	  <FONT COLOR="#4169E1">if</FONT> (!pts[curObj].createFinal())
	  {
		<FONT COLOR="#4169E1">if</FONT> (!errDlg.isShowing()) errDlg.show();
		nline --;
	  }
	  repaint();
	}
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

<STRONG><FONT COLOR="#4169E1">    public void HandleButtons(Object label)</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (drawing)
      {
        drawing = false;
	setcursor(drawing);
      }
      <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Clear All"</FONT>)
      {
	nline = -1;
	repaint();
	<FONT COLOR="#4169E1">return</FONT>;
      }

      <FONT COLOR="#4169E1">if</FONT> (action == DRAW_BEZIER || action == DRAW_BSPLINE)
      {
	<FONT COLOR="#4169E1">if</FONT> (drawing) pts[nline].done();
      }
      <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Draw Bezier"</FONT>)
      {
	action = DRAW_BEZIER;
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;=nline; i++)
	  pts[i].setShow(false);
        repaint();
      }
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Draw B-Spline"</FONT>)
      {
	action = DRAW_BSPLINE;
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;=nline; i++)
	  pts[i].setShow(false);
        repaint();
      }
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Modify"</FONT>)
      {
	action = MODIFY;
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;=nline; i++)
	  pts[i].setShow(true);
        repaint();
      }
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (label == <FONT COLOR="#666666">"Delete curve"</FONT>)
      {
	action = DELETE;
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;=nline; i++)
	  pts[i].setShow(true);
        repaint();
      }
    }

<STRONG><FONT COLOR="#4169E1">    public void paint(Graphics g)</FONT></STRONG> 
    {
      update(g);
    }

<STRONG><FONT COLOR="#4169E1">    public void update(Graphics g)</FONT></STRONG> {    <FONT COLOR="#B22222">//Don't bother</FONT>
      int i,n;
      Dimension d=size();

      <FONT COLOR="#4169E1">if</FONT> (img == null)
      {
      img = createImage(d.width, d.height);
      backg = img.getGraphics();
      }

      backg.setColor(new Color(255,255,255));    <FONT COLOR="#B22222">//Set color for background</FONT>
      backg.fillRect(0,0, d.width, d.height);  <FONT COLOR="#B22222">//Draw Backround</FONT>

      <FONT COLOR="#B22222">// draw border</FONT>
      backg.setColor(new Color(0,0,0));
      backg.drawRect(1,1,d.width-3,d.height-3);

      <FONT COLOR="#4169E1">for</FONT> (n=0; n &lt;= nline; n++)
	pts[n].draw(backg);

      g.drawImage(img, 0, 0, this);
    }
}

<STRONG><FONT COLOR="#4169E1">class ErrorFrame extends Frame</FONT></STRONG>
{
    Label label;
    Button button;
    String errMsg;

    ErrorFrame(String msg)
    {
	super(<FONT COLOR="#666666">"Error!"</FONT>);
	errMsg = msg;

	BorderLayout layout = new BorderLayout();
	setLayout(layout);

	label = new Label(errMsg);
	add(<FONT COLOR="#666666">"North"</FONT>, label);

	button = new Button(<FONT COLOR="#666666">"Ok"</FONT>);
	add(<FONT COLOR="#666666">"South"</FONT>, button);

	resize(200,100);
    }

<STRONG><FONT COLOR="#4169E1">    public boolean action(Event evt, Object arg)</FONT></STRONG>
    {
	<FONT COLOR="#4169E1">if</FONT> (arg == <FONT COLOR="#666666">"Ok"</FONT>)
		dispose();
        <FONT COLOR="#4169E1">return</FONT> true;
    }
}

<STRONG><FONT COLOR="#4169E1">class PointList </FONT></STRONG>{
    Point pt[];
    int num;
    int x,y,z; 	<FONT COLOR="#B22222">// color</FONT>
    boolean showLine;
    int curPt;
    final int MAXCNTL = 50;
    final int range = 5;

    PointList() {
	num = 0;
	curPt = -1;
	pt = new Point[MAXCNTL];
	x = (int)(Math.random() * 255);
	y = (int)(Math.random() * 255);
	z = (int)(Math.random() * 255);
    }

    boolean addPoint(int x, int y)
    {
	<FONT COLOR="#4169E1">if</FONT> (num == MAXCNTL) <FONT COLOR="#4169E1">return</FONT> false;
	pt[num] = new Point(x,y);
	num++;
	<FONT COLOR="#4169E1">return</FONT> true;
    }

    void changePoint(int x, int y)
    {
	pt[num-1].x = x;
	pt[num-1].y = y;
    }

    void changeModPoint(int x, int y)
    {
	pt[curPt].x = x;
	pt[curPt].y = y;
    }

    boolean createFinal()
    {
      <FONT COLOR="#4169E1">return</FONT> true;
    }

    boolean done()
    {
      <FONT COLOR="#4169E1">return</FONT> true;
    }

    void setShow(boolean show)
    {
      showLine = show;
    }

    int inRegion(int x, int y)
    {
       int i;
       <FONT COLOR="#4169E1">for</FONT> (i=0; i&lt;num; i++) 
	 <FONT COLOR="#4169E1">if</FONT> (Math.abs(pt[i].x-x) &lt; range &amp;&amp; Math.abs(pt[i].y-y) &lt; range)
	 {
	   curPt = i;
	   <FONT COLOR="#4169E1">return</FONT> i;
         }
       curPt = -1;
       <FONT COLOR="#4169E1">return</FONT> -1;
    }

    void draw(Graphics g)
    {
      int i;
      int l = 3;
      <FONT COLOR="#4169E1">for</FONT> (i=0; i&lt; num-1; i++)
      {
	g.drawLine(pt[i].x-l, pt[i].y, pt[i].x+l, pt[i].y);
	g.drawLine(pt[i].x, pt[i].y-l, pt[i].x, pt[i].y+l);
        drawDashLine(g, pt[i].x,pt[i].y,pt[i+1].x,pt[i+1].y);   <FONT COLOR="#B22222">//Draw segment</FONT>
      }
      g.drawLine(pt[i].x-l, pt[i].y, pt[i].x+l, pt[i].y);
      g.drawLine(pt[i].x, pt[i].y-l, pt[i].x, pt[i].y+l);
    }

    <FONT COLOR="#B22222">// draw dash lines</FONT>
<STRONG><FONT COLOR="#4169E1">    protected void drawDashLine(Graphics g, int x1, int y1, int x2, int y2)</FONT></STRONG>
    {
      final float seg = 8;
      double x, y;

      <FONT COLOR="#4169E1">if</FONT> (x1 == x2)
      {
	  <FONT COLOR="#4169E1">if</FONT> (y1 &gt; y2)
	  {
            int tmp = y1;
	    y1 = y2;
	    y2 = tmp;
	  }
	  y = (double)y1;
	  <FONT COLOR="#4169E1">while</FONT> (y &lt; y2)
	  {
	     double y0 = Math.min(y+seg, (double)y2);
             g.drawLine(x1, (int)y, x2, (int)y0);
	     y = y0 + seg;
	  }
	  <FONT COLOR="#4169E1">return</FONT>;
      }
      <FONT COLOR="#4169E1">else</FONT> <FONT COLOR="#4169E1">if</FONT> (x1 &gt; x2) 
      {
	int tmp = x1;
	x1 = x2;
	x2 = tmp;
        tmp = y1;
	y1 = y2;
	y2 = tmp;
      }
      double ratio = 1.0*(y2-y1)/(x2-x1);
      double ang = Math.atan(ratio);
      double xinc = seg * Math.cos(ang);
      double yinc = seg * Math.sin(ang);
      x = (double)x1;
      y = (double)y1;

      <FONT COLOR="#4169E1">while</FONT> ( x &lt;= x2 )
      {
	  double x0 = x + xinc;
	  double y0 = y + yinc;
	  <FONT COLOR="#4169E1">if</FONT> (x0 &gt; x2) {
	     x0 = x2;
	     y0  = y + ratio*(x2-x);
	  }
          g.drawLine((int)x, (int)y, (int)x0, (int)y0);
	  x = x0 + xinc;
	  y = y0 + yinc;
      }
    }
}


<STRONG><FONT COLOR="#4169E1">class bezierLine extends PointList </FONT></STRONG>{
    Point bpt[];
    int bnum;
    boolean ready;
    final int MAXPOINT = 1800;
    final int ENOUGH = 2;
    final int RECURSION = 900;
    int nPointAlloc;
    int enough;		<FONT COLOR="#B22222">// control how well we draw the curve.</FONT>
    int nRecur;		<FONT COLOR="#B22222">// counter of number of recursion</FONT>
    Point buffer[][];
    int nBuf, nBufAlloc;

    bezierLine() {
        bpt = new Point[MAXPOINT];
	nPointAlloc = MAXPOINT;
	bnum = 0;
	enough = ENOUGH;
	showLine = true;
	ready = false;
	buffer = null;
    }

<STRONG><FONT COLOR="#4169E1">    protected int distance(Point p0,Point p1,Point p2)</FONT></STRONG>
    {
        int a,b,y1,x1,d1,d2;
     
	<FONT COLOR="#4169E1">if</FONT>(p1.x==p2.x &amp;&amp; p1.y==p2.y) <FONT COLOR="#4169E1">return</FONT> Math.min(Math.abs(p0.x-p1.x),Math.abs(p0.y-p1.y));
        a=p2.x-p1.x;    b=p2.y-p1.y;
	y1=b*(p0.x-p1.x)+a*p1.y;
	x1=a*(p0.y-p1.y)+b*p1.x;
	d1=Math.abs(y1-a*p0.y);
	d2=Math.abs(x1-b*p0.x);
	<FONT COLOR="#4169E1">if</FONT> (a==0) <FONT COLOR="#4169E1">return</FONT> Math.abs(d2/b);
	<FONT COLOR="#4169E1">if</FONT> (b==0) <FONT COLOR="#4169E1">return</FONT> Math.abs(d1/a);
	<FONT COLOR="#4169E1">return</FONT> Math.min(Math.abs(d1/a),Math.abs(d2/b));
    }

<STRONG><FONT COLOR="#4169E1">    protected void curve_split(Point p[],Point q[],Point r[],int num)</FONT></STRONG>
    {
      int i,j;
     
<FONT COLOR="#B22222">//      for (i=0;i&lt;num;i++) q[i] = new Point(p[i]);</FONT>
      <FONT COLOR="#4169E1">for</FONT> (i=0;i&lt;num;i++) q[i].copy(p[i]);
      <FONT COLOR="#4169E1">for</FONT> (i=1;i&lt;=num-1;i++) {
<FONT COLOR="#B22222">//         r[num-i] = new Point(q[num-1]);</FONT>
         r[num-i].copy(q[num-1]);
	 <FONT COLOR="#4169E1">for</FONT> (j=num-1;j&gt;=i;j--) {
<FONT COLOR="#B22222">//	    q[j] = new Point((q[j-1].x+q[j].x)/2, (q[j-1].y+q[j].y)/2);</FONT>
	    q[j].x = (q[j-1].x+q[j].x)/2;
	    q[j].y = (q[j-1].y+q[j].y)/2;
	 }
      }
<FONT COLOR="#B22222">//      r[0] = new Point(q[num-1]);</FONT>
      r[0].copy(q[num-1]);
    }

    <FONT COLOR="#B22222">// reuse buffer</FONT>
<STRONG><FONT COLOR="#4169E1">    private Point get_buf(int num)</FONT></STRONG>[]
    {
      Point b[];
      <FONT COLOR="#4169E1">if</FONT> (buffer == null)
      {
        buffer = new Point[500][num];
	nBufAlloc = 500;
	nBuf = 0;
      }
      <FONT COLOR="#4169E1">if</FONT> (nBuf == 0)
      {
	b = new Point[num];
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt; num; i++) b[i] = new Point();
	<FONT COLOR="#4169E1">return</FONT> b;
      }
      <FONT COLOR="#4169E1">else</FONT> {
	nBuf --;
	b = buffer[nBuf];
        <FONT COLOR="#4169E1">return</FONT> b;
      }
    }

<STRONG><FONT COLOR="#4169E1">    private void put_buf(Point b[])</FONT></STRONG>
    {
      <FONT COLOR="#4169E1">if</FONT> (nBuf &gt;= nBufAlloc)
      {
        Point newBuf[][] = new Point[nBufAlloc + 500][num];
	<FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt;nBuf; i++) newBuf[i] = buffer[i];
	nBufAlloc += 500;
	buffer = newBuf;
      }
      buffer[nBuf] = b;
      nBuf++;
    }

<STRONG><FONT COLOR="#4169E1">    protected boolean bezier_generation(Point pt[], int num, Point result[], int n[])</FONT></STRONG>
    {
      Point   qt[],rt[];	<FONT COLOR="#B22222">// for split</FONT>
      int d[],i,max;
      
      nRecur++;
      <FONT COLOR="#4169E1">if</FONT> (nRecur &gt; RECURSION) <FONT COLOR="#4169E1">return</FONT> false;

      d = new int[MAXCNTL];
      <FONT COLOR="#4169E1">for</FONT> (i=1;i&lt;num-1;i++) d[i]=distance(pt[i],pt[0],pt[num-1]);
      max=d[1];
      <FONT COLOR="#4169E1">for</FONT> (i=2;i&lt;num-1;i++) <FONT COLOR="#4169E1">if</FONT> (d[i]&gt;max) max=d[i];
      <FONT COLOR="#4169E1">if</FONT> (max &lt;= enough || nRecur &gt; RECURSION) {
	   <FONT COLOR="#4169E1">if</FONT> (n[0]==0) {
	      <FONT COLOR="#4169E1">if</FONT> (bnum &gt; 0) 
		 result[0].copy(pt[0]);
	      <FONT COLOR="#4169E1">else</FONT>
	         result[0] = new Point(pt[0]);
	      n[0]=1;
	   }
	   <FONT COLOR="#B22222">//reuse</FONT>
	   <FONT COLOR="#4169E1">if</FONT> (bnum &gt; n[0])
		 result[n[0]].copy(pt[num-1]);
	   <FONT COLOR="#4169E1">else</FONT>
	         result[n[0]] = new Point(pt[num-1]);
	   n[0]++;
	   <FONT COLOR="#4169E1">if</FONT> (n[0] == MAXPOINT-1) <FONT COLOR="#4169E1">return</FONT> false;
      }
      <FONT COLOR="#4169E1">else</FONT> {
<FONT COLOR="#B22222">//	   qt = new Point[num];</FONT>
<FONT COLOR="#B22222">//	   rt = new Point[num];</FONT>
           qt = get_buf(num);
           rt = get_buf(num);
	   curve_split(pt,qt,rt,num);
	   <FONT COLOR="#4169E1">if</FONT> (!bezier_generation(qt,num,result,n)) <FONT COLOR="#4169E1">return</FONT> false;
	   put_buf(qt);
	   <FONT COLOR="#4169E1">if</FONT> (!bezier_generation(rt,num,result,n)) <FONT COLOR="#4169E1">return</FONT> false;
	   put_buf(rt);
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

<STRONG><FONT COLOR="#4169E1">    public boolean try_bezier_generation(Point pt[], int num, Point result[], int n[])</FONT></STRONG>
    {
       int oldN = n[0];

       <FONT COLOR="#4169E1">if</FONT> (enough == ENOUGH &amp;&amp; num &gt; 6) enough += 3;
<FONT COLOR="#B22222">//       if (enough &gt; ENOUGH) enough -= 5;</FONT>
       nRecur = 0;
       <FONT COLOR="#B22222">// in case of recursion stack overflow, relax "enough" and keep trying</FONT>
       <FONT COLOR="#4169E1">while</FONT> (!bezier_generation(pt, num, bpt, n))
       {
          n[0] = oldN;
	  enough += 5;
          nRecur = 0;
       }
       <FONT COLOR="#4169E1">return</FONT> true;
    }

    boolean createFinal()
    {
       int n[];
       n = new int[1];
       <FONT COLOR="#4169E1">if</FONT> (!try_bezier_generation(pt, num, bpt, n)) 
       {
	  bnum = 0;
	  <FONT COLOR="#4169E1">return</FONT> false;
       }
       <FONT COLOR="#4169E1">else</FONT> {
          bnum = n[0];
          <FONT COLOR="#4169E1">return</FONT> true;
       }
    }

    boolean done()
    {
       num --;
       showLine = false;
       ready = true;
       <FONT COLOR="#4169E1">return</FONT> createFinal();
    }

    void draw(Graphics g)
    {
      g.setColor(new Color(x,y,z));
      <FONT COLOR="#4169E1">if</FONT> (showLine)
      {
	super.draw(g);
	<FONT COLOR="#4169E1">if</FONT> (curPt != -1)
	  g.drawRect(pt[curPt].x-range, pt[curPt].y-range, 2*range+1,2*range+1);
      }

      <FONT COLOR="#4169E1">if</FONT> (ready)
      <FONT COLOR="#4169E1">for</FONT> (int i=0; i&lt; bnum-1; i++)
      {
        g.drawLine(bpt[i].x,bpt[i].y,bpt[i+1].x,bpt[i+1].y);   
      }
    }
}

<STRONG><FONT COLOR="#4169E1">class bspline extends bezierLine </FONT></STRONG>
{
<STRONG><FONT COLOR="#4169E1">    protected void bspline_to_Bezier(int j, Point p[], Point v[])</FONT></STRONG>
    {
      int h,i;
      double tmp,x1,x2;
      
      <FONT COLOR="#4169E1">for</FONT> (h=0;h&lt;=1;h++) {
         <FONT COLOR="#4169E1">for</FONT> (i=0;i&lt;=1;i++)  {
	     tmp=1.0*((j+h)-(j-2+h+i))*1.0/((j+1+i+h)-(j-2+h+i));   <FONT COLOR="#B22222">//  (2-i)/3</FONT>
	     x1=p[j-2+i+h].x;
	     x2=p[j-2+i+h-1].x;
	     v[2*h+i].x=(int)(tmp*x1+(1.0-tmp)*x2);
	     x1=p[j-2+i+h].y;
	     x2=p[j-2+i+h-1].y;
	     v[2*h+i].y=(int)(tmp*x1+(1.0-tmp)*x2);
	 }
	 tmp=1.0*((j+h)-(j-1+h))/((j+1+h)-(j-1+h));    	<FONT COLOR="#B22222">// 1/2</FONT>
	 x1=v[1+2*h].x;
	 x2=v[2*h].x;
	 v[3*h].x=(int)(tmp*x1+(1.0-tmp)*x2);
	 x1=v[1+2*h].y;
	 x2=v[2*h].y;
	 v[3*h].y=(int)(tmp*x1+(1-tmp)*x2);
      }
    }

<STRONG><FONT COLOR="#4169E1">    protected boolean bspline_generation(Point pt[],int n,Point result[],int num[])</FONT></STRONG>
    {
      Point v[];
      int i,j;
      
      v = new Point[4];
      <FONT COLOR="#4169E1">for</FONT> (i=0; i&lt;4; i++) v[i] = new Point();
      <FONT COLOR="#4169E1">for</FONT> (j=3;j&lt;n;j++) {
	 bspline_to_Bezier(j,pt,v);
	 <FONT COLOR="#4169E1">if</FONT> (num[0] &gt; 0) num[0]=num[0]-1;
	 <FONT COLOR="#4169E1">if</FONT> (!try_bezier_generation(v,4,result,num)) <FONT COLOR="#4169E1">return</FONT> false;
      }
      <FONT COLOR="#4169E1">return</FONT> true;
    }

    boolean createFinal()
    {
       int n[];
       n = new int[1];
       n[0] = 0;
       <FONT COLOR="#4169E1">if</FONT> (bspline_generation(pt, num, bpt, n))
       {
          bnum = n[0];
	  <FONT COLOR="#4169E1">return</FONT> true;
       }
       <FONT COLOR="#4169E1">else</FONT> {
          bnum = 0;
          <FONT COLOR="#4169E1">return</FONT> false;
       }
    }

}

<STRONG><FONT COLOR="#4169E1">class Point </FONT></STRONG>
{
    int x,y;

    Point(Point p)
    {
       x = p.x;
       y = p.y;
    }
    Point(int _x, int _y)
    {
       x = _x;
       y = _y;
    }
    Point()
    {
       x = 0;
       y = 0;
    }
    void copy(Point p)
    {
       x = p.x;
       y = p.y;
    }
}

</PRE>
</BODY>

</HTML>
