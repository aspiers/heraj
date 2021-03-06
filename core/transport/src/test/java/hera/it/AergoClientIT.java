/*
 * @copyright defined in LICENSE.txt
 */

package hera.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import hera.api.model.ChainIdHash;
import hera.client.AergoClient;
import hera.client.AergoClientBuilder;
import hera.exception.RpcConnectionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;

public class AergoClientIT extends AbstractIT {

  @Test
  public void testTryOnUnconnected() {
    final AergoClient client = new AergoClientBuilder()
        .withEndpoint("localhost:9999")
        .withNonBlockingConnect()
        .build();
    try {
      client.getBlockchainOperation().getBlockchainStatus();
      fail();
    } catch (RpcConnectionException e) {
      // good we expected this
    } finally {
      client.close();
    }
  }

  @Test
  public void testContextOnOtherThread() throws Exception {
    final AergoClient client = this.aergoClient;
    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    final Future<?> future = executorService.submit(new Runnable() {

      @Override
      public void run() {
        final ChainIdHash cached = client.getCachedChainIdHash();
        assertNotNull(cached);
      }
    });
    future.get();
  }

}
