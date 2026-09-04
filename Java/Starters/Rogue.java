class Rogue extends Character {
    protected boolean isHidden = false;

    Rogue(String nickname, double hp, double attackPwr) {
        super(nickname, hp, attackPwr);
    }

    @Override
    public void attack() {
        if (isHidden) {
            System.out.println(name + " attacks for " + (attackPower * 2) + " damage!");
            isHidden = false;
        } else {
            System.out.println(name + " attacks for " + attackPower + " damage!");
        }
    }

    public void hide() {
        isHidden = true;
        System.out.println(name + " hides in the shadows...");
    }
}