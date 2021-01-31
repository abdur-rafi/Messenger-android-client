package com.example.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;

public class fragment_new_group extends Fragment {
    public static Handler handler = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_group,container,false);
        ImageView imageView = view.findViewById(R.id.group_image);
        Button button = view.findViewById(R.id.choose_group_image);
        Button button1 = view.findViewById(R.id.create_group);
        TextView nameFiled = view.findViewById(R.id.group_nameField);
        ImageView toolbarImage = view.findViewById(R.id.toolbar_image);
        toolbarImage.setVisibility(View.GONE);
        TextView toolbarName = view.findViewById(R.id.name);
        toolbarName.setText("Create New Group");
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == Constants.CHANGE_IMAGE){
                    imageView.setImageBitmap((Bitmap)msg.obj);
                }
            }
        };
        button.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
        });
        button1.setOnClickListener(view1->{
            File path = getContext().getCacheDir();
            File file = new File(path, "messenger_" + "temp" + Constants.format);
            if (!file.exists()) System.out.println("================ messenger_" + "temp"
                    + Constants.format + " doesn't exist ================");
            String name = nameFiled.getText().toString();
            new Thread(()->{
                transferPackageAndroid transferPackage = new transferPackageAndroid("newGroup",new AccountAndroid(name,null),
                        null,Binders.instance.getPerson().getMyAccount().getDatabaseIndex(),-1,null,null);
                Transmit.transmitPackage(transferPackage);
                MainActivity.sendFile(file, Binders.instance.getPerson().getMyAccount().socket,null);
            }).start();
        });
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            new Thread(()-> {
                File path = getContext().getCacheDir();
                File file = new File(path, "messenger_" + "temp" + Constants.format);
                if (!file.exists()) System.out.println("================ messenger_" + "temp"
                        + Constants.format + " doesn't exist ================");
                saveBitmapToFile(data, file,getContext());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                Message message = Message.obtain();
                message.what = Constants.CHANGE_IMAGE;
                message.obj = bitmap;
                handler.sendMessage(message);
            }).start();
        }
    }
    public void saveBitmapToFile(Intent data, File out, Context context) {
        try {
            InputStream inputStream =context.getContentResolver().openInputStream(data.getData());
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            final int REQUIRED_SIZE = Constants.size; ;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = context.getContentResolver().openInputStream(data.getData());
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            FileOutputStream outputStream = new FileOutputStream(out);
            if (Constants.format.equals(".jpeg"))
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            else if (Constants.format.equals(".png"))
                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
