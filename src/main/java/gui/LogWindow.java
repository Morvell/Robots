package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.stream.StreamSupport;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import util.FrameSerializableContainer;
import util.JInternalFrameSerializable;

public class LogWindow extends JInternalFrame implements LogChangeListener, Serializable {

  private LogWindowSource m_logSource;
  private TextArea content;


  public LogWindow(LogWindowSource logSource) {
    super("Протокол работы", true, true, true, true);
    m_logSource = logSource;
    m_logSource.registerListener(this);
    content = new TextArea("");
    content.setSize(200, 500);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(content, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    updateLogContent();

    JInternalFrameSerializable<TextArea> serializater = new JInternalFrameSerializable<>(
        getTitle());

    addInternalFrameListener(new InternalFrameAdapter() {
      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        recreateFrame(serializater.deserialize());
      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        FrameSerializableContainer<TextArea> container = new FrameSerializableContainer<>(
            getLocation(), getSize(), isIcon, isMaximum, content);

        serializater.serialize(container);
      }
    });
  }


  private void updateLogContent() {
    StringBuilder contentLog = new StringBuilder();
    StreamSupport.stream(m_logSource.all().spliterator(),false).skip(1).forEach(entry -> {
      contentLog.append(entry.getMessage()).append("\n");
    });
    this.content.append(contentLog.toString());
    this.content.invalidate();
  }


  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }

  public void recreateFrame(FrameSerializableContainer<TextArea> serializater) {

    try {
      setSize(serializater.getSize());
      setLocation(serializater.getLocation());
      setIcon(serializater.getIsIcon());
      setMaximum(serializater.getIsMaximum());
      setDateOfContent(serializater.getContent());
    } catch (PropertyVetoException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void setDateOfContent(TextArea content) {
    this.content.setText(content.getText());
  }

}
