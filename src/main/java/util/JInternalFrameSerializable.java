package util;

import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class JInternalFrameSerializable<T> extends JInternalFrame implements Serializable {

  protected T content;

  public JInternalFrameSerializable(String date, boolean b, boolean b1,
      boolean b2, boolean b3) {
    super(date, b, b1, b2, b3);
    addInternalFrameListener(new InternalFrameAdapter() {

      @Override
      public void internalFrameOpened(InternalFrameEvent e) {
        deserialize();
      }

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {
        serialize();
      }
    });
  }


  public void serialize() {
    XMLEncoder encoder = null;
    FrameSerializableContainer<T> serializater = new FrameSerializableContainer<T>(
        getLocation(), getSize(), content);

    System.out.println(this.getClass().getName());

    String a = System.getProperty("user.home");
    Path path = Paths.get(a + "/Robots");
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    try {
      encoder = new XMLEncoder(
          new BufferedOutputStream(
              new FileOutputStream(
                  System.getProperty("user.home") + "/Robots/" + this.getClass().getName()
                      + ".xml")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    encoder.writeObject(serializater);
    encoder.close();

  }

  public void deserialize() {
    XMLDecoder decoder;
    FrameSerializableContainer<T> serializater;
    String a = System.getProperty("user.home");
    try {
      decoder = new XMLDecoder(
          new BufferedInputStream(
              new FileInputStream(
                  System.getProperty("user.home") + "/Robots/" + this.getClass().getName()
                      + ".xml")));
      serializater = (FrameSerializableContainer<T>) decoder.readObject();
      decoder.close();
      setSize(serializater.getSize());
      setLocation(serializater.getLocation());
      setDateOfContent(serializater.getContent());
    } catch (FileNotFoundException e1) {
      System.out.println("Что-то не так с файлом");
    } catch (Exception e1) {
      System.out.println(e1);
    }
  }

  public void makeClosedEvent(){
//    dispatchEvent(new InternalFrameEvent(this, InternalFrameEvent.INTERNAL_FRAME_CLOSED));
    try {
      setClosed(true);
    } catch (PropertyVetoException e) {
      e.printStackTrace();
    }
  }


  public abstract void setDateOfContent(T content);

}
