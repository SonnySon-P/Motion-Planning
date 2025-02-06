import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SmoothPath {

    public static List<Point> smoothPath(List<Point> path, List<Polygon> polygons) {
        // 創建一個新的ArrayList物件smoothedPath
        List<Point> smoothedPath = new ArrayList<>();

        // 創建碰撞檢查物件 (假設 CheckCollisions 類別已經定義好)
        CheckCollisions checkCollisions = new CheckCollisions();

        // 當前路徑的起點和終點
        int i = 0;
        Point prevPoint = path.get(i);
        Point nextPoint;
        smoothedPath.add(prevPoint);  // 把初始點加到結果路徑中

        // 主循環: 從第一個點開始，不斷尋找可行的直線段
        while (i < path.size()) {            
            // 從後往前檢查每一個終點，找到不會碰撞的直線
            boolean foundNext = false;
            for (int j = path.size() - 1; j > i; j--) {
                nextPoint = path.get(j);
                // 檢查從prevPoint到nextPoint的線段是否碰撞
                if (!checkCollisions.isLineAndPolygonsCollisions(prevPoint, nextPoint, polygons)) {
                    // 如果不碰撞，將prevPoint更新為nextPoint，並且更新i為j
                    prevPoint = nextPoint;
                    i = j;
                    smoothedPath.add(prevPoint);  // 把這個點加到結果路徑中
                    foundNext = true;
                    break;  // 跳出內部循環，繼續尋找下個可行的直線段
                }
            }

            // 如果沒有找到不會碰撞的直線段，則直接結束
            if (!foundNext) {
                break;
            }
        }

        // 確保最後一個點被加入到平滑路徑中
        if (i != path.size() - 1) {
            smoothedPath.add(path.get(path.size() - 1));
        }

        return smoothedPath;
    }
}
