import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.annotation.tailrec
import scala.collection.mutable

class Client(val address: InetAddress, val port: Int) {
  var time: Long = System.currentTimeMillis()

  def getClientKey(): String = s"$address:$port"
}

object SimpleUDPServer {

  val DatagramSizeBytes = 1400 // Best MTU  
  val clientsMap = mutable.HashMap[String, Client]()

  def main(args: Array[String]): Unit = {

    val port = 9876 // Port number to listen on
    val socket = new DatagramSocket(port) // Create a socket on the specified port
    println(s"Server is listening on port $port")

    serverLoop(socket)

    // socket.close() - Uncomment this if you want to close the socket after exiting the loop
  }

  def createNewConn(clientAddress: InetAddress, clientPort: Int): Unit = {
    val client = new Client(clientAddress, clientPort)
    clientsMap += (client.getClientKey() -> client)
  }

  def handlePacket(packet: DatagramPacket, clientKey: String): Unit = {
    // Convert the received data into a string
    val receivedData = new String(packet.getData, 0, packet.getLength)
    println(s"Received message: $receivedData")

    // Send an acknowledgment back to the client
    val response = s"Message received: $receivedData"
    val responseData = response.getBytes
    val clientAddress = packet.getAddress
    val clientPort = packet.getPort
    val responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort)

    // client.socket.send(responsePacket)
    // println(s"Sent acknowledgment to client: $response")
  }

  // Send packet to existing client or create a new client if the client is new
  def routePckt(packet: DatagramPacket): Unit = {
    val clientAddress = packet.getAddress
    val clientPort = packet.getPort
    val clientKey = s"$clientAddress:$clientPort"

    if (!clientsMap.contains(clientKey)) {
      // Verify if the packet is valid and process it
      createNewConn(clientAddress, clientPort)
    }
    handlePacket(packet, clientKey)
  }

  @tailrec
  def serverLoop(socket: DatagramSocket): Unit = {

    val buffer = new Array[Byte](DatagramSizeBytes) // to store incoming data
    val packet = new DatagramPacket(buffer, buffer.length)

    // Receive packet from client
    socket.receive(packet)
    Future(routePckt(packet)) // Process the received packet asynchronously

    Thread.sleep(500) // Sleep for 500 milliseconds
    serverLoop(socket)

  }

}
