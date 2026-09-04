import java.util.ArrayList;

public abstract class Character {
    protected String name;
    protected double health;
    protected double attackPower;

    Character(String nickname, double hp, double attackPwr) {
        this.name = nickname;
        this.health = hp;
        this.attackPower = attackPwr;
    }

    public void attack() {
        System.out.println(name + " attacks for " + attackPower + " damage!");
    }

    public void getStatus() {
        System.out.println("The status is: " + name + ", " + health);
    }

    public static void main(String[] args) {
        Warrior w1 = new Warrior("Baka", 100, 20, WeaponType.Axe);
        Mage m1 = new Mage("Jonny", 100, 25, SpellElement.Shadow);
        Mage m2 = new Mage("Minnie", 55, 30, SpellElement.Light);
        Rogue r1 = new Rogue("Lord", 50,15);

        ArrayList<Character> heroes = new ArrayList<>();
        heroes.add(w1);
        heroes.add(m1);
        heroes.add(m2);
        heroes.add(r1);

        for (Character c : heroes) {
            c.getStatus();
        }

        w1.attack();
        m1.attack();
        r1.attack();
        r1.hide();
        r1.attack();
        w1.shieldBlock();
        m1.castHeal();
        m2.castHeal();
    }
}
