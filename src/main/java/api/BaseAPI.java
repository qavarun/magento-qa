package api;

import java.util.HashMap;
import java.util.Map;

public class BaseAPI {

	public Map<String, String> getCommonHeaders(){
		Map<String,String> header = new HashMap<String, String>();
		header.put("Connection", "keep-alive");
		header.put("Cache-Control", "no-cache");
		return header;
	}
}
