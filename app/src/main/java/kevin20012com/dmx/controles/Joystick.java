package kevin20012com.dmx.controles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.ViewParent;

import kevin20012com.dmx.Punto;
import kevin20012com.dmx.R;


public class Joystick extends Control{
    private Punto mInnerCenter;

    private boolean mPressedState = false;

    private void setupDimensions(Context context){
        final Resources res = context.getResources();

        float innerScale = 1.83f;
        Rect rectOuter,rectInner;

        final Bitmap img1_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.joystick_1),mWidth,mHeight,true);
        final Bitmap img2_B = BitmapFactory.decodeResource(res,R.drawable.joystick_2);
        final Bitmap img3_B = BitmapFactory.decodeResource(res,R.drawable.joystick_3);
        final Bitmap img4_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.control_modo_edit),mWidth,mHeight,true);

        rectOuter = new Rect(0, 0, mWidth, mHeight);
        rectInner = new Rect(0, 0, (int) (mWidth / innerScale), (int) (mHeight / innerScale));

        SetEditModeBitmap(new BitmapDrawable(res,img4_B));
        SetBitmap(new BitmapDrawable(res, img1_B),0);
        SetBitmap(new BitmapDrawable(res, img2_B),1);
        SetBitmap(new BitmapDrawable(res, img3_B),2);
        //mBoundsBoxBitmap = new BitmapDrawable(res, img1_B);

        SetEditModeBitmapBounds(rectOuter);
        SetBitmapBounds(rectOuter,0);
        SetBitmapBounds(rectInner,1);
        SetBitmapBounds(rectInner,2);

        SetBitmapActive(true,0);
        SetBitmapActive(true,1);

        mInnerCenter = new Punto(mWidth / 2, mHeight / 2);
        SetInnerBounds();
    }

    /*public Joystick(Context context,AttributeSet attributes){
        super(context);
        String vWidth =  attributes.getAttributeValue("http://schemas.android.com/apk/res/android","layout_width");
        String vHeight =  attributes.getAttributeValue("http://schemas.android.com/apk/res/android","layout_height");

        setupDimensions(context,vWidth,vHeight);

        if(context instanceof JoystickListener){
            joystickCallback = (JoystickListener) context;
        }
    }*/

    public Joystick(Context context,String width,String height,int indice,int[] canales){
        super(context,2,canales,width,height);
        setupDimensions(context);

        mIndice = indice;
    }

    private void SetInnerBounds()
    {
        int width = GetBitmapBounds(1).width() / 2;
        int height = GetBitmapBounds(1).height() / 2;
        SetBitmapBounds(new Rect((int) mInnerCenter.x - width, (int)mInnerCenter.y - height, (int)mInnerCenter.x + width, (int)mInnerCenter.y + height),1);
        SetBitmapBounds(GetBitmapBounds(1),2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getEditMode()){
            if(HandleEditModeTouch(event)) return true;
        }else {
            int[] valores = {125,125};
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    SetBitmapActive(true,1);
                    SetBitmapActive(false,2);

                    mInnerCenter.x = mWidth / 2;
                    mInnerCenter.y = mHeight / 2;

                    SetInnerBounds();

                    controlCallback.onControlMoved(2,Canales,valores);
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    SetBitmapActive(false,1);
                    SetBitmapActive(true,2);

                    Punto max = new Punto();
                    max.y = GetBitmapBounds(0).bottom;
                    max.x = GetBitmapBounds(0).right;

                    max.x -= GetBitmapBounds(0).centerX();
                    max.x -= GetBitmapBounds(1).width() / 2;

                    max.y -= GetBitmapBounds(0).centerY();
                    max.y -= GetBitmapBounds(1).height() / 2;

                    Punto newAxies = myFunctions.obtainFixedPoint(new Punto(mWidth / 2, mHeight / 2), new Punto(event.getX(), event.getY()), max);

                    mInnerCenter = newAxies;

                    SetInnerBounds();
                    float aux = mInnerCenter.x - (GetBitmapBounds(0).centerX() - max.x);
                    aux *= 255;
                    aux /= (2 * max.x);
                    int fixedCenterX = (int) aux;

                    aux = mInnerCenter.y - (GetBitmapBounds(0).centerY() - max.y);
                    aux *= 255;
                    aux /= (2 * max.y);
                    int fixedCenterY = (int) aux;
                    valores[0] = fixedCenterX;
                    valores[1] = fixedCenterY;
                    controlCallback.onControlMoved(2,Canales,valores);
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


