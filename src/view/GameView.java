package view;

import model.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * this is the view for once the game has begun.
 * handles all clicking, GUI controlling, etc. Also contains the game loop, which handles tower attacks, moving enemies and
 * drawing everything in the game
 */
public class GameView extends Application implements Observer {
	
	//all global variables
	//names are made to be self explanatory
	private GameMap map;
	private Player player;
	private double maxHealth;
	private boolean isGameRunning, isWaveInProgress, fastMode = false;
	private Text goldCount, towerCount, enemyCount, scoreCount;
	private ProgressBar healthBar;
	private Canvas canvas;
	private GraphicsContext gc;
	private boolean isShopping;
	private int x, y, newX, newY, waveNum, spawnCount;
	private int gameCycleSpeed = 500;		//measured in ms
	private Tile selectedTile;
	private Stage primaryStage;
	private VBox shop;
	private Text info;
	private Button upgradeButton;
	private ButtonHandler buttonHandler;
	private String upgrade;
	private RadioButton rb1, rb2, rb3;
	private boolean rangeShown = false;
	private boolean skip = true;
	private boolean gameOver = false;
	private BorderPane borderPane;
	private Circle circle;
	private int draggable = 0;
	private String buyingTower;
	private final Font buttonFont2 = Font.loadFont(getClass().getResource("/font/DIEDIEDI.ttf").toExternalForm(), 11);
	private final Font buttonFont = Font.loadFont(getClass().getResource("/font/DIEDIEDI.ttf").toExternalForm(), 18);

	//all images used
	private Image path = new Image("images/MapTiles-Path.png");
	private Image buildable = new Image("images/MapTiles-Buildable.png");
	private Image spawner = new Image("images/MapTiles-spawn.gif");
	private Image end = new Image("images/MapTiles-End.gif");
	private Image poison = new Image("images/Enemy/poison.gif", 32, 32, true, true);
	private Image slowdown = new Image("images/Enemy/slowed.gif", 32, 32, true, true);
	private Image freeze = new Image("images/Enemy/frozen.gif", 32, 32, true, true);
	private Image attack = new Image("images/Tower/NewNormalAttack.gif", 32, 0, false, false);
	private Image normalAttack = new Image("images/Tower/NormalAttack.gif", 116, 64, false, false);
	private Image wizardAttack = new Image("images/Tower/WizardAttack.gif", 57, 83, false, false);
	private Image magicAttack = new Image("images/Tower/MagicAttack.gif", 93, 70, false, false);
	Image youLose = new Image("images/LoseMessage.gif", 200,200,false,false);

	/**
	 * constructs the view and initializes all necessary global variables.
	 * adds observers to appropriate model classes.
	 *   
	 * @param player Creates a game view with a given player, who is playing on a GameMap.
	 */
	public GameView(Player player) {
		this.player = player;
		this.map = player.getMap();
		this.map.setPlayer(player);
		this.maxHealth = player.getHealth();
		this.waveNum = 0;
		this.isGameRunning = true;
		this.isWaveInProgress = false;
		this.fastMode = false;
		this.isShopping = true;
		
		//add observers
		player.addObserver(this);
		map.addObserver(this);
	}

	/**
	 * launches application with the given args
	 * 
	 * @param args a list of arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * initializes all buttons, canvas and graphics, the shop, draws the map 
	 * and sets/shows the scene
	 * 
	 * @param primaryStage the stage that we will be setting
	 */
	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;
		
		String difficulty = map.getClass().getSimpleName().substring(4);
		primaryStage.setTitle("Tower Defense - " + difficulty);
		
		//create the scene and BorderPane
		borderPane = new BorderPane();
		borderPane.setPadding(new Insets(10, 10, 10, 10));
		Scene scene = new Scene(borderPane, 630, 655);
		
		//the canvas and graphics to be used
		this.canvas = new Canvas(730, 590);
		this.gc = canvas.getGraphicsContext2D();
		
		borderPane.setStyle("-fx-background-image: url(\"images/gameBackground.gif\");-fx-background-repeat: repeat;");
		borderPane.getStylesheets().add(getClass().getResource("/view/radio.css").toExternalForm());
		borderPane.getStylesheets().add(getClass().getResource("/view/buttonStyle.css").toExternalForm());

		//create buttons
		Button playButton = new Button("Pause");
		Button speedButton = new Button("Fast");
		Button saveButton = new Button("Save");
		Button startWaveButton = new Button("Start Wave");

		//set their font
		playButton.setFont(buttonFont);
		saveButton.setFont(buttonFont);
		speedButton.setFont(buttonFont);
		startWaveButton.setFont(buttonFont);

		//set their handlers
		buttonHandler = new ButtonHandler();
		playButton.setOnAction(buttonHandler);
		speedButton.setOnAction(buttonHandler);
		saveButton.setOnAction(buttonHandler);
		startWaveButton.setOnAction(buttonHandler);

		//create the shop view
		HBox stat = stat();
		shop = shop();
		HBox buttonBox = new HBox(10);
		buttonBox.getChildren().addAll(playButton, saveButton, speedButton, startWaveButton);

		//and make it all look pretty
		borderPane.setLeft(shop);
		borderPane.setTop(stat);
		BorderPane.setMargin(canvas, new Insets(5,0,0,0));
		borderPane.setCenter(canvas);
		BorderPane.setAlignment(buttonBox, Pos.CENTER_RIGHT);
		BorderPane.setMargin(buttonBox, new Insets(-35,0,0,0));
		borderPane.setBottom(buttonBox);
		borderPane.setRight(null);
		
		//draw the map and show the scene
		primaryStage.setScene(scene);
		primaryStage.show();
		this.drawMap();
	}

	/**
	 * draws all the towers currently on the map. if tower is attacking, draw the attack
	 * animation.
	 *
	 */
	public void drawTower() {
		//for all towers the player has put down, draw them.
		for (Tower tower : player.getTowers()) {
			
			gc.drawImage(buildable, (tower.getTile().getXpos() * 32), (tower.getTile().getYpos() * 32));
			dragDrop(new ImageView(tower.getImage().getImage()));
			gc.drawImage(tower.getImage().getImage(), (tower.getTile().getXpos() * 32)-16, tower.getTile().getYpos() * 32-26, 32*2, 32*2);

			//if attacking draw the animation for it
			if (tower.isAttacking())
				gc.drawImage(attack, (tower.getTile().getXpos() * 32)+5, tower.getTile().getYpos() * 32-100, 18, 90);
		}
	}

	/**
	 * draws every enemy on the map given where they are moving, and if they are effected by
	 * a tower. If an enemy is being attacked by a tower, draws the tower explosion gif
	 * depending on which tower is attacking the enemy.
	 * 
	 */
	public void drawEnemy() {
		LinkedList<Enemy> enemiesListDrawing = map.getAliveEnemies();
		//for all alive enemies
		for (Enemy enemyDraw : enemiesListDrawing) {
			//skip if they do not have a picture due to being dead or escaped
			if (enemyDraw.getImage() == null)
				continue;
			//only draw if they're on the map still and alive
			if(enemyDraw.getStatus().compareTo(EnemyStatus.ESCAPED) != 0){
				if ((enemyDraw.getHealth() >= 0) && (enemyDraw.getStatus().compareTo(EnemyStatus.DIED) != 0)
						&& (enemyDraw.getStatus().compareTo(EnemyStatus.ESCAPED) != 0)) {
					if (enemyDraw.getName().equalsIgnoreCase("bat")){
						gc.drawImage(enemyDraw.getImage(), enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
						gc.setFill(Color.LAWNGREEN);
						gc.fillRect((enemyDraw.getYPos()*32)+6, (enemyDraw.getXPos()*32)-6, 20*(enemyDraw.getHealth()/(double)enemyDraw.getOriginalHealth()), 5);
					}
					else if (enemyDraw.getName().equalsIgnoreCase("skeleton")){
						gc.drawImage(enemyDraw.getImage(), enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
						gc.setFill(Color.LAWNGREEN);
						gc.fillRect((enemyDraw.getYPos()*32)+6, (enemyDraw.getXPos()*32)-6, 20*(enemyDraw.getHealth()/(double)enemyDraw.getOriginalHealth()), 5);
					}
					else if (enemyDraw.getName().equalsIgnoreCase("ghost")){
						gc.drawImage(enemyDraw.getImage(), enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
						gc.setFill(Color.LAWNGREEN);
						gc.fillRect((enemyDraw.getYPos()*32)+6, (enemyDraw.getXPos()*32)-6, 20*(enemyDraw.getHealth()/(double)enemyDraw.getOriginalHealth()), 5);
					}

					//if they are under an effect, draw that effect
					if (enemyDraw.getStatus()==EnemyStatus.POISON)
						gc.drawImage(poison, enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
					else if (enemyDraw.getStatus() == EnemyStatus.SLOWDOWN)
						gc.drawImage(slowdown, enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
					else if (enemyDraw.getStatus() == EnemyStatus.FREEZE)
						gc.drawImage(freeze, enemyDraw.getYPos() * 32, enemyDraw.getXPos() * 32);
					
					Tower tower = enemyDraw.isAttackedBy();
					//draw the tower attack explosion on the enemy being attacked
					if (tower != null) {
						if (tower.toString().equals("A"))
							gc.drawImage(normalAttack, enemyDraw.getYPos() * 32-32, enemyDraw.getXPos() * 32-32);
						else if (tower.toString().equals("B"))
							gc.drawImage(magicAttack, enemyDraw.getYPos() * 32-32, enemyDraw.getXPos() * 32-10);
						else
							gc.drawImage(wizardAttack, enemyDraw.getYPos() * 32-10, enemyDraw.getXPos() * 32-10);
					}

				}

			}
		}
	}

	/**
	 * draws our entire map by drawing all the tiles in the GameMap map object's grid and calling drawEnemy() and drawTower()
	 */
	public void drawMap() {
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				Tile[][] grid = map.getGrid();

				//for every tile on the map, draw whats appropriate
				for (int j = 0; j < grid.length; j++) {
					for (int i = 0; i < grid.length; i++) {

						if (grid[i][j].getContent() == null)
							continue;

						if (grid[i][j].getContent().getType() == TileType.EMPTY) {
							//gc.clearRect(j * 32, i * 32, 32, 32);
							gc.drawImage(buildable, (j) * 32, (i) * 32); 
						} 
						else if (grid[i][j].getContent().getType() == TileType.PATH) {
							gc.drawImage(path, (j) * 32, (i) * 32);
						} else if (grid[i][j].getContent() instanceof Tile_Spawn) {
							gc.drawImage(spawner, (j) * 32, (i) * 32, 32, 32);
						} else if (grid[i][j].getContent() instanceof Tile_Destroy) {
							gc.drawImage(end, (j) * 32, (i) * 32, 32, 32);
						}
					}
				}

				drawEnemy();
				drawTower();
			}
		});

	}

	/**
	 * public HBox stat() - builds an HBox for all of the game stats and returns it
	 * 
	 * @return HBox containing all the player stats such as gold, health, score etc.
	 */
	public HBox stat() {
		
		//create the necessary images
		Image goldImage = new Image("images/gold.png");
		ImageView goldIV = new ImageView(goldImage);
		Image enemyImage = new Image("images/Enemy/Skeleton/Skeleton_right_1.png");
		ImageView enemyIV = new ImageView(enemyImage);
		Image scoreImage = new Image("images/score.png");
		ImageView scoreIV = new ImageView(scoreImage);
		Image healthImage = new Image("images/health.png");
		ImageView healthIV = new ImageView(healthImage);
		Image towerImage = new Image("images/Tower/Tower_Normal_level_1.png");
		ImageView towerIV = new ImageView(towerImage);

		//set their ratios
		goldIV.setPreserveRatio(true);
		goldIV.setFitHeight(25);
		enemyIV.setPreserveRatio(true);
		enemyIV.setFitHeight(25);
		scoreIV.setPreserveRatio(true);
		scoreIV.setFitHeight(25);
		healthIV.setPreserveRatio(true);
		healthIV.setFitHeight(25);
		towerIV.setPreserveRatio(true);
		towerIV.setFitHeight(25);

		healthBar = new ProgressBar(player.getHealth() / maxHealth);
		healthBar.setStyle("-fx-accent: #099f09;");

		//creat the text
		goldCount = new Text(Integer.toString(player.getGold()));
		towerCount = new Text(Integer.toString(player.getTowers().size()));
		enemyCount = new Text(Integer.toString(map.getAliveEnemies().size()));
		scoreCount = new Text(Integer.toString(player.getScore()));

		//set the font
		goldCount.setFont(buttonFont);
		towerCount.setFont(buttonFont);
		enemyCount.setFont(buttonFont);
		scoreCount.setFont(buttonFont);

		//set their colors
		goldCount.setStroke(Color.RED);
		towerCount.setStroke(Color.RED);
		enemyCount.setStroke(Color.RED);
		scoreCount.setStroke(Color.RED);

		//create the HBoxes
		HBox playerStats = new HBox(70);
		HBox gold = new HBox(5);
		HBox tower = new HBox(5);
		HBox score = new HBox(5);
		HBox enemy = new HBox(5);
		HBox health = new HBox(5);

		//add them all to their HBoxes before adding them all to one and returning it
		gold.getChildren().addAll(goldIV, goldCount);
		tower.getChildren().addAll(towerIV, towerCount);
		score.getChildren().addAll(scoreIV, scoreCount);
		enemy.getChildren().addAll(enemyIV, enemyCount);
		health.getChildren().addAll(healthIV, healthBar);
		playerStats.getChildren().addAll(gold, tower, enemy, score, health);
		return playerStats;
	}

	/**
	 * creates a VBox for the shop area and returns it
	 * 
	 * @return VBox containing all the tower pictures. Hover over a tower to see the stats.
	 * Drag a tower onto the map to place it given it is a valid location and player has the funds
	 */
	public VBox shop() {

		Label shopHeader = new Label("SHOP");
		shopHeader.setFont(buttonFont);
		shopHeader.setTextFill(Color.RED);

		Tower towerA = new TowerA(map);
		Tower towerB = new TowerB(map);
		Tower towerC = new TowerC(map);
		
		ImageView towerAImage = towerA.getImage();
		ImageView towerBImage = towerB.getImage();
		ImageView towerCImage = towerC.getImage();
		
		towerAImage.setFitHeight(85);
		towerBImage.setFitHeight(85);
		towerCImage.setFitHeight(85);
		
		dragDrop(towerAImage);
		dragDrop(towerBImage);
		dragDrop(towerCImage);

		VBox towers = new VBox();
		towers.getChildren().addAll(shopHeader, towerAImage, towerBImage, towerCImage);

		return towers;
	}

	/**
	 * handles all of the tower dragging and dropping. drag a tower from the VBox on the left onto the map to place it
	 * given player has the funds and the location is valid. Also shows a "ghost" image of the tower whilst dragging
	 * for increased ease of use.
	 * 
	 * @param image the image that is being used for the drag drop ghosting feature
	 */
	public void dragDrop(ImageView image) {
		// handles when player clicks on tower to upgrade and view tower info
		canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {

				if (isShopping) {
					int x = (int) event.getY() / 32;
					int y = (int) event.getX() / 32;
					
					if (x < 17 && y < 17 && map.getGrid()[x][y].getContent() instanceof Tower && !rangeShown) {
						selectedTile = map.getGrid()[x][y];
						Tower tower = (Tower) selectedTile.getContent();
						
						//creates the range circle when clicking on the tower based off of its range
						int range = tower.getRange();
						circle = new Circle(32*y+86, 32*x+56, (range-1)*27+54, Color.RED.deriveColor(1, 1, 1, 0.2));
						borderPane.getChildren().addAll(circle);
						rangeShown = true;
						
						//the upgrade stats and price shown
						String stats = "\n\n" + tower.getStats() + "\nUpgrade $: \n" + tower.getUpgradePrice();
						info = new Text(stats);
						info.setFill(Color.RED);
						upgradeButton = new Button("Upgrade");
						ToggleGroup group = new ToggleGroup();
	
						rb1 = new RadioButton("Range");
						rb1.setToggleGroup(group);
						rb1.setUserData("R");
	
						rb2 = new RadioButton("Speed");
						rb2.setToggleGroup(group);
						rb2.setUserData("S");
	
						rb3 = new RadioButton("Damage");
						rb3.setToggleGroup(group);
						rb3.setUserData("D");
	
						//display that upgrade info
						group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
							public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
								if (group.getSelectedToggle() != null) 
									upgrade = group.getSelectedToggle().getUserData().toString();	
							}
						});
	
						//handle tower upgrading
						upgradeButton.setOnAction(buttonHandler);
						upgradeButton.setFont(buttonFont2);
						info.setStyle("-fx-font-size: 9px; ");
						shop.getChildren().addAll(info, rb1, rb2, rb3, upgradeButton);
	
					//remove the range circle when the tower is not selected
					} else if (rangeShown) {
						rangeShown = false;
						borderPane.getChildren().remove(circle);
						shop.getChildren().removeAll(info, rb1, rb2, rb3, upgradeButton);
					}
				}
			}
		});

		// handles tower drag/dropping image ghosting
		image.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				isShopping = true;
				Dragboard db = image.startDragAndDrop(TransferMode.ANY);

				// setting the mouse pointer image when dragging
				buyingTower = ((LocatedImage) image.getImage()).getURL();
				Image dragImage = new Image(((LocatedImage) image.getImage()).getURL() , 29, 59, true, true);
				if (buyingTower.substring(13).equals("Tower_Normal_level_1.png")) 
					dragImage = new Image(((LocatedImage) image.getImage()).getURL() , 49, 70, true, true);
				else if (buyingTower.substring(13).equals("Tower_Magic_level_1.png")) 
					dragImage = new Image(((LocatedImage) image.getImage()).getURL() , 31, 68, true, true);
				db.setDragView(dragImage);

				ClipboardContent content = new ClipboardContent();
				String s = Character.toString((char) draggable);
				content.put(new DataFormat(s), "asdf");
				db.setContent(content);
				event.consume();
				draggable++;		 
			}
		});

		// when player drags an existing tower to move it
		canvas.setOnDragDetected(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				isShopping = false;
				Dragboard db = canvas.startDragAndDrop(TransferMode.ANY);
				x = (int) event.getY() / 32;
				y = (int) event.getX() / 32;
				if (map.getGrid()[x][y].getContent() instanceof Tower) {
					selectedTile = map.getGrid()[x][y];
					String image = ((LocatedImage) ((Tower) selectedTile.getContent()).getImage().getImage()).getURL();
					Image dragImage = new Image(image , 29, 59, true, true);
					if (buyingTower.substring(13).equals("Tower_Normal_level_1.png")) 
						dragImage = new Image(image, 49, 70, true, true);
					else if (buyingTower.substring(13).equals("Tower_Magic_level_1.png")) 
						dragImage = new Image(image, 31, 68, true, true);
					db.setDragView(dragImage);

					String s = Character.toString((char) draggable);
					ClipboardContent content = new ClipboardContent();
					content.put(new DataFormat(s), "asdf");
					db.setContent(content);
					event.consume();
					draggable++;		 
				}
			}
		});	

		// sets the icon. if buying, there will be a plus sign with the tower image
		canvas.setOnDragOver(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				if (isShopping)
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				else
					event.acceptTransferModes(TransferMode.MOVE);
			}
		});

		// when player drops the dragged image to a tile
		// there are two cases: shopping and moving
		canvas.setOnDragDropped(new EventHandler <DragEvent>() {
			public void handle(DragEvent event) {
				if (!isShopping) {
					newY = (int) event.getX() / 32;
					newX = (int) event.getY() / 32;
					if (!(x==newX && y==newY) && selectedTile != null && map.getGrid()[newX][newY].getContent().getType() == TileType.EMPTY) {
						Tile tile = map.getGrid()[newX][newY];
						Tower tower = (Tower) selectedTile.getContent();
						tower.setTile(tile);							
						tile.setContent(tower);
						selectedTile.setContent(new Tile_Empty());
						drawMap();
					}
				} 

				else {
					int y = (int) event.getX();
					int x = (int) event.getY();

					String imageName = buyingTower.substring(13);
					Tower tower;
					if (imageName.equals("Tower_Normal_level_1.png"))
						tower = new TowerA(map);
					else if (imageName.equals("Tower_Magic_level_1.png"))
						tower = new TowerB(map);
					else // Wizard
						tower = new TowerC(map);

						x /= 32;
						y /= 32;
						if (x >= 0 && y >=0 && x < 17 && y < 17
								&& map.getGrid()[x][y].getContent() != null
								&& map.getGrid()[x][y].getContent().getType() == TileType.EMPTY) {
							Tile tile = map.getGrid()[x][y];
							tower.setTile(tile);
							tile.setContent(player.buyTower(tower));
							drawTower();
						}
					}
				

				event.setDropCompleted(true);
				isShopping = true;
			}
		});

	}

	/**
	 * if the update is instanceof Player, updates the players stats, else if instanceof GameMap updates the map.
	 * then redraws the game. this is ran whenever a model object is modified and the (this) observer is notified.
	 * 
	 * @param o the instance object that is being updated
	 * @param arg unused
	 */
	@Override
	public void update(Observable o, Object arg) {
		//if the model update is player, change those stats
		if (o instanceof Player) {
			player = (Player) o;
			map = player.getMap();
			goldCount.setText(Integer.toString(player.getGold()));
			towerCount.setText(Integer.toString(player.getTowers().size()));
			scoreCount.setText(Integer.toString(player.getScore()));
			enemyCount.setText(Integer.toString(player.getMap().getAliveEnemies().size()));
			healthBar.setProgress(player.getHealth() / maxHealth);
		
		//else change the map
		} else if (o instanceof GameMap) {
			map = (GameMap) o;
			enemyCount.setText(Integer.toString(map.getAliveEnemies().size()));
		}
		drawMap();
	}

	/**
	 * handles all of the game option button clicking by implementing an EventHandler
	 */
	public class ButtonHandler implements EventHandler<ActionEvent> {

		/**
		 * given the player has clicked one of the game option buttons, handles that command accordingly
		 * 
		 * @param e the game option button clicked in the form of an ActionEvent
		 */
		@Override
		public void handle(ActionEvent e) {
			String command = ((Button) e.getSource()).getText();

			if (command.equals("Resume")) {
				isGameRunning = true;
				((Button) e.getSource()).setText("Pause");
			} else if (command.equals("Pause")) {
				isGameRunning = false;
				((Button) e.getSource()).setText("Resume");
			} else if (command.equals("Normal")) {
				((Button) e.getSource()).setText("Fast");
				gameCycleSpeed = 500;	//measured in ms
				fastMode = false;
			} else if (command.equals("Fast")) {
				((Button) e.getSource()).setText("Normal");
				gameCycleSpeed = 250;  //measured in ms
				fastMode = true;
			} else if (command.equals("Save")) {
				try {
					SaverLoader.saveFile(map);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (command.equals("Upgrade")) {
				if (upgrade == null || upgrade.isEmpty())
					return;
				if (upgrade.equals("R"))
					player.upgradeTower((Tower) selectedTile.getContent(), true, false, false);
				else if (upgrade.equals("S"))
					player.upgradeTower((Tower) selectedTile.getContent(), false, false, true);
				else
					player.upgradeTower((Tower) selectedTile.getContent(), false, true, false);
			} else if (command.equals("Start Wave") && !isWaveInProgress && isGameRunning && waveNum < 4) {

				spawnCount = 0;
				waveNum++;

				//timer and timer task for the game loop
				Timer t = new Timer();
				TimerTask tt = new TimerTask(){
					
					/**
					 * for each cycle of the timertask, run one iteration of the game loop
					 */
					@Override
					public void run() {
						doGameLoop(this,t);
					}
				};

				t.scheduleAtFixedRate(tt, 0, 250); //runs at 250ms per tick, but emulated to be 500ms unless fast mode

				isWaveInProgress = false;
				map.setEnemies(new LinkedList<Enemy>());
				t.purge();
			}
		}
	}


	/**
	 * a single iteration of our core game loop using a timertask to run this method, and a timer schedule to 250ms. if the game is set
	 * to normal speed, we always skip every other iteration of the loop to run at 500ms. if the game is paused, we always return. For
	 * every iterations, enemies move once at their speed, towers attack once, dead enemies award score and gold, player loses health for
	 * letting an enemy escape, checks for win/loss conditions etc.
	 * 
	 * @param tt a task that runs this method once to be used by the timer per run
	 * @param timer the timer set to 250ms running the timer task with no delay.
	 */
	private void doGameLoop(TimerTask tt, Timer timer){
		isWaveInProgress = true;

		//so long as the game is over draw the lose gif. the game will never leave this state
		if (gameOver || player.getHealth() <= 0){
			gc.drawImage(youLose, 150, 150);
			gc.setStroke(Color.RED);
			gc.strokeText("YOU LOSE. GAME OVER!", 185, 260);
			return;
		}
		
		// win message
		if (waveNum >= 4 && player.getHealth() > 0){
			gc.setStroke(Color.LAWNGREEN);
			gc.strokeText("YOU SURVIVED ALL ROUNDS!", 185, 260);
			gc.strokeText("YOU WIN!", 185, 280);
			return;
		}

		//skip every other iteration of the game loop, currently running at 250ms
		//to emulate a 500ms game loop for normal speed mode. if fastMode == true,
		//loop runs at 250ms
		if (!fastMode){
			if (skip){
				skip = !skip;
				return;
			}
			skip = !skip;
		}

		//pause the game
		if (!isGameRunning)
			return;

		//handles the amount of enemies to spawn per wave (1+waveNum of each enemy)
		if (waveNum >= spawnCount){
			spawnCount++;
			map.spawn();
		}

		enemyCount.setText(Integer.toString(this.aliveEnemyCount()));

		
		//if all alive enemies are gone, end the wave
		if (map.getAliveEnemies().size() <= 0){
			tt.cancel();
			timer.cancel();
			timer.purge();
			isWaveInProgress = false;
		}

		//for every tower on the map
		for (Tower t : player.getTowers()){
			Enemy deadEnemy = t.attack();
			//if a tower killed something
			if (deadEnemy != null){
				//add the score, the gold and set the enemy to dead
				int score = deadEnemy.getArmor()+deadEnemy.getOriginalHealth()+1;
				System.out.println("Tower "+ t.getTowerType().toString() + " has killed " + deadEnemy.toString()
				+ " for "+ score + " points and " + deadEnemy.giveGold() + " gold");
				player.adjustScore(score);
				player.adjustGold(deadEnemy.giveGold());
				deadEnemy.setStatus(EnemyStatus.DIED);
				//map.getAliveEnemies().remove(deadEnemy);
			}
		}

		LinkedList<Enemy> enemies = (LinkedList<Enemy>) map.getAliveEnemies();
		ListIterator<Enemy> aliveEnemies = enemies.listIterator();
		//while enemies are still alive, move them and handle that move accordingly
		while(aliveEnemies.hasNext()){
			//if the player is dead, end the game
			if (player.getHealth() <= 0) {
				if (player.getHealth() <= 0){
					System.out.println("You lose!");
					gameOver = true;
					//youLost();
				}
				break;
			}
			Enemy enemy = aliveEnemies.next();
			//skip the enemy if it is dead or escaped
			if (!enemy.isAlive())
				continue;
			if (enemy.hasEscaped()) {
				//if it just escaped, handle it accordingly
				player.loseHealth(enemy.getHealth());
				System.out.printf(enemy.toString() + " with " + enemy.getHealth() + " health!\n");
				if (player.getHealth() <= 0){
					System.out.println("You lose!");
					gameOver = true;
				}
				//map.removeEnemy(enemy);
				enemy.setStatus(EnemyStatus.ESCAPED);
			} else if (!enemy.isAlive()) {
				aliveEnemies.remove();
			} else {
				//move the enemy at the appropriate speed
				enemy.move(enemy.currentSpeed());
				//if the enemy is poisoned, take 1 health
				if (enemy.getStatus() == EnemyStatus.POISON){
					enemy.setHealth(enemy.getHealth()-1);
					//if the poison killed it
					if (enemy.getHealth() <= 0){
						enemy.setStatus(EnemyStatus.DIED);
						System.out.println(enemy.toString() + "has died to poison!");
						player.adjustScore(enemy.getArmor()+enemy.getOriginalHealth()+1);
						player.adjustGold(enemy.giveGold());
					}
				}
				if (enemy.hasEscaped()){
					player.loseHealth(enemy.getHealth());
					System.out.printf(enemy.toString() + " with " + enemy.getHealth() + " health!\n");
					enemy.setStatus(EnemyStatus.ESCAPED);
				}
			}

		}

		drawMap();

	}

	/**
	 * helper method to count alive enemies by iterating through each and checking their status
	 */
	private int aliveEnemyCount(){
		int retval = 0;
		try{
			for (Enemy e : map.getEnemies()){
				EnemyStatus s = e.getStatus();
				if (s == EnemyStatus.DIED || s == EnemyStatus.ESCAPED)
					continue;
				else
					retval++;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return retval;
	}
}
