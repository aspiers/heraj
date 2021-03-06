/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import static hera.util.EncodingUtils.decodeBase58WithCheck;
import static hera.util.EncodingUtils.encodeBase58WithCheck;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.encode.Encodable;
import hera.exception.DecodingFailureException;
import hera.spec.resolver.EncryptedPrivateKeySpec;
import hera.util.BytesValueUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@ApiAudience.Public
@ApiStability.Unstable
@EqualsAndHashCode
public class EncryptedPrivateKey implements Encodable {

  /**
   * Create {@code EncryptedPrivateKey} with a base58 with checksum encoded value.
   *
   * @param encoded a base58 with checksum encoded encoded value
   * @return created {@link EncryptedPrivateKey}
   *
   * @throws DecodingFailureException if decoding failed
   */
  @ApiAudience.Public
  public static EncryptedPrivateKey of(final String encoded) {
    return new EncryptedPrivateKey(encoded);
  }

  /**
   * Create {@code EncryptedPrivateKey}.
   *
   * @param bytesValue {@link BytesValue}
   * @return created {@link EncryptedPrivateKey}
   *
   */
  @ApiAudience.Private
  public static EncryptedPrivateKey of(final BytesValue bytesValue) {
    return new EncryptedPrivateKey(bytesValue);
  }

  @Getter
  protected final BytesValue bytesValue;

  /**
   * EncryptedPrivateKey constructor.
   *
   * @param encoded a base58 with checksum encoded encoded value
   *
   * @throws DecodingFailureException if decoding failed
   */
  @ApiAudience.Public
  public EncryptedPrivateKey(final String encoded) {
    this(decodeBase58WithCheck(encoded));
  }

  /**
   * EncryptedPrivateKey constructor.
   *
   * @param bytesValue {@link BytesValue}
   *
   */
  @ApiAudience.Private
  public EncryptedPrivateKey(final BytesValue bytesValue) {
    if (BytesValue.EMPTY != bytesValue) {
      BytesValueUtils.validatePrefix(bytesValue, EncryptedPrivateKeySpec.PREFIX);
    }
    this.bytesValue = bytesValue;
  }

  @Override
  public String getEncoded() {
    return encodeBase58WithCheck(getBytesValue());
  }

  @Override
  public String toString() {
    return getEncoded();
  }

}
