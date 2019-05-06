import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class CfgClass {

	String name = "";
	String inhereted = "";
	HashMap<String, String> properterties = new HashMap<>();
	HashMap<String, String> inhproperterties = new HashMap<>();
	HashMap<String, CfgClass> classes = new HashMap<>();
	HashMap<String, CfgClass> inhclasses = new HashMap<>();
	
	private boolean _rebuilt;
	
	public CfgClass(String clazzstr) {
		String[] arr = clazzstr.split(",");
		if (arr.length == 2) {
			inhereted = arr[1];
		}
		name = clazzstr.split(":")[0].split(",")[0];
		try {
			CfgClass preclazz = Test.clazzes.get(clazzstr.substring(0,clazzstr.lastIndexOf("/")));
			if(preclazz!=null)preclazz.classes.put(arr[0], this);
		}catch (Exception e) {
			System.out.println("Error on "+clazzstr);
		}
		
		

			
		// Test.clazzes.get("")
	}
	
	public String getProperty(String key) {
		return properterties.getOrDefault(key, inhproperterties.get(key));
	}
	
	public CfgClass getClass(String key) {
		return classes.getOrDefault(key, inhclasses.get(key));
	}
	
	public ArrayList<String> allKeys(){
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(properterties.keySet());
		keys.addAll(inhproperterties.keySet());
		return keys;
	}
	
	public ArrayList<String> allClassKeys(){
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(classes.keySet());
		keys.addAll(inhclasses.keySet());
		return keys;
	}

	public void rebuild() {
		if(!_rebuilt) {
			if(Test.clazzes.get(inhereted)!=null) {
				Test.clazzes.get(inhereted).rebuild();
				for (String key : Test.clazzes.get(inhereted).allKeys()) {
					if(!properterties.containsKey(key))
						inhproperterties.put(key, Test.clazzes.get(inhereted).getProperty(key));
				}
				for (String key : Test.clazzes.get(inhereted).allClassKeys()) {
					if(!classes.containsKey(key))
						inhclasses.put(key, Test.clazzes.get(inhereted).getClass(key));
				}
			}
			_rebuilt = true;
		}
	}
	
	

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("Name: " + name+"\r\n");
		b.append("Inhereted: " + inhereted+"\r\n");
		for (Entry<String, String> entry : properterties.entrySet()) {
			b.append("new  "+entry.getKey()+"="+entry.getValue()+"\r\n");
		}
		b.append("\r\n");
		for (Entry<String, String> entry : inhproperterties.entrySet()) {
			b.append("old  "+entry.getKey()+"="+entry.getValue()+"\r\n");
		}
		b.append("\r\n");
		b.append(allClassKeys()+"\r\n");
		b.append("Subclasscount: "+classes.size()+" "+inhclasses.size());
		return b.toString();
	}

}
