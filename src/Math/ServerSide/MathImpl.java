package Math.ServerSide;

import Math.MathAPI;

public class MathImpl implements MathAPI{
    public float do_add(float a, float b){
        return a+b;
    }
    public float do_sqr(float a){
        return a*a;
    }
}
