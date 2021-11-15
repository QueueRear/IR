package Files;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static dom4j.ExtractTest.*;

public class openAllDocs {
    static ArrayList<ArrayList<String>> formatted = new ArrayList<>();

    public static void readFiles(String filepath) {
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {// 是目录文件
//                System.out.println("逮到一个文件夹");
//                System.out.println("path=" + filepath);
                String[] fileList = file.list();
                for (String f : fileList) {
                    if (f.equals(".DS_Store")) {
                        continue;
                    }
                    String newPath = filepath + "\\" + f;// 文件夹中文件/下一级文件夹路径
                    readFiles(newPath);
                }
            }
            else {
//                System.out.println("逮到一个文件");
//                System.out.println("path=" + filepath);
                extractContent(filepath);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<String>> getFormatted(String filepath) {
        readFiles(filepath);
        return formatted;
    }

    @Test
    public static void main(String[] args) {
        try {
            readFiles("D:\\Users\\你爹的电脑\\Desktop\\test.txt");
            System.out.println(formatted);
        }
        catch (Exception e) {
        }
    }
}
