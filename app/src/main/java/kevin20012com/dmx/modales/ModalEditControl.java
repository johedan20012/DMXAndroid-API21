package kevin20012com.dmx.modales;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kevin20012com.dmx.R;
import kevin20012com.dmx.controles.Control;

public class ModalEditControl extends DialogFragment {
    private static final String TAG = "DialogEditControl";

    private Control.ControlListener controlCallback;

    private int mNumInputs = 0,mIndice = 0;

    //widgets
    private EditText mInput1,mInput2,mInput3,mInput4;
    private TextView mActionOk, mActionCancel;
    private RelativeLayout relativeLayout;
    private int[] channels;
    private int mWidth,mHeight;
    //vars
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_controls, container, false);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.layout_secundario);
        Log.d(TAG,"Lo cree");
        mInput1 = view.findViewById(R.id.input);
        mInput2 = view.findViewById(R.id.input2);
        mInput3 = view.findViewById(R.id.input3);
        mInput4 = view.findViewById(R.id.input4);
        mInput1.setText(String.valueOf(channels[0]));
        if(mNumInputs == 2) mInput2.setText(String.valueOf(channels[1]));
        mInput3.setText(String.valueOf(mWidth));
        mInput4.setText(String.valueOf(mHeight));
        mActionCancel = view.findViewById(R.id.action_cancel);
        mActionOk = view.findViewById(R.id.action_ok);
        if(mNumInputs == 1){
            relativeLayout.removeView(mInput2);
            mInput2 = null;
            TextView mText = view.findViewById(R.id.textView2);
            relativeLayout.removeView(mText);
            relativeLayout.invalidate();
        }
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
                int[] canales  = new int[1];
                if(mNumInputs == 2) canales = new int[2];

                canales[0] =(!mInput1.getText().toString().equals(""))? Integer.valueOf(mInput1.getText().toString()) : 0;
                if(mNumInputs == 2)canales[1] = (!mInput2.getText().toString().equals(""))? Integer.valueOf(mInput2.getText().toString()) : 0;

                int width = (!mInput3.getText().toString().equals(""))? Integer.valueOf(mInput3.getText().toString()) : 100;
                int height = (!mInput4.getText().toString().equals(""))? Integer.valueOf(mInput4.getText().toString()) : 100;
                controlCallback.onControlEdited(mIndice,canales,width,height);
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void Inicializar(Control.ControlListener callback,int[] inputs,int indice,int width,int height){
        Log.d(TAG,"mNumInputs:"+inputs.length);
        mNumInputs = inputs.length;

        channels = inputs;

        mWidth = width;
        mHeight = height;

        mIndice = indice;
        controlCallback = callback;
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (ModalAddControl.OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }*/

}
