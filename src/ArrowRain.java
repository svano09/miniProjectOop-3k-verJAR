public class ArrowRain extends Skill {
    public ArrowRain() {
        super("Arrow Rain", "Attack : 350% + playerHp + 10%", 4, 0);
    }

    @Override
    public int useSkill(Character player, Character Enemy) {
        int baseDamage = player.atkPower();
        int damage = (int) (baseDamage * 3.5);

        int hppalyer = (int) (player.getCurrentHp() * 0.1);
        player.heal(hppalyer);

        return damage;
    }
}
