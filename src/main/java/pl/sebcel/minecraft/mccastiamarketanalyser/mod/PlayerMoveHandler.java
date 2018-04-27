package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISignFoundListener;

public class PlayerMoveHandler {

    private final static int HORIZONTAL_SIGN_READING_RANGE_IN_BLOCKS = 15;
    private final static int VERTICAL_SIGN_READING_RANGE_IN_BLOCKS = 5;

    private Set<ISignFoundListener> signFoundListeners = new HashSet<>();

    public void addSignFoundListener(ISignFoundListener signFoundListener) {
        this.signFoundListeners.add(signFoundListener);
    }

    @SubscribeEvent
    public void handlePlayerEnteringChunkEvent(EnteringChunk event) {
        if (event.getEntity() instanceof EntityPlayer) {
            System.out.println("Player is entering chunk (" + event.getNewChunkX() + "," + event.getNewChunkZ() + ") from chunk (" + event.getOldChunkX() + "," + event.getOldChunkZ() + ") " + event.getPhase());
            readSigns(event.getEntity());
        }
    }

    private Vec3d previousPlayerPosition = null;

    @SubscribeEvent
    public void playerMoveEvent(LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            Vec3d currentPlayerPosition = ((EntityPlayer) event.getEntity()).getPositionVector();
            if (playerMoved(previousPlayerPosition, currentPlayerPosition)) {
                previousPlayerPosition = currentPlayerPosition;
                readSigns(event.getEntity());
            }
        }
    }

    private void readSigns(Entity player) {
        BlockPos playerPosition = player.getPosition();
        for (int x = playerPosition.getX() - HORIZONTAL_SIGN_READING_RANGE_IN_BLOCKS; x <= playerPosition.getX() + HORIZONTAL_SIGN_READING_RANGE_IN_BLOCKS; x++) {
            for (int y = playerPosition.getY(); y <= playerPosition.getY() + VERTICAL_SIGN_READING_RANGE_IN_BLOCKS; y++) {
                for (int z = playerPosition.getZ() - HORIZONTAL_SIGN_READING_RANGE_IN_BLOCKS; z <= playerPosition.getZ() + HORIZONTAL_SIGN_READING_RANGE_IN_BLOCKS; z++) {
                    TileEntity tileEntity = player.getEntityWorld().getTileEntity(new BlockPos(x, y, z));
                    if (tileEntity instanceof TileEntitySign) {
                        for (ISignFoundListener listener : signFoundListeners) {
                            listener.onSignFound((TileEntitySign) tileEntity);
                        }
                    }
                }
            }
        }
    }

    // Player position is a real number internally, but integer in the API. As a result player's position
    // returned by API changes even if player stays still. For example, when x = 13.5, API returns x = 13,
    // then x = 14, then x = 13, etc.
    private boolean playerMoved(Vec3d previousPosition, Vec3d currentPosition) {
        if (previousPosition == null) {
            return true;
        }

        boolean xChanged = (int) previousPosition.x != (int) currentPosition.x;
        boolean yChanged = (int) previousPosition.y != (int) currentPosition.y;
        boolean zChanged = (int) previousPosition.z != (int) currentPosition.z;

        return xChanged || yChanged || zChanged;
    }
}