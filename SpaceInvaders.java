
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;

public class SpaceInvaders extends Application {

	// creates our list of aliens (using list because there are multiple aliens)
	// also creating our player and the projectiles of both the player and aliens

	private ArrayList<ImageView> aliens;

	private ImageView player;

	private ArrayList<Circle> alienProjs;

	private ArrayList<Circle> playerProjs;

	// ints to store numLives and score
	private int numLives;
	private int score;

	// text to display numLives and score
	private Text lives;
	private Text points;

	// pane, timer, and timeline
	private Pane root;
	private AnimationTimer timer;
	private Timeline timeline;

	// speed of aliens
	private double speed;
	
	private double velX;
	
	// placeMarkers will be used to determine which way the aliens need to move
	// based on their orientation around the placeMarker circle
	private Circle placeMarker;
	
	// boolean that checks if the aliens are to the right or left of the
	// placeMarkers
	private boolean isRightOfMarker;

	public SpaceInvaders() {

		// initializing aliens and projectiles
		aliens = new ArrayList<ImageView>();
		alienProjs = new ArrayList<Circle>();
		playerProjs = new ArrayList<Circle>();

		// initializing pane
		root = new Pane();

		// adding aliens to list
		addAliens();

		// initializing and adding player to scene
		player = player(225, 650);
		root.getChildren().add(player);

		// initializing text and ints and adding them to scene
		numLives = 1;
		score = 0;
		
		velX = 0;

		// setting fonts, etc for lives and points
		lives = new Text("1 life left!");
		lives.setLayoutX(30);
		lives.setLayoutY(30);
		lives.setFont(Font.font(STYLESHEET_CASPIAN, 20));
		lives.setFill(Color.WHITE);

		points = new Text("Score: 0");
		points.setLayoutX(400);
		points.setLayoutY(30);
		points.setFont(Font.font(STYLESHEET_CASPIAN, 20));
		points.setFill(Color.WHITE);

		// adding lives and points to root pane
		root.getChildren().addAll(lives, points);

		// initializes middleOfScene and sets its position
		placeMarker = new Circle();
		placeMarker.setLayoutX(0);

		// initializes isRightOfMarker to true
		isRightOfMarker = true;
	}

	@Override
	public void start(Stage stage) throws Exception {

		// creates game
		SpaceInvaders game = new SpaceInvaders();

		// initializing the timer

		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {

				// call to updateGame
				player.setLayoutX(player.getLayoutX() + velX);
				updateGame();
				
			}

		};

		timer.start();

		// timeline that will make the aliens shoot after a specific duration

		timeline = new Timeline(new KeyFrame(Duration.seconds(0.35), event -> {
			if (!aliens.isEmpty()) {
				aliensShoot();

			}
		}));

		// since the animation must repeat forever, I used the indefinite property
		timeline.setCycleCount(Animation.INDEFINITE);
		// start timeline
		timeline.play();

		// creating scene
		Scene scene = new Scene(root, 500, 700);
		scene.setFill(Color.BLACK);

		// handler to handle player movement
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				// if the game is over then don't let the user move
				if (numLives <= 0 || aliens.isEmpty()) {
					return;
				}
				
				// if left key is pressed
				if (event.getCode() == KeyCode.LEFT) {
					
					// handling out of bounds, ensures that user cannot move out of map
					if (player.getLayoutX() <= 0) {

						setVelX(0);

					} 
					
					else {
						
						setVelX(-5);
						
						
					}

				}

				// if right key is pressed
				if (event.getCode() == KeyCode.RIGHT) {

					// bounds handling again
					if (player.getLayoutX() >= 450) {
						
						setVelX(0);
						
					}

					else {
						
						setVelX(5);
						
					}

				}
			}

		});
		
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				
				// if the game is over then don't let the user move
				if (numLives <= 0 || aliens.isEmpty()) {
					return;
				}
				
				// if user clicks space then shoot
				if (event.getCode() == KeyCode.SPACE) {

					playerShoot(player.getLayoutX());

				}
				
				// if left key is pressed
				if (event.getCode() == KeyCode.LEFT) {
					
					// handling out of bounds, ensures that user cannot move out of map
					if (player.getLayoutX() <= 0) {

						setVelX(0);
					} 
					
					else {
						
						setVelX(0);
						
						
					}

				}

				// if right key is pressed
				if (event.getCode() == KeyCode.RIGHT) {

					// bounds handling again
					if (player.getLayoutX() >= 450) {
						
						setVelX(0);
						
					}

					else {
						
						setVelX(0);
						
					}

				}
				
				
			}

		});
		
		stage.setScene(scene);
		stage.setTitle("Space Invaders");
		stage.show();

	}

	public static void main(String[] args) {

		launch();

	}

	// updates the game by calling all our helper methods

	public void updateGame() {

		updatePlayerShoot();
		updateAliensShoot();
		isPlayerDestroyed();
		isAlienDestroyed();
		moveAliens();
		hasPlayerWon();

	}

	// adds aliens to list of aliens
	public void addAliens() {

		// adding our aliens. There will be 18 of them
		// and they will be evenly split into 3 equal rows of 6. 
		
		for (int i = 0, x = 40; i < 6; i++, x += 70) {
			
			aliens.add(alien(x, 50));
			root.getChildren().add((Node) aliens.get(i));
			
		}
		
		for (int i = 0, x = 40; i < 6; i++, x += 70) {
			
			aliens.add(alien(x, 120));
			root.getChildren().add((Node) aliens.get(i + 6));
			
		}
		
		for (int i = 0, x = 40; i < 6; i++, x += 70) {
			
			aliens.add(alien(x, 190));
			root.getChildren().add((Node) aliens.get(i + 12));
			
		}

	}

	public void playerShoot(double x) {

		// create our projectile to be added
		Circle projectile = projectile((x + 25), 650);

		// add the projectile to our list of player projectiles
		playerProjs.add(projectile);

		// add the projectile to the scene. Since whenever you add something to a list,
		// it always appears at the end, if got the last element in the list and added
		// it,
		// which ensures that I am always adding the most recent player projectile and
		// not adding
		// an older one
		root.getChildren().add(playerProjs.get(playerProjs.size() - 1));

	}

	// method that generates a random num between min and max (inclusive)
	public int rand(int min, int max) {
		
		return (int) (Math.random() * max + min);
		
	}

	public void aliensShoot() {
		
		int getRandomShootingAlienIndex = rand(0, aliens.size() - 1);
		
		alienProjs.add(projectile(aliens.get(getRandomShootingAlienIndex).getLayoutX() + 25,
				aliens.get(getRandomShootingAlienIndex).getLayoutY() + 25));
		
		root.getChildren().add((Node) alienProjs.get(alienProjs.size() - 1));
		
	}

	public void updatePlayerShoot() {

		if (!(playerProjs.isEmpty())) {

			for (int i = 0; i < playerProjs.size(); i++) {

				// moves the location of the player projectile up by 3 (towards the aliens)
				playerProjs.get(i).setLayoutY(playerProjs.get(i).getLayoutY() - 3);

				// if the player projectile is off screen (y coordinate is less than zero)
				// then remove that projectile from both the scene and the list of player projs
				if (playerProjs.get(i).getLayoutY() <= 0) {

					root.getChildren().remove(playerProjs.get(i));
					playerProjs.remove(i);

				}

			}

		}
	}

	public void updateAliensShoot() {

		if (!(alienProjs.isEmpty())) {

			for (int i = 0; i < alienProjs.size(); i++) {

				// moves the location of the alien projectile down by 3 (towards the player)
				alienProjs.get(i).setLayoutY(alienProjs.get(i).getLayoutY() + 3);

				// if the alien projectile is off screen (y coordinate is greater than or equal
				// to scene size)
				// then remove that projectile from both the scene and the list of alien projs
				if (alienProjs.get(i).getLayoutY() <= 0) {

					root.getChildren().remove(alienProjs.get(i));
					alienProjs.remove(i);

				}

			}

		}

	}

	public void isAlienDestroyed() {

		for (int i = 0; i < playerProjs.size(); i++) {

			for (int j = 0; j < aliens.size(); j++) {

				// checks if the player projectile collided with alien using position values
				// using the value 50 because that is the width and height of both the aliens
				// and the player

				boolean collisionCheck = (playerProjs.get(i).getLayoutX() > aliens.get(j).getLayoutX())
						&& (playerProjs.get(i).getLayoutX() < aliens.get(j).getLayoutX() + 50)
						&& (playerProjs.get(i).getLayoutY() > aliens.get(j).getLayoutY())
						&& (playerProjs.get(i).getLayoutY() < aliens.get(j).getLayoutY() + 50);

				// if it did collide
				if (collisionCheck) {

					// remove the player projectile that collided with the alien and the alien
					// itself from the scene
					root.getChildren().removeAll(playerProjs.get(i), aliens.get(j));

					// remove the player projectile and alien from their respective lists
					playerProjs.remove(i);
					aliens.remove(j);

					score += 100;

					points.setText("Score: " + String.valueOf(score));

				}

			}
		}

	}

	// same logic as isAlienDestroyed however we now check if the alien projectile
	// collided with the player
	public void isPlayerDestroyed() {

		for (int i = 0; i < alienProjs.size(); i++) {

			boolean collisionCheck = (alienProjs.get(i).getLayoutX() > player.getLayoutX())
					&& (alienProjs.get(i).getLayoutX() < player.getLayoutX() + 50)
					&& (alienProjs.get(i).getLayoutY() > player.getLayoutY())
					&& (alienProjs.get(i).getLayoutY() < player.getLayoutY() + 50);

			if (collisionCheck) {

				// decrement numLives by 1
				numLives = numLives - 1;

				// reset player location back to middle
				player.setLayoutX(225);

				// set the text to new numLives value
				lives.setText(String.valueOf(numLives) + " lives left!");
			}

		}

	}

	public void moveAliens() {

		// code to end game if aliens touch bottom of screen
		
		boolean isTouching = false;
		
		for (int i = 0; i < aliens.size(); i++) {
			
			// if the alien is touching the y- coordinate scene boundary
			if (aliens.get(i).getLayoutY() >= 650) {
				
				isTouching = true;
				break;
			}
			
		}
		
		// if it is touching
		
		if (isTouching) {
			
			// set lives to 0, update text and call hasPlayerWon
			numLives = 0;
			lives.setText(String.valueOf(numLives) + " lives left!");
			hasPlayerWon();
			
		}
		
		// if its to the right then we keep moving the aliens to the right
		if (isRightOfMarker) {
			
			speed = 1.2;
			
		}
		// if its to the left then we keep moving the aliens to the left
		else {
		
			speed = -1.2;
			
		}

		// if its all the way to the left, meaning that we now must start moving the
		// aliens to the right
		// we first move all the aliens closer to the player and then set
		// isRightOfMarker to true so the next time,
		// they will move to the right. This code will ensure that the aliens move in a
		// snake like pattern

		if (placeMarker.getLayoutX() <= -20) {

			for (int i = 0; i < aliens.size(); i++) {

				aliens.get(i).setLayoutY(aliens.get(i).getLayoutY() + 8);

			}

			isRightOfMarker = true;

		}

		if (placeMarker.getLayoutX() >= 40) {

			for (int i = 0; i < aliens.size(); i++) {

				aliens.get(i).setLayoutY(aliens.get(i).getLayoutY() + 8);

			}

			isRightOfMarker = false;

		}

		// moving the aliens horizontally
		for (int i = 0; i < aliens.size(); i++) {

			aliens.get(i).setLayoutX(aliens.get(i).getLayoutX() + speed);

		}

		// updating the placeMarker so the next time this method is called, the aliens
		// move appropriately
		placeMarker.setLayoutX(placeMarker.getLayoutX() + speed);

	}

	// checks if player has won and displays appropriate message
	public void hasPlayerWon() {

		if (aliens.isEmpty()) {

			// display a message that tells user they won

			Text winText = new Text();
			winText.setLayoutX(150);
			winText.setLayoutY(300);
			winText.setFill(Color.GREEN);
			winText.setFont(Font.font(STYLESHEET_CASPIAN, 50));
			winText.setText("YOU WIN!");
			root.getChildren().add(winText);

			// stop timer and timeline
			timer.stop();
			timeline.stop();
			
		}

		if (numLives <= 0) {

			// display a message that tells user they lost

			Text loseText = new Text();
			loseText.setLayoutX(150);
			loseText.setLayoutY(300);
			loseText.setFill(Color.RED);
			loseText.setFont(Font.font(STYLESHEET_CASPIAN, 50));
			loseText.setText("YOU LOST!");
			root.getChildren().add(loseText);

			// stop timer and timeline
			timer.stop();
			timeline.stop();

			
		}

	}

	// method to create a projectile with a specific location based off the
	// parameters
	// passed in

	public Circle projectile(double x, double y) {

		Circle projectile = new Circle();
		projectile.setLayoutX(x);
		projectile.setLayoutY(y);
		projectile.setRadius(3);
		projectile.setFill(Color.YELLOW);
		return projectile;
	}

	// method to create an alien with a specific location based off the parameters
	// passed in

	public ImageView alien(double x, double y) {

		ImageView i = new ImageView(new Image(getClass().getResourceAsStream("alien.png")));
		i.setFitWidth(50);
		i.setFitHeight(50);
		i.setLayoutX(x);
		i.setLayoutY(y);
		return i;

	}

	// method to create a player with a specific location based off the parameters
	// passed in

	public ImageView player(double x, double y) {

		ImageView i = new ImageView(new Image(getClass().getResourceAsStream("spaceship.png")));
		i.setFitWidth(50);
		i.setFitHeight(50);
		i.setLayoutX(x);
		i.setLayoutY(y);
		return i;

	}
	
	public void setVelX(double velX) {
		
		this.velX = velX;
	}
}
