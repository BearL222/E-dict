import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.security.*;

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
			meaning = arrangeOutput(builder.toString());
			// meaning = builder.toString();

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

	private String arrangeOutput(String result) {
		String output = "";
		if (provider == 0) {
			int p = 0;
			int q = 0;
			int r = 0;
			q = result.indexOf("basic");
			output += result.substring(17, q - 4) + "\n";
			p = result.indexOf("explains");
			q = result.indexOf("query");
			output += result.substring(p + 12, q - 5) + "\n\n网络:\n";

			p = result.indexOf("value");
			q = result.indexOf("key");
			r = result.indexOf("\"", q + 6);
			int last = result.lastIndexOf("value");

			while (p <= last && p > 10) {
				output += result.substring(q + 6, r) + "\n";
				output += result.substring(p + 8, q - 3)+"\n\n";

				p = result.indexOf("value", p+1);
				q = result.indexOf("key", q+1);
				r = result.indexOf("\"", q + 6);
			}

		} else if (provider == 1) {
			String[] tmp = result.split(":");
			output += tmp[tmp.length - 1].substring(1, tmp[tmp.length - 1].length() - 4);

		} else if (provider == 2) {

		}

		return output;
	}
}
