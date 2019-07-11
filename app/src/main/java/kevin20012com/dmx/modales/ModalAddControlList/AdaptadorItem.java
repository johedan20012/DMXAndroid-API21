package kevin20012com.dmx.modales.ModalAddControlList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kevin20012com.dmx.R;

public class AdaptadorItem extends BaseAdapter {
    private Context context;
    private ArrayList<Item> listItems;

    public AdaptadorItem(Context context, ArrayList<Item> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.dialog_controles_item, null);
        ImageView imgFoto = convertView.findViewById(R.id.imgControl);
        TextView nameControl = convertView.findViewById(R.id.nameControl);
        TextView descControl = convertView.findViewById(R.id.descControl);

        imgFoto.setImageResource(item.getImgFoto());
        nameControl.setText(item.getTitulo());
        descControl.setText(item.getDescripcion());

        return convertView;
    }
}
