package ftn.robert.sentence_member;

/**
 * Predstavlja reči u rečenici i nasleđuje SentenceMember klasu.
 *
 * Created by Robert on 5.2.2017.
 */

public class Word extends SentenceMember {

    public enum Parameter {CAPITAL, NON_CAPITAL, SPACE_BEFORE, NO_SPACE_BEFORE}

    private boolean capital;
    private boolean spaceBefore;

    /**
     * Konstruktor sa parametorm.
     *
     * @param input Ulazna reč.
     */
    public Word(String input) {
        this(input, Parameter.NON_CAPITAL, Parameter.SPACE_BEFORE);
    }

    /**
     * Konstruktor sa parametrima.
     *
     * @param input Ulazna reč.
     * @param parameters Dodatni parametri reči. Mogući parametri:
     *                   1. Parameter.NON_CAPITAL ukoliko reč nije sa početnim velikim slovom.
     *                   2. Parameter.CAPITAL ukoliko je reč sa početnim velikim slovom.
     *                   3. Parameter.SPACE_BEFORE ukoliko pre reči dolazi razmak.
     *                   5. Parameter.NO_SPACE_BEFORE ukoliko pre reči ne dolazi razmak.
     * @throws NullPointerException Ukoliko je ulaz input null onda se baca exception.
     */
    public Word(String input, Parameter ... parameters) throws  NullPointerException {
        if (input == null) {
            throw new NullPointerException("Invalid word input");
        }

        int i = 0;
        for (Parameter p : parameters) {
            if (i > 1) {
                continue;
            }
            switch (p) {
                case CAPITAL:
                    capital = true;
                    break;
                case NON_CAPITAL:
                    capital = false;
                    break;
                case SPACE_BEFORE:
                    spaceBefore = true;
                    break;
                case NO_SPACE_BEFORE:
                    spaceBefore = false;
                    break;
            }
            i++;
        }
        this.stringValue = input;
    }

    @Override
    public Type getSentenceMemberType() {
        return Type.WORD;
    }

    @Override
    public String toString() {
        String result;
        if (capital) {
            result = Character.toUpperCase(stringValue.charAt(0)) + stringValue.substring(1);
        } else {
            result = stringValue;
        }

        if (spaceBefore) {
            return " " + result;
        } else {
            return result;
        }
    }

    public boolean isCapital() {
        return capital;
    }
}
