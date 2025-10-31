package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
	final GameLluviaMenu game;
    private OrthographicCamera camera;
	// Variables globales de dibujo
	private SpriteBatch batch;
	private BitmapFont font;
	// Objetos del juego
	private Tarro tarro;
	private Lluvia lluvia;
	// Variables de nivel
	private LevelManager levelManager;
    private Level currentLevel;
    private boolean levelWon;
	// Tiempo de juego
	private float gameTime = 0f;

	public GameScreen(final GameLluviaMenu game, LevelManager levelManager) {

		this.game = game;
		this.levelManager = levelManager;
        this.batch = game.getBatch();
        this.font = game.getFont();
	
		// Cargar imagen y sonido para la gota y el tarro, 64x64 píxeles cada uno  
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		tarro = new Tarro(new Texture(Gdx.files.internal("knight.png")),hurtSound);
         
		// Cargar imágenes de los proyectiles y soundtrack del nivel
        Texture bulletTex = new Texture(Gdx.files.internal("fire_bullet.png"));
		Music levelMusic = Gdx.audio.newMusic(Gdx.files.internal("level1_ost.mp3"));
		 
		// Crear la lluvia
		lluvia = new Lluvia(bulletTex, levelMusic);
		
		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// Inicializar LevelManager
		if (!levelManager.isLoaded()) {
			levelManager.loadLevels(bulletTex);
		}
		this.currentLevel = levelManager.getCurrentLevel();

		// Creación del tarro
		tarro.crear();
		
		// Creación de la lluvia
		lluvia.crear(currentLevel);
	}

	@Override
	public void render(float delta) {
		gameTime += delta;

		// Detectar input de pausa manual (P o ESC)
		if (Gdx.input.isKeyJustPressed(Input.Keys.P) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			// Reusar el método pause() para detener la música y cambiar de pantalla
			pause();
			return;
		}

		// Toggle debug mode (tecla D)
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			if (tarro != null) tarro.toggleDebugMode();
		}

		// Limpia la pantalla con color azul oscuro
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// Actualizar matrices de la cámara
		camera.update();

		// Actualizar 
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// Dibujar textos en la parte superior de la pantalla
		font.draw(batch, "Puntos: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas: " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore: " + game.getHigherScore(), camera.viewportWidth/2-50, 475);

		font.draw(batch, String.format("Time: %.2fs", gameTime), 160, 475);
		
		//if (!tarro.estaHerido()) {
		// Movimiento del tarro desde teclado
		tarro.actualizarMovimiento();
		// Caida de la lluvia
		if (!lluvia.actualizarMovimiento(tarro)) { // Si devuelve false, el juego ha terminado
			// Actualizar HigherScore
			if (game.getHigherScore() < tarro.getPuntos())
				game.setHigherScore(tarro.getPuntos());
			// Ir a la ventana de fin de juego. Destruir pantalla actual
			game.setScreen(new GameOverScreen(game));
			dispose();
			return;
		}

		// Si el nivel terminó, ir a pantalla de nivel completado
		if (lluvia.isLevelComplete()) {
			game.setScreen(new LevelCompleteScreen(game, this, levelManager));
			dispose();
			return;
		}
		//}
		
		tarro.dibujar(batch); // Renderizar el tarro
		lluvia.actualizarDibujoLluvia(batch); // Renderizar los proyectiles de la lluvia
		batch.end();

		// Dibujar hitboxes en modo debug
		tarro.dibujarHitbox(camera);
	}

	@Override
	public void resize(int width, int height) {
		// No hacer nada
	}

	@Override
	public void show() {
	  	// Continuar con sonido de lluvia
	  	lluvia.continuar();
	}

	@Override
	public void hide() {
		// No hacer nada
	}

	@Override
	public void pause() {
		lluvia.pausar();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {
		// No hacer nada
	}

	@Override
	public void dispose() {
		// Liberar recursos
      	tarro.destruir();
      	lluvia.destruir();
	}
}