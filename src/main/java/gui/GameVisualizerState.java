package gui;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameVisualizerState {

  private double m_robotPositionX = 100;
  private double m_robotPositionY = 100;
  private double m_robotDirection = 0;

  private int m_targetPositionX = 150;
  private int m_targetPositionY = 100;

  private double maxVelocity = 0.1;
  private double maxAngularVelocity = 0.001;

  public String makeText() {
    StringBuilder res = new StringBuilder();
    res.append(String.format("robot position X: %f\n", m_robotPositionX));
    res.append(String.format("robot position Y: %f\n", m_robotPositionY));
    res.append(String.format("robot direction: %f\n", m_robotDirection));
    res.append(String.format("target position X: %d\n", m_targetPositionX));
    res.append(String.format("target position X: %d\n", m_targetPositionY));
    res.append(String.format("max velocity: %f\n", maxVelocity));
    res.append(String.format("max angular velocity: %f\n", maxAngularVelocity));
    return res.toString();
  }
}
