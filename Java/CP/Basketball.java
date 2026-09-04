import java.util.Scanner;

public class Basketball {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String record = in.next();

        int a = 0;
        int b = 0;

        for (int i = 0; i < record.length(); i += 2) {
            char player = record.charAt(i);
            int points = record.charAt(i + 1) - '0';

            if (player == 'A') {
                a += points;
            } else {
                b += points;
            }
        }

        if (a > b) {
            System.out.println("A");
        } else {
            System.out.println("B");
        }
    }
} 
    
