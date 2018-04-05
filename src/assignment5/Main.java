package assignment5;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import static javafx.application.Application.launch;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	@Override
	public void start(Stage primaryStage) throws Exception{
		try{
			primaryStage.setTitle("World");
			GridPane s = new GridPane();
			GridPane world = new GridPane();
			s.add(world, 0, 0);
			Scene scene = new Scene(s,1200, 800);
			primaryStage.setScene(scene);
			primaryStage.show();

			paintGridLines(world);

			GridPane rSide = new GridPane();
			GridPane bottom = new GridPane();

			GridPane animationStuff = new GridPane();
			Button animateB = new Button("Animate");
			Slider animateS = new Slider();
			animateS.setMin(1);
			animateS.setMax(5);
			animateS.setValue(1);
			animateS.setShowTickLabels(false);
			animateS.setShowTickMarks(false);
			animateS.setBlockIncrement(5);
			animateS.setPrefWidth(200);
			animationStuff.add(animateS, 0,0);
			animationStuff.add(animateB,1,0);
			animationStuff.setPadding(new Insets(10,10,10,10));
			animationStuff.setHgap(20);
			bottom.add(animationStuff,0,0);
			s.add(bottom,0,1);


			Button makeButton = new Button("MAKE");
			Slider makeN = new Slider();
			makeN.setMin(0);
			makeN.setMax(500);
			makeN.setValue(0);
			makeN.setShowTickLabels(true);
			makeN.setShowTickMarks(true);
			makeN.setMajorTickUnit(100);
			makeN.setMinorTickCount(10);
			makeN.setBlockIncrement(5);
			makeN.setPrefWidth(200);

			ObservableList<String> Crits = FXCollections.observableArrayList("Critter1","Critter2","Critter3",
					"Critter4", "Algae", "TragicCritter", "Craig", "AlgaephobicCritter");
			final ComboBox CritSelect = new ComboBox(Crits);

			GridPane stepGrid = new GridPane();
			Button step1 = new Button("1");
			Button step100 = new Button("100");
			Button step1000 = new Button("1000");
			Text step = new Text("Step:");
			step.setFont(Font.font ("Verdana", 20));
			stepGrid.add(step, 0, 0);
			stepGrid.add(step1,1,0);
			stepGrid.add(step100,2,0);
			stepGrid.add(step1000,3,0);
			stepGrid.setHgap(20);

			rSide.add(stepGrid,0,3);
			rSide.add(makeN,0,2);
			rSide.add(makeButton,1,2);
			rSide.add(CritSelect,0,1);
			rSide.setVgap(10);
			rSide.setHgap(10);


			s.setHgap(30);
			s.setVgap(10);
			s.setPadding(new Insets(10, 10, 10, 10));
			s.add(rSide, 1, 0);


		}
		catch (Exception e) {
		e.printStackTrace();
	}
	}

	/*
	 * Paint the grid lines in something not yellow.  The purpose is two-fold -- to indicate boundaries of
	 * icons, and as place-holders for empty cells.  Without placeholders, grid may not display properly.
	 */
	private static void paintGridLines(GridPane grid) {
		int size = 600/Params.world_height;
		for (int r = 0; r < Params.world_height; r++)
			for (int c = 0; c < Params.world_width; c++) {
				Shape s = new Rectangle(size, size);
				s.setFill(null);
				s.setStroke(Color.GRAY);
				grid.add(s, c, r);
			}

	}
}
