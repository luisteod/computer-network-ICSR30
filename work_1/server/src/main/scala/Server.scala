
import java.net.InetSocketAddress

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging
import akka.io.{IO, Udp}

import scala.io.StdIn

object UdpServer {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()

    val host = "localhost"
    val port = 9010
    val udpServer = system.actorOf(Props(classOf[UdpServer], host, port))

    println(s"UDP Server up. Enter Ctl+C to stop...")
    StdIn.readLine() // run until user cancels
  }
}

class UdpServer(host: String = "localhost", port: Int = 0) extends Actor {
  import context.system
  val log = Logging(context.system, this.getClass)

  override def preStart(): Unit = {
    log.info(s"Starting UDP Server on $host:$port")
    IO(Udp) ! Udp.Bind(self, new InetSocketAddress(host, port))
  }

  def receive: Receive = {
    case Udp.Bound(local) =>
      log.info(s"UDP Server is listening to ${local.getHostString}:${local.getPort}")

    case Udp.Received(data, remote) =>
      val msg = data.decodeString("utf-8").replaceAll("\n", " ")
      log.info(s"${remote.getHostString}:${remote.getPort} says: ${msg}")

      // echo back to sender
      sender ! Udp.Send(data, remote)

    case Udp.Unbind =>
      sender ! Udp.Unbind

    case Udp.Unbound =>
      context.stop(self)
  }

  override def postStop(): Unit = {
    log.info(s"Stopping UDP Server.")
  }
}
