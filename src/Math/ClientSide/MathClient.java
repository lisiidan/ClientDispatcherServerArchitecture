package Math.ClientSide;

import Math.MathAPI;
import ToyORB.NamingService;

public class MathClient {
    public static void main(String[] args) {
        MathAPI mathAPI = (MathAPI) NamingService.getObjectReference("Math", MathAPI.class);

        System.out.println(mathAPI.do_add(1.0f,2.9f));

        System.out.println(mathAPI.do_sqr(3.0f));
    }
}
