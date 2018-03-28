package gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import javax.swing.JPanel;
import util.FrameSerializableContainer;
import util.JInternalFrameSerializable;

public class GameWindow extends JInternalFrameSerializable<GameVisualizer> {

  protected GameVisualizer content;

  public GameWindow() {
    super("Игровое поле", true, true, true, true);
    content = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(content, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();

  }

  @Override
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
  }

  @Override
  public void setDateOfContent(GameVisualizer content) {
    this.content.setGameVisualizer(content);
  }
}
