import java.util.Arrays;
import java.util.Scanner;

public class feedingseals {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int buckets = sc.nextInt();
        int capacityVolunteer = sc.nextInt();
        int[] bucketsWeights = new int[buckets];
        for (int i = 0; i < buckets; i++) {
            bucketsWeights[i] = sc.nextInt();
        }

        Arrays.sort(bucketsWeights);
        int low = 0;
        int high = buckets - 1;
        int needVolunteers = 0;

        while (low <= high) {
            if (low == high) {
                needVolunteers++;
                break;
            }

            if (bucketsWeights[low] + bucketsWeights[high] <= capacityVolunteer) {
                low++;
            }
            high--;
            needVolunteers++;

        }

        System.out.println(needVolunteers);
    }
}