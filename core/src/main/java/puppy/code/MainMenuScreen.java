package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;

    // Música del menú + timer para el efecto "fade"
    private Music menuMusic;
    private float t = 0f;

    public MainMenuScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Cargar el archivo de música de fondo del menú principal
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu_ost.mp3"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(1f);
		menuMusic.play();
    }

    @Override
    public void render(float delta) {
        t += delta;
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Efecto "fade" para el texto del nombre del juego
        float alpha = 0.675f + 0.325f * (float)Math.sin(t * 2.0f);

        // Texto naranja y grande con el nombre del juego
        font.getData().setScale(6.0f, 6.0f);
        font.setColor(new Color(1f, 0.45f, 0.1f, alpha));
        String title = "CINDERFALL";
        // Centrar el nombre
        font.draw(batch, title, (camera.viewportWidth / 2f) - 270f, (camera.viewportHeight / 2f) + 70f);

        // Texto “Toca para continuar”
        font.getData().setScale(1.5f, 1.5f);
        font.setColor(1f, 1f, 1f, 1f);
        font.draw(batch, "Toca para continuar", (camera.viewportWidth / 2f) - 100f, (camera.viewportHeight / 2f) - 10f);

        // Texto con los controles en la esquina superior derecha
        font.getData().setScale(1.0f, 1.0f);
        font.setColor(0.9f, 0.9f, 0.9f, 0.9f);
        font.draw(batch, "P = Pausa   |   D = Debug", camera.viewportWidth - 180f, camera.viewportHeight - 10f);

        batch.end();

        if (Gdx.input.justTouched()) {
            game.getLevelManager().reset(); // Empezar desde el nivel 1
            game.setScreen(new GameScreen(game, game.getLevelManager()));
            dispose();
        }
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void hide() {
        // Detener la música al salir del menú
        if (menuMusic != null) menuMusic.stop();
    }

    @Override
    public void dispose() {
        // Solo hacer dispose de musicMenu. font y batch compartidos con GameLluviaMenú
        if (menuMusic != null) menuMusic.dispose();
    }
}