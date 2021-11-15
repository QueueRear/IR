package lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static dom4j.ExtractTest.getExtracted;

public class indexManagerTest {
    // 创建索引库
    @Test
    public static void createIndexTest(String filepath, String dirPath) throws Exception {
        ArrayList<ArrayList<String>> formedDocs = getExtracted(filepath);

        List<Document> docList = new ArrayList<>();
        for (ArrayList<String> formedDoc : formedDocs) {
            // 创建文档对象
            Document document = new Document();

            // 创建域对象并放入文档对象中
            document.add(new StringField("docNO", formedDoc.get(0), Field.Store.YES));
            document.add(new StringField("docType", formedDoc.get(1), Field.Store.YES));
            document.add(new StringField("txtType", formedDoc.get(2), Field.Store.YES));

            String t = formedDoc.get(3);
            document.add(new TextField("text", t, Field.Store.NO));

            //生成summary
            int maxDisplay = 30;
            String[] s = t.split(" ");
            int len = (s.length < maxDisplay) ? s.length : maxDisplay;
            String summary = "";
            for (int i = 0; i < len; i++) {
                summary += s[i] + " ";
            }
            if (s.length > maxDisplay) {
                summary += "...";
            }
            document.add(new TextField("summary", summary, Field.Store.YES));

            // 将文档对象放入文档集合中
            docList.add(document);
        }

        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();

        // 创建目录对象
        File file = new File(dirPath);// 创建存放索引的文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        Directory dir = FSDirectory.open(Paths.get(dirPath));

        // 指定切分词中使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 指定输出位置和使用的config输出对象
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();// 清除已存在的索引库

        // 将文档添加到索引库
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }

        // 关闭写入流，释放资源
        indexWriter.close();
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String path = "D:\\Users\\你爹的电脑\\Desktop\\新建文件夹";
        try {
            createIndexTest(path, "D:\\dir");
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));
    }
}
