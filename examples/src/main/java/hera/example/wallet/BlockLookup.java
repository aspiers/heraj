/*
 * @copyright defined in LICENSE.txt
 */

package hera.example.wallet;

import hera.api.model.Block;
import hera.api.model.BlockHash;
import hera.api.model.BlockMetadata;
import hera.example.AbstractExample;
import hera.wallet.Wallet;
import hera.wallet.WalletBuilder;
import hera.wallet.WalletType;
import java.util.List;

public class BlockLookup extends AbstractExample {

  protected void blockLookup() {
    // make wallet object
    final Wallet wallet = new WalletBuilder()
        .withEndpoint(hostname)
        .withNonBlockingConnect()
        .build(WalletType.Naive);

    // get best block hash, height
    final BlockHash bestHash = wallet.getBestBlockHash();
    final long bestHeight = wallet.getBestBlockHeight();

    // query block by best block hash
    final Block blockByHash = wallet.getBlock(bestHash);
    System.out.println("Block by hash: " + blockByHash);

    // query block by best height
    final Block blockByHeight = wallet.getBlock(bestHeight);
    System.out.println("Block by height: " + blockByHeight);

    // query previous block by hash
    final Block previousBlock =
        wallet.getBlock(blockByHash.getPreviousHash());
    System.out.println("Previous block: " + previousBlock);

    // close the wallet
    wallet.close();
  }

  protected void blockHeaderLookup() {
    // make aergo client object
    final Wallet wallet = new WalletBuilder()
        .withEndpoint(hostname)
        .withNonBlockingConnect()
        .build(WalletType.Naive);

    // get best block hash, height
    final BlockHash bestHash = wallet.getBestBlockHash();
    final long bestHeight = wallet.getBestBlockHeight();

    // query block metadata corresponding to the block hash
    final BlockMetadata blockHeaderByHash = wallet.getBlockMetadata(bestHash);
    System.out.println("Block metadata by hash: " + blockHeaderByHash);

    // query block metadata corresponding to the block hash
    final BlockMetadata blockHeaderByHeight = wallet.getBlockMetadata(bestHeight);
    System.out.println("Block metadata by height: " + blockHeaderByHeight);

    // query 10 block metadatas starting from best block backward with best block hash
    final List<BlockMetadata> blockHeadersByHash =
        wallet.listBlockMetadatas(bestHash, 10);
    System.out.println("Block metadatas by hash: " + blockHeadersByHash);

    // query 10 block metadatas starting from best block backward with best block height
    final List<BlockMetadata> blockHeadersByHeight =
        wallet.listBlockMetadatas(bestHeight, 10);
    System.out.println("Block metadatas by height: " + blockHeadersByHeight);

    // close the client
    wallet.close();
  }

  @Override
  public void run() {
    blockLookup();
    blockHeaderLookup();
  }

  public static void main(String[] args) {
    new BlockLookup().run();
  }

}
