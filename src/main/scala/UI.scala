import java.util.concurrent.{LinkedBlockingQueue, BlockingDeque}
import LeapObjectWrappers._
import de.sciss.osc.Message
import javax.swing.SpringLayout.Constraints
import swing._
import event._
import swing.GridBagPanel.{Fill}

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

class UI extends MainFrame {

  val slots = Seq(
    new UISlot("Hand"),
    new UISlot("Finger"),
    new UISlot("Tool"),
    new UISlot("Circle"),
    new UISlot("Swipe"),
    new UISlot("KeyTap"),
    new UISlot("ScreenTap")
  )

  private val queue = new LinkedBlockingQueue[Seq[(LeapObjectWrappers.WrappedLeapObject,  Seq[Message])]]()

  def updateMessage(sWObj: Seq[(LeapObjectWrappers.WrappedLeapObject,  Seq[Message])]) = {
    queue.put(sWObj)
  }

  private def internalUpdateMessage(wObj: WrappedLeapObject, msg: Seq[Message]) = {
    wObj match {
      case LHand(_, _, _, _, _, _, _, _, _, _, _) => slots(0).message.text = msg.toString()
      case LFinger(_, _, _, _, _, _) => slots(1).message.text = msg.toString()
      case LTool(_, _, _, _, _, _) => slots(2).message.text = msg.toString()
      case LGestureCircle(_, _, _, _) => slots(3).message.text = msg.toString()
      case LGestureSwipe(_, _, _, _) => slots(4).message.text = msg.toString()
      case LGestureKeyTap(_, _, _) => slots(5).message.text = msg.toString()
      case LGestureScreenTap(_, _, _) => slots(6).message.text = msg.toString()
    }
  }

  val consumerRunnable = new Runnable {

    def run() = {
      while (true) {
        val seq = queue.take()

        seq.foreach({ x =>
          val wObj = x._1
          val msg = x._2
          internalUpdateMessage(wObj, msg)
      })
      }
    }
  }

  val consumer = new Thread(consumerRunnable)


  lazy val ui = new GridBagPanel {
    val c = new Constraints
    val shouldFill = true
    if (shouldFill) {
      c.fill = Fill.Horizontal
    }

    c.fill = Fill.Horizontal
    c.gridx = 0;
    c.gridy = 0
    layout(new Label("Sending OSC to "+GlobalConfig.socketAddress)) = c

    slots.zipWithIndex.foreach {
      t => {
        val slot = t._1
        val idx = t._2
        c.fill = Fill.Horizontal
        c.gridx = 0;
        c.gridy = 1 + idx;
        layout(slot) = c
      }

    }

    consumer.start()
  }

  contents = ui

  override def closeOperation() = {
    LeapOSC.shutdown()
  }

  title = "LeapOSC"

}
