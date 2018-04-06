package assignment5;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import java.util.List;

import static javafx.application.Application.launch;

public class Main extends Application {
    public boolean animateFlag = false;
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
            rSide.setPadding(new Insets(10,10,10,10));
            rSide.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10))));
            GridPane bottom = new GridPane();

            ObservableList<String> Crits = FXCollections.observableArrayList("Critter1","Critter2","Critter3",
                    "Critter4", "Algae", "TragicCritter", "Craig", "AlgaephobicCritter");
            final ComboBox CritSelect = new ComboBox(Crits);

            TextField seed = new TextField();
            Text seedText = new Text("Seed:");
            seedText.setFont(Font.font ("Verdana", 20));
            Text pickCritter = new Text("Select Critter Type: ");
            pickCritter.setFont(Font.font ("Verdana", 18));
            Text statsTitle = new Text("Stats:");
            statsTitle.setFont(Font.font ("Verdana", 20));
            TextArea statsOut = new TextArea();
            statsOut.setWrapText(true);
            statsOut.setPrefRowCount(5);
            statsOut.setEditable(false);
            statsOut.setPrefWidth(10);
            statsOut.setPrefHeight(100);
            CritSelect.setOnAction(event -> {
                stats(CritSelect, statsOut);
            });

            GridPane animationStuff = new GridPane();
            Button animateB = new Button("Animate");
            Button stopAnimate = new Button("Stop");
            stopAnimate.setDisable(true);
            stopAnimate.setOnAction(event -> {animateFlag = false;});
            bottom.add(stopAnimate, 2, 0);

            Slider animateS = new Slider();
            animateS.setMin(1);
            animateS.setMax(10);
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


            Timeline timeline = new Timeline();
            animateB.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    statsOut.clear();
                    animateFlag = !animateFlag;
                    if(!animateFlag){
                        return;
                    }

                    stopAnimate.setDisable(false);
                    animateB.setDisable(true);


                    timeline.getKeyFrames().add(new KeyFrame(Duration.millis((int) 1000/animateS.getValue()),
                            new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event){
                                    if(!animateFlag){
                                        stopAnimate.setDisable(true);
                                        animateB.setDisable(false);
                                        stats(CritSelect, statsOut);
                                        return;
                                    }
                                    try {
                                        Critter.worldTimeStep();
                                    } catch (InvalidCritterException e) {
                                        e.printStackTrace();
                                    }
                                    paintGridLines(world);
                                }

                            }));
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();


                }
            });


            Button setSeed = new Button("Set");
            setSeed.setOnAction(event -> {
                Critter.setSeed(Integer.parseInt(seed.getText()));
            });

            Text makeTitle = new Text("Make:");
            makeTitle.setFont(Font.font ("Verdana", 20));


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
                stats(CritSelect, statsOut);
                paintGridLines(world);
            });



            //Step
            GridPane stepGrid = new GridPane();
            Button step1 = new Button("1");
            Button step100 = new Button("100");
            Button step1000 = new Button("1000");
            Text step = new Text("Step:");
            step.setFont(Font.font ("Verdana", 20));
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
                stats(CritSelect, statsOut);
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
                stats(CritSelect, statsOut);
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
                stats(CritSelect, statsOut);
                paintGridLines(world);
            });


            rSide.add(pickCritter,0,0);
            rSide.add(CritSelect,0,1);
            rSide.add(makeTitle, 0, 2);
            rSide.add(makeN,0,3);
            rSide.add(makeButton,1,3);
            rSide.add(step, 0, 4);
            rSide.add(stepGrid,0,5);
            rSide.add(seedText,0,6);
            rSide.add(seed, 0,7);
            rSide.add(setSeed,1,7);
            rSide.add(statsTitle, 0,8);
            rSide.add(statsOut,0,9);




            rSide.setVgap(20);
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

    public void stats(ComboBox box, TextArea out){
        try {
            if (!(Critter.critterTypes.contains("assignment5." + box.getValue()))) {//if invalid type throw invalid critter exception
                throw new InvalidCritterException((String)box.getValue());
            }
            List<Critter> crits = Critter.getInstances("assignment5." + box.getValue());
            Class<?> critterClass = Class.forName("assignment5." + box.getValue()); //get class corresponding to critter_class_name
            Method m[] = critterClass.getDeclaredMethods();
            for (int i=0; i<m.length; i++){
                if (m[i].getName().equals("runStats")){
                    try{
                        out.setText((String)m[i].invoke(critterClass, crits));
                    }
                    catch(InvocationTargetException e){
                        System.out.println("error processing: " + box.getValue());
                    }
                }
            }
        }
        catch (InvalidCritterException e) {
            System.out.println("error processing: " + box.getValue());
        }
        catch (Exception e) {                                            //if other exception print stack trace
            System.out.println("error processing: " + box.getValue());
        }

    }

    /*
     * Paint the grid lines in something not yellow.  The purpose is two-fold -- to indicate boundaries of
     * icons, and as place-holders for empty cells.  Without placeholders, grid may not display properly.
     */
    private static void paintGridLines(GridPane grid) {
        int size = 600/Params.world_height;
//		grid.getColumnConstraints().add(new ColumnConstraints(size));
//		grid.getRowConstraints().add(new RowConstraints(size));
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);
        for (int r = 0; r < Params.world_height; r++)
            for (int c = 0; c < Params.world_width; c++) {
                Shape s = new Rectangle(size, size);
                s.setFill(null);
                s.setStroke(Color.WHITE);
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