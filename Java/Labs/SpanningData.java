import java.util.*;

public class SpanningData {

    static final int N1 = 5, N2 = 2, N3 = 2, N4 = 9;
    static final int N = 10 + N3;
    static final double K = 1.0 - N3 * 0.01 - N4 * 0.005 - 0.05;

    int[][] adj;
    int[][] undir;
    int[][] W;

    boolean[] inMST;
    List<int[]> mstEdges;
    int totalCost;
    boolean done;

    SpanningData() {
        Random rng1 = new Random(N1 * 1000 + N2 * 100 + N3 * 10 + N4);
        adj = buildDir(rng1);
        undir = buildUndir(adj);

        Random rng2 = new Random(N1 * 1000 + N2 * 100 + N3 * 10 + N4);
        W = buildWeights(rng2);

        printAll();
    }

    static int[][] buildDir(Random rng) {
        int[][] a = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = rng.nextDouble() * 2.0 * K >= 1.0 ? 1 : 0;
        return a;
    }

    static int[][] buildUndir(int[][] a) {
        int[][] u = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                u[i][j] = a[i][j] | a[j][i];
        return u;
    }

    int[][] buildWeights(Random rng) {
        double[][] B = new double[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                B[i][j] = rng.nextDouble() * 2.0;

        int[][] C = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                C[i][j] = (int) Math.ceil(B[i][j] * 100 * undir[i][j]);

        int[][] D = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                D[i][j] = C[i][j] > 0 ? 1 : 0;

        int[][] H = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                H[i][j] = D[i][j] == D[j][i] ? 1 : 0;

        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++) {
                int val = D[i][j] * H[i][j] * C[i][j];
                result[i][j] = val;
                result[j][i] = val;
            }
        return result;
    }

    void reset() {
        inMST = new boolean[N];
        mstEdges = new ArrayList<>();
        totalCost = 0;
        done = false;
        inMST[0] = true;
    }

    String step() {
        if (done) return null;

        int bu = -1, bv = -1, bw = Integer.MAX_VALUE;
        for (int u = 0; u < N; u++) {
            if (!inMST[u]) continue;
            for (int v = 0; v < N; v++) {
                if (inMST[v] || W[u][v] == 0) continue;
                if (W[u][v] < bw) { bw = W[u][v]; bu = u; bv = v; }
            }
        }

        if (bu == -1) {
            done = true;
            return "Prim done  total cost = " + totalCost;
        }

        inMST[bv] = true;
        mstEdges.add(new int[]{bu, bv, bw});
        totalCost += bw;
        return "add " + (bu + 1) + " - " + (bv + 1) + "  w=" + bw + "  total=" + totalCost;
    }

    boolean isMSTEdge(int i, int j) {
        for (int[] e : mstEdges)
            if ((e[0] == i && e[1] == j) || (e[0] == j && e[1] == i)) return true;
        return false;
    }

    void printAll() {
        System.out.println("Adir [k=" + K + " N=" + N + "]:");
        for (int[] row : adj) System.out.println("  " + Arrays.toString(row));
        System.out.println("\nAundir:");
        for (int[] row : undir) System.out.println("  " + Arrays.toString(row));
        System.out.println("\nWeight matrix W:");
        for (int[] row : W) System.out.println("  " + Arrays.toString(row));
    }

    void printResults() {
        System.out.println("\nPrim MST:");
        for (int[] e : mstEdges)
            System.out.println("  " + (e[0] + 1) + " - " + (e[1] + 1) + "  w=" + e[2]);
        System.out.println("Total cost: " + totalCost);
    }
}
