import java.util.Scanner;

public class lvable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        String n = sc.nextLine();

        if (n.contains("lv")) {
            System.out.println(0);
        } else if (n.indexOf("l") != -1 || n.indexOf("v") != -1) {
            System.out.println(1);
        } else {
            System.out.println(2);
        }

    }
}