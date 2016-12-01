
package application.server;

//有道和百度翻译完�?
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.security.*;
import java.net.URLDecoder;

class Search {
	private int provider;
	private String word;
	private String meaning;

	char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	Search(int provider, String word) {
		this.provider = provider;
		this.word = word;
		fun();
	}

	public String getMeaning() {
		return meaning;
	}

	private void fun() {
		try {

			URL[] url = new URL[3];
			url[0] = new URL("http://fanyi.youdao.com/openapi.do");
			url[1] = new URL("http://api.fanyi.baidu.com/api/trans/vip/translate");
			url[2] = new URL("http://dict-co.iciba.com/api/dictionary.php");

			HttpURLConnection connection = (HttpURLConnection) url[provider].openConnection();
			connection.addRequestProperty("encoding", "UTF-8");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			java.io.OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);

			if (provider == 0) {
				bw.write("keyfrom=e-dictionary&key=1341070078&type=data&doctype=json&version=1.1&q=" + this.word);
			} else if (provider == 1) {
				String appid = "20161130000033057";
				String baiduKey = "Ym_JMt619EaSp2y8czZC";
				int salt = new Random().nextInt(1000000000);
				String s = appid + this.word + salt + baiduKey;
				bw.write("q=" + this.word + "&from=en&to=zh&appid=" + appid + "&salt=" + salt + "&sign=" + getSign(s));
			} else if (provider == 2) {
				bw.write("w=" + this.word + "&key=DCC1EAA89FD3B5C540FDA96B080B5F72");
			}
			bw.flush();

			java.io.InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}

			bw.close();
			osw.close();
			os.close();
			br.close();
			isr.close();
			is.close();

			// 处理输出
			//meaning = arrangeOutput(builder.toString());
			meaning = builder.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// md5加密(baidu)
	private String getSign(String s) throws NoSuchAlgorithmException {
		byte[] strByte = s.getBytes();
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strByte);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	private String arrangeOutput(String result) throws UnsupportedEncodingException {
		String output = "";
		if (provider == 0) {
			String[] tmp = result.split(":");
			output += tmp[1].substring(2, tmp[1].indexOf("basic") - 4) + "\n";
			output += "美音�?" + tmp[3].substring(1, tmp[3].indexOf("phonetic") - 3) + "\n";
			output += "发音�?" + tmp[4].substring(1, tmp[4].indexOf("uk-phonetic") - 3) + "\n";
			output += "英音�?" + tmp[5].substring(1, tmp[5].indexOf("explains") - 3) + "\n";
			output += "意思：" + tmp[6].substring(1, tmp[6].indexOf("query") - 4).replace("\"", "") + "\n网络:\n";
			String webVal = result.substring(result.indexOf("web") + 6, result.length() - 2).replace("[", "")
					.replace("]", "").replaceAll("[{}\"]", "");
			tmp = webVal.split(":");
			for (int i = 1; i < tmp.length; i += 2) {
				if (tmp[i + 1].indexOf(",") != -1) {
					output += tmp[i + 1].substring(0, tmp[i + 1].indexOf(","));
				} else {
					output += tmp[i + 1];
				}
				output += "\n" + tmp[i].substring(0, tmp[i].lastIndexOf(",")) + "\n";
			}
		} else if (provider == 1) {
			String[] tmp = result.split(":");
			String charVal = tmp[tmp.length - 1].substring(1, tmp[tmp.length - 1].length() - 4);
			String[] seperateChar = charVal.replaceAll("\\\\", "").split("u");
			for (int i = 1; i < seperateChar.length; i++) {
				int numHEX = Integer.valueOf(seperateChar[i].substring(0, seperateChar[i].length()), 16);
				output += (char) numHEX;
			}
		} else if (provider == 2) {

		}
		return output;
	}
}
