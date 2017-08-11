package com.me4502.blockcraft.blockchain;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public class BlockChain {

    private List<BlockElement> chain = Lists.newArrayList(getInitialBlock());

    public BlockElement getInitialBlock() {
        return new BlockElement(
                0,
                "",
                System.nanoTime(),
                new BlockData(),
                "abcdefghijklmnopqrstuvwxyz"
        );
    }

    public BlockElement getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public String calculateHash(int index, String previousHash, long timestamp, BlockData data) {
        return Integer.toHexString(Objects.hash(index, previousHash, timestamp, data));
    }

    public boolean isNodeValid(BlockElement newNode, BlockElement previousNode) {
        if (previousNode.getIndex() + 1 != newNode.getIndex()) {
            System.out.println("Node has invalid index!");
            return false;
        } else if (!Objects.equals(previousNode.getHash(), newNode.getPreviousHash())) {
            System.out.println("Node has invalid hash!");
            return false;
        } else if (!Objects.equals(calculateHash(newNode.getIndex(), newNode.getPreviousHash(),
                newNode.getTimestamp(), newNode.getData()), newNode.getHash())) {
            System.out.println("Node has invalid hash!");
            return false;
        }

        return true;
    }

    public BlockElement generateNextBlock(BlockData data) {
        BlockElement previousBlock = getLatestBlock();
        int nextIndex = previousBlock.getIndex() + 1;
        long nextTimestamp = System.nanoTime();
        String nextHash = calculateHash(nextIndex, previousBlock.getHash(), nextTimestamp, data);
        return new BlockElement(nextIndex, previousBlock.getHash(), nextTimestamp, data, nextHash);
    }
}
