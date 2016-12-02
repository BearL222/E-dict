
package application.server;

//词典api全部完成
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
			URL url = null;
			// 0：有道；1：百度；2：金山
			if (provider == 0) {
				url = new URL("http://fanyi.youdao.com/openapi.do");
			} else if (provider == 1) {
				url = new URL("http://api.fanyi.baidu.com/api/trans/vip/translate");
			} else if (provider == 2) {
				url = new URL("http://dict-co.iciba.com/api/dictionary.php?w=" + this.word
						+ "&type=json&key=DCC1EAA89FD3B5C540FDA96B080B5F72");
			}
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

	private String arrangeOutput(String result) throws UnsupportedEncodingException {
		String output = "";
		if (provider == 0) {
			// 有道
			String[] tmp = result.split(":");
			output += tmp[1].substring(2, tmp[1].indexOf("basic") - 4) + "\n";
			output += "美音：" + tmp[3].substring(1, tmp[3].indexOf("phonetic") - 3) + "\n";
			output += "发音：" + tmp[4].substring(1, tmp[4].indexOf("uk-phonetic") - 3) + "\n";
			output += "英音：" + tmp[5].substring(1, tmp[5].indexOf("explains") - 3) + "\n";
			output += "意思：" + tmp[6].substring(1, tmp[6].indexOf("query") - 4).replace("\"", "")+"\n";
			output+="网络:\n";
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
			// 百度
			output+=uni2char(result.substring(result.indexOf("dst")+6,result.indexOf("\"",result.indexOf("dst")+7)));
		} else if (provider == 2) {
			// 金山
			output += jinshanCut(result, "word_name");
			if (result.charAt(result.indexOf("word_past") + 11) == '[') {
				// 动词
				output += "第三人称：" + jinshanCut(result, "word_third");
				output += "过去式：" + jinshanCut(result, "word_past");
				output += "完成时：" + jinshanCut(result, "word_done");
				output += "进行时：" + jinshanCut(result, "word_ing");
			}
			if (result.charAt(result.indexOf("word_pl") + 9) == '[') {
				// 名词
				output += "单词复数：" + jinshanCut(result, "word_pl");
			}
			if (result.charAt(result.indexOf("word_er") + 9) == '[') {
				// 形容词
				output += "比较级：" + jinshanCut(result, "word_er");
				output += "最高级：" + jinshanCut(result, "word_est");
			}
			// 音标
			output += "英音：" + jinshanCut(result, "ph_en");
			output += "美音：" + jinshanCut(result, "ph_am");
			// 意思
			output += "意思：" + jinshanCut(result, "part\"");
			output += jinshanCut(result, "means");
		}
		return output;
	}
	
	// 金山 截取所需部分
	private String jinshanCut(String strAll, String partName) {
		if (partName == "part\"") {
			return jinshanPh(strAll.substring(strAll.indexOf(partName) + partName.length() + 2,
					strAll.indexOf("\"", strAll.indexOf(partName) + partName.length() + 3))) + " ";
		}
		if (strAll.charAt(strAll.indexOf(partName) + partName.length() + 2) == '\"') {
			return jinshanPh(strAll.substring(strAll.indexOf(partName) + partName.length() + 3,
					strAll.indexOf("\"", strAll.indexOf(partName) + partName.length() + 4))) + "\n";
		}
		String result = "";
		String[] partList = strAll
				.substring(strAll.indexOf(partName) + partName.length() + 3,
						strAll.indexOf("]", strAll.indexOf(partName) + partName.length() + 4))
				.replace("\"", "").split(",");
		if (partName != "means") {
			for (int i = 0; i < partList.length - 1; i++) {
				result += partList[i] + ",";
			}
			result += partList[partList.length - 1];
		} else {
			for (int i = 0; i < partList.length - 1; i++) {
				result += uni2char(partList[i]) + ",";
			}
			result += uni2char(partList[partList.length - 1]);
		}
		return result + "\n";
	}

	// unicode码变为汉字
	private String uni2char(String uniVal) {
		String charVal = "";
		String[] seperateChar = uniVal.replaceAll("\\\\", "").split("u");
		for (int i = 1; i < seperateChar.length; i++) {
			int numHEX = Integer.valueOf(seperateChar[i].substring(0, seperateChar[i].length()), 16);
			charVal += (char) numHEX;
		}
		return charVal;
	}

	// 金山 音标
	private String jinshanPh(String ph) {
		String result = "";
		for (int i = 0; i < ph.length(); i++) {
			if (ph.charAt(i) == '\\') {
				result += uni2char(ph.substring(i, i + 6));
				i += 5;
			} else {
				result += ph.substring(i, i + 1);
			}
		}
		return result;
	}
}
