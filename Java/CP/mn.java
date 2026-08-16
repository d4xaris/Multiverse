import java.util.Scanner;

public class mn {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        long as = (long) n * (n + 1) / 2;
        long ss = 0;
        
        for (int i = 0; i < n - 1; i++) {
            ss += sc.nextInt();
        }

        System.out.println(as - ss);
    }
}