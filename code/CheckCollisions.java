import java.util.List;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;

public class CheckCollisions {
    // 檢查點是否與多邊形發生碰撞
    public boolean isPointAndPolygonsCollisions(Point point, List<Polygon> polygons) {
        // 遍歷每個多邊形，檢查點是否位於其中一個多邊形內部
        for (Polygon polygon : polygons) {
            if (polygon.contains(point)) {
                return true;  // 若有碰撞，返回true
            }
        }
        return false;  // 沒有碰撞，返回false
    }

    // 檢查線是否與多邊形發生碰撞 
    public boolean isLineAndPolygonsCollisions(Point startPoint, Point endPoint, List<Polygon> polygons) {
        Line2D line = new Line2D.Float(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

        // 檢查每一個障礙物是否與路徑線段相交
        for (Polygon polygon : polygons) {
            if (polygon.getBounds2D().intersectsLine(line)) {
                return true;  // 如果碰撞，返回true
            }
        }
        return false;  // 沒有碰撞，返回false
    }
}