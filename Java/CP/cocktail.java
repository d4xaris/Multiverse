import java.util.Arrays;
import java.util.Scanner;

public class cocktail {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int t = sc.nextInt();

        int[] d = new int[n];
        for(int i = 0; i < n; i++) {
            d[i] = sc.nextInt();
        }

        Arrays.sort(d);

        boolean pos = true;
        for (int k = 0; k < n; k++) {
            int ti = d[n - 1 - k];
            int req = (n - k - 1) * t;
            if (!(ti > req)) {
                pos = false;
                break;
            }
        }

        System.out.println(pos ? "YES" : "NO");

    }
}