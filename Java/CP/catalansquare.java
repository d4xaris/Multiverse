import java.math.BigInteger;
import java.util.Scanner;

public class catalansquare {

    public static BigInteger[] catalanNumbers(int n) {
        BigInteger[] catalan = new BigInteger[n + 1];
        catalan[0] = BigInteger.ONE;
        
        for (int k = 0; k < n; k++) {
            BigInteger numerator = catalan[k].multiply(BigInteger.valueOf(2L * (2 * k + 1)));
            catalan[k + 1] = numerator.divide(BigInteger.valueOf(k + 2));
        }
        return catalan;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        BigInteger[] catalan = catalanNumbers(n);

        BigInteger sn = BigInteger.ZERO;
        for (int k = 0; k <= n; k++) {
            sn = sn.add(catalan[k].multiply(catalan[n - k]));
        }

        System.out.println(sn);
    }
}