package Math.ServerSide;

import java.util.regex.*;

import ByteCommunication.MessageMarshaller.Message;
import Math.MathAPI;

public class MathMessageServer {
    private MathImpl mathImpl;

    public MathMessageServer(MathAPI mathAPI){
        this.mathImpl = (MathImpl) mathAPI;
    }

    public Message get_answer(Message msg)
    {
        Pattern pattern = Pattern.compile("(\\w+)\\((.*)\\)");
        Matcher matcher = pattern.matcher(msg.data);
        String methodName, paramString;
        String[] paramParts;
        if(matcher.matches()){
            methodName = matcher.group(1);
            paramString = matcher.group(2);
            paramParts = paramString.split(",\\s*");
        } else {
            throw new IllegalArgumentException("Request should be formatted like this: methodname(type1 value1, type2 value2 ...)");
        }
        String result;

        if(methodName.equals("do_add")){
            int params = 0;
            float[] paramValues = new float[2];
            for (String param : paramParts) {
                if(params >= 2){
                    throw new IllegalArgumentException("Too many parameters");
                }
                String[] typeAndValue = param.trim().split("\\s+", 2);
                String type = typeAndValue[0];
                String value = typeAndValue[1];
                if(!type.equals("float")){
                    throw new IllegalArgumentException("do_add requires float parameter");
                }
                paramValues[params++] = Float.parseFloat(value);
            }
            result = Float.toString(mathImpl.do_add(paramValues[0], paramValues[1]));
        }
        else if(methodName.equals("do_sqr")){
            int params = 0;
            float[] paramValues = new float[1];
            for (String param : paramParts) {
                if(params >= 1){
                    throw new IllegalArgumentException("Too many parameters");
                }
                String[] typeAndValue = param.trim().split("\\s+", 2);
                String type = typeAndValue[0];
                String value = typeAndValue[1];
                if(!type.equals("float")){
                    throw new IllegalArgumentException("do_sqr requires float parameter");
                }
                paramValues[params++] = Float.parseFloat(value);
            }
            result = Float.toString(mathImpl.do_sqr(paramValues[0]));
        }
        else{
            throw new IllegalArgumentException("Unknown method " + methodName);
        }
        return new Message(msg.sender, result);
    }
}
