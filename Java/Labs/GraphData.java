import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GraphData {
    public int n1;
    public int n2;
    public int n3;
    public int n4;
    public int n;

    public int[][] directed1;
    public int[][] undirected1;
    public int[][] directed2;

    public int[] degreesUndirected;
    public int[] degreeDirected;
    public int[] outDegrees1;
    public int[] inDegrees1;

    public int[] outDegrees2;
    public int[] inDegrees2;

    public boolean regularUndirected;
    public boolean regularDirected;
    public int regularDegreeUnd;
    public int regularDegreeDir;

    public List<Integer> pendantUndirected;
    public List<Integer> isolatedUndirected;

    public List<Integer> pendantDirected;
    public List<Integer> isolatedDirected;

    public int[][] a2;
    public int[][] a3;

    public List<String> pathsLen2;
    public List<String> pathsLen3;

    public int[][] reachability;
    public int[][] strongMatrix;
    public List<List<Integer>> sccs;
    public int[][] condensation;

    public static GraphData analyzeGraph(int n1, int n2, int n3, int n4) {
        GraphData data = new GraphData();
        data.n1 = n1;
        data.n2 = n2;
        data.n3 = n3;
        data.n4 = n4;

        data.n = 10 + n3;

        long seed = buildSeed(n1, n2, n3, n4);

        Random rng1 = new Random(seed);
        Random rng2 = new Random(seed);

        data.directed1 = generateDirectedMatrix(data.n, calcK1(n3, n4), rng1);
        data.undirected1 = makeUndirectedFromDirected(data.directed1);

        data.directed2 = generateDirectedMatrix(data.n, calcK2(n3, n4), rng2);

        data.degreesUndirected = degreesUndirected(data.undirected1);
        data.outDegrees1 = outDegrees(data.directed1);
        data.inDegrees1 = inDegrees(data.directed1);
        data.degreeDirected = totalDirectedDegrees(data.outDegrees1, data.inDegrees1);

        int[] regUnd = isRegular(data.degreesUndirected);
        data.regularUndirected = regUnd[0] == 1;
        data.regularDegreeUnd = regUnd[1];

        int[] regDir = isRegular(data.degreeDirected);
        data.regularDirected = regDir[0] == 1;
        data.regularDegreeDir = regDir[1];

        List<List<Integer>> undLists = pendantAndIsolatedUndirected(data.degreesUndirected);
        data.pendantUndirected = undLists.get(0);
        data.isolatedUndirected = undLists.get(1);

        List<List<Integer>> dirLists = pendantAndIsolatedDirected(data.outDegrees1, data.inDegrees1);
        data.pendantDirected = dirLists.get(0);
        data.isolatedDirected = dirLists.get(1);

        data.outDegrees2 = outDegrees(data.directed2);
        data.inDegrees2 = inDegrees(data.directed2);

        data.a2 = multiplyMatrices(data.directed2, data.directed2);
        data.a3 = multiplyMatrices(data.a2, data.directed2);

        data.pathsLen2 = findPathsLen2(data.directed2);
        data.pathsLen3 = findPathsLen3(data.directed2);

        data.reachability = transitiveClosure(data.directed2);
        data.strongMatrix = strongConnectivityMatrix(data.reachability);
        data.sccs = stronglyConnectedComponents(data.strongMatrix);
        data.condensation = buildCondensation(data.directed2, data.sccs);

        return data;
    }

    public static int buildSeed(int n1, int n2, int n3, int n4) {
        return n1 * 1000 + n2 * 100 + n3 * 10 + n4;
    }

    public static double calcK1(int n3, int n4) {
        return 1.0 - n3 * 0.01 - n4 * 0.01 - 0.3;
    }

    public static double calcK2(int n3, int n4) {
        return 1.0 - n3 * 0.005 - n4 * 0.005 - 0.27;
    }

    public static int[][] makeMatrix(int n) {
        return new int[n][n];
    }

    public static int[][] copyMatrix(int[][] a) {
        int n = a.length;
        int[][] res = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, res[i], 0, n);
        }
        return res;
    }

    public static int[][] generateDirectedMatrix(int n, double k, Random rng) {
        int[][] a = makeMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double val = rng.nextDouble() * 2.0;
                val *= k;
                a[i][j] = val >= 1.0 ? 1 : 0;
            }
        }
        return a;
    }

    public static int[][] makeUndirectedFromDirected(int[][] a) {
        int n = a.length;
        int[][] res = copyMatrix(a);

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (res[i][j] == 1 || res[j][i] == 1) {
                    res[i][j] = 1;
                    res[j][i] = 1;
                }
            }
        }
        return res;
    }

    public static int[] degreesUndirected(int[][] a) {
        int n = a.length;
        int[] deg = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 1) {
                    if (i == j) {
                        deg[i] += 2;
                    } else {
                        deg[i]++;
                    }
                }
            }
        }
        return deg;
    }

    public static int[] outDegrees(int[][] a) {
        int n = a.length;
        int[] res = new int[n];

        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += a[i][j];
            }
            res[i] = sum;
        }
        return res;
    }

    public static int[] inDegrees(int[][] a) {
        int n = a.length;
        int[] res = new int[n];

        for (int j = 0; j < n; j++) {
            int sum = 0;
            for (int i = 0; i < n; i++) {
                sum += a[i][j];
            }
            res[j] = sum;
        }
        return res;
    }

    public static int[] totalDirectedDegrees(int[] outDeg, int[] inDeg) {
        int n = outDeg.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = outDeg[i] + inDeg[i];
        }
        return res;
    }

    public static int[] isRegular(int[] degrees) {
        if (degrees.length == 0) {
            return new int[]{1, 0};
        }
        int first = degrees[0];
        for (int d : degrees) {
            if (d != first) {
                return new int[]{0, -1};
            }
        }
        return new int[]{1, first};
    }

    public static List<List<Integer>> pendantAndIsolatedUndirected(int[] deg) {
        List<Integer> pendant = new ArrayList<>();
        List<Integer> isolated = new ArrayList<>();

        for (int i = 0; i < deg.length; i++) {
            if (deg[i] == 1) pendant.add(i + 1);
            if (deg[i] == 0) isolated.add(i + 1);
        }

        List<List<Integer>> result = new ArrayList<>();
        result.add(pendant);
        result.add(isolated);
        return result;
    }

    public static List<List<Integer>> pendantAndIsolatedDirected(int[] outDeg, int[] inDeg) {
        List<Integer> pendant = new ArrayList<>();
        List<Integer> isolated = new ArrayList<>();

        for (int i = 0; i < outDeg.length; i++) {
            int total = outDeg[i] + inDeg[i];
            if (total == 1) pendant.add(i + 1);
            if (total == 0) isolated.add(i + 1);
        }

        List<List<Integer>> result = new ArrayList<>();
        result.add(pendant);
        result.add(isolated);
        return result;
    }

    public static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int n = a.length;
        int[][] res = makeMatrix(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += a[i][k] * b[k][j];
                }
                res[i][j] = sum;
            }
        }
        return res;
    }

    public static List<String> findPathsLen2(int[][] a) {
        int n = a.length;
        List<String> paths = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    if (a[i][j] == 1 && a[j][k] == 1) {
                        paths.add((i + 1) + " - " + (j + 1) + " - " + (k + 1));
                    }
                }
            }
        }
        return paths;
    }

    public static List<String> findPathsLen3(int[][] a) {
        int n = a.length;
        List<String> paths = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        if (a[i][j] == 1 && a[j][k] == 1 && a[k][l] == 1) {
                            paths.add((i + 1) + " - " + (j + 1) + " - " + (k + 1) + " - " + (l + 1));
                        }
                    }
                }
            }
        }
        return paths;
    }

    public static int[][] transitiveClosure(int[][] a) {
        int n = a.length;
        int[][] r = copyMatrix(a);

        for (int i = 0; i < n; i++) {
            r[i][i] = 1;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (r[i][j] == 1 || (r[i][k] == 1 && r[k][j] == 1)) {
                        r[i][j] = 1;
                    }
                }
            }
        }

        return r;
    }

    public static int[][] strongConnectivityMatrix(int[][] reach) {
        int n = reach.length;
        int[][] s = makeMatrix(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reach[i][j] == 1 && reach[j][i] == 1) {
                    s[i][j] = 1;
                }
            }
        }

        return s;
    }

    public static List<List<Integer>> stronglyConnectedComponents(int[][] strong) {
        int n = strong.length;
        boolean[] used = new boolean[n];
        List<List<Integer>> comps = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (used[i]) continue;

            List<Integer> comp = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (strong[i][j] == 1) {
                    comp.add(j);
                }
            }

            for (int v : comp) {
                used[v] = true;
            }

            if (!comp.isEmpty()) {
                comps.add(comp);
            }
        }

        return comps;
    }

    public static int[][] buildCondensation(int[][] a, List<List<Integer>> comps) {
        int m = comps.size();
        int[][] cond = makeMatrix(m);

        int[] vertexToComp = new int[a.length];
        Arrays.fill(vertexToComp, -1);

        for (int ci = 0; ci < comps.size(); ci++) {
            for (int v : comps.get(ci)) {
                vertexToComp[v] = ci;
            }
        }

        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (a[i][j] == 1) {
                    int ci = vertexToComp[i];
                    int cj = vertexToComp[j];
                    if (ci != cj) {
                        cond[ci][cj] = 1;
                    }
                }
            }
        }

        return cond;
    }

    public static String matrixToString(int[][] a) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : a) {
            for (int v : row) {
                sb.append(v).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String verticesListString(List<Integer> vertices) {
        if (vertices == null || vertices.isEmpty()) return "none";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < vertices.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(vertices.get(i));
        }
        return sb.toString();
    }

    public static String componentsToString(List<List<Integer>> comps) {
        if (comps == null || comps.isEmpty()) {
            return "Components's missing\n";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < comps.size(); i++) {
            List<Integer> temp = new ArrayList<>();
            for (int v : comps.get(i)) {
                temp.add(v + 1);
            }
            Collections.sort(temp);
            sb.append("Component ").append(i + 1).append(": ").append(temp).append("\n");
        }
        return sb.toString();
    }

    public String buildReport() {
        StringBuilder sb = new StringBuilder();

        sb.append("Group: ").append(10 * n1 + n2).append("\n");
        sb.append("Variant: ").append(10 * n3 + n4).append("\n");
        sb.append("Vertice count: ").append(n).append("\n\n");

        sb.append("FIRST DIGRAPH\n");
        sb.append(matrixToString(directed1)).append("\n");

        sb.append("FIRST UNDIRECTED GRAPH\n");
        sb.append(matrixToString(undirected1)).append("\n");

        sb.append("Degrees of vertices in an undirected graph: \n");
        sb.append(Arrays.toString(degreesUndirected)).append("\n");
        sb.append("Regular: ").append(regularUndirected);
        if (regularUndirected) {
            sb.append(", degree of regularness: ").append(regularDegreeUnd).append("\n");
        } else {
            sb.append("\n");
        }
        sb.append("Pendant vertices: ").append(verticesListString(pendantUndirected)).append("\n");
        sb.append("Isolated vertices: ").append(verticesListString(isolatedUndirected)).append("\n\n");

        sb.append("For digraph:\n");
        sb.append("Output semi-degrees of graph: ").append(Arrays.toString(outDegrees1)).append("\n");
        sb.append("Input semi-degrees of graph:: ").append(Arrays.toString(inDegrees1)).append("\n");
        sb.append("Vertices degrees: ").append(Arrays.toString(degreeDirected)).append("\n");
        sb.append("Regular: ").append(regularDirected);
        if (regularDirected) {
            sb.append(", degree of regularness: ").append(regularDegreeDir).append("\n");
        } else {
            sb.append("\n");
        }
        sb.append("Pendant vertices: ").append(verticesListString(pendantDirected)).append("\n");
        sb.append("Isolated vertices: ").append(verticesListString(isolatedDirected)).append("\n\n");

        sb.append("SECOND DIGRAPH\n");
        sb.append(matrixToString(directed2)).append("\n");

        sb.append("Output semi-degrees of graph: ").append(Arrays.toString(outDegrees2)).append("\n");
        sb.append("Input semi-degrees of graph:: ").append(Arrays.toString(inDegrees2)).append("\n\n");

        sb.append("A^2\n");
        sb.append(matrixToString(a2)).append("\n");

        sb.append("A^3\n");
        sb.append(matrixToString(a3)).append("\n");

        sb.append("PATH LENGTH 2\n");
        if (pathsLen2.isEmpty()) {
            sb.append("Paths' missing\n");
        } else {
            for (String p : pathsLen2) sb.append(p).append("\n");
        }
        sb.append("\n");

        sb.append("PATH LENGTH 3\n");
        if (pathsLen3.isEmpty()) {
            sb.append("Paths' missing\n");
        } else {
            for (String p : pathsLen3) sb.append(p).append("\n");
        }
        sb.append("\n");

        sb.append("REACH MATRIX\n");
        sb.append(matrixToString(reachability)).append("\n");

        sb.append("STRONG CONNECTIVITY MATRIX\n");
        sb.append(matrixToString(strongMatrix)).append("\n");

        sb.append("COMPONENTS OF STRONG CONNECTIVITY\n");
        sb.append(componentsToString(sccs)).append("\n");

        sb.append("CONDENSATION GRAPH\n");
        sb.append(matrixToString(condensation)).append("\n");

        return sb.toString();
    }
}