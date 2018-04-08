package util;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JInternalFrameSerializable<T>  {

  String title;

  public void serialize(FrameSerializableContainer<T> container) {
    XMLEncoder encoder = null;
    FrameSerializableContainer<T> serializater = container;

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
                  System.getProperty("user.home") + "/Robots/" + title
                      + ".xml")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    encoder.writeObject(serializater);
    encoder.close();

  }

  public FrameSerializableContainer<T> deserialize() {
    XMLDecoder decoder;
    FrameSerializableContainer<T> serializater = new FrameSerializableContainer<T>();
    String a = System.getProperty("user.home");
    try {
      decoder = new XMLDecoder(
          new BufferedInputStream(
              new FileInputStream(
                  System.getProperty("user.home") + "/Robots/" + title
                      + ".xml")));
      serializater = (FrameSerializableContainer<T>) decoder.readObject();
      decoder.close();

    } catch (FileNotFoundException e1) {
      System.out.println("Что-то не так с файлом");
    } catch (Exception e1) {
      System.out.println(e1);
    }
    return serializater;
  }


}
