package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.Adapters.fragmentAdapter;
import com.example.messenger.Adapters.sortedAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import sample.Binders;
import sample.Constants;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;

public class contact extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private DrawerLayout drawer;
    public static Handler handler;
    private ImageView nev_image;
    int setItem = -1;
    private sortedAdapter adapter;
    private NavigationView navView;
    private NavigationView navView2;
    private Toolbar toolbar;
    private ImageView toolbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        setLeftNavigationView();

        setToolbarMenu();

        setRightNavigationView();

        setDrawer();

        setRecycleView();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == Constants.ADD_CONTACT) {
                    adapter.addItem();
                } else if (msg.what == Constants.CHANGE_CURRENT_IMAGE) {
                    nev_image.setImageBitmap(Binders.instance.map.get(Binders.instance.getPerson().getMyAccount().fileId));
                    toolbarImage.setImageBitmap(Binders.instance.map.get(Binders.instance.getPerson().getMyAccount().fileId));
                } else if (msg.what == Constants.ADD_MESSAGE) {
                    GroupMessageAndroid grp = (GroupMessageAndroid) msg.obj;
                    if(!adapter.changeItem(grp)){
                        Message message = Message.obtain();
                        message.what = Constants.ADD_MESSAGE_TO_ARCHIVE;
                        message.obj = grp;
                        if(fragment_archived_message.handler != null){
                            fragment_archived_message.handler.sendMessage(message);
                        }
                    }
                } else if (msg.what == Constants.CHANGE_IMAGE) {
                    adapter.notifyDataSetChanged();
                }
                else if(msg.what == Constants.REMOVE_FROM_ARCHIVED){
                    GroupMessageAndroid grp = (GroupMessageAndroid)  msg.obj;
                    adapter.addItem(grp);
                }
                else if(msg.what == Constants.REDO_SELECTED_ITEM){
                    adapter.redoClickedItem();
                }

                super.handleMessage(msg);
            }
        };

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView search = (SearchView) searchItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    private void setLeftNavigationView(){
        navView = findViewById(R.id.nav_view);
        View hview = navView.getHeaderView(0);
        nev_image = hview.findViewById(R.id.nev_header_image);
        TextView name = hview.findViewById(R.id.nev_header_title);
        ImageView settings = hview.findViewById(R.id.nav_header_settings);
        int id = Binders.instance.getPerson().getMyAccount().fileId;
        if (!Binders.instance.map.containsKey(id)) {
            File path = getCacheDir();
            File file = new File(path, "messenger_" + id + Constants.format);
            if (!file.exists()) System.out.println("================ messenger_" + id
                    + Constants.format + " doesn't exist ================");
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            Binders.instance.map.put(id, bitmap);
        }
        nev_image.setImageBitmap(Binders.instance.map.get(id));
        name.setText(Binders.instance.getPerson().getMyAccount().getName());
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.addContact) {
                viewPager2.setCurrentItem(0);
                drawer.closeDrawer(GravityCompat.START);
                setItem = 0;
            } else if (item.getItemId() == R.id.exit) {
                transferPackageAndroid packet = new transferPackageAndroid("exit", null,
                        null, Binders.instance.getPerson().getMyAccount().getDatabaseIndex(), -1, Binders.instance.getPerson().getUnseenCount(),
                        Binders.instance.getPerson().getNewCount(),Binders.instance.archivedMessageSet);

                new Thread(() -> {
                    Transmit.transmitPackage(packet);
                    finishAffinity();
                }).start();
            } else if (item.getItemId() == R.id.newGroup) {
                setItem = 0;
                viewPager2.setCurrentItem(2);
                drawer.closeDrawer(GravityCompat.START);
            }
            else if(item.getItemId() == R.id.log_out){
                transferPackageAndroid packet = new transferPackageAndroid("exit", null,
                        null, Binders.instance.getPerson().getMyAccount().getDatabaseIndex(), -1, Binders.instance.getPerson().getUnseenCount(),
                        Binders.instance.getPerson().getNewCount(),Binders.instance.archivedMessageSet);

                new Thread(() -> Transmit.transmitPackage(packet)).start();
                Intent intent = new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else if(item.getItemId() == R.id.archived_messages){
                viewPager2.setCurrentItem(3);
                drawer.closeDrawer(GravityCompat.START);
                setItem = 0;
            }
            return false;
        });
        settings.setOnClickListener((view -> {
            setItem = 0;
            viewPager2.setCurrentItem(1);
            drawer.closeDrawer(GravityCompat.START);
        }));
    }

    private void setToolbarMenu(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0,0,0,0);
        toolbarImage = findViewById(R.id.toolbar_image);
        toolbarImage.setVisibility(View.GONE);
        TextView textView = findViewById(R.id.name);
        textView.setVisibility(View.GONE);

        int id = Binders.instance.getPerson().getMyAccount().fileId;
        toolbarImage.setImageBitmap(Binders.instance.map.get(id));
    }

    private void setRightNavigationView(){
        navView2 = findViewById(R.id.nav_view2);
        viewPager2 = navView2.findViewById(R.id.view_pager);
        if (viewPager2 == null) {
            System.out.println("=============== null viewpager2 =================");
        }
        fragmentAdapter adapter1 = new fragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter1);
        viewPager2.setUserInputEnabled(false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        navView2.getLayoutParams().width = displayMetrics.widthPixels;
    }

    private void setDrawer(){
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (view.getId() == R.id.nav_view2) {
                    setItem = -1;
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
                } else if (view.getId() == R.id.nav_view && setItem != -1) {
                    drawer.openDrawer(GravityCompat.END);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);
                }
            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);
    }

    private void setRecycleView(){
        Context context =  this;
        ArrayList<GroupMessageAndroid> list = Binders.instance.unArchivedList;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new sortedAdapter(list, this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            GroupMessageAndroid grp;
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                grp = adapter.removeItem(viewHolder.getAdapterPosition());
                adapter.archiveItem(grp);
                Binders.instance.archivedMessageSet.add(grp.getDatabaseIndex());
                Snackbar snackbar = Snackbar.make(recyclerView,grp.getGroupName() +" moved to archived", BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("Undo", view -> {
                            adapter.addItem(grp);
                            adapter.unArchiveItem(grp);
                            Binders.instance.archivedMessageSet.remove(grp.getDatabaseIndex());
                        });
                snackbar.show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(context, R.color.Ash))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_archive_24)
                        .addSwipeLeftLabel("Archive")
                        .create()
                        .decorate();


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);

    }
}