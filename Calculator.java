import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.Stack;

public class Calculator extends Application {
	private final int DEFAULT_SIZE = 540;
	private final int DEFAULT_SMALL_SIZE = 390;
	private TextField output = new TextField("");
	private String boxStyle = "-fx-background-color: black; -fx-border-color: white; -fx-border-width: 3";
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		pane.setTop(getTop());
		pane.setLeft(getVBoxL());
		pane.setRight(getVBoxR());
		
		Scene scene = new Scene(pane);
		primaryStage.setTitle("MY CALCULATOR");
		primaryStage.setScene(scene);
		primaryStage.setHeight(DEFAULT_SIZE);
		primaryStage.setWidth(DEFAULT_SIZE);
		primaryStage.setResizable(false);
		primaryStage.show();	
	}
	
	private MenuBar getMenuBar() {
		Menu fileMenu = new Menu("File");
		MenuItem clear = new MenuItem("Clear All");
		MenuItem exit = new MenuItem("Exit");
		fileMenu.getItems().add(clear);
		fileMenu.getItems().add(exit);
		clear.setOnAction(e -> {output.setText("");});
		exit.setOnAction(e -> {System.exit(0);});
		
		Menu operations = new Menu("Operations");
		MenuItem[] menuItems = {new MenuItem("Add       (+)"), new MenuItem("Subtract (-)"), 
								new MenuItem("Multiply (*)"), new MenuItem("Divide    (/)")};
		String[] operatorStr = {" + ", " - ", " * ", " / ", " = "};
		for (int i = 0; i < menuItems.length; i++) {
			String add = operatorStr[i];
			operations.getItems().add(menuItems[i]);
			menuItems[i].setOnAction(e -> {output.setText(output.getText() + add);});
		}
		
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefWidth(DEFAULT_SIZE);
		menuBar.getMenus().addAll(fileMenu, operations);
		return menuBar;
	}
	
	private FlowPane getTop() {
		FlowPane topPane = new FlowPane();
		topPane.getChildren().addAll(getMenuBar(), getHBox());
		return topPane;
	}
	
	private HBox getHBox() {
		HBox hBox = new HBox(20);
		hBox.setPrefWidth(DEFAULT_SIZE);
		hBox.setPadding(new Insets(20,30,20,10));
		hBox.setStyle(boxStyle);
		hBox.setAlignment(Pos.CENTER_RIGHT);
	    
		Label equal = new Label("");
		equal.setTextFill(Color.web("#FFFFFF", 1));
		equal.setFont(Font.font("Cambria", 20));
		hBox.getChildren().add(equal);
		
		output.setEditable(false);
		output.setAlignment(Pos.CENTER_RIGHT);
		output.setPrefSize(370, 45);
		output.setFont(Font.font(24));
		hBox.getChildren().add(output);
		return hBox;
	}
	
	private VBox getVBoxL() {
		final int boxHeight = 70;
		final int boxWidth = 70;
		final int numButtons = 12;
		
		VBox vBox = new VBox(20);
		vBox.setStyle(boxStyle);
		GridPane grid = new GridPane();
		grid.setPrefSize(DEFAULT_SMALL_SIZE, DEFAULT_SMALL_SIZE);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(30);
		grid.setVgap(20);
		
		String[] labels = {"1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "AC"}; 
		Button[] numbers = new Button[numButtons];
		int idx = 0;
		for (int row = 0; row < numButtons/3; row++)
			for (int col = 0; col < numButtons/4; col++) {
				numbers[idx] = new Button();
				numbers[idx].setPrefSize(boxWidth, boxHeight);
				numbers[idx].setStyle("-fx-background-color: yellow; -fx-font-weight: bold; -fx-font-size: 25;");
				numbers[idx].setAlignment(Pos.CENTER);
				numbers[idx].setText(labels[idx]);
				grid.add(numbers[idx], col, row);
				idx++;
			}
		for (int i = 0; i < numButtons - 1; i++) {
			String add;
			if (i < numButtons - 3)
				add = Integer.toString(i + 1);
			else if (i == numButtons - 3)
				add = ".";
			else
				add = "0";
			numbers[i].setOnAction(e -> {output.setText(output.getText() + add);});
		}
		numbers[11].setOnAction(e -> {output.setText("");});
		vBox.getChildren().add(grid);
		return vBox;
	}
	
	private VBox getVBoxR() {
		final int boxHeight = 50;
		final int boxWidth = 90;
		final int numButtons = 5;
		
		VBox vBox = new VBox(20);
		vBox.setPrefSize(DEFAULT_SIZE - DEFAULT_SMALL_SIZE, DEFAULT_SMALL_SIZE);
		vBox.setStyle(boxStyle);
		vBox.setAlignment(Pos.CENTER);
		Button[] operator = new Button[numButtons];
		String[] operatorStr = {" + ", " - ", " * ", " / ", " = "};
		String[] operations = {"Add.png", "Subtract.png", "Multiply.png", "Divide.png", "Equal.jpg"};
		
		for (int i = 0; i < numButtons; i++) {
			operator[i] = new Button();
			operator[i].setPrefSize(boxWidth, boxHeight);
			Image img = new Image(operations[i]);
			ImageView view = new ImageView(img);
			view.setFitHeight(50);
			view.setFitWidth(60);
			operator[i].setGraphic(view);
			operator[i].setStyle("-fx-background-color: black");
			vBox.getChildren().add(operator[i]);
		}
		
		for (int i = 0; i < numButtons - 1; i++) {
			String add = operatorStr[i];
			operator[i].setOnAction(e -> {output.setText(output.getText() + add);});
		}
		operator[numButtons - 1].setOnAction(e -> {output.setText(calculate(output.getText()));});
		return vBox;
	}
	
	private String calculate(String input) {
		String[] arr = input.split(" ");
		Stack<Double> numStack = new Stack<Double>();
		Stack<Character> chStack = new Stack<Character>();
		int index = 1;
		
		numStack.push(Double.parseDouble(arr[0]));
		while (index < arr.length) {	
			if (arr[index].charAt(0) >= '0' && arr[index].charAt(0) <= '9')
				numStack.push(Double.parseDouble(arr[index]));
			else if (arr[index].charAt(0) == '*' || arr[index].charAt(0) == '/') {
				double x = numStack.pop();
				double y = Double.parseDouble(arr[index + 1]);
				double z = (arr[index].charAt(0) == '*') ? (x*y) : (x/y);
				numStack.push(z);
				index++;
			} else {
				chStack.push(arr[index].toCharArray()[0]);
			}
			index++;
		}
		
		double result = 0;
		while (!chStack.empty()) {
			char operand = chStack.pop();
			double num = numStack.pop();
			num = operand == '+' ? num : num * -1;
			result += num;
		}
		result += numStack.pop();
		return Double.toString(result);
	}
}