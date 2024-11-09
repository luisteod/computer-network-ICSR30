import lib.SendStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import java.net.DatagramSocket

object TestSendStream {

    val resourcePath = "/home/luis/Documents/UTFPR/7-Periodo/redes/work_1/server/src/main/resources/payload"

    def main(args: Array[String]): Unit = {
        val sendStream = new SendStream()
        // Read the file
        val file = new File(resourcePath)
        val stream = ByteBuffer.wrap(Files.readAllBytes(file.toPath))
        var socket = new DatagramSocket()
        sendStream.send(stream, socket, "127.0.0.1", 9876)

    }
    
}