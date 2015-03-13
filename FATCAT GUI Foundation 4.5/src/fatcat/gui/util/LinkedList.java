/**
 * @(#)LinkedList.java	2.10 2009-9-2
 *
 * FATCAT团队，北京航空航天大学，2009。版权所有，保留所有权利。
 * FATCAT PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * (http://www.cnblogs.com/fatcat)
 */
package fatcat.gui.util;

/**
 * 链表数据结构。
 * @author 张弓
 * @version 2.10
 */
public class LinkedList {

	private final Node head;
	private Node tail;
	private int size;

	/**
	 * 创建一个新链表。
	 */
	public LinkedList() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		size = 0;
	}

	/**
	 * 向链表尾部追加一个数据。
	 * @param data 要追加的数据
	 * @return 追加后的链表结点总数数
	 */
	public final int append(Object data) {
		tail.data = data;
		tail = tail.next = new Node();
		return ++size;
	}

	/**
	 * 查找数据<code>data</code>在链表中的索引。
	 * @param data 要查找的数据
	 * @return <code>data</code>在链表中的索引
	 */
	public final int indexOf(Object data) {
		Node ptr = head.next;
		int i = 0;
		while (ptr != tail) {
			if (ptr.data == data) {
				return i;
			}
			ptr = ptr.next;
			i++;
		}
		return -1;
	}

	/**
	 * 查找数据<code>data</code>在链表中的索引。
	 * @param data 要查找的数据
	 * @return <code>data</code>在链表中的索引
	 * @deprecated 已被<code>indexOf(Object)</code>方法替代
	 * @see #indexOf(java.lang.Object) 查找数据索引
	 */
	public final int getIndexOf(Object data) {
		return indexOf(data);
	}

	/**
	 * 在数据<code>before</code>之前插入新数据<code>data</code>。
	 * @param data 要插入的数据
	 * @param before 已经存在于链表中的数据
	 * @return 是否插入成功
	 */
	public final boolean insert(Object data, Object before) {
		Node ptr = head;
		while (ptr.next != tail) {
			if (ptr.next.data == before) {
				Node newNode = new Node(data);
				newNode.next = ptr.next;
				ptr.next = newNode;
				size++;
				return true;
			} else {
				ptr = ptr.next;
			}
		}
		return false;
	}

	/**
	 * 删除链表中的数据<code>data</code>。
	 * @param data 要删除的数据
	 * @return 是否成功删除
	 */
	public final boolean remove(Object data) {
		Node ptr = head;
		while (ptr.next != tail) {
			if (ptr.next.data == data) {
				ptr.next = ptr.next.next;
				size--;
				return true;
			} else {
				ptr = ptr.next;
			}
		}
		return false;
	}

	/**
	 * 删除链表中的指定数据。
	 * @param index 要删除数据的索引
	 * @return 是否成功删除
	 */
	public final boolean remove(int index) {
		if (index >= 0 && index < size) {
			Node ptr = head;
			int i = 0;
			while (i != index) {
				ptr = ptr.next;
				i++;
			}
			ptr.next = ptr.next.next;
			size--;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 测试链表中是否存在数据<code>data</code>。
	 * 返回值等价于表达式<code>indexOf(data) != -1</code>的值。
	 * @param data 待检索的数据
	 * @return 链表中是否存在数据<code>data</code>
	 */
	public final boolean has(Object data) {
		return indexOf(data) != -1;
	}

	/**
	 * 移除链表中的所有数据。
	 */
	public final void removeAll() {
		head.next = tail = new Node();
		size = 0;
	}

	/**
	 * 取得链表中的节点数。
	 * @return 链表中的节点数。
	 */
	public final int size() {
		return size;
	}

	/**
	 * 根据索引取得数据。
	 * 头结点索引为<code>0</code>，尾节点索引为<code>size() - 1</code>。
	 * @param index 指定索引
	 * @return 索引所表示的数据
	 */
	public final Object get(int index) {
		if (index >= 0 && index < size) {
			Node ptr = head.next;
			int i = 0;
			while (i != index) {
				ptr = ptr.next;
				i++;
			}
			return ptr.data;
		} else {
			return null;
		}
	}

	/**
	 * 取得链表中头结点数据。
	 * @return 头结点数据；如不存在则返回<code>null</code>
	 */
	public final Object getHead() {
		return head.next.data;
	}

	/**
	 * 取得链表中尾结点数据。
	 * @return 尾结点数据；如不存在则返回<code>null</code>
	 */
	public final Object getTail() {
		Node ptr = head;
		while (ptr.next != tail) {
			ptr = ptr.next;
		}
		return ptr.data;
	}

	/**
	 * 从链表头部弹出一个数据。
	 * @return 头结点数据；如不存在则返回<code>null</code>
	 */
	public final Object popHead() {
		if (size > 0) {
			Object rst = head.next.data;
			head.next = head.next.next;
			size--;
			return rst;
		} else {
			return null;
		}
	}

	/**
	 * 从链表尾部弹出一个数据。
	 * @return 尾结点数据；如不存在则返回<code>null</code>
	 */
	public final Object popTail() {
		if (size > 0) {
			Node ptr = head;
			while (ptr.next != tail) {
				ptr = ptr.next;
			}
			Object rst = ptr.data;
			ptr.data = null;
			ptr.next = null;
			tail = ptr;
			size--;
			return rst;
		} else {
			return null;
		}
	}

	/**
	 * 从头部压入数据<code>data</code>。
	 * @param data 要添加的数据
	 * @return 压入后的链表结点数
	 */
	public final int pushHead(Object data) {
		Node newNode = new Node(data);
		newNode.next = head.next;
		head.next = newNode;
		return ++size;
	}

	/**
	 *
	 * @return
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[LinkedList](Size = " + size + ")\n");
		Node ptr = head.next;
		int i = 0;
		while (ptr != tail) {
			sb.append("\t[" + i + "]" + ptr.data + "\n");
			i++;
			ptr = ptr.next;
		}
		sb.append("====End====");
		return sb.toString();
	}

	/**
	 * 复制此链表。
	 * @return 复制的链表引用。
	 */
	public final LinkedList clone() {
		LinkedList rst = new LinkedList();
		Node ptr = head.next;
		while (ptr != tail) {
			rst.append(ptr.data);
			ptr = ptr.next;
		}
		return rst;
	}

	/**
	 * 取得一个此链表的迭代器。
	 * @return 此链表的迭代器
	 */
	public final Iterator iterator() {
		return new Iterator() {

			Node ptr = head;

			public final boolean hasNext() {
				return ptr.next != tail;
			}

			public final Object next() {
				if (hasNext()) {
					ptr = ptr.next;
				}
				return ptr.data;
			}
		};
	}
}

class Node {

	Object data;
	Node next;

	public Node() {
		this(null);
	}

	public Node(Object data) {
		this.data = data;
		next = null;
	}
}
