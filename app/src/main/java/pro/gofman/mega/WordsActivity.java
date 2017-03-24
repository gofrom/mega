package pro.gofman.mega;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;
import com.rey.material.app.Dialog;
import com.rey.material.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class WordsActivity extends AppCompatActivity {

    // private Elements el;
    private int elid = 0;
    private RecyclerView rvWords;
    private WordsAdapter adWords;
    private RecyclerView.LayoutManager lmWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);

        rvWords = (RecyclerView) findViewById(R.id.rv_words);
        lmWords = new LinearLayoutManager(this);
        rvWords.setLayoutManager(lmWords);


        elid = getIntent().getIntExtra( Elements.ELID, 0 );


        adWords = new WordsAdapter( getWordsByElementID(elid) );
        //adWords.setActivity( this );
        //MegadevicesAdapter( md.getAllMegadevices( MainActivity.db )  );
        rvWords.setAdapter( adWords );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_words);
        fab.attachToRecyclerView(rvWords);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddWord();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Слова");
    }

    private void clickAddWord(){
        final Dialog dgAddWord = new Dialog( this );


        dgAddWord
                .title(R.string.dialog_add_word)
                .contentView(R.layout.dialog_add_word)
                .negativeAction(R.string.dialog_cancel)
                .positiveAction(R.string.dialog_add)
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("positiveAction", "1");
                        //removeItem(el);
                        EditText mWord = (EditText) dgAddWord.findViewById(R.id.et_add_word);
                        if (mWord != null) {
                            if (MainActivity.db.isOpen()) {
                                String w = mWord.getText().toString();

                                //Log.d("clickAddWord", w + " " + String.valueOf(elid));

                                ContentValues cv = new ContentValues();
                                cv.put( DatabaseHandler.KEY_WORDS_IDELEMENT, elid);
                                cv.put( DatabaseHandler.KEY_ITEM, w );

                                MainActivity.db.insert(
                                        DatabaseHandler.TABLE_WORDS,
                                        null,
                                        cv
                                );
                                adWords.addItem( adWords.getItemCount(), w );

                            }


                        }


                        dgAddWord.dismiss();

                    }
                })
                .negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d("negativeAction", "2");
                        dgAddWord.dismiss();
                    }
                });

        dgAddWord.show();


    }

    private List<String> getWordsByElementID(int elid) {
        List<String> w = new ArrayList<String>();

        if ( !MainActivity.db.isOpen() ) {
            return w;
        }

        Cursor c = MainActivity.db.rawQuery(
                "SELECT * FROM " + DatabaseHandler.TABLE_WORDS
                        + " WHERE " + DatabaseHandler.KEY_WORDS_IDELEMENT
                        + " = " + String.valueOf(elid),
                null
        );

        if ( c.moveToFirst() ) {
            do {
                w.add( c.getString( c.getColumnIndex( DatabaseHandler.KEY_ITEM ) ) );
            } while ( c.moveToNext() );
            c.close();
        }


        return w;
    }


}
