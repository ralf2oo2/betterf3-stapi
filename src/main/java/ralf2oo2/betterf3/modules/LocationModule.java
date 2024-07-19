package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.mixin.flattening.ChunkAccessor;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Utils;

public class LocationModule extends BaseModule{

    public LocationModule(){
        this.defaultNameColor = 0x00AA00;
        this.defaultValueColor = 0x55FFFF;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("dimension"));
        lines.add(new DebugLine("facing"));
        lines.add(new DebugLine("rotation"));
        lines.add(new DebugLine("light"));
        lines.add(new DebugLine("light_server"));
        lines.add(new DebugLine("highest_block"));
        lines.add(new DebugLine("highest_block_server"));
        lines.add(new DebugLine("biome"));
        lines.add(new DebugLine("local_difficulty"));
        lines.add(new DebugLine("days_played"));
    }
    @Override
    public void update(Minecraft minecraft) {
        String chunkLightString = "";
        String chunkLightServerString = "";
        String localDifficultyString = "";
        String highestBlock = "";
        String highestBlockServer = "";
        String biome = minecraft.world.method_1781().method_1787((int)minecraft.player.x, (int)minecraft.player.y).name;
        lines.get(7).setValue(biome);
        String difficulty = Utils.getDifficultyString(minecraft.world.field_213);
        lines.get(8).setValue(difficulty);
        minecraft.world.getTime();
    }
}
