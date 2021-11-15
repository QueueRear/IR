package contentExtractor;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static contentExtractor.Preprocess.modifier;

public class Extract {// 提取格式化后的检索内容
    static ArrayList<ArrayList<String>> extracted = new ArrayList<>();

    private static void extractContent(String filepath) {// 提取 xml 标签里的内容
        File file = new File(filepath);
        try {
            ArrayList<String> formedDoc = new ArrayList<>();
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(file);// 创建Document对象
            Element rootElement = doc.getRootElement();// 获取根节点
            List<Element> subElementList = rootElement.elements();// 获取当前标签的子标签，返回列表
            for (Element subElement : subElementList) {
                formedDoc.add(subElement.getText().trim());// 去掉首尾空格
            }
            extracted.add(formedDoc);
        }
        catch (DocumentException e) {
            modifier(file);// 解析器出错，需要处理掉”&“
            extractContent(filepath);// 重试
        }
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
}
