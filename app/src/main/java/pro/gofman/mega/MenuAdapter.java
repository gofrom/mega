package pro.gofman.mega;


import android.content.Intent;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.Dialog;


import java.util.List;

/**
 * Created by gofman on 31.08.15.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    String[] menu_type = {
            "Неопределено", "Устройство", "Заказ звонка"
    };

    private List<Menu> dsMenu;
    private MenuActivity mActivity = null;

    public MenuAdapter(List<Menu> ds ) {
        this.dsMenu = ds;
    }
    public void setActivity(MenuActivity menuActivity) {
        this.mActivity = menuActivity;
    }

    private void startActivity(Intent intent) {
        this.mActivity.startActivityForResult(intent, MenuActivity.EDIT_MENU);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_menu, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        Menu mn = dsMenu.get(i);

        Log.d("1", "Кол-во: " + String.valueOf(dsMenu.size()) + " " + mn.getItem());

        vh.mItem.setText(mn.getItem());
        vh.mMenuPic.setImageResource(mn.getImg());

        vh.cardViewClickListener.setMenuID(mn.getID(), i);
        vh.deleteButtonClickListener.setMn( mn );

    }

    @Override
    public int getItemCount() {
        return this.dsMenu.size();
    }

    public void addItem(int pos, Menu mn){
        this.dsMenu.add(pos, mn);
        notifyDataSetChanged();
    }

    public void updateItem(int pos, Menu mn) {
        this.dsMenu.set( pos, mn );
        notifyItemChanged(pos);
        notifyDataSetChanged();
    }

    public void removeItem(Menu mn) {
        int pos = this.dsMenu.indexOf(mn);
        this.dsMenu.remove(pos);
        mn.setDB( MainActivity.db );
        mn.deleteMenu();

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mItem;
        private CardView mCard;
        private ImageView mMenuPic;
        private Button mDelete;
        private CardViewClickListener cardViewClickListener;
        private DeleteButtonClickListener deleteButtonClickListener;



        public ViewHolder(View v) {
            super(v);

            mItem = (TextView) v.findViewById(R.id.tv_menu_item);
            mCard = (CardView) v.findViewById(R.id.card_menu);
            mMenuPic = (ImageView) v.findViewById(R.id.iv_menu_pic);
            mDelete = (Button) v.findViewById(R.id.btn_menu_del);
            mDelete.applyStyle(R.style.LightFlatButtonRippleStyle);

            cardViewClickListener = new CardViewClickListener();
            mCard.setOnClickListener(cardViewClickListener);

            deleteButtonClickListener = new DeleteButtonClickListener();
            mDelete.setOnClickListener(deleteButtonClickListener);

        }
    }
    class CardViewClickListener implements View.OnClickListener {
        private int mnid = 0;
        private int pos = 0;

        @Override
        public void onClick(View v) {
            //Log.d("ElementRecycleView", el.getItem() + " " + String.valueOf(el.getID()));


            Intent intent = new Intent( v.getContext(), MenuEditActivity.class);
            intent.putExtra( Menu.MENUID, this.mnid );
            intent.putExtra( Menu.MENUPOS, this.pos );

            startActivity(intent);


            /*
            final Dialog dgEditMenu = new Dialog( v.getContext() );
            dgEditMenu.contentView(R.layout.activity_menu_edit);
            dgEditMenu.se
            Spinner sp_type = (Spinner) dgEditMenu.findViewById(R.id.sp_menu_type);
            sp_type.setAdapter(new ArrayAdapter<String>(v.getContext(), R.layout.row_spinner, menu_type));
            //sp_type.setSelection(mn.getType());

            dgEditMenu.title("Меню")
                    .negativeAction(R.string.dialog_cancel)
                    .positiveAction(R.string.dialog_save)
                    .positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Log.d("positiveAction", "1");


                            dgEditMenu.dismiss();

                        }
                    })
                    .negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Log.d("negativeAction", "2");
                            dgEditMenu.dismiss();
                        }
                    });

            dgEditMenu.show();
            */



        }

        public void setMenuID(int mn, int pos) {
            this.mnid = mn;
            this.pos = pos;
        }
    }
    class DeleteButtonClickListener implements View.OnClickListener {
        private Menu mn;

        @Override
        public void onClick(View v) {
            if ( this.mn != null ) {
                //Log.d("DeleteButtonClick", "1");
                final Dialog dgDelete = new Dialog( v.getContext() );

                dgDelete.title(R.string.dialog_delete_menu)
                        .negativeAction(R.string.dialog_cancel)
                        .positiveAction(R.string.dialog_delete)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("positiveAction", "1");
                                removeItem( mn );
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
        public void setMn(Menu mn) {
            this.mn = mn;
        }
    }
}
