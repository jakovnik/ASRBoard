package ftn.robert.sentence_member;

import java.util.ArrayList;

/**
 * Predstavlja niz SentenceMembera, sa odgovarajucim metodama za njihovu
 * manipulaciju.
 *
 * Created by Robert on 5.2.2017.
 */

public class Sentence {

    private ArrayList<SentenceMember> sentence;

    /**
     * Konstruktor bez parametara kojim se alocira 20 mesta za niz SentenceMember-a
     */
    public Sentence() {
        sentence = new ArrayList<>(20);
    }

    /**
     * Metoda za ubacivanje novog SentenceMember-a u postojeci niz SentenceMember-a.
     *
     * @param sentenceMember Novi SentenceMemberkoji se dodaje.
     */
    public void addMember(SentenceMember sentenceMember) {
        sentence.add(sentenceMember);
    }

    /**
     * Metoda za pribavljanje SentenceMember clana sa indeksom index iz niza SentenceMember-a.
     *
     * @param index Indeks SentenceMember clana koji je trazen.
     * @return SentenceMember sa indeksom index iz niza sentence.
     */
    public SentenceMember getSentenceMemberAtIndex(int index) {
        return sentence.get(index);
    }

    /**
     * Metoda za pretvaranje niza SentenceMembera iz promenljive sentence u string prema
     * utvrdjenom pravopisu.
     *
     * @return Odgovarajuci string dobijen iz niza SentenceMembera.
     */
    public String sentenceToString() {
        StringBuilder result = new StringBuilder(1000);
        String current;

        for (int i = 0, n = sentence.size(); i < n; i++) {
            current = sentence.get(i).toString();
            SentenceMember currentMember = sentence.get(i);
            SentenceMember.Type type = currentMember.getSentenceMemberType();
            switch (type) {
                case WORD:
                    result.append(current);
                    break;
                case NUMBER:

                    break;
                case PUNCTUATION:
                        result.append(current);
                    break;
                case SENTENCE_STOP:
                    result.append(current);
                    break;
                case SPECIAL_CHARACTER:
                    result.append(current);
                    break;
                default:

                    break;
            }
        }

        return result.toString();
    }

    /**
     * Metoda za dobijanje broja clanova SentenceMember niza.
     * @return Broj clanova niza SentenceMembera tipa int.
     */
    public int getLength() {
        return sentence.size();
    }

}
