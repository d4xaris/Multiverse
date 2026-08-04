// smallest up, biggest bottom, 6 boxes
// no particular order and no two will be equal
// 6 values box's height, last 2 height two towers
// sum of the box heights will equal the sum of the tower heights
// Output the heights of the three boxes in the first tower + 3 in the second

import java.util.Scanner;

public class towering {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[6];

        for (int i = 0; i < 6; i++) {
            arr[i] = sc.nextInt();
        }

        int tower1 = sc.nextInt();
        int tower2 = sc.nextInt();

        for (int i = 0; i < 6; i++) {
            for (int j = i + 1; j < 6; j++) {
                for (int k = j + 1; k < 6; k++) {
                    int sum = arr[i] + arr[j] + arr[k];

                    if (sum == tower1) {
                        int x = arr[i];
                        int y = arr[j];
                        int z = arr[k];

                        if (x < y) { int temp = x; x = y; y = temp; }
                        if (y < z) { int temp = y; y = z; z = temp; }
                        if (x < y) { int temp = x; x = y; y = temp; }

                        System.out.println(x + " " + y + " " + z);

                        int[] tower2boxes = new int[3];
                        int pos = 0;

                        for (int a = 0; a < 6; a++) {
                            if (a != i && a != j && a != k) {
                                tower2boxes[pos] = arr[a];
                                pos++;
                            }
                        }

                        int p = tower2boxes[0];
                        int q = tower2boxes[1];
                        int r = tower2boxes[2];

                        if (p < q) { int temp = p; p = q; q = temp; }
                        if (q < r) { int temp = q; q = r; r = temp; }
                        if (p < q) { int temp = p; p = q; q = temp; }

                        System.out.println(p + " " + q + " " + r);
                    }
                }
            }
        }
    }
}