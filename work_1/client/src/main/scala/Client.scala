// import akka.actor.{Actor, ActorSystem, Props}
// import akka.io.{IO, Udp}
// import java.net.InetSocketAddress
// import akka.util.ByteString

// class Client(remote: InetSocketAddress) extends Actor {
//   import context.system

//   override def preStart(): Unit = {
//     val message = "Hello, UDP Server!"
//     println(s"Sending message to server: $message")
//     val data = ByteString(message)
//     IO(Udp) ! Udp.Send(data, remote)
//   }

//   def receive: Receive = {
//     case Udp.Received(data, sender) =>
//       println(s"Received response from server: ${data.utf8String}")

//     case Udp.CommandFailed(_: Udp.Send) =>
//       println("Failed to send message")
//       context.stop(self)

//     case Udp.Unbind =>
//       context.stop(self)
//   }
// }

// object ClientApp {
//   def main(args: Array[String]): Unit = {
//     val system = ActorSystem("UdpClientSystem")
//     val serverAddress = new InetSocketAddress("localhost", 12345)
//     system.actorOf(Props(classOf[Client], serverAddress), name = "UdpClient")

//     println("Press ENTER to exit...")
//     scala.io.StdIn.readLine() // Keep client running until user input
//     system.terminate()
//   }
// }
