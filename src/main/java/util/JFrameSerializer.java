package util;

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
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Log
public class JFrameSerializer<T> {

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
        log.warning(ex.getMessage());
      }
    }
    try {
      encoder = new XMLEncoder(
          new BufferedOutputStream(
              new FileOutputStream(
                  System.getProperty("user.home") + "/Robots/" + title
                      + ".xml")));
    } catch (FileNotFoundException e1) {
      log.warning(e1.getMessage());
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
      log.warning(e1.getMessage());
    } catch (Exception e1) {
      log.severe(e1.getMessage());
    }
    return serializater;
  }


}
