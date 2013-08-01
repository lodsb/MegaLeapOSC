import com.leapmotion.leap._

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

object LeapObjectWrappers {

  type LVector = com.leapmotion.leap.Vector

  abstract class WrappedLeapObject

  case class LNothing(id: Int) extends WrappedLeapObject

  case class LHand(id: Int, pos: LVector, vel: LVector, norm: LVector,
                   dir: LVector, sphCenter: LVector, sphRadius: Float,
                   rotAxis: LVector, rotAngle: Float, trans: LVector,
                   scale: Float) extends WrappedLeapObject

  case class LFinger(id: Int, pos: LVector, vel: LVector, dir: LVector,
                     length: Float, width: Float) extends WrappedLeapObject

  case class LTool(id: Int, pos: LVector, vel: LVector, dir: LVector,
                   length: Float, width: Float) extends WrappedLeapObject

  case class LGestureCircle(center: LVector, normal: LVector, progess: Float, radius: Float) extends WrappedLeapObject

  case class LGestureSwipe(pos: LVector, dir: LVector, start: LVector, speed: Float) extends WrappedLeapObject

  case class LGestureKeyTap(pos: LVector, dir: LVector, progress: Float) extends WrappedLeapObject

  case class LGestureScreenTap(pos: LVector, dir: LVector, progress: Float) extends WrappedLeapObject


  def wrap(x: Hand, lastFrame: Frame, id:Int): WrappedLeapObject =
    LHand(id, x.palmPosition(), x.palmVelocity(),
      x.palmNormal(), x.direction(), x.sphereCenter(), x.sphereRadius(),
      x.rotationAxis(lastFrame), x.rotationAngle(lastFrame), x.translation(lastFrame),
      x.scaleFactor(lastFrame))


  def wrap(f: Finger, id: Int): WrappedLeapObject =
    LFinger(id, f.tipPosition(), f.tipVelocity(), f.direction(), f.length(), f.width())


  def wrap(t: Pointable, id: Int): WrappedLeapObject =
    LTool(id, t.tipPosition(), t.tipVelocity(), t.direction(), t.length(), t.width())


  def wrap(c: CircleGesture): WrappedLeapObject =
    LGestureCircle(c.center(), c.normal(), c.progress(), c.radius())


  def wrap(s: SwipeGesture): WrappedLeapObject =
    LGestureSwipe(s.position(), s.direction(), s.startPosition(), s.speed())


  def wrap(k: KeyTapGesture): WrappedLeapObject =
    LGestureKeyTap(k.position(), k.direction(), k.progress())


  def wrap(t: ScreenTapGesture): WrappedLeapObject =
    LGestureScreenTap(t.position(), t.direction(), t.progress())

}
