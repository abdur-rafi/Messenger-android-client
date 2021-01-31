//package com.example.messenger;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.messenger.Adapters.ContactAdapter;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.zip.Inflater;
//
//import sample.Binders;
//import sample.Constants;
//import sample.DataPackage.MessagePackage.GroupMessageAndroid;
//
//public class fragment_chat extends Fragment {
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_chat, container,false);
//        ImageView imageView = view.findViewById(R.id.toolbar_image);
//        ImageView nev_image = view.findViewById(R.id.nev_header_image);
//        int id = Binders.instance.getPerson().getMyAccount().fileId;
//        if(Binders.instance.map.containsKey(id)){
//            imageView.setImageBitmap(Binders.instance.map.get(id));
////            nev_image.setImageBitmap(Binders.instance.map.get(id));
//        }
//        else{
//            File path = getContext().getCacheDir();
//            File file = new File(path, "messenger_" + id + Constants.format);
//            if (!file.exists()) System.out.println("craaaaaaaaaaaaap");
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            imageView.setImageBitmap(bitmap);
//            Binders.instance.map.put(id,bitmap);
////            nev_image.setImageBitmap(bitmap);
//        }
//
//        ListView listView = view.findViewById(R.id.list);
//        ArrayList<GroupMessageAndroid> list = Binders.instance.getPerson().getGroups();
//        ContactAdapter adapter = new ContactAdapter(getContext(), list);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener((adapterView, view2, i, l) -> {
//            Binders.instance.activeIndex = i;
//            Intent intent = new Intent(getContext(), messages.class);
//            startActivity(intent);
//        });
//        return view;
//    }
//}
