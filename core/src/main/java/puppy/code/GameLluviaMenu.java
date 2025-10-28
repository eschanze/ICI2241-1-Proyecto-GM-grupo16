package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameLluviaMenu extends Game {

	private SpriteBatch batch;
	private BitmapFont font;
	private int higherScore;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // Usar la fuente por defecto de LibGDX (Arial)
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

	public int getHigherScore() {
		return higherScore;
	}

	public void setHigherScore(int higherScore) {
		this.higherScore = higherScore;
	}
}