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

// Clase para el jugador
public class Jugador implements Colisionable {
	// Variables del jugador
	private Rectangle player; // Área del jugador
	private Rectangle hitbox; // Área de colisión del jugador
	private static final float HITBOX_SIZE = 8; // Tamaño de la hitbox cuadrada

	private Texture playerImage; // Imagen del jugador
	private Sound sonidoHerido; // Sonido al ser dañado
	
	private int vidas = 1; // Vidas iniciales
	private int puntos = 0; // Puntos iniciales
	private int velX = 400; // Velocidad de movimiento
	private float focusMultiplier = 0.4f; // Multiplicador de velocidad al enfocar

	private boolean herido = false; // Estado de herido
	private int tiempoHeridoMax = 50; // i-frames que dura el estado de herido
	private int tiempoHerido; // Contador de i-frames restantes

    private ShapeRenderer shapeRenderer; // Para dibujar la hitbox en modo debug
    private boolean debugMode = false; // Inicializar modoDebug desactivado, se activa con la tecla D
	   
	public Jugador(Texture tex, Sound ss) {
		playerImage = tex;
		sonidoHerido = ss;
	}

	public Rectangle getArea() {
		return player;
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
		player = new Rectangle();
		player.x = (800 / 2) - (128 / 2);
		player.y = 20;
		player.width = 128;
		player.height = 128;

		// Crear la hitbox ligeramente más pequeña que el jugador
		hitbox = new Rectangle();
		actualizarHitbox();

		// Inicializar el ShapeRenderer
		shapeRenderer = new ShapeRenderer();
	}

	// Lógica del jugador al colisionar con otro objeto
    @Override
    public boolean alColisionar(Colisionable other) {
		// Si colisiona con un proyectil...
        if (other instanceof Proyectil) {
            Proyectil p = (Proyectil) other;
            
            switch (p.getTipo()) {
				// 1: Proyectil normal
                case 1:
                    dañar();
                    break;
				// 2: "Quieto-daño": daña solo si el jugador está QUIETO
                case 2:
                    if (!enMovimiento()) {
                        dañar();
                    }
                    break;
				// 3: "Mov-daño": daña solo si el tarro está EN MOVIMIENTO
                case 3:
                    if (enMovimiento()) {
                        dañar();
                    }
                    break;
				// 4 (ejemplo): Proyectil "bueno"
                case 4:
                default:
                    sumarPuntos(10);
                    break;
            }
        }
		return false; // El jugador nunca debe ser removido después de colisionar
    }

	public void dañar() {
		vidas--;
		herido = true;
		tiempoHerido = tiempoHeridoMax;
		sonidoHerido.play();
	}

	public void dibujar(SpriteBatch batch) {
		if (!herido)  
			batch.draw(playerImage, player.x, player.y);
		else {
			batch.draw(playerImage, player.x, player.y+ MathUtils.random(-5, 5));
			tiempoHerido--;
			if (tiempoHerido <= 0) herido = false;
		}
	}

	public void dibujarHitbox(OrthographicCamera camera) {
		// Mostrar hitbox si estamos en modo debug OR si el jugador está usando SHIFT (movimiento lento)
		if (debugMode || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setColor(1, 0, 0, 1); // Rojo
			shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
			shapeRenderer.end();
		}
	}

	private void actualizarHitbox() {
		hitbox.x = player.x + (player.width - HITBOX_SIZE) / 2 - 16;
		hitbox.y = player.y + (player.height - HITBOX_SIZE) / 2 - 16;
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
		player.x += moveX * velX * mult * Gdx.graphics.getDeltaTime();
		player.y += moveY * velX * mult * Gdx.graphics.getDeltaTime();
		
		// Mantener dentro de los límites de la pantalla
		if(player.x < 0) player.x = 0;
		if(player.x > 800 - player.width) player.x = 800 - player.width;
		if(player.y < 0) player.y = 0;
		if(player.y > 480 - player.height) player.y = 480 - player.height;

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
		playerImage.dispose();
		if (shapeRenderer != null) {
			shapeRenderer.dispose();
			shapeRenderer = null;
		}
	}

	// Métodos públicos para controlar el modo debug desde otras clases
	public void setDebugMode(boolean enabled) {
		this.debugMode = enabled;
	}

	public void toggleDebugMode() {
		this.debugMode = !this.debugMode;
	}

	public boolean isDebugMode() {
		return this.debugMode;
	}
}