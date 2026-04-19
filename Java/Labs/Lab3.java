import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

public class Lab3 extends Application {

    static final int N1 = 5, N2 = 2, N3 = 2, N4 = 9;
    static final int N = 10 + N3;
    static final double R = 16;

    @Override
    public void start(Stage stage) {
        int[][] adir = generateAdir();
        int[][] aundir = generateAundir(adir);

        printMatrix("Adir", adir);
        printMatrix("Aundir", aundir);

        Pane left = new Pane();
        left.setPrefSize(600, 650);

        Pane right = new Pane();
        right.setPrefSize(600, 650);

        Point[] pts = vertexPositions(600, 650);
        drawGraph(left, adir, pts, true, "Directed graph");
        drawGraph(right, aundir, pts, false, "Undirected graph");

        HBox root = new HBox(left, right);

        stage.setTitle("Lab 3 | IM-52, variant 29");
        stage.setScene(new Scene(root, 1200, 650));
        stage.show();
    }

    static int[][] generateAdir() {
        Random rng = new Random(N1 * 1000 + N2 * 100 + N3 * 10 + N4);
        double k = 1.0 - N3 * 0.02 - N4 * 0.005 - 0.25;
        int[][] A = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                A[i][j] = rng.nextDouble() * 2.0 * k >= 1.0 ? 1 : 0;
            }
        }

        return A;
    }

    static int[][] generateAundir(int[][] D) {
        int[][] U = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                U[i][j] = (D[i][j] | D[j][i]);
            }
        }

        return U;
    }

    static void printMatrix(String title, int[][] matrix) {
        System.out.println("\n" + title + ":");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    static Point[] vertexPositions(double width, double height) {
        double rectW = width * 0.6;
        double rectH = height * 0.6;
        double startX = (width - rectW) / 2.0;
        double startY = (height - rectH) / 2.0;

        Point[] pts = new Point[N];
        int count = N - 1;
        int perSide = (int)Math.ceil(count / 4.0);

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

            pts[i] = new Point(x, y);
        }

        pts[N - 1] = new Point(width / 2.0, height / 2.0);
        return pts;
    }

    static void drawGraph(Pane pane, int[][] matrix, Point[] points, boolean directed, String title) {
        pane.getChildren().clear();

        pane.getChildren().add(new Text(20, 25, title));

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == 1) {
                    if (directed || j >= i) {
                        drawEdge(pane, points, i, j, directed);
                    }
                }
            }
        }

        for (int i = 0; i < N; i++) {
            drawNode(pane, points[i], i + 1);
        }
    }

    static void drawEdge(Pane pane, Point[] pts, int i, int j, boolean directed) {
        Point a = pts[i];
        Point b = pts[j];

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
        Circle circle = new Circle(x, y - 20, 25);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        pane.getChildren().add(circle);
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

    record Point(double x, double y) {}

    public static void main(String[] args) {
        launch();
    }
}