package sample.TransferPackage;

import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;

import java.io.*;
import java.util.ArrayList;

public class FromServerAndroid implements Serializable {
    private String state = null;
    private GroupMessageAndroid groupMessage = null;
    private ArrayList<MessageAndroid>  message = null;
    private int toBeModified = 0;
    private int another = 0;
    private long serialVersionUID = 6L;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public GroupMessageAndroid getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(GroupMessageAndroid groupMessage) {
        this.groupMessage = groupMessage;
    }

    public ArrayList<MessageAndroid> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<MessageAndroid> message) {
        this.message = message;
    }

    public int getToBeModified() {
        return toBeModified;
    }

    public void setToBeModified(int toBeModified) {
        this.toBeModified = toBeModified;
    }

    public int getAnother() {
        return another;
    }

    public void setAnother(int another) {
        this.another = another;
    }

    public FromServerAndroid(String state, GroupMessageAndroid groupMessage, ArrayList<MessageAndroid> message, int toBeModified, int another) {
        this.state = state;
        this.groupMessage = groupMessage;
        this.message = message;
        this.toBeModified = toBeModified;
        this.another = another;
    }

}
