package hff.elegant.blog.commons.search;

import hff.elegant.blog.commons.Paging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**
 * 索引库管理
 * 
 * @author ZhangYajun
 *
 * @since 7-17, 2014
 */
public class HiLucene {
    private static final Logger logger = LoggerFactory.getLogger(HiLucene.class);

    private final static BooleanQuery nullQuery = new BooleanQuery();

    private static final IKAnalyzer analyzer = new IKAnalyzer();

    private static final int MAX_COUNT = 1000;

    private String indexPath;

    private final static Formatter highlighter_formatter = new SimpleHTMLFormatter("<span class=\"highlight\">","</span>");

    public final static String FN_ID = "___id";
    public final static String FN_CLASSNAME = "___class";

    @SuppressWarnings("serial")
    private final static List<String> nowords = new ArrayList<String>(){{
        try {
            addAll(IOUtils.readLines(HiLucene.class.getResourceAsStream("/stopword.dic")));
        } catch (IOException e) {
            logger.error("Unabled to read stopword file", e);
        }
    }};    

    @SuppressWarnings("serial")
    private final static List<String> ReserveKeys = new ArrayList<String>(){{        
        try {
            addAll(IOUtils.readLines(HiLucene.class.getResourceAsStream("/keywords.dic")));
        } catch (IOException e) {
            logger.error("Unabled to read keywords file", e);
        }
    }};

    /**
     * 构造索引库管理实例
     * 
     * @param idx_path
     */
    public static HiLucene init(String indexPath) throws IOException {
        HiLucene holder = new HiLucene();
        indexPath = FilenameUtils.normalize(indexPath);
        File file = new File(indexPath);
        if (!file.exists() || !file.isDirectory())
            throw new FileNotFoundException(indexPath);
        if (!indexPath.endsWith(File.separator))
            indexPath += File.separator;
        holder.indexPath = indexPath;

        return holder;
    }

    private IndexWriter getWriter(Class<? extends Searchable> objClass) throws IOException {
        Directory directory = FSDirectory.open(new File(indexPath + objClass.getSimpleName()));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
        config.setOpenMode(OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(directory, config);
    }

    private IndexSearcher getSearcher(Class<? extends Searchable> objClass) throws IOException {
        Directory directory = FSDirectory.open(new File(indexPath + objClass.getSimpleName()));
        return new IndexSearcher(DirectoryReader.open(directory));
    }

    /**
     * 添加索引
     * @param docs
     * @throws IOException 
     */
    public int add(Searchable obj) throws IOException {
        if (obj == null)
            return 0;
        int doc_count = 0;
        IndexWriter writer = getWriter(obj.getClass());
        try {
            Document doc = new Document();

            doc.add(new LongField(FN_ID, obj.id(), Field.Store.YES));
            doc.add(new StoredField(FN_CLASSNAME, obj.getClass().getName()));

            // 存储字段
            List<String> fields = obj.storeFields();
            if (null != fields) {
                for (String fn : fields) {
                    Object fv = readField(obj, fn);
                    if (fv != null)
                    doc.add(obj2field(fn, fv, true));
                }
            }

            // 索引字段
            fields = obj.indexFields();
            if (null != fields) {
                for (String fn : fields) {
                    String fv = (String) readField(obj, fn);
                    if (fv != null) {
                        TextField tf = new TextField(fn, fv, Field.Store.NO);
                        //tf.setBoost(obj.boost());
                        doc.add(tf);
                    }
                }
            }
            writer.addDocument(doc);
            doc_count++;

            writer.commit();
        } finally {
            System.out.println("是否有删除="+writer.hasDeletions());  
            //如果不indexWriter.optimize()以下两个会有区别  
            System.out.println("一共有"+writer.maxDoc()+"索引");  
            System.out.println("还剩"+writer.numDocs()+"索引");  
            writer.close();
            writer = null;
        }
        return doc_count;
    }

    /**
     * 删除索引
     * @param docs
     * @throws IOException 
     */
    public int delete(Searchable obj) throws IOException {
        if (obj == null)
            return 0;
        int doc_count = 0;
        IndexWriter writer = getWriter(obj.getClass());
        try{
            writer.deleteDocuments(new Term("___id", Long.toString(obj.id())));
            doc_count++;
            writer.commit();
        }finally{
            System.out.println("是否有删除="+writer.hasDeletions());  
            //如果不indexWriter.optimize()以下两个会有区别  
            System.out.println("一共有"+writer.maxDoc()+"索引");  
            System.out.println("还剩"+writer.numDocs()+"索引");  
            writer.close();
            writer = null;
        }
        return doc_count;
    }

    /**
     * 更新索引
     * @param docs
     * @throws IOException 
     */
    public void update(Searchable objs) throws IOException {
        delete(objs);
        add(objs);
    }

    /**
     * 优化索引库
     * @param objClass
     * @throws IOException
     */
    public void optimize(Class<? extends Searchable> objClass) throws IOException {
        IndexWriter writer = getWriter(objClass);
        try{
            writer.forceMerge(1);
            writer.commit();
        }finally{
            writer.close();
            writer = null;
        }
    }

    /**
     * 访问对象某个属性的值
     *
     * @param obj 对象
     * @param field 属性名
     * @return  Lucene 文档字段
     */
    private static Object readField(Object obj, String field) {
        try {
            return PropertyUtils.getProperty(obj, field);
        } catch (Exception e) {
            logger.error("Unabled to get property '"+field+"' of " + obj.getClass().getName(), e);
            return null;
        }

    }

    private static Field obj2field(String field, Object fieldValue, boolean store) {
        if(fieldValue == null)
            return null;
        
        if (fieldValue instanceof Date) //日期
            return new LongField(field, ((Date)fieldValue).getTime(), store?Field.Store.YES:Field.Store.NO);
        if (fieldValue instanceof Number) //其他数值
            return new StringField(field, String.valueOf(((Number)fieldValue).longValue()), store?Field.Store.YES:Field.Store.NO);
        //其他默认当字符串处理
        return new StringField(field, (String)fieldValue, store?Field.Store.YES:Field.Store.NO);
    }

    /**
     * 单库搜索
     * @param objClass
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    public List<Long> find(Class<? extends Searchable> objClass, Query query, Filter filter, Sort sort, Paging page) throws IOException {
        List<Long> ids = new ArrayList<Long>();
        try {
            IndexSearcher searcher = getSearcher(objClass);
            List<Searchable> results = find(searcher, query, filter, sort, page);
            for(Searchable obj : results){
                if(obj != null)
                    ids.add(obj.id());
            }
        } catch (IndexNotFoundException e) {
        }
        return ids;
    }

    /**
     * 搜索
     * @param searcher
     * @param query
     * @param filter
     * @param sort
     * @param page
     * @param count
     * @return
     * @throws IOException
     */
    private List<Searchable> find(IndexSearcher searcher, Query query, Filter filter, Sort sort, Paging page2) throws IOException {
        try{
            TopDocs hits = null;
            if(filter != null && sort != null)
                hits = searcher.search(query, filter, MAX_COUNT, sort);
            else if(filter != null)
                hits = searcher.search(query, filter, MAX_COUNT);
            else if(sort != null)
                hits = searcher.search(query, MAX_COUNT, sort);
            else
                hits = searcher.search(query, MAX_COUNT);
            if(hits==null) return null;

            int page = page2.getPageIndex();
            int count = page2.getPageSize();
            page2.setTotalRecordsNumber(hits.totalHits);
            List<Searchable> results = new ArrayList<Searchable>();
            int nBegin = (page - 1) * count;
            int nEnd = Math.min(nBegin + count, hits.scoreDocs.length);
            for (int i = nBegin; i < nEnd; i++){
                ScoreDoc s_doc = (ScoreDoc)hits.scoreDocs[i];
                Document doc = searcher.doc(s_doc.doc);
                Searchable obj = doc2obj(doc);
                if(obj != null && !results.contains(obj)){
                    results.add(obj);    
                }
            }
            return results;
            
        }catch(IOException e){
            logger.error("Unabled to find via query: " + query, e);
        }
        return null;
    }

    /**
     * 返回文档中保存的对象 id
     * @param doc
     * @return
     */
    public static long docid(Document doc) {
        return NumberUtils.toLong(doc.get(FN_ID), 0);
    }

    /**
     * 获取文档对应的对象类
     * @param doc
     * @return
     * @throws ClassNotFoundException 
     */
    public static Searchable doc2obj(Document doc) {
        try{
            long id = docid(doc);
            if(id <= 0)
                return null;
            Searchable obj = (Searchable)Class.forName(doc.get(FN_CLASSNAME)).newInstance();
            obj.setId(id);
            return obj;
        }catch(Exception e){
            logger.error("Unabled generate object from document#id="+doc.toString(), e);
            return null;
        }
    }

    /**
     * 重整搜索关键短语
     * @param key
     * @return
     */
    public static String cleanupKey(String key) {
        if(ReserveKeys.contains(key.trim().toLowerCase()))
            return key;

        StringBuilder sb = new StringBuilder();
        List<String> keys = splitKeywords(key);
        for (String word : keys) {
            if (sb.length() > 0)
                sb.append(' ');
            sb.append(word);
        }

        return sb.toString();
    }    

    /**
     * 关键字切分
     * @param sentence 要分词的句子
     * @return 返回分词结果
     */
    public static List<String> splitKeywords(String sentence) {

        List<String> keys = new ArrayList<String>();

        if (StringUtils.isNotBlank(sentence)) {
            StringReader reader = new StringReader(sentence);
            IKSegmenter ikseg = new IKSegmenter(reader, true);
            try {
                do {
                    Lexeme me = ikseg.next();
                    if (me == null)
                        break;
                    String term = me.getLexemeText();
                    //if(term.length() == 1)
                    //    continue;
                    //if(StringUtils.isNumeric(StringUtils.remove(term,'.')))
                    //    continue;
                    if (nowords.contains(term.toLowerCase()))
                        continue;
                    keys.add(term);
                } while (true);
            } catch (IOException e) {
                logger.error("Unable to split keywords", e);
            }
        }
        return keys;
    }

    /**
     * 对一段文本执行语法高亮处理
     * @param text 要处理高亮的文本
     * @param key 高亮的关键字
     * @return 返回格式化后的HTML文本
     */
    public static String highlight(String text, String key) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(text))
            return text;
        String result = null;
        try {
            PhraseQuery pquery = new PhraseQuery();
            for(String sk : splitKeywords(key)){
                pquery.add(new Term("", QueryParser.escape(sk)));
            }         
            QueryScorer scorer = new QueryScorer(pquery);
            Highlighter hig = new Highlighter(highlighter_formatter, scorer);
            TokenStream tokens = analyzer.tokenStream(null, new StringReader(text));
            result = hig.getBestFragment(tokens, text);
        } catch (Exception e) {
            logger.error("Unabled to hightlight text", e);
        }
        return (result != null)?result:text;
    }

    /**
     * 生成查询条件
     * @param field
     * @param q
     * @param boost
     * @param machine_action
     * @return
     */
    public static Query makeQuery(String field, String q, float boost) {
        if(StringUtils.isBlank(q) || StringUtils.isBlank(field))
            return nullQuery;
        QueryParser parser = new QueryParser(Version.LUCENE_42, field, analyzer);
        parser.setDefaultOperator(QueryParser.OR_OPERATOR);
        try{
            Query querySinger = parser.parse(q);
            querySinger.setBoost(boost);
            //System.out.println(querySinger.toString());
            return querySinger;
        }catch(Exception e){
            TermQuery queryTerm = new TermQuery(new Term(field, q));
            queryTerm.setBoost(boost);
            //System.out.println(queryTerm.toString());
            return queryTerm;
        }
    }

    public static Query makeQuery(String[] fields, BooleanClause.Occur[] flags, String q, float boost) {
        if(StringUtils.isBlank(q) || null == fields || fields.length == 0)
            return nullQuery;
        try {
            return MultiFieldQueryParser.parse(Version.LUCENE_42, q, fields, flags, analyzer);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
