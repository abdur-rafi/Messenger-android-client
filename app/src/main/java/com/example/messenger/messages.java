package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.messenger.Adapters.MessageAdapter;
import com.example.messenger.Adapters.fragmentAdapterMessage;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;

public class messages extends AppCompatActivity {

    ListView listView;
    EditText messageField;
    public static Handler handler = null;

    NavigationView nav_view;
    DrawerLayout drawer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        toolbar.setPadding(0,0,0,0);
        ImageView imageView = findViewById(R.id.toolbar_image);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        nav_view = findViewById(R.id.message_nav);
        nav_view.getLayoutParams().width = width;
        drawer = findViewById(R.id.message_drawer_layout);
        ViewPager2 viewPager2 = nav_view.findViewById(R.id.message_nav_viewPager);
        viewPager2.setUserInputEnabled(false);
        fragmentAdapterMessage adapterMessage = new fragmentAdapterMessage(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapterMessage);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,GravityCompat.END);
        GroupMessageAndroid grp = Binders.instance.getPerson().getGroups()
                .get(Binders.instance.activeIndex);
        if(grp.isAddAble())
            viewPager2.setCurrentItem(1);
        else
            viewPager2.setCurrentItem(0);
        int j = Binders.instance.activeIndex;
        j = Binders.instance.getPerson().getGroups().get(j).fileIndex;
        if (Binders.instance.map.containsKey(j)) {
            imageView.setImageBitmap(Binders.instance.map.get(j));
        } else {
            File path = this.getCacheDir();
            File file = new File(path, "messenger_" + j + Constants.format);
            if (!file.exists()) System.out.println("================ messenger_" + j
                    + Constants.format + " doesn't exist ================");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
            Binders.instance.map.put(j, bitmap);
        }
        listView = findViewById(R.id.message_list);
        ArrayList<MessageAndroid> list = Binders.instance.getPerson().getGroups().get(Binders.instance.activeIndex).getGroupMessage();
        MessageAdapter adapter = new MessageAdapter(this, list);
        listView.setAdapter(adapter);
        Context context = this;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == Constants.ADD_MESSAGE) {
                    adapter.notifyDataSetChanged();
                    listView.post(() -> {
                        listView.setSelection(listView.getCount() - 1);
                    });
                }
                super.handleMessage(msg);
            }
        };
        TextView textView = findViewById(R.id.name);
        textView.setText(Binders.instance.getPerson().getGroups().get(Binders.instance.activeIndex).getGroupName());
        messageField = findViewById(R.id.messageField);
        ImageButton button = findViewById(R.id.sendMessage);
        button.setOnClickListener(view -> {
            String message = messageField.getText().toString().trim();
            if (message.equals("")) return;
            int id = Binders.instance.activeIndex;
            int senderId = Binders.instance.getPerson().getParticipationIndex().get(id);
            int dbIndex = Binders.instance.getPerson().getGroups().get(id).getDatabaseIndex();
            new Thread(() -> {
                boolean first = true;
                int size = Binders.instance.getPerson().getGroups().get(id).getGroupMessage().size();
                if (size != 0) {
                    first = Binders.instance.getPerson().getGroups().get(id)
                            .getGroupMessage().get(size - 1).getSenderIndex() != senderId;
                }
                MessageAndroid message1 = new MessageAndroid(message, senderId, Binders.instance.getPerson().getMyAccount().getPort()
                        , null, null, first);
                ArrayList<MessageAndroid> arr = new ArrayList<>();
                arr.add(message1);
                transferPackageAndroid transferPackage = new transferPackageAndroid("message", null, arr,
                        dbIndex, -1, null, null);
                Transmit.transmitPackage(transferPackage);
            }).start();
            messageField.setText("");
        });
        imageView.setOnClickListener(view -> drawer.openDrawer(GravityCompat.END));

        Message message = Message.obtain();
        message.what = Constants.REDO_SELECTED_ITEM;
        contact.handler.sendMessage(message);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.END)){
            if(Binders.instance.drawer != null && Binders.instance.drawer.isDrawerOpen(GravityCompat.END)){
                Binders.instance.drawer.closeDrawer(GravityCompat.END);
                return;
            }
            drawer.closeDrawer(GravityCompat.END);
            return;
        }
//        Binders.instance.background.setBackgroundColor(Color.parseColor("#ffffff"));
//        Binders.instance.unseen.setText("0");
//        Binders.instance.unseen.setVisibility(View.INVISIBLE);

        int i = Binders.instance.activeIndex;
        Binders.instance.getPerson().getUnseenCount().set(i,0);
        Binders.instance.getPerson().getGroups().get(i).unseenCount = 0;
        Binders.instance.activeIndex = -1;
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu,menu);
        return true;
    }
}