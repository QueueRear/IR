package dom4j;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExtractTest {
    static ArrayList<ArrayList<String>> extracted = new ArrayList<>();

    private static void modifier (File file) {
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

    @Test
    public static void extractContent(String filepath) {// 提取 xml 标签里的内容
        SAXReader saxReader = new SAXReader();
        ArrayList<String> formedDoc = new ArrayList<>();
        File file = new File(filepath);
        try {
            Document doc = saxReader.read(file);// 创建Document对象
            Element rootElement = doc.getRootElement();// 获取根节点
            List<Element> subElementList = rootElement.elements();// 获取当前标签的子标签，返回列表
            for (Element subElement : subElementList) {
                formedDoc.add(subElement.getText().trim());// 去掉首尾空格
            }
        } catch (DocumentException e) {
            modifier(file);// 解析器出错，需要处理掉”&“
            extractContent(filepath);
        }
//        System.out.println(formedDoc.get(0));
//        System.out.println(formedDoc.get(1));
//        System.out.println(formedDoc.get(2));
//        System.out.println(formedDoc.get(3));
        extracted.add(formedDoc);
    }

    private static void readFiles(String filepath) {
        try {
            File file = new File(filepath);// 给定一个路径（可以是文件或者文件夹）
            if (file.isDirectory()) {// 是目录文件
                String[] fileList = file.list();// 列出子文件/文件夹
                for (String f : fileList) {// 依次访问每个子文件夹
                    if (f.equals(".DS_Store")) {
                        continue;
                    }
                    String newPath = filepath + "\\" + f;// 文件夹中文件/下一级文件夹路径
                    readFiles(newPath);
                }
            }
            else {
                extractContent(filepath);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> getExtracted(String filepath) {
        readFiles(filepath);
        return extracted;
    }

    public static void main(String[] args) {
        try {
            String filepath = "D:\\Users\\你爹的电脑\\Desktop\\新建文件夹";
            extractContent(filepath);

//            File file = new File(filepath);
//            String[] fileList = file.list();
//            for (String f : fileList) {
//                String docPath = filepath + "\\" + f;// 文件路径
//                System.out.println("逮到一个文件");
//                System.out.println("path=" + docPath);
//                content(docPath);
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
