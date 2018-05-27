package corp.nminhanh.placesaver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Minh Anh on 1/10/2018.
 */

public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(@NonNull Context context, ArrayList<Item> list) {
        super(context, 0, list);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View currentView = convertView;
        Item currentItem = getItem(position);

        if (currentView == null) {
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
            holder.nameTextView = currentView.findViewById(R.id.item_name);
            holder.imageView = currentView.findViewById(R.id.item_image);
            holder.descriptionTextView = currentView.findViewById(R.id.item_description);
            currentView.setTag(holder);
        } else {
            holder = (ViewHolder) currentView.getTag();
        }
        holder.nameTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());

        GlideApp.with(getContext()).load(currentItem.getPath()).centerCrop().into(holder.imageView);
        return currentView;
    }
}
