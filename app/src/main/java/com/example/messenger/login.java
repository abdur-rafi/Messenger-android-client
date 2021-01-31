package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.DataPackage.PersonAndroid;
import sample.Server.serverThread;
import sample.TransferPackage.Transmit;
import sample.TransferPackage.transferPackageAndroid;


public class login extends AppCompatActivity {

    public Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.logIn);
        Context context = this;
        ProgressBar progressBar = findViewById(R.id.login_progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == Constants.LOG_IN){
                        login.setEnabled(true);
                        startIntent(context);
                }
                else if(msg.what == Constants.LOG_IN_FAILURE){
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.INVISIBLE);
                    login.setEnabled(true);
                }
            }
        };
        login.setOnClickListener(view -> {
            EditText nameField = findViewById(R.id.login_nameField);
            EditText passwordField = findViewById(R.id.login_passwordField);
            EditText portField = findViewById(R.id.login_portField);
            String name = nameField.getText().toString();
            String password = passwordField.getText().toString();
            int port = Integer.parseInt(portField.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
            login.setEnabled(false);
            transferPackageAndroid transmitPackage = new transferPackageAndroid("logIn", new AccountAndroid(name, password, port),
                    null, -1, -1, null, null);
            new Thread(() -> {
                Socket socket;
                PersonAndroid person;
                try {
                    socket = new Socket(Constants.getInstance().host, Constants.getInstance().tcpPort);
                    ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    oos.writeObject(transmitPackage);
                    oos.flush();
                    runOnUiThread(() -> progressBar.setProgress(20));
                    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                    person = (PersonAndroid) ois.readObject();

                    if(person == null){
                        Message message = Message.obtain();
                        message.what = Constants.LOG_IN_FAILURE;
                        handler.sendMessage(message);
                        return;
                    }
                    printPerson(person);
                    runOnUiThread(()->progressBar.setProgress(40));
                    int c = ois.readInt();
                    int d = 60 / c;
                    int p = 40;
                    System.out.println("=================== number of files : " + c + " ===================");
                    while (--c >= 0) {
                        MainActivity.receiveFile(socket, null, context,ois);
                        p += d;
                        int p1 = p;
                        runOnUiThread(() -> progressBar.setProgress(p1));
                    }
                    runOnUiThread(() -> progressBar.setProgress(100));
                    person.getMyAccount().socket = socket;
                    person.getMyAccount().isActive = true;
                    Binders.instance.setPerson(person);
                    transferPackageAndroid packet = new transferPackageAndroid("addSocket", null, null, person.getMyAccount().getDatabaseIndex()
                            , -1, null, null);
                    person.getMyAccount().datagramSocket = Transmit.transmitPackage(packet);
                    new serverThread(person.getMyAccount().datagramSocket,context).start();
                    Message message = Message.obtain();
                    message.what = Constants.LOG_IN;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

        });

    }

    public static void printPerson(PersonAndroid person){
        System.out.println("===============Logged In===============");
        System.out.println("Account Info:");
        System.out.println("Name                : " + person.getMyAccount().getName());
        System.out.println("Port                : " + person.getMyAccount().getPort());
        System.out.println("MessageGroup count  : " + person.getGroups().size());
    }

    public void startIntent(Context context) {
        Intent intent = new Intent(context, contact.class);
        startActivity(intent);
    }
}