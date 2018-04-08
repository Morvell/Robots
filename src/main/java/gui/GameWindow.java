package gui;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import util.FrameSerializableContainer;
import util.JInternalFrameSerializable;

public class GameWindow extends JInternalFrame {

  GameVisualizer content;

  public GameWindow() {
    super("Игровое поле", true, true, true, true);
    content = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(content, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    JInternalFrameSerializable<GameVisualizer> serializater = new JInternalFrameSerializable<>(getTitle());

    addInternalFrameListener(new InternalFrameAdapter() {
      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        recreateFrame(serializater.deserialize());
      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        FrameSerializableContainer<GameVisualizer> container = new FrameSerializableContainer<>(
            getLocation(), getSize(), isIcon, isMaximum, content);

        serializater.serialize(container);
      }
    });

  }


  public void recreateFrame(FrameSerializableContainer<GameVisualizer> serializater) {
    try {
      setSize(serializater.getSize());
      setLocation(serializater.getLocation());
      setIcon(serializater.getIsIcon());
      setMaximum(serializater.getIsMaximum());
      setDateOfContent(serializater.getContent());
    }
    catch (PropertyVetoException e) {
      System.out.println(e);
    }
    catch (Exception e){
      System.out.println(e);
    }
  }

  public void setDateOfContent(GameVisualizer content) {
    this.content.setGameVisualizer(content);
  }
}
