import java.util.Scanner;

public class twok {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        for (long k = 1; k <= n; k++) {
            long t = k * k * (k * k - 1) / 2;
            long a = 4 * (k - 1) * (k - 2);
            System.out.println(t - a);
        }
    }
}