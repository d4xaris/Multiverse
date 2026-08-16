import java.util.Scanner;

public class iarray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] arr = new long[n];

        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextLong();
        }

        long m = 0;
        long p = arr[0];

        for (int i = 1; i < n; i++) {
            if (arr[i] < p) {
                m += (p - arr[i]);
                arr[i] = p;
            } else {
                p = arr[i];
            }
        }
        System.out.println(m);
    }
}
