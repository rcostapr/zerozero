package com.myfeup.zerozero;

import java.io.Serializable;

public class State implements Serializable {

    int state;

    State(int state){
        this.state =state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
