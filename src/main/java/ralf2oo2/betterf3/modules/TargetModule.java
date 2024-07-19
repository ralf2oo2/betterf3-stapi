package ralf2oo2.betterf3.modules;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.event.registry.MobHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.MobHandlerRegistry;
import ralf2oo2.betterf3.utils.*;

import java.util.ArrayList;
import java.util.List;

public class TargetModule extends BaseModule{

    public TargetModule(){
        this.defaultNameColor = 0x00aaff;
        this.defaultValueColor = 0xffff55;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        DebugLine blockEntityLine = new DebugLine("block_entity");
        blockEntityLine.enabled = false;

        lines.add(new DebugLine("targeted_block"));
        lines.add(new DebugLine("id_block"));
        lines.add(new DebugLine("block_meta"));
        lines.add(blockEntityLine);
        lines.add(new DebugLineList("block_states"));
        lines.add(new DebugLineList("block_tags"));
        lines.add(new DebugLine(""));
        lines.add(new DebugLine("targeted_entity"));
    }
    @Override
    public void update(Minecraft minecraft) {
        String targetedBlock = "";
        String idBlock = "";

        double pitch = minecraft.player.pitch;
        double yaw = minecraft.player.yaw;
        double distance = 18.0d;

        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);

        double x = -Math.sin(yawRadians) * Math.cos(pitchRadians);
        double y = -Math.sin(pitchRadians);
        double z = Math.cos(yawRadians) * Math.cos(pitchRadians);

        x *= distance;
        y *= distance;
        z *= distance;

        double targetX = minecraft.player.x + x;
        double targetY = minecraft.player.y + y;
        double targetZ = minecraft.player.z + z;
        Vec3d playerPos = Vec3d.create(minecraft.player.x, minecraft.player.y, minecraft.player.z);

        Vec3d targetPos = Vec3d.create(targetX, targetY, targetZ);

        HitResult hitResult = minecraft.world.method_162(playerPos, targetPos, true, true);
        if(hitResult != null && hitResult.type == HitResultType.BLOCK){
            int id = minecraft.world.getBlockId(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            Block block = Block.BLOCKS[id];

            targetedBlock = hitResult.blockX + ", " + hitResult.blockY + ", " + hitResult.blockZ;
            idBlock = block.getRegistryEntry().getKey().get().getValue().toString();

            lines.get(0).setValue(targetedBlock);
            lines.get(1).setValue(idBlock);
            lines.get(2).setValue(Integer.toString(minecraft.world.getBlockMeta(hitResult.blockX, hitResult.blockY, hitResult.blockZ)));

            BlockEntity entity = minecraft.world.getBlockEntity(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            if (entity != null) {
                String className = entity.getClass().getName();
                lines.get(3).setValue(className.substring(className.lastIndexOf('.') + 1));
            } else{
                lines.get(3).active = false;
            }

            BlockState blockState = minecraft.world.getBlockState(hitResult.blockX, hitResult.blockY, hitResult.blockZ);

            List<Object> blockStates = new ArrayList<>();
            blockState.getEntries().entrySet().forEach((entry -> blockStates.add(Utils.propertyToString(entry, nameColor, valueColor))));
            ((DebugLineList)lines.get(4)).setValues(blockStates);

            List<Object> blockTags = new ArrayList<>();
            blockState.streamTags().forEach((entry -> blockTags.add(new Text(new TextSection("#" + entry.id().namespace + ":", nameColor), new TextSection(entry.id().path.toString(), valueColor)))));
            ((DebugLineList)lines.get(5)).setValues(blockTags);
        }
        else {
            for (int i = 0; i < 7; i++) {
                lines.get(i).active = false;
            }
        }
        if(minecraft.field_2823 != null  && minecraft.field_2823.type == HitResultType.ENTITY){
            Entity entity = minecraft.field_2823.entity;
            String entityId = EntityRegistry.getId(entity);
            lines.get(7).setValue(entityId);
        } else lines.get(7).active = false;
    }
}
