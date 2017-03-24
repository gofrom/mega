package pro.gofman.mega;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;


import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;


import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Item7.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Item7#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Item7 extends Fragment {

    public static final String MENU_ID = "menu_id";

    LinearLayout root_v;
    String url = null;
    String res = null;
    boolean isConnected = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /// Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_item7, null);


        root_v = (LinearLayout) v.findViewById(R.id.root_ll_vertical);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        LinearLayout ll = null;
        TextView tv = null;



        isConnected = MainActivity.isAllowedWifi();
        if (!isConnected) {
            Toast t = Toast.makeText( v.getContext(), R.string.wifi_not_allowed, Toast.LENGTH_SHORT);
            t.show();
        }


        if(getArguments() != null) {
            int menu = getArguments().getInt(MENU_ID);

            if ( menu > 0 ) {
                List<Elements> le = new Elements(MainActivity.db).getElementsByMenuItem(menu);

                for (int i = 0; i < le.size(); i++) {

                    Elements el = le.get(i);

                    // Элемент управления типа Нагрузка
                    // управляется переключателем
                    if ( el.getType() == Elements.TYPE_LIGHT || el.getType() == Elements.TYPE_OUTLET ) {

                        // Создание LinearLayout горизонтального
                        ll = new LinearLayout( v.getContext() );
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding( 0, 32, 0, 0);

                        // Создание Switch-а
                        Switch newSwitch = new Switch( v.getContext() );

                        newSwitch.setLayoutParams( new LinearLayout.LayoutParams( MATCH_PARENT, WRAP_CONTENT, 0) );
                        newSwitch.setPadding( 32, 10, 20, 10 ); // l t r b
                        newSwitch.setId(el.getID());
                        newSwitch.setText( el.getItem() );

                        final boolean Status = getStatusElement( el );
                        newSwitch.setChecked(Status);

                        newSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                Log.d("OnCheckedChangeListener", String.valueOf(b) + " 1 " + String.valueOf(Status));

                                sendCommand( compoundButton.getId() );
                            }
                        });

                        /*
                        newSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(Switch aSwitch, boolean b) {



                                /*
                                if (!Status & b ) {
                                    Log.d("OnCheckedChangeListener", String.valueOf(b) + " 1 " + String.valueOf(Status));
                                    sendCommand( aSwitch.getId() );
                                } else {
                                    Log.d("OnCheckedChangeListener", String.valueOf(b) + " 2 " + String.valueOf(Status));
                                    sendCommand( aSwitch.getId() );
                                }
                                */
                            //}
                        //});
                        //*/

                        ll.addView(newSwitch);

                        root_v.addView(ll);
                    } // TYPE_LIGHT

                    if ( el.getType() == Elements.TYPE_LOCK ) {
                        // Создание LinearLayout горизонтального
                        ll = new LinearLayout( v.getContext() );
                        ll.setOrientation( LinearLayout.HORIZONTAL );
                        ll.setPadding( 0, 32, 0, 0);

                        Switch newButton = new Switch( v.getContext() );
                        newButton.setLayoutParams( new LinearLayout.LayoutParams( MATCH_PARENT, WRAP_CONTENT, 0) );
                        newButton.setId( el.getID() );
                        newButton.setText( el.getItem() );
                        newButton.setPadding( 32, 10, 20, 10);


                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean status = false;
                                if ( sendCommand( v.getId() ) ) {
                                    status = true;
                                    ( (Switch) v ).setChecked( status );
                                } else {
                                    status = false;
                                    ( (Switch) v ).setChecked( status );
                                }
                            }
                        });

                        /*
                        // Создание TextView
                        tv = new TextView( v.getContext() );
                        tv.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1));
                        tv.setPadding(20, 20, 0, 0); // l t r b
                        tv.setText( el.getItem() );


                        // Создание Switch-а

                        Switch newButton = new Switch( v.getContext() );

                        newButton.applyStyle(R.style.Material_Widget_Switch);
                        newButton.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0));
                        newButton.setPadding(0, 32, 20, 0); // l t r b

                        newButton.setId(el.getID());

                        //final boolean Status = getStatusElement( el );


                        newButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                boolean status = false;
                                if (sendCommand(v.getId())) {
                                    status = true;
                                    ((Switch) v).setChecked( status );
                                } else {
                                    status = false;
                                    ((Switch) v).setChecked( status );
                                }

                            }
                        });
                        */

                        //ll.addView(tv);
                        ll.addView(newButton);

                        root_v.addView(ll);

                    } // TYPE_LOCK
                 }
            }
        }
        return v;
    }

    boolean getStatusElement(Elements el) {
        boolean r = false;
        // Состояние щамка пока не умеем получать, запрос делать не надо
        if ( el.getType() == Elements.TYPE_LOCK ) {
            return r;
        }
        if (!isConnected) {
            return r;
        }

        el.setDB(MainActivity.db);
        MegaDevices md = el.getDev();


        url = "http://" + md.getIPAddr()
                + ":" + String.valueOf( md.getPort() )
                + "/" + md.getPassword()
                + "/" + "?pt=" + String.valueOf( el.getPort() )
                + "&cmd=get";

        Log.d("URL", "Ссылка: " + url);
        HttpClientRequest mr = new HttpClientRequest();
        mr.execute(new RequestType(url));
        try {
            res = mr.get();
            if (res != null) {
                r = res.equals(Elements.STATUS_ON);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return r;

    }

    boolean sendCommand(int id) {
        boolean r = false;
        Log.d("sendCommand", "есть " + String.valueOf(id) );
        if (!isConnected) {
            return r;
        }
        Elements el = new Elements(MainActivity.db, id);
        el.getElement();

        Log.d("sendCommand", "el получен " + el.getItem() + " " + String.valueOf(el.getDevID()));
        MegaDevices md = el.getDev();

        url = "http://" + md.getIPAddr()
                + ":" + String.valueOf(md.getPort())
                + "/" + md.getPassword()
                + "/" + "?cmd=" + String.valueOf( el.getPort() ) + ":2";

        Log.d("URL", "Ссылка: " + url);

        if ( el.getType() == Elements.TYPE_LIGHT ) {
            HttpClientRequest mr = new HttpClientRequest();
            mr.execute(new RequestType(url));
            r = true;
        } else if ( el.getType() == Elements.TYPE_LOCK ) {
            HttpClientRequest mr = new HttpClientRequest();
            mr.execute(new RequestType(url), new RequestType(url, 1000));
            r = true;
        }

        return r;
    }



}
