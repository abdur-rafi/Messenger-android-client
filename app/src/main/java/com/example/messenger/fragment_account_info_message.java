//package com.example.messenger;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.media.Image;
//import android.net.Uri;
//import android.os.Binder;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import sample.Binders;
//import sample.Constants;
//import sample.DataPackage.AccountAndroid;
//import sample.TransferPackage.Transmit;
//import sample.TransferPackage.transferPackageAndroid;
//
//
//public class fragment_account_info_message extends Fragment {
//    private CircleImageView image;
//    private ProgressBar progressBar;
//    private Button button;
//    public static Handler handler;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_account_info, container, false);
//        image = view.findViewById(R.id.info_image);
//        progressBar = view.findViewById(R.id.info_progressbar);
//        progressBar.setVisibility(View.INVISIBLE);
//        ImageView toolbarImage = view.findViewById(R.id.toolbar_image);
//        toolbarImage.setVisibility(View.GONE);
//        TextView toolbarName = view.findViewById(R.id.name);
//        toolbarName.setText("Account Info");
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                if (msg.what == Constants.SET_PROGRESS) {
//                    if (progressBar.getVisibility() == View.INVISIBLE)
//                        progressBar.setVisibility(View.VISIBLE);
//                    if (button.isEnabled()) button.setEnabled(false);
//                    progressBar.setProgress(msg.arg1);
//                    if (msg.arg1 == 100) {
//                        progressBar.setVisibility(View.INVISIBLE);
//                        System.out.println("here");
//                        image.setImageBitmap(Binders.instance.map.get(Binders.instance.getPerson().getMyAccount().fileId));
//                        button.setEnabled(true);
//                        Message message = Message.obtain();
//                        message.what = Constants.CHANGE_CURRENT_IMAGE;
//                        contact.handler.sendMessage(message);
//                        System.out.println("image : " + image);
//                    }
//                }
//            }
//        };
//        TextView name = view.findViewById(R.id.info_name);
//        TextView port = view.findViewById(R.id.info_port);
//        button = view.findViewById(R.id.info_change_image);
//        AccountAndroid acc = null;
//        if(Binders.instance.activeIndex == -1)
//            acc = Binders.instance.getPerson().getMyAccount();
//        else {
//            button.setVisibility(View.GONE);
//            int j = Binders.instance.activeIndex;
//            for(AccountAndroid account:Binders.instance.getPerson().getGroups()
//                    .get(j).getParticipants()){
//                if(account.getPort() != Binders.instance.getPerson().getMyAccount().getPort()){
//                    acc = account;
//                    break;
//                }
//            }
//        }
//        if(acc == null) return null;
//        int id = acc.fileId;
//        if (!Binders.instance.map.containsKey(id)) {
//            File path = getContext().getCacheDir();
//            File file = new File(path, "messenger_" + id + ".png");
//            if (!file.exists()) System.out.println("craaaaaaaaaaaaap");
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            Binders.instance.map.put(id, bitmap);
//        }
//
//        image.setImageBitmap(Binders.instance.map.get(id));
//        name.setText(acc.getName());
//        port.setText(Integer.toString(acc.getPort()));
//        button.setOnClickListener(view1 -> {
//
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE);
//        });
//        return view;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Constants.PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//
//            Thread thread = new Thread(() -> {
//                File path = Binders.instance.context.getCacheDir();
//                int j = Binders.instance.getPerson().getMyAccount().fileId;
//                File file = new File(path, "messenger_" + j + Constants.format);
//                saveBitmapToFile(data, file);
//                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                Binders.instance.map.put(Binders.instance.getPerson().getMyAccount().fileId, bitmap);
//                Thread thread1 = new Thread(() -> {
//                    transferPackageAndroid transferPackage = new transferPackageAndroid("updateImage", Binders.instance.getPerson().getMyAccount(), null,
//                            0, 0, null, null);
//                    Transmit.transmitPackage(transferPackage);
//                    MainActivity.sendFile(file, Binders.instance.getPerson().getMyAccount().socket,null);
//                    Message message1 = Message.obtain();
//                    message1.what = Constants.SET_PROGRESS;
//                    message1.arg1 = 100;
//                    message1.obj = image;
//                    handler.sendMessage(message1);
//                });
//                Binders.instance.executorService.execute(thread1);
//            });
//            thread.start();
//        }
//    }
//
//    public static void saveBitmapToFile(Intent data, File out) {
//        try {
//            InputStream inputStream = Binders.instance.context.getContentResolver().openInputStream(data.getData());
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            o.inSampleSize = 6;
//            BitmapFactory.decodeStream(inputStream, null, o);
//            inputStream.close();
//            Message message = Message.obtain();
//            message.what = Constants.SET_PROGRESS;
//            message.arg1 = 20;
//            handler.sendMessage(message);
//            final int REQUIRED_SIZE = 25;
//            int scale = 1;
//            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
//                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
//                scale *= 2;
//            }
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            inputStream = Binders.instance.context.getContentResolver().openInputStream(data.getData());
//
//            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
//            inputStream.close();
//            Message message1 = Message.obtain();
//            message1.arg1 = 40;
//            message1.what = Constants.SET_PROGRESS;
//            handler.sendMessage(message1);
//            FileOutputStream outputStream = new FileOutputStream(out);
//
//            if (Constants.format.equals(".jpeg"))
//                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            else if (Constants.format.equals(".png"))
//                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.close();
//            Message message2 = Message.obtain();
//            message2.what = Constants.SET_PROGRESS;
//            message2.arg1 = 70;
//            handler.sendMessage(message2);
//            System.out.println(out.length());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
