package com.example.messenger;

import android.os.Binder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.messenger.Adapters.ContactAdapter;
import com.example.messenger.Adapters.groupInfoDrawerAdapter;
import com.google.android.material.navigation.NavigationView;

import sample.Binders;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;

public class fragment_group_info extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info,container,false);
        ImageView toolbar_image = view.findViewById(R.id.toolbar_image);
        toolbar_image.setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.name);
        textView.setText("Group Info");
        ImageView groupImage = view.findViewById(R.id.group_info_image);
        TextView groupName = view.findViewById(R.id.group_name);
        GroupMessageAndroid grp = Binders.instance.getPerson().getGroups()
                .get(Binders.instance.activeIndex);
        groupImage.setImageBitmap(Binders.instance.map.get(grp.fileIndex));
        groupName.setText(grp.getGroupName());
        ListView listView = view.findViewById(R.id.accountList);
        ContactAdapter adapter = new ContactAdapter(view.getContext(),grp.getParticipants());
        listView.setAdapter(adapter);
        DrawerLayout drawer = view.findViewById(R.id.group_info_drawer_layout);
        NavigationView navigation = view.findViewById(R.id.group_info_navigation);
        navigation.getLayoutParams().width = Binders.instance.width;
        Button button = view.findViewById(R.id.add_group_member);
        button.setOnClickListener(view1->{
            drawer.openDrawer(GravityCompat.END);
            Binders.instance.drawer = drawer;
        });
        ViewPager2 pager = navigation.findViewById(R.id.add_friend_pager);
        groupInfoDrawerAdapter adapter1 = new groupInfoDrawerAdapter(getFragmentManager(),getLifecycle());
        pager.setAdapter(adapter1);
        return view;
    }

}
