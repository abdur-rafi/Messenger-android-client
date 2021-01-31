package com.example.messenger.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.messenger.R;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;
import java.util.List;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;

public class MessageAdapter extends ArrayAdapter<MessageAndroid> {

    public MessageAdapter(Context context, List<MessageAndroid> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitemsender, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.messageText);
        TextView textViewAsh = convertView.findViewById(R.id.messageTextAsh);
        TextView name_text = convertView.findViewById(R.id.name_text);
        TextView bottom_text = convertView.findViewById(R.id.bottom_of_name_text);
        LinearLayout layout = convertView.findViewById(R.id.linearText);
        RelativeLayout relative = convertView.findViewById(R.id.relative);
        float d = Resources.getSystem().getDisplayMetrics().density;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins((int) (70 * d), 0, (int) (10 * d), 0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.setMargins((int) (10 * d), (int) (0 * d), (int) (70 * d), (int) (0 * d));
        lp2.addRule(RelativeLayout.RIGHT_OF,R.id.imageLeft);
        ImageView left = convertView.findViewById(R.id.imageLeft);
        MessageAndroid item = getItem(position);
        textView.setText(item.getMessage());
        textViewAsh.setText(item.getMessage());
        if(item.getSenderIndex() == -1){
            left.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0,(int)(50*d),0,0);
            left.getLayoutParams().width =(int) (100 * d);
            left.getLayoutParams().height =(int) (100 * d);
            left.setLayoutParams(params);
            GroupMessageAndroid grp = Binders.instance.getPerson().getGroups()
                    .get(Binders.instance.activeIndex);
            left.setImageBitmap(Binders.instance.map.get(
                    grp.fileIndex));
//            RelativeLayout.LayoutParams params_for_name_text = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//            params_for_name_text.addRule(RelativeLayout.BELOW,R.id.imageLeft);
//            name_text.setLayoutParams(params_for_name_text);
            name_text.setText(grp.getGroupName());
//            name_text.setGravity(Gravity.CENTER);
//            name_text.setVisibility(View.VISIBLE);
//            RelativeLayout.LayoutParams paramas_bottom = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//            paramas_bottom.addRule(RelativeLayout.BELOW,R.id.name_text);
//            bottom_text.setGravity(Gravity.CENTER);
            bottom_text.setText("You can now chat with " + grp.getGroupName());
//            bottom_text.setLayoutParams(paramas_bottom);
//            bottom_text.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            textViewAsh.setVisibility(View.GONE);
            return convertView;
        }
        RelativeLayout.LayoutParams image_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        image_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        image_params.setMargins((int)(10*d),0,0,0);
        left.setLayoutParams(image_params);
        left.getLayoutParams().height =(int) (35 * d);
        left.getLayoutParams().width = (int) ( 35 * d);
        layout.setVisibility(View.GONE);
//        name_text.setVisibility(View.GONE);
//        bottom_text.setVisibility(View.GONE);
        if (item.getSenderIndex() == Binders.instance.getPerson().getParticipationIndex()
                .get(Binders.instance.activeIndex)) {
            textView.setLayoutParams(lp);
            textView.setVisibility(View.VISIBLE);
            textViewAsh.setVisibility(View.GONE);
            left.setVisibility(View.GONE );
            if(item.isFirst()){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int)(15*d);
                relative.setLayoutParams(params);
            }
            else{
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int)(3*d);
                relative.setLayoutParams(params);

            }
//            left.requestLayout();

        } else {
            textViewAsh.setLayoutParams(lp2);
            textViewAsh.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            if (item.isFirst()) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int)(15*d);
                relative.setLayoutParams(params);
                left.getLayoutParams().height = (int) ( 35*d);
                left.setVisibility(View.VISIBLE);
                int j = item.getSenderIndex();
                j = Binders.instance.getPerson().getGroups().get(Binders.instance.activeIndex)
                        .getParticipants().get(j).fileId;
//                if(Binders.instance.map.containsKey(j)){
//                    left.setImageBitmap(Binders.instance.map.get(j));
//                }
//                else{
//                    File path = getContext().getCacheDir();
//                    File file = new File(path, "messenger_" + j + Constants.format);
//                    if (!file.exists()){
//                        System.out.println("================ messenger_"+j+Constants.format+" doesn't exist ================");
//                    }
//                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    left.setImageBitmap(bitmap);
//                    Binders.instance.map.put(j,bitmap);
//                }
                left.setImageBitmap(Binders.instance.map.get(j));
            }
            else {
                left.getLayoutParams().height = (int)(10*d);
                left.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = (int)(3*d);
                relative.setLayoutParams(params);
            }
        }
        return convertView;
    }

}
