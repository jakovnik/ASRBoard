package ftn.robert.asrboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import java.util.List;

/**
 * Nasleđuje Keyboard klasu radi namenskih izmena. Jedini razlog za uvođenje nove klase jeste
 * overrajdovanje getNearestKeys metode.
 *
 * Created by Robert on 3.2.2017.
 */

public class SoftKeyboard extends Keyboard {
    public SoftKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    /**
     * Potrebno je bilo overrajdovati metodu kako bi široka dugmad (npr. spacebar) imala čitavu
     * površinu aktivnu jer bez ovoga ivice širokog dugmeta ne mogu biti kliknute.
     */
    @Override
    public int[] getNearestKeys(int x, int y) {
        List<Key> keys = getKeys();
        Key[] mKeys = keys.toArray(new Key[keys.size()]);
        int i = 0;
        for (Key key : mKeys) {
            if(key.isInside(x, y))
                return new int[]{i};
            i++;
        }
        return new int[0];
    }
}
