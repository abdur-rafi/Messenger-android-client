package sample;


import android.graphics.Bitmap;
import android.os.Message;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.messenger.contact;
import com.example.messenger.fragment_add_contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sample.DataPackage.AccountAndroid;
import sample.DataPackage.MessagePackage.GroupMessageAndroid;
import sample.DataPackage.MessagePackage.MessageAndroid;
import sample.DataPackage.PersonAndroid;


public class Binders {
    public static Binders instance = new Binders();
    private PersonAndroid person = null;
    public int activeIndex = -1;
    public Map<Integer,Bitmap> map = new HashMap<>();
    public ExecutorService executorService = Executors.newFixedThreadPool(1);
    public ArrayList<GroupMessageAndroid> archivedList = new ArrayList<>();
    public ArrayList<GroupMessageAndroid> unArchivedList = new ArrayList<>();
    public Set<Integer> archivedMessageSet = new HashSet<>();
    public int width;
    public DrawerLayout drawer = null;

    public PersonAndroid getPerson() {
        return person;
    }

    public void setPerson(PersonAndroid person) {
        this.person = person;
        activeIndex = -1;
        map.clear();
        archivedList.clear();
        unArchivedList.clear();
        archivedMessageSet = person.getSet();
        setGroupImages();
        for(int i=0;i<person.getUnseenCount().size();++i){
            person.getGroups().get(i).unseenCount = person.getUnseenCount().get(i);
            person.getGroups().get(i).newCount = person.getNewCount().get(i);
        }
        for(GroupMessageAndroid grp:person.getGroups()){
            if(archivedMessageSet.contains(grp.getDatabaseIndex())) archivedList.add(grp);
            else unArchivedList.add(grp);
        }
        System.out.println("size od set : " +  person.getSet().size());

    }

    public synchronized void addGroupMessage(GroupMessageAndroid groupMessage, int id) {
        person.getParticipationIndex().add(id);
        person.getUnseenCount().add(0);
        person.getNewCount().add(++person.messageCount);
        person.getSent().add(0);
        person.addGroupMessage(groupMessage);
        groupMessage.newCount = person.messageCount;
        setIndividualGroupImage(person.getGroups().size()-1);
        Message message2 = Message.obtain();
        message2.what = Constants.ADD_CONTACT;
        contact.handler.sendMessage(message2);
        if(!groupMessage.isAddAble()) {
            Message message = Message.obtain();
            message.arg1 = 100;
            message.what = Constants.SET_PROGRESS;
            fragment_add_contact.handler_contact.sendMessage(message);
        }
    }


    public synchronized void addMessageOrAccount(MessageAndroid message, int databaseIndex, AccountAndroid account, String state) {
        int i = 0;
        Message message1 = null;
        for (GroupMessageAndroid groups : person.getGroups()) {
            if (groups.getDatabaseIndex() == databaseIndex) {
                if (message != null) {
                    groups.getGroupMessage().add(message);
                    message1 = Message.obtain();
                    message1.what = Constants.ADD_MESSAGE;
                    message1.obj = groups;
                } else groups.getParticipants().add(account);
                break;
            }
            ++i;
        }
        if (message != null) {
            person.getNewCount().set(i, ++person.messageCount);
            int n = person.getUnseenCount().get(i);
            person.getUnseenCount().set(i, n + 1);
            GroupMessageAndroid grps = person.getGroups().get(i);
            grps.unseenCount = n+1;
            contact.handler.sendMessage(message1);
        }
        if(account != null){
            Message message2 = Message.obtain();
            message2.what = Constants.SET_PROGRESS;
            message2.arg1 = 100;
            fragment_add_contact.handler_member.sendMessage(message2);
        }


    }

    public void setGroupImages() {
        for (GroupMessageAndroid groups : person.getGroups()) {
            if (!groups.isAddAble()) {
                for (AccountAndroid acc : groups.getParticipants()) {
                    if (acc.getPort() != person.getMyAccount().getPort()) {
                        groups.setGroupName(acc.getName());
                        groups.fileIndex = acc.fileId;
                        break;
                    }
                }
            }
        }
    }
    public void setIndividualGroupImage(int i) {
        GroupMessageAndroid group = person.getGroups().get(i);
        if (group.isAddAble()){
            return;
        }
        for (AccountAndroid acc : group.getParticipants()) {
            if (acc.getPort() != Binders.instance.getPerson().getMyAccount().getPort()) {
                group.fileIndex = acc.fileId;
                group.setGroupName(acc.getName());
                break;
            }
        }
    }



}
