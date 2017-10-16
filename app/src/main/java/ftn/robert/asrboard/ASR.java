package ftn.robert.asrboard;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Služi za pravilno instanciranje SpeechRecognizer klase, njeno startovanje,
 * obradu njenih rezultata ili kodova greške, kao i pravilno gašenje.
 * ASR klasa implementira RecognitionListener.
 *
 * Created by Robert on 1.2.2017.
 */
public abstract class ASR implements RecognitionListener {

    private static SpeechRecognizer myASR;
    Context ctx;
    public static final int SPEECH_RECOGNITION_NOT_SUPPORTED = -100;
    private static final String LIB_LOGTAG = "ASRLIB";

    /**
     * Konstruktor u kome se prvo proverava da li uredjaj podrzava SpeechRecognizer. Ukoliko
     * ne podrzava, baca se Exception, a ukoliko podrzava, pravi se nova instanca
     * SpeechRecognizer klase.
     *
     * @param ctx Context
     * @throws Exception Exception koji se baca ukoliko uredjaj ne podrzava SpeechRecognizer.
     */
    public ASR(Context ctx) throws Exception {
        this.ctx = ctx;

        PackageManager packManager = ctx.getPackageManager();

        // Iz nekog razloga stalno vraca prazan niz za api < 20 (za sada samo na emulatoru provereno)
        List intActivities = packManager
                .queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

        if(intActivities.size() != 0) {
            myASR = SpeechRecognizer.createSpeechRecognizer(ctx);
            myASR.setRecognitionListener(this);
        } else {
            myASR = null;
            this.processAsrError(SPEECH_RECOGNITION_NOT_SUPPORTED);
            throw new Exception("SpeechRecognizer not supported");
        }
    }

    /**
     * Metoda za startovanje SpeechRecognizer-a.
     */
    public void listen(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        /** The extra key used in an intent to the speech recognizer for voice search. */
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.ctx.getPackageName());

        /** Optional limit on the maximum number of results to return. */
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        /** Optional IETF language tag (as defined by BCP 47), for example "en-US". */
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "sr-RS");

        /** Informs the recognizer which speech model to prefer when
         * performing ACTION_RECOGNIZE_SPEECH. */
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        /** Optional boolean to indicate whether partial results should be returned by
         * the recognizer as the user speaks (default is false). */
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        /** The minimum length of an utterance. */
        intent.putExtra(RecognizerIntent.
                EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000);

        /** The amount of time that it should take after we stop hearing
         * speech to consider the input complete. */
        intent.putExtra(RecognizerIntent.
                EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 4000);

        /** The amount of time that it should take after we stop hearing
         * speech to consider the input possibly complete. */
        intent.putExtra(RecognizerIntent.
                EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);

        myASR.startListening(intent);
    }

    public void destroy() {
        if (myASR != null) {
            myASR.destroy();
        }
    }

    public void onResults(Bundle results) {
        Log.d(LIB_LOGTAG, "ASR results provided");
        if(results != null) {
            if(Build.VERSION.SDK_INT >= 14) { // ova provera gotovo da nije ni potrebna posto je minSDK verzija vec 18
                this.processAsrResults(results.getStringArrayList("results_recognition"), results.getFloatArray("confidence_scores"));
            } else {
                this.processAsrResults(results.getStringArrayList("results_recognition"), null);
            }
        } else {
            this.processAsrError(SpeechRecognizer.ERROR_NO_MATCH);
        }
    }

    public void onPartialResults(Bundle partialResults) {
        this.processAsrPartialResult(partialResults);
    }

    public void onReadyForSpeech(Bundle arg0) {
        this.processAsrReadyForSpeech();
    }

    public void onError(int errorCode) {
        this.processAsrError(errorCode);
    }

    public void onBeginningOfSpeech() {
        this.processBeginningOfSpeech();
    }

    public void onBufferReceived(byte[] buffer) {
    }

    public void onEndOfSpeech() {
    }

    public void onEvent(int arg0, Bundle arg1) {
    }

    public void onRmsChanged(float arg0) {
    }

    public abstract void processAsrPartialResult(Bundle partialResults);

    public abstract void processAsrResults(ArrayList<String> var1, float[] var2);

    public abstract void processAsrReadyForSpeech();

    public abstract void processAsrError(int errorCode);

    public abstract void processBeginningOfSpeech();


}

