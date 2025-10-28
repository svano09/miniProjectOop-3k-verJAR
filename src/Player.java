
import java.awt.Color;
import java.awt.Image;

public class Player extends Character {
    private int potions;

    public Player(String name, int maxHp, int currentHp, int atkPower, int def, Color color, Image pImage) {
        super(name, maxHp, currentHp, atkPower, def, pImage);
        this.potions = 5;
        addsignSkill(name);

    }

    public void addsignSkill(String name) {
        switch (name) {
            case "SWORDMAN":
                this.Skill = new BaldeBreaker();
                break;

            case "MAGE":
                this.Skill = new InfernoNova();
                break;

            case "ARROWMAN":
                this.Skill = new ArrowRain();
                break;

            default:
                this.Skill = null;
                break;
        }

    }

    @Override
    public int atkPower() {
        return atkPower + (int) (Math.random() * 100);
    }

    public boolean usePotions() {
        if (potions > 0 && currentHp < maxHp) {
            potions--;
            heal(150);
            return true;
        }
        return false;
    }

    public int getPotions() {
        return potions;
    }
}