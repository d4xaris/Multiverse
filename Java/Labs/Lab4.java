import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Lab4 extends Application {

    static final int N1 = 5, N2 = 2, N3 = 2, N4 = 9;
    static final double R = 16;

    @Override
    public void start(Stage stage) {
        GraphData data = GraphData.analyzeGraph(N1, N2, N3, N4);
        System.out.print(data.buildReport());

        Pane pane1 = createGraphPane();
        Pane pane2 = createGraphPane();
        Pane pane3 = createGraphPane();
        Pane pane4 = createGraphPane();

        drawGraph(pane1, data.directed1, true, "First digraph");
        drawGraph(pane2, data.undirected1, false, "First undirected graph");
        drawGraph(pane3, data.directed2, true, "Second digraph");
        drawGraph(pane4, data.condensation, true, "Condensation graph");

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        root.add(pane1, 0, 0);
        root.add(pane2, 1, 0);
        root.add(pane3, 0, 1);
        root.add(pane4, 1, 1);

        stage.setTitle("Lab 4 | IM-52, variant 29");
        stage.setScene(new Scene(root, 1200, 800));
        stage.show();
    }

    static Pane createGraphPane() {
        Pane pane = new Pane();
        pane.setPrefSize(580, 380);
        pane.setStyle("-fx-background-color: white; -fx-border-color: black;");
        return pane;
    }

    static Point[] vertexPositions(double width, double height, int n) {
        double rectW = width * 0.6;
        double rectH = height * 0.6;
        double startX = (width - rectW) / 2.0;
        double startY = (height - rectH) / 2.0;

        Point[] points = new Point[n];
        int count = n - 1;
        int perSide = (int) Math.ceil(count / 4.0);

        for (int i = 0; i < count; i++) {
            int side = i / perSide;
            int pos = i % perSide;
            double t = (double) pos / perSide;

            double x, y;

            switch (side) {
                case 0:
                    x = startX + t * rectW;
                    y = startY;
                    break;
                case 1:
                    x = startX + rectW;
                    y = startY + t * rectH;
                    break;
                case 2:
                    x = startX + rectW - t * rectW;
                    y = startY + rectH;
                    break;
                default:
                    x = startX;
                    y = startY + rectH - t * rectH;
                    break;
            }

            points[i] = new Point(x, y);
        }

        points[n - 1] = new Point(width / 2.0, height / 2.0);
        return points;
    }

    static void drawGraph(Pane pane, int[][] matrix, boolean directed, String title) {
        pane.getChildren().clear();

        double width = pane.getPrefWidth();
        double height = pane.getPrefHeight();

        pane.getChildren().add(new Text(15, 25, title));

        int n = matrix.length;
        if (n == 0) return;

        Point[] points = vertexPositions(width, height, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    if (directed || j >= i) {
                        drawEdge(pane, points, i, j, directed);
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            drawNode(pane, points[i], i + 1);
        }
    }

    static void drawEdge(Pane pane, Point[] points, int i, int j, boolean directed) {
        Point a = points[i];
        Point b = points[j];

        if (i == j) {
            drawLoop(pane, a.x, a.y);
            return;
        }

        double offset = (j - i) * 5.0;
        double cx = (a.x + b.x) / 2.0 + offset;
        double cy = (a.y + b.y) / 2.0 - offset;

        pane.getChildren().addAll(
                new Line(a.x, a.y, cx, cy),
                new Line(cx, cy, b.x, b.y)
        );

        if (directed) {
            double dx = b.x - cx;
            double dy = b.y - cy;
            double len = Math.sqrt(dx * dx + dy * dy);

            double newX = b.x - dx / len * R;
            double newY = b.y - dy / len * R;

            drawArrow(pane, cx, cy, newX, newY);
        }
    }

    static void drawLoop(Pane pane, double x, double y) {
        Circle loop = new Circle(x, y - 20, 25);
        loop.setFill(Color.TRANSPARENT);
        loop.setStroke(Color.BLACK);
        pane.getChildren().add(loop);
    }

    static void drawArrow(Pane pane, double x1, double y1, double x2, double y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLen = 12.0;
        double arrowAngle = Math.PI / 6.0;

        double ax1 = x2 - arrowLen * Math.cos(angle - arrowAngle);
        double ay1 = y2 - arrowLen * Math.sin(angle - arrowAngle);

        double ax2 = x2 - arrowLen * Math.cos(angle + arrowAngle);
        double ay2 = y2 - arrowLen * Math.sin(angle + arrowAngle);

        pane.getChildren().addAll(
                new Line(x2, y2, ax1, ay1),
                new Line(x2, y2, ax2, ay2)
        );
    }

    static void drawNode(Pane pane, Point p, int label) {
        Circle circle = new Circle(p.x, p.y, R);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);

        Text text = new Text(p.x - 4, p.y + 4, String.valueOf(label));
        pane.getChildren().addAll(circle, text);
    }

    static record Point(double x, double y) {}

    public static void main(String[] args) {
        launch();
    }
}