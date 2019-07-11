package kevin20012com.dmx.controles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import kevin20012com.dmx.Functions;
import kevin20012com.dmx.Punto;
import kevin20012com.dmx.R;

public class MiSwitch extends Control{
    private boolean mState = false;
    private final String TAG = "MiSwitch";

    private void setupDimensions(Context context){
        final Resources res = context.getResources();

        Rect rect = new Rect(0, 0, mWidth, mHeight);;

        final Bitmap img1_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.switch_on),mWidth,mHeight,true);
        final Bitmap img2_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.switch_off),mWidth,mHeight,true);
        final Bitmap img3_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.control_modo_edit),mWidth,mHeight,true);

        SetEditModeBitmap(new BitmapDrawable(res,img3_B));
        SetBitmap(new BitmapDrawable(res, img2_B),0);
        SetBitmap(new BitmapDrawable(res, img1_B),1);

        SetEditModeBitmapBounds(rect);
        SetBitmapBounds(rect,0);
        SetBitmapBounds(rect,1);

        SetBitmapActive(true,0);
    }

    public MiSwitch(Context context,String width,String height,int indice,int[] canales){
        super(context,1,canales,width,height);
        setupDimensions(context);
        mIndice = indice;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Invalidate the whole view. If the view is visible.
        Punto touch = new Punto();
        touch.x = event.getX();
        touch.y = event.getY();
        if(getEditMode()){
            if(HandleEditModeTouch(event)) return true;
        }else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    mState = !mState;
                    if(mState){
                        SetBitmapActive(false,0);
                        SetBitmapActive(true,1);
                    }else{
                        SetBitmapActive(true,0);
                        SetBitmapActive(false,1);
                    }
                    int[] val = {0};
                    if(mState) val[0] = 255;
                    controlCallback.onControlMoved(1,Canales,val);
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                default:
                    break;
            }
        }
        invalidate();
        ViewParent padre = this.getParent();
        if(padre != null) padre.requestDisallowInterceptTouchEvent(true);//Desactiva que mi padre ocupe mi onTouch event
        return true;
    }
}
