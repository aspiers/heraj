/*
 * @copyright defined in LICENSE.txt
 */

package hera.exception;

import static org.slf4j.LoggerFactory.getLogger;

import hera.util.ExceptionConverter;
import org.slf4j.Logger;

public class WalletExceptionConverter implements ExceptionConverter<WalletException> {

  protected final Logger logger = getLogger(getClass());

  @Override
  public WalletException convert(final Throwable t) {
    logger.debug("Handle exception {}", t.toString());
    if (t instanceof WalletException) {
      return (WalletException) t;
    } else {
      return new WalletException(t);
    }
  }

}
