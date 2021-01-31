package sample.Server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;


import com.example.messenger.MainActivity;
import com.example.messenger.contact;
import com.example.messenger.fragment_add_contact;
import com.example.messenger.messages;

import sample.Binders;
import sample.Constants;
import sample.DataPackage.AccountAndroid;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;
import sample.TransferPackage.FromServerAndroid;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class serverThread extends Thread {
    DatagramSocket datagramSocket;
    Context context;
    public serverThread(DatagramSocket datagramSocket, Context context) {
        this.datagramSocket = datagramSocket;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = datagramSocket;
            while (true) {
                byte[] buffer = new byte[100000];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                System.out.println("waiting to receive Here");
                socket.receive(packet);
                FromServerAndroid fromServer1 = null;
                try (ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buffer))) {
                    fromServer1 = (FromServerAndroid) inputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                FromServerAndroid fromServer = fromServer1;
                new Thread(() -> {
                    if (fromServer.getState().equals("addContact") || fromServer.getState().equals("newGroup")) {
                        final FromServerAndroid fromServer2 = fromServer;
                        if(fromServer.getToBeModified() == -1){
                            Message message = Message.obtain();
                            message.what = Constants.ADD_CONTACT_FAILURE;
                            fragment_add_contact.handler_contact.sendMessage(message);
                            return;
                        }
                        Socket socket1 = Binders.instance.getPerson().getMyAccount().socket;
                        Binders.instance.executorService.execute(new Thread(()->{
                            try {
                                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket1.getInputStream()));
                                GroupMessageAndroid grp = (GroupMessageAndroid) ois.readObject();
                                int c = ois.readInt();
                                while(--c>=0 ) MainActivity.receiveFile(socket1,null,context,ois);
                                if(fromServer.getState().equals("addContact")) {
                                    Message message = Message.obtain();
                                    message.arg1 = 50;
                                    message.what = Constants.SET_PROGRESS;
                                    fragment_add_contact.handler_contact.sendMessage(message);
                                }
                                Binders.instance.addGroupMessage(grp, fromServer2.getToBeModified());
                            } catch (IOException | ClassNotFoundException e){
                                e.printStackTrace();
                            }
                        }));
                    } else if (fromServer.getState().equals("updateImage")) {
                        FromServerAndroid fromServer2 = fromServer;
                        Thread thread = new Thread(()->{
                            Socket socket1 = Binders.instance.getPerson().getMyAccount().socket;
                            MainActivity.receiveFile(socket1, null,context,null);
                            int j = fromServer.getAnother();
                            File path = context.getCacheDir();
                            File file = new File(path,"messenger_" + j + Constants.format);
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            Binders.instance.map.put(j,bitmap);
                            Message message = Message.obtain();
                            message.what = Constants.CHANGE_IMAGE;
                            contact.handler.sendMessage(message);
                        });
                        Binders.instance.executorService.execute(thread);
                    } else if (fromServer.getState().equals("message") || fromServer.getState().equals("newMember")) {
                        if(fromServer.getToBeModified() == -1){
                            Message message = Message.obtain();
                            message.what = Constants.ADD_CONTACT_FAILURE;
                            fragment_add_contact.handler_member.sendMessage(message);
                            return;
                        }
                        if(fromServer.getState().equals("newMember")) {
                            Message message_ = Message.obtain();
                            message_.what = Constants.SET_PROGRESS;
                            message_.arg1 = 30;
                            fragment_add_contact.handler_member.sendMessage(message_);
                        }
                        MessageAndroid message = null;
                        AccountAndroid account = null;
                        if (fromServer.getState().equals("message")) {
                            message = fromServer.getMessage().get(0);
                        }
                        else {
                            account = fromServer.getGroupMessage().getParticipants().get(0);
                            MainActivity.receiveFile(Binders.instance.getPerson().getMyAccount().socket,
                                    null,context,null);
                            Message message_1 = Message.obtain();
                            message_1.what = Constants.SET_PROGRESS;
                            message_1.arg1 = 70;
                            fragment_add_contact.handler_member.sendMessage(message_1);
                        }
                        int index = fromServer.getToBeModified();
                        System.out.println("message or account");
                        MessageAndroid message1 = message;
                        AccountAndroid account1 = account;
                        Binders.instance.addMessageOrAccount(message1, index, account1,fromServer.getState());
                        Message message2 = Message.obtain();
                        message2.what = Constants.ADD_MESSAGE;
                        if(messages.handler != null)
                        messages.handler.sendMessage(message2);
                    }
                    else if(fromServer.getState().equals("notFound")){

                    }
                    else if(fromServer.getState().equals("active")){
                        System.out.println("active");
                        int port = fromServer.getToBeModified();
                        boolean active = fromServer.getAnother() == 1;
                        for(GroupMessageAndroid grps : Binders.instance.getPerson().getGroups()){
                            for(AccountAndroid acc:grps.getParticipants()){
                                if(acc.getPort() == port){
                                    acc.isActive = active;
                                }
                            }
                        }

                    }
                }).start();
                if (fromServer.getState().equals("exit")) {
                    System.out.println(fromServer.getState());
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}