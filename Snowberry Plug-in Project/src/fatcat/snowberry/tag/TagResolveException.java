package fatcat.snowberry.tag;

/**
 * 无法从特定的字符串解析出标签时抛出此异常。
 * @author 张弓
 */
public class TagResolveException extends Exception {
	
	private static final long serialVersionUID = -7118521677859361165L;

	public TagResolveException() {
		super();
	}
	
	public TagResolveException(String msg) {
		super(msg);
	}

}
