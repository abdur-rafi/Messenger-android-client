package com.example.messenger;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;

public class fragment_add_contact extends Fragment {
    public static Handler handler_contact,handler_member;
    private int state;
    private ProgressBar progressBar;
    private Button button;
    public fragment_add_contact(int i) {
        state = i;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        ImageView imageView = view.findViewById(R.id.toolbar_image);
        imageView.setVisibility(View.GONE);
        TextView textView = view.findViewById(R.id.name);
        textView.setText("Add Contact");
        button = view.findViewById(R.id.add_contact_button);
        progressBar = view.findViewById(R.id.add_contact_progress_bar);
        if(state == Constants.USE_FOR_ADD_MEMBER) {
            handler_member = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == Constants.SET_PROGRESS) {
                        progressBar.setProgress(msg.arg1);
                        if (msg.arg1 == 100) {
                            button.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            TextView nameField = view.findViewById(R.id.add_contact_name);
                            TextView portField = view.findViewById(R.id.add_contact_port);
                            nameField.setText("");
                            portField.setText("");
                        }
                    } else if (msg.what == Constants.ADD_CONTACT_FAILURE) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setEnabled(true);
                        Toast toast = Toast.makeText(button.getContext(), "User Not Found", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    super.handleMessage(msg);
                }
            };
        }
        else{
            handler_contact = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    if (msg.what == Constants.SET_PROGRESS) {
                        progressBar.setProgress(msg.arg1);
                        if (msg.arg1 == 100) {
                            button.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                            TextView nameField = view.findViewById(R.id.add_contact_name);
                            TextView portField = view.findViewById(R.id.add_contact_port);
                            nameField.setText("");
                            portField.setText("");
                        }
                    } else if (msg.what == Constants.ADD_CONTACT_FAILURE) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setEnabled(true);
                        Toast toast = Toast.makeText(button.getContext(), "User Not Found", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    super.handleMessage(msg);
                }
            };
        }
        progressBar.setVisibility(View.INVISIBLE);
        button.setOnClickListener((view1 -> {
            TextView nameField = view.findViewById(R.id.add_contact_name);
            TextView portField = view.findViewById(R.id.add_contact_port);
            if (state == Constants.USE_FOR_ADD_CONTACT) {
                String message = "addContact";
                int key = -1;
                try {
                    key = Integer.parseInt(portField.getText().toString());
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
                int index2 = Binders.instance.getPerson().getMyAccount().getDatabaseIndex();
                int index = index2;
                transferPackageAndroid transmitPackage = new transferPackageAndroid(message,
                        new AccountAndroid(nameField.getText().toString().trim(), null, key), null,
                        index, index2, null, null);
                new Thread(() -> Transmit.transmitPackage(transmitPackage)).start();
                button.setEnabled(false);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }
            else if(state == Constants.USE_FOR_ADD_MEMBER){
                int index = Binders.instance.activeIndex;
                index = Binders.instance.getPerson().getGroups().get(index).getDatabaseIndex();
                int index2 = Binders.instance.getPerson().getMyAccount().getDatabaseIndex();
                int key = -1;
                try {
                    key = Integer.parseInt(portField.getText().toString());
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }
                transferPackageAndroid transmitPackage = new transferPackageAndroid("newMember",
                        new AccountAndroid(nameField.getText().toString().trim(), null, key), null,
                        index, index2, null, null);
                new Thread(()->Transmit.transmitPackage(transmitPackage)).start();
                button.setEnabled(false);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }
        }));

        return view;
    }
}
