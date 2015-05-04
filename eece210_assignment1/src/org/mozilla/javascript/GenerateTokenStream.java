package org.mozilla.javascript;

import java.io.*;

public class GenerateTokenStream {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter JavaScript File To Parse: ");
			System.out.flush();
			String js_file = br.readLine();
			//TokenStream ts = new TokenStream(new StringReader("\\u0041 = y; x = 1 + 9; y = x++; y = this.a;"), "",1);
			TokenStream ts = new TokenStream(new FileReader(js_file), "", 1);
			int token;
			
			String t_name = null;
			token = ts.getToken();
			//System.out.print(token);
			while (Token.typeToName(token) != "EOF") {
				t_name = Token.typeToName(token);
				//System.out.println(t_name);
				if (t_name.equals("NAME")) {
					System.out.print(t_name + ": ");
					System.out.println(ts.getString());
				}
				else if (t_name.equals("NUMBER")) {
					System.out.print(t_name + ": ");
					System.out.println(ts.getNumber());
				}
				else {
					System.out.println(t_name);
				}
				token = ts.getToken();
			}
		} catch (IOException ie) {
			System.out.println("Error reading file");
		}
	}
}