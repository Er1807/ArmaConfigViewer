import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static String clazz = "TB_ACV_Class";
	public static String property = "TB_ACV_Property_Non_Inhereted";
	public static Pattern allmatch = Pattern.compile(".{9}\\[\"(.+?)\",(.*?)\\]$");

	public static HashMap<String, CfgClass> clazzes = new HashMap<String, CfgClass>();

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Eric\\Desktop\\ammodump.rpt"));
		String str = "";

		while ((str = reader.readLine()) != null) {

			Matcher match = allmatch.matcher(str);

			if (match.matches()) {
				// System.out.println(match.group(1) + " " + match.group(2));

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

		}
		for (CfgClass cfgClass : clazzes.values()) {
			cfgClass.rebuild();
		}
		System.out.println(clazzes.get("bin\\config.bin/CfgAmmo/M_AT"));
		System.out.println(clazzes.size());
		reader.close();
		System.out.println(System.currentTimeMillis()-start);
	}

	private static String removeflSquareBrackets(String clazzstr) {
		return clazzstr.substring(1, clazzstr.length() - 1);
	}

}
