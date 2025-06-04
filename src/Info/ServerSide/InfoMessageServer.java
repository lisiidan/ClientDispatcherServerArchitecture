package Info.ServerSide;

import Info.InfoAPI;
import ByteCommunication.MessageMarshaller.*;
import java.util.regex.*;

public class InfoMessageServer 
{
	private InfoImpl infoImpl;

	public InfoMessageServer(InfoAPI infoAPI){
		this.infoImpl = (InfoImpl) infoAPI;
	}

	public Message get_answer(Message msg)
	{
		Pattern pattern = Pattern.compile("(\\w+)\\((\\w+)\\s+(.+)\\)");
        Matcher matcher = pattern.matcher(msg.data);
		String methodName,paramType,paramValue;
		if (matcher.matches()) {
            methodName = matcher.group(1);
            paramType = matcher.group(2);
            paramValue = matcher.group(3);
        } else {
            throw new IllegalArgumentException("Request should be formatted like this: methodname(type value)");
        }
		String result;

		if(methodName.equals("get_road_info")){
			if(!paramType.equals("int")){
				throw new IllegalArgumentException("get_road_info requires int parameter");
			}
			int roadId = Integer.parseInt(paramValue);
			result = infoImpl.get_road_info(roadId);
		}
		else if (methodName.equals("get_temp")){
			if(!paramType.equals("String")){
				throw new IllegalArgumentException("get_temp requires String parameter");
			}
			result = Float.toString(infoImpl.get_temp(paramValue));
		}
		else{
			throw new IllegalArgumentException("Unknown method " + methodName);
		}
		return new Message(msg.sender, result);
	}
}