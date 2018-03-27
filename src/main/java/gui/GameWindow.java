package gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
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
  public void setDateOfContent(GameVisualizer content) {
    this.content.setGameVisualizer(content);
  }
}
