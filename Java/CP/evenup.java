import java.util.Scanner;
import java.util.Stack;

public class evenup {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        Stack<Integer> s = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            int v = sc.nextInt(); 
            int even = v % 2;
        
        if (!s.isEmpty() && s.peek() == even) {
            s.pop();
        } else {
            s.push(even);
        }
        
        } 
        System.out.println(s.size());
    }
}