/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import io.grpc.ManagedChannel;

public interface ChannelInjectable {
  void setChannel(ManagedChannel channel);
}
