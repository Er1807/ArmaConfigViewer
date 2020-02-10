import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static final String clazz = "TB_ACV_Class";
	public static final String property = "TB_ACV_Property_Non_Inhereted";
	public static Pattern allmatch = Pattern.compile(".{9}\\[\"(.+?)\",(.*?)\\]$");

	public static HashMap<String, CfgClass> clazzes = new HashMap<String, CfgClass>();

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		BufferedReader reader = new BufferedReader(
				new FileReader("C:/Users/Er018u.MUCLVAD1/Documents/Github/ArmaConfigViewer/cfgdump.rpt"));
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
			if (counter % 1000 == 0)
				System.out.println("Finished " + counter);

		}
		reader.close();
		System.out.println("Finished Reading\r\nRebuilding");
		for (CfgClass cfgClass : clazzes.values()) {
			cfgClass.rebuild();
		}
		System.out.println("Finished Rebuilding");
		System.out.println("Time took: " + (System.currentTimeMillis() - start));
		System.out.println("Classes: " + clazzes.size());

		try {
			System.out.println("Exporting");
			counter = 0;
			String home = "C:/Users/Eric/Documents/Github/ArmaConfigViewer/files";
			new File(home).mkdirs();
			for (String key : clazzes.keySet()) {
				String path = key.replace("bin\\config.bin", "");
				new File(home + path+ ".properties").getParentFile().mkdirs();
				if(!clazzes.get(key).classes.isEmpty())
					new File(home + path).mkdirs();
				new File(home + path+ ".properties").createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(home + path+ ".properties"));
				writer.write(clazzes.get(key).toString());
				writer.close();
				
				counter++;
				if (counter % 1000 == 0)
					System.out.println("Exportet " + counter);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private static String removeflSquareBrackets(String clazzstr) {
		return clazzstr.substring(1, clazzstr.length() - 1);
	}

}
