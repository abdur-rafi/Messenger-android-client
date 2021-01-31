package sample.DataPackage;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class AccountAndroid implements Serializable{
    private String Name;
    private String password;
    private int port = 0;
    private long serialVersionUID = 11L;
    private int databaseIndex;
    transient public InetAddress address;
    transient public int key;
    transient public boolean isActive = false;
    transient public DatagramSocket datagramSocket;
    transient public Socket socket;
    public int fileId = -1;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    public int getDatabaseIndex() {
        return databaseIndex;
    }

    public void setDatabaseIndex(int databaseIndex) {
        this.databaseIndex = databaseIndex;
    }


    public AccountAndroid(String name, String password) {
        Name = name;
        this.password = password;
    }

    public AccountAndroid(String name, String password, int port) {
        Name = name;
        this.password = password;
        this.port = port;
    }

    public AccountAndroid(String name, String password, int port, int databaseIndex,int fileId) {
        Name = name;
        this.password = password;
        this.port = port;
        this.databaseIndex = databaseIndex;
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return Name;
    }

}
