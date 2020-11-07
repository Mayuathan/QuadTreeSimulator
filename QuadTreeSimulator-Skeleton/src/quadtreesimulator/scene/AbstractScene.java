package quadtreesimulator.scene;


import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class AbstractScene {
	
	protected Canvas canvasNode;
	protected Map<String, Object> options;
	
	public AbstractScene() {
		options = new HashMap<>();
		addOption("displayFPS", new SimpleBooleanProperty( false));
	}
	
	public void setCanvas(Canvas canvas) {
		canvasNode = canvas;
	}
	
	public Canvas getCanvas() {
		return canvasNode;
	}
	
	public double w() {
		return canvasNode.getWidth();
	}
	
	public double h() {
		return canvasNode.getHeight();
	}
	
	public GraphicsContext gc() {
		return canvasNode.getGraphicsContext2D();
	}
	
	public void addOption(String uniqueName, Object option) throws IllegalStateException{
		if (options.containsKey(uniqueName)) {
			throw new IllegalStateException("Unique Name already exists.");
		}
		options.put(uniqueName,option);
	}
	
	public Object getOption(String uniqueName) {
		return options.get(uniqueName);
	}
	
	public abstract AbstractScene createScene();
}
