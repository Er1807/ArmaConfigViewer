import java.util.HashMap;
import java.util.Map.Entry;

public class CfgClass {

	String inhereted = "";
	HashMap<String, String> properterties = new HashMap<>();
	HashMap<String, CfgClass> classes = new HashMap<>();

	public CfgClass(String clazzstr) {
		String[] arr = clazzstr.split(",");
		if (arr.length == 2) {
			inhereted = arr[1];
		}
		CfgClass preclazz = Test.clazzes.get(clazzstr.substring(0,clazzstr.lastIndexOf("/")));
		if(preclazz!=null)preclazz.classes.put(arr[0], this);

			
		// Test.clazzes.get("")
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		
		b.append("Inhereted: " + inhereted+"\r\n");
		for (Entry<String, String> entry : properterties.entrySet()) {
			b.append("  "+entry.getKey()+"="+entry.getValue()+"\r\n");
		}
		
		b.append("Subclasscount: "+classes.size());
		return b.toString();
	}

}
