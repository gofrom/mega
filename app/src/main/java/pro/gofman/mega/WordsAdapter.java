package pro.gofman.mega;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;

import java.util.List;

/**
 * Created by gofman on 07.09.15.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {


    private List<String> dsWords;


    public WordsAdapter( List<String> ds ) {
        this.dsWords = ds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_words, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        String w = dsWords.get(i);

        //Log.d("1", "Кол-во: " + String.valueOf(dsElements.size()) + " " + el.getItem());
        vh.mItem.setText(w);
        vh.deleteButtonClickListener.setWord( w );



    }

    @Override
    public int getItemCount() {
        return this.dsWords.size();
    }
    public void addItem(int pos, String w) {
        this.dsWords.add(pos, w);
        notifyItemInserted(pos);
    }
    public void removeItem(String w) {
        int pos = this.dsWords.indexOf( w );
        this.dsWords.remove(pos);

        if ( MainActivity.db.isOpen() ) {
            MainActivity.db.delete(
                    DatabaseHandler.TABLE_WORDS,
                    DatabaseHandler.KEY_ITEM + " =? ",
                    new String[]{w}
            );
        }

        notifyItemRemoved(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mItem;
        private com.rey.material.widget.Button mDelete;
        private DeleteButtonClickListener deleteButtonClickListener;




        public ViewHolder(View v) {
            super(v);

            mItem = (TextView) v.findViewById(R.id.tv_word_item);
            mDelete = (Button) v.findViewById(R.id.btn_word_del);
            mDelete.applyStyle(R.style.LightFlatButtonRippleStyle);

            deleteButtonClickListener = new DeleteButtonClickListener();
            mDelete.setOnClickListener(deleteButtonClickListener);

        }
    }

    class DeleteButtonClickListener implements View.OnClickListener {
        private String el;

        @Override
        public void onClick(View v) {
            if (this.el != null) {
                //Log.d("DeleteButtonClick", "1");
                final Dialog dgDelete = new Dialog(v.getContext());

                dgDelete.title(R.string.dialog_delete_word)
                        .negativeAction(R.string.dialog_cancel)
                        .positiveAction(R.string.dialog_delete)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("positiveAction", "1");
                                removeItem(el);
                                dgDelete.dismiss();

                            }
                        })
                        .negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("negativeAction", "2");
                                dgDelete.dismiss();
                            }
                        });

                dgDelete.show();

            }
        }

        public void setWord(String w) {
            this.el = w;
        }
    }
}
