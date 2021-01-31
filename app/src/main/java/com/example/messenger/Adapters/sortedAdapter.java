package com.example.messenger.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.example.messenger.R;
import com.example.messenger.fragment_archived_message;
import com.example.messenger.messages;

import java.io.File;
import java.util.ArrayList;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;

public class sortedAdapter extends RecyclerView.Adapter<sortedAdapter.GroupMessageAndroidHolder> implements Filterable {

    private ArrayList<GroupMessageAndroid> arrayList;
    private SortedList<GroupMessageAndroid> sList;
    private Context context;
    private LinearLayout background_to_be_changed;
    private TextView unseen_to_be_seen;

    public sortedAdapter(ArrayList<GroupMessageAndroid> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sList = new SortedList<>(GroupMessageAndroid.class, new SortedListAdapterCallback<GroupMessageAndroid>(this) {
            @Override
            public int compare(GroupMessageAndroid o1, GroupMessageAndroid o2) {
                if (o1.newCount < o2.newCount) return 1;
                else if (o1.newCount > o2.newCount) return -1;
                else return o1.getGroupName().compareTo(o2.getGroupName());
            }

            @Override
            public boolean areContentsTheSame(GroupMessageAndroid oldItem, GroupMessageAndroid newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(GroupMessageAndroid item1, GroupMessageAndroid item2) {
                return item1.newCount == item2.newCount;
            }
        });
        sList.addAll(arrayList);
    }

    @NonNull
    @Override
    public GroupMessageAndroidHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitemcontact, parent, false);
        GroupMessageAndroidHolder holder = new GroupMessageAndroidHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMessageAndroidHolder holder, int position) {
        GroupMessageAndroid item = sList.get(position);
        String sender = item.getGroupName();
        if (item.getGroupMessage().size() > 0 && item.getGroupMessage().get(item.getGroupMessage().size() - 1).getSenderIndex() != -1) {
            int j = item.getGroupMessage().get(item.getGroupMessage().size() - 1).getSenderIndex();
            AccountAndroid acc = item.getParticipants().get(j);
            if (acc.getPort() == Binders.instance.getPerson().getMyAccount().getPort())
                sender = "You";
            else sender = acc.getName();
            String mess = item.getGroupMessage().get(item.getGroupMessage().size() - 1).getMessage();
            holder.prev_message.setText(sender + " : " + mess);
        } else {
            holder.prev_message.setText("you can now chat with " + sender);
        }
        holder.name.setText(item.getGroupName());
        holder.unseen.setText(Integer.toString(item.unseenCount));
        if (item.unseenCount == 0) {
            holder.unseen.setVisibility(View.INVISIBLE);
        } else {
            holder.unseen.setVisibility(View.VISIBLE);

        }
        if (!Binders.instance.map.containsKey(item.fileIndex)) {
            File path = context.getCacheDir();
            File file = new File(path, "messenger_" + item.fileIndex + Constants.format);
            if (!file.exists()) System.out.println("================ messenger_" + item.fileIndex
                    + Constants.format + " doesn't exist ================");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Binders.instance.map.put(item.fileIndex, bitmap);
        }
        holder.imageView.setImageBitmap(Binders.instance.map.get(item.fileIndex));

    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<GroupMessageAndroid> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(arrayList);
            }
            else {
                String pattern = charSequence.toString().toLowerCase();
                for(GroupMessageAndroid groups:arrayList){
                    if(groups.getGroupName().toLowerCase().contains(pattern)) filteredList.add(groups);
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            return null;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sList.clear();
            if(filterResults == null || filterResults.values == null)
                sList.addAll(arrayList);
            else
                sList.addAll((ArrayList<GroupMessageAndroid>)filterResults.values);
        }

    };

    public class GroupMessageAndroidHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView prev_message;
        private LinearLayout background;
        private TextView unseen;

        public GroupMessageAndroidHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.contact_image);
            name = itemView.findViewById(R.id.contact_name);
            prev_message = itemView.findViewById(R.id.contact_last_message);
            background = itemView.findViewById(R.id.background);
            unseen = itemView.findViewById(R.id.unseen_count);
            itemView.setOnClickListener(view -> {
                if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
                Binders.instance.activeIndex = Binders.instance.getPerson().getGroups().indexOf(sList.get(getAdapterPosition()));
                background.setBackgroundColor(Color.parseColor("#e0e0e0"));
                Intent intent = new Intent(view.getContext(), messages.class);
                background_to_be_changed = background;
                unseen_to_be_seen = unseen;
                view.getContext().startActivity(intent);
            });

        }
    }

    public void redoClickedItem(){
        background_to_be_changed.setBackgroundColor(context.getResources().getColor(R.color.Ash));
        unseen_to_be_seen.setText("0");
        unseen_to_be_seen.setVisibility(View.INVISIBLE);
    }

    public synchronized GroupMessageAndroid archiveItem(GroupMessageAndroid grp){
        Message message = Message.obtain();
        message.what = Constants.ADD_TO_ARCHIVED;
        message.obj = grp;
        if(fragment_archived_message.handler != null){
            fragment_archived_message.handler.sendMessage(message);
        } else Binders.instance.archivedList.add(grp);
        return grp;
    }

    public synchronized void addItem() {
        GroupMessageAndroid item = Binders.instance.getPerson().getGroups().get(Binders.instance.getPerson().getGroups().size() - 1);
        sList.add(item);
        arrayList.add(item);
    }

    public synchronized void addItem(GroupMessageAndroid grp) {
        sList.add(grp);
        arrayList.add(grp);
    }


    public synchronized boolean changeItem(GroupMessageAndroid message) {
        int id = sList.indexOf(message);
        if (id == -1) {
            return false;
        }
        System.out.println("================= changing Item =================");
        System.out.println("index                 : " + id);
        message.newCount = Binders.instance.getPerson().messageCount;
        sList.updateItemAt(id, message);
        return true;
    }

    public synchronized GroupMessageAndroid removeItem(int pos){
        GroupMessageAndroid grp = sList.get(pos);
        sList.removeItemAt(pos);
        arrayList.remove(grp);
        return  grp;
    }
    public synchronized void removeItem(GroupMessageAndroid grp){
        sList.remove(grp);
        arrayList.remove(grp);
    }
    public synchronized GroupMessageAndroid unArchiveItem(GroupMessageAndroid grp){
        Message message = Message.obtain();
        message.what = Constants.REMOVE_FROM_ARCHIVED;
        message.obj = grp;
        if(fragment_archived_message.handler != null){
            fragment_archived_message.handler.sendMessage(message);
        } else Binders.instance.archivedList.remove(grp);
        return grp;
    }
}
