import LeapObjectWrappers._
import de.sciss.osc.Implicits._
import de.sciss.osc.Message
import java.net.SocketAddress

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

object GlobalConfig {
  // should gestures be tracked?
  var useGestures = false

  // network address & port
  var socketAddress: SocketAddress = localhost -> 7200;

  // add your messages here / change this stuff
  def map(leapObject: LeapObjectWrappers.WrappedLeapObject) : Seq[Message] = {
     mapFunction(leapObject)
  }

  var mapFunction: LeapObjectWrappers.WrappedLeapObject=> Seq[Message] = { leapObject =>
    leapObject match {
      case x: LHand => Seq(Message("/hand"+x.id, x.pos.getX))
      case x: LFinger => Seq(Message("/finger"+x.id, x.pos.getX))
      case x: LTool => Seq(Message("/tool"+x.id, x.pos.getX))
      case x: LGestureCircle => Seq(Message("/circle", x.progess, x.radius))
      case x: LGestureSwipe => Seq(Message("/swipe", x.speed))
      case x: LGestureKeyTap => Seq(Message("/keytap", x.progress))
      case x: LGestureScreenTap => Seq(Message("/screentap", x.progress))
    }
  }
}
