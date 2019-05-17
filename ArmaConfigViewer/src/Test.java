import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static final String clazz = "TB_ACV_Class";
	public static final String property = "TB_ACV_Property_Non_Inhereted";
	public static Pattern allmatch = Pattern.compile(".{9}\\[\"(.+?)\",(.*?)\\]$");

	public static HashMap<String, CfgClass> clazzes = new HashMap<String, CfgClass>();

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Eric\\Desktop\\cfgdump.rpt"));
		String str = "";
		int counter = 0;
		while ((str = reader.readLine()) != null) {

			Matcher match = allmatch.matcher(str);

			if (match.matches()) {
				if (match.group(1).equals(clazz)) {
					String clazzstr = match.group(2);
					clazzstr = removeflSquareBrackets(clazzstr);
					if (!clazzes.containsKey(clazzstr.split(":")[0].split(",")[0])) {
						clazzes.put(clazzstr.split(":")[0].split(",")[0], new CfgClass(clazzstr));
					}

				} else if (match.group(1).equals(property)) {
					String propertystr = match.group(2);
					propertystr = removeflSquareBrackets(propertystr);
					String base = propertystr.substring(0, propertystr.lastIndexOf("/", propertystr.indexOf(",")));
					String second = propertystr.substring(propertystr.lastIndexOf("/", propertystr.indexOf(",")) + 1);
					String key = second.split(",")[0];
					String value = second.substring(second.indexOf(",") + 1);

					try {
						clazzes.get(base).properterties.put(key, value);
					} catch (Exception e) {
						System.out.println(
								propertystr + "     " + base + "    " + second + "    " + key + "       " + value);
					}
				}

			}
			counter++;
			if(counter%1000==0)System.out.println("Finished "+counter);

		}
		reader.close();
		System.out.println("Finished Reading\r\nRebuilding");
		for (CfgClass cfgClass : clazzes.values()) {
			cfgClass.rebuild();
		}
		System.out.println("Finished Rebuilding");
		System.out.println("Time took: " + ( System.currentTimeMillis()-start));
		System.out.println("Classes: " + clazzes.size());
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				String sr = sc.nextLine();
				if(sr.startsWith("find ")) {
					sr = sr.substring(5);
					System.out.println("Found:");
					for (String key : clazzes.keySet()) {
						if(key.contains(sr)){
							System.out.println(key);
						}
					}
				}else if(sr.startsWith("compare ")){
					sr = sr.substring(8);
					CfgCompare.compare(clazzes.get(sr.split(";")[0]), clazzes.get(sr.split(";")[1]));
				}else if(sr.startsWith("print ")){
					sr = sr.substring(6);
					System.out.println(clazzes.get(sr.split(";")[0]));
				}
			} catch (Exception e) {
			}
		}
		//CfgCompare.compare(clazzes.get("bin\\config.bin/CfgWeapons/rhs_weap_rshg2"), clazzes.get("bin\\config.bin/CfgWeapons/rhs_weap_rpg18"));
		
		
		
	}

	private static String removeflSquareBrackets(String clazzstr) {
		return clazzstr.substring(1, clazzstr.length() - 1);
	}

}
