package pro.gofman.mega;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rey.material.widget.Button;
import android.widget.TextView;



import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.util.List;

/**
 * Created by gofman on 26.06.15.
 */

public class MegadevicesAdapter extends RecyclerView.Adapter<MegadevicesAdapter.ViewHolder> {

    private List<MegaDevices> dsMegadevices;
    private Settings_Megadevices mActivity = null;

    public MegadevicesAdapter( List<MegaDevices> ds ) {
        this.dsMegadevices = ds;
    }
    public void setmActivity(Settings_Megadevices A) {
        this.mActivity = A;
    }
    private void startActivity(Intent intent) {
        this.mActivity.startActivityForResult(intent, Settings_Megadevices.EDIT_MEGADEVICE );
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_megadevices, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int i) {
        MegaDevices md = dsMegadevices.get(i);

        //Log.d("1", "Кол-во: " + String.valueOf(dsMegadevices.size()) + " " + md.getName());

        vh.mItem.setText(md.getName());
        vh.mDesc.setText( md.getDesc() );
        vh.mVer.setText(md.getVersion());
        vh.cardViewClickListener.setMd(md, i);
        vh.deleteButtonClickListener.setMd(md);
        vh.elementsButtonClickListener.setMdID( md.getID() );
    }

    @Override
    public int getItemCount() {
        return this.dsMegadevices.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(int pos, MegaDevices md){
        this.dsMegadevices.add(pos, md);
        notifyItemInserted( pos );
    }

    public void updateItem(int pos, MegaDevices md) {
        this.dsMegadevices.set( pos, md );
        notifyItemChanged(pos);
    }

    public void removeItem(MegaDevices md) {
        int pos = this.dsMegadevices.indexOf(md);
        this.dsMegadevices.remove(pos);
        md.deleteMegadevice(MainActivity.db);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mItem;
        public TextView mDesc;
        public TextView mVer;
        private CardView mCard;
        private Button mDelete;
        private Button mElements;
        private CardViewClickListener cardViewClickListener;
        private DeleteButtonClickListener deleteButtonClickListener;
        private ElementsButtonClickListener elementsButtonClickListener;


        public ViewHolder(View v) {
            super(v);
            mItem = (TextView) v.findViewById(R.id.tv_megadevice_item);
            mDesc = (TextView) v.findViewById(R.id.tv_megadevice_desc);
            mVer = (TextView) v.findViewById(R.id.tv_megadevice_ver);
            mCard = (CardView) v.findViewById(R.id.card_device);
            mDelete = (Button) v.findViewById(R.id.btn_megadevice_del);
            mDelete.applyStyle(R.style.LightFlatButtonRippleStyle);
            mElements = (Button) v.findViewById(R.id.btn_megadevice_el);
            mElements.applyStyle(R.style.LightFlatButtonRippleStyle);

            cardViewClickListener = new CardViewClickListener();
            mCard.setOnClickListener(cardViewClickListener);

            deleteButtonClickListener = new DeleteButtonClickListener();
            mDelete.setOnClickListener( deleteButtonClickListener );

            elementsButtonClickListener = new ElementsButtonClickListener();
            mElements.setOnClickListener( elementsButtonClickListener );

        }
    }
   class CardViewClickListener implements View.OnClickListener {
        private  MegaDevices md;
        private int pos = 0;

        @Override
        public void onClick(View v) {
            //Log.d( "CardView", md.getName() + " " + md.getID() );

            Intent intent = new Intent(v.getContext(), MegaDeviceEdit.class);
            intent.putExtra(MegaDeviceEdit.mdID, this.md.getID());
            intent.putExtra(MegaDevices.DEVPOS, this.pos );

            startActivity(intent);

        }

        public void setMd(MegaDevices md, int pos) {
            this.md = md;
            this.pos = pos;
        }
    }

    class DeleteButtonClickListener implements View.OnClickListener {
        private MegaDevices md;

        @Override
        public void onClick(View v) {
            if ( this.md != null ) {
                //Log.d("DeleteButtonClick", "1");
                final Dialog dgDelete = new Dialog( v.getContext() );

                dgDelete.title(R.string.dialog_delete_device)
                        .negativeAction(R.string.dialog_cancel)
                        .positiveAction(R.string.dialog_delete)
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("positiveAction", "1");
                                removeItem( md );
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
                // removeItem( md );
            }
        }
        public void setMd(MegaDevices md) {
            this.md = md;
        }
    }

    class ElementsButtonClickListener implements View.OnClickListener {
        private int DevID = 0;

        @Override
        public void onClick(View v) {
            if ( this.DevID > 0 ) {
                Intent intent = new Intent(v.getContext(), ElementsActivity.class);
                intent.putExtra( MegaDevices.DEVID, this.DevID );
                v.getContext().startActivity(intent);
            }
        }
        public void setMdID(int dev) {
            this.DevID = dev;
        }
    }

    /*
    class OptionsButtonMenuListener implements View.OnClickListener {
        private MegaDevices md;

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }
        private void showPopupMenu(View v) {
            PopupMenu m = new PopupMenu(v.getContext(), v);
            m.inflate(R.menu.menu_card_megadevice);

            PopupItemClickListener popupItemClickListener;
            popupItemClickListener = new PopupItemClickListener(v.getContext());
            popupItemClickListener.setMd(this.md);
            m.setOnMenuItemClickListener(popupItemClickListener);

            m.show();
        }
        public void setMd(MegaDevices md) {
            this.md = md;
        }

    }
    class PopupItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private MegaDevices md;
        private Context context;

        public PopupItemClickListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (item.getItemId() == R.id.edit_megadevice ) {

                //Toast t = Toast.makeText(this.context, this.md.getName(), Toast.LENGTH_SHORT);
                //t.show();

                Intent intent = new Intent(this.context, MegaDeviceEdit.class);
                intent.putExtra( MegaDeviceEdit.mdID, this.md.getID() );

                this.context.startActivity(intent);


            }
            return false;
        }


        public void setMd(MegaDevices md) {
            this.md = md;
        }
    }

    */


}
