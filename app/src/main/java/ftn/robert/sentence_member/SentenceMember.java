package ftn.robert.sentence_member;

/**
 * Abstraktna klasa koja koja definiše potrebna polja i metode svake
 * klase koja nasledi SentenceMember.
 *
 * Created by Robert on 5.2.2017.
 */

public abstract class SentenceMember {
    public enum Type {WORD, NUMBER, PUNCTUATION, SPECIAL_CHARACTER, SENTENCE_STOP}
    protected String stringValue;

    /**
     * Metoda za pribavljanje tipa klase koja nasleđuje SentenceMember klasu.
     *
     * @return Tip klase koji moze biti:
     *         1. WORD
     *         2. NUMBER
     *         3. PUNCTUATION
     *         4. SPECIAL_CHARACTER
     *         5. SENTENCE_STOP
     */
    public abstract Type getSentenceMemberType();

    public abstract String toString();

    /**
     *
     * @param input Ulazni token u vidu stringa za koji je potrebno odrediti kom tipu
     *              SentenceMember-a pripada.
     * @param previousSentenceMember SentenceMember koji prethodi ulaznom stringu input.
     * @return Nova instanca neke klase koja nasledjuje SentenceMember.
     */
    public static SentenceMember tokenToSentenceMember(String input, SentenceMember previousSentenceMember) {

        if (input.split(" ").length > 1) {

            // Ako se radi o tokenu sastavljenom od dve reci

            if (SpecialChar.isStringSpecialChar(input)) {
                return new SpecialChar(input);
            }

            // proverava se da li je znak interpunkcije
            if (Punctuation.isStringPunctuation(input)) {

                switch (previousSentenceMember.getSentenceMemberType()) {
                    case WORD:
                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_STOP) {
                            return new SentenceStop(new Punctuation(input));
                        }

                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_CONTINUE) {
                            return new Punctuation(input);
                        }
                        break;
                    case PUNCTUATION:
                        Punctuation p = new Punctuation(input);

                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_CONTINUE) {
                            if (p.isSpaceAfter()) {
                                return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE);
                            } else {
                                return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.NO_SPACE_BEFORE);
                            }
                        }

                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_STOP) {
                            if (p.isSpaceAfter()) {
                                return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE);
                            } else {
                                return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE);
                            }
                        }
                    case SENTENCE_STOP:
                        return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE);
                }
            } else {
                return null;
            }
        } else {

            // Ako se radi o tokenu koji je sastavljen od samo jedne reci

            switch (previousSentenceMember.getSentenceMemberType()) {
                case WORD:
                    if (Punctuation.isStringPunctuation(input)) {
                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_CONTINUE) {
                            return new Punctuation(input);
                        } else {
                            return new SentenceStop(new Punctuation(input));
                        }
                    } else {
                        return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE);
                    }
                case NUMBER:
                    if (Punctuation.isStringPunctuation(input)) {
                        if (Punctuation.getPunctuationTypeFromString(input) == Punctuation.PunctuationType.PUNCTUATION_CONTINUE) {
                            return new Punctuation(input);
                        } else {
                            return new SentenceStop(new Punctuation(input));
                        }
                    } else {
                        return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE);
                    }
                case PUNCTUATION:
                    Punctuation p = (Punctuation) previousSentenceMember;

                    if (p.getPunctuationType() == Punctuation.PunctuationType.PUNCTUATION_CONTINUE) {
                        if (p.isSpaceAfter()) {
                            return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE);
                        } else {
                            return new Word(input, Word.Parameter.NON_CAPITAL, Word.Parameter.NO_SPACE_BEFORE);
                        }
                    }

                    if (p.getPunctuationType() == Punctuation.PunctuationType.PUNCTUATION_STOP) {
                        if (p.isSpaceAfter()) {
                            return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE);
                        } else {
                            return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE);
                        }
                    }
                    break;
                case SENTENCE_STOP:
                    return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE);

                case SPECIAL_CHARACTER:
                    return new Word(input, Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE);
                default:
                    return null;

            }
        }
        return null;
    }

}










