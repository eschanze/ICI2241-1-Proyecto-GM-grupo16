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
    private final LevelManager levelManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    public LevelCompleteScreen(final GameLluviaMenu game, GameScreen previousScreen, LevelManager levelManager) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.levelManager = levelManager;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Nivel completado!", 250, 300);
        font.draw(batch, "Toca en cualquier lado para continuar al siguiente nivel", 80, 200);
        batch.end();

        if (Gdx.input.isTouched()) {
            // Si hay siguiente nivel, avanzar y crear nueva GameScreen
            if (levelManager.hasNextLevel()) {
                levelManager.advanceLevel();
                game.setScreen(new GameScreen(game, levelManager));
            } else {
                // No hay más niveles: terminar el juego (ir a GameOver)
                // (la pantalla anterior ya debería liberar recursos al cambiar)
                game.setScreen(new GameOverScreen(game));
            }
            dispose();
        }
    }

    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
