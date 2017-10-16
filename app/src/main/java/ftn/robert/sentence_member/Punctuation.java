package ftn.robert.sentence_member;

/**
 * Predstavlja znake interpunkcije u rečenici i nasleđuje klasu SentenceMember klasu.
 *
 * Created by Robert on 5.2.2017.
 */

public class Punctuation extends SentenceMember {

    public enum PunctuationType {PUNCTUATION_STOP, PUNCTUATION_CONTINUE, PUNCTUATION_UNKNOWN}

    public static final String punctionList      = "...,!?();:";
    public static final String punctionsContinue = ",;:()"; // posle ovih karaktera recenica se nastavlja
    public static final String punctionsStop     = "...!?"; // posle ovih karaktera ide veliko slovo
    public static final String punctuationNoSpaceAfter  = "(";     // posle ovih karaktera ne ide razmak
    public static final String punctuationSpaceAfter    = "...,!?;:)"; // posle ovih karaktera ide razmak
    public static final String punctuationNoSpaceBefore = "...,!?;:)"; // pre ovih karaktera ne ide razmak
    public static final String punctuationSpaceBefore   = "(";         // pre ovih karaktera ide razmak

    private PunctuationType punctuationType;
    private boolean spaceAfter;
    private boolean spaceBefore;

    /**
     * Konstruktor sa parametrom.
     *
     * @param input Ulazni znak interpunkcije u vidu reči.
     * @throws NullPointerException Ukoliko je ulazni string jednak null, baca se exception.
     */
    public Punctuation(String input) throws NullPointerException {

        if (input == null) {
            throw new NullPointerException("Invalid punctuation input");
        }

        this.stringValue = Punctuation.toPunctuation(input);

        if (stringValue == null) {
            throw new NullPointerException("Invalid punctuation input");
        }

        if (punctionsStop.contains(stringValue)) {
            punctuationType = PunctuationType.PUNCTUATION_STOP;
        } else if (punctionsContinue.contains(stringValue)) {
            punctuationType = PunctuationType.PUNCTUATION_CONTINUE;
        } else {
            punctuationType = PunctuationType.PUNCTUATION_UNKNOWN;
        }

        spaceBefore = (punctuationSpaceBefore.contains(stringValue));
        spaceAfter  = (punctuationSpaceAfter.contains(stringValue));
    }


    @Override
    public String toString() {
        if (spaceBefore) {
            return " " + stringValue;
        } else {
            return stringValue;
        }
    }

    @Override
    public Type getSentenceMemberType() {
        return Type.PUNCTUATION;
    }

    /**
     * Metoda za vraćanje tipa interpunkcije koji joj je dodeljen u konstruktoru.
     *
     * @return Tip interpunkcije. Može biti:
     *         1. PUNCTUATION_STOP
     *         2. PUNCTUATION_CONTINUE
     *         3. PUNCTUATION_UNKNOWN
     */
    public PunctuationType getPunctuationType() {
        return punctuationType;
    }

    /**
     * Meoda za vraćanje spaceAfter polja.
     *
     * @return Ukoliko znak interpunkcije pripada punctuationSpaceAfter listi onda je true.
     *         Ukoliko znak interpunkcije pripada punctuationNoSpaceAfter listi onda je false.
     */
    public boolean isSpaceAfter() {
        return spaceAfter;
    }

    /**
     * Meoda za vraćanje spaceBefore polja.
     *
     * @return Ukoliko znak interpunkcije pripada punctuationSpaceBefore listi onda je true.
     *         Ukoliko znak interpunkcije pripada punctuationNoSpaceBefore listi onda je false.
     */
    public boolean isSpaceBefore() {
        return spaceBefore;
    }

    /**
     * Metoda za vraćanje tipa znaka interpunkcije iz ulaznog stringa (znak interpunkcije je u vidu teksta)
     *
     * @param input Ulazni string za koji se određuje tip znaka interpunkcije.
     * @return Tip interpunkcije. Može biti:
     *         1. PUNCTUATION_STOP
     *         2. PUNCTUATION_CONTINUE
     *         3. PUNCTUATION_UNKNOWN
     */
    public static PunctuationType getPunctuationTypeFromString(String input) {
        String punctuation = toPunctuation(input);
        if (punctuation == null) {
            return PunctuationType.PUNCTUATION_UNKNOWN;
        }

        if (punctionsStop.contains(punctuation)) {
            return PunctuationType.PUNCTUATION_STOP;
        } else if (punctionsContinue.contains(punctuation)) {
            return PunctuationType.PUNCTUATION_CONTINUE;
        } else {
            return PunctuationType.PUNCTUATION_UNKNOWN;
        }
    }

    /**
     * Metoda za vraćanje tipa znaka interpunkcije iz ulaznog stringa (znak interpunkcije znaka)
     *
     * @param punctuation Ulazni string za koji se određuje tip znaka interpunkcije.
     * @return Tip interpunkcije. Može biti:
     *         1. PUNCTUATION_STOP
     *         2. PUNCTUATION_CONTINUE
     *         3. PUNCTUATION_UNKNOWN
     */
    public static PunctuationType getPunctuationTypeFromPunctuation(String punctuation) {
        if (punctuation == null) {
            return PunctuationType.PUNCTUATION_UNKNOWN;
        }

        if (punctionsStop.contains(punctuation)) {
            return PunctuationType.PUNCTUATION_STOP;
        } else if (punctionsContinue.contains(punctuation)) {
            return PunctuationType.PUNCTUATION_CONTINUE;
        } else {
            return PunctuationType.PUNCTUATION_UNKNOWN;
        }
    }

    /**
     * Metoda za proveru da li se string input u vidu reči nalazi u StringMap.stringToPunctuationMap
     * mapi.
     * @param input Ulazni string u vidu reči.
     * @return Boolean vrednost. True ukoliko se reč nalazi u mapi. False ukoliko se reč ne nalazi
     *         u mapi.
     */
    public static boolean isStringPunctuation(String input) {
        return (StringMap.stringToPunctuationMap.containsKey(input));
    }

    /**
     * Metoda za konvertovanje ulaznog stringa input u odgovarajući znak interpunkcije.
     *
     * @param input Ulazni znak interpunkcije u vidu reči.
     * @return Vraća se znak interpunkcije u vidu znaka ukoliko se input nalazi u
     *         StringMap.stringToPunctuationMap mapi.
     *         Vraća null ukoliko se ne nalazi.
     */
    public static String toPunctuation(String input) {
        if (isStringPunctuation(input) && StringMap.stringToPunctuationMap.containsKey(input)) {
            return StringMap.stringToPunctuationMap.get(input);
        } else {
            return null;
        }
    }


}
