package kevin20012com.dmx.controles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import java.util.Set;

import kevin20012com.dmx.Functions;
import kevin20012com.dmx.Punto;
import kevin20012com.dmx.R;

public class Panel extends Control {

    private Punto mPointerPos;

    private void setupDimensions(Context context){
        final Resources res = context.getResources();

        float innerScale = 0.1f;
        Rect rectOuter,rectInner;

        final Bitmap img1_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.pad_1),mWidth,mHeight,true);
        final Bitmap img2_B = BitmapFactory.decodeResource(res,R.drawable.pad_4);
        final Bitmap img3_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.control_modo_edit),mWidth,mHeight,true);

        rectOuter = new Rect(0, 0, mWidth, mHeight);
        rectInner = new Rect(0, 0, (int) (mWidth * innerScale), (int) (mHeight * innerScale));

        SetEditModeBitmap( new BitmapDrawable(res,img3_B));
        SetBitmap(new BitmapDrawable(res, img1_B),0);
        SetBitmap(new BitmapDrawable(res, img2_B),1);

        SetEditModeBitmapBounds(rectOuter);
        SetBitmapBounds(rectOuter,0);
        SetBitmapBounds(rectInner,1);

        SetBitmapActive(true,0);
        SetBitmapActive(true,1);

        mPointerPos = new Punto(mWidth / 2, mHeight / 2);
        setInnerBounds();
    }

    public Panel(Context context,String width,String height,int indice,int[] canales){
        super(context,2,canales,width,height);
        setupDimensions(context);
        mIndice = indice;
    }

    private void setInnerBounds(){
        int width = GetBitmap(1).getBounds().width() / 2;
        int height = GetBitmap(1).getBounds().height() / 2;
        SetBitmapBounds(new Rect((int) mPointerPos.x - width, (int)mPointerPos.y - height, (int)mPointerPos.x + width, (int)mPointerPos.y + height),1);
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
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    //mPressedState = true;
                    Punto max = new Punto();
                    max.x = getWidth();
                    max.y = getHeight();

                    if (touch.x < 0) touch.x = 0;
                    if (touch.x > max.x) touch.x = max.x;
                    if (touch.y < 0) touch.y = 0;
                    if (touch.y > max.y) touch.y = max.y;
                    mPointerPos.equal(touch);

                    touch = myFunctions.obtenerPuntoMapeado(touch, max);

                    int[] valores = {(int)touch.x,(int)touch.y};
                    controlCallback.onControlMoved(numCanales,Canales,valores);

                    setInnerBounds();
                    break;
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
