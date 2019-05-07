import java.util.ArrayList;

public class CfgCompare {

	public static void compare(CfgClass o1, CfgClass o2) {
		ArrayList<String> uniqueKeys = new ArrayList<String>();
		ArrayList<String> allkeys = new ArrayList<String>();
		allkeys.addAll(o1.allKeys());
		allkeys.addAll(o2.allKeys());

		for (String key : allkeys) {
			if (!o2.allKeys().contains(key) || !o1.allKeys().contains(key)
					|| !o1.getProperty(key).equals(o2.getProperty(key))) {
				if (!uniqueKeys.contains(key))
					uniqueKeys.add(key);
			}	
		}
		for (String key : uniqueKeys) {
			System.out.println(String.format("%-35s =  %-30s || %-30s ", key, o1.getProperty(key), o2.getProperty(key)));
		}
	}

}
