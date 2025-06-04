package Info.ServerSide;

import java.util.Random;
import Info.InfoAPI;

class InfoImpl implements InfoAPI {
    Random rand = new Random();
    public InfoImpl(){}
    public String get_road_info(int road_ID){
        String [] mockResponses = 
        {"The road in Arad is wet",
         "The road in Timisoara is dry",
         "The road in Bucharest is blocked by snow"};
        
        return mockResponses[rand.nextInt(mockResponses.length)];
    }
    public float get_temp(String city){
        return -30 + rand.nextFloat() * 60;
    }
}