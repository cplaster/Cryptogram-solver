package com.xyphoid.cryptogram_solver;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends ActionBarActivity {

    private Set<String> filterList;
    private HashMap<String, String> dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dict = new HashMap<String, String>();

        final AssetManager assetManager = getAssets();

        try {
            InputStream is = assetManager.open("dict2.bin");
            ObjectInputStream input = new ObjectInputStream(is);
            dict = (HashMap<String, String>)input.readObject();
        }
        catch(Exception e) {
            Log.d("Cryptogram-solver:",  "Failed to open dictionary.");
        }

        final Button button1 = (Button)findViewById(R.id.button1);
        final Button button2 = (Button)findViewById(R.id.button2);
        //final Button buttonSave = (Button)findViewById(R.id.buttonSave);
        //final Button buttonLoad = (Button)findViewById(R.id.buttonLoad);
        final TextView textView = (TextView)findViewById(R.id.textView2);
        final EditText searchText = (EditText)findViewById(R.id.editText1);
        final EditText filterText = (EditText)findViewById(R.id.editText2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchText.getText().toString();
                String wordPattern = getWordPattern(text);

                if(dict.containsKey(wordPattern)){
                    String words = dict.get(wordPattern);
                    String[] w = words.split(",");
                    String ret = "";
                    for(String s : w) {
                        ret += "\n" + s;
                    }

                    textView.setText(ret);
                }


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String pattern = filterText.getText().toString();
                String[] words = textView.getText().toString().split("\n");
                boolean ret = true;
                String retstr = "";
                char star = '*';

                for(String word : words) {
                    ret = true;
                    for(int i = 0; i < word.length(); i++) {
                        char patternChar = pattern.charAt(i);
                        if(pattern.charAt(i) != star) {
                            if (word.charAt(i) != patternChar) {
                                ret = false;
                                break;
                            }
                        }
                    }

                    if(ret) {
                        retstr += word + "\n";
                    }
                }

                textView.setText(retstr);
            }
        });

        /*
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                try {
                    final InputStream input = new FileInputStream("/mnt/shared/Andriod_Shared/enable1.txt");

                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    String line = null;
                    HashMap<String, String> d = new HashMap<String, String>();

                    int i = 0;

                    while((line = br.readLine()) != null){
                        String pattern = getWordPattern(line);
                        if(!d.containsKey(pattern)) {
                            d.put(pattern, line);
                        } else {
                            String s = d.get(pattern);
                            s += "," + line;
                            d.put(pattern, s);

                        }

                        i++;

                        if(i % 1000 == 0) {
                            Log.d("Crytogram-solver:" , "Processing line " + Integer.toString(i));
                        }
                    }

                    saveDictionary(d, input);

                    Log.d("Crytogram-solver:" , "Dictionary build complete!");

                }
                catch(Exception e) {
                    Log.d("Cryptogram-solver:", "Exception: " + e.getMessage() + "\n");
                }

            }
        });
        */

        /*
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Log.d("Crytogram-solver:" , "Reading dictionary...");

                dict = loadDictionary();

                Log.d("Crytogram-solver:" , "Dictionary read complete!");

            }
        });
        */
    }

    private HashMap<String, String> loadDictionary() {

        HashMap<String, String> newdict;

        try {
            InputStream file = new FileInputStream("/storage/emulated/legacy/dict2.bin");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);
            newdict = (HashMap<String, String>)input.readObject();
            this.dict = newdict;
            return newdict;
        }
        catch(Exception e){
            Log.d("Cryptogram-solver:", "Exception: " + e.getMessage() + "\n");
        }

        return null;
    }

    private void saveDictionary(HashMap<String, String> dict, InputStream input) {

        //FileOutputStream outputStream;

        try {
            //File patternDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/dict.bin");
            Log.d("Cryptogram-solver:", "Writing file: " + "/storage/emulated/legacy/dict2.bin");
            //outputStream = new FileOutputStream(new File(patternDirectory.getAbsolutePath().toString()), true);
            //OutputStream buffer = new BufferedOutputStream(outputStream);

            FileOutputStream outputStream = new FileOutputStream("/storage/emulated/legacy/dict2.bin");
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeObject(dict);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getWordPattern(String line){

        String word = line.toUpperCase();
        int nextNum = 0;
        HashMap<String, String> letterNums = new HashMap<String, String>();
        List<String> wordPattern = new ArrayList<String>();

        for(char letter : word.toCharArray()){
            String l = Character.toString(letter);
            if(!letterNums.containsKey(l)) {
                letterNums.put(l, Integer.toString(nextNum));
                nextNum++;
            }

            wordPattern.add(letterNums.get(l));
        }

        String ret = "";

        for(String s : wordPattern) {
            ret += s + ".";
        }

        return ret;
    }

    private Set<String> filterResults(String subject, Set<String> list) {

        String pattern = getWordPattern(subject);
        boolean result = true;
        this.filterList = new HashSet<String>();

        for(String word : list) {
            for(Integer i = 0; i < word.length(); i++) {
                if(word.substring(i,i) == subject.substring(i,i)) {
                    result = false;
                }
            }

            if(result) {
                filterList.add(word);
            }
        }

        return filterList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }
}
