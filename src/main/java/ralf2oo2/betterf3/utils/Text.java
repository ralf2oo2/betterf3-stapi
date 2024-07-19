package ralf2oo2.betterf3.utils;

import joptsimple.internal.Strings;
import net.minecraft.client.resource.language.TranslationStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Text {
    private TextSection[] textSections;

    public Text(TextSection... textSections) {
        this.textSections = textSections;
    }

    public Text(String... strings){
        if(strings == null){
            textSections = new TextSection[]{new TextSection("", 0xFFFFFF)};
            return;
        }
        TextSection[] sections = new TextSection[strings.length];
        for(int i = 0; i < strings.length; i++){
            sections[i] = new TextSection(strings[i], 0xFFFFFF);
        }
    }

    @Override
    public String toString() {
        if(textSections == null) return "";
        String string = "";
        for(int i = 0; i < textSections.length; i++){
            if(!Strings.isNullOrEmpty(textSections[i].text)){
                string += textSections[i].text;
            }
        }
        return string;
    }

    public int getSectionCount(){
        if(textSections == null) return 0;
        return textSections.length;
    }

    public TextSection getSection(int sectionIndex){
        return textSections[sectionIndex];
    }

    public TextSection[] getSections(){
        return textSections;
    }

    public static Text fromColorCodedString(String string){
        String[] sections = string.split("§");
        String[] validSections = Arrays.stream(sections).filter((x) -> x.length() > 1).toArray(String[]::new);
        TextSection[] textSections = new TextSection[validSections.length];
        for(int i = 0; i < textSections.length; i++){
            String colorCode = validSections[i].substring(0,1);
            String text = validSections[i].substring(1,validSections[i].length());
            textSections[i] = new TextSection(text, Utils.colors.get(colorCode));
        }
        return new Text(textSections);
    }

    public Text append(TextSection... textSections){
        if(textSections == null) return this;
        return new Text(
                Stream.concat(Arrays.stream(this.textSections), Arrays.stream(textSections)).toArray(TextSection[]::new)
        );
    }

    public static Text format(String format, Integer formatColor, Text... texts){
        List<TextSection> sections = new ArrayList<>();
        String formatString = TranslationStorage.getInstance().get(format);

        int nextTextIndex = 0;
        String currentFormatPart = "";
        boolean foundTokenStart = false;
        for (int i = 0; i < formatString.length(); i++) {
            char c = formatString.charAt(i);
            if(c == '%'){
                foundTokenStart = true;
                continue;
            }
            if(c == 's' && foundTokenStart){
                if(currentFormatPart != ""){
                    sections.add(new TextSection(currentFormatPart, formatColor));
                }
                if(texts != null && nextTextIndex < texts.length){
                    if(texts[nextTextIndex].getSections() != null){
                        sections.addAll(Arrays.asList(texts[nextTextIndex].getSections()));
                    }
                    nextTextIndex++;
                }
                currentFormatPart = "";
                continue;
            } else if(foundTokenStart){
                foundTokenStart = false;
            }
            currentFormatPart += c;
        }
        if(currentFormatPart != ""){
            sections.add(new TextSection(currentFormatPart, formatColor));
        }
        return new Text(sections.toArray(TextSection[]::new));
    }
}
