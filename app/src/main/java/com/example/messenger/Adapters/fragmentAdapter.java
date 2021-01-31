package com.example.messenger.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.messenger.fragment_account_info;
import com.example.messenger.fragment_add_contact;
import com.example.messenger.fragment_archived_message;
import com.example.messenger.fragment_new_group;

import sample.Constants;

public class fragmentAdapter extends FragmentStateAdapter {


    public fragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            System.out.println("=============== creating fragment_add_contact for ADD_CONTACT =============== ");
            return new fragment_add_contact(Constants.USE_FOR_ADD_CONTACT);
        }
        if(position == 1) {
            System.out.println("=============== creating fragment_account_info =============== ");
            return new fragment_account_info(Constants.INFO_FOR_CURRENT);
        }
        if(position == 2){
            System.out.println("=============== creating fragment_new_group =============== ");
            return new fragment_new_group();
        }
        if(position == 3){
            System.out.println("=============== creating fragment_add_contact =============== ");
            return new fragment_archived_message();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
