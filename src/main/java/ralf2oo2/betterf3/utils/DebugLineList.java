package ralf2oo2.betterf3.utils;

import java.util.ArrayList;
import java.util.List;

public class DebugLineList extends DebugLine{
    private List<String> values = new ArrayList<>();
    public DebugLineList(String id) {
        super(id);
    }

    public void values(final List<String> values){
        this.values = values;
        this.active = true;
    }

    public List<Text> toTexts(final int nameColor, final int valueColor){
        final List<Text> texts = new ArrayList<>();

        for(final String v : this.values){
            texts.add(Utils.formattedFromString(v, nameColor, valueColor));
        }
        return texts;
    }
}
