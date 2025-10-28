import java.awt.Image;

public class Goblin extends Enemy {
    public Goblin(int level, Image pImage) {
        super("Goblin", 180 + level * 4, 180 + level * 4, 20 + level * 2, 20 + level, level, pImage);

    }

    @Override
    public int atkPower() {
        return atkPower + (int) (Math.random() * 3);
    }
}