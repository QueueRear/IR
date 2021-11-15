import java.util.Scanner;

import static indexManager.IndexManager.createIndex;
import static searchManager.SearchManager.searchIndex;

public class Service {
    public static void main(String[] args) throws Exception {
        String filepath = "";
        System.out.println("请输入数据集路径：");
        Scanner s1 = new Scanner(System.in);
        filepath = s1.next();

        String dirPath = "D:\\dir";
        System.out.println("是否指定索引库路径？ Y/N");
        Scanner s2 = new Scanner(System.in);
        if (s2.next().equals("Y")) {
            System.out.println("请输入索引库路径：");
            Scanner s = new Scanner(System.in);
            dirPath = s.nextLine();
        }

        System.out.println("索引库创建中...");
        // 创建索引库
        long startTime = System.currentTimeMillis();
        createIndex(filepath, dirPath);
        long endTime = System.currentTimeMillis();
        System.out.println("创建索引耗时：" + (endTime - startTime) + " ms.\n");

        while (true){
            System.out.println("请输入检索语句：\n" +
                    "默认搜索域为“text”，如需更改请以“搜索域:搜索语句”的格式输入\n" +
                    "输入“quit”退出");
            Scanner s3 = new Scanner(System.in);
            String q = s3.nextLine();
            if (q.equals("quit")) {
                break;
            }

            System.out.println("请输入需要输出的结果数：");
            Scanner s4 = new Scanner(System.in);
            int n = s4.nextInt();

            // 显示查询语句
            System.out.println("q = " + q + "\n");
            // 搜索索引库
            startTime = System.currentTimeMillis();
            searchIndex(dirPath, q, n);
            endTime = System.currentTimeMillis();
            System.out.println("搜索耗时：" + (endTime - startTime) + " ms.\n");
        }
    }
}
