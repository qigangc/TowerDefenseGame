package view;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * 
 * When clicking on the rules button from the MainView, this view is called showing a list of the rules for the game
 *
 */
public class RuleView extends Application{

	/**
	 * launches the window
	 * 
	 * @param args the list of arguments for launch
	 */
	public static void main(String [] args){
		Application.launch(args);
	}

	/**
	 * starts the application. sets the title, generates needed images and sets the rule message to be displayed
	 * 
	 * @param primaryStage the stage in which we will be showing and adding everything needed for the window to.
	 */
	@Override
	public void start(Stage primaryStage){
		primaryStage.setTitle("Game Rules");

		BorderPane borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color: #000000");
		Scene scene = new Scene(borderPane, 550, 130);
		Image bloodGif = new Image("images/bloodm10.gif");
		ImageView blood = new ImageView(bloodGif);
		Text rule = new Text("          Destroy all waves of enemies to prevent them from reaching the destination\n"
				+ "    by obstructing different types of towers and placing them along the path of enemies.\n"
				+ "                                       To win, you must survive 3 waves of enemies.\n"
				+ "                                    If you die prior to clearing all the waves, you lose!");
		rule.setFill(Color.WHITE);
		rule.setFont(Font.font("American Typewriter"));
		VBox vbox = new VBox(-200);
		vbox.getChildren().addAll(blood, rule);
		borderPane.setCenter(vbox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
