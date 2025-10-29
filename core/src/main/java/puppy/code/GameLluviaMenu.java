package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameLluviaMenu extends Game {

	private SpriteBatch batch;
	private BitmapFont font;
	private int higherScore; // Variable global para almacenar la puntuación más alta
	LevelManager levelManager;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // Usar la fuente por defecto de LibGDX (Arial)
		// Inicializar sistema de niveles
		levelManager = new LevelManager();
		// Ir a la pantalla del menú principal
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // Importante para que se renderice la pantalla actual
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public int getHigherScore() {
		return higherScore;
	}

	public void setHigherScore(int higherScore) {
		this.higherScore = higherScore;
	}
}