package kevin20012com.dmx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;

public class Functions {
    public Bitmap resizeBitmap(Context context, Bitmap bitmap, float scale)
    {
        // Determine the button size based on the smaller screen dimension.
        // This makes sure the buttons are the same size in both portrait and landscape.
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int minDimension = Math.min(dm.widthPixels, dm.heightPixels);

        return Bitmap.createScaledBitmap(bitmap,
                (int) (minDimension * scale),
                (int) (minDimension * scale),
                true);
    }

    public Punto obtainFixedPoint(Punto center,Punto touch,Punto max){
        //abreviacion C = center , T = touch
        float disCT= center.getDistanceToPoint(touch);
        if(disCT > max.x){
            float ratio = max.x / disCT;
            touch.x = center.x + (touch.x - center.x) * ratio;
            touch.y = center.y + (touch.y - center.y) * ratio;
        }
        //Log.d("touch x",Float.toString(touch.x));
        //Log.d("touch y",Float.toString(touch.y));
        return touch;
    }

    public Punto obtenerPuntoMapeado(Punto punto,Punto limite){ //va de 0 -limX y 0 - limY , punto debe de estar entre  y lim , devuelve de 0 -255
        Punto regreso = punto;
        regreso.porEscalar(255.0f);
        regreso.x /= limite.x;
        regreso.y /= limite.y;
        return regreso;
    }

    public int MyAToI(String pCadena){
        int vValor = 0, vAux;
        //Log.d("Functions/MYATOI/pCADEN",pCadena);
        for(int i = 0; i < pCadena.length(); i++){
            vAux = pCadena.charAt(i) - '0';
            //Log.d("Functions/MyATOI/vAUx", Character.toString(pCadena.charAt(i)));
            if(vAux >= 0 && vAux <= 9){
                vValor *= 10;
                vValor += vAux;
            }else{
                break;
            }
        }
        return vValor;
    }
}
