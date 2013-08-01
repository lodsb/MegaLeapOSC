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

import com.twitter.util.Eval
import de.sciss.osc.Implicits._
import de.sciss.osc.Message
import java.io.File
import java.net.SocketAddress

abstract class Configuration {
  val useGestures: Boolean

  // network address & port
  val socketAddress: SocketAddress

  // add your messages here / change this stuff
  def map(leapObject: LeapObjectWrappers.WrappedLeapObject): Seq[Message]
}

object ConfigImporter {
  private val eval = new Eval()

  def loadConfig(filename: String) = {
    val file = new File(filename)

    val config = this.eval[Configuration](file)

    //dirt-style
    GlobalConfig.mapFunction = config.map
    GlobalConfig.socketAddress = config.socketAddress
    GlobalConfig.useGestures = config.useGestures


  }


}
