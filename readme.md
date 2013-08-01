#intro#
This is yet another OSC control-app for the Leap Motion sensor, it uses scala, so it is crossplatform.
It allows to use different configurations for your various sessions (ip/port & messages).
You can send multiple osc messages per event or arbitrarily
modify the event data by altering the configuration file. This is done via using Twitter's Eval library that allows to use native scala files to store
configurations - so if you want to dig deeper, you can just add some custom scala code to your config.

* The UI contains info such as the messages that are being sent.
* The app is multi-threaded for ui/network updates, so it should be responsive.
* If you make use of the IDs: they have been made consistent so 2 hands or fingers will always get the IDs 0 & 1 etc.

!evening hack code quality!

# LEAP SDK  / Setup #
just drop the LeapJava.jar, Leap.dll/.so/* & LeapJava.dll/.so/* into the libraries folder.
Given that you have sbt + java installed doing a "sbt compile" should be enough.

# Useage #
sbt "run somepathToMy/ConfigFile.scala"

## example config##
```scala
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
```

# Multiple Messages and further processing #
Since the config makes uses of sequences of messages you can send multiple messages per event.
Example:
>    case x: LHand => Seq(Message("/hand"+x.id, x.pos.getX), Messages("/hand"+x.id, x.pos.getY))
>
>    case x: LHand => Seq(Message("/hand", scala.math.acos(x.pos.getX/x.pos.getZ)), Messages("/handOther", x.scale*x.sphRadius ))
>

Of course, since you can actually use any scala code, you can reformat and further process the events as you wish;
Other code has to be simply contained in your scala-config file.
>    ...
>
>    case x: LHand => Seq(Message("/finger", runningMean(x.length)))
>
>    ...
>
>    def runningMean() {...}
>
>    ....

# Data / Attributes #
Most is taken right out of the LeapSDK:

>   LVector is the underlying LeapVector type, so you can get the X,Y,Z coordinates,
>   roll/pitch/yaw & magnitude etc.
>
>   LHand(id: Int, pos: LVector, vel: LVector, norm: LVector,
>                   dir: LVector, sphCenter: LVector, sphRadius: Float,
>                   rotAxis: LVector, rotAngle: Float, trans: LVector,
>                   scale: Float)
>
>   LFinger(id: Int, pos: LVector, vel: LVector, dir: LVector,
>                     length: Float, width: Float)
>
>   LTool(id: Int, pos: LVector, vel: LVector, dir: LVector,
>                   length: Float, width: Float)
>
>   LGestureCircle(center: LVector, normal: LVector, progess: Float, radius: Float)
>
>   LGestureSwipe(pos: LVector, dir: LVector, start: LVector, speed: Float)
>
>   LGestureKeyTap(pos: LVector, dir: LVector, progress: Float)
>
>   LGestureScreenTap(pos: LVector, dir: LVector, progress: Float)
