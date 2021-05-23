package com.projects.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.Shape;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture background1;
	Texture gameOver;
	Texture base;
	Texture homePage;
	Texture start;



	boolean game;

//    ShapeRenderer shapeRenderer;

	Texture[] birds;
	float birdY = 0;   //5 As only will change while touching.....................
	float velocity = 0; //6 The velocity by which the bird will go down/fall due to gravity.................
	Circle birdCircle;

	int flapState = 0;
	Texture topTube;
	Texture bottomTube;
	int level=1;
    int change=0;

	int gameState = 0;
	float gravity = 10;

	int score = 0;
	int scoringTube = 0;

	float gap = 750;
	float maxTubeOfSet;
	Random randomGenerator;
	float tubeVelocity = 12;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];   // distance between each tube;
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTheTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	BitmapFont font;
	BitmapFont fontScore;
	boolean gameStart;
	boolean command;




	@Override
	public void create() {

		game=true;
		gameStart=true;

		batch = new SpriteBatch();
		background1 = new Texture("view.jpeg");
		background = new Texture("night.jpeg");
		gameOver = new Texture("over.png");
		homePage=new Texture("background.png");
		start=new Texture("s.png");

		base=new Texture("base.png");

		//1 making an array of both birds so that they can appear sequentially to make it as bird is flying...............
		birds = new Texture[2];
//        shapeRenderer=new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		birds[0] = new Texture("birdup.png");
		birds[1] = new Texture("birddown.png");

		font=new BitmapFont();
		fontScore=new BitmapFont();
		font.setColor(Color.PINK);
		fontScore.setColor(Color.PINK);
		font.getData().setScale(5);
		fontScore.getData().setScale(5);
		command=false;



		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		maxTubeOfSet = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTheTubes = Gdx.graphics.getWidth() * 3 / 4;

		startGame();

	}

	public void startGame(){

		birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (625);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() * 3 / 4 + i * distanceBetweenTheTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}
	@Override
	public void render() {





		//2 setting the background and both birds(array) and also specify their positions...............

		batch.begin();
		batch.draw(background1, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
		if(gameStart) {
			batch.draw(homePage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(start,Gdx.graphics.getWidth() / 2 - start.getWidth() / 2, Gdx.graphics.getHeight() / 2 - start.getHeight() / 25);
			if (Gdx.input.justTouched()) {
				gameStart=false;
				command=true;
			}
		}
		if(!game) {
			batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}





		if (gameState == 1) {

				if (tubeX[scoringTube]+topTube.getWidth() < Gdx.graphics.getWidth() / 2) {
					score++;
					change=score%4;
					if(score%4==0){
						level++;
						if(level%2==0){
							game=false;
						}else{
							game=true;
						}
						startGame();
						tubeVelocity=tubeVelocity+4;
						gap=gap-20;
						distanceBetweenTheTubes=distanceBetweenTheTubes-80;
						velocity=velocity+10;


					}

					if (scoringTube < numberOfTubes - 1) {
						scoringTube++;
					} else {
						scoringTube = 0;
					}
				}


				if (Gdx.input.justTouched()) {
					velocity = -50;
				}
				for (int i = 0; i < numberOfTubes; i++) {

					if (tubeX[i] < -topTube.getWidth()) {
						tubeX[i] += numberOfTubes * distanceBetweenTheTubes;
						tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (625);

					} else {
						tubeX[i] = tubeX[i] - tubeVelocity;
					}


					batch.draw(topTube, tubeX[i], (Gdx.graphics.getHeight() / 2 + gap / 5 + tubeOffset[i]));
					batch.draw(bottomTube, tubeX[i], (Gdx.graphics.getHeight() / 2 - 2 * gap) + tubeOffset[i]);

					topTubeRectangles[i] = new Rectangle(tubeX[i], (Gdx.graphics.getHeight() / 2 + gap / 5 + tubeOffset[i]), topTube.getWidth(), topTube.getHeight());
					bottomTubeRectangles[i] = new Rectangle(tubeX[i], (Gdx.graphics.getHeight() / 2 - 2 * gap) + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
				}

				if (birdY > Gdx.graphics.getHeight()/7) {
					velocity = velocity + gravity;
					birdY -= velocity;
				} else {
					gameState = 2;
				}

			} else if (gameState == 0) {
				if (Gdx.input.justTouched()) {
					gameState = 1;

				}
			} else if (gameState == 2) {

			fontScore.draw(batch, "Score:"+String.valueOf(score),  Gdx.graphics.getWidth()/2- gameOver.getWidth() / 4,Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2-50);
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() / 2);
				if (Gdx.input.justTouched()) {
					gameState = 1;
					startGame();
					score = 0;
					velocity = 0;
					scoringTube = 0;
				}

			}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

		if(command) {
			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

				font.draw(batch, "Level:" + String.valueOf(level), 100, Gdx.graphics.getHeight());
			}
		batch.draw(base,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/7 );

		batch.end();

			birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);


			for (int i = 0; i < numberOfTubes; i++) {

				if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
					gameState = 2; // game over
				}
			}


		}



}
