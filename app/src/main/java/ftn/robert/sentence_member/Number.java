package ftn.robert.sentence_member;

/**
 * Predstavlja brojeve u rečenici i nasleđuje SentenceMember klasu.
 *
 * Created by Robert on 5.2.2017.
 */

public class Number extends SentenceMember {

    private int intValue;

    /**
     * Konstruktor sa parametrom.
     *
     * @param value Brojna vrednost tipa int.
     */
    public Number(int value) {
        this.intValue = value;
        this.stringValue = String.valueOf(value);
    }

    @Override
    public Type getSentenceMemberType() {
        return Type.NUMBER;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getValue() {
        return intValue;
    }
}
