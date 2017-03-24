package pro.gofman.mega;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by gofman on 10.07.15.
 */

// public class MegadevicesAdapter extends RecyclerView.Adapter<MegadevicesAdapter.ViewHolder> {
public class MegadevicePointsAdapter extends RecyclerView.Adapter<MegadevicePointsAdapter.ViewHolder> {
    private List<MegaDevicePoint> ds;

    public MegadevicePointsAdapter(List<MegaDevicePoint> ds) {
        this.ds = ds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_elements, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

/**


public class MegadevicesAdapter extends RecyclerView.Adapter<MegadevicesAdapter.ViewHolder> {

    private List<MegaDevices> dsMegadevices;

    public MegadevicesAdapter( List<MegaDevices> ds ) {
        this.dsMegadevices = ds;
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

        Log.d("1", "Кол-во: " + String.valueOf(dsMegadevices.size()) + " " + md.getName());

        vh.mItem.setText(md.getName() + " #" + String.valueOf(md.getID()));
        vh.mIPAddr.setText(md.getIPAddr());
        vh.cardViewClickListener.setMd(md);
        vh.optionsButtonMenuListener.setMd(md);
    }

    @Override
    public int getItemCount() {
        return this.dsMegadevices.size();
    }

    public void addItem(int pos, MegaDevices md){
        this.dsMegadevices.add(pos, md);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void removeItem(SQLiteDatabase db, int pos) {
        this.dsMegadevices.get(pos).deleteMegadevice( db );
        this.dsMegadevices.remove(pos);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mItem;
        public TextView mIPAddr;
        private CardView mCard;
        private ImageView mOptionsButton;
        private CardViewClickListener cardViewClickListener;
        private OptionsButtonMenuListener optionsButtonMenuListener;


        public ViewHolder(View v) {
            super(v);
            mItem = (TextView) v.findViewById(R.id.rv_megadevice_item);
            mIPAddr = (TextView) v.findViewById(R.id.rv_megadevice_ippadr);
            mCard = (CardView) v.findViewById(R.id.card);
            mOptionsButton = (ImageView) v.findViewById(R.id.rv_megadevice_options);

            cardViewClickListener = new CardViewClickListener();
            mCard.setOnClickListener(cardViewClickListener);
            optionsButtonMenuListener = new OptionsButtonMenuListener();
            mOptionsButton.setOnClickListener(optionsButtonMenuListener);
        }
    }
    class CardViewClickListener implements View.OnClickListener {
        private  MegaDevices md;

        @Override
        public void onClick(View v) {
            Log.d("5", md.getName() + " " + md.getID());

        }

        public void setMd(MegaDevices md) {
            this.md = md;
        }
    }

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










}
*/
