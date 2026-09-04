import java.util.Scanner;

public class jokerjuggling {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String n = in.nextLine();
        String g = in.nextLine();
        g += " ".repeat(Math.max(0, n.length() - g.length()));

        boolean b = false;

        for (int i = 0; i < n.length(); i++) {
                    
            if (n.charAt(i) == g.charAt(i)) {
                b = true;
            }
            if (n.charAt(i) != '*' && n.charAt(i) != g.charAt(i)) {
                System.out.println("no");
                return;
            }
        }

        if (b) {
           System.out.println("yes"); 
        } else {
           System.out.println("no");
        }
    }
}
