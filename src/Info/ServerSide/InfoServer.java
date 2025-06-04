package Info.ServerSide;

import ByteCommunication.Registry.Entry;
import ToyORB.NamingService;


public class InfoServer {
    public static void main(String[] args) {
        InfoImpl infoImpl = new InfoImpl();
		NamingService.register("InfoServer", infoImpl, new Entry("127.0.0.1", 1112));
    }
}
