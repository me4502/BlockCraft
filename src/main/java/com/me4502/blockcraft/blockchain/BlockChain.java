package com.me4502.blockcraft.blockchain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

public class BlockChain {

    private static Gson gson = new GsonBuilder().registerTypeAdapter(BlockElement.class, (JsonSerializer<BlockElement>) (src, typeOfSrc, context) -> {
        JsonObject object = new JsonObject();

        object.addProperty("index", src.getIndex());
        object.addProperty("previousHash", src.getPreviousHash());
        object.addProperty("hash", src.getHash());
        object.addProperty("timestamp", src.getTimestamp());

        JsonObject dataObject = new JsonObject();
        JsonObject blockPosObject = new JsonObject();
        blockPosObject.addProperty("x", src.getData().blockPos.getX());
        blockPosObject.addProperty("y", src.getData().blockPos.getY());
        blockPosObject.addProperty("z", src.getData().blockPos.getZ());

        dataObject.add("blockPos", blockPosObject);

        String name = String.valueOf(Block.REGISTRY.getNameForObject(src.getData().blockState.getBlock()));
        if (src.getData().blockState.getBlock().getMetaFromState(src.getData().blockState) > 0) {
            name += ";" + src.getData().blockState.getBlock().getMetaFromState(src.getData().blockState);
        }

        dataObject.addProperty("blockState", name);
        object.add("data", dataObject);

        return object;
    }).registerTypeAdapter(BlockElement.class, (JsonDeserializer<BlockElement>) (json, typeOfT, context) -> {
        int index = json.getAsJsonObject().get("index").getAsInt();
        String previousHash = json.getAsJsonObject().get("previousHash").getAsString();
        String hash = json.getAsJsonObject().get("hash").getAsString();
        long timestamp = json.getAsJsonObject().get("timestamp").getAsLong();

        JsonObject dataObject = json.getAsJsonObject().getAsJsonObject("data");

        JsonObject blockPosObject = dataObject.getAsJsonObject("blockPos");
        BlockPos blockPos = new BlockPos(blockPosObject.get("x").getAsInt(), blockPosObject.get("y").getAsInt(), blockPosObject.get("z").getAsInt());
        String serialisedBlockState = dataObject.get("blockState").getAsString();
        IBlockState blockState;
        if (serialisedBlockState.contains(";")) {
            String[] bits = serialisedBlockState.split(";");
            Block block = Block.getBlockFromName(bits[0]);
            blockState = block.getStateFromMeta(Integer.parseInt(bits[1]));
        } else {
            Block block = Block.getBlockFromName(serialisedBlockState);
            blockState = block.getDefaultState();
        }
        return new BlockElement(index, previousHash, timestamp, new BlockData(blockPos, blockState), hash);
    }).create();

    private String worldName;

    private List<BlockElement> chainList = Lists.newArrayList(getInitialBlock());

    public BlockChain(String worldName) {
        this.worldName = worldName;
    }

    public BlockElement getInitialBlock() {
        return new BlockElement(
                0,
                "",
                System.nanoTime(),
                new BlockData(new BlockPos(0, 0, 0), Blocks.AIR.getDefaultState()),
                "abcdefghijklmnopqrstuvwxyz"
        );
    }

    public boolean addBlock(BlockElement element) {
        return isNodeValid(element, getLatestBlock()) && chainList.add(element);
    }

    public ImmutableList<BlockElement> getChain() {
        return ImmutableList.copyOf(this.chainList);
    }

    public BlockElement getLatestBlock() {
        return chainList.get(chainList.size() - 1);
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

    public boolean isChainValid() {
        for (int i = 1; i < chainList.size(); i++) {
            if (!isNodeValid(chainList.get(i), chainList.get(i - 1))) {
                return false;
            }
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

    public void replaceChain(BlockChain chain) {
        if (chain.isChainValid() && chain.chainList.size() > chainList.size()) {
            this.chainList = chain.chainList;
            // TODO Sync with world.
        } else {
            System.out.println("Received BlockChain is invalid!");
        }
    }

    public void load() {
        System.out.println("Loading the chainzzz");
        File file = new File(worldName + ".chainz");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                chainList = gson.fromJson(reader, new TypeToken<List<BlockElement>>() {}.getType());
                if (chainList == null) {
                    // Heck, something got messed up
                    chainList = Lists.newArrayList(getInitialBlock());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        System.out.println("Saving the chainzzz");
        File file = new File(worldName + ".chainz");
        try(PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.append(gson.toJson(chainList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
