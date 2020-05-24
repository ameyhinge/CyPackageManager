package main;

import parser.ParserIpk;

public class Main {

	public static void main(String[] args) {
		ParserIpk pIpk = new ParserIpk();
		pIpk.parseIpkFile(true);
	}
}
