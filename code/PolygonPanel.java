import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PolygonPanel extends JPanel {
    private List<Polygon> polygons;  // 用來儲存多邊形
    private List<Polygon> polygonsEnlarge;  // 用來儲存放大後多邊形
    private int selectedPolygonIndex = -1;  // 目前被選中的多邊形
    private Point lastDragPoint;  // 最後一次拖曳的滑鼠座標
    private List<Point> path = new ArrayList<>();  // 存放計算出來的路徑

    // 起點和終點
    private Point start = new Point(100, 500);
    private Point end = new Point(700, 100);
    
    // 初始的多邊形座標(此多邊形指的是障礙物)
    private final int[][][] initialObjects = {
        {{50, 50}, {100, 50}, {100, 100}, {50, 100}},                 // 第一個多邊形
        {{250, 250}, {300, 300}, {250, 300}},                         // 第二個多邊形
        {{500, 250}, {700, 150}, {600, 350}},                         // 第三個多邊形
        {{450, 450}, {600, 450}, {600, 500}, {550, 540}, {450, 500}}  // 第四個多邊形
    };

    // 路徑繪製模式，控制用什麼方法繪製路徑
    public enum DrawMode {
        none,                  // 不繪製任何路徑
        calculatingPathLines,  // 繪製原路徑線段
        smoothingPathLines,    // 繪製原路徑線段
        trackPoints            // 繪製圓形物體
    }

    // 預設繪製路徑線段
    private DrawMode currentDrawMode = DrawMode.calculatingPathLines;

    public PolygonPanel() {
        // 初始化路徑數據
        clearPath();

        // 初始化多邊形座標數據
        polygons = new ArrayList<>();
        for (int[][] object : initialObjects) {
            Polygon polygon = new Polygon();
            for (int[] point : object) {
                polygon.addPoint(point[0], point[1]);
            }
            polygons.add(polygon);
        }

        // 設定面板背景色
        setBackground(Color.WHITE);

        // 增加滑鼠事件監聽器
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 檢查是否點有碰觸到多邊形，並記錄多邊形編號
                for (int i = 0; i < polygons.size(); i++) {
                    if (polygons.get(i).contains(e.getPoint())) {
                        selectedPolygonIndex = i;
                        lastDragPoint = e.getPoint();  // 滑鼠點下右鍵前的座標
                        break;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedPolygonIndex = -1; // 釋放被選中的多邊形
            }
        });

        // 添加滑鼠拖曳監聽器
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPolygonIndex != -1) {
                    Polygon selectedPolygon = polygons.get(selectedPolygonIndex);
                    int dx = e.getX() - lastDragPoint.x;
                    int dy = e.getY() - lastDragPoint.y;

                    // 移動多邊形的所有點
                    for (int i = 0; i < selectedPolygon.npoints; i++) {
                        selectedPolygon.xpoints[i] =  selectedPolygon.xpoints[i] + dx;
                        selectedPolygon.ypoints[i] =  selectedPolygon.ypoints[i] + dy;
                    }

                    lastDragPoint = e.getPoint();
                    repaint(); // 重繪畫面
                }
            }
        });
    }

    // 重新初始化多邊形座標
    public void resetPolygons() {
        polygons.clear();
        for (int[][] object : initialObjects) {
            Polygon polygon = new Polygon();
            for (int[] point : object) {
                polygon.addPoint(point[0], point[1]);
            }
            polygons.add(polygon);
        }
        currentDrawMode = DrawMode.none;  // 不繪製路徑線段
        clearPath();  // 清除路徑
        repaint();  // 重繪畫面
    }

    // 清空路徑
    public void clearPath() {
        path.clear();
    }

    // 路徑規劃
    public void motionPlanning() {
        // 預設繪製路徑線段
        currentDrawMode = DrawMode.calculatingPathLines;

        // 將移動物體的尺寸縮小為點，並同時把障礙物的尺寸進行放大處理
        PolygonScaler polygonScaler = new PolygonScaler();
        polygonsEnlarge = polygonScaler.scalePolygons(polygons, 50);

        // 執行A*路徑規劃
        path = AStar.findPath(start, end, polygonsEnlarge);

        repaint();  // 路徑計算後需要重繪畫面
    }

    // 設定繪製模式
    public void setDrawMode(DrawMode mode) {
        this.currentDrawMode = mode;
        repaint();  // 路徑計算後需要重繪畫面
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 繪製所有的多邊形
        for (Polygon polygon : polygons) {
            g.setColor(Color.BLUE);
            g.fillPolygon(polygon);
            g.setColor(Color.BLACK);
            g.drawPolygon(polygon);
        }

        // 繪製起點，顯示為綠色小圓點
        g.setColor(Color.GREEN);
        g.fillOval(start.x - 5, start.y - 5, 10, 10);

        // 繪製終點，顯示為紅色小圓點
        g.setColor(Color.RED);
        g.fillOval(end.x - 5, end.y - 5, 10, 10);

        // 繪製移動物體
        g.setColor(Color.BLACK);
        g.drawOval(start.x - 50, start.y - 50, 100, 100);

        // 根據當前的繪製模式來決定繪製的內容
        if (currentDrawMode == DrawMode.calculatingPathLines) {
            // 繪製路徑線段
            g.setColor(Color.BLACK);
            for (int i = 0; i < path.size() - 1; i++) {
                Point p1 = path.get(i);
                Point p2 = path.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        } else if (currentDrawMode == DrawMode.smoothingPathLines) {
            SmoothPath smoothPath = new SmoothPath();
            List<Point> correctPath = new ArrayList<>();
            correctPath = smoothPath.smoothPath(path, polygonsEnlarge);

            // 繪製路徑線段
            g.setColor(Color.BLACK);
            for (int i = 0; i < correctPath.size() - 1; i++) {
                Point p1 = correctPath.get(i);
                Point p2 = correctPath.get(i + 1);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        } else if (currentDrawMode == DrawMode.trackPoints) {
            // 繪製圓形物體
            g.setColor(Color.BLACK);
            for (int i = 0; i < path.size(); i = i + 50) {
                g.drawOval(path.get(i).x - 50, path.get(i).y - 50, 100, 100);
            }
        }
    }
}
