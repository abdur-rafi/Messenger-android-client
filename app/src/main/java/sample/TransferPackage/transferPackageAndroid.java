package sample.TransferPackage;

import sample.DataPackage.AccountAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class transferPackageAndroid implements Serializable {
    private String state = null;
    private AccountAndroid account = null;
    private ArrayList<MessageAndroid> messages = null;
    private int senderId = - 1;
    private int receiverId = -1;
    private ArrayList<Integer> count;
    private ArrayList<Integer> newCount;
    private long serialVersionUID = 3L;
    private Set<Integer> archivedMessageSet;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AccountAndroid getAccount() {
        return account;
    }

    public void setAccount(AccountAndroid account) {
        this.account = account;
    }

    public ArrayList<MessageAndroid> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageAndroid> messages) {
        this.messages = messages;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public ArrayList<Integer> getCount() {
        return count;
    }

    public void setCount(ArrayList<Integer> count) {
        this.count = count;
    }

    public ArrayList<Integer> getNewCount() {
        return newCount;
    }

    public void setNewCount(ArrayList<Integer> newCount) {
        this.newCount = newCount;
    }

    public Set<Integer> getArchivedMessageSet() {
        return archivedMessageSet;
    }

    public void setArchivedMessageSet(Set<Integer> archivedMessageSet) {
        this.archivedMessageSet = archivedMessageSet;
    }

    public transferPackageAndroid(String state, AccountAndroid account, ArrayList<MessageAndroid> messages, int senderId, int receiverId, ArrayList<Integer> count, ArrayList<Integer> newCount) {
        this.state = state;
        this.account = account;
        this.messages = messages;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.count = count;
        this.newCount = newCount;
        this.archivedMessageSet = null;
    }

    public transferPackageAndroid(String state, AccountAndroid account, ArrayList<MessageAndroid> messages, int senderId, int receiverId, ArrayList<Integer> count, ArrayList<Integer> newCount,Set<Integer> s) {
        this.state = state;
        this.account = account;
        this.messages = messages;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.count = count;
        this.newCount = newCount;
        this.archivedMessageSet = s;
    }


}
