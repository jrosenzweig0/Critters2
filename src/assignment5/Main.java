package assignment5;

import javafx.application.Application;
import javafx.scene.Scene;
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
			GridPane grid = new GridPane();
			Scene scene = new Scene(grid,600, 600);
			primaryStage.setScene(scene);
			primaryStage.show();

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
		int size = 300/Params.world_height;
		for (int r = 0; r < Params.world_height; r++)
			for (int c = 0; c < Params.world_width; c++) {
				Shape s = new Rectangle(size, size);
				s.setFill(null);
				s.setStroke(Color.BLUEVIOLET);
				grid.add(s, c, r);
			}

	}
}
