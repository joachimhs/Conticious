package no.haagensoftware.contentice.util;

import java.util.ArrayList;
import java.util.List;

public class ListToString {

	public static String convertFromList(List<String> list, String delimeter) {
		if (list == null || list.isEmpty()) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < list.size(); i++) { 
		    if (i > 0)  {
		        sb.append(delimeter);
		    }

		    sb.append(list.get(i)); 
		}
		
		return sb.toString();
	}

    public static List<String> convertToList(String content, String delimeter) {
        List<String> retList = new ArrayList<>();

        if (content.contains(delimeter)) {
            String[] values = content.split(delimeter);
            for (String val : values) {
                retList.add(val);
            }
        } else {
            retList.add(content);
        }

        return retList;
    }
}
