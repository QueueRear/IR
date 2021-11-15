package contentExtractor;

import java.io.*;

public class Preprocess {
    // 修改正文中的 ”&“
    public static void modifier (File file) {
        // 读取文件内容
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String str = "";
        try {
            br = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = br.readLine()) != null) {
                // 修改文本
                sb.append(tempStr.replace("&", "&amp;"));
                sb.append(System.getProperty("line.separator"));
            }
            br.close();
            str = sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    br = null;
                }
            }
        }
        // 写回文件
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(str);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                }
                catch (Exception e) {
                    bw = null;
                }
            }
        }
    }
}
