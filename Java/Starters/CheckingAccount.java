public class CheckingAccount {
    private double balance;
    private double rewardsBalance;

    public CheckingAccount(double balance) {
        this.balance = balance;
        this.rewardsBalance = 0.0;
    }

    private void calculateRewards(double amount){
        this.rewardsBalance = amount * 0.01;
    }

    public void purchase(double amount) {
        this.balance -= amount;
        this.calculateRewards(amount);
    }

    public void redeemRewards() {
        this.balance += this.rewardsBalance;
        this.rewardsBalance = 0.0;
    }
    public void runPractice() {
        CheckingAccount myAccount = new CheckingAccount(500.0);
        myAccount.purchase(100.0);

        System.out.println("Main Balance after purchase: $" + myAccount.balance);
        System.out.println("Rewards Balance earned: $" + myAccount.rewardsBalance);

        myAccount.redeemRewards();
        System.out.println("Main Balance after redemption: $" + myAccount.balance);
        System.out.println("Rewards Balance after redemption: $" + myAccount.rewardsBalance);

    }

    public static void main(String[] args) {
        CheckingAccount program = new CheckingAccount(0);
        program.runPractice();
    }
}
