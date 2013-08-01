import java.awt.Insets
import javax.swing.SpringLayout.Constraints
import swing._
import swing.GridBagPanel.{Fill, Anchor}
/*
 ++1>>  This source code is licensed as GPLv3 if not stated otherwise.
    >>  NO responsibility taken for ANY harm, damage done
    >>  to you, your data, animals, etc.
    >>
  +2>>
    >>  Project LeapOSC
  +3>>
    >>  Copyright (c) 2013:
    >>
    >>     |             |     |
    >>     |    ,---.,---|,---.|---.
    >>     |    |   ||   |`---.|   |
    >>     `---'`---'`---'`---'`---'
    >>                    // Niklas Kluegel
    >>
  +4>>
    >>  Made in Bavaria by fat little elves - since 1983.
 */
class UISlot(name: String) extends GridBagPanel {
  val c = new Constraints
  val shouldFill = true
  if (shouldFill) {
    c.fill = Fill.Horizontal
  }

  val spacer = new Label("   |   ")

  c.fill = Fill.Horizontal
  c.gridx = 0;
  c.gridy = 0;
  layout(spacer) = c


  val label = new Label(name)
  label.preferredSize = new Dimension(80,20)

  label.horizontalAlignment = Alignment.Left

  c.fill = Fill.Horizontal
  c.gridx = 1;
  c.gridy = 0;
  layout(label) = c

  /* TODO: add muting / solo-ing
  val spacer2 = new Label("   |  ")

  c.fill = Fill.Horizontal
  c.gridx = 2;
  c.gridy = 0;
  layout(spacer2) = c

  val mute = new RadioButton("Mute")
  c.weightx = 0.5

  c.fill = Fill.Horizontal
  c.gridx = 3;
  c.gridy = 0;
  layout(mute) = c

  val solo = new RadioButton("Solo")
  c.fill = Fill.Horizontal
  c.weightx = 0.5;
  c.gridx = 4;
  c.gridy = 0;
  layout(solo) = c
  */

  val spacer3 = new Label("  |    Message: ")

  c.fill = Fill.Horizontal
  c.gridx = 5;
  c.gridy = 0;
  layout(spacer3) = c

  val message = new Label("")
  message.preferredSize = new Dimension(400,20)
  c.fill = Fill.Horizontal
  c.weightx = 0.0;
  c.gridwidth = 10;
  c.gridx = 6;
  c.gridy = 0;
  layout(message) = c
}
