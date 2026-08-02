import java.io.*;
import java.util.*;

public class Warmup {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        StringTokenizer st = new StringTokenizer(br.readLine());

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = Integer.parseInt(st.nextToken());

        Arrays.sort(arr);

        List<Integer> evens = new ArrayList<>();
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : arr) {
            if (x % 2 == 0) evens.add(x);
            freq.put(x, freq.getOrDefault(x, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Sorted: ").append(Arrays.toString(arr)).append("\n");
        sb.append("Evens: ").append(evens).append("\n");
        sb.append("Freq of first element: ").append(freq.get(arr[0]));

        System.out.println(sb.toString());
    }
}