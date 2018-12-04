/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static hera.TransportConstants.CONTRACT_DEPLOY_EITHER;
import static hera.TransportConstants.CONTRACT_EXECUTE_EITHER;
import static hera.TransportConstants.CONTRACT_GETINTERFACE_EITHER;
import static hera.TransportConstants.CONTRACT_GETRECEIPT_EITHER;
import static hera.TransportConstants.CONTRACT_QUERY_EITHER;
import static hera.api.tupleorerror.Functions.identify;

import hera.ContextProvider;
import hera.ContextProviderInjectable;
import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.ContractEitherOperation;
import hera.api.model.Account;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInterface;
import hera.api.model.ContractInvocation;
import hera.api.model.ContractResult;
import hera.api.model.ContractTxHash;
import hera.api.model.ContractTxReceipt;
import hera.api.model.Fee;
import hera.api.tupleorerror.Function1;
import hera.api.tupleorerror.Function3;
import hera.api.tupleorerror.ResultOrError;
import hera.api.tupleorerror.ResultOrErrorFuture;
import hera.strategy.StrategyChain;
import io.grpc.ManagedChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@ApiAudience.Private
@ApiStability.Unstable
public class ContractEitherTemplate
    implements ContractEitherOperation, ChannelInjectable, ContextProviderInjectable {

  protected ContractBaseTemplate contractBaseTemplate = new ContractBaseTemplate();

  @Setter
  protected ContextProvider contextProvider;

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final StrategyChain strategyChain = StrategyChain.of(contextProvider.get());

  @Override
  public void setChannel(final ManagedChannel channel) {
    contractBaseTemplate.setChannel(channel);
  }

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final Function1<ContractTxHash,
      ResultOrErrorFuture<ContractTxReceipt>> receiptFunction = getStrategyChain().apply(
          identify(contractBaseTemplate.getReceiptFunction(), CONTRACT_GETRECEIPT_EITHER));

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final Function3<Account, ContractDefinition, Fee,
      ResultOrErrorFuture<ContractTxHash>> deployFunction = getStrategyChain()
          .apply(identify(contractBaseTemplate.getDeployFunction(), CONTRACT_DEPLOY_EITHER));

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final Function1<ContractAddress,
      ResultOrErrorFuture<ContractInterface>> contractInterfaceFunction =
          getStrategyChain().apply(identify(contractBaseTemplate.getContractInterfaceFunction(),
              CONTRACT_GETINTERFACE_EITHER));

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final Function3<Account, ContractInvocation, Fee,
      ResultOrErrorFuture<ContractTxHash>> executeFunction = getStrategyChain()
          .apply(identify(contractBaseTemplate.getExecuteFunction(), CONTRACT_EXECUTE_EITHER));

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private final Function1<ContractInvocation, ResultOrErrorFuture<ContractResult>> queryFunction =
      getStrategyChain()
          .apply(identify(contractBaseTemplate.getQueryFunction(), CONTRACT_QUERY_EITHER));

  @Override
  public ResultOrError<ContractTxReceipt> getReceipt(final ContractTxHash contractTxHash) {
    return getReceiptFunction().apply(contractTxHash).get();
  }

  @Override
  public ResultOrError<ContractTxHash> deploy(final Account creator,
      final ContractDefinition contractDefinition, final Fee fee) {
    return getDeployFunction().apply(creator, contractDefinition, fee).get();
  }

  @Override
  public ResultOrError<ContractInterface> getContractInterface(
      final ContractAddress contractAddress) {
    return getContractInterfaceFunction().apply(contractAddress).get();
  }

  @Override
  public ResultOrError<ContractTxHash> execute(final Account executor,
      final ContractInvocation contractInvocation, final Fee fee) {
    return getExecuteFunction().apply(executor, contractInvocation, fee).get();
  }

  @Override
  public ResultOrError<ContractResult> query(final ContractInvocation contractInvocation) {
    return getQueryFunction().apply(contractInvocation).get();
  }

}
