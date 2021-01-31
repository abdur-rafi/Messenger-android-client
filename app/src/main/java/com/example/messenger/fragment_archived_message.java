package com.example.messenger;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.Adapters.sortedAdapter;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import sample.Binders;
import sample.Constants;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;

public class fragment_archived_message extends Fragment {
    public static Handler handler = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archived_messages, container, false);
        ImageView toolbar_image = view.findViewById(R.id.toolbar_image);
        TextView name = view.findViewById(R.id.name);
        toolbar_image.setVisibility(View.GONE);
        name.setText("Archived Messages");
        RecyclerView recyclerView = view.findViewById(R.id.archived_recycler_view);
        sortedAdapter adapter = new sortedAdapter(Binders.instance.archivedList, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Context context = view.getContext();
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            GroupMessageAndroid grp;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                GroupMessageAndroid grp = adapter.removeItem(pos);
                Binders.instance.archivedMessageSet.remove(grp.getDatabaseIndex());
                Message message = Message.obtain();
                message.what = Constants.REMOVE_FROM_ARCHIVED;
                message.obj = grp;
                contact.handler.sendMessage(message);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.Ash))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_unarchive_24)
                        .addSwipeLeftLabel("Unarchive")
                        .create()
                        .decorate();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == Constants.ADD_TO_ARCHIVED) {
                    GroupMessageAndroid grp = (GroupMessageAndroid) msg.obj;
                    adapter.addItem(grp);
                } else if (msg.what == Constants.REMOVE_FROM_ARCHIVED) {
                    GroupMessageAndroid grp = (GroupMessageAndroid) msg.obj;
                    adapter.removeItem(grp);
                } else if (msg.what == Constants.ADD_MESSAGE_TO_ARCHIVE) {
                    GroupMessageAndroid grp = (GroupMessageAndroid) msg.obj;
                    adapter.changeItem(grp);
                }
            }
        };
        return view;
    }
}
