package ftn.robert.sentence_member;

/**
 * Predstavlja specijalne karaktere u rečenici (npr. karakter za nov red) i nasleđuje klasu SentenceMember.
 *
 * Created by Robert on 7.2.2017.
 */

public class SpecialChar extends SentenceMember {

    /**
     * Konstruktor sa parametrom.
     *
     * @param input Ulazni specijalni karakter u vidu reči.
     */
    public SpecialChar(String input) {
        this.stringValue = toSpecialChar(input);
    }

    @Override
    public Type getSentenceMemberType() {
        return Type.SPECIAL_CHARACTER;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Metoda kojom se proverava da li je ulazni string u vidu reči specijalan karakter.
     *
     * @param input Ulazna reč za koju se proverava da li je specijalni karakter.
     * @return Boolean vrednost. Ukoliko se input nalazi u StringMap.stringToSpecialCharMap
     *         mapi onda je true. Ukoliko se ne nalazi onda je false.
     */
    public static boolean isStringSpecialChar(String input) {
        if (StringMap.stringToSpecialCharMap.containsKey(input)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Metoda za konvertovanje ulaznog stringa input u odgovarajući specijalni karakter.
     *
     * @param input Ulazni specijalni karakter u vidu reči.
     * @return Vraća se specijalni karakter ukoliko se input nalazi u
     *         StringMap.stringToSpecialCharMap mapi.
     *         Vraća null ukoliko se ne nalazi.
     */
    public String toSpecialChar(String input) {
        if (isStringSpecialChar(input) && StringMap.stringToSpecialCharMap.containsKey(input)) {
            return StringMap.stringToSpecialCharMap.get(input);
        } else {
            return null;
        }
    }
}
