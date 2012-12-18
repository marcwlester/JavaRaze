package com.logo3.raze;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Raze implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	
	TiledMap tiledMap;
	TileAtlas tileAtlas;
	TileMapRenderer tileMapRenderer;
	
	Vector3 camDirection = new Vector3(1, 1, 0);
	Vector2 maxCamPosition = new Vector2(0, 0);
	
	Input input = new Input();
	
	float timePassed = 0;
	int frames = 0;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		float aspectRatio = (float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight();
		//camera = new OrthographicCamera(1, h/w);
		camera = new OrthographicCamera(100f * aspectRatio, 100f);
		//camera.position.set(50, 0, 10);
		batch = new SpriteBatch();
		
		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		
		tiledMap = TiledLoader.createMap(Gdx.files.internal("data/maps/worldmap.tmx"));
		tileAtlas = new TileAtlas(tiledMap, Gdx.files.internal("data/maps"));
		tileMapRenderer = new TileMapRenderer(tiledMap, tileAtlas, 8, 8);
		
		camera.position.set(tileMapRenderer.getMapWidthUnits() / 2, tileMapRenderer.getMapHeightUnits() / 2, 0);
		maxCamPosition.set(tileMapRenderer.getMapWidthUnits(), tileMapRenderer.getMapHeightUnits());
		
		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		tileAtlas.dispose();
		tileMapRenderer.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		float elapsed = Gdx.graphics.getDeltaTime();
		float scale = timePassed>0.5?1-timePassed / 2:0.5f + timePassed / 2;
		
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
		//sprite.draw(batch);
		
		//batch.end();
		float moveX = 0f;
		float moveY = 0f;
		if (input.buttons[Input.UP]) {
			moveY += 50f * elapsed;
		}
		if (input.buttons[Input.DOWN]) {
			moveY -= 50f * elapsed;
		}
		if (input.buttons[Input.LEFT]) {
			moveX -= 50f * elapsed;
		}
		if (input.buttons[Input.RIGHT]) {
			moveX += 50f * elapsed;
		}
		
		camera.zoom = 5f;
		camera.position.add(moveX, moveY, 0f);
		camera.update();
		tileMapRenderer.render(camera);
		input.tick();
		
		timePassed += elapsed;
		frames++;
		if(timePassed > 1.0f) {
			Gdx.app.log("SpritePerformanceTest2", "fps: " + frames);
			timePassed = 0;
			frames = 0;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
}
