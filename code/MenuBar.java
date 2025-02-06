import javax.swing.*;

public class MenuBar {
    private JFrame frame;
    private PolygonPanel polygonPanel;

    // 接收JFrame及PolygonPanel，以便後續事件中使用
    public MenuBar(JFrame frame, PolygonPanel polygonPanel) {
        this.frame = frame;
        this.polygonPanel = polygonPanel;  // 儲存PolygonPanel實例
    }

    public JMenuBar createMenuBar() {
        // 創建MenuBar
        JMenuBar menuBar = new JMenuBar();

        // 創建並設置File選單
        JMenu fileMenu = new JMenu("File");
        JMenuItem initialItem = new JMenuItem("Initial");
        JMenuItem closeItem = new JMenuItem("Close");
        fileMenu.add(initialItem);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        // 創建並設置Operate選單
        JMenu operateMenu = new JMenu("Operate");
        JMenuItem runItem = new JMenuItem("Run");
        operateMenu.add(runItem);

        // 創建並設置Display選單
        JMenu displayMenu = new JMenu("Display");
        JMenuItem calculatingPathsItem = new JMenuItem("Calculating Paths");
        JMenuItem smoothingPathsItem = new JMenuItem("Smoothing Paths");
        JMenuItem trackItem = new JMenuItem("Track");
        displayMenu.add(calculatingPathsItem);
        displayMenu.add(smoothingPathsItem);
        displayMenu.addSeparator();
        displayMenu.add(trackItem);
        // 預設禁用Display中的選項
        calculatingPathsItem.setEnabled(false);
        smoothingPathsItem.setEnabled(false);
        trackItem.setEnabled(false);

        // 創建並設置Help選單
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        // 將各個選單加入選單欄
        menuBar.add(fileMenu);
        menuBar.add(operateMenu);
        menuBar.add(displayMenu);
        menuBar.add(helpMenu);

        // 設置菜單項的事件處理器
        setMenuItemActions(initialItem, closeItem, runItem, calculatingPathsItem, smoothingPathsItem, trackItem, aboutItem);

        return menuBar;
    }

    // 設置選單項的事件處理
    private void setMenuItemActions(JMenuItem initialItem, JMenuItem closeItem, JMenuItem runItem, JMenuItem calculatingPathsItem, JMenuItem smoothingPathsItem, JMenuItem trackItem, JMenuItem aboutItem) {
        // 設置Initial事件
        initialItem.addActionListener(e -> {
            polygonPanel.resetPolygons();  // 重設多邊形
            polygonPanel.clearPath();  // 清空路徑
            // 當按下Initial時禁用Display中的選項
            calculatingPathsItem.setEnabled(false);
            smoothingPathsItem.setEnabled(false);
            trackItem.setEnabled(false);
        });

        // 設置Close事件
        closeItem.addActionListener(e -> System.exit(0));

        // 設置Run事件
        runItem.addActionListener(e -> {
            polygonPanel.motionPlanning();
            // 當按下Run時啟用Display中的選項
            calculatingPathsItem.setEnabled(true);
            smoothingPathsItem.setEnabled(true);
            trackItem.setEnabled(true);
        });

        // 設置Calculating Paths事件
        calculatingPathsItem.addActionListener(e -> polygonPanel.setDrawMode(PolygonPanel.DrawMode.calculatingPathLines));

        // 設置Smoothing Paths事件
        smoothingPathsItem.addActionListener(e -> polygonPanel.setDrawMode(PolygonPanel.DrawMode.smoothingPathLines));

        // 設置Track事件
        trackItem.addActionListener(e -> polygonPanel.setDrawMode(PolygonPanel.DrawMode.trackPoints));

        // 設置About事件
        aboutItem.addActionListener(e -> showAboutDialog());
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame, "This is a Motion Planning example program based on A*.",
                                      "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
