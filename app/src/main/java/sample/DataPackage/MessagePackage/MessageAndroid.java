package sample.DataPackage.MessagePackage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class MessageAndroid implements Serializable {
    private String message;
    private int senderIndex;
    private int senderPort;
    private LocalDate date;
    private LocalTime time;
    private boolean isFirst;
    private String fileName = null;
    private int fileIndex = 0;
    private long serialVersionUID = 5L;
    public boolean inAndroid = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderIndex() {
        return senderIndex;
    }

    public void setSenderIndex(int senderIndex) {
        this.senderIndex = senderIndex;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public MessageAndroid(String message, int senderIndex, int senderPort, LocalDate date, LocalTime time, boolean isFirst) {
        this.message = message;
        this.senderIndex = senderIndex;
        this.senderPort = senderPort;
        this.date = date;
        this.time = time;
        this.isFirst = isFirst;
    }

    public MessageAndroid(String message, int senderIndex, int senderPort, LocalDate date, LocalTime time, boolean isFirst, String fileName, int fileIndex) {
        this.message = message;
        this.senderIndex = senderIndex;
        this.senderPort = senderPort;
        this.date = date;
        this.time = time;
        this.isFirst = isFirst;
        this.fileName = fileName;
        this.fileIndex = fileIndex;
    }



}
