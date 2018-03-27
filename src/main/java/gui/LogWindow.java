package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import util.FrameSerializater;

public class LogWindow extends JInternalFrame implements LogChangeListener {

  private LogWindowSource m_logSource;
  private TextArea m_logContent;


  public LogWindow(LogWindowSource logSource) {
    super("Протокол работы", true, true, true, true);
    m_logSource = logSource;
    m_logSource.registerListener(this);
    m_logContent = new TextArea("");
    m_logContent.setSize(200, 500);

    setDefaultCloseOperation(EXIT_ON_CLOSE);

    addInternalFrameListener(new InternalFrameAdapter() {

      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        XMLDecoder decoder = null;
        FrameSerializater<TextArea> serializater;
        String a=System.getProperty("user.home");
        try {
          decoder = new XMLDecoder(
              new BufferedInputStream(
                  new FileInputStream(System.getProperty("user.home")+"/Robots/"+"LogWindow.xml")));
          serializater = (FrameSerializater<TextArea>) decoder.readObject();
          decoder.close();
          setSize(serializater.getSize());
          setLocation(serializater.getLocation());
          m_logContent.setText(serializater.getContent().getText());
        } catch (FileNotFoundException e1) {
          System.out.println("Что-то не так с файлом");
        }
        catch (Exception e1){
          System.out.println("Что-то страшное");
        }

      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        XMLEncoder encoder = null;
        FrameSerializater<TextArea> serializater = new FrameSerializater<>(
            getLocation(), getSize(), m_logContent);

//        System.out.println(this.getClass().getName());

        String a = System.getProperty("user.home");
        Path path = Paths.get(a+"/Robots");
        if(!Files.exists(path)) {
          try {
            Files.createDirectories(path);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
        try {
          encoder = new XMLEncoder(
              new BufferedOutputStream(
                  new FileOutputStream(System.getProperty("user.home") + "/Robots/" + "LogWindow.xml")));
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        encoder.writeObject(serializater);
        encoder.close();

      }
    });

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_logContent, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    updateLogContent();
  }


  private void updateLogContent() {
    StringBuilder content = new StringBuilder();
    for (LogEntry entry : m_logSource.all()) {
      content.append(entry.getMessage()).append("\n");
    }
    m_logContent.setText(content.toString());
    m_logContent.invalidate();
  }

  public void makeClosedEvent(){
//    dispatchEvent(new InternalFrameEvent(this, InternalFrameEvent.INTERNAL_FRAME_CLOSED));
    try {
      setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }
}
