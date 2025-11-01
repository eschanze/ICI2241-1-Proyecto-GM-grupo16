package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
	private final GameLluviaMenu game;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
    private boolean waitingForRelease = true; // evita reactivar inmediatamente si el toque causó la pérdida
	private float t = 0f;
 
	public GameOverScreen(final GameLluviaMenu game) {
		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	@Override
	public void render(float delta) {
		t += delta;
		ScreenUtils.clear(0f, 0f, 0f, 1f);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		// Efecto "fade"
		float alpha = 0.8f + 0.2f * (float)Math.sin(t * 1.5f);

		batch.begin();
        font.getData().setScale(3.0f, 3.0f);
        font.setColor(new Color(0.75f, 0f, 0f, alpha));
        font.draw(batch, "MORISTE", (camera.viewportWidth / 2f) - 100f, (camera.viewportHeight / 2f) + 70f);
        font.getData().setScale(2f, 2f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Toca para reiniciar", (camera.viewportWidth / 2f) - 125f, (camera.viewportHeight / 2f));
		batch.end();


		// Evitar que un toque que causó la muerte reinicie inmediatamente:
		if (waitingForRelease) {
			if (!Gdx.input.isTouched()) waitingForRelease = false;
		} else {
			if (Gdx.input.isTouched()) {
				// Reiniciar el nivel actual
				game.setScreen(new GameScreen(game, game.getLevelManager()));
				dispose();
			}
		}
	}

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}