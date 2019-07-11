package kevin20012com.dmx;

public class Punto {
    public float x,y;

    public Punto(){
        x = 0;
        y = 0;
    }

    public Punto(int x_1,int y_1){
        x = x_1;
        y = y_1;
    }

    public Punto(float x_1,float y_1){
        x = x_1;
        y = y_1;
    }

    public void equal(Punto p1){
        x = p1.x;
        y = p1.y;
    }

    public float getDistanceToPoint(Punto p2){
        float distance = (float) Math.pow((p2.x - x),2) + (float)Math.pow((p2.y - y),2);
        distance = (float)Math.sqrt(distance);
        return distance;
    }

    public void porEscalar(float escalar){
        x *= escalar;
        y *= escalar;
    }

    public void entreEscalar(float escalar){
        x /= escalar;
        y /= escalar;
    }
}
