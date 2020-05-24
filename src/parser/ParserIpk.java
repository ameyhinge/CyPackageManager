package parser;

import java.io.BufferedReader;
import java.io.FileReader;

import objects.PackageObject;

public class ParserIpk {
	static String APP_NAME = new String();
	static String VERSION = new String();
	static boolean IS_DEPENDENCIES = false;
	static boolean BLOCK_START = false;

	public void parseIpkFile(boolean mainFile) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("resources/sample.ipk"));
			String currentLine = null;
			while ((currentLine = br.readLine()) != null) {
				if (IS_DEPENDENCIES == false || APP_NAME.equals("") || VERSION.equals("")) {
					checkForGlobalProp(currentLine, mainFile);
					if (IS_DEPENDENCIES == true) {
						continue;
					}
				}
				if (IS_DEPENDENCIES == true) {
					if (currentLine.charAt(0) == '{') {
						BLOCK_START = true;
						continue;
					}
					if (currentLine.charAt(0) == '}') {
						BLOCK_START = false;
						break;
					}
					if (BLOCK_START == true) {
						PackageObject.DEPENDENCY_MAP.putAll(ParserUtils.getDependencyProps(currentLine));
					}
				}

			}
			for (String k : PackageObject.DEPENDENCY_MAP.keySet()) {
				System.out.println(k + " " + PackageObject.DEPENDENCY_MAP.get(k).getPackageVersion() + " "
						+ PackageObject.DEPENDENCY_MAP.get(k).getPackageUrl() + " "
						+ PackageObject.DEPENDENCY_MAP.get(k).getPackageBranch());
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkForGlobalProp(String currentLine, boolean mainFile) {
		int colonIndex = currentLine.indexOf(":");
		if (colonIndex > -1) {
			if (APP_NAME.equals("") && currentLine.indexOf("name") > -1) {
				currentLine = currentLine.substring(colonIndex + 1, currentLine.length());
				currentLine = ParserUtils.quoteHandler(currentLine);
				if (mainFile == true) {
					PackageObject.APP_NAME = currentLine;
				}
				APP_NAME = currentLine;
			} else if (VERSION.equals("") && currentLine.indexOf("version") > -1) {
				currentLine = currentLine.substring(colonIndex + 1, currentLine.length());
				currentLine = ParserUtils.quoteHandler(currentLine);
				if (mainFile == true) {
					PackageObject.VERSION = currentLine.toString();
				}
				VERSION = currentLine.toString();
			} else if (currentLine.indexOf("dependencies") > -1) {
				IS_DEPENDENCIES = true;
			}
		}
	}
}
