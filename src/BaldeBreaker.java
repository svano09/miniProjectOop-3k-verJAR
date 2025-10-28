public class BaldeBreaker extends Skill {
    public BaldeBreaker() {
        super("Blade Breaker", "Attack : 300% Debuff + Def Down : 20 % until enemyhp = 0", 3, 0);
    }

    @Override
    public int useSkill(Character player, Character enamy) {
        int baseDamage = player.atkPower();
        int damage = (int) (baseDamage * 3.0);

        if (enamy instanceof Enemy) {
            Enemy enemy = (Enemy) enamy;
            int originalDef = enemy.getDef();
            int defReduction = (int) (originalDef * 0.2);
            enemy.setDef(originalDef - defReduction);

        }
        return damage;
    }

}
