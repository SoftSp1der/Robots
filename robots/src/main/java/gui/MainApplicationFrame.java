package main.java.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.util.List;

import main.java.helper.Triple;
import main.java.helper.JsonManager;

import main.java.log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final GameLogic logic = new GameLogic();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width - inset * 2,
            screenSize.height - inset * 2);

        setContentPane(desktopPane);
        desktopPane.setPreferredSize(new Dimension(1000, 1000));
        desktopPane.setName("desktop");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameMonitorWindow gameMonitor = new GameMonitorWindow(logic);
        addWindow(gameMonitor);

        GameWindow gameWindow = new GameWindow(logic);
        addWindow(gameWindow);

        JsonManager.load_positions(desktopPane);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    public void quit() {
        var answer = JOptionPane.showConfirmDialog(null, "Выйти?", "Выйти", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            JsonManager.save_positions(desktopPane);
            System.exit(0);
        } else{
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        }
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private void addDropDownOption(JMenu menu, String optionName, int keyEvent, ActionListener lambda) {
        JMenuItem item = new JMenuItem(optionName, keyEvent);
        item.addActionListener(lambda);
        menu.add(item);
    }

    private void addMenuDropDown(JMenuBar menuBar, String dropDownName, int keyEvent, String accessibilityText,
        List<Triple<String, Integer, ActionListener>> options
    ) {
        JMenu menu = new JMenu(dropDownName);
        menu.setMnemonic(keyEvent);
        menu.getAccessibleContext().setAccessibleDescription(accessibilityText);
        for (var option : options) {
            addDropDownOption(menu, option.a, option.b, option.c);
        }
        menuBar.add(menu);
    }
    
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        addMenuDropDown(menuBar, "Режим отображения", 
            KeyEvent.VK_V, "Управление режимом отображения приложения",
            List.of(new Triple<String, Integer, ActionListener>("Системная схема", KeyEvent.VK_S, (event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            }), new Triple<String, Integer, ActionListener>("Универсальная схема", KeyEvent.VK_S, (event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            }))
        );

        addMenuDropDown(menuBar, "Тесты", KeyEvent.VK_T, "Тестовые команды",
            List.of(new Triple<String, Integer, ActionListener>("Сообщение в лог", KeyEvent.VK_S, (event) -> {
                Logger.debug("Новая строка");
            }))
        );

        addMenuDropDown(menuBar, "Выйти", KeyEvent.VK_T, "Выйти",
            List.of(new Triple<String, Integer, ActionListener>("Выйти", KeyEvent.VK_S, (event) -> {
                quit();
            }))
        );

        return menuBar;
    }
    
    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
