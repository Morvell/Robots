package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import util.JInternalFrameSerializable;

public class LogWindow extends JInternalFrameSerializable<TextArea> implements LogChangeListener {

  private LogWindowSource m_logSource;


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
  }


  private void updateLogContent() {
    StringBuilder contentLog = new StringBuilder();
    for (LogEntry entry : m_logSource.all()) {
      contentLog.append(entry.getMessage()).append("\n");
    }
    this.content.append(contentLog.toString());
    this.content.invalidate();
  }


  @Override
  public void onLogChanged() {
    EventQueue.invokeLater(this::updateLogContent);
  }

  @Override
  public void setDateOfContent(TextArea content) {
    this.content.setText(content.getText());
  }
}
