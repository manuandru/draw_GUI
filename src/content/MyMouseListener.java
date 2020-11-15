package content;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.NoSuchElementException;

import panel.Line;

public class MyMouseListener implements MouseListener, MouseMotionListener{

	private final GuiHandler gui;
	private Line lineToTraslate;
	private Line lineToColor;
	private boolean gotLineToMove = false;
	private boolean gotLineToColor = false;
	private boolean isTraslating = false;
	public MyMouseListener(GuiHandler gui) {
		this.gui = gui;
	}
	
	public void mouseDragged(MouseEvent e) { 	
    	this.gui.getpCenterPanel().addPoint(e.getX(), e.getY());
    	this.gui.getpCenterPanel().setPenSize(this.gui.getPenSize());
    	this.gui.getpCenterPanel().setColor(this.gui.getCurrentColor());
    	this.gui.getpCenterPanel().repaint();
    }
	
	public void mouseReleased(MouseEvent e) {
    	this.gui.getpCenterPanel().createLineObj();
    	this.gui.getpCenterPanel().repaint(); 
    }
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(this.gotLineToMove) {
			this.gui.getpCenterPanel().traslateLine(this.lineToTraslate, new Point(e.getX(), e.getY()), isTraslating);
			this.gui.getpCenterPanel().repaint();
			this.isTraslating = true;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3 && !this.gotLineToMove) {
			try {
				this.lineToTraslate = this.gui.getpCenterPanel().getLineAtCoordinates(new Point((int)e.getX(), (int)e.getY()));
				System.out.println(lineToTraslate.toString() + this.gotLineToMove);	
				this.gotLineToMove = true;
				this.gui.getpCenterPanel().changeLinesColor(this.lineToTraslate, Color.GRAY);
			}catch(NoSuchElementException exc){
				System.out.println("Didn't find a line here");
			}
		}else if(e.getButton() == MouseEvent.BUTTON3 && this.gotLineToMove) {	
				this.gui.getpCenterPanel().changeLinesColor(this.lineToTraslate, this.lineToTraslate.getLineLastColor());
				this.gotLineToMove = false; //I dropped the object		
				this.gui.getpCenterPanel().resetVector(); //I reset the vector used by the Panel to traslate the selected line
				this.isTraslating = false; //The object is not traslating anymore
		}else if(e.getButton() == MouseEvent.BUTTON2 && !this.gotLineToColor) {
			try {
				this.lineToColor = this.gui.getpCenterPanel().getLineAtCoordinates(new Point((int)e.getX(), (int)e.getY()));
				this.gotLineToColor = true;
				this.gui.getpCenterPanel().changeLinesColor(this.lineToColor, this.gui.getCurrentColor());
				System.out.println(this.lineToColor.toString());
			}catch(NoSuchElementException exc){
				System.out.println("Didn't find a line here");
				this.gotLineToColor = false;
			}		
		}else if(e.getButton() == MouseEvent.BUTTON2 && this.gotLineToColor) {
			this.gotLineToColor = false;
		}
	}

	public void setLineToTraslate(Line lineToTraslate) {
		this.lineToTraslate = lineToTraslate;
	}
	

}
