package ftn.robert.sentence_member;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Klasa koja u sebi sadrži samo key-value parove reči i njihovog znaka interpunkcije.
 *
 * Created by Robert on 7.2.2017.
 */

public class StringMap {

    public static final Map<String, String> stringToSpecialCharMap;
    public static final Map<String, String> stringToPunctuationMap;
    static {
        stringToPunctuationMap = new LinkedHashMap<String, String>();
        stringToPunctuationMap.put("tačka", ".");
        stringToPunctuationMap.put("zarez", ",");
        stringToPunctuationMap.put("zapeta", ",");
        stringToPunctuationMap.put("tri tačke", "...");
        stringToPunctuationMap.put("upitnik", "?");
        stringToPunctuationMap.put("znak upitnik", "?");
        stringToPunctuationMap.put("uzvičnik", "!");
        stringToPunctuationMap.put("znak uzvičnik", "!");
        stringToPunctuationMap.put("uskličnik", "!");
        stringToPunctuationMap.put("znak uskličnik", "!");
        stringToPunctuationMap.put("tačka zarez", ";");
        stringToPunctuationMap.put("dve tačke", ":");
        stringToPunctuationMap.put("povlaka", "-");
        stringToPunctuationMap.put("crtica", "-");
        stringToPunctuationMap.put("otvorena zagrada", "(");
        stringToPunctuationMap.put("zatvorena zagrada", ")");

        stringToSpecialCharMap = new LinkedHashMap<String, String>();
        stringToSpecialCharMap.put("nov red",  "\n");
        stringToSpecialCharMap.put("novi red", "\n");
    }
}
