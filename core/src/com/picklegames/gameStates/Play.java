package com.picklegames.gameStates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.picklegames.entities.Fish;
import com.picklegames.entities.Food;
import com.picklegames.game.FishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.Boundary;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.handlers.DayNightCycle;
import com.picklegames.handlers.MyContactListener;
import com.picklegames.managers.GameStateManager;

// Miguel Garnica
// Dec 9, 2016
public class Play extends GameState {

	private Fish fisho;
	private BitmapFont font;
	private Vector3 mousePos;
	private MyContactListener cl;

	private Background bg;
	private int fishtankID;

	private DayNightCycle dayNight;
	private float dayNightRotation = 0;
	private Boundary bound;
	private Array<Food> foods;

	public Play(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		bound = new Boundary(50, 50, (int)(Gdx.graphics.getWidth() * .90f), (int)(Gdx.graphics.getHeight() * .60f));

		fishtankID = 1;
		// load fish
		fisho = new Fish();
		createFishBody();
		fisho.setBound(bound);

		// load font
		font = new BitmapFont();
		font.setColor(Color.GOLD);
		font.getData().setScale(3);

		// load background
		bg = new Background(game.getHudCam());
		TextureRegion texR;
		Texture tex;

		// load layer 1
		FishGame.res.loadTexture("images/background.png", "bg");
		tex = FishGame.res.getTexture("bg");
		texR = new TextureRegion(tex);
		bg.addImage(texR, 0, 0, hudCam.viewportWidth, hudCam.viewportHeight);

		// load layer 2
		FishGame.res.loadTexture("images/Fishtank" + fishtankID + ".png", "fishtank");
		tex = FishGame.res.getTexture("fishtank");
		texR = new TextureRegion(tex);
		bg.addImage(texR, 0, 0, hudCam.viewportWidth, hudCam.viewportHeight);

		// load food
		foods = new Array<Food>();

		// load and set world contact listener
		cl = new MyContactListener();
		game.getWorld().setContactListener(cl);

		batch.begin();
		dayNight = new DayNightCycle(20, 180, batch);
		batch.end();

		mousePos = new Vector3();

		CreateBox2D.createBoxBoundary(game.getWorld(), new Vector2(10, 10), 925, 400, B2DVars.BIT_WALL,
				B2DVars.BIT_PLAYER);
	}

	@Override
	public void handleInput() {
		System.out.println(bound);
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {

		}

	}

	float timeElapsed = 0;

	@Override
	public void update(float dt) {
		timeElapsed += dt;

		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mousePos);
		// System.out.println(mousePos);
		this.handleInput();
		// update mouse position

		// update fish
		fisho.update(dt);

		// dirty
		Array<Body> bodies = cl.getBodiesToRemove();
		for (int i = 0; i < bodies.size; i++) {
			Body b = bodies.get(i);
			foods.removeValue((Food) b.getUserData(), true);
			game.getWorld().destroyBody(b);
		}
		bodies.clear();

		// update food
		for (Food f : foods) {
			f.update(dt);
		}
		
		if(timeElapsed > .75f){
			if(foods.size < 20){
				createFood();
			}
			timeElapsed = 0;
		}

		// update cycle rotation
		dayNightRotation += 0.05f;

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		batch.setProjectionMatrix(hudCam.combined);
		dayNight.renderCycle(dayNightRotation);
		bg.render(batch);

		batch.setProjectionMatrix(cam.combined);
		// render food
		for (Food f : foods) {
			f.render(batch);
		}

		// render fish
		fisho.render(batch);

		if (fisho.isClicked()) {
			font.draw(batch, "STOP!!", fisho.getWorldPosition().x - fisho.getWidth() / 2,
					fisho.getWorldPosition().y + fisho.getHeight());
		}
	}

	public void createFishBody() {

		BodyDef bdef = CreateBox2D.createBodyDef(300, 200, BodyType.DynamicBody);
		Shape shape = CreateBox2D.createCircleShape(fisho.getWidth() / 2);
		FixtureDef fdef = CreateBox2D.createFixtureDef(shape, B2DVars.BIT_PLAYER, B2DVars.BIT_WALL);
		fdef.filter.maskBits = B2DVars.BIT_WALL | B2DVars.BIT_FOOD;

		// set body to be fish body
		fisho.setBody(CreateBox2D.createBody(game.getWorld(), bdef, fdef, "fish"));
	}

	public void createFood() {

		BodyDef bdef;
		Shape shape;
		FixtureDef fdef;
		Food f = new Food();
		bdef = CreateBox2D.createBodyDef((float) (Math.random() * 800) + 20, (float) ( Math.random() * 100 )+ 300,
				BodyType.DynamicBody);
		shape = CreateBox2D.createCircleShape(f.getWidth() / 2);
		fdef = CreateBox2D.createFixtureDef(shape, B2DVars.BIT_FOOD, B2DVars.BIT_PLAYER);
		fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_WALL;
		f.setBody(CreateBox2D.createBody(game.getWorld(), bdef, fdef, "food"));
		f.getBody().setUserData(f);
		foods.add(f);

	}

	@Override
	public void dispose() {

	}

}
