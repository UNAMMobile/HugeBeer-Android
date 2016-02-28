package com.chelathon.unammobile.hugebeer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Safe on 28/02/2016.
 */
public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<String> names;

    //Constructor to initialize values
    public GridAdapter(Context context,List<String> names) {

        this.context  = context;
        this.names=names;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return names.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        // LayoutInflator to call external grid_item.xml file
        ImageView circle;
        TextView text;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        gridView = new View(context);

            // get layout from grid_item.xml ( Defined Below )
        gridView = inflater.inflate( R.layout.grid_item , null);
        circle=(ImageView) gridView.findViewById(R.id.grid_item_image);
        text=(TextView) gridView.findViewById(R.id.grid_item_text);

        text.setText(names.get(position));

        //circle.setScaleX(sizes.get(position));
        //circle.setScaleY(sizes.get(position));


        return gridView;
    }
}
