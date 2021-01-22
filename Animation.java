import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Animation extends Application {
	private final int DEFAULT_SIZE = 600;
	private final int TOP_HEIGHT = 80;
	private final String DEFAULT_STYLE = "-fx-border-color: black;" + 
										 "-fx-border-width: bw;" + 
										 "-fx-background-color: white;";
	private String currentShape = "";
	private String[] shapesName = {"POLYGON", "CYLINDER", "BOX", "SPHERE"};
	private Node[] shapes = {getPolygon(), getCylinder(), getBox(), getSphere()};
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setPrefSize(DEFAULT_SIZE, DEFAULT_SIZE + TOP_HEIGHT);
		pane.setTop(getTransitionButtons());
		pane.setLeft(getVerticalGrid(getGridCell(shapes[0], "4 2 2 2"), getGridCell(shapes[1], "2 2 4 2")));
		pane.setRight(getVerticalGrid(getGridCell(shapes[2], "4 4 2 2"), getGridCell(shapes[3], "2 4 4 2")));
	    
		Scene scene = new Scene(pane);
		primaryStage.setTitle("SHAPES ANIMATION");
		primaryStage.setScene(scene);
		primaryStage.setHeight(DEFAULT_SIZE + TOP_HEIGHT);
		primaryStage.setWidth(DEFAULT_SIZE + 10);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}
	
	private VBox getVerticalGrid(HBox topBox, HBox downBox) {
		VBox vbox = new VBox();
		vbox.getChildren().addAll(topBox, downBox);
		return vbox;
	}
	
	private HBox getTransitionButtons() {
		HBox hbox = new HBox(20);
		hbox.setPrefHeight(TOP_HEIGHT);
		hbox.setStyle("-fx-background-color: black;");
		hbox.setAlignment(Pos.CENTER);
		Button[] transition = new Button[4];
		String[] types = {"ROTATE", "SCALE", "SEQUENTIAL", "2D FADE"};
		for (int i = 0; i < types.length; i++) {
			transition[i] = new Button(types[i]);
			transition[i].setPrefSize(120, 50);
			transition[i].setStyle("-fx-background-color: white; -fx-text-fill: #ff0000; "
					+ "-fx-font-size: 0.4cm; -fx-font-weight: bold;");
			hbox.getChildren().add(transition[i]);
		}
		transition[0].setOnMouseClicked(e -> {getRotate().play();});
		transition[1].setOnMouseClicked(e -> {getScale().play();});
		transition[2].setOnMouseClicked(e -> {getSequential().play();});
		transition[3].setOnMouseClicked(e -> {getFade().play();});
		return hbox;
	}
	
	private HBox getGridCell(Node node, String borderWidth) {
		HBox box = new HBox();
		box.setPrefSize(DEFAULT_SIZE/2 - 2, DEFAULT_SIZE/2 - 2);
		box.setStyle(DEFAULT_STYLE.replaceAll("bw", borderWidth));
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(node);
		return box;
	}
	
	private Polygon getPolygon() {
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(
				new Double[] {90.0, 290.0, 60.0, 160.0, 155.0, 100.0, 250.0, 160.0, 220.0, 290.0,}
		);
		polygon.setFill(Color.DARKGREEN);
		polygon.setOnMouseClicked(e -> {currentShape = "POLYGON";});
		return polygon;
	}
	
	private Box getBox() {
		Box box = new Box(160, 140, 200);
		PhongMaterial phMaterial = new PhongMaterial();
		phMaterial.setDiffuseColor(Color.RED);
		box.setMaterial(phMaterial);
		box.setOnMouseClicked(e -> {currentShape = "BOX";});
		return box;
	}
	
	private Sphere getSphere() {
		Sphere sphere = new Sphere(90);
		PhongMaterial phMaterial = new PhongMaterial();
		phMaterial.setDiffuseColor(Color.BLUE);
		sphere.setMaterial(phMaterial);
		sphere.setOnMouseClicked(e -> {currentShape = "SPHERE";});
		return sphere;
	}
	
	private Cylinder getCylinder() {
		Cylinder cylinder = new Cylinder();
		cylinder.setHeight(170);
		cylinder.setRadius(70);
		PhongMaterial phMaterial = new PhongMaterial();
		phMaterial.setDiffuseColor(Color.YELLOW);
		cylinder.setMaterial(phMaterial);
		cylinder.setOnMouseClicked(e -> {currentShape = "CYLINDER";});
		return cylinder;
	}
	
	private Node findShape() {
		Node node = null;
		for (int i = 0; i < shapes.length; i++) 
			if (shapesName[i] == currentShape) {
				node = shapes[i];
				break;
			}
		return node;
	}
	private RotateTransition getRotate() {
		RotateTransition rotateTransition = new RotateTransition();
		rotateTransition.setDuration(Duration.millis(7000));
		rotateTransition.setNode(findShape());
		rotateTransition.setAxis(Rotate.X_AXIS);
		rotateTransition.setByAngle(360);
		rotateTransition.setCycleCount(2);
		rotateTransition.setAutoReverse(false);
		return rotateTransition;
	}
	
	private ScaleTransition getScale() {
		ScaleTransition scaleTransition = new ScaleTransition();
		scaleTransition.setDuration(Duration.millis(1000));
		scaleTransition.setNode(findShape());
		scaleTransition.setByY(0.45);
		scaleTransition.setByX(0.45);
		scaleTransition.setCycleCount(4);
		scaleTransition.setAutoReverse(true);
		return scaleTransition;
	}
	
	private FadeTransition getFade() {
		FadeTransition fade = new FadeTransition(Duration.millis(2000));  
		fade.setNode(findShape());
        fade.setFromValue(1.0f);  
        fade.setToValue(0.3f);  
        fade.setCycleCount(4);  
        fade.setAutoReverse(true);
        return fade;
	}
	
	private SequentialTransition getSequential() {
		PauseTransition pause = new PauseTransition(Duration.millis(1000));
        TranslateTransition translate = new TranslateTransition(Duration.millis(1000));  
        translate.setToX(-50f);
        translate.setToY(50f);
        translate.setCycleCount(2);  
        translate.setAutoReverse(true);  
        
		SequentialTransition seqTransition = new SequentialTransition(
				findShape(), getScale(), pause, translate, getFade(), getRotate()
		);
		return seqTransition;
	}
}
