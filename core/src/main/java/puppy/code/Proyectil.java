package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Proyectil {
    private Rectangle area;
	private Rectangle hitbox; // Área de colisión del proyectil
	private static final float HITBOX_SIZE = 8; // Tamaño de la hitbox cuadrada
    public float velocidadX;
    public float velocidadY;
    public int tipo; // 1 = dañino, 2 = bueno
    public Texture textura;
    
    // Constructor
    public Proyectil(float x, float y, float vx, float vy, int tipo, Texture tex) {
        // Area es para dibujar el sprite (usa el tamaño de la textura)
        area = new Rectangle(x, y, tex.getWidth(), tex.getHeight());
        
        // Hitbox más pequeña y centrada en el sprite
        float offsetX = (tex.getWidth() - HITBOX_SIZE) / 2;
        float offsetY = (tex.getHeight() - HITBOX_SIZE) / 2;
        hitbox = new Rectangle(x + offsetX, y + offsetY, HITBOX_SIZE, HITBOX_SIZE);
        
        // Velocidades, tipo, y textura
        velocidadX = vx;
        velocidadY = vy;
        this.tipo = tipo;
        textura = tex;
    }
    
    public Rectangle getArea() {
        return area;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getTipo() {
        return tipo;
    }

    // Función auxiliar para obtener la rotación en grados basada en la velocidad
    // La usamos para rotar el sprite del proyectil al dibujarlo, basado en su dirección de movimiento
    public float getRotationDeg() {
        return MathUtils.atan2(velocidadY, velocidadX) * MathUtils.radiansToDegrees;
    }

    // Actualizar la posición del proyectil
    public void actualizar(float delta) {
        float dx = velocidadX * delta;
        float dy = velocidadY * delta;
        
        area.x += dx;
        area.y += dy;
        hitbox.x += dx;
        hitbox.y += dy;
    }
    
    // Verificar si el proyectil está fuera de la pantalla
    public boolean fueraDePantalla() {
        return area.x < -32 || area.x > 832 || area.y < -32 || area.y > 512;
    }
}