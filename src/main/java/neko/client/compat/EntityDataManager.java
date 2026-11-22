package neko.client.compat;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Uses vanilla block-entity queries when the server grants query permission. */
public final class EntityDataManager {
    private static final EntityDataManager INSTANCE = new EntityDataManager();
    private final Map<BlockPos, CompoundTag> cache = new HashMap<>();
    private final Map<BlockPos, Long> requested = new HashMap<>();
    private Level world;
    public static EntityDataManager getInstance() { return INSTANCE; }
    public EntityDataManager getCache() { return this; }
    public CompoundTag getBlockEntityNbtFromCache(BlockPos pos) {
        checkWorld(Minecraft.getInstance().level);
        return cache.get(pos);
    }
    private void checkWorld(Level current) {
        if (world != current) { world = current; cache.clear(); requested.clear(); }
    }
    public BlockEntity getFromBlockEntityCache(BlockPos pos) {
        CompoundTag tag = getBlockEntityNbtFromCache(pos);
        return tag == null || world == null ? null : BlockEntity.loadStatic(pos, world.getBlockState(pos), tag);
    }
    public void requestBlockEntityWrapped(Level current, BlockPos pos) {
        checkWorld(current);
        Minecraft client = Minecraft.getInstance();
        if (current == null || client.player == null || !client.player.hasPermissions(2) || client.getConnection() == null) return;
        long now = System.currentTimeMillis();
        if (now - requested.getOrDefault(pos, 0L) < 1000L) return;
        BlockPos key = pos.immutable();
        requested.put(key, now);
        client.getConnection().getDebugQueryHandler().queryBlockEntityTag(key, tag -> {
            if (world == current && tag != null) cache.put(key, tag.copy());
        });
    }
    // These bulk query protocols are not provided by Litematica 0.15.4.
    public boolean hasServuxServer() { return false; }
    public boolean hasBackupStatus() { return false; }
    public boolean getIfReceivedBackupPackets() { return false; }
    public boolean hasCompletedChunk(ChunkPos pos) { return false; }
    public void requestServuxBulkEntityData(ChunkPos pos, int minY, int maxY) { }
    public void requestBackupBulkEntityData(ChunkPos pos, int minY, int maxY) { }
}
