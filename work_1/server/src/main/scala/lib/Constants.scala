package lib

object Constants {
  // Define your constants here
  val DefaultPort: Int = 9876
  val BufferSize: Int = 1024
  val HostAddress: String = "127.0.0.1"
  
  // Add more constants as needed
  val MaxRetries: Int = 5
  val TimeoutDuration: Long = 3000L // in milliseconds
  val RTT = 1000

  val maxBulkPackets = 5
  val headerSize = 10
  val MTU = 1400
}