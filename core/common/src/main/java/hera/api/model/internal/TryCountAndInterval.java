/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model.internal;

import static hera.util.ValidationUtils.assertNotNull;
import static hera.util.ValidationUtils.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.util.ThreadUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;

@ApiAudience.Private
@ApiStability.Unstable
@ToString
@EqualsAndHashCode
public class TryCountAndInterval {

  protected static final Logger logger = getLogger(TryCountAndInterval.class);

  /**
   * Create TryCountAndInterval instance.
   *
   * @param tryCount a try count
   * @param interval an try interval
   * @return an TryCountAndInterval instance
   */
  public static TryCountAndInterval of(final int tryCount, final Time interval) {
    return new TryCountAndInterval(tryCount, interval);
  }

  @Getter
  protected final int count;

  @Getter
  protected final Time interval;

  /**
   * TryCountAndInterval constructor.
   *
   * @param tryCount a try count
   * @param interval an try interval
   */
  public TryCountAndInterval(final int tryCount, final Time interval) {
    assertTrue(tryCount >= 0, "Try count must be >= 0");
    assertNotNull(interval, "Interval hash must not null");
    this.count = tryCount;
    this.interval = interval;
  }

  public void trySleep() {
    ThreadUtils.trySleep(interval.toMilliseconds());
  }

}
