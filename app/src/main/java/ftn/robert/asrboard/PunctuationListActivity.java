package ftn.robert.asrboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ftn.robert.sentence_member.Sentence;
import ftn.robert.sentence_member.StringMap;

/**
 * Activity za prikaz liste dostupnih znaka interpunkcije koji se mogu prepoznati ASR sistemom.
 * PunctiationListActivity nasleđuje AppCompatActivity.
 *
 * Created by Robert on 15.2.2017.
 */

public class PunctuationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.punctuation_list);

        ArrayList<String> list = new ArrayList<>(StringMap.stringToPunctuationMap.size() +
                StringMap.stringToSpecialCharMap.size());

        for (String s : StringMap.stringToPunctuationMap.keySet()) {
            list.add(s);
        }

        for (String s : StringMap.stringToSpecialCharMap.keySet()) {
            list.add(s);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.punctuationList);
        listView.setAdapter(adapter);
    }
}
