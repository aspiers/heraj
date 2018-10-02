/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static hera.api.model.BytesValue.of;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hera.AbstractTestCase;
import hera.api.AccountAsyncOperation;
import hera.api.model.Account;
import hera.api.model.AccountAddress;
import hera.api.model.Authentication;
import hera.api.model.EncryptedPrivateKey;
import hera.api.tupleorerror.ResultOrError;
import hera.api.tupleorerror.ResultOrErrorFuture;
import java.util.List;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import types.AergoRPCServiceGrpc.AergoRPCServiceFutureStub;

@SuppressWarnings("unchecked")
@PrepareForTest({AergoRPCServiceFutureStub.class})
public class AccountEitherTemplateTest extends AbstractTestCase {

  protected final AccountAddress ACCOUNT_ADDRESS =
      new AccountAddress(of(new byte[] {AccountAddress.ADDRESS_VERSION}));

  protected final String PASSWORD = randomUUID().toString();

  @Test
  public void testList() throws Exception {
    ResultOrErrorFuture<List<Account>> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.list()).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    final ResultOrError<List<Account>> accountListFuture = accountTemplate.list();
    assertNotNull(accountListFuture);
  }

  @Test
  public void testCreate() throws Exception {
    ResultOrErrorFuture<Account> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.create(anyString())).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    final ResultOrError<Account> createdAccount = accountTemplate.create(randomUUID().toString());
    assertNotNull(createdAccount);
  }

  @Test
  public void testGet() throws Exception {
    ResultOrErrorFuture<Account> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.get(any(AccountAddress.class))).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    final ResultOrError<Account> account = accountTemplate.get(ACCOUNT_ADDRESS);
    assertNotNull(account);
  }

  @Test
  public void testLock() throws Exception {
    ResultOrErrorFuture<Boolean> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.lock(any())).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    ResultOrError<Boolean> lockResult =
        accountTemplate.lock(Authentication.of(ACCOUNT_ADDRESS, PASSWORD));
    assertNotNull(lockResult);
  }

  @Test
  public void testUnlock() throws Exception {
    ResultOrErrorFuture<Boolean> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.unlock(any())).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    ResultOrError<Boolean> unlockResult =
        accountTemplate.unlock(Authentication.of(ACCOUNT_ADDRESS, PASSWORD));
    assertNotNull(unlockResult);
  }

  @Test
  public void testImportKey() throws Exception {
    ResultOrErrorFuture<Account> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.importKey(any(), any(), any())).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    ResultOrError<Account> importedAccount = accountTemplate.importKey(
        new EncryptedPrivateKey(of(new byte[] {EncryptedPrivateKey.PRIVATE_KEY_VERSION})),
        PASSWORD);
    assertNotNull(importedAccount);
  }

  @Test
  public void testExportKey() throws Exception {
    ResultOrErrorFuture<EncryptedPrivateKey> futureMock = mock(ResultOrErrorFuture.class);
    when(futureMock.get(anyLong(), any())).thenReturn(mock(ResultOrError.class));
    AccountAsyncOperation asyncOperationMock = mock(AccountAsyncOperation.class);
    when(asyncOperationMock.exportKey(any())).thenReturn(futureMock);

    final AccountEitherTemplate accountTemplate = new AccountEitherTemplate(asyncOperationMock);

    ResultOrError<EncryptedPrivateKey> exportedKey =
        accountTemplate.exportKey(Authentication.of(ACCOUNT_ADDRESS, PASSWORD));
    assertNotNull(exportedKey);
  }

}
