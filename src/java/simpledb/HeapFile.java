package simpledb;

import java.io.*;
import java.util.*;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples
 * in no particular order. Tuples are stored on pages, each of which is a fixed
 * size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage
 * constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 * @author Sam Madden
 */
public class HeapFile implements DbFile {

    private final File file;

    private final TupleDesc tupledesc;
    /**
     * Constructs a heap file backed by the specified file.
     * 
     * @param f
     *            the file that stores the on-disk backing store for this heap
     *            file.
     */
    public HeapFile(File f, TupleDesc td) {
        // some code goes here
        file = f;
        tupledesc = td;
    }

    /**
     * Returns the File backing this HeapFile on disk.
     * 
     * @return the File backing this HeapFile on disk.
     */
    public File getFile() {
        // some code goes here
        return file;
    }

    /**
     * Returns an ID uniquely identifying this HeapFile. Implementation note:
     * you will need to generate this tableid somewhere to ensure that each
     * HeapFile has a "unique id," and that you always return the same value for
     * a particular HeapFile. We suggest hashing the absolute file name of the
     * file underlying the heapfile, i.e. f.getAbsoluteFile().hashCode().
     * 
     * @return an ID uniquely identifying this HeapFile.
     */
    public int getId() {
        // some code goes here
        return file.getAbsoluteFile().hashCode();
    }

    /**
     * Returns the TupleDesc of the table stored in this DbFile.
     * 
     * @return TupleDesc of this DbFile.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return tupledesc;
    }

    // see DbFile.java for javadocs
    public Page readPage(PageId pid) {
        // some code goes here
        if(pid.getTableId() != getId())
        {
            throw new IllegalArgumentException();
        }
        Page page = null;
        byte[] data = new byte[BufferPool.getPageSize()];

        try
        {
            RandomAccessFile tmp = new RandomAccessFile(getFile(),"r");
            int offset = pid.getPageNumber() * BufferPool.getPageSize();
            tmp.seek(offset);
            tmp.read(data, 0, data.length);
            page = new HeapPage((HeapPageId) pid, data);
        }catch(IOException error)
        {
            error.printStackTrace();
        }
        return page;
    }

    // see DbFile.java for javadocs
    public void writePage(Page page) throws IOException {
        // some code goes here
        // not necessary for lab1
    }

    /**
     * Returns the number of pages in this HeapFile.
     */
    public int numPages() {
        // some code goes here
        int num = (int)Math.floor((file.length() * 1.0) / BufferPool.getPageSize());
        return num;
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> insertTuple(TransactionId tid, Tuple t)
            throws DbException, IOException, TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public ArrayList<Page> deleteTuple(TransactionId tid, Tuple t) throws DbException,
            TransactionAbortedException {
        // some code goes here
        return null;
        // not necessary for lab1
    }

    // see DbFile.java for javadocs
    public DbFileIterator iterator(TransactionId tid) {
        // some code goes here
        return new HeapFileIterator(this,tid);
    }

    private static final class HeapFileIterator implements DbFileIterator{
        private final HeapFile heapFile;
        private final TransactionId transactionId;
        private Iterator<Tuple> tuplesInPage;
        private int address;

        public HeapFileIterator(HeapFile hf,TransactionId td){
            heapFile = hf;
            transactionId = td;
        }

        private Iterator<Tuple> getPageTuples(HeapPageId pageId) throws TransactionAbortedException, DbException{
            HeapPage page0 = null;
            try {
                page0 = (HeapPage) Database.getBufferPool().getPage(transactionId, pageId, Permissions.READ_ONLY);
            }finally {
                return page0.iterator();
            }
        }

        public void open() throws DbException, TransactionAbortedException {
            address = 0;
            HeapPageId pageId = new HeapPageId(heapFile.getId(), address);
            tuplesInPage = getPageTuples(pageId);
        }

        public boolean hasNext() throws DbException, TransactionAbortedException {
            if(tuplesInPage == null)
            {
                return false;
            }
            if(tuplesInPage.hasNext())
            {
                return true;
            }
            if(address < heapFile.numPages() - 1)
            {
                address++;
                HeapPageId pageId = new HeapPageId(heapFile.getId(), address);
                tuplesInPage = getPageTuples(pageId);
                return tuplesInPage.hasNext();
            }
            else
            {
                return false;
            }
        }

        public Tuple next() throws DbException, TransactionAbortedException, NoSuchElementException {
            if(tuplesInPage == null || !tuplesInPage.hasNext()){
                throw new NoSuchElementException();
            }
            return tuplesInPage.next();
        }

        public void close(){
            tuplesInPage = null;
        }

        public void rewind() throws DbException, TransactionAbortedException {
            close();
            open();
        }
    }
}

