/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.model;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

@ApiAudience.Public
@ApiStability.Unstable
@Value
@Builder(builderMethodName = "newBuilder")
public class StakeInfo {

  @NonNull
  @Default
  AccountAddress address = AccountAddress.EMPTY;

  @NonNull
  @Default
  Aer amount = Aer.ZERO;

  long blockNumber;

}
