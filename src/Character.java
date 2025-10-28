import java.awt.Image;

public abstract class Character {

    protected String name;
    protected int maxHp, currentHp, atkPower, def;
    protected Image pImage;
    protected Skill Skill;

    public Character(String name, int maxHp, int currentHp, int atkPower, int def, Image pImage) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = currentHp;
        this.atkPower = atkPower;
        this.def = def;
        this.pImage = pImage;
        this.Skill = null;
    }

    public void takedamage(int damage) {
        int actualDamage = Math.max(0, damage - def);
        currentHp = Math.max(0, currentHp - actualDamage);
    }

    public void heal(int amountHeal) {
        currentHp = Math.min(maxHp, currentHp + amountHeal);
    }

    public abstract int atkPower();

    public boolean isAlive() {
        return currentHp > 0;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAtkPower() {
        return atkPower;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public Image getImage() {
        return pImage;
    }

    public Skill getSkill() {
        return Skill;
    }

    public void setSkill(Skill skill) {
        this.Skill = skill;
    }

    public int useSkill(Character enemy) {
        if (Skill == null) {
            return 0;
        }
        if (!Skill.isAvailable()) {
            return -1;
        }
        int damage = Skill.useSkill(this, enemy);
        Skill.currenCooldown = Skill.getCooldown();
        return damage;
    }

    public void reduceSkillCooldown() {
        if (Skill != null) {
            Skill.reduceCooldown();
        }
    }

}