import javax.swing.*;

public class MotionPlanningExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 創建主框架
            JFrame frame = new JFrame("Motion Planning Example");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            // 創建PolygonPanel，並將其添加到JFrame
            PolygonPanel polygonPanel = new PolygonPanel();
            frame.add(polygonPanel);
            
            // 創建MenuBar，並將其添加到JFrame
            MenuBar menuBar = new MenuBar(frame, polygonPanel);
            frame.setJMenuBar(menuBar.createMenuBar());

            //  顯示GUI
            frame.setVisible(true);
        });
    }
}
