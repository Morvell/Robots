package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, Serializable{

  private LogWindowSource m_logSource;
  private TextArea m_logContent;


  public LogWindow(LogWindowSource logSource) {
    super("Протокол работы", true, true, true, true);
    m_logSource = logSource;
    m_logSource.registerListener(this);
    m_logContent = new TextArea("");
    m_logContent.setSize(200, 500);


    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_logContent, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    updateLogContent();
  }

  public void serializing(){

    XMLEncoder e = null;
    String a=System.getProperty("user.home");
    try {
      e = new XMLEncoder(
          new BufferedOutputStream(
              new FileOutputStream(System.getProperty("user.home")+"/"+"LogWindow.xml")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    e.close();

  }




  private void updateLogContent() {
    StringBuilder content = new StringBuilder();
    for (LogEntry entry : m_logSource.all()) {
      content.append(entry.getMessage()).append("\n");
    }
    m_logContent.setText(content.toString());
    m_logContent.invalidate();
  }


  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }
}
