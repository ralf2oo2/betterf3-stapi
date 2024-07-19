package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.mixin.flattening.ChunkAccessor;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.Utils;

import java.util.Optional;

public class LocationModule extends BaseModule{

    public static String[] directions = {
        "south", "west", "north", "east"
    };

    public static String[] coord_directions = {
            "positive_z", "negative_x", "negative_z", "positive_x"
    };

    public LocationModule(){
        this.defaultNameColor = 0x00AA00;
        this.defaultValueColor = 0x55FFFF;

        this.nameColor = defaultNameColor;
        this.valueColor = defaultValueColor;

        lines.add(new DebugLine("dimension"));
        lines.add(new DebugLine("facing"));
        lines.add(new DebugLine("rotation"));
        lines.add(new DebugLine("light"));
        //lines.add(new DebugLine("light_server"));
        lines.add(new DebugLine("highest_block"));
        //lines.add(new DebugLine("highest_block_server"));
        lines.add(new DebugLine("biome"));
        lines.add(new DebugLine("local_difficulty"));
        lines.add(new DebugLine("days_played"));
    }
    @Override
    public void update(Minecraft minecraft) {
        String biome = minecraft.world.method_1781().method_1787(
            (int) Math.floor(minecraft.player.x),
            (int) Math.floor(minecraft.player.y)
        ).name;

        int lookDir = (MathHelper.floor(
        (double) (minecraft.player.yaw * 4.0F / 360.0F) + 0.5) & 3
        );

        int topY = minecraft.world.getTopY(
            (int) Math.floor(minecraft.player.x),
            (int) Math.floor(minecraft.player.z)
        );

        String dimensionId;
        Optional dimensionContainer = DimensionRegistry.INSTANCE.getByLegacyId(minecraft.player.dimensionId);
        if(dimensionContainer.isEmpty()){
            dimensionId = "Invalid";
        } else {
            dimensionId = DimensionRegistry.INSTANCE.getId((DimensionContainer<?>) dimensionContainer.get()).toString();
        }

        lines.get(0).setValue(dimensionId);
        lines.get(1).setValue(String.format("%s (%s)",
            TranslationStorage.getInstance().get("text.betterf3.line." + directions[lookDir]),
            TranslationStorage.getInstance().get("text.betterf3.line." + coord_directions[lookDir])
        ));

        String yaw = String.format("%.1f", Utils.wrapDegrees(minecraft.player.yaw));
        String pitch = String.format("%.1f", Utils.wrapDegrees(minecraft.player.pitch));
        lines.get(2).setValue(String.format(TranslationStorage.getInstance().get("format.betterf3.rotation"), yaw, pitch));

        Chunk chunk = minecraft.world.method_199(
            (int) Math.floor(minecraft.player.x),
            (int) Math.floor(minecraft.player.z)
        );

        int blockLight = chunk.getLight(
            LightType.BLOCK,
            ((int) Math.floor(minecraft.player.x)) & 15,
            (int) Math.floor(minecraft.player.y),
            ((int) Math.floor(minecraft.player.z)) & 15
        );

        int skyLight = chunk.getLight(
            LightType.SKY,
            ((int) Math.floor(minecraft.player.x)) & 15,
            (int) Math.floor(minecraft.player.y),
            ((int) Math.floor(minecraft.player.z)) & 15
        );

        lines.get(3).setValue(String.format(
                TranslationStorage.getInstance().get("format.betterf3.chunklight"),
                Math.max(skyLight, blockLight), skyLight, blockLight
        ));

        lines.get(4).setValue(String.valueOf(topY - 1));

        lines.get(5).setValue(biome);

        String difficulty = Utils.getDifficultyString(minecraft.world.field_213);
        lines.get(6).setValue(difficulty);

        lines.get(7).setValue(String.valueOf((int)Math.floor(minecraft.world.getTime() / 24000)));
    }
}
