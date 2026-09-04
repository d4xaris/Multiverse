import java.util.Scanner;

public class router {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        
        int x = 0, y = 0;
        int minx = 0, maxx = 0;
        int miny = 0, maxy = 0;
        
        for (int i = 0; i < n; i++) {
            String dir = sc.next();
            int d = sc.nextInt();
            
             switch (dir) {
                case "U":
                    y += d;
                    break;
                case "R":
                    x += d;
                    break;
                case "D":
                    y -= d;
                    break;
                case "L":
                    x -= d;
                    break;
            }
            
            if (x < minx) {
                minx = x;
            }
                
            if (x > maxx) {
                maxx = x;
            }
            
            if (y < miny) {
                miny = y;
            }
            
            if (y > maxy) {
                maxy = y;
            }
            
        }
        
        int w = (maxx - minx) + 40;
        int h = (maxy - miny) + 40;
        
        System.out.println(w + " " + h);
    }   
}