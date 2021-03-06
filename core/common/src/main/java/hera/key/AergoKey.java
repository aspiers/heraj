
/*
 * @copyright defined in LICENSE.txt
 */

package hera.key;

import static hera.util.IoUtils.from;
import static hera.util.Sha256Utils.digest;
import static org.slf4j.LoggerFactory.getLogger;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.encode.Decoder;
import hera.api.encode.Encoder;
import hera.api.model.AccountAddress;
import hera.api.model.BytesValue;
import hera.api.model.EncryptedPrivateKey;
import hera.api.model.Hash;
import hera.api.model.RawTransaction;
import hera.api.model.Signature;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.exception.HerajException;
import hera.spec.resolver.AddressResolver;
import hera.spec.resolver.EncryptedPrivateKeyResolver;
import hera.spec.resolver.SignatureResolver;
import hera.spec.resolver.TransactionHashResolver;
import hera.util.NumberUtils;
import hera.util.pki.ECDSAKey;
import hera.util.pki.ECDSAKeyGenerator;
import hera.util.pki.ECDSASignature;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slf4j.Logger;

@ApiAudience.Public
@ApiStability.Unstable
@EqualsAndHashCode(exclude = {"logger", "verifier"})
public class AergoKey implements KeyPair, Signer, MessageSigner {

  /**
   * Create a key pair with encoded encrypted private key and password.
   *
   * @param encodedEncryptedPrivateKey base58 with checksum encoded encrypted private key
   * @param password password to decrypt
   * @return key instance
   */
  public static AergoKey of(final String encodedEncryptedPrivateKey, final String password) {
    return new AergoKey(encodedEncryptedPrivateKey, password);
  }

  /**
   * Create a key pair with encrypted private key and password.
   *
   * @param encryptedPrivateKey encrypted private key
   * @param password password to decrypt
   * @return key instance
   */
  public static AergoKey of(final EncryptedPrivateKey encryptedPrivateKey, final String password) {
    return new AergoKey(encryptedPrivateKey, password);
  }

  protected final transient Logger logger = getLogger(getClass());

  protected final AergoSignVerifier verifier = new AergoSignVerifier();

  protected final ECDSAKey ecdsakey;

  @Getter
  protected final AccountAddress address;

  /**
   * AergoKey constructor.
   *
   * @param encodedEncryptedPrivateKey base58 with checksum encoded encrypted private key
   * @param password password to decrypt
   */
  public AergoKey(final String encodedEncryptedPrivateKey, final String password) {
    this(new EncryptedPrivateKey(encodedEncryptedPrivateKey), password);
  }

  /**
   * AergoKey constructor.
   *
   * @param encryptedPrivateKey encrypted private key
   * @param password password to decrypt
   */
  public AergoKey(final EncryptedPrivateKey encryptedPrivateKey, final String password) {
    try {
      final BytesValue decryptedBytes =
          EncryptedPrivateKeyResolver.decrypt(encryptedPrivateKey, password);
      final byte[] rawPrivateKey = decryptedBytes.getValue();
      this.ecdsakey = new ECDSAKeyGenerator().create(new BigInteger(1, rawPrivateKey));
      this.address = AddressResolver.deriveAddress(ecdsakey.getPublicKey());
    } catch (final Exception e) {
      throw new HerajException(e);
    }
  }

  /**
   * AergoKey constructor.
   *
   * @param ecdsakey keypair
   */
  public AergoKey(final ECDSAKey ecdsakey) {
    this.ecdsakey = ecdsakey;
    this.address = AddressResolver.deriveAddress(ecdsakey.getPublicKey());
  }

  @Override
  public PrivateKey getPrivateKey() {
    return ecdsakey.getPrivateKey();
  }

  @Override
  public PublicKey getPublicKey() {
    return ecdsakey.getPublicKey();
  }

  @Override
  public AccountAddress getPrincipal() {
    return getAddress();
  }

  @Override
  public Transaction sign(final RawTransaction rawTransaction) {
    try {
      logger.debug("Sign raw transaction: {}", rawTransaction);
      final TxHash withoutSignature = TransactionHashResolver.calculateHash(rawTransaction);
      final ECDSASignature ecdsaSignature =
          ecdsakey.sign(withoutSignature.getBytesValue().getValue());
      final Signature signature =
          SignatureResolver.serialize(ecdsaSignature, ecdsakey.getParams().getN());
      logger.trace("Raw signature: {}", ecdsaSignature);
      logger.trace("Serialized signature: {}", signature);
      final TxHash withSignature = TransactionHashResolver.calculateHash(rawTransaction, signature);
      final Transaction transaction = Transaction.newBuilder()
          .rawTransaction(rawTransaction)
          .signature(signature)
          .hash(withSignature)
          .build();
      return transaction;
    } catch (HerajException e) {
      throw e;
    } catch (Exception e) {
      throw new HerajException(e);
    }
  }

  @Override
  public String signMessage(final String message) {
    return signMessage(message, Encoder.Base64);
  }

  @Override
  public String signMessage(final String message, final Encoder encoder) {
    try {
      final Signature signature = signMessage(new BytesValue(message.getBytes()));
      final byte[] rawSignature = signature.getSign().getValue();
      return from(encoder.encode(new ByteArrayInputStream(rawSignature)));
    } catch (HerajException e) {
      throw e;
    } catch (Exception e) {
      throw new HerajException(e);
    }
  }

  @Override
  public Signature signMessage(final BytesValue message) {
    try {
      logger.debug("Sign to message: {}", message);
      final Hash hash = new Hash(new BytesValue(digest(message.getValue())));
      logger.debug("Hashed message: {}", hash);
      final ECDSASignature ecdsaSignature = ecdsakey.sign(hash.getBytesValue().getValue());
      final Signature signature =
          SignatureResolver.serialize(ecdsaSignature, ecdsakey.getParams().getN());
      logger.trace("Serialized signature: {}", signature);
      return signature;
    } catch (Exception e) {
      throw new HerajException(e);
    }
  }

  /**
   * Check if {@code Transaction} is valid.
   *
   * @param transaction transaction to verify
   * @return if valid
   */
  public boolean verify(final Transaction transaction) {
    return verifier.verify(transaction);
  }

  /**
   * Check if {@code base64EncodedSignature} is valid for current {@code accountAddress} and
   * {@code message}. It hashes {@code message} and verify hashed one.
   *
   * @param message a message
   * @param base64EncodedSignature a base64 encoded signature
   * @return if valid
   */
  public boolean verifyMessage(final String message, final String base64EncodedSignature) {
    return verifier.verifyMessage(getAddress(), message, base64EncodedSignature);
  }

  /**
   * Check if {@code base64EncodedSignature} is valid for current {@code accountAddress} and
   * {@code message}. It hashes {@code message} and verify hashed one.
   *
   * @param message a message
   * @param encodedSignature an encoded signature
   * @param decoder a decoder to decode encoded signature
   *
   * @return if valid
   */
  public boolean verifyMessage(String message, String encodedSignature, Decoder decoder) {
    return verifier.verifyMessage(getAddress(), message, encodedSignature, decoder);
  }

  /**
   * Check if {@code signature} is valid for current {@code accountAddress} and {@code message}. It
   * hashes {@code message} and verify hashed one.
   *
   * @param message a message
   * @param signature signature
   * @return if valid
   */
  public boolean verifyMessage(final BytesValue message, final Signature signature) {
    return verifier.verifyMessage(getAddress(), message, signature);
  }

  /**
   * Check if {@code signature} is valid for {@code hash} and current address.
   *
   * @param hashedMessage a hashed message
   * @param signature signature to verify
   * @return if valid
   */
  public boolean verifyMessage(final Hash hashedMessage, final Signature signature) {
    return verifier.verifyMessage(getAddress(), hashedMessage, signature);
  }

  /**
   * Return encrypted private key.
   *
   * @param password encrypt key
   * @return encrypted key
   */
  public EncryptedPrivateKey export(final String password) {
    try {
      final BytesValue privateKeyBytes = new BytesValue(getRawPrivateKey());
      return EncryptedPrivateKeyResolver.encrypt(privateKeyBytes, password);
    } catch (Exception e) {
      throw new HerajException(e);
    }
  }

  protected byte[] getRawPrivateKey() {
    final org.bouncycastle.jce.interfaces.ECPrivateKey ecPrivateKey =
        (org.bouncycastle.jce.interfaces.ECPrivateKey) getPrivateKey();
    final BigInteger d = ecPrivateKey.getD();
    return NumberUtils.positiveToByteArray(d);
  }

  @Override
  public String toString() {
    return String.format("AergoKey(address=%s)", getAddress());
  }

}
