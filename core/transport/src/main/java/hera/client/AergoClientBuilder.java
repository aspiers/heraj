/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static org.slf4j.LoggerFactory.getLogger;

import hera.Context;
import hera.ContextProvider;
import hera.DefaultConstants;
import hera.Strategy;
import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.model.internal.Time;
import hera.exception.RpcException;
import hera.strategy.ConnectStrategy;
import hera.strategy.JustRetryStrategy;
import hera.strategy.NettyConnectStrategy;
import hera.strategy.OkHttpConnectStrategy;
import hera.strategy.PlainTextChannelStrategy;
import hera.strategy.SecurityConfigurationStrategy;
import hera.strategy.TimeoutStrategy;
import hera.strategy.TlsChannelStrategy;
import hera.util.Configuration;
import hera.util.conf.InMemoryConfiguration;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

@ApiAudience.Public
@ApiStability.Unstable
public class AergoClientBuilder implements ClientConfiguer<AergoClientBuilder> {

  public static final String SCOPE = "global";

  protected static final Map<Class<?>, Strategy> necessaryStrategyMap;

  static {
    final Map<Class<?>, Strategy> map = new HashMap<Class<?>, Strategy>();
    map.put(ConnectStrategy.class, new NettyConnectStrategy());
    map.put(TimeoutStrategy.class, new TimeoutStrategy(DefaultConstants.DEFAULT_TIMEOUT));
    map.put(SecurityConfigurationStrategy.class, new PlainTextChannelStrategy());
    necessaryStrategyMap = Collections.unmodifiableMap(map);
  }

  protected final Logger logger = getLogger(getClass());

  protected final Map<Class<?>, Strategy> strategyMap = new LinkedHashMap<Class<?>, Strategy>();

  protected Configuration configuration = new InMemoryConfiguration();

  @Override
  public AergoClientBuilder addConfiguration(final String key, final String value) {
    configuration.define(key, value);
    return this;
  }

  @Override
  public AergoClientBuilder withEndpoint(final String endpoint) {
    configuration.define("endpoint", endpoint);
    return this;
  }

  @Override
  public AergoClientBuilder withNonBlockingConnect() {
    strategyMap.put(ConnectStrategy.class, new NettyConnectStrategy());
    return this;
  }

  @Override
  public AergoClientBuilder withBlockingConnect() {
    strategyMap.put(ConnectStrategy.class, new OkHttpConnectStrategy());
    return this;
  }

  @Override
  public AergoClientBuilder withTimeout(final long timeout, final TimeUnit unit) {
    strategyMap.put(TimeoutStrategy.class, new TimeoutStrategy(timeout, unit));
    return this;
  }

  @Override
  public AergoClientBuilder withRetry(final int count, final long interval, final TimeUnit unit) {
    strategyMap.put(JustRetryStrategy.class, new JustRetryStrategy(count, Time.of(interval, unit)));
    return this;
  }

  @Override
  public AergoClientBuilder withPlainText() {
    strategyMap.put(SecurityConfigurationStrategy.class, new PlainTextChannelStrategy());
    return this;
  }

  @Override
  public AergoClientBuilder withTransportSecurity(final String serverCommonName,
      final String serverCertPath, final String clientCertPath, final String clientKeyPath) {
    try {
      return withTransportSecurity(serverCommonName, new FileInputStream(serverCertPath),
          new FileInputStream(clientCertPath), new FileInputStream(clientKeyPath));
    } catch (Exception e) {
      throw new RpcException(e);
    }
  }

  @Override
  public AergoClientBuilder withTransportSecurity(final String serverCommonName,
      final InputStream serverCertInputStream,
      final InputStream clientCertInputStream, final InputStream clientKeyInputStream) {
    strategyMap.put(SecurityConfigurationStrategy.class, new TlsChannelStrategy(serverCommonName,
        serverCertInputStream, clientCertInputStream, clientKeyInputStream));
    return this;
  }

  /**
   * Build {@link AergoClient} with the current context. If necessary strategy is not provided,fill
   * necessary strategy.
   *
   * @return {@link AergoClient}
   */
  public AergoClient build() {
    final AergoClient client = new AergoClient(buildContext());
    return client;
  }

  protected Context buildContext() {
    for (final Class<?> necessaryClass : necessaryStrategyMap.keySet()) {
      if (!strategyMap.containsKey(necessaryClass)) {
        strategyMap.put(necessaryClass, necessaryStrategyMap.get(necessaryClass));
      }
    }
    final Context context = ContextProvider.defaultProvider.get()
        .withStrategies(new HashSet<Strategy>(this.strategyMap.values()))
        .withConfiguration(configuration)
        .withScope(AergoClientBuilder.SCOPE);
    logger.info("Global context: {}", context);
    return context;
  }

}
