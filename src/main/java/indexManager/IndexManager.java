package indexManager;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static contentExtractor.Extract.getExtracted;

public class IndexManager {
    // 创建索引库
    public static void createIndex(String filepath, String dirPath) throws Exception {
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
            int maxDisplay = 40;// summary最大词数
            String[] s = t.split(" ");
            String summary = "";
            if (s.length <= maxDisplay) {
                summary = t;
            }
            else {
                for (int i = 0; i < maxDisplay; i++) {
                    summary += s[i] + " ";
                }
                summary += "...";
            }
            document.add(new StoredField("summary", summary));

            // 将文档对象放入文档集合中
            docList.add(document);
        }

        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();

        // 创建目录对象
        File file = new File(dirPath);
        if (!file.exists()) {// 创建存放索引的文件夹
            file.mkdir();
        }
        Directory dir = FSDirectory.open(Paths.get(dirPath));

        // 指定切分词中使用的分词器
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        // 使用的config输出索引库
        IndexWriter indexWriter = new IndexWriter(dir, config);
        indexWriter.deleteAll();// 清除已存在的索引库

        // 将文档添加到索引库
        for (Document doc : docList) {
            indexWriter.addDocument(doc);
        }

        // 关闭输出流，释放资源
        indexWriter.close();
    }
}
