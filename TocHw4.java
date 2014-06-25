import java.net.*;
import java.io.*;
import org.json.*;

public class TocHw4 {
	public static void main(String[] args) throws Exception {

		String url = args[0];

		try {

			int cont = 0, year, month, price, MAXcont = 3, n = 0,j = 0;
			int[][] trapri = new int[2000][100];
			int[] Maxtrad = new int[100];
			String[] roadname = new String[2000];
			String address;

			TocHw4 hw4 = new TocHw4();
			hw4.getURLstr(url);
			JSONArray jsonRealPrice = new JSONArray(new JSONTokener(
					new FileReader(new File("URL.txt"))));
			
			for (int i = 0; i < jsonRealPrice.length(); i++) {
				JSONObject object;
				object = jsonRealPrice.getJSONObject(i);
				address = object.getString("土地區段位置或建物區門牌");
				price = Integer.valueOf(object.optString("總價元"));
				year = Integer.valueOf(object.optString("交易年月"));
				month = (104 - year / 100) * 12 - year % 100;

				
				if (address.indexOf('路') != -1)
					address = address.substring(0, address.indexOf('路') + 1);
				else if (address.indexOf('大') == address.indexOf('道') - 1)
					address = address.substring(0, address.indexOf('道') + 1);
				else if (address.indexOf('街') != -1)
					address = address.substring(0, address.indexOf('街') + 1);
				else if (address.indexOf('巷') != -1)
					address = address.substring(0, address.indexOf('巷') + 1);


				for (j = cont - 1; j >= -1; j--) {
					if (j == -1) {
						j = cont++;
						roadname[j] = address;
						trapri[j][2] = price;
						break;
					} else if (address.equalsIgnoreCase(roadname[j]))
						break;
				}

				if (trapri[j][month] == 0) {
					trapri[j][month] = 1;
					trapri[j][0]++;
				}
				if (price > trapri[j][1])
					trapri[j][1] = price;
				else if (price < trapri[j][2])
					trapri[j][2] = price;
			}


			for (int i = 0; i < cont; i++)
				if (trapri[i][0] > MAXcont) {
					MAXcont = trapri[i][0];
					Maxtrad[n = 0] = i;
				} else if (trapri[i][0] == MAXcont)
					Maxtrad[++n] = i;

			for (int i = 0; i <= n; i++)
				System.out.println(roadname[Maxtrad[i]] + ", 最高成交價: "
						+ trapri[Maxtrad[i]][1] + ", 最低成交價: " + trapri[Maxtrad[i]][2]);

		} catch (IOException e) {
		}
	}

	public void getURLstr(String strURL) {
		try {
			URL url_address = new URL(strURL);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					url_address.openStream(), "UTF-8"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("URL.txt",
					false));
			String oneLine = null;

			while ((oneLine = br.readLine()) != null) {
				bw.write(oneLine);
			}
			br.close();
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}