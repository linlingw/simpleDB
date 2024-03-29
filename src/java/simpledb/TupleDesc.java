package simpledb;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {


    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        public final Type fieldType;
        
        /**
         * The name of the field
         * */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        // some code goes here
        return new IDItemIterator();
    }

    private class IDItemIterator implements  Iterator<TDItem>{
        private int count = 0;

        public boolean hasNext() {
            return (tdItemList.size() > count);
        }

        public TDItem next(){
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }
            else
            {
                return tdItemList.get(count++);
            }
        }
    }/***/

    private static final long serialVersionUID = 1L;

    private List<TDItem> tdItemList;/***/

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
        tdItemList = new ArrayList<>();
        for(int i = 0;i < typeAr.length;i++)
        {
            tdItemList.add(new TDItem(typeAr[i],fieldAr[i]));
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
        tdItemList = new ArrayList<>();
        for(int i = 0;i < typeAr.length;i++)
        {
            tdItemList.add(new TDItem(typeAr[i],null));
        }
    }

    public TupleDesc(List<TDItem> result0) {
        tdItemList = new ArrayList<>();
        for(int i = 0;i < result0.size();i++)
        {
            tdItemList.add(new TDItem(result0.get(i).fieldType,result0.get(i).fieldName));
        }
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItemList.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        if(i < 0 || i >= numFields())
        {
            throw new NoSuchElementException();
        }
        return tdItemList.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        if(i < 0 || i >= numFields())
        {
            throw new NoSuchElementException();
        }
        return tdItemList.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
        if(name == null)
        {
            throw new NoSuchElementException();
        }
        for(int i = 0;i < numFields();i++)
        {
            if(tdItemList.get(i).fieldName != null)
            {
                if(tdItemList.get(i).fieldName.equals(name))
                {
                    return i;
                }
            }
        }
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
        int totalsize = 0;
        for(int i = 0;i < numFields();i++)
        {
            totalsize += tdItemList.get(i).fieldType.getLen();
        }
        return totalsize;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
        List<TDItem> tmp1 = td1.tdItemList;
        List<TDItem> tmp2 = td2.tdItemList;
        int leng1 = tmp1.size();
        int leng2 = tmp2.size();
        List<TDItem> result0 = null;
        result0 = new ArrayList<>();
        for(int i = 0;i < tmp1.size();i++)
        {
            result0.add(new TDItem(tmp1.get(i).fieldType,tmp1.get(i).fieldName));
        }
        for(int i = 0;i < tmp2.size();i++)
        {
            result0.add(new TDItem(tmp2.get(i).fieldType,tmp2.get(i).fieldName));
        }
        return new TupleDesc(result0);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */

    public boolean equals(Object o) {
        // some code goes here
        if(this == o)
        {
            return true;
        }
        if(o instanceof TupleDesc)
        {
            TupleDesc tmp = (TupleDesc) o;
            if(numFields() != tmp.numFields())
            {
                return false;
            }
            for(int i = 0;i < numFields();i++)
            {
                if((tdItemList.get(i).fieldName != tmp.tdItemList.get(i).fieldName) || (tdItemList.get(i).fieldType != tmp.tdItemList.get(i).fieldType))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        StringBuffer result0 =  new StringBuffer();
        result0.append("(");
        for(int i = 0;i < numFields();i++)
        {
            result0.append(tdItemList.get(i).toString() + ",");
        }
        result0.append(")");
        return result0.toString();
    }
}
