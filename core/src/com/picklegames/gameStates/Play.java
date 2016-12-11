package com.picklegames.gameStates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.picklegames.entities.Fish;
import com.picklegames.game.FishGame;
import com.picklegames.handlers.B2DVars;
import com.picklegames.handlers.Background;
import com.picklegames.handlers.CreateBox2D;
import com.picklegames.managers.GameStateManager;

// Miguel Garnica
// Dec 9, 2016
public class Play extends GameState {

	private Fish fisho;
	private BitmapFont font;
	private Vector3 mousePos;
	
	private Background bg;
	private int fishtankID;

	public Play(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		fishtankID = 3;
		// load fish
		fisho = new Fish();
		createFishBody();
		
		// load font
		font = new BitmapFont();
		font.setColor(Color.GOLD);
		font.getData().setScale(3);
		
		// load background
		bg = new Background(game.getHudCam());
		TextureRegion texR;
		Texture tex;
		
		FishGame.res.loadTexture("images/background.png", "bg");
		tex = FishGame.res.getTexture("bg");
		texR = new TextureRegion(tex);
		bg.addImage(texR, 0, 0, hudCam.viewportWidth, hudCam.viewportHeight);
		
		FishGame.res.loadTexture("images/Fishtank"+ fishtankID+".png", "fishtank");
		tex = FishGame.res.getTexture("fishtank");
		texR = new TextureRegion(tex);
		bg.addImage(texR, 0, 0, hudCam.viewportWidth, hudCam.viewportHeight);
		
	
		mousePos = new Vector3();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float dt) {
		
		// update mouse position
		mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		cam.unproject(mousePos);
		
		// update fish
		fisho.update(dt);


	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		batch.setProjectionMatrix(hudCam.combined);
		bg.render(batch);
		
		batch.setProjectionMatrix(cam.combined);
		fisho.render(batch);

		if (fisho.isClicked()) {
			font.draw(batch, "STOP!!", fisho.getWorldPosition().x - fisho.getWidth()/2, fisho.getWorldPosition().y + fisho.getHeight());
		}
	}

	public void createFishBody() {

		BodyDef bdef = CreateBox2D.createBodyDef(300, 200, BodyType.DynamicBody);
		Shape shape = CreateBox2D.createCircleShape(fisho.getWidth() / 2);
		FixtureDef fdef = CreateBox2D.createFixtureDef(shape, B2DVars.BIT_PLAYER, B2DVars.BIT_WALL);

		// set body to be fish body
		fisho.setBody(CreateBox2D.createBody(game.getWorld(), bdef, fdef, "fish"));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
