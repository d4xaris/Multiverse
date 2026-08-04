// x S6 M8 L12 

import java.util.Scanner;

public class alittleleftoverpizza {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        int S = 0;
        int M = 0;
        int L = 0;

        for (int i = 0; i < n; i++) {
            String size = scan.next();
            int sl = scan.nextInt();


            switch (size) {
                case "S":
                    S += sl;
                    break;
                case "M":
                    M += sl;
                    break;
                default:
                    L += sl;
                    break;
            }

        }   
            int bs = (S + 6 - 1) / 6;
            int bm = (M + 8 - 1) / 8;
            int bl = (L + 12 - 1) / 12;

            int a = bs + bm + bl;

            System.out.println(a);

    }
}
