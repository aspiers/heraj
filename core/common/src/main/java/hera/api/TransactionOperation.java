/*
 * @copyright defined in LICENSE.txt
 */

package hera.api;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.model.AccountAddress;
import hera.api.model.Aer;
import hera.api.model.Transaction;
import hera.api.model.TxHash;

/**
 * Provide transaction related operations. It provides followings:
 *
 * <ul>
 *  <li>lookup transaction</li>
 *  <li>commit transaction</li>
 *  <li>sending aergo with a key stored in server keystore</li>
 * </ul>
 *
 * @author bylee, Taeik Lim
 *
 */
@ApiAudience.Public
@ApiStability.Unstable
public interface TransactionOperation {

  /**
   * Get transaction.
   *
   * @param txHash transaction's hash
   * @return transaction
   */
  Transaction getTransaction(TxHash txHash);

  /**
   * Commit transaction.
   *
   * @param transaction transaction to commit
   * @return transaction hash
   */
  TxHash commit(Transaction transaction);

  /**
   * Send transaction. This method automatically fill nonce, sign and commit in a server. This
   * method is valid only if sender is stored in a server key store. Make sure that {@code sender}
   * is unlocked.
   *
   * @param sender aergo sender
   * @param recipient aergo recipient
   * @param amount aergo amount
   * @return transaction hash
   */
  TxHash send(AccountAddress sender, AccountAddress recipient, Aer amount);

}
