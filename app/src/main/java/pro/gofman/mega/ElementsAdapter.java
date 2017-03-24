package pro.gofman.mega;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rey.material.widget.Button;
import android.widget.TextView;
import com.rey.material.app.Dialog;


import java.util.List;

public class ElementsAdapter extends RecyclerView.Adapter<ElementsAdapter.ViewHolder> {



    private List<Elements> dsElements;
    private ElementsActivity mActivity = null;

    public ElementsAdapter( List<Elements> ds ) {
        this.dsElements = ds;
    }

    public void setActivity(ElementsActivity elementsActivity) {
        this.mActivity = elementsActivity;
    }

    private void startActivity(Intent intent) {
        this.mActivity.startActivityForResult(intent, ElementsActivity.EDIT_ELEMENT);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_elements, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        Elements el = dsElements.get(i);

        //Log.d("1", "Кол-во: " + String.valueOf(dsElements.size()) + " " + el.getItem());
        vh.mItem.setText( el.getItem() );
        vh.mDesc.setText( el.getDesc() );
        vh.mMenuItem.setText( el.getMenuItem() );

        vh.cardViewClickListener.setElID( el.getID(), i );
        vh.wordsButtonClickListener.setElID( el.getID() );
        vh.deleteButtonClickListener.setEl( el );

    }

    @Override
    public int getItemCount() {
        return this.dsElements.size();
    }
    public void updateItem(int pos, Elements el) {
        this.dsElements.set( pos, el );
        notifyItemChanged(pos);
    }

    public void addItem(int pos, Elements el){
        this.dsElements.add(pos, el);
        notifyItemInserted(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(Elements el) {
        int pos = this.dsElements.indexOf( el );
        this.dsElements.remove(pos);
        el.setDB(MainActivity.db);
        el.deleteElement();

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mItem;
        public TextView mDesc;
        private CardView mCard;
        private TextView mMenuItem;
        private com.rey.material.widget.Button mWords;
        private com.rey.material.widget.Button mDelete;
        private CardViewClickListener cardViewClickListener;
        private DeleteButtonClickListener deleteButtonClickListener;
        private WordsButtonClickListener wordsButtonClickListener;



        public ViewHolder(View v) {
            super(v);

            mItem = (TextView) v.findViewById(R.id.tv_element_item);
            mDesc = (TextView) v.findViewById(R.id.tv_element_desc);
            mCard = (CardView) v.findViewById(R.id.card_element);
            mMenuItem = (TextView) v.findViewById(R.id.tv_element_menu);
            mWords = (Button) v.findViewById(R.id.btn_element_words);
            mWords.applyStyle(R.style.LightFlatButtonRippleStyle);
            mDelete = (Button) v.findViewById(R.id.btn_element_del);
            mDelete.applyStyle(R.style.LightFlatButtonRippleStyle);


            cardViewClickListener = new CardViewClickListener();
            mCard.setOnClickListener(cardViewClickListener);

            deleteButtonClickListener = new DeleteButtonClickListener();
            mDelete.setOnClickListener(deleteButtonClickListener);

            wordsButtonClickListener = new WordsButtonClickListener();
            mWords.setOnClickListener(wordsButtonClickListener);



        }
    }
    class CardViewClickListener implements View.OnClickListener {
        private int elid;
        private int pos;

        @Override
        public void onClick(View v) {
            //Log.d("ElementRecycleView", el.getItem() + " " + String.valueOf(el.getID()));
            Intent intent = new Intent(v.getContext(), ElementEditActivity.class);
            intent.putExtra(Elements.ELID, this.elid);
            intent.putExtra(Elements.ELPOS,this.pos);

            // v.getContext().startActivity(intent);
            startActivity(intent);
        }

        public void setElID(int el, int pos) {
            this.elid = el;
            this.pos = pos;
        }
    }
    class WordsButtonClickListener implements View.OnClickListener {
        private int ElID = 0;

        @Override
        public void onClick(View v) {
            if ( this.ElID > 0 ) {
                Intent intent = new Intent(v.getContext(), WordsActivity.class);
                intent.putExtra( Elements.ELID, this.ElID );
                v.getContext().startActivity(intent);
            }
        }
        public void setElID(int dev) {
            this.ElID = dev;
        }
    }
    class DeleteButtonClickListener implements View.OnClickListener {
        private Elements el;

        @Override
        public void onClick(View v) {
            if ( this.el != null ) {
                //Log.d("DeleteButtonClick", "1");
                final Dialog dgDelete = new Dialog( v.getContext() );

                dgDelete.title(R.string.dialog_delete_element)
                        .negativeAction(R.string.dialog_cancel)
                        .positiveAction(R.string.dialog_delete)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("positiveAction", "1");
                                removeItem( el );
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
        public void setEl(Elements el) {
            this.el = el;
        }
    }

}
