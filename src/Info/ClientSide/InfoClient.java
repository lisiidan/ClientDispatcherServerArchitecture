package Info.ClientSide;

import Info.InfoAPI;
import ToyORB.NamingService;

public class InfoClient {
    public static void main(String[] args) {
        InfoAPI infoAPI = (InfoAPI) NamingService.getObjectReference("Info", InfoAPI.class);

        System.out.println(infoAPI.get_road_info(1));

        System.out.println(infoAPI.get_temp("Timisoara"));
    }
}
