import java.awt.Image;

public class Ork extends Enemy {
    public Ork(int lavel, Image image) {
        super("Ork", 220 + lavel * 4, 220 + lavel * 4, 40 + lavel * 2, 30 + lavel, lavel, image);
    }

    public int atkPower() {
        return this.atkPower + (int) (Math.random() * 5.0);
    }
}
