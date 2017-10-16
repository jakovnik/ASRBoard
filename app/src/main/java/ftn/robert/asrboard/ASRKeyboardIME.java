package ftn.robert.asrboard;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.util.ArrayList;
import ftn.robert.sentence_member.ComposingText;

/**
 * Predstavlja Input Method Editor (nasleđuje InputMethodServise klasu) za
 * Automatic Speech Recognition servis za unos. Kako bi servis mogao biti
 * prikazan na ekranu u tu svrhu se koristi ASRBoardView klasa. Kako bi ASRKeyobardIME klasa mogla
 * uspešno da komunicira sa svojim View-om, implementira dva interfejsa:
 *
 *  1. KeyboardView.OnKezboardActionListener - pošto je za tastaturu potreban Listener za razne
 *     akcije koje se mogu javiti na njoj (najvaznija je onKey) ASRKezboardIme se registruje
 *     za tu namenu.
 *  2. View.OnClickListener - pošto se na servisu za unos sem tastature nalaze i dodatni dugmići
 *     (dugme za podešavanja, dugme za izlazak i dugme za pokretanje/zaustavljanje servisa za
 *     prepoznavanje govora) ASRKeyboardIME se registruje da bude njihov OnClickListener.
 *
 * Created by Robert on 3.2.2017.
 *
 */
public class ASRKeyboardIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, View.OnClickListener {

    private ASRKeyboardView mASRKeyboardView;
    private ASR asr;
    private boolean isListening;
    private String currentText; // tekst koji se trenutno nalazi u TextEdit polju u koji korisnik
                                // zeli da unosi tekst
    private ComposingText composingText;
    private Context context;
    private ArrayList<Character> punctuations;

    private static final String LOGTAG = "ASRBOARD";

    /**
     * Callback metoda koja se poziva kada se za servis treba stvoriti odgovarajući view.
     *
     * @return Nova instanca ASRKezboardView klase dobijena inflate metodom R.layout.ime_servise
     *         layout-a.
     */
    @Override
    public View onCreateInputView() {

        mASRKeyboardView = (ASRKeyboardView) getLayoutInflater().inflate(R.layout.ime_service, null);
        mASRKeyboardView.setOnButtonsClickListener(this);
        mASRKeyboardView.setOnASRKeyboardActionListener(this);
        isListening = false;
        context = this;

        return mASRKeyboardView;
    }

    /**
     * Metoda koja se poziva kada se pritisne X na tastaturi.
     */
    public void handleClose() {
        stopVoiceRecognition();
        requestHideSelf(0); // InputMethodService metoda za sakrivanje tastature
    }

    /**
     * Metoda koja se poziva kada se pritisne dugme za snimanje.
     */
    public void handleListening() {
        if (isListening) {
            stopVoiceRecognition();
        } else {
            // proverava se da li postoji dozvola da se snima zvuk
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                punctuations.clear();
                startVoiceRecognition();
            } else {
                // ako ne postoji dozovla korisnik se obavestava
                Toast toast = Toast.makeText(context, "You do not have permission to recod audio", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

    /**
     * Metoda koja se poziva kada se pritisne na dugme za podesavanja.
     */
    public void handleSettings() {
        stopVoiceRecognition();
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Metoda koja se poziva za brisanje unetog teksta.
     */
    public void handleBackspace() {
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }


    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }
    public void changeInputMethod() {
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //List<InputMethodInfo> enabledInputMethodList = imm.getEnabledInputMethodList();
        //imm.setInputMethod(getCurrentFocus().getWindowToken(), enabledInputMethodList.get(1).getId());
        //Toast.makeText(context, "Ulazi u pravu funkciju 1", Toast.LENGTH_SHORT).show();
        //InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        //imeManager.switchToNextInputMethod(getToken(), false /* onlyCurrentIme */);
        //imeManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imeManager.showInputMethodPicker();
        //Toast.makeText(context, "Ulazi u pravu funkciju 2", Toast.LENGTH_SHORT).show();

    }

    /**
     * Metoda za unos karaktera u TextEdit polje u zavisnosti od njegovog primaryCode-a
     *
     * @param primaryCode Jedinstveni kod za karakter
     * @param keyCodes Ukoliko taster sem primarnog koda poseduje i neke druge
     */
    public void handleCharacter(int primaryCode, int[] keyCodes) {
        if (isListening) {
            punctuations.add((char)primaryCode);
        } else {
            getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.listenButton:
                handleListening();
                break;
            case R.id.settingButton:
                handleSettings();
                break;
            case R.id.closeButton:
                handleClose();
                break;
            default:
                Log.e(LOGTAG, "Invalid button id");
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                if (!isListening) {
                    handleBackspace();
                }
                break;

            case Keyboard.KEYCODE_SHIFT:
                if (!isListening) {
                    int shiftState = mASRKeyboardView.getShiftState();
                    shiftState++;
                    shiftState %= 3;
                    mASRKeyboardView.setShiftState(shiftState);
                }
                break;
            case -2: //change input method to text
                if (!isListening) {
                    changeInputMethod();
                    //Toast.makeText(context,"na pravom je mestu",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                handleCharacter(primaryCode, keyCodes);
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    /**
     * Metoda za startovanje ASR sistema kao i za propratne vizualne promene prilikom slusanja.
     */
    private void startVoiceRecognition() {

        asr = startNewASR();
        if (asr != null) {
            isListening = true;
            asr.listen();
            mASRKeyboardView.setListening(true);
        }
    }

    /**
     * Metoda za zaustavljanje ASR sistema kao i za propratne vizualne promene.
     * Umesto da se poziva asr.stopListening() koji zaustavlja snimanje od strane asr objekta,
     * poziva se asr.destroy() jer ga on unistava i time ne ostavlja nikakvu mogucnost za
     * njegovo blokiranje.
     * Nekada nakon vise uzastopnih paljenja i gasenja snimanja, moze doci do blokiranja snimaca
     * koji kasnije ne moze da se odglavi, dok se ne restartuje tastatura. Da bi se to izbeglo,
     * asr objekat se unistava, a prilikom svakog novog snimanja on se ponovo kreira.
     */
    private void stopVoiceRecognition() {
        isListening = false;
        mASRKeyboardView.setListening(false);
        mASRKeyboardView.setShiftState(SoftKeyboardView.NO_SHIFT);
        try {
            getCurrentInputConnection().finishComposingText();
            asr.destroy();
        } catch (Exception e) {
            Log.e(LOGTAG, "ASR is null so it can not be stopped");
        }
    }

    /**
     * InputMethodService metoda.
     */
    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        asr = null;
        punctuations = new ArrayList<>(10);
    }

    /**
     * Metoda za inicijalizovanje nove instance ASR klase, zajedno sa overridovanjem abstraktnih
     * metoda.
     *
     * @return Nova instanca ASR klase.
     */
    private ASR startNewASR() {
        try {
            return new ASR(context) {

                /**
                 * Zavrsna metoda koja se poziva kada server da svoju konacnu odluku o rezultatu.
                 *
                 * @param nBestList        ArrayList-a svih mogucih rezultata. Ima onoliko elemenata koliko se
                 *                         posalje EXTRA_MAX_RESULTS u RecognizerIntent-u.
                 * @param nBestConfidences Za svaki element ArrayList-e dolazi i odgovarajuci
                 *                         bestConfidence faktor
                 */
                @Override
                public void processAsrResults(ArrayList<String> nBestList, float[] nBestConfidences) {

                    String result = nBestList.get(0);
                    composingText = new ComposingText();
                    composingText.setInput(result, currentText);

                    switch (mASRKeyboardView.getShiftState()) {
                        case SoftKeyboardView.NO_SHIFT:
                            result = composingText.returnText();
                            break;
                        case SoftKeyboardView.SHIFT:
                            result = composingText.returnCapitalizedText();
                            break;
                        case SoftKeyboardView.CAPSLOCK:
                            result = composingText.returnCapsLockText();
                            break;
                    }

                    if (!result.equals("")) {
                        getCurrentInputConnection().commitText(result, result.length());
                    }
                    if (!punctuations.isEmpty()) {
                        for (int i = 0, n = punctuations.size(); i < n; i++) {
                            getCurrentInputConnection().commitText(String.valueOf(punctuations.get(i)), 1);
                        }
                        punctuations.clear();
                    }

                    isListening = false;
                    mASRKeyboardView.setListening(false);
                    mASRKeyboardView.setShiftState(SoftKeyboardView.NO_SHIFT);
                    this.destroy();
                }

                /**
                 * Metoda koja se poziva nakon svakog delimicnog rezultata koji pristigne sa servera.
                 *
                 * @param partialResults delimicni rezultati sa servera u vidu Bundle objekta.
                 *                       Sem stringa sadrzi i confident faktore.
                 */
                @Override
                public void processAsrPartialResult(Bundle partialResults) {
                    ArrayList<String> partialResult = partialResults.
                            getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String result;

                    if (partialResult != null) {
                        result = partialResult.get(0);
                        if (!result.equals("")) {

                            composingText = new ComposingText();
                            composingText.setInput(result, currentText);

                            switch (mASRKeyboardView.getShiftState()) {
                                case SoftKeyboardView.NO_SHIFT:
                                    result = composingText.returnText();
                                    break;
                                case SoftKeyboardView.SHIFT:
                                    result = composingText.returnCapitalizedText();
                                    break;
                                case SoftKeyboardView.CAPSLOCK:
                                    result = composingText.returnCapsLockText();
                                    break;
                            }

                            getCurrentInputConnection().setComposingText(result, result.length());
                        }
                    }
                }

                /**
                 * Metoda koja se poziva kada je ASR sistem spreman za prepoznavanje govora,
                 * kako bi se pribavio tekst koji se trenutno nalazi u TextEdit polju.
                 */
                @Override
                public void processAsrReadyForSpeech() {
                    Log.d(LOGTAG, "Ready for speech");
                    currentText = getCurrentInputConnection().getExtractedText(new ExtractedTextRequest(), 0).text.toString();
                }

                /**
                 * Metoda koja se poziva kada se desi greska u ASR sistemu. Zamisljeno je da se
                 * greska hendluje objavljivanjem Toast poruke sa odgovarajucim tekstom u
                 * zavisnosti od koda greske, ali na nekim API nivoima to ne radi.
                 *
                 * @param errorCode Kod greske.
                 */
                @Override
                public void processAsrError(int errorCode) {
                    String errorMessage;

                    switch (errorCode) {
                        case SpeechRecognizer.ERROR_AUDIO:
                            errorMessage = "Audio recording error";
                            break;
                        case SpeechRecognizer.ERROR_CLIENT:
                            errorMessage = "Other client side error";
                            break;
                        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                            errorMessage = "Insufficient permissions";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK:
                            errorMessage = "Network error";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                            errorMessage = "Network operation timed out";
                            break;
                        case SpeechRecognizer.ERROR_NO_MATCH:
                            errorMessage = "No recognition result match";
                            break;
                        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                            errorMessage = "RecognitionService busy";
                            break;
                        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                            errorMessage = "No speech input";
                            break;
                        case SpeechRecognizer.ERROR_SERVER:
                            errorMessage = "Server sends error status";
                            break;
                        case SPEECH_RECOGNITION_NOT_SUPPORTED:
                            errorMessage = "SpeechRecognizer not supported";
                            break;
                        default:
                            errorMessage = "Unknown error";
                    }

                    Toast toast = Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    isListening = false;
                    mASRKeyboardView.setListening(false);
                    mASRKeyboardView.setShiftState(SoftKeyboardView.NO_SHIFT);

                    this.destroy();
                }

                /**
                 * Metoda koja se poziva kada je SpeechRecognizer aktivan i prepozna da je korisnik
                 * počeo sa govorom. Korisiti se samo da bi se načinile vizuelne promene u View-u.
                 * Trenutno se samo menja status tekst na tastaturi.
                 */
                @Override
                public void processBeginningOfSpeech() {
                    mASRKeyboardView.onSpeechBegin();
                }
            };

        } catch (Exception e) {
            Log.e(LOGTAG, e.getMessage());
            return null;
        }
    }
}
