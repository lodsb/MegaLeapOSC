import LeapObjectWrappers._
import java.net.SocketAddress

new Configuration {
  import de.sciss.osc._
  import Implicits._

  // should gestures be tracked?
  val useGestures = false

  // network address & port
  val socketAddress: SocketAddress = localhost -> 1338;

  // add your messages here / change this stuff
  def map(leapObject: LeapObjectWrappers.WrappedLeapObject) : Seq[Message] = {

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
