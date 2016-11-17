package Simulation;

import OSPABA.*;

public class MyMessage extends MessageForm {

    private String msg = "";

    public MyMessage(Simulation sim) {
        super(sim);
    }

    public MyMessage(MyMessage original) {
        super(original);
        // copy() is called in superclass
    }

    public void setMsg(String paMsg) {
        msg = paMsg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public MessageForm createCopy() {
        return new MyMessage(this);
    }

    @Override
    protected void copy(MessageForm message) {
        super.copy(message);
        MyMessage original = (MyMessage) message;
    }
}
