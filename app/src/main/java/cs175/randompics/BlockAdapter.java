package cs175.randompics;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

import cs175.randompics.randomPic_model.PicObject;

/**
 * Created by Pei Liu on 16/10/1.
 */
public class BlockAdapter extends BaseAdapter {
    private Context context;
    private List<PicObject> thumbList;

    public BlockAdapter(Context context, List<PicObject> list) {
        this.context = context;
        this.thumbList = list;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView showImage;
        PicObject obj;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        obj = thumbList.get(position);

        // get layout from block.xml
        gridView = inflater.inflate(R.layout.block, null);
        showImage = (ImageView) gridView.findViewById(R.id.showPic);
        if (obj.getState() == 1) {
            showImage.clearColorFilter();
        } else {
            showImage.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
        }
        Picasso.with(context).load(obj.getName()).into(showImage);

        return gridView;
    }

    @Override
    public int getCount() {
        return thumbList.size();
    }

    @Override
    public Object getItem(int position) {
        return thumbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}