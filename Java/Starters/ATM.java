public class ATM{
    public static int totalMoney = 0;
    public static int numATMs = 0;

    public int money;

    public ATM(int inputMoney){
        this.money = inputMoney;
        numATMs += 1;
        totalMoney += inputMoney;
    }

    public void withdrawMoney(int amountToWithdraw){
        if(amountToWithdraw <= this.money){
            this.money -= amountToWithdraw;
            totalMoney -= amountToWithdraw;
        }
    }

    public void depositMoney(int amountToDeposit){
       if(amountToDeposit > 0){
           this.money += amountToDeposit;
           totalMoney += amountToDeposit;
       }
       else {
           System.out.println("You can't deposit that!");
       }
    }

    public static void averageMoney(){
        System.out.println(totalMoney / numATMs);
    }

    public static void main(String[] args){

        System.out.println("Total number of ATMs: " + ATM.numATMs);
        ATM firstATM = new ATM(1000);
        ATM secondATM = new ATM(500);
        System.out.println("Total number of ATMs: " + ATM.numATMs);

        System.out.println("Total amount of money in all ATMs: " + ATM.totalMoney);
        firstATM.withdrawMoney(500);
        secondATM.withdrawMoney(200);
        System.out.println("Total amount of money in all ATMs: " + ATM.totalMoney);

        ATM.averageMoney();
        firstATM.depositMoney(20);
        secondATM.depositMoney(200);
        System.out.println("Total amount of money in all ATMs: " + ATM.totalMoney);
        secondATM.depositMoney(-10);

        ATM thirdATM = new ATM(3000);
        System.out.println("Total number of ATMs: " + ATM.numATMs);
        System.out.println("Total amount of money in all ATMs: " + ATM.totalMoney);
    }
}