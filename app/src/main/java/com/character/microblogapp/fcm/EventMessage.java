package com.character.microblogapp.fcm;

public class EventMessage {
    public int nWhat;
    public Object obj;

    public EventMessage(int nWhat, Object obj) {
        this.nWhat = nWhat;
        this.obj = obj;
    }
}
