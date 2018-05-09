---
layout: post
title: Java并发读取并入库large-size XML文件
subtitle: 通过多线程并发实现大XML文件的解析和入库
---

> 项目中需要读取1G大小的XML文件，解析文件，将相应的字段存入数据库中<br><br>
解析XML可以通过`SAX`和`DOM`两种方式；`SAX`可以边读取边入库；`DOM`必须将整个文件加载到内存<br><br>
解析一个1G的XML文件很快，但是1G文件有数十万条记录，入库的过程相对于解析就很慢<br><br>
考虑的内存的问题，采用`SAX`的方式对文件进行解析<br><br>
考虑到边读取边入库，要创建不同的`解析文件线程`和`入库线程`<br><br>
考虑到入库的`I/O`花销时间要远远大于解析的时间，采用`1个解析文件线程`和`多个(32)入库线程`

**SAX**

The Simple API for XML(SAX)是一个基于回调函数(callback routines)或者事件处理器(event handler)的事件驱动型(event-based)API，可以用来解析XML的不同部分。使用SAX时，需要注册不同事件的handler，然后解析XML文件。

**程序大体思路：**

- 文件解析线程(`SourceProcessor.java`)
    - 负责文件的解析(当解析的数据缓存达到一定的数量时，添加到工作队列)
- 入库线程(`DatabaseWriter.java`)
    - 多线程并发从工作队列中取出来数据，并入库
- 线程协作类(`SaxProcessor.java`)
    - 定义个工作队列，文件解析线程和入库线程共同使用这个工作队列协同工作
    - 负责文件解析线程和入库线程的协同工作
        - 当工作队列满时，文件解析线程等待
        - 当入库线程消费数据后，通知正在等待的线程
    - 负责文件解析完毕后线程池的关闭
        - 当文件解析未完成时，主线程等待
        - 当文件解析完毕时，通知主线程，主线程关闭线程池

**文件解析线程：**

    package org.fmz.parsexml.mysql;

    import oracle.xml.parser.v2.SAXParser;
    import org.xml.sax.Attributes;
    import org.xml.sax.ContentHandler;
    import org.xml.sax.Locator;
    import org.xml.sax.SAXException;

    import java.io.FileInputStream;
    import java.util.ArrayList;
    import java.util.List;

    /**XML文件解析类    我用的是ORACLE的解析包xmlparserv2.jar
     * SAX在解析的过程中，在一个元素结尾与另一个元素开始处，解析器会把他当成一个文本结点。 
     * characters方法会多出很多空格，最后用一个笨的方法解决了他，还请批评指正。 
     */
    public class SourceProcessor extends Thread implements ContentHandler {

        
        private SaxProcessor saxProcessor;//生产者-消费者协作类
        private String targetFilename = null;//目标解析文件
        private boolean recordStart = false;//开始解析
        private boolean useable = false;//开始收集

        private List<List<String>> cacheList = new ArrayList<List<String>>();//要添加到消费队列的缓存
        private List<String> curDatas = new ArrayList<String>();//收集的一条记录
        private StringBuffer curData = new StringBuffer();//收集的文本值

        /**
         * 收集<tag></tab>内字符串
         */
        @Override
        public void characters(char[] ch, int start, int length)
        throws SAXException {

            if (this.useable == true) {
                curData.append(new String(ch, start, length));
            
            }
        
        }
        
        /**
         * 解析节点开始的回调函数
         */
        @Override
        public void startElement(String uri, String localName, String name,
                Attributes atts) throws SAXException {
            if (localName.equals(this.saxProcessor.getSetting("group", "group"))) {
                this.recordStart = true;
            
            } else if (recordStart == true) {
                /* 可以收集 */
                this.useable = true;
            
            }

        
        }

        /**
         * 解析节点结束的回调函数
         */
        @Override
        public void endElement(String uri, String localName, String name)
        throws SAXException {
            /* 一条记录完成 */
            if (localName.equals(this.saxProcessor.getSetting("group", "group"))) {
                cacheList.add(curDatas);
                this.curDatas = new ArrayList<String>();
                /* 缓存的数据大于200条，添加到消费队列 */
                if (cacheList.size() >= 100) {
                    this.saxProcessor.addToQueue(cacheList);
                    cacheList = new ArrayList<List<String>>();
                
                }
            
            } else if (this.recordStart == true) {
                curDatas.add(curData.toString().trim());
                curData = new StringBuffer();
                this.useable = false;
            
            }
        
        }
        
        /**
         * 解析文件结束回调函数
         */
        @Override
            public void endDocument() throws SAXException {
                if (cacheList.size() > 0) {
                this.saxProcessor.addToQueue(cacheList);
            
                }
            this.saxProcessor.setParsingComplete();
            System.out.println("文件解析结束！");
        
            }

        @Override
            public void endPrefixMapping(String prefix) throws SAXException {

        
            }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {
        
        }

        @Override
        public void processingInstruction(String target, String data)
        throws SAXException {

        
        }

        @Override
            public void setDocumentLocator(Locator locator) {

        
            }

        @Override
            public void skippedEntity(String name) throws SAXException {
            
        
            }

        @Override
            public void startDocument() throws SAXException {

        
            }

        @Override
        public void startPrefixMapping(String prefix, String uri)
        throws SAXException {
        
        }
        
        public SourceProcessor(String threadName) {
            super(threadName);
        
        }

        public SaxProcessor getSaxProcessor() {
            return saxProcessor;
        
        }

        public void setSaxProcessor(SaxProcessor saxProcessor) {
            this.saxProcessor = saxProcessor;
        
        }
        
        public boolean isRecordStart() {
            return recordStart;
        
        }

        public void setRecordStart(boolean recordStart) {
            this.recordStart = recordStart;
        
        }

        public String getTargetFilename() {
            return targetFilename;
        
        }

        public void setTargetFilename(String targetFilename) {
            this.targetFilename = targetFilename;
        
        }
        
        public boolean isUseable() {
            return useable;
        
        }

        public void setUseable(boolean useable) {
            this.useable = useable;
        
        }

        public void run() {
            try {
                SAXParser parser = new SAXParser();//Oracle SAX解析类对象
                // 解析类对象设置属性
                parser.setAttribute(SAXParser.STANDALONE, Boolean.valueOf(true));
                parser.setValidationMode(SAXParser.NONVALIDATING);
                parser.setContentHandler(this);//设置handler属性
                this.saxProcessor.setParserActive();//
                parser.parse(new FileInputStream(this.targetFilename));

            
            } catch (ProcessingCompleteException pce) {
                pce.printStackTrace();
            
            } catch (Exception e) {
                e.printStackTrace();
            
            }
        
        }


    }

**入库线程：**

    package org.fmz.parsexml.mysql;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
    import java.util.List;

    /* 数据库写入线程类，记录数达到commitCharge条则提交,最后提交剩余记录 */
    public class DatabaseWriter extends Thread {
        private Connection connection;//数据库连接
        private SaxProcessor saxProcessor;//生产者-消费者协作类
        private String threadName;//线程名称
        private int commitCharge;//一次事务提交的statement数
        private int recordCount = 0;//记录数
        
        DatabaseWriter(SaxProcessor processor, String threadName,
                Connection connection) {
            this.connection = connection;
            this.saxProcessor = processor;
            this.threadName = threadName;
        
        }

        public Connection getConnection() {
            return connection;
        
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        
        }

        public SaxProcessor getSaxProcessor() {
            return saxProcessor;
        
        }

        public void setSaxProcessor(SaxProcessor processor) {
            this.saxProcessor = processor;
        
        }

        public void setCommitCharge(int commitCharge) {
            this.commitCharge = commitCharge;
        
        }

        public void run() {
            PreparedStatement stat = null;
            try {
                connection.setAutoCommit(false);
                stat = connection
                        .prepareStatement("insert into testfp values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

            
            } catch (SQLException e1) {
                e1.printStackTrace();
            
            }

            while (!this.saxProcessor.processingComplete()) {
                List<List<String>> datas = this.saxProcessor.getNextData();
                System.out.println(this.threadName + " run!!");

                if (datas != null && stat != null) {
                    for (int i = 0; i < datas.size(); i++) {
                        try {
                            List<String> record = (List<String>) datas.get(i);

                            stat.setString(1, (String) record.get(0));
                            stat.setString(2, (String) record.get(1));
                            stat.setString(3, (String) record.get(2));
                            stat.setString(4, (String) record.get(3));
                            stat.setString(5, (String) record.get(4));
                            stat.setString(6, (String) record.get(5));
                            stat.setString(7, (String) record.get(6));
                            stat.setString(8, (String) record.get(7));
                            stat.setString(9, (String) record.get(8));
                            stat.setString(10, (String) record.get(9));
                            stat.setString(11, (String) record.get(10));
                            stat.setString(12, (String) record.get(11));
                            stat.setString(13, (String) record.get(12));

                            stat.execute();
                            this.recordCount++;

                        
                        } catch (SQLException e) {
                            e.printStackTrace();
                        
                        }
                    
                    }
                
                }
                if (recordCount >= commitCharge) {
                    try {
                        connection.commit();
                        this.recordCount = 0;
                    
                    } catch (SQLException e) {
                        e.printStackTrace();
                    
                    }
                
                }

            
            }
            try {
                if (!connection.isClosed()) {
                    connection.commit();
                
                }
            
            } catch (SQLException e) {
                e.printStackTrace();
            
            }
        
        }

    }

**线程协作类(main()在这个类中)：**

    package org.fmz.parsexml.mysql;

    import oracle.xml.parser.v2.XMLDocument;
    import org.w3c.dom.Document;
    import org.xml.sax.SAXException;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.text.DecimalFormat;
    import java.util.Date;
    import java.util.List;
    import java.util.Vector;
    import java.util.concurrent.BlockingDeque;
    import java.util.concurrent.LinkedBlockingDeque;
    import java.util.concurrent.ThreadPoolExecutor;
    import java.util.concurrent.TimeUnit;

    /*
     * 解析控制程序 
     */
    public class SaxProcessor extends ConnectionProvider {
        /* 工作队列，暂存解析的数据 */
        private Vector<List<List<String>>> workQueue = new Vector<List<List<String>>>();
        /* 线程池 */
        BlockingDeque<Runnable> queue = new LinkedBlockingDeque<Runnable>();//线程池使用的队列
        ThreadPoolExecutor executor = new ThreadPoolExecutor(32, 64, 1, TimeUnit.MINUTES, queue);
        private int threadCount;//线程数
        Thread saxReader;//文件解析线程
        private boolean parserActive = false;//解析进行中

        private void setWriterCount(int count) {
            this.threadCount = count;
        
        }

        private int getWriterCount() {
            return this.threadCount;
        
        }
        
        public Vector<List<List<String>>> getWorkQueue() {
            return workQueue;
        
        }

        public void setWorkQueue(Vector<List<List<String>>> workQueue) {
            this.workQueue = workQueue;
        
        }

        protected synchronized void setParserActive() {
            this.parserActive = true;
        
        }

        protected synchronized void setParsingComplete() {
            this.parserActive = false;
            notifyAll();
        
        }

        public synchronized boolean parsingComplete() {
            return !this.parserActive;
        
        }

        public synchronized boolean processingComplete() {

            boolean result = (parsingComplete()) && (this.workQueue.size() == 0);
            return result;
        
        }

        /**
         * 规定当消费队列的大小是大于线程数两倍时，认为线程池已满
         * @return
         */
        private boolean listQueueFull() {
            return (this.workQueue.size() >= (2 * getWriterCount()));
        
        }

        /* 向工作队列添加一个任务,并通知所有受阻线程 */
        protected synchronized void addToQueue(List<List<String>> data) throws SAXException {
            if (listQueueFull()) {
                try {
                    wait();
                
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                
                }
            
            }

            this.workQueue.addElement(data);
            notifyAll();

        
        }

        /**
         * 去除消费队列下一条数据 通知文件解析线程
         * 如果文件解析没完成且消费队列为空 等待...
         * @param thread
         * @return
         */
        public synchronized List<List<String>> getNextData() {
            List<List<String>> data = null;
            while (!parsingComplete() && (this.workQueue.size() == 0)) {
                try {
                    wait();
                
                } catch (InterruptedException ioe) {
                
                }
            
            }
            if (this.workQueue.size() > 0) {
                data = (List<List<String>>) this.workQueue.remove(0);
                notifyAll();
            
            }
            return data;
        
        }

        public SaxProcessor() throws SQLException, IOException, SAXException {
            super();
        
        }

        public void doSomething(String[] args) {
            try {
                //设置协作类threadCount线程属性
                setWriterCount(Integer.parseInt(getSetting("ThreadCount", "4")));
                this.saxReader = createSourceProcessor();//创建文件解析线程
                this.setParserActive();//设置解析为激活状态
                this.saxReader.start();//文件解析线程启动
                createDatabaseWriters();//创建入库线程
                waitForCompletion();//主线程等待文件解析线程结束
            
            } catch (Exception e) {
                e.printStackTrace();
                this.setParsingComplete();
            
            }
        
        }

        /**
         * 主线程等待文件解析线程结束后，关闭线程池
         */
        private synchronized void waitForCompletion() {
            while (!parsingComplete()) {
                try {
                    wait();
                
                } catch (InterruptedException ioe) {
                
                }
            
            }
            this.executor.shutdown();
        
        }

        /**
         * 创建threadCount个入库线程
         * @throws SQLException
         */
        private void createDatabaseWriters() throws SQLException {
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern("000000");
            int commitCharge = Integer.parseInt(getSetting("CommitCharge", "50"));
            for (int i = 0; i < getWriterCount(); i++) {
                System.out.println(getWriterCount());
                String threadName = "Writer_" + df.format(i + 1);
                Connection conn = getNewConnection();
                conn.setAutoCommit(false);//关闭数据库自动commit
                DatabaseWriter writer = new DatabaseWriter(this, threadName, conn);
                writer.setCommitCharge(commitCharge);

                this.executor.execute(writer);//线程提交给线程池处理
            
            }
        
        }

        /**
         * 创建一个文件解析线程
         * @return
         * @throws SQLException
         */
        private Thread createSourceProcessor() throws SQLException {
            String threadName = "SaxReader";
            SourceProcessor saxReader = new SourceProcessor(threadName);
            saxReader.setSaxProcessor(this);
            saxReader.setTargetFilename(getSetting("SourceXML", "DIR"));
            return saxReader;
        
        }

        protected synchronized void printXML(Document xml, PrintWriter pw)
            throws IOException {
            ((XMLDocument) xml).print(pw);
        
            }

        /* 主函数 */
        public static void main(String[] args) {
            Date beginDate = new Date();
            try {
                SaxProcessor app = new SaxProcessor();
                app.initializeConnection();
                app.doSomething(args);
                System.out.println("用时: "
                        + (new Date().getTime() - beginDate.getTime()));
            
            } catch (Exception e) {
                e.printStackTrace();
            
            }
        
        }

    }

**其他依赖类：**

    package org.fmz.parsexml.mysql;

    import oracle.xml.parser.v2.DOMParser;
    import oracle.xml.parser.v2.XMLDocument;
    import oracle.xml.parser.v2.XMLElement;

    import org.w3c.dom.Element;
    import org.w3c.dom.NodeList;
    import org.w3c.dom.Text;
    import org.xml.sax.SAXException;

    import java.io.*;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

    public class ConnectionProvider extends Object {
        public static final boolean DEBUG = true;

        protected Connection connection;

        protected XMLDocument connectionDefinition;

        public static final String CONNECTION = "Connection";
        public static final String DRIVER = "Driver";
        public static final String HOSTNAME = "Hostname";
        public static final String PORT = "Port";
        public static final String DBNAME = "Dbname";
        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";
        public static final String POOL = "Pool";

        public static final String DEFAULT_CONNECTION_DEFINITION = "E:\\connection-mysql.xml";
        public static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
        public static final String DEFAULT_HOSTNAME = "localhost";
        public static final String DEFAULT_PORT = "3306";

        public static final String TARGET_DIRECTORY = "targetDirectory";

        protected PrintStream log;

        public ConnectionProvider() {

        }

        public void initializeConnection() throws SAXException, IOException,
                SQLException {
            this.initializeConnection(System.out);
        }

        public void initializeConnection(PrintStream log) throws SAXException,
                IOException, SQLException {
            this.log = log;
            /* 加载数据库配置文件connection.xml为XMLDocument */
            loadConnectionSettings();
            /* 打开数据库连接 */
            this.connection = openConnection();
        }
        
        public void loadConnectionSettings() throws IOException, SAXException {
            /*String filename = System.getProperty(
                    "com.oracle.st.xmldb.pm.ConnectionParameters",
                    this.DEFAULT_CONNECTION_DEFINITION);*/
            String filename = DEFAULT_CONNECTION_DEFINITION;//connection-mysql.xml
            loadConnectionSettings(filename);
        }

        public void loadConnectionSettings(String filename) throws IOException,
                SAXException {
            if (DEBUG) {
                System.out.println("Using connection.xml Parameters from : "
                        + filename);
            }
            Reader reader = new FileReader(new File(filename));
            DOMParser parser = new DOMParser();
            parser.parse(reader);
            XMLDocument doc = parser.getDocument();
            setConnectionSettings(doc);
            if (DEBUG) {
                dumpConnectionSettings();
            }
        }
        
        public ConnectionProvider getConnectionProvider() {
            return this;
        }

        public void setLogger(PrintStream log) {
            this.log = log;
        }

        private void setConnectionSettings(XMLDocument doc) {
            this.connectionDefinition = doc;
        }

        private void dumpConnectionSettings() throws IOException {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            this.connectionDefinition.print(pw);
            pw.close();
            sw.close();
        }

        public Connection getConnection() throws SQLException {
            return this.connection;
        }

        public void closeConnection(Connection conn) throws Exception {
            if (isPooled()) {
                conn.close();
            }
        }

        public Connection getConnection(String schema, String passwd)
                throws Exception {
            if (isPooled()) {
                return  this.getConnection(schema, passwd);
            } else {
                return this.connection;
            }
        }

        public String getSetting(String nodeName) {
            return getSetting(nodeName, null);
        }

        /* 根据<tag>名称，得到相应的值 */
        public String getSetting(String nodeName, String defaultValue) {
            XMLElement root = (XMLElement) this.connectionDefinition
                    .getDocumentElement();//得到DOM文件的根节点
            NodeList children = root.getChildrenByTagName(nodeName);
            if (children.getLength() != 0) {
                Element element = (Element) children.item(0);
                Text text = (Text) element.getFirstChild();
                if (text != null) {
                    return text.getData();
                }
            }
            return defaultValue;
        }

        protected String getDriver() {
            return getSetting(DRIVER, DEFAULT_DRIVER);
        }

        protected String getHostname() {
            return getSetting(HOSTNAME, DEFAULT_HOSTNAME);
        }

        protected String getPort() {
            return getSetting(PORT, DEFAULT_PORT);
        }

        protected String getDbname() {
            return getSetting(DBNAME);
        }
        
        protected String getUsername() {
            return getSetting(USERNAME);
        }

        protected boolean isPooled() {
            String usePool = getSetting(POOL, Boolean.FALSE.toString());
            return !usePool.equalsIgnoreCase(Boolean.FALSE.toString());
        }

        protected String getPassword() {
            return getSetting(PASSWORD);
        }

        protected String getDatabaseURL() {
            if(getDriver() != null){
                return "jdbc:mysql://" + getHostname() + ":" + getPort() + "/" + getDbname(); 
            }else{
                return null;
            }
        }

        private Connection openConnection() throws SQLException {
            String user = getUsername();
            String password = getPassword();
            String connectionString = user + "/" + password + "@"
                    + getDatabaseURL();
            Connection conn = null;
            if (DEBUG) {
                this.log.println("ConnectionProvider.establishConnection(): Connecting as "
                        + connectionString);
            }
            try {
                /*DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());*/
                try {
                    /* 加载数据库驱动 */
                    Class.forName(getDriver());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                conn =  DriverManager.getConnection(
                        getDatabaseURL(), user, password);
                if (DEBUG) {
                    this.log.println("ConnectionProvider.establishConnection(): Database Connection Established");
                }
            } catch (SQLException sqle) {
                int err = sqle.getErrorCode();
                this.log.println("ConnectionProvider.establishConnection(): Failed to connect using "
                        + connectionString + "; the error code is " + err);
                sqle.printStackTrace(this.log);
                throw sqle;
            }
            return conn;
        }

        public Connection getNewConnection() throws SQLException {
            return openConnection();
        }

        public XMLDocument getConnectionSettings() {
            return this.connectionDefinition;
        }
    }

> 数据库连接类

    package org.fmz.parsexml.mysql;

    import org.xml.sax.SAXException;

    public class ProcessingCompleteException extends SAXException {

        private static final long serialVersionUID = 1L;

        public ProcessingCompleteException() {
            super("Processing Complete");
        }
    }

> 异常类

**配置文件：**

    <?xml version="1.0" encoding="UTF-8"?>
    <SourceXML>
        <Driver>com.mysql.jdbc.Driver</Driver>
        <Hostname>127.0.0.1</Hostname>
        <Port>3306</Port>
        <Username>fmz</Username>
        <Dbname>parsexml</Dbname>
        <Password>147258</Password>
        <Table>testmt</Table>
        <SourceXML>E:\\fpcx_ext.xml</SourceXML>
        <CommitCharge>100</CommitCharge>
        <ThreadCount>32</ThreadCount>
    </SourceXML>

> connection-mysql.xml

**被解析文件示例：**

<?xml version="1.0" encoding="gbk"?>
<business comment="认证接口" id="RZJK">
    <body>
        <output>
            <returncode>01</returncode>
            <returnmsg>处理成功！</returnmsg>
            <returnfpnum>3492</returnfpnum>
            <fpxx>
                <group>
                    <fpdm>44139085</fpdm>
                    <fphm>44139085</fphm>
                    <kprq>2016-12-05</kprq>
                    <xfmc>北京市北京饭店</xfmc>
                    <hjje>2320.75</hjje>
                    <hjse>139.25 </hjse>
                    <fpzt>0</fpzt>
                    <gxzt>0</gxzt>
                    <gxrq/>
                    <sfsmrz>0</sfsmrz>
                    <rzrq/>
                    <fpzl>1</fpzl>
                    <rzyf/>
                </group>
            </fpxx>
        </output>
    </body>
</business>

> fpcx.xml
