package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PausaScreen implements Screen {

    private final GameLluviaMenu game;
    private GameScreen juego;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    public PausaScreen(final GameLluviaMenu game, GameScreen juego) {
        this.game = game;
        this.juego = juego;
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
        font.getData().setScale(2f, 2f);
        font.setColor(Color.WHITE);
        font.draw(batch, "JUEGO PAUSADO", 100, 340);

        font.getData().setScale(1.2f, 1.2f);
        font.draw(batch, "Controles:", 100, 280);
        font.draw(batch, "Flechitas para moverse", 100, 250);
        font.draw(batch, "SHIFT para modo enfoque (focus)", 100, 220);
        font.draw(batch, "P para pausar/reanudar", 100, 190);
        font.draw(batch, "D para activar/desactivar Debug", 100, 160);

        font.getData().setScale(1.4f, 1.4f);
        font.draw(batch, "Toca en cualquier lado para continuar", 100, 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            // Restaurar scale para el texto en GameScreen
            font.getData().setScale(2f, 2f);
            game.setScreen(juego);
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