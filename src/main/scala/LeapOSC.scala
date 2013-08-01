import com.leapmotion.leap.{Gesture, Controller}
import de.sciss.osc.UDP
import java.io.{File, IOException}
import sun.text.normalizer.UBiDiProps
import swing.event.EditDone
import swing._
import event.EditDone
import swing.event.EditDone
import swing.event.EditDone

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
object LeapOSC extends App {

  import de.sciss.osc._
  import Implicits._

  var listener: LeapListener = null;
  var controller: Controller = null;

  var oscSender: OscSender = null

  override def main(args: Array[String]) = {

    if (args.length < 1) {
      println("Config file missing!")
      println("Usage: sbt \"run <myconfigfile.scala>\"")
    }

    ConfigImporter.loadConfig(args(0))

    // loading failed when not doing it manually in my environment
    val libPath = new File("./libraries").getCanonicalPath() + "/";
    System.load(libPath + "Leap.dll")
    System.load(libPath + "LeapJava.dll")

    val ui = new UI()
    ui.open()

    oscSender = new OscSender()

    // Gesture handling
    val listener = new LeapListener(oscSender, ui)
    val controller = new Controller
    controller.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES)

    controller.enableGesture(Gesture.Type.TYPE_CIRCLE, GlobalConfig.useGestures)
    controller.enableGesture(Gesture.Type.TYPE_SWIPE, GlobalConfig.useGestures)
    controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP, GlobalConfig.useGestures)
    controller.enableGesture(Gesture.Type.TYPE_KEY_TAP, GlobalConfig.useGestures)

    controller.addListener(listener)

    oscSender.start


    try{
      // dirty
      ui.consumer.join()
    } catch {
      case e:Exception => e.printStackTrace()
    }

  }

  // this is called from the ui
   def shutdown() {
    if (controller != null && listener != null) {
      controller.removeListener(listener)
    }

    if(oscSender != null) {
      oscSender.shutdown
    }

    System.exit(0)
  }
}
