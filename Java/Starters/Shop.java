import java.util.ArrayList;

public class Shop { 

    public static void main(String[] args) {
        ArrayList<String> goodies = new ArrayList<>();
        String[] toAdd = {"apple", "banana", "cherry", "ant", "mango", "ant", "ant"};

        int i = 0;
        for (i = 0; i < toAdd.length; i++) {
            goodies.add(toAdd[i]);
        }
        
        for (String goodie: goodies) {
            System.out.println(goodie);
        };

        i = 0;
        while (i < goodies.size()) {
            if (goodies.get(i).equals("ant")) {
                goodies.remove(i);
            } else {
                i++;
            }
        };

        System.out.println(goodies);
    }
}