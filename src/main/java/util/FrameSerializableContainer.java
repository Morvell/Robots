package util;

import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import javax.swing.JInternalFrame;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FrameSerializableContainer<T> implements Serializable{
  Point location;
  Dimension size;
  Boolean isIcon, isMaximum;
  T content;
}