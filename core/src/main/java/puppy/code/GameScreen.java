package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
	private SpriteBatch batch;
	private BitmapFont font;
	private Tarro tarro;
	private Lluvia lluvia;
	   
	// boolean activo = true;

	public GameScreen(final GameLluviaMenu game) {

		this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
	
		// Cargar imagen y sonido para la gota y el tarro, 64x64 píxeles cada uno  
		Sound hurtSound = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
		tarro = new Tarro(new Texture(Gdx.files.internal("bucket.png")),hurtSound);
         
		// Cargar sonidos e imágenes de la gota y "sonido de fondo" de la lluvía
        Texture gota = new Texture(Gdx.files.internal("drop.png"));
        Texture gotaMala = new Texture(Gdx.files.internal("dropBad.png"));
         
        Sound dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        
		Music rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		lluvia = new Lluvia(gota, gotaMala, dropSound, rainMusic);
		
		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		// Creación del tarro
		tarro.crear();
		
		// Creación de la lluvia
		lluvia.crear();
	}

	@Override
	public void render(float delta) {

		// Limpia la pantalla con color azul oscuro
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// Actualizar matrices de la cámara
		camera.update();

		// Actualizar 
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		// Dibujar textos en la parte superior de la pantalla
		font.draw(batch, "Gotas totales: " + tarro.getPuntos(), 5, 475);
		font.draw(batch, "Vidas: " + tarro.getVidas(), 670, 475);
		font.draw(batch, "HighScore: " + game.getHigherScore(), camera.viewportWidth/2-50, 475);
		
		if (!tarro.estaHerido()) {
			// Movimiento del tarro desde teclado
	        tarro.actualizarMovimiento();        
			// Caida de la lluvia
	        if (!lluvia.actualizarMovimiento(tarro)) {
	    	  	// Actualizar HigherScore
	    	  	if (game.getHigherScore() < tarro.getPuntos())
	    			game.setHigherScore(tarro.getPuntos());
	    	  	// Ir a la ventana de fin de juego. Destruir pantalla actual
				game.setScreen(new GameOverScreen(game));
				dispose();
	       	}
		}
		
		tarro.dibujar(batch);
		lluvia.actualizarDibujoLluvia(batch);
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