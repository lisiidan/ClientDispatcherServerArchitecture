package ToyORB;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ByteCommunication.MessageMarshaller.Message;

public class GenericMessageServer {
    private Object reference;

    public GenericMessageServer(Object reference){
        this.reference = reference;
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

        try {
            Class<?>[] paramTypes = new Class<?>[paramParts.length];
            Object[] paramValues = new Object[paramParts.length];
            for (int i = 0; i < paramParts.length; i++) {
                String[] typeAndValue = paramParts[i].trim().split("\\s+", 2);
                if (typeAndValue.length != 2)
                    throw new IllegalArgumentException("Parameter format should be: type value");
                String type = typeAndValue[0];
                String value = typeAndValue[1];
                switch (type) {
                    case "int" -> {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.parseInt(value);
                    }
                    case "float" -> {
                        paramTypes[i] = float.class;
                        paramValues[i] = Float.parseFloat(value);
                    }
                    case "String" -> {
                        paramTypes[i] = String.class;
                        paramValues[i] = value;
                    }
                    default -> throw new IllegalArgumentException("Unsupported parameter type: " + type);
                }
            }
            Method method = reference.getClass().getMethod(methodName, paramTypes);
            Object result = method.invoke(reference, paramValues);
            return new Message(msg.sender, result.toString());

        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException("Failed to invoke method: " + methodName + "\n");
        }
    }
}
