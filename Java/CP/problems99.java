import java.util.Scanner;

public class problems99 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();

        int u = ((a + 1) / 100 + 1) * 100 - 1;  
        int l = u - 100;                     

        if (l > 0 && (a - l) < (u - a)) {
            System.out.println(l);
        } else {
            System.out.println(u);
        }
    }
}