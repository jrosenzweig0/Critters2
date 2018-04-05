package assignment5;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;

import java.util.List;

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


//			//runStats
//            Button runStats = new Button("Stats");
//            rightU.add(new Text(Critter.runStats(selection from combo box)));



            //setSeed

            TextField seed = new TextField();
            Text seedText = new Text("Seed:");
            seedText.setFont(Font.font ("Verdana", 20));
            rSide.add(seedText,0,5);
            rSide.add(seed, 0,6);
            Button setSeed = new Button("Set");
            rSide.add(setSeed,1,6);


            //Make
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
            makeButton.setOnAction(event -> {
                int numCrit = (int)makeN.getValue();
                for (int i = 0; i < numCrit; i++) {
                    try{
                        Critter.makeCritter("assignment5." + CritSelect.getValue());
                    }
                    catch (InvalidCritterException e){
                        System.out.println("NAH");
                    }
                }
                paintGridLines(world);
            });
            
            

            //Step
			GridPane stepGrid = new GridPane();
			Button step1 = new Button("1");
			Button step100 = new Button("100");
			Button step1000 = new Button("1000");
			Text step = new Text("Step:");
			step.setFont(Font.font ("Verdana", 20));
			rSide.add(step, 0, 3);
			stepGrid.add(step1,0,0);
			stepGrid.add(step100,1,0);
			stepGrid.add(step1000,2,0);
			stepGrid.setHgap(20);
			
			//Step 1
            step1.setOnAction(event -> {
                try{
                    Critter.worldTimeStep();
                }
                catch (InvalidCritterException e){
                    System.out.println("NAH");
                }
                paintGridLines(world);
            });
            
            //Step 100
            step100.setOnAction(event -> {
                for(int i = 0; i<100; i++) {
	            	try{
	                    Critter.worldTimeStep();
	                }
	                catch (InvalidCritterException e){
	                    System.out.println("NAH");
	                }
                }
                paintGridLines(world);
            });
            
            //Step 1000
            step1000.setOnAction(event -> {
            	for(int i = 0; i<1000; i++) {
	                try{
	                    Critter.worldTimeStep();
	                }
	                catch (InvalidCritterException e){
	                    System.out.println("NAH");
	                }
            	}
                paintGridLines(world);
            });

			rSide.add(stepGrid,0,4);
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
                if (!Critter.firstTime) {
                    List<Critter> cTile = Critter.world.get(r).get(c).crittersOnTile();
                    if (cTile.size() > 0) {
                        switch (cTile.get(0).viewShape()) {
                            case CIRCLE:
                                s = new Circle(size / 2);
                                s.setFill(cTile.get(0).viewFillColor());
                                break;
                            case SQUARE:
                                s = new Rectangle(size, size);
                                s.setFill(cTile.get(0).viewFillColor());
                                break;
                        }
                    }
                }
                grid.add(s,c,r);
			}

	}
}
