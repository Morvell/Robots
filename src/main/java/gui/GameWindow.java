package gui;

import java.awt.BorderLayout;
import java.awt.TextArea;
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
import util.FrameSerializater;

public class GameWindow extends JInternalFrame {

  private final GameVisualizer m_visualizer;

  public GameWindow() {
    super("Игровое поле", true, true, true, true);
    m_visualizer = new GameVisualizer();
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(m_visualizer, BorderLayout.CENTER);
    getContentPane().add(panel);
    pack();
    addInternalFrameListener(new InternalFrameAdapter() {
      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        String a = System.getProperty("user.home");
        XMLDecoder decoder;
        FrameSerializater<GameVisualizer> serializater;
        try {
          decoder = new XMLDecoder(
              new BufferedInputStream(
                  new FileInputStream(System.getProperty("user.home")+"/Robots/"+this.getClass().getName()+".xml")));
          serializater = (FrameSerializater<GameVisualizer>) decoder.readObject();
          setSize(serializater.getSize());
          setLocation(serializater.getLocation());
          m_visualizer.setGameVisualizer(serializater.getContent());
        } catch (FileNotFoundException e1) {
          System.out.println("File not found");
        }


      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        XMLEncoder encoder = null;
        FrameSerializater<GameVisualizer> serializater = new FrameSerializater<>(
            getLocation(), getSize(), m_visualizer);

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
                  new FileOutputStream(System.getProperty("user.home") + "/Robots/" + this.getClass().getName()+".xml")));
        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        encoder.writeObject(serializater);
        encoder.close();

      }
    });
  }
}
