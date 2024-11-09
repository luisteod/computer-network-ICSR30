import lib.ReceiveStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import java.net.DatagramSocket

object TestReceiveStream {

    def main(args: Array[String]): Unit = {
        val receiveStream = new ReceiveStream()
        println("Listening on port 9876...")
        var socket = new DatagramSocket(9876)
        var buffer = ByteBuffer.allocate(6024)
        receiveStream.receive(socket, buffer)

    }
    
}