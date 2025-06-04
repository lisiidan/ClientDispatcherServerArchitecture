package Math.ServerSide;

import ByteCommunication.Registry.Entry;
import ToyORB.NamingService;

public class MathServer {
    public static void main(String[] args) {
        MathImpl mathImpl = new MathImpl();
		NamingService.register("MathServer", mathImpl, new Entry("127.0.0.1", 1113));
    }
}
