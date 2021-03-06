/*
 * @copyright defined in LICENSE.txt
 */

package hera.client.stream;

import static org.slf4j.LoggerFactory.getLogger;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class GrpcStreamSubscription<T> implements hera.api.model.Subscription<T> {

  protected final transient Logger logger = getLogger(getClass());

  protected final io.grpc.Context.CancellableContext context;

  @Override
  public void unsubscribe() {
    logger.info("Unsubscribe subscription: {}", getClass());
    synchronized (this) {
      context.close();
    }
  }

  @Override
  public boolean isUnsubscribed() {
    return context.isCancelled();
  }

}
