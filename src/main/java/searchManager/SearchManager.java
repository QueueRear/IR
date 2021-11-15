package searchManager;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class SearchManager {
    public static void searchIndex(String dirPath, String q, int n) throws ParseException, IOException {
        // 创建查询对象，参数一：默认查询域；参数二：使用的分词器（对query使用）
        QueryParser queryParser = new QueryParser("text", new StandardAnalyzer());

        // 设置搜索关键词
        Query query = queryParser.parse(q);
        String[] s = q.split(":");// 检查是否搜索别的域
        if (s.length > 1) {
            query = new TermQuery(new Term(s[0], s[1]));
        }

        // 创建目录对象，指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get(dirPath));

        // 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);

        // 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(new MySimilarity());

        // 搜索并返回结果
        System.out.println("共找到" + indexSearcher.count(query) + "条结果。\n");
        TopDocs topDocs = indexSearcher.search(query, n);

        // 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 遍历结果集
        if (scoreDocs != null) {
            for (int i = 0; i < scoreDocs.length; i++) {
                // 获取文档id【lucene内部给的编号】
                int docID = scoreDocs[i].doc;
                // 通过文档id获取文档内容
                Document doc = indexSearcher.doc(docID);
                // 输出得分，访问文档具体域的内容
                System.out.printf("%02d [%.4f] %s\n", i + 1, scoreDocs[i].score, doc.get("docNO"));
                System.out.println(doc.get("summary") + "\n");
            }
        }

        // 关闭流
        indexReader.close();
    }

    public static void main(String[] args) {
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
            long startTime = System.currentTimeMillis();
            try {
                searchIndex("C:\\dir", q, n);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("搜索耗时：" + (endTime - startTime) + " ms.\n");
        }
    }
}
