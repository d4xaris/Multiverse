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

public class Lab6 extends Application {

    static final double R = 16;

    SpanningData data;
    Point[] pts;
    Pane graphPane, mstPane;
    TextArea logArea;
    Button btnStep;
    StringBuilder log = new StringBuilder();

    @Override
    public void start(Stage stage) {
        data = new SpanningData();
        pts = makePositions(585, 375);

        graphPane = makePane(585, 375);
        mstPane = makePane(585, 375);

        logArea = new TextArea();
        logArea.setPrefSize(1180, 100);
        logArea.setEditable(false);

        btnStep = new Button("Next Step [Space]");
        Button bReset = new Button("Reset");

        btnStep.setOnAction(e -> step());
        bReset.setOnAction(e -> setup());

        HBox buttons = new HBox(10, btnStep, bReset);
        buttons.setPadding(new Insets(6));
        HBox panes = new HBox(10, graphPane, mstPane);
        VBox root = new VBox(6, panes, buttons, logArea);
        root.setPadding(new Insets(8));

        Scene scene = new Scene(root, 1200, 560);
        scene.setOnKeyPressed(e -> { if (e.getCode().toString().equals("SPACE")) step(); });

        stage.setTitle("Lab 6 | IM-52, variant 29 - Prim MST");
        stage.setScene(scene);
        stage.show();

        setup();
    }

    void setup() {
        log = new StringBuilder();
        data.reset();
        addLog("Prim start from vertex 1  (k=" + SpanningData.K + " N=" + SpanningData.N + ")");
        btnStep.setDisable(false);
        redraw();
    }

    void step() {
        if (data.done) return;
        String msg = data.step();
        if (msg != null) addLog(msg);
        if (data.done) {
            data.printResults();
            btnStep.setDisable(true);
        }
        redraw();
    }

    void redraw() {
        drawGraph();
        drawMST();
    }

    void drawGraph() {
        graphPane.getChildren().clear();
        int N = SpanningData.N;
        graphPane.getChildren().add(new Text(15, 20,
                "Undirected weighted graph [step " + data.mstEdges.size() + "/" + (N - 1) + "]"));

        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                if (data.W[i][j] > 0) {
                    boolean mst = data.isMSTEdge(i, j);
                    drawEdge(graphPane, pts, i, j, mst ? Color.ROYALBLUE : Color.LIGHTGRAY,
                            mst ? 2.5 : 0.8, data.W[i][j]);
                }

        for (int i = 0; i < N; i++)
            drawNode(graphPane, pts[i], i + 1, data.inMST[i] ? Color.LIGHTGREEN : Color.WHITE);
    }

    void drawMST() {
        mstPane.getChildren().clear();
        int N = SpanningData.N;
        mstPane.getChildren().add(new Text(15, 20,
                "MST (Prim)  cost=" + data.totalCost));

        for (int[] e : data.mstEdges)
            drawEdge(mstPane, pts, e[0], e[1], Color.ROYALBLUE, 2.5, e[2]);

        for (int i = 0; i < N; i++)
            if (data.inMST[i])
                drawNode(mstPane, pts[i], i + 1, Color.LIGHTGREEN);
    }

    static void drawEdge(Pane pane, Point[] pts, int i, int j,
                         Color color, double w, int weight) {
        Point a = pts[i], b = pts[j];

        double offset = (j - i) * 5.0;
        double cx = (a.x + b.x) / 2.0 + offset;
        double cy = (a.y + b.y) / 2.0 - offset;

        pane.getChildren().addAll(line(a.x, a.y, cx, cy, color, w),
                line(cx, cy, b.x, b.y, color, w));

        Text wt = new Text(cx - 8, cy - 3, String.valueOf(weight));
        wt.setFill(color == Color.ROYALBLUE ? Color.ROYALBLUE : Color.GRAY);
        pane.getChildren().add(wt);
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

    static Point[] makePositions(double W, double H) {
        int N = SpanningData.N;
        double rW = W * 0.7, rH = H * 0.72;
        double sX = (W - rW) / 2, sY = (H - rH) / 2 + 14;
        Point[] p = new Point[N];
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
            p[i] = new Point(x, y);
        }
        p[N - 1] = new Point(W / 2.0, H / 2.0 + 12);
        return p;
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
