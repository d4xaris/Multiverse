import java.util.*;

public class TraversalData {

    static final int N1 = 5, N2 = 2, N3 = 2, N4 = 9;
    static final int N = 10 + N3;
    static final double K = 1.0 - N3 * 0.01 - N4 * 0.005 - 0.15;

    static final int UNVISITED = 0, FRONTIER = 1, VISITED = 2;

    enum Mode { BFS, DFS }

    int[][] adj;
    Mode mode;
    int startVertex;

    int[] status;
    int[] parent;
    int[] visitOrder;
    int visitIdx;
    List<int[]> treeEdges;
    Deque<int[]> frontier;
    boolean done;

    TraversalData() {
        adj = buildMatrix();
        mode = Mode.BFS;
        printMatrix(adj);
    }

    static int[][] buildMatrix() {
        Random rng = new Random(N1 * 1000 + N2 * 100 + N3 * 10 + N4);
        int[][] a = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = rng.nextDouble() * 2.0 * K >= 1.0 ? 1 : 0;
        return a;
    }

    void reset(Mode m) {
        mode = m;
        status = new int[N];
        parent = new int[N];
        visitOrder = new int[N];
        treeEdges = new ArrayList<>();
        visitIdx = 0;
        done = false;
        frontier = new ArrayDeque<>();
        Arrays.fill(parent, -1);
        Arrays.fill(visitOrder, -1);

        int s = nextUnvisited();
        if (s == -1) {
            done = true;
            startVertex = -1;
        } else {
            startVertex = s + 1;
            push(s, -1);
        }
    }

    String step() {
        if (done) return null;

        while (!frontier.isEmpty()) {
            int[] item = mode == Mode.BFS ? frontier.pollFirst() : frontier.pop();
            int v = item[0], par = item[1];
            if (status[v] == VISITED) continue;

            status[v] = VISITED;
            parent[v] = par;
            visitOrder[v] = ++visitIdx;
            if (par != -1) treeEdges.add(new int[]{par, v});

            if (mode == Mode.BFS) {
                for (int j = 0; j < N; j++)
                    if (adj[v][j] == 1 && status[j] == UNVISITED) push(j, v);
            } else {
                for (int j = N - 1; j >= 0; j--)
                    if (adj[v][j] == 1 && status[j] == UNVISITED) push(j, v);
            }

            return mode + " visit " + (v + 1) + " order=" + visitIdx
                    + (par != -1 ? " parent=" + (par + 1) : " [root]");
        }

        int next = nextUnvisited();
        if (next != -1) {
            push(next, -1);
            return step();
        }

        done = true;
        printResults();
        return mode + " done";
    }

    void push(int v, int par) {
        status[v] = FRONTIER;
        if (mode == Mode.BFS) frontier.addLast(new int[]{v, par});
        else frontier.push(new int[]{v, par});
    }

    int nextUnvisited() {
        for (int i = 0; i < N; i++)
            if (status[i] == UNVISITED && hasOut(i)) return i;
        return -1;
    }

    boolean hasOut(int v) {
        for (int j = 0; j < N; j++) if (adj[v][j] == 1) return true;
        return false;
    }

    int[][] treeMatrix() {
        int[][] t = new int[N][N];
        for (int[] e : treeEdges) t[e[0]][e[1]] = 1;
        return t;
    }

    void printResults() {
        System.out.println("\n" + mode + " done");
        System.out.println("Visit order:");
        for (int i = 0; i < N; i++)
            System.out.println("  v" + (i + 1) + " -> " + (visitOrder[i] == -1 ? "-" : visitOrder[i]));
        System.out.println("Tree matrix:");
        for (int[] row : treeMatrix())
            System.out.println("  " + Arrays.toString(row));
    }

    static void printMatrix(int[][] m) {
        System.out.println("Adir [k=" + K + " N=" + N + "]:");
        for (int[] row : m) System.out.println("  " + Arrays.toString(row));
    }
}
