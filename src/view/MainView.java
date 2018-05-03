package view;
import model.*;
import javafx.scene.image.ImageView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

/**
 * the starting view containing the option to view the rules, load the game, start a new one on the three given difficulties.
 */
public class MainView extends Application{
	private GameView gameView;
	public static void main(String [] args){
		Application.launch(args);
	}
  
  /**
   * starts and initiates the views. declares all fonts the the pictures, gifts and sounds that will be needed to show the view.
   * sets all the buttons in the appropriate locations and declares the handlers for each of the click-able buttons
   * 
   * @param primaryStage the stage we will be adding all of the buttons to then sets and shows the scene
   */
  @Override
  public void start(Stage primaryStage) {  
	
    primaryStage.setTitle("Tower Defense");
    BorderPane borderPane = new BorderPane();
    Scene scene = new Scene(borderPane, 685, 394);
    // change the background image here
    borderPane.setStyle("-fx-background-image: url(\"images/SpookyBackground.jpg\");-fx-background-size: 700, 390;-fx-background-repeat: no-repeat;");
    borderPane.getStylesheets().add(getClass().getResource("/view/buttonStyle.css").toExternalForm());
    Font headerFont = Font.loadFont(getClass().getResource("/font/Bloodrac.ttf").toExternalForm(), 107);
    Font font = Font.loadFont(getClass().getResource("/font/DIEDIEDI.ttf").toExternalForm(), 45);
    Font font2 = Font.loadFont(getClass().getResource("/font/DIEDIEDI.ttf").toExternalForm(), 20);
    
    Image bloodGif = new Image("images/bloodm10.gif");
    ImageView blood = new ImageView(bloodGif);
    Text header = new Text("Tower Defense");
    
    //the buttons we will use
    Button easyButton = new Button("Easy");
    Button intmButton = new Button("Intermediate");
    Button hardButton = new Button("Hard");
    Button ruleButton = new Button("Game Rules");
    Button loadButton = new Button("Load Game");
    ruleButton.setPrefWidth(130);
    loadButton.setPrefWidth(130);
    
    Text theme = new Text("                      Haunted edition"); // change the theme name here
    theme.setFill(Color.web("#FFFFFF"));
    theme.setFont(font);
    theme.setStyle("-fx-stroke-width:1px;-fx-stroke:black;");

    //add the buttons to the appropriate boxes
    HBox buttons = new HBox(25);
    buttons.getChildren().addAll(easyButton, intmButton, hardButton);
    VBox vbox = new VBox(5);
    vbox.getChildren().addAll(ruleButton, loadButton);
    VBox mainHeader = new VBox(-60);
    VBox headerBox = new VBox(-235);
    headerBox.getChildren().addAll(blood, header);
    mainHeader.getChildren().addAll(headerBox, theme);
    
    // change header here
    header.setFont(headerFont);
    header.setStyle("-fx-stroke-width:1px;-fx-stroke:gray;");
    DropShadow dropShadow = new DropShadow();
    dropShadow.setColor(Color.BLACK);
    dropShadow.setRadius(25);
    dropShadow.setSpread(0.1);
    dropShadow.setBlurType(BlurType.GAUSSIAN);
    header.setEffect(dropShadow);
    header.setFill(Color.web("#870208"));

    //set the fonts
	easyButton.setFont(font);
	intmButton.setFont(font);
	hardButton.setFont(font);
	ruleButton.setFont(font2);
	loadButton.setFont(font2);

	//set the listeners
    ButtonListener buttonListener = new ButtonListener(primaryStage);
    easyButton.setOnAction(buttonListener);
    intmButton.setOnAction(buttonListener);
    hardButton.setOnAction(buttonListener);
    ruleButton.setOnAction(buttonListener);
    loadButton.setOnAction(buttonListener);
    
    // change sound here
	Media sound = new Media(new File("bin/sound/hauntedhouse.wav").toURI().toString());
	MediaPlayer mediaPlayer = new MediaPlayer(sound);
	mediaPlayer.play();	
	
	// set border pane here
    borderPane.setTop(mainHeader);
    BorderPane.setAlignment(buttons, Pos.CENTER);
    BorderPane.setMargin(buttons, new Insets(12,12,12,12));
    borderPane.setBottom(buttons);
    borderPane.setCenter(vbox);
    BorderPane.setMargin(vbox, new Insets(0,12,0,12));
    primaryStage.setScene(scene);
    primaryStage.show();


  }
  
  /**
   * listener for any of the buttons on this view. if any of the buttons are clicked, takes the input and handles the command 
   * accordingly
   */
  public class ButtonListener implements EventHandler<ActionEvent> {
		private Stage stage;
	  	
		/**
		 * constructor for the button listener
		 * 
		 * @param stage the stage that the command is coming from (the MainView)
		 */
		public ButtonListener(Stage stage) {
			this.stage = stage;
		}
		
		@Override
		public void handle(ActionEvent e) {
			
			
			String text = ((Button) e.getSource()).getText();
			if (text.equals("Game Rules")) {
	            RuleView ruleView = new RuleView();
	            Stage stage = new Stage();
	            ruleView.start(stage);
	            return;
			}
			
			if (text.equals("Load Game")){
				//TODO: Still bug here.
				try {
					GameMap map = SaverLoader.loadFile();
					map.setEnemyPath1(SaverLoader.returnEnemyPath());
					map.setLoadMap(true);
					Player player = map.getPlayer();
					gameView = new GameView(player);
					gameView.start(new Stage());			
					stage.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
			
			GameMap map;
			if (text.equals("Easy"))
				map = new Map_Easy();
			else if (text.equals("Intermediate"))
				map = new Map_Intermediate();
			else
				map = new Map_Hard();

			Player player = new Player(map);

			gameView = new GameView(player);
			gameView.start(new Stage());			
			stage.close();
		}
  }  
}

