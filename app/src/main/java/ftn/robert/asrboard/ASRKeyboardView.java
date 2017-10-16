package ftn.robert.asrboard;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Predstavlja view ASRKeyboardIME servisa. Instanca ove klase se renderuje
 * u onCreateInputView callback metodi ASRKeyboardIME servisa.
 *
 * Created by Robert on 3.2.2017.
 */

public class ASRKeyboardView extends RelativeLayout {

    /** Boolean promenljiva u kojoj se pamti da li je snimanje aktivno ili ne. */
    private boolean isListening;

    private Context context;

    // Vizuelni elementi u view-u

    /** ImageButton polje za preusmeravanje korisnika na activity za podešavanja (slika zupčanika) */
    private ImageButton settingsButton;

    /** ImageButton polje za zatvaranje tastature (slika X) */
    private ImageButton closeButton;

    /** ImageButton polje za pokretanje/zaustavljanje ASR sistema (slika mikrofona) */
    private ImageButton listenButton;

    /** TextView polje koje se nalazi iznad dugmeta listen i prikazuje trenutni status. */
    private TextView statusText;

    /** SoftKeyboardView polje u kome je smeštena sama tastatura i nalazi se izpod dugmeta listen. */
    private SoftKeyboardView mSoftKeyboardView;


    /**
     * Metoda za pribavljanje stanja shift dugmeta na tastaturi.
     *
     * @return Stanje shift dugmeta na tastaturi tipa int koje moze da bude:
     *         1. NO_SHIFT (vrednost 0)
     *         2. SHIFT    (vrednost 1)
     *         3. CAPSLOCK (vrednost 2)
     *
     *         Ova stanja su definisana u SoftKeyboardView klasi.
     */
    public int getShiftState() {
        return mSoftKeyboardView.getShiftState();
    }

    /**
     * Metoda za setovanje stanja shift dugmeta na tastaturi.
     *
     * @param shiftState Novo stanje shift dugmeta tipa int koje moze da bude:
     *                   1. NO_SHIFT (vrednost 0)
     *                   2. SHIFT    (vrednost 1)
     *                   3. CAPSLOCK (vrednost 2)
     *
     *                   Ova stanja su definisana u SoftKeyboardView klasi.
     */
    public void setShiftState(int shiftState) {
        mSoftKeyboardView.setShiftState(shiftState);
    }

    /**
     * Konstruktor kojim se samo pamti context iz kog je view potekao.
     */
    public ASRKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * Callback metoda koja se poziva kada se zavrsi renderovanje kako bi se pronasli view-ovi
     * i setovali njihovi odgovarajuci listeneri.
     * Ovo se ne moze raditi u konstruktoru jer aplikacija puca.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        settingsButton = (ImageButton) findViewById(R.id.settingButton);
        closeButton = (ImageButton) findViewById(R.id.closeButton);
        listenButton = (ImageButton) findViewById(R.id.listenButton);
        statusText = (TextView) findViewById(R.id.statusText);
        mSoftKeyboardView = (SoftKeyboardView) findViewById(R.id.softKeyboard);

        mSoftKeyboardView.setKeyboard(new SoftKeyboard(context, R.xml.qwerty));
        mSoftKeyboardView.setPreviewEnabled(false);
        listenButton.setSoundEffectsEnabled(false);

        isListening = false; // u pocetku ASR nije aktivan
        mSoftKeyboardView.setShiftState(SoftKeyboardView.NO_SHIFT); // u pocetku SHIFT nije pritisnut
    }

    /**
     * Metoda za postavljanje OnKeyboardActionListener-a za dugmice vezane za sam KeyboardView
     * (znaci interpunkcije, backspace, shift, space i enter).
     *
     * @param listener Klasa koja je registrovana kao KeyboardView.OnKeyboardActionListener.
     */
    public void setOnASRKeyboardActionListener(KeyboardView.OnKeyboardActionListener listener) {
        mSoftKeyboardView.setOnKeyboardActionListener(listener);
    }

    /**
     * Metoda za postavljanje OnClickListenera-a za tri dugmica (settings, close i listen) koji se
     * nalaze na tastatruri.
     *
     * @param listener Klasa koja je registrovana kao View.OnClickListener.
     */
    public void setOnButtonsClickListener(View.OnClickListener listener) {
        listenButton.setOnClickListener(listener);
        settingsButton.setOnClickListener(listener);
        closeButton.setOnClickListener(listener);
    }

    /**
     * Metoda koja se poziva kada se detektuje da je korisnik poceo sa govorom.
     * Poziva se samo radi vizuelnih promena, tj. promene status teksta.
     */
    public void onSpeechBegin() {
        if (isListening) {
            statusText.setText(getResources().getString(R.string.tap_to_pause));
        }
    }

    /**
     * Metoda kojom se u zavisnosti od ulaznog parametra listening cine vizuelne promene na
     * tastaturi.
     * @param listening Ukoliko je true, status tekst se postavlja na "Speak now" i ikonica
     *                  mikrofona se menja u zelenu, cime se signalizuje da je slusanje ASR sistema
     *                  aktivno.
     *                  Ukoliko je false, status tekst se postavlja na "Tap to speak" i ikonica
     *                  mikrofona se menja u belu, cine se signalizuje da slusanje ASR sistema
     *                  nije aktivno.
     */
    public void setListening(boolean listening) {
        isListening = listening;

        if (isListening) {
            listenButton.setImageResource(R.drawable.microphone_active);
            statusText.setText(getResources().getString(R.string.speak_now));
        } else {
            listenButton.setImageResource(R.drawable.microphone_inactive);
            statusText.setText(getResources().getString(R.string.tap_to_speak));
        }
    }

}



