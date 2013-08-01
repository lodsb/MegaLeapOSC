import de.sciss.osc.{Message, PacketCodec, UDP}
import java.util.concurrent.LinkedBlockingQueue

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
class OscSender {

  var udpClient : UDP.Client= null

  val consumerRunnable = new Runnable {
    var running = true

    // OSC init
    val cfg = UDP.Config()
    cfg.codec = PacketCodec().doublesAsFloats().booleansAsInts()
    udpClient = UDP.Client(GlobalConfig.socketAddress, cfg)
    udpClient.connect()

    def run() = {
      while (running) {
        val seq = queue.take()

        seq.foreach {
          x => {
            val messages = x._2
            try {
              messages.foreach( msg => udpClient ! msg)
            }catch {
              case e:Exception => e.printStackTrace()
            }

          }
        }
      }
    }
  }

  private val queue = new LinkedBlockingQueue[Seq[(LeapObjectWrappers.WrappedLeapObject,  Seq[Message])]]()

  def sendMessages( seq: Seq[(LeapObjectWrappers.WrappedLeapObject,  Seq[Message])]) = {
    queue.put(seq)
  }

  val consumer = new Thread(consumerRunnable)

  def start = consumer.start()

  def shutdown = {
    consumerRunnable.running = false
    if (udpClient != null ) {
      udpClient.close()
    }
  }


}
