import java.awt.Image;

public abstract class Enemy extends Character {
    protected int level;

    public Enemy(String name, int maxHp, int currentHp, int atkPower, int def, int level, Image pImage) {
        super(name, maxHp, currentHp, atkPower, def, pImage);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

}
