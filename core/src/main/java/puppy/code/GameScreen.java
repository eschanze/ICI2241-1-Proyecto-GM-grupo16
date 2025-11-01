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
	private Jugador jugador;
	private ProyectilManager proyectilManager;
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
		font.getData().setScale(2f, 2f); // Tamaño del texto durante GameScreen
	
		// Cargar sónido de daño y el sprite del jugador
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		jugador = new Jugador(new Texture(Gdx.files.internal("knight.png")),hurtSound);
         
		// Cargar imágenes de los proyectiles y soundtrack del nivel
        Texture bulletTex = new Texture(Gdx.files.internal("fire_bullet.png"));
		Texture bullet2Tex = new Texture(Gdx.files.internal("fire_bullet_2.png"));
		Music levelMusic = Gdx.audio.newMusic(Gdx.files.internal("level1_ost.mp3"));
		 
		// Crear el ProyectilManager
		proyectilManager = new ProyectilManager(bulletTex, levelMusic);
		
		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// Inicializar LevelManager
		if (!levelManager.isLoaded()) {
			levelManager.loadLevels(bulletTex, bullet2Tex);
		}
		this.currentLevel = levelManager.getCurrentLevel();

		// Creación de clase jugador
		jugador.crear();
		
		// Creación del ProyectilManager
		proyectilManager.crear(currentLevel);
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
			if (jugador != null) jugador.toggleDebugMode();
		}

		// Limpia la pantalla con color rojo oscuro
		ScreenUtils.clear(0.05f, 0f, 0f, 0.6f);

		// Actualizar matrices de la cámara
		camera.update();

		// Actualizar 
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// Dibujar textos en la parte superior de la pantalla
		font.draw(batch, "Puntos: " + jugador.getPuntos(), 5, 475);
		font.draw(batch, "Vidas: " + jugador.getVidas(), 680, 475);
		font.draw(batch, "HighScore: " + game.getHigherScore(), 470, 475);

		font.draw(batch, String.format("Tiempo: %.2fs", gameTime), 180, 475);
		
		//if (!tarro.estaHerido()) {
		// Movimiento del tarro desde teclado
		jugador.actualizarMovimiento();
		// Caida de la lluvia
		if (!proyectilManager.actualizarMovimiento(jugador)) { // Si devuelve false, el juego ha terminado
			// Actualizar HigherScore
			if (game.getHigherScore() < jugador.getPuntos())
				game.setHigherScore(jugador.getPuntos());
			// Ir a la ventana de fin de juego. Destruir pantalla actual
			game.setScreen(new GameOverScreen(game));
			dispose();
			return;
		}

		// Si el nivel terminó, ir a pantalla de nivel completado
		if (proyectilManager.isLevelComplete()) {
			game.setScreen(new LevelCompleteScreen(game, this, levelManager));
			dispose();
			return;
		}
		//}
		
		jugador.dibujar(batch); // Renderizar el tarro
		proyectilManager.actualizarDibujoProyectiles(batch); // Renderizar los proyectiles
		batch.end();

		// Dibujar hitboxes en modo debug
		jugador.dibujarHitbox(camera);
	}

	@Override
	public void resize(int width, int height) {
		// No hacer nada
	}

	@Override
	public void show() {
	  	proyectilManager.continuar();
	}

	@Override
	public void hide() {
		// No hacer nada
	}

	@Override
	public void pause() {
		proyectilManager.pausar();
		game.setScreen(new PausaScreen(game, this)); 
	}

	@Override
	public void resume() {
		// No hacer nada
	}

	@Override
	public void dispose() {
		// Liberar recursos
      	jugador.destruir();
      	proyectilManager.destruir();
	}
}