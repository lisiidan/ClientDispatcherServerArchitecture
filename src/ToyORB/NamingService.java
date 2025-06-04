package ToyORB;

// import java.lang.reflect.Constructor;
// import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

// import ByteCommunication.Commons.Address;
import ByteCommunication.Registry.Entry;
import ByteCommunication.RequestReply.Requestor;
import ByteCommunication.MessageMarshaller.*;

public class NamingService {
    private static String name = "NamingService";
	private static Requestor r = new Requestor(name);
    private static Marshaller m = new Marshaller();

	private static Message sendRequest(Message request){
        byte[] bytes = m.marshal(request);
        bytes = r.deliver_and_wait_feedback(Dispatcher.getAddress(), bytes);
        return m.unmarshal(bytes);
    }
    public static void register(String name, Object impl, Entry entry){
        // Registry.instance().put(name, entry);
		Message request = new Message(name, "register(" + name + " " + entry.toString() +")");
        Message response = sendRequest(request);
		try {
			GenericMessageServer messageServer = new GenericMessageServer(impl);
        	GenericServerProxy proxy = new GenericServerProxy(messageServer, entry);
			proxy.start();
			// Class<?> implClass = impl.getClass();
			// String baseName = implClass.getSimpleName().replace("Impl", "");
			// String packageName = implClass.getPackageName();

			// Class<?> messageServerClass = Class.forName(packageName + "." + baseName + "MessageServer");
			// Constructor<?> messageServerCtor = messageServerClass.getConstructor(implClass.getInterfaces()[0]);
			// Object messageServer = messageServerCtor.newInstance(impl);

			// Class<?> proxyClass = Class.forName(packageName + "." + baseName + "ServerProxy");
			// Constructor<?> proxyCtor = proxyClass.getConstructor(messageServerClass, Address.class);
			// Object proxy = proxyCtor.newInstance(messageServer, entry);

			// Method startMethod = proxyClass.getMethod("start");
			// startMethod.invoke(proxy);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to start proxy for " + name);
		}
    }
	public static Object getObjectReference(String serviceName, Class<?> interfaceClass){
		Message request = new Message(name, "getObjectReference(" + serviceName + ")");
		Message response = sendRequest(request);

		// Create entry with InfoServer ip and port
		String[] parts = response.data.split(":");
        String destinationId = parts[0];
        int portNr = Integer.parseInt(parts[1]);
		Entry entry = new Entry(destinationId, portNr);

		// Generate proxy
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new RemoteInvocationHandler(entry));
	}

}
