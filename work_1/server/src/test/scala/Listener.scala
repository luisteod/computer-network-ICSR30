import java.net.{DatagramPacket, DatagramSocket}
import java.nio.ByteBuffer

object UdpReceiver {
  def main(args: Array[String]): Unit = {
    // Create a DatagramSocket to listen on port 9876
    val socket = new DatagramSocket(9876)
    println("Listening on port 9876...")

    // Buffer to store incoming data
    val buffer = new Array[Byte](6024)
    while (true) {
      try {
        // Create a DatagramPacket to receive data
        val packet = new DatagramPacket(buffer, buffer.length)

        // Receive the packet (this method blocks until a packet is received)
        socket.receive(packet)

        val pcktData = packet.getData()
        val pcktLen = packet.getLength()

        val seqNumberBytes = pcktData.slice(0, 4)
        val seqNumberInt = ByteBuffer.wrap(seqNumberBytes).getInt()

        val checksumBytes = pcktData.slice(4, 6)
        val checksumInt = ByteBuffer.wrap(checksumBytes).getShort()

        val ackNumberBytes = pcktData.slice(6, 10)
        val ackNumberInt = ByteBuffer.wrap(ackNumberBytes).getInt()

        // Convert the received bytes to a string
        val payload = new String(pcktData, 10, pcktLen)

        // Print the received data
        println(
          "\n-------------------------------------------------------------------\n" +
            s"Received packet from ${packet.getAddress}:${packet.getPort}" + "\n" +
            s"Sequence Number : $seqNumberInt" + "\n" +
            s"Checksum :  $checksumInt" + "\n" +
            s"Ack Number : $ackNumberInt" + "\n" +
            s"Data : $payload" +
            "\n-------------------------------------------------------------------\n"
        )
      } catch {
        case e: Exception =>
          println(s"Error receiving packet: ${e.getMessage}")
      }
    }
  }
}
