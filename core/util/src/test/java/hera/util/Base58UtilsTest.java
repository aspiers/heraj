/*
 * @copyright defined in LICENSE.txt
 */

package hera.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import hera.AbstractTestCase;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Test;

public class Base58UtilsTest extends AbstractTestCase {

  public static final byte[] DECODED = "Hello Aergo this is hera the queen".getBytes();

  public static final String ENCODED = "2dvaTHm2BaTDBPnm6R2thH1wHtyVdfTpBd98uxhdLxeTdNV";

  public static final byte[] DECODED_WITH_CHECKSUM = {0x42, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
      13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33};

  public static final String ENCODED_WITH_CHECKSUM =
      "AmJaNDXoPbBRn9XHh9onKbDKuAzj88n5Bzt7KniYA78qUEc5EwBd";

  @Test
  public void testEncode() {
    assertEquals(ENCODED, Base58Utils.encode(DECODED));
    assertEquals("", Base58Utils.encode(new byte[0]));
    assertEquals("", Base58Utils.encode(null));
  }

  @Test
  public void testDecode() throws IOException {
    assertTrue(Arrays.equals(DECODED, Base58Utils.decode(ENCODED)));
    assertTrue(Arrays.equals(new byte[0], Base58Utils.decode("")));
    assertTrue(Arrays.equals(new byte[0], Base58Utils.decode(null)));
  }

  @Test(expected = IOException.class)
  public void shouldThrowExceptionWhenDecode() throws IOException {
    Base58Utils.decode("=");
  }

  @Test
  public void testEncodeWithCheck() {
    assertEquals(ENCODED_WITH_CHECKSUM, Base58Utils.encodeWithCheck(DECODED_WITH_CHECKSUM));
    assertEquals("", Base58Utils.encodeWithCheck(new byte[0]));
    assertEquals("", Base58Utils.encodeWithCheck(null));
  }

  @Test
  public void testDecodeWithCheck() throws IOException {
    assertTrue(
        Arrays.equals(DECODED_WITH_CHECKSUM, Base58Utils.decodeWithCheck(ENCODED_WITH_CHECKSUM)));
    assertTrue(Arrays.equals(new byte[0], Base58Utils.decodeWithCheck("")));
    assertTrue(Arrays.equals(new byte[0], Base58Utils.decodeWithCheck(null)));
  }

  @Test(expected = IOException.class)
  public void shouldThrowExceptionWhenDecodeWithCheck() throws IOException {
    Base58Utils.decode("=");
  }

}
