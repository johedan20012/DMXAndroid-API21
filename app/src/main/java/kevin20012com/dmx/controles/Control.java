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
import android.view.View;
import android.widget.RelativeLayout;

import kevin20012com.dmx.Functions;
import kevin20012com.dmx.MainActivity;
import kevin20012com.dmx.Punto;
import kevin20012com.dmx.R;
import kevin20012com.dmx.modales.ModalAddControl;
import kevin20012com.dmx.modales.ModalEditControl;

public class Control extends View {
    private static final String TAG = "CONTROL";

    protected int mWidth;
    protected int mHeight;

    private BitmapDrawable[] mBitmaps = new BitmapDrawable[4];
    private boolean[] mBitmapActive = new boolean[4];

    protected int[] Canales = {0,0,0};
    protected int numCanales = 0;

    private boolean onEditMode = false;
    protected int mIndice = -1;

    protected Control.ControlListener controlCallback;

    private BitmapDrawable mEditMaskBitmap;
    private BitmapDrawable mWHTFThisDontWork;

    Functions myFunctions;

    protected void SetBitmapActive(boolean active,int index) {mBitmapActive[index] = active;}

    protected void SetEditModeBitmap(BitmapDrawable bitmap){
        mEditMaskBitmap = bitmap;
    }

    protected  void SetEditModeBitmapBounds(Rect bounds){
        mEditMaskBitmap.setBounds(bounds);
    }

    protected void SetBitmap(BitmapDrawable bitmap,int index) {mBitmaps[index] = bitmap; if(index == 0) mWHTFThisDontWork = bitmap;}

    protected BitmapDrawable GetBitmap(int index){return mBitmaps[index];}

    protected void SetBitmapBounds(Rect bounds, int index){
        mBitmaps[index].setBounds(bounds);
        if(index == 0)mWHTFThisDontWork.setBounds(bounds);
    }

    protected Rect GetBitmapBounds(int index){return mBitmaps[index].getBounds();}

    public void setCanales(int[] canales){
        Canales = canales;
    }

    public void setEditMode(boolean mode){
        onEditMode = mode;
    }

    public boolean getEditMode(){
        return onEditMode;
    }

    public interface ControlListener{
        void onControlDeleted(int indice);
        void onControlMoved(int numCanales,int[] canales,int[] valores);
        void onControlEdited(int indice,int[] canales,int witdh,int height);
    }

    protected Control(Context context,int nCanales ,int[] valores,String pWidth,String pHeight){
        super(context);
        numCanales = nCanales;
        Canales = valores;
        if(context instanceof Control.ControlListener){
            controlCallback = (Control.ControlListener) context;
        }
        myFunctions = new Functions();

        mWidth = (pWidth != null) ? myFunctions.MyAToI(pWidth) : 0;
        mHeight = (pHeight != null) ? myFunctions.MyAToI(pHeight) : 0;
    }

    protected boolean HandleEditModeTouch(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Punto max = new Punto();
                max.x = 0.3125f * getWidth();
                max.y = 0.3125f * getHeight();
                if(event.getX() > 0 && event.getX() <= max.x && event.getY() > 0 && event.getY() <= max.y){
                    controlCallback.onControlDeleted(mIndice);
                    return true;
                }

                if(event.getX() <= getWidth() && event.getX() >= (getWidth() - max.x) && event.getY() >= 0 && event.getY() <= max.y){
                    ModalEditControl modal = new ModalEditControl();
                    modal.Inicializar(controlCallback,Canales,mIndice,mWidth,mHeight);
                    modal.show(MainActivity.fragmentManager,"ModalEditControles");
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                Punto evento = new Punto();
                evento.x = event.getX() + getX() - mEditMaskBitmap.getBounds().width() / 2;
                evento.y = event.getY() + getY() - mEditMaskBitmap.getBounds().height() / 2;

                setX(evento.x);
                setY(evento.y);
                return false;
        }
        return false;
    }

    public void changeSize(int width,int height){
        Log.d(TAG,"Width:"+width);
        Log.d(TAG,"Height:"+height);
        for(int a=0; a<mBitmaps.length; a++){
            if(mBitmaps[a]!=null){ ///Escalar el bitmap
                Rect bounds = mBitmaps[a].getBounds();
                Log.d(TAG,"Left:"+bounds.left);
                Log.d(TAG,"Right:"+bounds.right);
                Log.d(TAG,"Top:"+bounds.top);
                Log.d(TAG,"Bottom:"+bounds.bottom);
                bounds.left = (width*bounds.left) / mWidth;
                bounds.top = (height*bounds.top) / mHeight;
                bounds.bottom = (height*bounds.bottom) / mHeight;
                bounds.right = (width*bounds.right) / mWidth;
                Log.d(TAG,"Left:"+bounds.left);
                Log.d(TAG,"Right:"+bounds.right);
                Log.d(TAG,"Top:"+bounds.top);
                Log.d(TAG,"Bottom:"+bounds.bottom);
                //mBitmaps[a].setBounds(0,0,width,height);
            }
        }

        mEditMaskBitmap.setBounds(0,0,width,height);
        mWHTFThisDontWork.setBounds(0,0,width,height);
        mWidth = width;
        mHeight = height;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas){
        ///Log.d(TAG,"OnDraw");
        for(int a=0; a<mBitmaps.length; a++){
            if(mBitmaps[a]!= null && mBitmapActive[a]){ mBitmaps[a].draw(canvas);}
        }
        //if(mWHTFThisDontWork != null) mWHTFThisDontWork.draw(canvas);
        if(mEditMaskBitmap != null && onEditMode){ mEditMaskBitmap.draw(canvas);}else{ if(onEditMode) Log.d(TAG,"No dibuje edit");}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Log.d(TAG,"onMeasure");
        setMeasuredDimension(mWidth, mHeight);
    }
}
