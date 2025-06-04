package ToyORB;

import ByteCommunication.Commons.Address;
import ByteCommunication.Registry.Entry;
import ByteCommunication.Registry.Registry;
import ByteCommunication.RequestReply.ByteStreamTransformer;
import ByteCommunication.RequestReply.Replyer;
import ByteCommunication.MessageMarshaller.*;

import java.util.regex.*;

class DispatcherTransformer implements ByteStreamTransformer
{
	private MessageServer originalServer; // object reference?

	public DispatcherTransformer(MessageServer s)
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

class MessageServer 
{
	public Message get_answer(Message msg)
	{
		Pattern pattern = Pattern.compile("(\\w+)\\((\\w+)(?:\\s+(.+))?\\)");
        Matcher matcher = pattern.matcher(msg.data);
		String operation,serviceName,entryString;
		if (matcher.matches()) {
            operation = matcher.group(1);
            serviceName = matcher.group(2);
            entryString = matcher.group(3);
        } else {
            throw new IllegalArgumentException("Request should be formatted like this: operation(serviceName entryString?)");
        }

		if(operation.equals("register")){
            String[] parts = entryString.split(":");
            String destinationId = parts[0];
            int portNr = Integer.parseInt(parts[1]);
			Registry.instance().put(serviceName, new Entry(destinationId, portNr));
			// System.out.println("Registered serviceName=" + serviceName);
            return new Message(msg.sender, "Success");
		}
		else if (operation.equals("unregister")){
			Registry.instance().remove(serviceName);
			return new Message(msg.sender, "Success");
		}
        else if (operation.equals("getObjectReference")){
			// System.out.println("Trying to fetch object reference for " + serviceName);
            Entry entry = Registry.instance().get(serviceName + "Server");
            return new Message(msg.sender, entry.toString());
        }
		else{
			throw new IllegalArgumentException("Unknown operation " + operation);
		}
	}
}

public class Dispatcher {
    private static String name = "DispatcherServer";
    private static Entry entry = new Entry("127.0.0.1", 1111);

    public static Address getAddress() {
        return entry;
    }

    public static void main(String[] args) {
        Registry.instance().put(name,entry);

        ByteStreamTransformer transformer = new DispatcherTransformer(new MessageServer());
        Replyer replyer = new Replyer(name, entry);

        while(true){
            replyer.receive_transform_and_send_feedback(transformer);
        }
    }
}
