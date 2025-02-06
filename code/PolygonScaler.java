import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

public class PolygonScaler {
    public static List<Polygon> scalePolygons(List<Polygon> polygons, int scaleSize) {
        // 用來儲存放大後的多邊形
        List<Polygon> scaledPolygons = new ArrayList<>();

        for (Polygon polygon : polygons) {
            // 計算多邊形的中心點
            int[] center = calculateCenter(polygon);

            // 放大並儲存放大後的多邊形
            Polygon scaledPolygon = scalePolygon(polygon, center, scaleSize);
            scaledPolygons.add(scaledPolygon);
        }
        return scaledPolygons;
    }

    // 計算多邊形的中心點
    private static int[] calculateCenter(Polygon polygon) {
        int sumX = 0;
        int sumY = 0;
        for (int i = 0; i < polygon.npoints; i++) {
            sumX = sumX + polygon.xpoints[i];
            sumY = sumY + polygon.ypoints[i];
        }
        return new int[]{sumX / polygon.npoints, sumY / polygon.npoints};
    }

    // 放大多邊形的每個頂點
    private static Polygon scalePolygon(Polygon polygon, int[] center, int scaleSize) {
        Polygon scaledPolygon = new Polygon();
        for (int i = 0; i < polygon.npoints; i++) {
            // 計算放大後的X座標
            int scaledX = 0;
            if (0 < polygon.xpoints[i] - center[0]) {
                scaledX = center[0] + (polygon.xpoints[i] - center[0]) + scaleSize;
            } else {
                scaledX = center[0] + (polygon.xpoints[i] - center[0]) - scaleSize;
            }

            // 計算放大後的Y座標
            int scaledY = 0;
            if (0 < polygon.ypoints[i] - center[1]) {
                scaledY = center[1] + (polygon.ypoints[i] - center[1]) + scaleSize;
            } else {
                scaledY = center[1] + (polygon.ypoints[i] - center[1]) - scaleSize;
            }

            // 將放大後的座標添加到新的多邊形中
            scaledPolygon.addPoint(scaledX, scaledY);
        }
        return scaledPolygon;
    }
}