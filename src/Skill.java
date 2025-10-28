public abstract class Skill {
    protected String name;
    protected String description;
    protected int cooldown;
    protected int currenCooldown;

    public Skill(String name, String description, int cooldown, int currenCooldown) {
        this.name = name;
        this.description = description;
        this.cooldown = cooldown;
        this.currenCooldown = currenCooldown;

    }

    public abstract int useSkill(Character palyer, Character enemy);

    public boolean isAvailable() {
        return currenCooldown == 0;
    }

    public void reduceCooldown() {
        if (currenCooldown > 0) {
            currenCooldown--;
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCurrenCooldown() {
        return currenCooldown;
    }

    public String getInfo() {
        if (isAvailable()) {
            return name + " (Ready to use)";
        } else {
            return name + " (Cooldown: " + currenCooldown + " turns remaining)";
        }

    }
}