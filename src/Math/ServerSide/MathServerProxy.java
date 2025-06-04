package Math.ServerSide;

import ByteCommunication.RequestReply.ByteStreamTransformer;
import ByteCommunication.MessageMarshaller.*;
import ByteCommunication.RequestReply.Replyer;
import ByteCommunication.Commons.Address;

class ServerTransformer implements ByteStreamTransformer
{
	private MathMessageServer originalServer; // object reference?

	public ServerTransformer(MathMessageServer s)
	{
		originalServer = s;
	}

	public byte[] transform(byte[] in)
	{
		Message msg;
		Marshaller m = new Marshaller();
		msg = m.unmarshal(in);

		Message answer = originalServer.get_answer(msg);

		byte[] bytes = m.marshal(answer);
		return bytes;

	}
}

public class MathServerProxy
{
    private MathMessageServer server;
    private Address addr;
    public MathServerProxy(MathMessageServer server, Address addr){
        this.server = server;
        this.addr = addr;
    }
    public void start(){
        ByteStreamTransformer transformer = new ServerTransformer(server);
        Replyer replyer = new Replyer("MathServerProxy", addr);

        while(true){
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}