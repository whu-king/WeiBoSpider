package ikanalyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * ikanalyzer.IKAnalyzerUtil 示例
 * 2012-3-2
 * <p/>
 * 以下是结合Lucene3.4 API的写法
 */
public class IKAnalyzerUtil {

    public static void main(String[] args) throws Exception {
        test();
        test2();
    }

    public static void test() {
        //Lucene Document的域名
        String fieldName = "text";
//检索内容
        String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
//实例化IKAnalyzer分词器
        Analyzer analyzer = new IKAnalyzer();
        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
        IndexSearcher isearcher = null;
        try {
//建立内存索引对象
            directory = new RAMDirectory();
//配置IndexWriterConfig
            IndexWriterConfig iwConfig = new
                    IndexWriterConfig(Version.LUCENE_34, analyzer);
            iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
//写入索引
            Document doc = new Document();
            doc.add(new Field("ID", "10000", Field.Store.YES,
                    Field.Index.NOT_ANALYZED));
            doc.add(new Field(fieldName, text, Field.Store.YES,
                    Field.Index.ANALYZED));
            iwriter.addDocument(doc);
            iwriter.close();
//搜索过程**********************************
//实例化搜索器
            ireader = IndexReader.open(directory);
            isearcher = new IndexSearcher(ireader);
            String keyword = "中文";
//使用QueryParser查询分析器构造Query对象
            QueryParser qp = new QueryParser(Version.LUCENE_34,
                    fieldName, analyzer);
            qp.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = qp.parse(keyword);
//搜索相似度最高的5条记录
            TopDocs topDocs = isearcher.search(query, 5);
            System.out.println("命中：" + topDocs.totalHits);
//输出结果
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < topDocs.totalHits; i++) {
                Document targetDoc = isearcher.doc(scoreDocs[i].doc);
                System.out.println("内容：" + targetDoc.toString());
            }
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (ireader != null) {
                try {
                    ireader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void test2() throws Exception {
        String text = "宁夏" +
                "满洲国铸造村北路对尼玛公安局非常失望，小偷抓到了，半年过去不让了解办案进度。都开始刑事诉讼了，完全不通知受害方。网上咨询后，态度相当恶劣：小偷赔不起，我们只管刑事诉讼，其他不管。那我他妈报个屁的警啊，这警察是给老百姓办事么？草！@重庆网警 @重庆交巡警 新溉大道";
        //创建分词对象
        Analyzer anal = new IKAnalyzer(true);
        StringReader reader = new StringReader(text);
        //分词
        TokenStream ts = anal.tokenStream(" ", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        //遍历分词数据
        while (ts.incrementToken()) {
            System.out.print(term.toString() + "|");
        }
        reader.close();
        System.out.println();
    }

    public static List<String> split(String text) throws IOException {
        List<String> results = new ArrayList<String>();
        Analyzer anal = new IKAnalyzer(true);
        StringReader reader = new StringReader(text);
        TokenStream ts = anal.tokenStream(" ", reader);
        CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            results.add(term.toString());
//            System.out.print(term.toString()+"|");
        }
        reader.close();
        return results;
    }

}
