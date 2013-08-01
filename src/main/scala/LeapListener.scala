import de.sciss.osc.{UDP, Transport}
import java.io.IOException;
import java.lang.Math;
import com.leapmotion.leap._
;
import com.leapmotion.leap.Gesture.State
import sun.text.normalizer.UBiDiProps
;

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
class LeapListener(var osc: OscSender, ui: UI) extends Listener {
  override def onInit(con: Controller) = {
    println("initialized")
  }

  override def onExit(con: Controller) = {
    println("exited")
  }

  override def onConnect(con: Controller) = {
    println("connected")
  }

  override def onDisconnect(con: Controller) = {
    println("disconnected")
  }

  private var frameCounter = 0;
  private val frameUpdates = 5;
  private var lastFrame: Frame = null;

  override def onFrame(con: Controller) = {
    import scala.collection.JavaConverters._

    val frame = con.frame()


    frameCounter = frameCounter + 1;

    if (frameCounter % frameUpdates == 0) {
      frameCounter = 0
      lastFrame = frame
    }


    val hands = frame.hands().asScala

    var wrappedObjects = Seq[LeapObjectWrappers.WrappedLeapObject]()

    try {
    if (frame != null && !frame.hands().empty()) {
      val numHands = hands.size


      hands.foreach({
        hand => {
          wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(hand, lastFrame, hand.id() % numHands)

          val leapPointables = hand.pointables()

          if (!leapPointables.empty()) {

            val pointables = leapPointables.asScala

            // this is done to have somewhat consistent ids throughout the whole session
            // e.g. first hand has always id 0
            val numPointables = pointables.foldLeft(0) {
              (x, y) => if (y.isTool) {
                x + 1
              } else {
                x
              }
            }

            pointables.foreach({

              pointable => {
                if (pointable.isTool) {
                  wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(pointable, pointable.id() % numPointables)
                }
              }
            })


          }

          val leapFingers = hand.fingers()

          if(!leapFingers.empty()) {
            val fingers = leapFingers.asScala

            val numFingers = fingers.foldLeft(0) {
              (x, y) => if (y.isFinger) {
                x + 1
              } else {
                x
              }
            }

            fingers.foreach {
              finger => {
                wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(finger, finger.id() % numFingers)
              }

            }
          }
        }
      })

      val gestures = frame.gestures().asScala

      gestures.foreach {
        gesture =>
          gesture.`type`() match {
            case Gesture.Type.TYPE_CIRCLE => wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(new CircleGesture(gesture))
            case Gesture.Type.TYPE_SWIPE => wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(new SwipeGesture(gesture))
            case Gesture.Type.TYPE_SCREEN_TAP => wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(new ScreenTapGesture(gesture))
            case Gesture.Type.TYPE_KEY_TAP => wrappedObjects = wrappedObjects :+ LeapObjectWrappers.wrap(new KeyTapGesture(gesture))
            case _ => {}
          }
      }


    }

    } catch {
      case e: Exception => e.printStackTrace()
    }
      // this mapping could be done somewhere else. However if it is done in both ui and osc threads,
      // then functions called within the message mapping in Configuration may perform non-deterministically
      val messageMap = wrappedObjects.map( wObj => (wObj, GlobalConfig.map(wObj)))

      ui.updateMessage(messageMap)
      osc.sendMessages(messageMap)


    }




}
