package ftn.robert.sentence_member;

/**
 * Predstavlja znak interpunkcije kojim se završava rečenica i nasleđuje SentenceMember klasu.
 *
 * Created by Robert on 6.2.2017.
 */

public class SentenceStop extends SentenceMember {

    private Punctuation punctuation;

    /**
     * Konstruktor sa parametrom.
     *
     * @param punctuation Ulazni parametar je instanca klase Punctuation.
     */
    public SentenceStop(Punctuation punctuation) {
        this.punctuation = punctuation;
    }

    @Override
    public String toString() {
        return punctuation.toString();
    }

    @Override
    public Type getSentenceMemberType() {
        return Type.SENTENCE_STOP;
    }
}
