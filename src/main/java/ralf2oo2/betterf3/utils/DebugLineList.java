package ralf2oo2.betterf3.utils;

import java.util.ArrayList;
import java.util.List;

public class DebugLineList extends DebugLine{
    private List<Object> values = new ArrayList<>();
    public DebugLineList(String id) {
        super(id);
    }

    public void setValues(final List<Object> values){
        this.values = values;
        this.active = true;
    }

    public List<Text> toTexts(final Integer nameColor, final Integer valueColor){
        final List<Text> texts = new ArrayList<>();

        if(values.stream().count() > 0){
            if(values.get(0) instanceof Text){
                for(final Object v : this.values){
                    texts.add((Text)v);
                }
            }
            else{
                for(final Object v : this.values){
                    texts.add(Utils.formattedFromString((String)v, nameColor, valueColor));
                }
            }
        }
        return texts;
    }
}
