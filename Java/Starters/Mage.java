class Mage extends Character {
    protected SpellElement spellElement;

    Mage(String nickname, double hp, double attackPwr, SpellElement spell) {
        super(nickname, hp, attackPwr);
        this.spellElement = spell;
    }

    @Override
    public void attack() {
        System.out.println(name + " attacks for " + attackPower + " damage, using " + spellElement);
    }

    public void castHeal() {
        if (health >= 100) {
            System.out.println("Cannot heal!");
        } else {
            double healed = Math.min(health + 30, 100) - health;
            health += healed;
            System.out.println(name + " has been healed for " + healed + " HP! Current HP: " + health);
        }
    }
}