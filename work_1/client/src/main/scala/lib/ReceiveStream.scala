package lib
import java.net.{DatagramPacket, DatagramSocket}
import java.nio.ByteBuffer

class ReceiveStream {

  def receive(
      socket: DatagramSocket,
      buffer: ByteBuffer
  ): Unit = {

    while (true) {

      // Buffer to store incoming data
      val tmpBuffer = new Array[Byte](6024)
      // Create a DatagramPacket to receive data
      val packet = new DatagramPacket(tmpBuffer, tmpBuffer.length)

      // Receive the packet (this method blocks until a packet is received)
      socket.receive(packet)
      describePckt(packet)
      val pcktDataBuffer = getDataFromPckt(packet)
      // Copy data from sourceBuffer to destBuffer destBuffer.put(sourceBuffer)
      acummulateData(pcktDataBuffer, buffer, packet.getLength())
    }

    val decodedData = decodesDataStream(buffer.array())
    println("Decoded data: " + decodedData)
  }

  def acummulateData(srcBuffer: ByteBuffer, destBuffer: ByteBuffer, length: Int): Unit = {
    destBuffer.put(srcBuffer.array(), 0, length)
  }

  def decodesDataStream(buffer: Array[Byte]): String = {
    val data = new String(buffer)
    data
  }

  def describePckt(pckt: DatagramPacket): Unit = {

    val pcktData = pckt.getData()
    val pcktLen = pckt.getLength()

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
        s"Received packet from ${pckt.getAddress}:${pckt.getPort}" + "\n" +
        s"Sequence Number : $seqNumberInt" + "\n" +
        s"Checksum :  $checksumInt" + "\n" +
        s"Ack Number : $ackNumberInt" + "\n" +
        s"Data : $payload" +
        "\n-------------------------------------------------------------------\n"
    )

  }

  def getDataFromPckt(pckt: DatagramPacket): ByteBuffer = {
    val pcktData = pckt.getData()
    val pcktLen = pckt.getLength()
    val pcktDataBuffer = ByteBuffer.allocate(pcktLen)
    pcktDataBuffer.put(pcktData, 10, pcktLen)
    pcktDataBuffer
  }

  def getSeqNumberFromPckt(pckt: DatagramPacket): Int = {
    val pcktData = pckt.getData()
    val seqNumberBytes = pcktData.slice(0, 4)
    val seqNumberInt = ByteBuffer.wrap(seqNumberBytes).getInt()
    seqNumberInt
  }

  def getChecksumFromPckt(pckt: DatagramPacket): Short = {
    val pcktData = pckt.getData()
    val checksumBytes = pcktData.slice(4, 6)
    val checksumInt = ByteBuffer.wrap(checksumBytes).getShort()
    checksumInt
  }

  def getAckNumberFromPckt(pckt: DatagramPacket): Int = {
    val pcktData = pckt.getData()
    val ackNumberBytes = pcktData.slice(6, 10)
    val ackNumberInt = ByteBuffer.wrap(ackNumberBytes).getInt()
    ackNumberInt
  }
}
