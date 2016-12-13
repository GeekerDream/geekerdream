package collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 8683452581122892189L;

    /**
     * ArrayList��Ԫ�ص����黺�����洢�����ArrayList���ϵ�Ԫ�أ�
     * transient��ֻ�����������ֶΣ��ڶ������л��Ĺ����У����д����η����ε��ֶ��ǲ��ᱻ���л���
     * ���磺User����userName��transient password����ô���л�User���ʱ��password�ǲ��ᱻ���л��ģ����Է����л��ó�passwordҲ��null
     */
    private transient Object[] elementData;

    /**
     * ����ĳ���
     *
     * @serial
     */
    private int size;

    /**
     * ���ι���������Ҫ����һ����ʼ������Ĭ�Ϲ�������10�ֽڣ��˹����������Զ����С��
     *
     * @param  initialCapacity  ��ʼ����
     * @throws IllegalArgumentException ����ʼ����С��0�����׳��쳣
     */
    public ArrayList(int initialCapacity) {
        //���ø��๹����
    	super();
    	//����ʼ����С��0�����׳��쳣��
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+
                                               initialCapacity);
        //����ʼ�������ڵ���0����newһ������ΪinitialCapacity��Object���飬������elementData����
        this.elementData = new Object[initialCapacity];
    }

    /**
     * �չ�������Ĭ��ռ���Ǹ��ֽڣ���˼���ǣ�������new ArrayList();��ʱ����ʵnew�����ļ�����ռ��ʮ�ֽڵĿռ��
     */
    public ArrayList() {
    	//���ô��ι�����������ʼ����Ϊ10
        this(10);
    }

    /**
     * ���ι�����������һ�����ϣ�Ȼ�󽫴���ļ��ϸ���elementData�����ȸ���size
     *
     * @param c ����ļ���
     */
    public ArrayList(Collection<? extends E> c) {
    	//������ļ���ת��Ϊ���飬����elementData
        elementData = c.toArray();
        //������ļ��ϵĳ��ȸ���size
        size = elementData.length;
        //c.toArray���ܷ��صĲ���Object���飬��ʱ���������bug��(see 6260652)�����ԼӴ��ж�
        if (elementData.getClass() != Object[].class)
        	//��elementData��c.toArray()�������������ͣ����������ĸ��ƣ����Ƶ�����ΪObject����
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    }

    /**
     * �����黺������С������ʵ��ArrayList�洢Ԫ�صĴ�С���ͷŵ�addʱ������1.5�����������Щnull
     * ����elementData = Arrays.copyOf(elementData, size);
     * �÷������û��ֶ����ã��Լ��ٿռ���Դ�˷ѵ�Ŀ�ġ�
     */
    public void trimToSize() {
    	//modCount��AbstractList�����ԣ�protected transient int modCount = 0;
    	//modCount�ñȼ�������ÿ�ζ�List������ɾ����modCount�������++����
        modCount++;
        //�����黺������С(���ܱ�size����Ϊ����1.5������nullռλ��Ҳ���������Ĭ�Ϲ��캯���󣬸����һ��Ԫ�أ���ʱ elementData.length = 10���� size = 1)��ֵ��oldCapacity
        int oldCapacity = elementData.length;
        //���ʵ�ʴ�СС�����黺������С
        if (size < oldCapacity) {
        	//ͨ����һ��������ʹ�ÿռ�õ���Ч���ã������������Դ�˷ѵ����
        	//�����黺����elementData��Ϊʵ�ʴ洢��С��
            elementData = Arrays.copyOf(elementData, size);
        }
    }

    /**
     * Increases the capacity of this <tt>ArrayList</tt> instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > 0)
            ensureCapacityInternal(minCapacity);
    }

    /**
     * ��������Ƿ���Ҫ��������
     * @param minCapacity����С����
     */
    private void ensureCapacityInternal(int minCapacity) {
        modCount++;
        // ��ֹ������룺ȷ��ָ������С�����������黺������ǰ�ĳ���
        //����С�������ڵ�ǰ���黺�������ȣ�����������1.5�����ݣ�������������±�Խ���쳣��
        if (minCapacity - elementData.length > 0)
        	//1.5������
            grow(minCapacity);
    }

    /**
     * ���黺������elementData�������洢����
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * ���ݣ���ȷ��ArrayList�����ܴ洢minCapacity��Ԫ��
     */
    private void grow(int minCapacity) {
        // �����������ȸ��Ƹ�oldCapacity
        int oldCapacity = elementData.length;
        //��ԭ�л��������Ƚ���1.5������
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // �� newCapacity ����С�� minCapacity  
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        // �� newCapacity �������洢����������д���������  
        if (newCapacity - MAX_ARRAY_SIZE > 0)
        	//���д���������  
            newCapacity = hugeCapacity(minCapacity);
        // �������������
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
    
    /**
     * ���������䣬������Integer.MAX_VALUE
     * @param minCapacity
     * @return
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    /**
     * ��������ĳ���
     */
    public int size() {
        return size;
    }

    /**
     * �жϼ����Ƿ�Ϊ��
     *
     * @return true�����鳤��Ϊ0��false�����鳤�Ȳ�Ϊ0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * �жϼ������Ƿ������Ԫ��
     *
     * @return true��������false��������
     */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * ���ص�һ�γ��ֵ�ָ��Ԫ�ص�����,-1�����Ԫ�ز��ټ�����
     */
    public int indexOf(Object o) {
    	//��������������null
        if (o == null) { //�ж�null��Ϊ�˷�ֹ��ָ��
        	//��������
            for (int i = 0; i < size; i++)
            	//��������д���nullԪ�أ��򷵻�null�ڴ˼����е�λ��
                if (elementData[i]==null)
                    return i;
        } else {//�����������ݲ���null
        	//��������
            for (int i = 0; i < size; i++)
            	//��������д��ڴ���Ĵ�Ԫ�أ��򷵻ش�Ԫ���ڼ����е�λ��
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * �������һ�γ��ֵ�ָ��Ԫ�ص�����,-1�����Ԫ�ز��ټ�����
     */
    public int lastIndexOf(Object o) {
    	//��������������null
        if (o == null) { //�ж�null��Ϊ�˷�ֹ��ָ��
        	//�������ϣ������һ����ǰ�������Ŀ�����ҵ����һ�γ��ֵ�ָ��Ԫ�ص�λ��
            for (int i = size-1; i >= 0; i--)
            	//��������д���nullԪ�أ��򷵻�null�ڴ˼����е�λ��
                if (elementData[i]==null)
                    return i;
        } else {//�����������ݲ���null
        	//�������ϣ������һ����ǰ�������Ŀ�����ҵ����һ�γ��ֵ�ָ��Ԫ�ص�λ��
            for (int i = size-1; i >= 0; i--)
            	//��������д��ڴ���Ĵ�Ԫ�أ��򷵻ش�Ԫ���ڼ����е�λ��
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * ����һ��ǳ������ArrayList
     *
     */
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
                ArrayList<E> v = (ArrayList<E>) super.clone();
            //��������elementData��v.elementData
            v.elementData = Arrays.copyOf(elementData, size);
            //����仯�Ĵ�������Ϊ0
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    /**
     * ������ת��ΪObject����
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array.  If the list fits in the
     * specified array, it is returned therein.  Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this list.
     *
     * <p>If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list), the element in
     * the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of the
     * list <i>only</i> if the caller knows that the list does not contain
     * any null elements.)
     *
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // Positional Access Operations

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * �����±�Ӽ�����ȡ��Ԫ��
     */
    public E get(int index) {
    	//����Ƿ�ᷢ���쳣
        rangeCheck(index);
        //ֱ�ӷ��������е�index��λ�õ�Ԫ��ֵ
        return elementData(index);
    }

    /**
     * �ڵ�indexλ�ò���elementԪ��
     *
     * @param index �����е�indexλ��
     * @param element �������Ԫ��
     * @return ������Ԫ��֮ǰ�ļ����е�indexλ��Ԫ��
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E set(int index, E element) {
    	//����Ƿ�ᷢ���쳣
        rangeCheck(index);
        //�ȴӼ�����ȡ���±�Ϊindex�ĵ�Ԫ��ֵ����ֵ��oldValue
        E oldValue = elementData(index);
        //����Ԫ�����¸�ֵ��elementData[index]
        elementData[index] = element;
        //���ز�����Ԫ��֮ǰ�ļ����е�indexλ��Ԫ�أ������ݣ�
        return oldValue;
    }

    /**
     * ��Ԫ����ӵ�����
     *
     * @param e Ԫ��
     */
    public boolean add(E e) {
    	//��������
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //������һ���Ĳ������˲���Ͳ�����������±�Խ������
        elementData[size++] = e;
        return true;
    }

    /**
     * ��ָ��λ�ò���ָ��Ԫ��
     *
     * @param ָ��λ��
     * @param Ԫ��
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public void add(int index, E element) {
    	//����쳣
        rangeCheckForAdd(index);
    	//��������
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        //System.arraycopy(src, srcPos, dest, destPos, length);
        //��һ����Ҫ���Ƶ����飬�ڶ����Ǵ�Ҫ���Ƶ�����ĵڼ�����ʼ���������Ǹ��Ƶ��ǣ��ĸ��Ǹ��Ƶ�������ڼ�����ʼ�����һ���Ǹ��Ƴ���
        System.arraycopy(elementData, index, elementData, index + 1,
                         size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * �Ӽ������Ƴ��±�Ϊindex��Ԫ��
     *
     * @param �Զ����±�
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public E remove(int index) {
        //����쳣
    	rangeCheck(index);

        modCount++;
        //�ҵ�index��Ԫ��ֵ
        E oldValue = elementData(index);
        
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        //�����һ��Ԫ������Ϊnull
        elementData[--size] = null; // Let gc do its work
        //���ر��Ƴ���ֵ
        return oldValue;
    }

    /**
     * ɾ����һ�γ����ڼ����б��е�Ԫ��
     * @param o �Զ���Ҫɾ����Ԫ��
     * 
     * ע�⣺�ǵ�һ��������ȫ��
     */
    public boolean remove(Object o) {
        if (o == null) { // ɾ����һ��null
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                	//�Ƴ�
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
            	//�����������ƥ���Ԫ�أ����Ƴ�
                if (o.equals(elementData[index])) {
                	//�Ƴ�
                	fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /*
     * �Ƴ�ָ��λ�õ�Ԫ��
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        //�����������һ��Ԫ������Ϊnull
        elementData[--size] = null; // Let gc do its work
    }

    /**
     * �������
     */
    public void clear() {
        modCount++;
        
        //ѭ����ǰ����
        for (int i = 0; i < size; i++)
        	//�������ÿһ��Ԫ�ض�����Ϊnull
            elementData[i] = null;
        //��������Ϊ0
        size = 0;
    }

    /**
     * ���Ϻϲ�
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        //����Ƿ���Ҫ����
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                             numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     *
     * @throws IndexOutOfBoundsException if {@code fromIndex} or
     *         {@code toIndex} is out of range
     *         ({@code fromIndex < 0 ||
     *          fromIndex >= size() ||
     *          toIndex > size() ||
     *          toIndex < fromIndex})
     */
    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex,
                         numMoved);

        // Let gc do its work
        int newSize = size - (toIndex-fromIndex);
        while (size != newSize)
            elementData[--size] = null;
    }

    /**
     * ��ȫ��ʩ������Ƿ�ᷢ�������±�Խ���쳣
     */
    private void rangeCheck(int index) {
    	//�������ĳ��ȴ�������ĳ��ȣ����׳��쳣
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * ��ȫ��ʩ������Ƿ�ᷢ�������±�Խ���쳣
     */
    private void rangeCheckForAdd(int index) {
    	//�������ĳ��ȴ�������ĳ��ȣ���С��0���׳��쳣
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * ��ӡ�쳣��־
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection.
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection.  In other words, removes from this list all
     * of its elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            if (r != size) {
                System.arraycopy(elementData, r,
                                 elementData, w,
                                 size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Save the state of the <tt>ArrayList</tt> instance to a stream (that
     * is, serialize it).
     *
     * @serialData The length of the array backing the <tt>ArrayList</tt>
     *             instance is emitted (int), followed by all of its elements
     *             (each an <tt>Object</tt>) in the proper order.
     */
    private void writeObject(java.io.ObjectOutputStream s)
        throws java.io.IOException{
        // Write out element count, and any hidden stuff
        int expectedModCount = modCount;
        s.defaultWriteObject();

        // Write out array length
        s.writeInt(elementData.length);

        // Write out all elements in the proper order.
        for (int i=0; i<size; i++)
            s.writeObject(elementData[i]);

        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }

    }

    /**
     * Reconstitute the <tt>ArrayList</tt> instance from a stream (that is,
     * deserialize it).
     */
    private void readObject(java.io.ObjectInputStream s)
        throws java.io.IOException, ClassNotFoundException {
        // Read in size, and any hidden stuff
        s.defaultReadObject();

        // Read in array length and allocate array
        int arrayLength = s.readInt();
        Object[] a = elementData = new Object[arrayLength];

        // Read in all elements in the proper order.
        for (int i=0; i<size; i++)
            a[i] = s.readObject();
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
     * {@code fromIndex} and {@code toIndex} are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations.
     *
     * <p>This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>
     *      list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for {@link #indexOf(Object)} and
     * {@link #lastIndexOf(Object)}, and all of the algorithms in the
     * {@link Collections} class can be applied to a subList.
     *
     * <p>The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> parent,
                int offset, int fromIndex, int toIndex) {
            this.parent = parent;
            this.parentOffset = fromIndex;
            this.offset = offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = ArrayList.this.modCount;
        }

        public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }

        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }

        public int size() {
            checkForComodification();
            return this.size;
        }

        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
            parent.add(parentOffset + index, e);
            this.modCount = parent.modCount;
            this.size++;
        }

        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            parent.removeRange(parentOffset + fromIndex,
                               parentOffset + toIndex);
            this.modCount = parent.modCount;
            this.size -= toIndex - fromIndex;
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(this.size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;

            checkForComodification();
            parent.addAll(parentOffset + index, c);
            this.modCount = parent.modCount;
            this.size += cSize;
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        public ListIterator<E> listIterator(final int index) {
            checkForComodification();
            rangeCheckForAdd(index);
            final int offset = this.offset;

            return new ListIterator<E>() {
                int cursor = index;
                int lastRet = -1;
                int expectedModCount = ArrayList.this.modCount;

                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @SuppressWarnings("unchecked")
                public E next() {
                    checkForComodification();
                    int i = cursor;
                    if (i >= SubList.this.size)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i + 1;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @SuppressWarnings("unchecked")
                public E previous() {
                    checkForComodification();
                    int i = cursor - 1;
                    if (i < 0)
                        throw new NoSuchElementException();
                    Object[] elementData = ArrayList.this.elementData;
                    if (offset + i >= elementData.length)
                        throw new ConcurrentModificationException();
                    cursor = i;
                    return (E) elementData[offset + (lastRet = i)];
                }

                public int nextIndex() {
                    return cursor;
                }

                public int previousIndex() {
                    return cursor - 1;
                }

                public void remove() {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        SubList.this.remove(lastRet);
                        cursor = lastRet;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void set(E e) {
                    if (lastRet < 0)
                        throw new IllegalStateException();
                    checkForComodification();

                    try {
                        ArrayList.this.set(offset + lastRet, e);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                public void add(E e) {
                    checkForComodification();

                    try {
                        int i = cursor;
                        SubList.this.add(i, e);
                        cursor = i + 1;
                        lastRet = -1;
                        expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (expectedModCount != ArrayList.this.modCount)
                        throw new ConcurrentModificationException();
                }
            };
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        private void rangeCheck(int index) {
            if (index < 0 || index >= this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > this.size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }
    }
}
