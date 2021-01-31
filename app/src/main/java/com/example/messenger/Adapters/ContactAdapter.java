package com.example.messenger.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messenger.R;

import java.io.File;
import java.util.List;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;

public class ContactAdapter extends ArrayAdapter<AccountAndroid> {

    public ContactAdapter(Context context, List<AccountAndroid> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemcontact, parent, false);
        }
        AccountAndroid item = getItem(position);
        ImageView personImage = convertView.findViewById(R.id.contact_image);
        TextView personName = convertView.findViewById(R.id.contact_name);
        TextView lastMessage = convertView.findViewById(R.id.contact_last_message);
        TextView count = convertView.findViewById(R.id.unseen_count);
        count.setVisibility(View.GONE);
        if(!Binders.instance.map.containsKey(item.fileId)){
            File path = getContext().getCacheDir();
            File file = new File(path, "messenger_" + item.fileId +Constants.format);
            if (!file.exists()) System.out.println("================ messenger_" + item.fileId
                    + Constants.format + " doesn't exist ================");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Binders.instance.map.put(item.fileId, bitmap);
        }
        personImage.setImageBitmap(Binders.instance.map.get(item.fileId));
        personName.setText(item.getName());
        lastMessage.setText("online");
        return convertView;
    }

}
