package ToyORB;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import ByteCommunication.Commons.Address;
import ByteCommunication.MessageMarshaller.Marshaller;
import ByteCommunication.MessageMarshaller.Message;
import ByteCommunication.RequestReply.Requestor;

public class RemoteInvocationHandler implements InvocationHandler{
    private Address address;
    private Requestor r;
    private Marshaller m;

    public RemoteInvocationHandler(Address address) {
        this.address = address;

        r = new Requestor("Client");
        m = new Marshaller();
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append("(");

        Class<?>[] paramTypes = method.getParameterTypes();
        for (int i = 0; args != null && i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(getTypeName(paramTypes[i])).append(" ").append(args[i]);
        }
        sb.append(")");
        // System.out.println("Request message: " + sb);
        Message response = sendRequest(new Message("Client", sb.toString()));

        Class<?> returnType = method.getReturnType();
        if (returnType.equals(void.class)) return null;
        if (returnType.equals(int.class)) return Integer.parseInt(response.data);
        if (returnType.equals(float.class)) return Float.parseFloat(response.data);
        if (returnType.equals(String.class)) return response.data;

        throw new UnsupportedOperationException("Unsupported return type: " + returnType);
    }

    private Message sendRequest(Message request){
        byte[] bytes = m.marshal(request);
        bytes = r.deliver_and_wait_feedback(address, bytes);
        return m.unmarshal(bytes);
    }

    private String getTypeName(Class<?> clazz) {
        if (clazz.equals(int.class)) return "int";
        if (clazz.equals(float.class)) return "float";
        if (clazz.equals(String.class)) return "String";
        throw new IllegalArgumentException("Unsupported parameter type: " + clazz);
    }
}
