package kevin20012com.dmx.modales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import kevin20012com.dmx.R;
import kevin20012com.dmx.modales.ModalAddControlList.AdaptadorItem;
import kevin20012com.dmx.modales.ModalAddControlList.Item;

public class ModalAddControl extends DialogFragment {

    private ListView listControles;
    private AdaptadorItem adaptadorListControles;

    private static final String TAG = "MyCustomDialog";

    public interface OnInputListener{
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;

    //widgets
    private EditText mInput;
    private TextView mActionOk, mActionCancel;

    //vars
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_controles, container, false);
        mActionCancel = view.findViewById(R.id.action_cancel);
        mActionOk = view.findViewById(R.id.action_ok);
        mInput = view.findViewById(R.id.input);

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: capturing input");

                String input = mInput.getText().toString();
//                if(!input.equals("")){
//
//                    //Easiest way: just set the value
//                    ((MainActivity)getActivity()).mInputDisplay.setText(input);
//
//                }

                //"Best Practice" but it takes longer
                mOnInputListener.sendInput(input);

                getDialog().dismiss();
            }
        });

        listControles = (ListView) view.findViewById(R.id.listControles);
        adaptadorListControles = new AdaptadorItem(getActivity(), GetControlesItems());
        listControles.setAdapter(adaptadorListControles);

        listControles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object listItem = list.getItemAtPosition(position);
                String input = "";

                switch (position) {
                    case 0:
                        input = "switch";
                        break;
                    case 1:
                        input = "pad";
                        break;
                    case 2:
                        input = "joy";
                        break;
                }

                if (input != "") { //TODO esto significa que le dio click a un elemento d mi listview y agregara ese control
                    mOnInputListener.sendInput(input);
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    private ArrayList<Item> GetControlesItems(){
        ArrayList<Item> listItems = new ArrayList<>();
        listItems.add(new Item(R.drawable.switch_icon, "Switch" ,"Alterna entre el valor 0(off) y 255(on) de un canal"));
        listItems.add(new Item(R.drawable.pad_icon, "Pad" ,"El eje X es el valor del primer canal y el eje Y del segundo"));
        listItems.add(new Item(R.drawable.joystick_icon, "Joystick" ,"El eje X es el valor del primer canal y el eje Y del segundo, se regresa al centro cuando no lo tocas"));

        return listItems;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
