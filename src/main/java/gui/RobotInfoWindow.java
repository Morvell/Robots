package gui;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import lombok.extern.java.Log;
import util.FrameSerializableContainer;
import util.JFrameSerializer;

@Log
public class RobotInfoWindow extends JInternalFrame implements Observer<GameVisualizerState> {
  private TextArea content;

  public RobotInfoWindow() {
    super("Информация о роботе", true, true, true, true);
    content = new TextArea("sdfsdfsdf");
    content.setSize(200, 500);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(content, BorderLayout.CENTER);
    notify(new GameVisualizerState());
    getContentPane().add(panel);
    pack();


    JFrameSerializer<TextArea> serializater = new JFrameSerializer<>(
        getTitle());

    addInternalFrameListener(new InternalFrameAdapter() {
      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        recreateFrame(serializater.deserialize());
      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        FrameSerializableContainer<TextArea> container = new FrameSerializableContainer<>(
            getLocation(), getSize(), isIcon(), isMaximum(), content);

        serializater.serialize(container);
      }
    });
  }

  public void recreateFrame(FrameSerializableContainer<TextArea> serializater) {

    try {
      setSize(serializater.getSize());
      setLocation(serializater.getLocation());
      setIcon(serializater.getIsIcon());
      setMaximum(serializater.getIsMaximum());
//      setDateOfContent(serializater.getContent());
    } catch (PropertyVetoException e) {
      log.warning(e.getMessage());
    } catch (Exception e) {
      log.severe(e.getMessage());
    }
  }

  public void setDateOfContent(TextArea content) {
    this.content.setText(content.getText());
  }

  @Override
  public void notify(GameVisualizerState state) {
    content.setText(state.makeText());
  }
}
