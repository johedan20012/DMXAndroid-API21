package kevin20012com.dmx;


import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import kevin20012com.dmx.controles.Control;
import kevin20012com.dmx.controles.Joystick;
import kevin20012com.dmx.controles.MiSwitch;
import kevin20012com.dmx.controles.Panel;
import kevin20012com.dmx.modales.ModalAddControl;

//import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , ModalAddControl.OnInputListener, Control.ControlListener{ //implements Joystick.JoystickListener{
    PendingIntent mPermissionIntent;
    UsbDevice device;
    UsbDevice DMX = null;
    UsbManager manager;
    UsbDeviceConnection deviceConnection;
    BroadcastReceiver mUsbReceiver;

    Vector <Control> controles = new Vector<>(5,3);

    public static FragmentManager fragmentManager;

    public boolean editMode = false;

    int pantallaActual = 0; //0 inicio, 1 controles, 2 luces , 3 escenas , 4 peliculas

    private static final String ACTION_USB_PERMISSION = "com.mobilemerit.usbhost.USB_PERMISSION";

    DrawerLayout drawerLayout;

    private static final String TAG = "MainActivity";

    @Override
    public void sendInput(String input) {
        Log.d(TAG, "sendInput: got the input: " + input);
        ///fixme Agregar el control que mande el input
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.layoutPrincipal);
        if(input.equals("panel") || input.equals("pad")){ //TODO añadimos un panel
            Log.d(TAG,"Panel agregado");
            int[] canales = {33,34};
            Control nuevo =new Panel(MainActivity.this,"250dp","250dp",controles.size(),canales);
            nuevo.setX(constraintLayout.getWidth()/2);
            nuevo.setY(constraintLayout.getHeight()/2);
            if(editMode) nuevo.setEditMode(true);
            controles.add(nuevo);
            constraintLayout.addView(nuevo);
            nuevo = null;

        }else if(input.equals("joy") || input.equals("Joystick")){  //TODO añadimos un joystick
            Log.d(TAG,"Joystick agregado");
            int[] canales = {35,36};
            Control nuevo =new Joystick(MainActivity.this,"250dp","250dp",controles.size(),canales);
            nuevo.setX(constraintLayout.getWidth()/2);
            nuevo.setY(constraintLayout.getHeight()/2);
            if(editMode) nuevo.setEditMode(true);
            controles.add(nuevo);
            constraintLayout.addView(nuevo);
            nuevo = null;
        }else if(input.equals("switch") || input.equals("boton")){ //TODO añadimos un switch
            Log.d(TAG,"Switch añadido");
            int[] canales = {35};
            Control nuevo = new MiSwitch(MainActivity.this,"200dp","200dp",controles.size(),canales);
            nuevo.setX(constraintLayout.getWidth()/2);
            nuevo.setY(constraintLayout.getHeight()/2);
            if(editMode) nuevo.setEditMode(true);
            controles.add(nuevo);
            constraintLayout.addView(nuevo);
            nuevo = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button conectar = (Button) findViewById(R.id.check);
        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInfo();
            }
        });

        fragmentManager = getFragmentManager();
    }

    private void crearLuz(Context context,int tipo,String width,String height,int[] canales,ConstraintLayout pConstraintLayout){
        Control nuevo = (tipo == 0)? new Panel(context,width,height,controles.size(),canales) : new MiSwitch(context,width,height,controles.size(),canales);
        nuevo.setX(pConstraintLayout.getWidth()/2);
        nuevo.setY(pConstraintLayout.getHeight()/2);
        if(editMode) nuevo.setEditMode(true);
        controles.add(nuevo);
        pConstraintLayout.addView(nuevo);
    }
    private void setControlesView(final ConstraintLayout constraintLayout){
        Button boton = new Button(this);
        boton.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        boton.setY(constraintLayout.getHeight()-150);
        boton.setX(constraintLayout.getWidth()-150);
        final Bitmap img1_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.boton_mas),100,100,true);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //FIXME Este boton abrira un modal donde elijes el control a agregar
                //Toast.makeText(MainActivity.this, "Boton", Toast.LENGTH_SHORT).show();
                ModalAddControl modal = new ModalAddControl();
                modal.show(fragmentManager,"ModalControles");
            }
        });
        boton.setBackground(new BitmapDrawable(getResources(), img1_B));

        Button boton1 = new Button(this);
        boton1.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        boton1.setY(constraintLayout.getHeight()-300);
        boton1.setX(constraintLayout.getWidth()-150);
        final Bitmap img2_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.boton_editar),100,100,true);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ///FIXME Este boton iniciaria el modo editar edit esto ya funciona
                Toast.makeText(MainActivity.this, "EditMode:"+!editMode, Toast.LENGTH_SHORT).show();
                editMode = !editMode;
                for(int a=0; a<controles.size(); a++){
                    if(controles.elementAt(a)!=null){
                        controles.elementAt(a).setEditMode(editMode);
                        //float aux = controles.elementAt(a).getX(); //todo fuerzo el redraw de cada view
                        controles.elementAt(a).invalidate();
                    }
                }
                Bitmap img2_B = null;
                if (editMode){
                    img2_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boton_editar2), 100, 100, true);
                }else {
                    img2_B = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.boton_editar), 100, 100, true);
                }
                v.setBackground(new BitmapDrawable(getResources(), img2_B));
            }
        });
        boton1.setBackground(new BitmapDrawable(getResources(), img2_B));

        constraintLayout.addView(boton1);
        constraintLayout.addView(boton);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int title;
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.layoutPrincipal);
        switch (menuItem.getItemId()) {
            case R.id.nav_controles:
                title = R.string.menu_controles;
                if(pantallaActual==1){ drawerLayout.closeDrawer(GravityCompat.START); return false;}
                pantallaActual = 1;
                constraintLayout.removeAllViews();
                setControlesView(constraintLayout);
                break;
            case R.id.nav_luces:
                title = R.string.menu_luces;

                break;
            case R.id.nav_escenas:
                title = R.string.menu_escenas;
                break;
            case R.id.nav_peliculas:
                title = R.string.menu_peliculas;
                break;
            default:
                throw new IllegalArgumentException("menu option not implemented!!");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        Toast toast = Toast.makeText(this, title, Toast.LENGTH_SHORT);
        toast.show();
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onControlMoved(int numCanales,int[] canales,int[] valores){
        boolean errorDMX = false;
        for(int a=0; a<numCanales; a++){
            if(!SenUsbMessage(canales[a],valores[a],true)) errorDMX = true;
        }
        if(errorDMX) checkInfo();
    }

    @Override
    public void onControlDeleted(int indice){
        Log.d(TAG,"Borre el indice "+String.valueOf(indice));
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.layoutPrincipal);
        constraintLayout.removeView(controles.elementAt(indice));
        controles.insertElementAt(null,indice);
        controles.removeElementAt(indice);
    }

    @Override
    public void onControlEdited(int indice,int[] canales,int width,int height){
        controles.elementAt(indice).setCanales(canales);
        //FIXME controles.elementAt(indice).changeSize(width,height);
    }

    private boolean SenUsbMessage(int channel,int value,boolean debugg){
        String b = "";
        b += "Canal:" + channel + " Valor:" + value;
        channel -= 1;
        if(debugg) Log.d(TAG,b);
        //value = (value > 255)? 255 : (value < 0)? 0 : value;
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if(deviceConnection == null || DMX == null){
            return false;
        } // No se escaneo por dmx
        if(!manager.hasPermission(DMX)) return false; //DMX encontrado pero no se otorgaron los permisos
        //String b = "";
        b += "Canal:" + channel + " Valor:" + value;
        channel -= 1;
        if(debugg) Log.d(TAG,b);
        byte[] buffer = new byte[20];
        deviceConnection.controlTransfer(64 | 0 | 128, 1, value, channel, buffer, buffer.length, 5000);
        return true;
    }

    private void checkInfo() {
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        /*
         * this block required if you need to communicate to USB devices it's
         * take permission to device
         * if you want than you can set this to which device you want to communicate
         */
        // ------------------------------------------------------------------
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        // -------------------------------------------------------------------

        HashMap<String , UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        String i = "";
        while (deviceIterator.hasNext()) {
            device = deviceIterator.next();
            if(!manager.hasPermission(device)){ manager.requestPermission(device, mPermissionIntent); return;}
            i += "\n" + "DeviceID: " + device.getDeviceId() + "\n"
                    + "DeviceName: " + device.getDeviceName() + "\n"
                    + "DeviceClass: " + device.getDeviceClass() + " - "
                    + "DeviceSubClass: " + device.getDeviceSubclass() + "\n"
                    + "VendorID: " + device.getVendorId() + "\n"
                    + "ProductID: " + device.getProductId() + "\n";
            if (device.getProductId() == 1500 && device.getVendorId() == 5824){
                DMX = device;
                deviceConnection = manager.openDevice(DMX);
                if (!deviceConnection.claimInterface(DMX.getInterface(0), true)) {
                    //textInfo.setText("Error intenta otra vez");
                    Log.d(TAG,"Error al conectar con el DMX");
                    return;
                }
            }
        }
    }

    public int Myatoi(String str) {
        if (str == null || str.length() < 1)
            return 0;

        // trim white spaces
        str = str.trim();

        char flag = '+';

        // check negative or positive
        int i = 0;
        if (str.charAt(0) == '-') {
            flag = '-';
            i++;
        } else if (str.charAt(0) == '+') {
            i++;
        }
        // use double to store result
        double result = 0;

        // calculate value
        while (str.length() > i && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
            result = result * 10 + (str.charAt(i) - '0');
            i++;
        }

        if (flag == '-')
            result = -result;

        // handle max and min
        if (result > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        if (result < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;

        return (int) result;
    }
}
