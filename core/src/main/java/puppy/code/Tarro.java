package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

// Clase para el tarro que recoge las gotas de lluvia
public class Tarro {
	// Variables del tarro
	private Rectangle bucket; // Área del tarro
	private Rectangle hitbox; // Área de colisión del tarro
	private static final float HITBOX_SIZE = 8; // Tamaño de la hitbox cuadrada

	private Texture bucketImage; // Imagen del tarro
	private Sound sonidoHerido; // Sonido al ser dañado
	
	private int vidas = 3; // Vidas iniciales
	private int puntos = 0; // Puntos iniciales
	private int velX = 400; // Velocidad de movimiento
	private float focusMultiplier = 0.4f; // Multiplicador de velocidad al enfocar

	private boolean herido = false; // Estado de herido
	private int tiempoHeridoMax = 50; // i-frames que dura el estado de herido
	private int tiempoHerido; // Contador de i-frames restantes

    private ShapeRenderer shapeRenderer; // Para dibujar la hitbox en modo debug
    private boolean debugMode = true; // Activar/desactivar modo debug
	   
	public Tarro(Texture tex, Sound ss) {
		bucketImage = tex;
		sonidoHerido = ss;
	}

	public Rectangle getArea() {
		return bucket;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}
	
	public int getVidas() {
		return vidas;
	}

	public int getPuntos() {
		return puntos;
	}

	public void sumarPuntos(int pp) {
		puntos+=pp;
	}

	public boolean estaHerido() {
	   return herido;
    }
	
	public void crear() {
		bucket = new Rectangle();
		bucket.x = (800 / 2) - (64 / 2);
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		// Crear la hitbox ligeramente más pequeña que el tarro
		hitbox = new Rectangle();
		actualizarHitbox();

		// Inicializar el ShapeRenderer
		shapeRenderer = new ShapeRenderer();
	}

	public void dañar() {
		vidas--;
		herido = true;
		tiempoHerido = tiempoHeridoMax;
		sonidoHerido.play();
	}

	public void dibujar(SpriteBatch batch) {
		if (!herido)  
			batch.draw(bucketImage, bucket.x, bucket.y);
		else {
			batch.draw(bucketImage, bucket.x, bucket.y+ MathUtils.random(-5, 5));
			tiempoHerido--;
			if (tiempoHerido <= 0) herido = false;
		}
	}

	public void dibujarHitbox(OrthographicCamera camera) {
		if (debugMode) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(1, 0, 0, 1); // Rojo
			shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
			shapeRenderer.end();
		}
	}

	private void actualizarHitbox() {
		hitbox.x = bucket.x + (bucket.width - HITBOX_SIZE) / 2;
		hitbox.y = bucket.y + (bucket.height - HITBOX_SIZE) / 2;
		hitbox.width = HITBOX_SIZE;
		hitbox.height = HITBOX_SIZE;
	}
	   
	public void actualizarMovimiento() { 
        float mult = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ||
                     Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) ? focusMultiplier : 1f;

		// Variables para el movimiento
		float moveX = 0;
		float moveY = 0;

		// Movimiento horizontal
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveX -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveX += 1;
		// Movimiento vertical
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveY -= 1;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveY += 1;

		// Normalizar el movimiento diagonal
		if (moveX != 0 && moveY != 0) {
			moveX *= 0.7071f; // 1/sqrt(2)
			moveY *= 0.7071f;
		}

		// Aplicar la velocidad, multiplicador, y el tiempo delta
		bucket.x += moveX * velX * mult * Gdx.graphics.getDeltaTime();
		bucket.y += moveY * velX * mult * Gdx.graphics.getDeltaTime();
		
		// Mantener dentro de los límites de la pantalla
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;
		if(bucket.y < 0) bucket.y = 0;
		if(bucket.y > 480 - 64) bucket.y = 480 - 64;

		// Actualizar la hitbox
		actualizarHitbox();
	}

	public boolean enMovimiento() {
		// El jugador mantiene alguna tecla direccional presionada
		return Gdx.input.isKeyPressed(Input.Keys.LEFT)
			|| Gdx.input.isKeyPressed(Input.Keys.RIGHT)
			|| Gdx.input.isKeyPressed(Input.Keys.UP)
			|| Gdx.input.isKeyPressed(Input.Keys.DOWN);
	}

	public void iniciarInvulnerabilidad(float segundos) {
		herido = true;
		tiempoHerido = (int)(segundos * 60); // Asumiendo 60 FPS
	}

	public void destruir() {
		bucketImage.dispose();
	}
}