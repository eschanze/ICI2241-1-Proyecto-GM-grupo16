package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class LevelCompleteScreen implements Screen {

    private final GameLluviaMenu game;
    private final GameScreen previousScreen;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    public LevelCompleteScreen(final GameLluviaMenu game, GameScreen previousScreen) {
        this.game = game;
        this.previousScreen = previousScreen;
        // this.levelManager = levelManager;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0.7f);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.getData().setScale(3.0f, 3.0f);
        font.draw(batch, "NIVEL COMPLETADO", (camera.viewportWidth / 2f) - 270f, (camera.viewportHeight / 2f) + 70f);
        font.getData().setScale(1f, 1f);
        font.draw(batch, "Toca en cualquier lado para continuar al siguiente nivel", (camera.viewportWidth / 2f) - 270f,
                (camera.viewportHeight / 2f));
        batch.end();

        if (Gdx.input.justTouched()) {
            // Si hay siguiente nivel, avanzar y crear nueva GameScreen
            if (LevelManager.getInstance().hasNextLevel()) {
                LevelManager.getInstance().advanceLevel();
                game.setScreen(new GameScreen(game));
            } else {
                // No hay más niveles: ir al menú principal
                // (la pantalla anterior ya debería liberar recursos al cambiar)
                LevelManager.getInstance().reset();
                game.setScreen(new MainMenuScreen(game));
            }
            dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
