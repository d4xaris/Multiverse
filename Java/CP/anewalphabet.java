import java.util.Scanner;

public class anewalphabet {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine();
        
        String[] tb = {
            "@", "8", "(", "|)", "3", "#", "6", "[-]", "|",
            "_|", "|<", "1", "[]\\/[]", "[]\\[]", "0", "|D",
            "(,)", "|Z", "$", "']['", "|_|", "\\/", "\\/\\/",
            "}{", "`/", "2"
        };

        String r = "";

        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            char l = Character.toLowerCase(c); // ik it flags it as a problem cuz I have a class Character in Starters but idc

            if (l >= 'a' && l <= 'z') {
                r = r + tb[l - 'a'];
            } else {
                r = r + c;
            }
        }

        System.out.println(r);   
    }
}