package ftn.robert.asrboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import java.util.List;

/**
 * Nasleđuje KeyboardView klasu radi namenskih izmena. U svom view-u sadrzi instacnu SoftKeyboard
 * klase koja nasleđuje Keyboard klasu.
 *
 * Created by Robert on 3.2.2017.
 */

public class SoftKeyboardView extends KeyboardView {

    /** Paint za pozadinu tastature. */
    private Paint keyboardBackground;

    /** Paint za dugmice na tastaturi. */
    private Paint keyColor;

    /** Paint za specijalne dugmiće na tastaturi. */
    private Paint specialKeyColor;

    /** Slika za backspace dugme. */
    private Bitmap backspaceBitmap;

    /** Slika za shift dugme kada nije aktivan. */
    private Bitmap noShiftBitmap;

    /** Slika za shift dugme kada je aktivan. */
    private Bitmap shiftBitmap;

    /** Slika za shift dugme kada je u CapsLock modu. */
    private Bitmap capsLockBitmap;

    /** Slika za enter dugme. */
    private Bitmap enterBitmap;

    /** Int polje u kome je vrednost za koliko treba smanjiti okolne ivice svakog dugmića na
     * tastaturi. */
    private int keyShrink;

    /** Int polje u kojem je vrednost visine dugmića na tastaturi. */
    private int keyHeight;

    /** TextPaint polje za boju tekstualnih natpisa na dugmićima na tastaturi. */
    private TextPaint textPaint;

    /** Int polje u kojem je vrednost API nivoa uređaja. Bitan je zbog iscrtavanja oblika dugmića.
     * Za API nivo 20 ili iznad dugmići su zaobljeni, a za nivo ispod 20, dugmići su pravougaonici
     * oštrih ivica. */
    private int apiLevel;

    /** Kada dugme shif nije aktivno (vrednost 0). */
    public static final int NO_SHIFT = 0;

    /** Kada je dugme shift aktivno (vrednost 1). */
    public static final int SHIFT    = 1;

    /** Kada je dugme shift u CapsLock modu (vrednost 2). */
    public static final int CAPSLOCK = 2;

    /** Int polje u kom se čuva trenutno stanje shift dugmeta. */
    private int shiftState;


    /**
     * Konstruktor.
     */
    public SoftKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        keyboardBackground = new Paint();
        keyboardBackground.setColor(getResources().getColor(R.color.colorBluish));

        /** Bez ANTI_ALIAS_FLAG natpisi na tipkama budu losi */
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.textColor));
//        textPaint.setColor(Color.BLACK);

        keyColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        keyColor.setColor(Color.WHITE);

        specialKeyColor = new Paint(Paint.ANTI_ALIAS_FLAG);
        specialKeyColor.setColor(getResources().getColor(R.color.colorGreyDarker));

        apiLevel = android.os.Build.VERSION.SDK_INT;
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        super.setKeyboard(keyboard);

        keyHeight = getKeyboard().getKeys().get(0).height;
        keyShrink = (int)(keyHeight * 0.05);

        loadKeyIcons();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPaint(keyboardBackground);

        List<Key> keys = getKeyboard().getKeys();
        int textSize;




        for (Key key : keys) {
            if (apiLevel >= Build.VERSION_CODES.LOLLIPOP) {
                drawRoundedKey(canvas, key);
            } else {
                drawSquareKey(canvas, key);
            }

            drawKeyIcon(canvas, key);

            textSize = (int)(key.height * 0.5);
            textPaint.setTextSize(textSize);

            if (key.label != null) {
                canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                        key.y + ((key.height + textSize)/ 2) - (keyShrink/2)  , textPaint);
            }
        }
    }

    /**
     * Metoda za pribavljanje stanja shift dugmeta na tastaturi.
     *
     * @return Stanje shift dugmeta na tastaturi tipa int koje moze da bude:
     *         1. NO_SHIFT (vrednost 0)
     *         2. SHIFT    (vrednost 1)
     *         3. CAPSLOCK (vrednost 2)
     */
    public int getShiftState() {
        return shiftState;
    }

    /**
     * Metoda za setovanje stanja shift dugmeta na tastaturi.
     *
     * @param shiftState Novo stanje shift dugmeta tipa int koje moze da bude:
     *                   1. NO_SHIFT (vrednost 0)
     *                   2. SHIFT    (vrednost 1)
     *                   3. CAPSLOCK (vrednost 2)
     */
    public void setShiftState(int shiftState) {
        invalidateAllKeys();
        this.shiftState = shiftState;
    }

    /**
     * Metoda za učitavanje bitmap slika za specijalne dugmiće na tastaturi.
     */
    private void loadKeyIcons() {
        backspaceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.backspace_grey);
        enterBitmap     = BitmapFactory.decodeResource(getResources(), R.drawable.enter_grey);
        noShiftBitmap   = BitmapFactory.decodeResource(getResources(), R.drawable.no_shift);
        shiftBitmap     = BitmapFactory.decodeResource(getResources(), R.drawable.shift);
        capsLockBitmap  = BitmapFactory.decodeResource(getResources(), R.drawable.capslock);

        backspaceBitmap = Bitmap.createScaledBitmap(backspaceBitmap,
                keyHeight - keyShrink*2, keyHeight - keyShrink*2, false);
        enterBitmap     = Bitmap.createScaledBitmap(enterBitmap,
                keyHeight - keyShrink*2, keyHeight - keyShrink*2, false);
        noShiftBitmap   = Bitmap.createScaledBitmap(noShiftBitmap,
                keyHeight - keyShrink*2, keyHeight - keyShrink*2, false);
        shiftBitmap     = Bitmap.createScaledBitmap(shiftBitmap,
                keyHeight - keyShrink*2, keyHeight - keyShrink*2, false);
        capsLockBitmap  = Bitmap.createScaledBitmap(capsLockBitmap,
                keyHeight - keyShrink*2, keyHeight - keyShrink*2, false);
    }

    /**
     * Metoda za iscrtavanje bitmap slike za specijalni dugmić na tastaturi.
     *
     * @param canvas Canvas na kom se iscrtava bitam slika.
     * @param key Key za koji se bitmap slika iscrtava.
     */
    private void drawKeyIcon(Canvas canvas, Key key) {
        switch (key.codes[0]) {

            // SHIFT dugme
            case -1:
                switch (shiftState) {
                    case NO_SHIFT:
                        canvas.drawBitmap(noShiftBitmap,
                                key.x + (key.width - noShiftBitmap.getWidth())/2,
                                key.y + keyShrink, null);
                        break;

                    case SHIFT:
                        canvas.drawBitmap(shiftBitmap,
                                key.x + (key.width - shiftBitmap.getWidth())/2,
                                key.y + keyShrink, null);
                        break;

                    case CAPSLOCK:
                        canvas.drawBitmap(capsLockBitmap,
                                key.x + (key.width - capsLockBitmap.getWidth())/2,
                                key.y + keyShrink, null);
                        break;
                }
                break;

            // BACKSPACE dugme
            case -5:
                canvas.drawBitmap(backspaceBitmap,
                        key.x + (key.width - backspaceBitmap.getWidth())/2,
                        key.y + keyShrink, null);
                break;

            // ENTER dugme
            case 10:
                canvas.drawBitmap(enterBitmap,
                        key.x + (key.width - enterBitmap.getWidth())/2,
                        key.y + keyShrink, null);
                break;
            default:

        }
    }

    /**
     * Metoda za iscrtavanje zaobljenih dugmića na tastaturi.
     *
     * @param canvas Canvas na kom se dugmić iscrtava.
     * @param key Key koji se iscrtava.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawRoundedKey(Canvas canvas, Key key) {
        if (key.pressed) {
            canvas.drawRoundRect(key.x + keyShrink,
                    key.y + keyShrink,
                    key.x + key.width - keyShrink,
                    key.y + key.height - keyShrink,
                    keyShrink * 2, keyShrink * 2, keyboardBackground);
        } else {
            if (key.codes[0] == 10 || key.codes[0] == Keyboard.KEYCODE_DELETE || key.codes[0] == -1) {
                canvas.drawRoundRect(key.x + keyShrink,
                        key.y + keyShrink,
                        key.x + key.width - keyShrink,
                        key.y + key.height - keyShrink,
                        keyShrink * 2, keyShrink * 2, specialKeyColor);
            } else {
                canvas.drawRoundRect(key.x + keyShrink,
                        key.y + keyShrink,
                        key.x + key.width - keyShrink,
                        key.y + key.height - keyShrink,
                        keyShrink * 2, keyShrink * 2, keyColor);
            }
        }
    }

    /**
     * Metoda za iscrtavanje dugmića sa spicastim ivicama na tastaturi.
     *
     * @param canvas Canvas na kom se dugmić iscrtava.
     * @param key Key koji se iscrtava.
     */
    private void drawSquareKey(Canvas canvas, Key key) {
        if (key.pressed) {
            canvas.drawRect(key.x + keyShrink,
                    key.y + keyShrink,
                    key.x + key.width - keyShrink,
                    key.y + key.height - keyShrink,
                    keyboardBackground);
        } else {
            if (key.codes[0] == 10|| key.codes[0] == Keyboard.KEYCODE_DELETE || key.codes[0] == -1) {
                canvas.drawRect(key.x + keyShrink,
                        key.y + keyShrink,
                        key.x + key.width - keyShrink,
                        key.y + key.height - keyShrink,
                        specialKeyColor);
            } else {
                canvas.drawRect(key.x + keyShrink,
                        key.y + keyShrink,
                        key.x + key.width - keyShrink,
                        key.y + key.height - keyShrink,
                        keyColor);
            }
        }
    }
}
