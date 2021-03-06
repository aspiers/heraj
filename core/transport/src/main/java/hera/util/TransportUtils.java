/*
 * @copyright defined in LICENSE.txt
 */

package hera.util;

import static hera.util.NumberUtils.byteArrayToPositive;
import static hera.util.NumberUtils.positiveToByteArray;

import com.google.protobuf.ByteString;
import hera.api.model.Aer;
import hera.api.model.BlockHash;
import hera.api.model.BytesValue;
import hera.api.model.TxHash;
import hera.exception.HerajException;

public class TransportUtils {

  /**
   * Transform a raw byte array to {@link ByteString} in protobuf. If either raw byte array is null
   * or raw byte array is empty, return {@link ByteString#EMPTY}
   *
   * @param rawBytes a raw bytes
   * @return protobuf {@link ByteString}
   */
  public static ByteString copyFrom(final byte[] rawBytes) {
    if (null == rawBytes || 0 == rawBytes.length) {
      return ByteString.EMPTY;
    }
    return ByteString.copyFrom(rawBytes);
  }

  /**
   * Transform {@link BytesValue} to {@link ByteString} in protobuf. If either bytesValue or value
   * of bytesValue is null, return {@link ByteString#EMPTY}
   *
   * @param bytesValue {@link BytesValue}
   * @return protobuf {@link ByteString}
   */
  public static ByteString copyFrom(final BytesValue bytesValue) {
    if (null == bytesValue || null == bytesValue.getValue()) {
      return ByteString.EMPTY;
    }
    return ByteString.copyFrom(bytesValue.getValue());
  }

  /**
   * Transform {@link Aer} to {@link ByteString} in protobuf. If aer isn null, return
   * {@link ByteString#EMPTY}.
   *
   * @param aer an aer
   * @return protobuf {@link ByteString}
   */
  public static ByteString copyFrom(final Aer aer) {
    if (null == aer || Aer.EMPTY == aer) {
      return ByteString.EMPTY;
    }
    return ByteString.copyFrom(positiveToByteArray(aer.getValue()));
  }

  /**
   * Convert long value to {@link ByteString} in a little endian form.
   *
   * @param longValue long value
   * @return converted protobuf {@link ByteString} in a little endian
   */
  public static ByteString copyFrom(final long longValue) {
    final byte[] rawBytes = new byte[8];
    rawBytes[0] = (byte) (0xFF & (longValue));
    rawBytes[1] = (byte) (0xFF & (longValue >> 8));
    rawBytes[2] = (byte) (0xFF & (longValue >> 16));
    rawBytes[3] = (byte) (0xFF & (longValue >> 24));
    rawBytes[4] = (byte) (0xFF & (longValue >> 32));
    rawBytes[5] = (byte) (0xFF & (longValue >> 40));
    rawBytes[6] = (byte) (0xFF & (longValue >> 48));
    rawBytes[7] = (byte) (0xFF & (longValue >> 56));
    return ByteString.copyFrom(rawBytes);
  }

  /**
   * Parse protobuf {@link ByteString} to {@link BytesValue}.
   *
   * @param bytesString a protobuf ByteString
   * @return parsed {@link BytesValue}.
   */
  public static BytesValue parseToBytesValue(final ByteString bytesString) {
    if (null == bytesString || ByteString.EMPTY.equals(bytesString)) {
      return BytesValue.EMPTY;
    }
    return BytesValue.of(bytesString.toByteArray());
  }

  /**
   * Parse raw aer to {@link Aer}.
   *
   * @param rawAer a raw aer
   * @return parsed {@link Aer}.
   */
  public static Aer parseToAer(final byte[] rawAer) {
    if (null == rawAer || 0 == rawAer.length) {
      return Aer.EMPTY;
    }
    return Aer.of(byteArrayToPositive(rawAer));
  }

  /**
   * Parse raw aer to {@link Aer}.
   *
   * @param rawAer a raw aer
   * @return parsed {@link Aer}.
   */
  public static Aer parseToAer(final ByteString rawAer) {
    if (null == rawAer || ByteString.EMPTY.equals(rawAer)) {
      return Aer.EMPTY;
    }
    return parseToAer(rawAer.toByteArray());
  }

  /**
   * Transform {@link ByteString} to {@link TxHash}.
   *
   * @param byteString a grpc byteString
   * @return txHash
   */
  public static TxHash parseToTxHash(final ByteString byteString) {
    if (null == byteString || ByteString.EMPTY == byteString) {
      return new TxHash(BytesValue.EMPTY);
    }
    return new TxHash(BytesValue.of(byteString.toByteArray()));
  }

  /**
   * Transform {@link ByteString} to {@link BlockHash}.
   *
   * @param byteString a grpc byteString
   * @return blockHash
   */
  public static BlockHash parseToBlockHash(final ByteString byteString) {
    if (null == byteString || ByteString.EMPTY == byteString) {
      return new BlockHash(BytesValue.EMPTY);
    }
    return new BlockHash(BytesValue.of(byteString.toByteArray()));
  }

  /**
   * Sha256 digest and return digested in hexa.
   *
   * @param string a string
   * @return a hexa encoded digested raw bytes
   */
  public static String sha256AndEncodeHexa(final String string) {
    return sha256AndEncodeHexa(string.getBytes());
  }

  /**
   * Sha256 digest and return digested in hexa.
   *
   * @param rawBytes a raw bytes.
   * @return a hexa encoded digested raw bytes
   */
  public static String sha256AndEncodeHexa(final byte[] rawBytes) {
    return HexUtils.encode(Sha256Utils.digest(rawBytes));
  }

}
