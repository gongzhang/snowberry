package fatcat.snowberry.tag;

/**
 * 当源代码添加、修改、删除失败时抛出此异常。
 * @author 张弓
 *
 */
public class SourceEditingException extends Exception {

	private static final long serialVersionUID = 81287570073862414L;

	public SourceEditingException() {
		super();
	}
	
	public SourceEditingException(String msg) {
		super(msg);
	}

}
