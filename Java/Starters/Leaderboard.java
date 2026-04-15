import java.util.ArrayList;
import java.util.Arrays;

class Leaderboard {
    public static void main(String[] args) {
        String[] podium = {"Alpha", "Bravo", "Charlie"};
        ArrayList<String> challangers = new ArrayList<>();

        challangers.add("Delta");
        challangers.add("Echo");
        challangers.add("Foxtrot");

        podium[1] = null;
        challangers.remove(0);

        podium[1] = challangers.get(0);
        challangers.add(0, "Ghost");
        
        String swap = podium[2];
        podium[2] = challangers.get(2);
        challangers.set(2, swap);
        
        System.out.println("Challangers size: " + challangers.size());
        System.out.println("The winner: " + podium[0]);

        System.out.println("Full Podium: " + Arrays.toString(podium));
        System.out.println("Full Challengers: " + challangers);
    
  }
}

