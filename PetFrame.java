package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.concurrent.*;

/**
 * @author Ayase
 * @date 2019/10/30-14:28
 */
public class PetFrame extends JFrame {
    static Point origin = new Point();
    JLabel mainView;
    ExecutorService playThreadPool;
    JTextField txChat;
    JButton jbChat;
    JPanel chatBar;
    JPanel menuBar;
    public PetFrame(int firstX, int firstY){

        //用Jlabel显示图片
        mainView = loadPicture("data\\pictures\\2.png");
        playThreadPool = Executors.newCachedThreadPool();


        add(mainView);

        addMouseListener(new MouseAdapter() {
            // 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
            @Override
            public void mousePressed(MouseEvent e) {
                // 当鼠标按下的时候获得窗口当前的位置
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            // 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
            @Override
            public void mouseDragged(MouseEvent e) {
                // 当鼠标拖动时获取窗口当前位置
                Point p = getLocation();
                // 设置窗口的位置
                // 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
                p.getLocation();
                PrintStream stream=null;
                try {

                    stream=new PrintStream("data\\setting.dat");//写入的文件path
                    stream.print(p.x + "\n"+ p.y);//写入的字符串
                } catch (FileNotFoundException ee) {
                    ee.printStackTrace();
                }
                stream.close();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 取消窗口标题栏
        setUndecorated(true);
        // 背景透明
        setBackground(new Color(0,0,0,0));
        //设置位置并显示在最前端
        setBounds(firstX,firstY,200,200);
        setAlwaysOnTop(true);
        //设置取消窗体任务栏图标
        setType(JFrame.Type.UTILITY);
        //设置托盘图标
        setTray(this);
        validate();
        setVisible(true);

        walkForward();
    }

    /**
     * @MethodName loadPicture
     * @Description TODO
     * 加载图片
     * @Param [x, y, url]
     * @Return javax.swing.JLabel
     * @author Ayase
     * @date 10:21
     */
    public JLabel loadPicture(String url){
        JLabel jLabel = new JLabel();
        ImageIcon icon = new ImageIcon(url);
        int picWidth = icon.getIconWidth(),pinHeight = icon.getIconHeight();
        icon.setImage(icon.getImage().getScaledInstance(picWidth,pinHeight,Image.SCALE_DEFAULT));
        jLabel.setBounds(0,0,picWidth,pinHeight);
        jLabel.setIcon(icon);
        return jLabel;
    }

    private void cgJlabelImg(JLabel jLabel,String imgUrl){
        ImageIcon icon = new ImageIcon(imgUrl);
        jLabel.setIcon(icon);
    }

    private void walkForward(){
        playThreadPool.execute(() -> {
            int i = 2;
            boolean issub = false;
            try {
                while (true) {
                    Thread.sleep(200);
                    cgJlabelImg(mainView, "data\\pictures\\" + i + ".png");
                    if (issub) {
                        i--;
                        if (i == 1) {
                            issub = false;
                        }
                    } else {
                        i++;
                        if (i == 3) {
                            issub = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //设置托盘菜单
    private static void setTray(JFrame frame) {
        // 判断系统是否支持系统托盘
        if (SystemTray.isSupported()) {
            // 获取当前系统的托盘
            SystemTray tray = SystemTray.getSystemTray();

            // 为托盘添加一个右键弹出菜单
            PopupMenu popMenu = new PopupMenu();

            MenuItem itemOpen = new MenuItem("Show");
            itemOpen.addActionListener(e -> frame.setVisible(true));

            MenuItem itemHide = new MenuItem("Hide");
            itemHide.addActionListener(e -> frame.setVisible(false));

            MenuItem itemExit = new MenuItem("Exit");
            itemExit.addActionListener(e -> System.exit(0));

            popMenu.add(itemOpen);
            popMenu.add(itemHide);
            popMenu.add(itemExit);

            // 设置托盘图标
            ImageIcon icon = new ImageIcon("F:\\尼尔\\Na\\gsdata\\game.png");
            Image image = icon.getImage().getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT);

            TrayIcon trayIcon = new TrayIcon(image, "桌面宠物", popMenu);

            // 自适应尺寸，这个属性至关重要
            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }
}
