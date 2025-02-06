import java.awt.*;
import java.util.*;
import java.util.List;

public class AStar {
    public static List<Point> findPath(Point start, Point end, List<Polygon> polygons) {
        // Open List(用於搜尋新路徑用)和Closed List(用於記錄走過的路徑)
        Set<Point> openList = new HashSet<>();  // 一个不允許有重複元素的集合
        Set<Point> closedList = new HashSet<>();
        Map<Point, Point> cameFrom = new HashMap<>();  // 用來回溯路徑
        Map<Point, Integer> gScore = new HashMap<>();  // 從起始點到目前節點實際移動的距離
        Map<Point, Integer> fScore = new HashMap<>();  // 目前節點的評價分數總和

        // 創建Collisions
        CheckCollisions checkCollisions = new CheckCollisions();
        
        // 初始化
        openList.add(start);
        gScore.put(start, 0);
        fScore.put(start, heuristic(start, end));  // F(n) = G(n) + H(n)，因為是起點G(n)=0
        
        // A*算法主循環
        while (!openList.isEmpty()) {
            // 找出fScore最小的點
            Point current = getLowestFScorePoint(openList, fScore);
            
            // 如果找到終點，則回溯路徑
            if (current.equals(end)) {
                return reconstructPath(cameFrom, current);
            }
            
            // 將當前current移至封閉列表
            openList.remove(current);
            closedList.add(current);
            
            // 檢查鄰近點
            for (Point neighbor : getNeighbors(current)) {
                // 已經評估過的鄰近點，不再處理
                if (closedList.contains(neighbor)) {
                    continue;
                }
                
                // 確保鄰近點不與多邊形發生碰撞
                if (checkCollisions.isPointAndPolygonsCollisions(neighbor, polygons)) {
                    continue;
                }
                
                // 計算gScore，此處的current與neighbor的距離認定，採heuristic用計算
                int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE) + heuristic(current, neighbor);
                
                // 將新的neighbor加入openList
                if (!openList.contains(neighbor)) {
                    openList.add(neighbor);
                } else if (tentativeGScore >= gScore.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    continue;
                }
                
                // 更新cameFrom、gScore和fScore
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, end));
            }
        }
        
        return new ArrayList<>(); // 如果沒有找到路徑，返回空列表
    }
    
    // 計算兩點之間的距離，採用曼哈頓距離
    private static int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    
    // 從已經找到的路徑中回溯出完整的路徑
    private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        path.add(current);
        
        while (cameFrom.containsKey(current)) {  // 檢查ameFrom中是否存在current
            current = cameFrom.get(current);
            path.add(current);
        }
        
        // 反轉排序路徑
        Collections.reverse(path);

        return path;
    }
    
    // 取得當前點的鄰居(4個方向，上下左右)
    private static List<Point> getNeighbors(Point p) {
        List<Point> neighbors = new ArrayList<>();
        neighbors.add(new Point(p.x + 1, p.y));
        neighbors.add(new Point(p.x - 1, p.y));
        neighbors.add(new Point(p.x, p.y + 1));
        neighbors.add(new Point(p.x, p.y - 1));      
        return neighbors;
    }
    
    // 找出fScore最小的點
    private static Point getLowestFScorePoint(Set<Point> openList, Map<Point, Integer> fScore) {
        Point lowest = null;
        int lowestFScore = Integer.MAX_VALUE;
        
        for (Point point : openList) {
            int score = fScore.getOrDefault(point, Integer.MAX_VALUE);  // 取得指定point對應對fScore
            if (score < lowestFScore) {
                lowestFScore = score;
                lowest = point;
            }
        }
        
        return lowest;
    }
}