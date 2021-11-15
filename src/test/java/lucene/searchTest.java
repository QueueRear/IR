package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class searchTest {
    @Test
    public void indexSearchTest() throws ParseException, IOException {
        // 创建分词器（对query使用）
        Analyzer analyzer = new StandardAnalyzer();

        // 创建查询对象，参数一：默认查询域；参数二：使用的分词器
        QueryParser queryParser = new QueryParser("text", analyzer);

        // 设置搜索关键词
        System.out.println("请输入检索语句：");
        Scanner s1 = new Scanner(System.in);
        Query query = queryParser.parse(s1.nextLine());

        // 创建目录对象，指定索引库的位置
        Directory dir = FSDirectory.open(Paths.get("D:\\dir"));

        // 创建输入流对象
        IndexReader indexReader = DirectoryReader.open(dir);

        // 创建搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        indexSearcher.setSimilarity(new MySimilarityTest());

        // 搜索并返回结果
        System.out.println("请输入需要输出的结果数：");
        Scanner s2 = new Scanner(System.in);
        int n = s2.nextInt();
        TopDocs topDocs = indexSearcher.search(query, n);

        // 获取结果集
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 遍历结果集
        if (scoreDocs != null) {
            if (scoreDocs.length == 0) {
                System.out.println("无结果。");
            }
            for (int i = 0; i < scoreDocs.length; i++) {
                // 获取文档id【lucene内部给的编号】
                int docID = scoreDocs[i].doc;
                // 通过文档id获取文档内容
                Document doc = indexSearcher.doc(docID);
                // 输出得分，访问文档具体域的内容
                System.out.printf("%02d [%.4f] %s\n", i + 1, scoreDocs[i].score, doc.get("docNO"));
                System.out.println(doc.get("summary"));
                System.out.println();
            }
        }
        else {
            System.out.println("无结果。");
        }

        // 关闭流
        indexReader.close();
    }
}
