import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lab5 extends Application {

    static final double R = 16;

    TraversalData data;
    Point[] graphPts;
    Pane graphPane, treePane;
    TextArea logArea;
    Button btnStep;
    StringBuilder log = new StringBuilder();

    @Override
    public void start(Stage stage) {
        data = new TraversalData();
        graphPts = makePositions(585, 375);

        graphPane = makePane(585, 375);
        treePane = makePane(585, 375);

        logArea = new TextArea();
        logArea.setPrefSize(1180, 100);
        logArea.setEditable(false);

        Button bBFS = new Button("BFS");
        Button bDFS = new Button("DFS");
        btnStep = new Button("Next Step [Space]");
        Button bReset = new Button("Reset");

        bBFS.setOnAction(e -> init(TraversalData.Mode.BFS));
        bDFS.setOnAction(e -> init(TraversalData.Mode.DFS));
        btnStep.setOnAction(e -> step());
        bReset.setOnAction(e -> init(data.mode));

        HBox buttons = new HBox(10, bBFS, bDFS, btnStep, bReset);
        buttons.setPadding(new Insets(6));
        HBox panes = new HBox(10, graphPane, treePane);
        VBox root = new VBox(6, panes, buttons, logArea);
        root.setPadding(new Insets(8));

        Scene scene = new Scene(root, 1200, 560);
        scene.setOnKeyPressed(e -> { if (e.getCode().toString().equals("SPACE")) step(); });

        stage.setTitle("Lab 5 | IM-52, variant 29");
        stage.setScene(scene);
        stage.show();

        init(TraversalData.Mode.BFS);
    }

    void init(TraversalData.Mode mode) {
        log = new StringBuilder();
        data.reset(mode);
        addLog(mode + " start from vertex " + data.startVertex);
        btnStep.setDisable(data.done);
        redraw();
    }

    void step() {
        if (data.done) return;
        String msg = data.step();
        if (msg != null) addLog(msg);
        if (data.done) {
            addLog("Visit order: " + visitOrderString());
            btnStep.setDisable(true);
        }
        redraw();
    }

    String visitOrderString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TraversalData.N; i++) {
            if (i > 0) sb.append(", ");
            sb.append((i + 1)).append("->").append(data.visitOrder[i] == -1 ? "-" : data.visitOrder[i]);
        }
        return sb.toString();
    }

    void redraw() {
        drawGraph();
        drawTree();
    }

    void drawGraph() {
        graphPane.getChildren().clear();
        int N = TraversalData.N;

        graphPane.getChildren().add(new Text(15, 20,
                data.mode + " - graph [step " + data.visitIdx + "/" + N + "]"));

        Set<String> treeSet = new HashSet<>();
        for (int[] e : data.treeEdges) treeSet.add(e[0] + "_" + e[1]);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (data.adj[i][j] == 1) {
                    boolean tree = treeSet.contains(i + "_" + j);
                    drawEdge(graphPane, graphPts, i, j, true,
                            tree ? Color.ROYALBLUE : Color.LIGHTGRAY,
                            tree ? 2.0 : 0.8);
                }

        for (int i = 0; i < N; i++)
            drawNode(graphPane, graphPts[i], i + 1, nodeColor(data.status[i]));
    }

    void drawTree() {
        treePane.getChildren().clear();
        treePane.getChildren().add(new Text(15, 20, data.mode + " - traversal tree"));

        if (data.treeEdges.isEmpty()) return;

        int N = TraversalData.N;
        Map<Integer, List<Integer>> children = new HashMap<>();
        for (int i = 0; i < N; i++)
            if (data.visitOrder[i] != -1) children.put(i, new ArrayList<>());
        for (int[] e : data.treeEdges)
            children.get(e[0]).add(e[1]);

        List<Integer> roots = new ArrayList<>();
        for (int i = 0; i < N; i++)
            if (data.visitOrder[i] != -1 && data.parent[i] == -1) roots.add(i);
        if (roots.isEmpty()) return;

        Point[] tp = treePositions(roots, children, 585, 375);

        for (int[] e : data.treeEdges)
            if (tp[e[0]] != null && tp[e[1]] != null)
                drawEdge(treePane, tp, e[0], e[1], true, Color.ROYALBLUE, 2.0);

        for (int i = 0; i < N; i++) {
            if (data.visitOrder[i] == -1 || tp[i] == null) continue;
            drawNode(treePane, tp[i], i + 1, nodeColor(data.status[i]));
            Text ord = new Text(tp[i].x + R, tp[i].y - R, String.valueOf(data.visitOrder[i]));
            ord.setFill(Color.CRIMSON);
            treePane.getChildren().add(ord);
        }
    }

    Point[] treePositions(List<Integer> roots, Map<Integer, List<Integer>> children, double W, double H) {
        int N = TraversalData.N;
        Point[] pos = new Point[N];
        List<List<Integer>> levels = new ArrayList<>();
        Queue<Integer> q = new ArrayDeque<>(roots);
        Set<Integer> placed = new HashSet<>(roots);

        while (!q.isEmpty()) {
            int sz = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < sz; i++) {
                int v = q.poll();
                level.add(v);
                for (int c : children.getOrDefault(v, List.of()))
                    if (placed.add(c)) q.add(c);
            }
            levels.add(level);
        }

        double my = 40, usH = H - 2 * my - 30, usW = W - 80;
        double lh = levels.isEmpty() ? 0 : usH / levels.size();

        for (int li = 0; li < levels.size(); li++) {
            List<Integer> lv = levels.get(li);
            double y = my + 30 + li * lh + lh / 2;
            double sw = usW / lv.size();
            for (int ni = 0; ni < lv.size(); ni++)
                pos[lv.get(ni)] = new Point(40 + ni * sw + sw / 2, y);
        }
        return pos;
    }

    static void drawEdge(Pane pane, Point[] pts, int i, int j,
                         boolean directed, Color color, double w) {
        Point a = pts[i], b = pts[j];
        if (a == null || b == null) return;

        if (i == j) {
            Circle loop = new Circle(a.x, a.y - 20, 18);
            loop.setFill(Color.TRANSPARENT);
            loop.setStroke(color);
            loop.setStrokeWidth(w);
            pane.getChildren().add(loop);
            return;
        }

        double offset = (j - i) * 5.0;
        double cx = (a.x + b.x) / 2.0 + offset;
        double cy = (a.y + b.y) / 2.0 - offset;

        pane.getChildren().addAll(line(a.x, a.y, cx, cy, color, w), line(cx, cy, b.x, b.y, color, w));

        if (directed) {
            double dx = b.x - cx, dy = b.y - cy;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len < 1e-9) return;
            double nx = b.x - dx / len * R, ny = b.y - dy / len * R;
            double angle = Math.atan2(ny - cy, nx - cx), al = 12, aa = Math.PI / 6;
            pane.getChildren().addAll(
                    line(nx, ny, nx - al * Math.cos(angle - aa), ny - al * Math.sin(angle - aa), color, w),
                    line(nx, ny, nx - al * Math.cos(angle + aa), ny - al * Math.sin(angle + aa), color, w));
        }
    }

    static void drawNode(Pane pane, Point p, int label, Color fill) {
        Circle c = new Circle(p.x, p.y, R);
        c.setFill(fill);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(1.5);
        Text t = new Text(p.x - (label >= 10 ? 7 : 4), p.y + 5, String.valueOf(label));
        pane.getChildren().addAll(c, t);
    }

    static Line line(double x1, double y1, double x2, double y2, Color c, double w) {
        Line l = new Line(x1, y1, x2, y2);
        l.setStroke(c);
        l.setStrokeWidth(w);
        return l;
    }

    static Color nodeColor(int s) {
        if (s == TraversalData.FRONTIER) return Color.YELLOW;
        if (s == TraversalData.VISITED) return Color.LIGHTGREEN;
        return Color.WHITE;
    }

    static Point[] makePositions(double W, double H) {
        int N = TraversalData.N;
        double rW = W * 0.7, rH = H * 0.72;
        double sX = (W - rW) / 2, sY = (H - rH) / 2 + 14;
        Point[] pts = new Point[N];
        int cnt = N - 1, perSide = (int) Math.ceil(cnt / 4.0);
        for (int i = 0; i < cnt; i++) {
            int side = i / perSide, pos = i % perSide;
            double t = (double) pos / perSide, x, y;
            switch (side) {
                case 0 -> { x = sX + t * rW;      y = sY; }
                case 1 -> { x = sX + rW;           y = sY + t * rH; }
                case 2 -> { x = sX + rW - t * rW; y = sY + rH; }
                default -> { x = sX;               y = sY + rH - t * rH; }
            }
            pts[i] = new Point(x, y);
        }
        pts[N - 1] = new Point(W / 2.0, H / 2.0 + 12);
        return pts;
    }

    static Pane makePane(double w, double h) {
        Pane p = new Pane();
        p.setPrefSize(w, h);
        p.setStyle("-fx-background-color: white; -fx-border-color: #aaa;");
        return p;
    }

    void addLog(String msg) {
        log.append(msg).append("\n");
        logArea.setText(log.toString());
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    record Point(double x, double y) {}

    public static void main(String[] args) { launch(); }
}
