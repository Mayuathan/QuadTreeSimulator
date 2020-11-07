package quadtreesimulator.animator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utility.QuadTree;
import quadtreesimulator.scene.ColorDetectionScene;

public class QuadTreeAnimator extends AbstractAnimator {
	
	private int[] buffer;
	private double x;
	private double y;
	private boolean initilized;
	private Canvas drawingCanvas;
	private QuadTree qt;
	
	public void init() {
		if (initilized) {
			return;
		}
		initilized = true;
		qt = ((ColorDetectionScene)scene).getQuadTree();
		ObjectProperty<Color> color = (ObjectProperty<Color>) scene.getOption("color");
		color.addListener(( v, o, n) -> qt.clear());
		drawingCanvas = new Canvas(scene.w(),scene.h());
		Canvas canvas =scene.getCanvas();
		canvas.setOnMouseDragged(e -> {x = e.getX();y = e.getY();});
				
		EventHandler<MouseEvent> eventHandler = new EventHandler<>() {
			public void handle(MouseEvent e) {
				GraphicsContext gc = drawingCanvas.getGraphicsContext2D();
				if(e.isPrimaryButtonDown()) {
					gc.setStroke(color.get());
				}
				gc.setLineWidth(2);
				if(isRunning()) {
					gc.strokeLine(x, y, e.getX(), e.getY());
				}
				x=e.getX();
				y=e.getY();
			}
		};
		canvas.setOnMouseDragged(eventHandler);
		
	}
	public void clear() {
		qt.clear();
		clearAndFill(drawingCanvas.getGraphicsContext2D(), Color.TRANSPARENT);
	}
	protected void handle(GraphicsContext gc, long now) {
		init();
		clearAndFill(gc, Color.TRANSPARENT);
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage image = drawingCanvas.snapshot(sp, null);
		if (image != null)
				gc.drawImage(image, 0, 0);
		BooleanProperty bp = (BooleanProperty) scene.getOption("displayQuadTree"); 
		if (bp.get()) {
			buffer =new int[ (int) ( scene.w() * scene.h()) + 1];
			image.getPixelReader().getPixels( 0, 0, (int) scene.w(), (int) scene.h(), PixelFormat.getIntArgbInstance(), buffer, 0, (int) scene.w());
			ObjectProperty< Color> color = (ObjectProperty<Color>) scene.getOption("color");
			qt.push(buffer, (int) scene.w(), color.get());
			qt.getDrawable().draw(gc);
		}
	}

}
