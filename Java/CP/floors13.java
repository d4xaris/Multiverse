import java.util.Scanner;

public class floors13 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        
        int a;
        if (n < 13) {
            a = n;
        } else {
            a = n + 1;
        }

        System.out.println(a);
    }
}
