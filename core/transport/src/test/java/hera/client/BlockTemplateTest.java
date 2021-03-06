/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static hera.api.model.BytesValue.of;
import static hera.client.ClientConstants.BLOCK_GET_BLOCK_BY_HASH;
import static hera.client.ClientConstants.BLOCK_GET_BLOCK_BY_HEIGHT;
import static hera.client.ClientConstants.BLOCK_GET_METADATA_BY_HASH;
import static hera.client.ClientConstants.BLOCK_GET_METADATA_BY_HEIGHT;
import static hera.client.ClientConstants.BLOCK_LIST_METADATAS_BY_HASH;
import static hera.client.ClientConstants.BLOCK_LIST_METADATAS_BY_HEIGHT;
import static hera.client.ClientConstants.BLOCK_SUBSCRIBE_BLOCK;
import static hera.client.ClientConstants.BLOCK_SUBSCRIBE_BLOCKMETADATA;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hera.AbstractTestCase;
import hera.ContextProvider;
import hera.api.function.Function1;
import hera.api.function.Function2;
import hera.api.function.WithIdentity;
import hera.api.model.Block;
import hera.api.model.BlockHash;
import hera.api.model.BlockMetadata;
import hera.api.model.StreamObserver;
import hera.api.model.Subscription;
import hera.client.internal.BlockBaseTemplate;
import hera.client.internal.HerajFutures;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import types.AergoRPCServiceGrpc.AergoRPCServiceFutureStub;

@PrepareForTest({AergoRPCServiceFutureStub.class})
public class BlockTemplateTest extends AbstractTestCase {

  @Override
  public void setUp() {
    super.setUp();
  }

  protected BlockTemplate supplyBlockTemplate(
      final BlockBaseTemplate blockBaseTemplate) {
    final BlockTemplate blockTemplate = new BlockTemplate();
    blockTemplate.blockBaseTemplate = blockBaseTemplate;
    blockTemplate.setContextProvider(ContextProvider.defaultProvider);
    return blockTemplate;
  }

  @Test
  public void testGetBlockMetadataByHash() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Future<BlockMetadata> future = HerajFutures.success(BlockMetadata.newBuilder().build());
    when(base.getBlockMetatdataByHashFunction())
        .thenReturn(new Function1<BlockHash, Future<BlockMetadata>>() {
          @Override
          public Future<BlockMetadata> apply(BlockHash t) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final BlockMetadata blockMetadata =
        blockTemplate.getBlockMetadata(new BlockHash(of(randomUUID().toString().getBytes())));
    assertNotNull(blockMetadata);
    assertEquals(BLOCK_GET_METADATA_BY_HASH,
        ((WithIdentity) blockTemplate.getBlockMetadataByHashFunction()).getIdentity());
  }

  @Test
  public void testGetBlockMetadataByHeight() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Future<BlockMetadata> future = HerajFutures.success(BlockMetadata.newBuilder().build());
    when(base.getBlockMetadataByHeightFunction())
        .thenReturn(new Function1<Long, Future<BlockMetadata>>() {
          @Override
          public Future<BlockMetadata> apply(Long t) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final BlockMetadata blockMetadata =
        blockTemplate.getBlockMetadata(randomUUID().hashCode());
    assertNotNull(blockMetadata);
    assertEquals(BLOCK_GET_METADATA_BY_HEIGHT,
        ((WithIdentity) blockTemplate.getBlockMetadataByHeightFunction()).getIdentity());
  }

  @Test
  public void testListBlockMetadatasByHash() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final List<BlockMetadata> list = new ArrayList<BlockMetadata>();
    final Future<List<BlockMetadata>> future = HerajFutures.success(list);
    when(base.getListBlockMetadatasByHashFunction())
        .thenReturn(new Function2<BlockHash, Integer, Future<List<BlockMetadata>>>() {
          @Override
          public Future<List<BlockMetadata>> apply(BlockHash t1, Integer t2) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final List<BlockMetadata> blockMetadatas = blockTemplate.listBlockMetadatas(
        new BlockHash(of(randomUUID().toString().getBytes())), randomUUID().hashCode());
    assertNotNull(blockMetadatas);
    assertEquals(BLOCK_LIST_METADATAS_BY_HASH,
        ((WithIdentity) blockTemplate.getListBlockMetadatasByHashFunction()).getIdentity());
  }

  @Test
  public void testListBlockMetadatasByHeight() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final List<BlockMetadata> list = new ArrayList<BlockMetadata>();
    final Future<List<BlockMetadata>> future = HerajFutures.success(list);
    when(base.getListBlockMetadatasByHeightFunction())
        .thenReturn(new Function2<Long, Integer, Future<List<BlockMetadata>>>() {
          @Override
          public Future<List<BlockMetadata>> apply(Long t1, Integer t2) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final List<BlockMetadata> blockMetadatas =
        blockTemplate.listBlockMetadatas(randomUUID().hashCode(), randomUUID().hashCode());
    assertNotNull(blockMetadatas);
    assertEquals(BLOCK_LIST_METADATAS_BY_HEIGHT,
        ((WithIdentity) blockTemplate.getListBlockMetadatasByHeightFunction()).getIdentity());
  }

  @Test
  public void testGetBlockByHash() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Future<Block> future = HerajFutures.success(Block.newBuilder().build());
    when(base.getBlockByHashFunction())
        .thenReturn(new Function1<BlockHash, Future<Block>>() {
          @Override
          public Future<Block> apply(BlockHash t) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final Block block =
        blockTemplate.getBlock(new BlockHash(of(randomUUID().toString().getBytes())));
    assertNotNull(block);
    assertEquals(BLOCK_GET_BLOCK_BY_HASH,
        ((WithIdentity) blockTemplate.getBlockByHashFunction()).getIdentity());
  }

  @Test
  public void testGetBlockByHeight() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Future<Block> future = HerajFutures.success(Block.newBuilder().build());
    when(base.getBlockByHeightFunction())
        .thenReturn(new Function1<Long, Future<Block>>() {
          @Override
          public Future<Block> apply(Long t) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final Block block = blockTemplate.getBlock(randomUUID().hashCode());
    assertNotNull(block);
    assertEquals(BLOCK_GET_BLOCK_BY_HEIGHT,
        ((WithIdentity) blockTemplate.getBlockByHeightFunction()).getIdentity());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSubscribeBlockMetadata() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Subscription<BlockMetadata> mockSubscription = mock(Subscription.class);
    final Future<Subscription<BlockMetadata>> future = HerajFutures.success(mockSubscription);
    when(base.getSubscribeBlockMetadataFunction())
        .thenReturn(new Function1<StreamObserver<BlockMetadata>,
            Future<Subscription<BlockMetadata>>>() {

          @Override
          public Future<Subscription<BlockMetadata>> apply(
              StreamObserver<BlockMetadata> t2) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final Subscription<BlockMetadata> subscription = blockTemplate.subscribeNewBlockMetadata(null);
    assertNotNull(subscription);
    assertEquals(BLOCK_SUBSCRIBE_BLOCKMETADATA,
        ((WithIdentity) blockTemplate.getSubscribeBlockMetadataFunction()).getIdentity());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSubscribeBlock() {
    final BlockBaseTemplate base = mock(BlockBaseTemplate.class);
    final Subscription<Block> mockSubscription = mock(Subscription.class);
    final Future<Subscription<Block>> future = HerajFutures.success(mockSubscription);
    when(base.getSubscribeBlockFunction())
        .thenReturn(new Function1<StreamObserver<Block>, Future<Subscription<Block>>>() {

          @Override
          public Future<Subscription<Block>> apply(StreamObserver<Block> t2) {
            return future;
          }
        });

    final BlockTemplate blockTemplate = supplyBlockTemplate(base);

    final Subscription<Block> subscription = blockTemplate.subscribeNewBlock(null);
    assertNotNull(subscription);
    assertEquals(BLOCK_SUBSCRIBE_BLOCK,
        ((WithIdentity) blockTemplate.getSubscribeBlockFunction()).getIdentity());
  }

}
