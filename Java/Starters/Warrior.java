class Warrior extends Character {
    protected WeaponType weaponType;

    Warrior(String nickname, double hp, double attackPwr, WeaponType weaponType) {
        super(nickname, hp, attackPwr);
        this.weaponType = weaponType;
    }

    Warrior(String nickname, double hp, double attackPwr) {
        super(nickname, hp, attackPwr);
        this.weaponType = WeaponType.Sword;
    }
    @Override
    public void attack() {
        System.out.println(name + " swings their " + weaponType + " for " + (attackPower * 1.5) + " damage!");
    }

    public void shieldBlock() {
        System.out.println(name + " blocks with their shield!");
    }
}
