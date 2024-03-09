package simpledb;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Tuple maintains information about the contents of a tuple. Tuples have a
 * specified schema specified by a TupleDesc object and contain Field objects
 * with the data for each field.
 */
public class Tuple implements Serializable {

    private static final long serialVersionUID = 1L;

    private TupleDesc tupleDesc;

    private Field[] fields;

    private RecordId recordId;

    private int fieldnum;
    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td
     *            the schema of this tuple. It must be a valid TupleDesc
     *            instance with at least one field.
     */
    public Tuple(TupleDesc td) {
        // some code goes here
        tupleDesc = td;
        fields = new Field[td.numFields()];
        fieldnum = fields.length;
    }

    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
        // some code goes here
        return tupleDesc;
    }

    /**
     * @return The RecordId representing the location of this tuple on disk. May
     *         be null.
     */
    public RecordId getRecordId() {
        // some code goes here
        return recordId;
    }

    /**
     * Set the RecordId information for this tuple.
     *
     * @param rid
     *            the new RecordId for this tuple.
     */
    public void setRecordId(RecordId rid) {
        // some code goes here
        recordId = rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i
     *            index of the field to change. It must be a valid index.
     * @param f
     *            new value for the field.
     */
    public void setField(int i, Field f) {
        // some code goes here
        if(i < 0 || i >= fieldnum)
        {
            throw new IllegalStateException();
        }
        fields[i] = f;
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i
     *            field index to return. Must be a valid index.
     */
    public Field getField(int i) {
        // some code goes here
        if(i < 0 || i >= fieldnum)
        {
            throw new IllegalStateException();
        }
        return fields[i];
    }

    /**
     * Returns the contents of this Tuple as a string. Note that to pass the
     * system tests, the format needs to be as follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN
     *
     * where \t is any whitespace (except a newline)
     */
    public String toString() {
        // some code goes here
        if(fieldnum <= 0)
        {
            throw new UnsupportedOperationException("Implement this");
        }
        StringBuffer tmp = new StringBuffer();
        for(int i = 0;i < fieldnum;i++)
        {
            if(i == fieldnum - 1)
            {
                tmp.append(Objects.toString(fields[i],"null") + "\n");
            }
            else
            {
                tmp.append(Objects.toString(fields[i],"null") + "\t");
            }
        }
        return tmp.toString();
    }

    /**
     * @return
     *        An iterator which iterates over all the fields of this tuple
     * */
    public Iterator<Field> fields()
    {
        // some code goes here
        return new FieldIterator();
    }

    private class FieldIterator implements Iterator<Field>{
        private int count = 0;

        public boolean hasNext() {
            return fieldnum > count;
        }

        public Field next(){
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }
            return fields[count++];
        }
    }

    /**
     * reset the TupleDesc of this tuple (only affecting the TupleDesc)
     * */
    public void resetTupleDesc(TupleDesc td)
    {
        // some code goes here
        tupleDesc = td;
    }
}
