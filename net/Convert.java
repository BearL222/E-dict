
package net;

public final class Convert {
	public static String[] getRecv(String strRead) {
		if (strRead == null) return null;
		return strRead.replaceAll("<br>", "\n").split("\\\\n");
	}
	
	public static String getSend(String[] strPrint) {
		if (strPrint == null) return null;
		return String.join("\\n", strPrint).replaceAll("\\r\\n?", "\n")
			.replaceAll("\\n", "<br>");
	}
	
	public static void main(String[] args) {
		String[] msg = new String[] { "1\n111", "2\n222", "3\n333", };
		
		String convert = getSend(msg);
		System.out.println(convert);
		
		msg = getRecv(convert);
		System.out.println(msg.length);
		System.out.println(msg[0]);
		System.out.println(msg[1]);
		System.out.println(msg[2]);
		
	}
}
