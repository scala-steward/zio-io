package zio.io.net

import zio._
import zio.stream._

private[net] trait Socket {

  /** Reads up to `maxBytes` from the peer.
    *
    * Returns `None` if the "end of stream" is reached, indicating there will be no more bytes sent.
    */
  def read(maxBytes: Int): UIO[Option[Chunk[Byte]]]

  /** Reads exactly `numBytes` from the peer in a single chunk.
    *
    * Returns a chunk with size < `numBytes` upon reaching the end of the stream.
    */
  def readN(numBytes: Int): UIO[Chunk[Byte]]

  /** Reads bytes from the socket as a stream. */
  def reads: UStream[Chunk[Byte]]

  /** Indicates that this channel will not read more data. Causes `End-Of-Stream` be signalled to `available`. */
  def endOfInput: UIO[Unit]

  /** Indicates to peer, we are done writing. * */
  def endOfOutput: UIO[Unit]

  def isOpen: UIO[Boolean]

  /** Writes `bytes` to the peer.
    *
    * Completes when the bytes are written to the socket.
    */
  def write(bytes: Chunk[Byte]): UIO[Unit]

  /** Writes the supplied stream of bytes to this socket via `write` semantics.
    */
  def writes: Sink[Nothing, Chunk[Byte], Nothing, Nothing]
}
