package com.example.messenger.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.messenger.fragment_account_info;
import com.example.messenger.fragment_group_info;

import sample.Constants;

public class fragmentAdapterMessage extends FragmentStateAdapter {

    public fragmentAdapterMessage(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            System.out.println("=============== creating fragment_account_info =============== ");
            return new fragment_account_info(Constants.INFO_FOR_OTHER);
        }
        if(position == 1) {
            System.out.println("=============== creating fragment_group_info =============== ");
            return new fragment_group_info();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
