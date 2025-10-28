import java.awt.Image;

public class Dragon extends Enemy {

    public Dragon(int level, Image pImage) {
        super("Dragon", 500 + level * 10, 500 + level * 10, 800 + level * 3, 30 + level * 2, level, pImage);

    }

    @Override
    public int atkPower() {
        return atkPower + (int) (Math.random() * 8);
    }
}
