package ftn.robert.asrboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

/**
 * Predstavlja prozor za proveru odgovarajućih dozvola ASRBoard servisa, kao i omogućavanje servisa
 * sa unos, kao i odabir podrazumevanog servisa za unos.
 * SettingActivity nasleđuje AppCompatActivitz klasu.
 *
 * Created by Robert on 3.2.2017.
 */

public class SettingsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button listButton = (Button) findViewById(R.id.listButton);

        setButton(button1);
        setButton(button2);
        setButton(listButton);

        // Ukoliko ne postoji dozvola za snimanje zvuka onda se ona zatrazi od korisnika
        if (!isPermissionEnabled()) {
            requestPermission();
        }
    }

    /**
     * Metoda za proveru da li je ASRBoard omogućen kao servis za unos.
     *
     * @return Boolean vrednost. True ukoliko je ASRBord omogućen kao servis za unos.
     *         False ukoliko nije.
     */
    private boolean isInputMethodEnabled() {

        // ID ASRBoard servisa
        String myInputMethod = (new ComponentName(getApplicationContext(), ASRKeyboardIME.class)).flattenToShortString();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        for (int i = 0, n = mInputMethodProperties.size(); i < n; i++) {
            InputMethodInfo imi = mInputMethodProperties.get(i);

            String id = imi.getId();

            if (myInputMethod.equals(id)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Metoda za proveru da li je ASRBoard podrazumevani servis za unos.
     *
     * @return Boolean vrednost. True ukoliko je ASRBoard podrazumevani servis za unos.
     *         False ukoliko nije.
     */
    private boolean isDefaultInputMethod() {
        // ID ASRBoard servisa
        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        imeManager.showInputMethodPicker();
        return true;
    }

    /**
     * Metoda za kreiranje dialoga za ommogućavanje ASRBoard serivsa za unos.
     *
     * @return AlertDialog.
     */
    private AlertDialog inputMethodEnabledAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setMessage(R.string.input_method_dialog_message)
                .setTitle(R.string.input_method_dialog_title);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                startActivityForResult(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS), 0);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    /**
     * Metoda za kreiranje dialoga za odabir ASRBoard za podrazumevani servis za unos.
     *
     * @return AlertDialog.
     */
    private AlertDialog defaultInputMethodAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setMessage(R.string.default_input_method_dialog_message)
                .setTitle(R.string.default_input_method_dialog_title);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    /**
     * Metoda za proveru da li postoji odgovarajuća dozvola.
     *
     * @return Boolean vrednost. True ukoliko postoji dozvola. False ukoliko ne postoji.
     */
    private boolean isPermissionEnabled() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Metoda za traženje dozvole od korisnika.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
    }

    /**
     * Metoda za podešavanje OnClickListenera za svaki button na ekranu.
     *
     * @param button Button za koji se podešava OnClikcListener.
     */
    private void setButton(Button button) {
        switch (button.getId()) {
            case R.id.button1:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Proveri se da li je omogucena tastatura, a ako nije korisnik se pita da omoguci
                        if (!isInputMethodEnabled()) {
                            AlertDialog dialog = inputMethodEnabledAlertDialog();
                            dialog.show();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.input_method_alredy_enabled, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                break;
            case R.id.button2:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Proverava se da li je tastatura podrazumevana, a ako nije korisnik se pita da je
                        // postavi kao podrazumevanu
                        if (!isDefaultInputMethod()) {
                            AlertDialog dialog = defaultInputMethodAlertDialog();
                            dialog.show();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.alredy_default_input_method, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
                break;
            case R.id.listButton:
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), PunctuationListActivity.class));
                    }
                });
        }
    }
}
