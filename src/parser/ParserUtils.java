package parser;

import java.util.HashMap;
import java.util.Map;

import objects.Dependency;

public class ParserUtils {
	private static StringBuilder sb = new StringBuilder();

	public static String quoteHandler(String str) {
		sb.setLength(0);
		sb.append(str);
		while (sb.charAt(0) == ' ' || sb.charAt(0) == '\t') {
			sb.deleteCharAt(0);
		}
		while (sb.charAt(sb.length() - 1) == ' ') {
			sb.deleteCharAt(sb.length() - 1);
		}
		if (sb.charAt(0) == '"' && sb.charAt(sb.length() - 1) == '"') {
			sb.deleteCharAt(0);
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	private static StringBuilder depName = new StringBuilder();
	private static StringBuilder depVal = new StringBuilder();
	private static boolean colonFound = false;
	private static String[] res = new String[2];

	private static String[] breakDependencyString(String str) {
		depName.setLength(0);
		depVal.setLength(0);
		colonFound = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ':') {
				colonFound = true;
				continue;
			}
			if (colonFound == false) {
				depName.append(str.charAt(i));
			} else if (colonFound == true) {
				depVal.append(str.charAt(i));
			}
		}
		res[0] = quoteHandler(depName.toString());
		if (!(depVal.length() == 0)) {
			res[1] = quoteHandler(depVal.toString());
		}
		return res;
	}

	private static Map<String, Dependency> depMap = new HashMap<String, Dependency>();

	private static String[] keyVal = new String[2];
	private static StringBuilder version = new StringBuilder();
	private static StringBuilder url = new StringBuilder();
	private static StringBuilder branch = new StringBuilder();
	private static int currIndex = -1;

	public static Map<String, Dependency> getDependencyProps(String str) {
		depMap.clear();
		Dependency dep = new Dependency();
		version.setLength(0);
		url.setLength(0);
		branch.setLength(0);
		currIndex = -1;

		keyVal = breakDependencyString(str);
		if (keyVal[1] == null) {
			dep.setPackageVersion("");
			dep.setPackageUrl("");
			dep.setPackageBranch("");
			depMap.put(keyVal[0], dep);
			return depMap;
		}
		currIndex = keyVal[1].indexOf("ver~");
		if (currIndex > -1) {
			currIndex += 4;
			while (currIndex < keyVal[1].length()) {
				if (str.charAt(currIndex) == ' ') {
					break;
				} else {
					version.append(keyVal[1].charAt(currIndex));
				}
				currIndex++;
			}
		}
		currIndex = keyVal[1].indexOf("url~");
		if (currIndex > -1) {
			currIndex += 4;
			while (currIndex < keyVal[1].length()) {
				if (keyVal[1].charAt(currIndex) == ' ') {
					break;
				} else {
					url.append(keyVal[1].charAt(currIndex));
				}
				currIndex++;
			}
		}
		currIndex = keyVal[1].indexOf("branch~");
		if (currIndex > -1) {
			currIndex += 7;
			while (currIndex < keyVal[1].length()) {
				if (keyVal[1].charAt(currIndex) == ' ') {
					break;
				} else {
					branch.append(keyVal[1].charAt(currIndex));
				}
				currIndex++;
			}
		}
		dep.setPackageVersion(version.toString());
		dep.setPackageUrl(url.toString());
		dep.setPackageBranch(branch.toString());
		depMap.put(keyVal[0], dep);
		return depMap;
	}

}
