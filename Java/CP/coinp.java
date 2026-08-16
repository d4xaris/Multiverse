// with Scanner, but it's too slow

// import java.util.Scanner;

// public class coinp {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         long t = sc.nextLong();

//         for (int i = 0; i < t; i++) {
//             long a = sc.nextLong();
//             long b = sc.nextLong();

//             if ((a + b) % 3 == 0 && a <= 2 * b && b <= 2 * a) {
//                 System.out.println("YES");
//             } else {
//                 System.out.println("NO");
//             }
//         }
//     }    
// }

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

public class coinp {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer st = new StreamTokenizer(br);

        st.nextToken();

        int n = (int) st.nval;

        for (int i = 0; i < n; i++) {
            st.nextToken();
            long a = (long) st.nval;
            st.nextToken();
            long b = (long) st.nval;

            if ((a + b) % 3 == 0 && a <= 2 * b && b <= 2 * a) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }        
    }  
}