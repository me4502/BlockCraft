package com.me4502.blockcraft.blockchain;

public class BlockElement {

    private int index;
    private String previousHash;
    private long timestamp;
    private BlockData data;
    private String hash;

    public BlockElement(int index, String previousHash, long timestamp, BlockData data, String hash) {
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.data = data;
        this.hash = hash;
    }

    public int getIndex() {
        return this.index;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public BlockData getData() {
        return this.data;
    }
}
