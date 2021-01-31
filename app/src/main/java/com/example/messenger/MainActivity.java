package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.DataPackage.PersonAndroid;
import sample.Server.serverThread;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == Constants.CREATE_ACCOUNT){
                    Intent intent = new Intent(context,contact.class);
                    startActivity(intent);
                }
            }
        };
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        Binders.instance.width = width;

//        Binders.instance.context = getBaseContext();
        Button button = findViewById(R.id.createAccount);
        Button logIn = findViewById(R.id.enterLogIn);

        logIn.setOnClickListener(view->{
            Intent intent = new Intent(context,login.class);
            startActivity(intent);
        });

        button.setOnClickListener(view -> {
            EditText nameField = findViewById(R.id.nameField);
            EditText passwordField = findViewById(R.id.password);
            String name = nameField.getText().toString();
            String password = passwordField.getText().toString();
            AccountAndroid account = new AccountAndroid(name, password);
            transferPackageAndroid transmitPackage = new transferPackageAndroid("createUser", account, null,
                    -1, -1, null, null);
            button.setEnabled(false);
            new Thread(()->{
                try{
                    Socket socket = new Socket(Constants.host, Constants.tcpPort);
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.writeObject(transmitPackage);
                    oos.flush();
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    PersonAndroid person = (PersonAndroid) ois.readObject();
                    receiveFile(socket,null,context,ois);
                    printPerson(person);
                    person.getMyAccount().socket = socket;
                    Binders.instance.setPerson(person);
                    transferPackageAndroid packet = new transferPackageAndroid("addSocket",null,null,person.getMyAccount().getDatabaseIndex()
                            ,-1,null,null);
                    person.getMyAccount().datagramSocket =  Transmit.transmitPackage(packet);
                    new serverThread(person.getMyAccount().datagramSocket,getBaseContext()).start();
                    Message message = Message.obtain();
                    message.what = Constants.CREATE_ACCOUNT;
                    handler.sendMessage(message);
                } catch (IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public static void printPerson(PersonAndroid person){
        System.out.println("===============Account Created===============");
        System.out.println("Account Info:");
        System.out.println("Name                : " + person.getMyAccount().getName());
        System.out.println("Port                : " + person.getMyAccount().getPort());
        System.out.println("MessageGroup count  : " + person.getGroups().size());
    }

    public static void receiveFile(Socket socket, String fileName,Context context,ObjectInputStream ois) {
        try {
            if(ois == null){
                ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            }
            System.out.println("================= Receiving file =================");
            int count = 0;
            String name = ois.readUTF();
            if(fileName != null) name = fileName;
            System.out.println("File name           : " + name);
            Long size = ois.readLong();
            System.out.println("file size           : " + size);
            byte[] buffer = new byte[100000];
            File path = context.getCacheDir();
            File file = new File(path,"messenger_" + name + Constants.format);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            System.out.println("======= starting reading file =======");
            while (size > 0 && (count = ois.read(buffer,0,(int)Math.min(buffer.length,size))) > 0) {
                bos.write(buffer, 0, count);
                size -= count;
            }
            System.out.println("======= finished reading file =======");

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendFile(File file,Socket socket,ObjectOutputStream oos){
        byte[] buffer = new byte[100000];
        try {
            if(oos == null){
                oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            }
            System.out.println("================== Sending File ==================");
            System.out.println("File Name         : " + file.getName());
            System.out.println("length            : " + file.length());
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            oos.writeUTF(file.getName());
            oos.writeLong(file.length());
            int count;
            System.out.println("====== Starting sending file ======");
            while ((count = bis.read(buffer)) > 0) {
                oos.write(buffer, 0, count);
            }
            System.out.println("====== finished sending file ======");
            oos.flush();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}