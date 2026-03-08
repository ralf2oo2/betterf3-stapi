package ralf2oo2.betterf3.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resource.language.TranslationStorage;
import net.modificationstation.stationapi.api.util.Identifier;
import ralf2oo2.betterf3.utils.DebugLine;
import ralf2oo2.betterf3.utils.DebugLineList;
import ralf2oo2.betterf3.utils.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseModule {
    public Integer nameColor;
    public Integer valueColor;

    public Integer defaultNameColor;
    public Integer defaultValueColor;

    public boolean enabled = true;
    public boolean temporary = false;

    protected List<DebugLine> lines = new ArrayList<>();

    public Identifier id;

    public BaseModule(Identifier id){
        this.id = id;
    }

    public List<DebugLine> getLines() {
        return lines;
    }

    public List<Text> getLinesFormatted(boolean reducedDebug) {

        List<Text> linesString = new ArrayList<>();


        for (DebugLine line : lines) {

            if (reducedDebug && !line.inReducedDebug) {
                continue;
            }
            if (!line.active || !line.enabled) {
                continue;
            }

            if (line instanceof DebugLineList) {
                DebugLineList lineList = (DebugLineList) line;
                linesString.addAll(lineList.toTexts(nameColor, valueColor));
                continue;
            }


            if (!line.isCustom) {
                linesString.add(line.toText(nameColor, valueColor, nameColor));
            } else {
                linesString.add(line.toTextCustom(nameColor, nameColor));
            }

        }

        return linesString;
    }
    public DebugLine getLine(String id) {

        Optional<DebugLine> lineOptional = lines.stream().filter(line -> line.getId().equals(id)).findFirst();

        return lineOptional.orElse(null);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void update(Minecraft minecraft);
}
