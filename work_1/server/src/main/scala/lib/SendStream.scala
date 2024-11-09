package lib
import java.nio.ByteBuffer
import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import Constants._

// packet structure: [sequence number (4 bytes), checksum (2 bytes), ack number (4 bytes), data (1400 bytes)]

class SendStream {

  // After send a couple of packets, the sender must wait for the receiver to acknowledge the receipt of the data.
  def send(
      buffer: ByteBuffer,
      socket: DatagramSocket,
      address: String,
      port: Int
  ): Unit = {

    // string address to InetAddress
    val inetAddress: InetAddress = InetAddress.getByName(address)

    var dynNumBulkPckts = 1
    // Set num of packets that has inside the buffer
    val totalNumPckts = buffer.remaining() / MTU + 1

    var sendedPackets = 1
    var confirmedSendedPackets = 0

    // Incrementally send packets util reach the maxBulkPackets
    while (buffer.hasRemaining) {

      // Send packets in bulks size of dynNumBulkPckts
      for (iteratorNumBulkPackets <- 0 until dynNumBulkPckts if buffer.hasRemaining) {

        println("dynNumBulkPckts: " + dynNumBulkPckts)
        
        val dataChunkSize = Math.min(buffer.remaining(), MTU)
        val dataChunk = new Array[Byte](dataChunkSize)
        buffer.get(dataChunk)
        val dataPackeged = packageData(dataChunk, sequenceNumber = sendedPackets)
        val packet = new DatagramPacket(
          dataPackeged,
          dataPackeged.length,
          inetAddress,
          port
        )

        socket.send(packet)

        if (dynNumBulkPckts < maxBulkPackets)
          dynNumBulkPckts += 1
        
        sendedPackets += 1
      }

    //   // Wait for the receiver to acknowledge the receipt of the data and read the acknowledgment number
    //   val ackPacket =
    //     new DatagramPacket(new Array[Byte](headerSize), headerSize)
    //   socket.receive(ackPacket)
    //   // get the acknowledgment number
    //   val ackNumber = ByteBuffer.wrap(ackPacket.getData).getInt(6)
    //   // move the buffer to the next packet
    //   buffer.position(ackNumber)

    }
    println("Sending request to Luis")
  }

  def packageData(data: Array[Byte], sequenceNumber: Int): Array[Byte] = {
    val buffer = ByteBuffer.allocate(headerSize + data.length)
    buffer.putInt(sequenceNumber)
    buffer.putShort(0)
    buffer.putInt(0)
    buffer.put(data)
    // buffer
    buffer.array()
  }
}
