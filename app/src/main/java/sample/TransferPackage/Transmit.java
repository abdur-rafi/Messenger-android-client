package sample.TransferPackage;

import sample.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Transmit {
    String host = "192.168.0.102";
    public static DatagramSocket transmitPackage(transferPackageAndroid transferPackage){
        DatagramSocket datagramSocket = null;
        try {
            InetAddress address = InetAddress.getByName(Constants.getInstance().host);  // getByName()
            datagramSocket = new DatagramSocket();
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(transferPackage);
            oo.close();
            byte[] buffer = bStream.toByteArray();
            System.out.println("Sent packet size: " + buffer.length);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Constants.getInstance().udpPort);
            datagramSocket.send(packet);
        } catch (SocketException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return datagramSocket;
    }
}
