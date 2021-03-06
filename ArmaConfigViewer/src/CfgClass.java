import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class CfgClass {

	String name = "";
	String inhereted = "";
	HashMap<String, String> properterties = new HashMap<>();
	HashMap<String, String> inhproperterties = new HashMap<>();
	HashMap<String, String> inhpropertertiesSource = new HashMap<>();
	HashMap<String, CfgClass> classes = new HashMap<>();
	HashMap<String, CfgClass> inhclasses = new HashMap<>();
	HashMap<String, String> inhclassesSource = new HashMap<>();
	
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
		CfgClass inheretedClass = Test.clazzes.get(inhereted);
			if(inheretedClass!=null) {
				inheretedClass.rebuild();
				for (String key : inheretedClass.allKeys()) {
					if(!properterties.containsKey(key)) {
						inhproperterties.put(key, inheretedClass.getProperty(key));
						inhpropertertiesSource.put(key, inheretedClass.inhpropertertiesSource.getOrDefault(key, inheretedClass.name));
					}
				}
				for (String key : inheretedClass.allClassKeys()) {
					if(!classes.containsKey(key))
						inhclasses.put(key, inheretedClass.getClass(key));
						inhclassesSource.put(key, inheretedClass.inhclassesSource.getOrDefault(key, inheretedClass.name));
				}
			}
			_rebuilt = true;
		}
	}
	
	public void getParents(ArrayList<String> toAdd){

		if(Test.clazzes.containsKey(inhereted)) {
			Test.clazzes.get(inhereted).getParents(toAdd);
		}
		toAdd.add(name.replace("bin\\config.bin/", ""));
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		if(Test.clazzes.containsKey(inhereted)) {
			ArrayList<String> parents = new ArrayList<String>();
			Test.clazzes.get(inhereted).getParents(parents);
			b.append("Parent: [");
			for (String name : parents) {
				b.append(name+",");
			}
			b.append("]\r\n");
		}
		
		for (Entry<String, String> entry : properterties.entrySet()) {
			b.append(entry.getKey()+" = "+entry.getValue()+"\r\n");
		}
		for (Entry<String, String> entry : inhproperterties.entrySet()) {
			b.append(inhpropertertiesSource.get(entry.getKey()).replace("bin\\config.bin/", "")+": "+entry.getKey()+" = "+entry.getValue()+"\r\n");
		}
		for (Entry<String, CfgClass> entry : classes.entrySet()) {
			b.append(entry.getKey()+" = class\r\n");
		}
		for (Entry<String, CfgClass> entry : inhclasses.entrySet()) {
			b.append(inhclassesSource.get(entry.getKey()).replace("bin\\config.bin/", "")+": "+entry.getKey().replace("bin\\config.bin/", "")+" = class\r\n");
		}
		return b.toString();
	}

}
