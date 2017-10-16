package ftn.robert.sentence_member;

import java.util.ArrayList;

/**
 * Klasa za pravilno određivanje i sortiranje elemenata niza stringova u
 * odgovarajuće SentenceMember članove.
 *
 * Created by Robert on 5.2.2017.
 */

public class ComposingText {

    private static final char SPACE = ' ';
    private static final char NEW_LINE = '\n';
    private Sentence sentence;

    /**
     * Glavna metoda za unos teksta. Na osnovu unetog stringa input sortira reči u odgovarajuće
     * SentenceMember-e po urvrđenom pravilu
     *
     * @param input Ulazni tekst u vidu stringa.
     * @param previous Do sada unesen tekst u vidu stringa u TextEdit polju.
     */
    public void setInput(String input, String previous) {
        char lastChar;
        boolean isCapital;
        boolean spaceBefore;
        ArrayList<SentenceMember> sentenceMembers;

        /** Odredjivanje da li je nastavak recenice ili pocetak recenice,
         * kao i da li pre prvog karaktera ide razmak ili ne */
        isCapital = false;  // cisto rezerve radi da se ipak setuje
        spaceBefore = true; // cisto rezerve radi da se ipak setuje

        if (previous == null) {
            isCapital = true;
            spaceBefore = false;
        } else if (previous.isEmpty()) {
            isCapital = true;
            spaceBefore = false;
        } else {
            lastChar = previous.charAt(previous.length() - 1);

            if (lastChar == NEW_LINE) {
                spaceBefore = false;
                isCapital = true;
            } else if (lastChar == SPACE) {
                spaceBefore = false;
                for (int i = previous.length() - 2; i >= 0; i++) {
                    if (previous.charAt(i) == SPACE) {
                        continue;
                    }
                    if (previous.charAt(i) == NEW_LINE) {
                        isCapital = true;
                        break;
                    }
                    if (Character.isLetter(previous.charAt(i)) || Character.isDigit(previous.charAt(i))) {
                        isCapital = false;
                        break;
                    } else {
                        Punctuation.PunctuationType type = Punctuation.getPunctuationTypeFromPunctuation(String.valueOf(previous.charAt(i)));
                        switch (type) {
                            case PUNCTUATION_STOP:
                                isCapital = true;
                                break;
                            case PUNCTUATION_CONTINUE:
                            default:
                                isCapital = false;
                        }
                        break;
                    }
                }
            } else if (Punctuation.punctionList.contains(String.valueOf(lastChar))){
                spaceBefore = true;
                Punctuation.PunctuationType type = Punctuation.getPunctuationTypeFromPunctuation(String.valueOf(lastChar));
                switch (type) {
                    case PUNCTUATION_STOP:
                        isCapital = true;
                        break;
                    case PUNCTUATION_CONTINUE:
                    default:
                        isCapital = false;
                }
            } else {
                spaceBefore = true;
                isCapital = false;
            }
        }

        /** Obrada unesenog teksta, tj. stvaranje niza SentenceMember-a */
        String[] tokens = input.split(" ");
        StringBuilder tokenBuilder = new StringBuilder(100);
        sentenceMembers = new ArrayList<>(50); // pretpostavka da izgovoreni tekst nece imati vise od 50 SentenceMember-a
        SentenceMember sentenceMember;

        for (int i = 0, j = 0, n = tokens.length; i < n; i++, j++) {
            if (i < n - 1) {
                if (tokenBuilder.length() > 1) {
                    tokenBuilder.delete(0, tokenBuilder.length());
                }

                tokenBuilder.append(tokens[i]);
                tokenBuilder.append(" ");
                tokenBuilder.append(tokens[i + 1]);

                if (i == 0) {
                    // posto prvi token nema SentenceMember pre sebe, samo se on proverava da li je
                    // punctuation ili SpecialChar

                    if (SpecialChar.isStringSpecialChar(tokenBuilder.toString())) {
                        sentenceMembers.add(new SpecialChar(tokenBuilder.toString()));
                        i++;
                    } else if (Punctuation.isStringPunctuation(tokenBuilder.toString())) {
                        if (isCapital) {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        } else {
                            Punctuation.PunctuationType type = Punctuation.getPunctuationTypeFromString(tokenBuilder.toString());

                            switch (type) {
                                case PUNCTUATION_STOP:
                                    sentenceMembers.add(new SentenceStop(new Punctuation(tokenBuilder.toString())));
                                    break;
                                case PUNCTUATION_CONTINUE:
                                case PUNCTUATION_UNKNOWN:
                                    sentenceMembers.add(new Punctuation(tokenBuilder.toString()));
                                    break;
                            }
                            i++;
                        }
                    } else if (Punctuation.isStringPunctuation(tokens[i])){
                        if (isCapital) {
                            sentenceMembers.add(new Word(tokens[i]));
                        } else {
                            Punctuation.PunctuationType type = Punctuation.getPunctuationTypeFromString(tokens[i]);

                            switch (type) {
                                case PUNCTUATION_STOP:
                                    sentenceMembers.add(new SentenceStop(new Punctuation(tokens[i])));
                                    break;
                                case PUNCTUATION_CONTINUE:
                                case PUNCTUATION_UNKNOWN:
                                    sentenceMembers.add(new Punctuation(tokens[i]));
                                    break;
                            }
                        }
                    } else {
                        if (isCapital) {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        } else {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.NON_CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        }
                    }
                } else {
                    sentenceMember = SentenceMember.tokenToSentenceMember(tokenBuilder.toString(), sentenceMembers.get(j - 1));
                    if (sentenceMember == null) {
                        sentenceMember = SentenceMember.tokenToSentenceMember(tokens[i], sentenceMembers.get(j - 1));
                        if (sentenceMember != null) {
                            sentenceMembers.add(sentenceMember);
                        }
                    } else {
                        sentenceMembers.add(sentenceMember);
                        i++;
                    }
                }
            } else {
                if (i == 0) {
                    // posto prvi token nema SentenceMember pre sebe, samo se on proverava da li je
                    // punctuation
                    if (Punctuation.isStringPunctuation(tokens[i])) {
                        if (isCapital) {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        } else {
                            Punctuation.PunctuationType type = Punctuation.getPunctuationTypeFromString(tokens[i]);

                            switch (type) {
                                case PUNCTUATION_STOP:
                                    sentenceMembers.add(new SentenceStop(new Punctuation(tokens[i])));
                                    break;
                                case PUNCTUATION_CONTINUE:
                                case PUNCTUATION_UNKNOWN:
                                    sentenceMembers.add(new Punctuation(tokens[i]));
                                    break;
                            }
                        }
                    } else {
                        if (isCapital) {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        } else {
                            if (spaceBefore) {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.NON_CAPITAL, Word.Parameter.SPACE_BEFORE));
                            } else {
                                sentenceMembers.add(new Word(tokens[i], Word.Parameter.NON_CAPITAL, Word.Parameter.NO_SPACE_BEFORE));
                            }
                        }
                    }
                } else {
                    sentenceMember = SentenceMember.tokenToSentenceMember(tokens[i], sentenceMembers.get(j - 1));
                    if (sentenceMember != null) {
                        sentenceMembers.add(sentenceMember);
                    }
                }
            }
        }

        sentence = new Sentence();
        for (int i = 0, n = sentenceMembers.size(); i < n; i++) {
            sentence.addMember(sentenceMembers.get(i));
        }
    }

    public void setInput(String input) {
        setInput(input, null);
    }

    /**
     * Glavna metoda za vraćanje teksta iz niza rečenice tipa Sentence, koja je sačinjena od
     * niza SentenceMember-a.
     *
     * @return Pravilno napisana rečenica.
     */
    public String returnText() {
        return sentence.sentenceToString();
    }

    /**
     * Ukoliko se traži da tekst bude sa velikim početnim slovom.
     * Ukoliko je početak teksta razmak, traži se prvi karakter koji mora biti iz alfabeta
     * koji se onda kapitalizuje. Ukoliko nije iz alfabeta, npr. broj ili znak interpunkcije,
     * karakter se preskače i izlazi se iz metode.
     *
     * @return Tekst u vidu stringa.
     */
    public String returnCapitalizedText() {
        String result = returnText();
        StringBuilder stringBuilder = new StringBuilder(result.length());

        int index = 0;
        for (int n = result.length(); index < n - 1; index++) {
            if (result.charAt(index) == ' ') {
                stringBuilder.append(" ");
                continue;
            }
            break;
        }

        if (Character.isLetter(result.charAt(index))) {
            stringBuilder.append(Character.toUpperCase(result.charAt(index)));
            stringBuilder.append(result.substring(index + 1));
            return stringBuilder.toString();
        } else {
            return result;
        }
    }

    /**
     * Metoda koja sve alfabetske karaktere "diže" iliti kapitalizuje.
     *
     * @return Tekst u vidu stringa sa svim velikim slovima.
     */
    public String returnCapsLockText() {
        String result = returnText();
        StringBuilder stringBuilder = new StringBuilder(result.length());

        for (char c : result.toCharArray()) {
            if (Character.isLetter(c)) {
                c = Character.toUpperCase(c);
            }

            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

}
