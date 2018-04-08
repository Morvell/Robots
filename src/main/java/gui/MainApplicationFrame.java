package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import log.Logger;
import lombok.Value;


public class MainApplicationFrame extends JFrame {

  private final JDesktopPane desktopPane = new JDesktopPane();
  private List<JInternalFrame> frames = new ArrayList<>();

  public MainApplicationFrame() {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset,
        screenSize.width - inset * 2,
        screenSize.height - inset * 2);

    setContentPane(desktopPane);

    UIManager.put("OptionPane.yesButtonText", "Да");
    UIManager.put("OptionPane.noButtonText", "Нет");

    LogWindow logWindow = createLogWindow();
    addWindow(logWindow);

    GameWindow gameWindow = new GameWindow();
    gameWindow.setSize(400, 400);
    addWindow(gameWindow);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        int result = JOptionPane
            .showConfirmDialog(null, "Дейтвительно хотите выйти?", "Выход из приложения",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.OK_OPTION) {
          frames.forEach(JInternalFrame::dispose);
          System.exit(0);
        }
      }
    });


    setJMenuBar(generateMenuBar());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
    frames.add(frame);
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


  private JMenuItem createMenuItem(String text, int keyEvent, ActionListener actionEvent) {
    JMenuItem menuItem = new JMenuItem(text, keyEvent);
    menuItem.addActionListener(actionEvent);
    return menuItem;
  }

  private JMenu createMenu(String text, int mnemonic, String descriptor) {
    JMenu menu = new JMenu(text);
    menu.setMnemonic(mnemonic);
    menu.getAccessibleContext().setAccessibleDescription(descriptor);
    return menu;
  }

  private void addMenuToBar(JMenuBar bar, JMenu... menus) {

    for (JMenu a : menus) {
      bar.add(a);
    }
  }

  private void addItemToMenu(JMenu menu, JMenuItem... items) {
    for (JMenuItem a : items) {
      menu.add(a);
    }
  }

  private JMenuBar generateMenuBar() {

    JMenu lookAndFeelMenu = createMenu("Режим отображения", KeyEvent.VK_V,
        "Управление режимом отображения приложения");

    JMenuItem systemLookAndFeel = createMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
      setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      this.invalidate();
    });
    JMenuItem crossplatformLookAndFeel = createMenuItem("Универсальная схема", KeyEvent.VK_S,
        (event) -> {
          setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
          this.invalidate();
        });

    addItemToMenu(lookAndFeelMenu, systemLookAndFeel, crossplatformLookAndFeel);

    JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");

    JMenuItem addLogMessageItem = createMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> {
      Logger.debug("Новая строка");
    });

    addItemToMenu(testMenu, addLogMessageItem);

    JMenu actionMenu = createMenu("Управление", KeyEvent.VK_E, "Разного рода управление");

    JMenuItem exitItem = createMenuItem("Выход", KeyEvent.VK_ESCAPE, e -> {
      dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    });

    addItemToMenu(actionMenu, exitItem);

    JMenuBar menuBar = new JMenuBar();
    addMenuToBar(menuBar, actionMenu, lookAndFeelMenu, testMenu);

    return menuBar;
  }

  private void setLookAndFeel(String className) {
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException | InstantiationException
        | IllegalAccessException | UnsupportedLookAndFeelException e) {
    }

  }
}
