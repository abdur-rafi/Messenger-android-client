package sample.DataPackage.MessagePackage;

import android.os.Build;

import androidx.annotation.RequiresApi;

import sample.DataPackage.AccountAndroid;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GroupMessageAndroid implements Serializable {
    private ArrayList<MessageAndroid> groupMessage;
    private ArrayList<AccountAndroid> participants;
    private ArrayList<Integer> personIndex;
    private int databaseIndex;
    private ArrayList<String> sharedFiles;
    private String groupName;
    private boolean addAble;
    private long serialVersionUID = 4L;
    public LocalDateTime lastMessageTime;
    public int fileIndex;
    public int unseenCount = 0;
    public int newCount = 0;

    public ArrayList<MessageAndroid> getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(ArrayList<MessageAndroid> groupMessage) {
        this.groupMessage = groupMessage;
    }

    public ArrayList<AccountAndroid> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<AccountAndroid> participants) {
        this.participants = participants;
    }

    public ArrayList<Integer> getPersonIndex() {
        return personIndex;
    }

    public void setPersonIndex(ArrayList<Integer> personIndex) {
        this.personIndex = personIndex;
    }

    public int getDatabaseIndex() {
        return databaseIndex;
    }

    public void setDatabaseIndex(int databaseIndex) {
        this.databaseIndex = databaseIndex;
    }

    public ArrayList<String> getSharedFiles() {
        return sharedFiles;
    }

    public void setSharedFiles(ArrayList<String> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public boolean isAddAble() {
        return addAble;
    }

    public void setAddAble(boolean addAble) {
        this.addAble = addAble;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public GroupMessageAndroid(ArrayList<MessageAndroid> groupMessage, ArrayList<AccountAndroid> participants,
                               int databaseIndex, ArrayList<String> sharedFiles, String groupName,
                               boolean addAble,
                               ArrayList<Integer> personIndex) {
        this.groupMessage = groupMessage;
        this.participants = participants;
        this.databaseIndex = databaseIndex;
        this.sharedFiles = sharedFiles;
        this.groupName = groupName;
        this.addAble = addAble;
        this.personIndex = personIndex;
        this.lastMessageTime = LocalDateTime.now();
    }

}
