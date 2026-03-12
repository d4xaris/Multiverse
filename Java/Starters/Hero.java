public class Hero {
    String heroName;
    double hp;
    int lvl;

    public Hero(String name, double health, int level){
        heroName = name;
        hp = health;
        lvl = level;
    }

    public void takeDamage(int damage){
        hp -= damage;
        
        if (hp <= 0) {
            System.out.println(heroName + " is dead!");
        }
    }

    public void drinkPotion(){
        hp += 20;
    }

    public String levelUp(){
        lvl++;
        return "Congrats! You leveled up! Now your level is " + lvl;
    }

    public void attack(Hero enemy, Hero attacker, int power) {
        if (Math.random() > 0.8) { 
        power *= 2;
        System.out.println("CRITICAL HIT!");
        }

        System.out.println(attacker.heroName + " attacks " + enemy.heroName + " for " + power + " damage!");
        enemy.takeDamage(power);
    }


    public static void main(String[] args) {
        // герої
        Hero myHero = new Hero("Baka", 100, 1);
        Hero boss = new Hero("Dragon", 200, 15);

        // дії
        myHero.takeDamage(99);
        
        while (myHero.lvl < 10) {
           String message = myHero.levelUp(); 
           System.out.println(message);
        }

        System.out.println("Current " + myHero.heroName + "'s " + "health: " + myHero.hp);     
        myHero.drinkPotion();
        System.out.println("After potion: " + myHero.hp); 

        // файт
        myHero.attack(boss, myHero, 50);
        System.out.println(boss.heroName + " HP: " + boss.hp);

        myHero.attack(myHero, boss, 67);
        System.out.println();

    }

}
