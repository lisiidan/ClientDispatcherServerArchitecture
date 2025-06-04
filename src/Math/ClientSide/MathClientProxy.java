package Math.ClientSide;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.Marshaller;
import ByteCommunication.MessageMarshaller.Message;
import ByteCommunication.RequestReply.Requestor;
import Math.MathAPI;

public class MathClientProxy implements MathAPI{
    private Address serverAddress;
    private String name;
    private Requestor r;
    private Marshaller m;

    public MathClientProxy(Address addr) {
        this.serverAddress = addr;
        this.name = "MathClientProxy";
        
        r = new Requestor(name);
        m = new Marshaller();
    }

    public float do_add(float a, float b){
        Message request = new Message(name, "do_add(float " + a +", float " + b + ")");
        Message response = sendRequest(request);
        return Float.parseFloat(response.data);
    }

    public float do_sqr(float a){
        Message request = new Message(name, "do_sqr(float " + a + ")");
        Message response = sendRequest(request);
        return Float.parseFloat(response.data);
    }

    private Message sendRequest(Message request){
        byte[] bytes = m.marshal(request);
        bytes = r.deliver_and_wait_feedback(serverAddress, bytes);
        return m.unmarshal(bytes);
    }
}
