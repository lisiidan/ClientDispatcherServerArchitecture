package Info.ClientSide;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.Marshaller;
import ByteCommunication.MessageMarshaller.Message;
import ByteCommunication.RequestReply.Requestor;
import Info.InfoAPI;

public class InfoClientProxy implements InfoAPI{
    private Address serverAddress;
    private String name;
    private Requestor r;
    private Marshaller m;

    public InfoClientProxy(Address addr) {
        this.serverAddress = addr;
        this.name = "InfoClientProxy";
        
        r = new Requestor(name);
        m = new Marshaller();
    }

    public float get_temp(String city){
        Message request = new Message(name, "get_temp(String " + city +")");
        Message response = sendRequest(request);
        return Float.parseFloat(response.data);
    }

    public String get_road_info(int road_ID){
        Message request = new Message(name, "get_road_info(int " + road_ID +")");
        Message response = sendRequest(request);
        return response.data;
    }

    private Message sendRequest(Message request){
        byte[] bytes = m.marshal(request);
        bytes = r.deliver_and_wait_feedback(serverAddress, bytes);
        return m.unmarshal(bytes);
    }
}
