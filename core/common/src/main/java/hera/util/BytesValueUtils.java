/*
 * @copyright defined in LICENSE.txt
 */

package hera.util;

import hera.api.model.BytesValue;
import hera.exception.HerajException;
import java.util.Arrays;

public class BytesValueUtils {

  /**
   * Validate a prefix of the target.
   *
   * @param target byte array to validate
   * @param prefix a prefix
   */
  public static void validatePrefix(final BytesValue target, final byte prefix) {
    validatePrefix(target.getValue(), prefix);
  }

  /**
   * Validate a prefix of the target.
   *
   * @param target byte array to validate
   * @param prefix a prefix
   */
  public static void validatePrefix(final byte[] target, final byte prefix) {
    if (null == target || 0 == target.length) {
      throw new HerajException("Bytes is empty");
    }
    if (prefix != target[0]) {
      throw new HerajException("Prefix should be " + prefix + " but was " + target[0]);
    }
  }

  /**
   * Append a prefix to the given {@code target}.
   *
   * @param target a target
   * @param prefix a prefix to append
   * @return a bytes value with prefix appended
   */
  public static BytesValue append(final BytesValue target, final byte prefix) {
    if (null == target) {
      return BytesValue.of(append(BytesValue.EMPTY.getValue(), prefix));
    }
    return BytesValue.of(append(target.getValue(), prefix));
  }

  /**
   * Append a prefix to the given {@code target}.
   *
   * @param target a target
   * @param prefix a prefix to append
   * @return a bytes value with prefix appended
   */
  public static byte[] append(final byte[] target, final byte prefix) {
    if (null == target || 0 == target.length) {
      return new byte[] {prefix};
    }
    final byte[] ret = new byte[target.length + 1];
    ret[0] = prefix;
    System.arraycopy(target, 0, ret, 1, target.length);
    return ret;
  }

  /**
   * Trim prefix of the {@code target}. Return {@link BytesValue#EMPTY} if {@code source} is null or
   * empty.
   *
   * @param target a byte array
   * @return byte array with prefix trimmed
   */
  public static BytesValue trimPrefix(final BytesValue target) {
    if (null == target || BytesValue.EMPTY == target) {
      return BytesValue.EMPTY;
    }
    return BytesValue.of(trimPrefix(target.getValue()));
  }

  /**
   * Trim prefix of the {@code target}. Return empty byte array if {@code source} is null or empty.
   *
   * @param source a byte array
   * @return byte array with prefix trimmed
   */
  public static byte[] trimPrefix(final byte[] source) {
    if (null == source || 0 == source.length) {
      return new byte[0];
    }
    return Arrays.copyOfRange(source, 1, source.length);
  }

}
