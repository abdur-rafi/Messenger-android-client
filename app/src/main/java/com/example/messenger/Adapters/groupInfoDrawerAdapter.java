package com.example.messenger.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.messenger.fragment_add_contact;

import sample.Constants;

public class groupInfoDrawerAdapter extends FragmentStateAdapter {
    public groupInfoDrawerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) {
            System.out.println("=============== creating fragment_add_contact for ADD_MEMBER =============== ");
            return new fragment_add_contact(Constants.USE_FOR_ADD_MEMBER);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
