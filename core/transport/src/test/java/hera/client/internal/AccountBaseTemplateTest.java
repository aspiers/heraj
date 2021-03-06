/*
 * @copyright defined in LICENSE.txt
 */

package hera.client.internal;

import static hera.util.TransportUtils.copyFrom;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import com.google.common.util.concurrent.ListenableFuture;
import hera.AbstractTestCase;
import hera.ThreadLocalContextProvider;
import hera.api.function.Function1;
import hera.api.model.AccountAddress;
import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.StakeInfo;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.key.AergoKeyGenerator;
import hera.key.Signer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import types.AergoRPCServiceGrpc.AergoRPCServiceFutureStub;
import types.Blockchain;
import types.Rpc;

@PrepareForTest({AergoRPCServiceFutureStub.class})
public class AccountBaseTemplateTest extends AbstractTestCase {

  protected final String password = randomUUID().toString();

  protected AccountBaseTemplate supplyAccountTemplateBase(
      final AergoRPCServiceFutureStub aergoService) {
    final AccountBaseTemplate accountTemplateBase = new AccountBaseTemplate();
    accountTemplateBase.aergoService = aergoService;
    accountTemplateBase.contextProvider = new ThreadLocalContextProvider(context, this);
    return accountTemplateBase;
  }

  @Override
  public void setUp() {
    super.setUp();
  }

  @Test
  public void testGetState() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    ListenableFuture<Blockchain.State> mockListenableFuture =
        service.submit(new Callable<Blockchain.State>() {
          @Override
          public Blockchain.State call() throws Exception {
            return Blockchain.State.newBuilder().build();
          }
        });
    when(aergoService.getState(any(Rpc.SingleBytes.class))).thenReturn(mockListenableFuture);

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);

    final Future<AccountState> accountStateFuture =
        accountTemplateBase.getStateFunction().apply(accountAddress);
    assertNotNull(accountStateFuture.get());
  }

  @Test
  public void testCreateName() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    final Future<TxHash> future =
        HerajFutures.success(new TxHash(BytesValue.of(randomUUID().toString().getBytes())));
    TransactionBaseTemplate mockTransactionBaseTemplate = mock(TransactionBaseTemplate.class);
    when(mockTransactionBaseTemplate.getCommitFunction())
        .thenReturn(new Function1<Transaction, Future<TxHash>>() {
          @Override
          public Future<TxHash> apply(Transaction t) {
            return future;
          }
        });

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.transactionBaseTemplate = mockTransactionBaseTemplate;

    final Signer key = new AergoKeyGenerator().create();
    final Future<TxHash> nameTxHash =
        accountTemplateBase.getCreateNameFunction().apply(key, randomUUID().toString(), 0L);
    assertNotNull(nameTxHash.get());
  }

  @Test
  public void testUpdateName() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    final Future<TxHash> future =
        HerajFutures.success(new TxHash(BytesValue.of(randomUUID().toString().getBytes())));
    TransactionBaseTemplate mockTransactionBaseTemplate = mock(TransactionBaseTemplate.class);
    when(mockTransactionBaseTemplate.getCommitFunction())
        .thenReturn(new Function1<Transaction, Future<TxHash>>() {
          @Override
          public Future<TxHash> apply(Transaction t) {
            return future;
          }
        });

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.transactionBaseTemplate = mockTransactionBaseTemplate;

    final Signer account = new AergoKeyGenerator().create();
    final AccountAddress newOwner = new AergoKeyGenerator().create().getAddress();
    final Future<TxHash> nameTxHash = accountTemplateBase.getUpdateNameFunction()
        .apply(account, randomUUID().toString(), newOwner, 0L);
    assertNotNull(nameTxHash.get());
  }

  @Test
  public void testGetNameOwner() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    ListenableFuture<Rpc.NameInfo> mockListenableFuture =
        service.submit(new Callable<Rpc.NameInfo>() {
          @Override
          public Rpc.NameInfo call() throws Exception {
            return Rpc.NameInfo.newBuilder()
                .setOwner(copyFrom(accountAddress.getBytesValue()))
                .build();
          }
        });
    when(aergoService.getNameInfo(any(Rpc.Name.class))).thenReturn(mockListenableFuture);

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.aergoService = aergoService;

    final Future<AccountAddress> nameTxHash =
        accountTemplateBase.getGetNameOwnerFunction().apply(randomUUID().toString(), 3L);
    assertNotNull(nameTxHash.get());
  }

  @Test
  public void testStake() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    final Future<TxHash> future =
        HerajFutures.success(new TxHash(BytesValue.of(randomUUID().toString().getBytes())));
    TransactionBaseTemplate mockTransactionBaseTemplate = mock(TransactionBaseTemplate.class);
    when(mockTransactionBaseTemplate.getCommitFunction())
        .thenReturn(new Function1<Transaction, Future<TxHash>>() {
          @Override
          public Future<TxHash> apply(Transaction t) {
            return future;
          }
        });

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.transactionBaseTemplate = mockTransactionBaseTemplate;

    final Signer signer = new AergoKeyGenerator().create();
    final Future<TxHash> staTxHash = accountTemplateBase.getStakingFunction()
        .apply(signer, Aer.ONE, 0L);
    assertNotNull(staTxHash.get());
  }

  @Test
  public void testUnstake() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    final Future<TxHash> future =
        HerajFutures.success(new TxHash(BytesValue.of(randomUUID().toString().getBytes())));
    TransactionBaseTemplate mockTransactionBaseTemplate = mock(TransactionBaseTemplate.class);
    when(mockTransactionBaseTemplate.getCommitFunction())
        .thenReturn(new Function1<Transaction, Future<TxHash>>() {
          @Override
          public Future<TxHash> apply(Transaction t) {
            return future;
          }
        });

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.transactionBaseTemplate = mockTransactionBaseTemplate;

    final Signer signer = new AergoKeyGenerator().create();
    final Future<TxHash> staTxHash =
        accountTemplateBase.getUnstakingFunction().apply(signer, Aer.ONE, 0L);
    assertNotNull(staTxHash.get());
  }

  @Test
  public void testGetStakingInfo() throws Exception {
    final AergoRPCServiceFutureStub aergoService = mock(AergoRPCServiceFutureStub.class);
    ListenableFuture<Rpc.Staking> mockListenableFuture =
        service.submit(new Callable<Rpc.Staking>() {
          @Override
          public Rpc.Staking call() throws Exception {
            return Rpc.Staking.newBuilder().setAmount(copyFrom(Aer.ONE)).build();
          }
        });
    when(aergoService.getStaking(any(Rpc.AccountAddress.class))).thenReturn(mockListenableFuture);

    final AccountBaseTemplate accountTemplateBase = supplyAccountTemplateBase(aergoService);
    accountTemplateBase.aergoService = aergoService;

    final Future<StakeInfo> stakingInfo =
        accountTemplateBase.getStakingInfoFunction().apply(accountAddress);
    assertNotNull(stakingInfo.get());
  }

}
