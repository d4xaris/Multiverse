import java.util.Scanner;

public class repetitions {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String n = sc.next();

        int max = 1;
        int cur = 1;

        for (int i = 1; i < n.length(); i++) {
            if (n.charAt(i) == n.charAt(i - 1)) {
                cur++;
            } else {
                cur = 1;
            }

            if (cur > max) {
                max = cur;
            }
        }

        System.out.println(max);
    }
}