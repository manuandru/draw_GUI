package panel;

import java.awt.*;

import javax.swing.*;

import content.Pair;

import java.util.*;

// Specializzazione ad-hoc per un JPanel
public class DrawPanel extends JPanel {
	private static final Color DEFALUT_COLOR = Color.BLACK;
	static final int DEFALUT_SIZE = 1;
	private static final long serialVersionUID = 7114066347061701832L;
	private LinesSet linesSet;
	private Line line;

	public DrawPanel() {
		this.line = new Line();
		this.linesSet = new LinesSet();
		this.linesSet.addLine(this.line);
	}

	// override del metodo di disegno
	public void paint(Graphics g) {
		super.paintComponent(g);

		for (Line e : this.linesSet.getLines()) {
			ColoredSizedPoint last = null;
			for (ColoredSizedPoint c : e.getColoredSizedPoint()) {
				if (last != null) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setColor(last.getPointColor());
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.setStroke(new BasicStroke(c.getPointSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					g2d.drawLine((int) last.getPointCoordinates().getX() + (c.getPointSize() / 2),
							(int) last.getPointCoordinates().getY() + (c.getPointSize() / 2),
							(int) c.getPointCoordinates().getX() + (c.getPointSize() / 2),
							(int) c.getPointCoordinates().getY() + (c.getPointSize() / 2));

//					g.setColor(c.getPointColor());
//					g.fillOval(c.getPointCoordinates().x, c.getPointCoordinates().y, c.getPointSize(), c.getPointSize());
				}
				last = new ColoredSizedPoint(c.getPointCoordinates(), c.getPointColor(), c.getPointSize());
			}
		}
	}

	public void deleteEverything() {
		this.linesSet.empty();
		this.line = new Line();
		this.linesSet = new LinesSet();
		this.linesSet.addLine(this.line);
	}

	public void setPenSize(final int size) {
		for (ColoredSizedPoint c : this.line.getColoredSizedPoint()) {
			c.setPointSize(size);
		}
	}

	public void setColor(final Color color) {
		for (ColoredSizedPoint c : this.line.getColoredSizedPoint()) {
			c.setPointColor(color);
		}
	}

	public Line getLineAtCoordinates(Point p) throws NoSuchElementException {
		for (Line l : this.linesSet.getLines()) {
			for (int i = (-l.getLineSize()); i <= l.getLineSize(); i++) {
				for (int j = (-l.getLineSize()); j <= l.getLineSize(); j++) {
					if (l.getPoints().contains(new Point((int) (p.getX() + i), (int) (p.getY() + j)))) {
						return l;
					}
				}
			}
		}
		throw new NoSuchElementException();
	}

	public void traslateLine(Line l, Point coordinatesToTranslate) {
		Pair<Point, Integer> vector = this.findClosestPoint(l, coordinatesToTranslate);
		for (ColoredSizedPoint p : l.getColoredSizedPoint()) {
			
			p.traslate(new Point((int)(p.getPointCoordinates().getX() + vector.first.getX()), (int)(p.getPointCoordinates().getY() + vector.first.getY())));
		}
	}

	private Pair<Point, Integer> findClosestPoint(Line l, Point a) {
		Point closest = new Point();
		int minDis = 99999;
		for (Point p : l.getPoints()) {
			int dis = (int)(Math.pow((p.getX() - a.getX()), 2) + Math.pow((p.getY() - a.getY()), 2));
			if ( dis < minDis) {
				minDis = dis;
				closest = p;
			}
		}
		System.out.println(" " + closest + minDis);
		return new Pair<>(closest, minDis);
	}

	public void createLineObj() {
		this.linesSet.addLine(this.line);
		this.line = new Line();
		this.linesSet.addLine(this.line);
	}

	public void addPoint(final int x, final int y) {
		int lastSize = DEFALUT_SIZE;
		Color c1 = DEFALUT_COLOR;
		for (ColoredSizedPoint c : this.line.getColoredSizedPoint()) {
			lastSize = c.getPointSize();
			c1 = c.getPointColor();
		}

		if (lastSize < DEFALUT_SIZE) {
			lastSize = DEFALUT_SIZE;
		}

		this.line.addPoint(new ColoredSizedPoint(new Point(x - (lastSize / 2), y - (lastSize / 2)), c1, lastSize));
	}

}
