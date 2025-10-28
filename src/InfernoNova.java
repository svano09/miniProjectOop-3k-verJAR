public class InfernoNova extends Skill {
    public InfernoNova() {
        super("Inferno Nova", "Attack : 500% + PlayHp - 10%", 5, 0);
    }

    @Override
    public int useSkill(Character player, Character Enamy) {
        int baseDamage = player.atkPower();
        int damage = (int) (baseDamage * 5.0);

        int hplayer = (int) (player.getCurrentHp() * 0.1);
        player.takedamage(hplayer);

        return damage;
    }
}
