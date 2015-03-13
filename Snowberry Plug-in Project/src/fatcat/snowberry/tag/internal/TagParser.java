package fatcat.snowberry.tag.internal;

import java.util.ArrayList;
import java.util.LinkedList;

import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.TagResolveException;

public class TagParser {
	
	/**
	 * 根据约定格式的字符串生成标签实例。
	 * <p>
	 * 文法：<i>
	 * <p>
	 * <标签信息> ::= <标识符>{<标识符> = "<字串>"}
	 * </p>
	 * <p>
	 * <标识符> ::= <标识符字符>{<标识符字符>}
	 * </p>
	 * <p>
	 * <标识符字符> ::= 字母 | 数字 | 下划线 | 点 | 减号
	 * </p>
	 * <p>
	 * <字串> ::= {非双引号的任意字符}
	 * </p>
	 * </i></p>
	 * 
	 * @param descriptionString 标签信息字串
	 * @return 标签
	 * @see #createTagDescriptionString(ITag, boolean) 逆操作
	 */
	public static Tag parseTag(String string) throws TagResolveException {
		TagParser parser = new TagParser();
		parser.tokenize(string.toCharArray());
		return parser.parse();
	}
	
	@Deprecated
	private static final String ENDL = System.getProperty("line.separator");
	
	/**
	 * 根据指定的标签，生成相应约定格式的描述字符串。
	 * <p>
	 * 在生成的字串中应包含标签名和属性表的所有信息：
	 * </p>
	 * <p>
	 * <i> Name Property1 = "Value1" Property2 = "Value2" ... </i>
	 * </p>
	 * 
	 * <p><strong>注意：</strong></p>
	 * <p>
	 * 生成多行文本的功能已经废弃，因此<code>allowMultiline</code>参数应当始终指定为<code>false</code>。
	 * 要生成多行文本时，请参见{@link #parseStrings(ITag)}方法。
	 * </p>
	 * 
	 * @param tag
	 *            给定的标签
	 * @param allowMultiline 已过时，应当始终指定{@code false}
	 * @throws IllegalArgumentException 若<code>allowMultiline</code>参数为<code>true</code>时抛出此异常
	 * @return 描述字符串
	 * @see #createTag(String) 逆操作
	 */
	public static String parseString(ITag tag, boolean allowMultiline) {
		
		// 输出多行文本的功能已经去除
		if (allowMultiline) throw new IllegalArgumentException("allowMultiline参数不能为true");
		
		StringBuffer str = new StringBuffer();
		String spl = allowMultiline ? (ENDL + " * \t") : " ";
		str.append(tag.getName());
		for (int i = 0; i < tag.size(); i++) {
			str.append(spl);
			str.append(tag.getPropertyName(i));
			str.append(" = \"");
			str.append(tag.getPropertyValue(i));
			str.append("\"");
		}
		return str.toString();
	}
	
	/**
	 * 根据标签生成约定格式的描述字符串。
	 * <p>
	 * 返回的字符串数组是按顺序排列的多行内容。
	 * </p>
	 * <code>
	 * result[0] == "Name"
	 * <br>result[1] == "Property1 = "Value1""
	 * <br>result[2] == "Property2 = "Value2""
	 * <br>result[3] == "Property3 = "Value3""
	 * <br>...
	 * </code>
	 * 
	 * @param tag 指定的标签
	 * @return 分行存储的字符串
	 */
	public static String[] parseStrings(ITag tag) {
		LinkedList<String> rst = new LinkedList<String>();
		rst.add(tag.getName());
		for (int i = 0; i < tag.size(); i++) {
			rst.add(tag.getPropertyName(i) + " = \"" + tag.getPropertyValue(i) + "\"");
		}
		return rst.toArray(new String[0]);
	}
	
	final ArrayList<Token> tokens;
	int cur = 0;
	
	public TagParser() {
		tokens = new ArrayList<Token>();
	}
	
	Token nextToken() {
		if (cur < tokens.size()) {
			return tokens.get(cur++);
		} else {
			return new Token();
		}
	}
	
	public Tag parse() throws TagResolveException {
		Tag tag = new Tag();
		Token token = nextToken();
		if (token.type == Token.TYPE_ID) {
			tag.setName(token.data);
			while ((token = nextToken()).type != Token.TYPE_UNKNOWN) {
				String name, value;
				if (token.type != Token.TYPE_ID) throw new TagResolveException("lack of property name");
				name = token.data;
				if (nextToken().type != Token.TYPE_EQUAL) throw new TagResolveException("lack of equal(=)");
				if (nextToken().type != Token.TYPE_QUOTES) throw new TagResolveException("lack of quotes(\")");
				if ((token = nextToken()).type != Token.TYPE_STRING) throw new TagResolveException("lack of property value");
				value = token.data;
				if (nextToken().type != Token.TYPE_QUOTES) throw new TagResolveException("lack of quotes(\")");
				tag.addProperty(name, value);
			}
			return tag;
		} else
			throw new TagResolveException("lack of tag name");
	}
	
	public void tokenize(char[] input) {
		int p = 0, cp = 0;
		int state = Token.TYPE_UNKNOWN;
		while (cp != input.length) {
			switch (state) {
			case Token.TYPE_UNKNOWN:
				if (Character.isWhitespace(input[cp])) {
					cp++;
				} else if (isIDHeader(input[cp])) {
					state = Token.TYPE_ID;
				} else if (input[cp] == '=') {
					Token token = new Token();
					token.type = Token.TYPE_EQUAL;
					token.data = "=";
					tokens.add(token);
					cp++;
				} else if (input[cp] == '\"') {
					Token token = new Token();
					token.type = Token.TYPE_QUOTES;
					token.data = "\"";
					tokens.add(token);
					state = Token.TYPE_STRING;
					cp++;
				} else {
					Token token = new Token();
					token.type = Token.TYPE_UNKNOWN;
					tokens.add(token);
					cp++;
				}
				p = cp;
				break;
				
			case Token.TYPE_ID:
				if (isIDHeader(input[cp])) {
					cp++;
				} else {
					Token token = new Token();
					token.type = Token.TYPE_ID;
					token.data = new String(input).substring(p, cp);
					tokens.add(token);
					p = cp;
					state = Token.TYPE_UNKNOWN;
				}
				break;
				
			case Token.TYPE_STRING:
				if (input[cp] != '\"') {
					cp++;
				} else {
					Token token = new Token();
					token.type = Token.TYPE_STRING;
					token.data = new String(input).substring(p, cp);
					tokens.add(token);
					token = new Token();
					token.type = Token.TYPE_QUOTES;
					token.data = "\"";
					tokens.add(token);
					cp++;
					p = cp;
					state = Token.TYPE_UNKNOWN;
				}
				break;
			}
		}
	}
	
	/*
	 * 字母 | 数字 | 下划线 | 点 | 减号
	 */
	static boolean isIDHeader(char ch) {
		return Character.isLetterOrDigit(ch) ||
			ch == '_' ||
			ch == '.' ||
			ch == '-';
	}

}

class Token {
	
	static final int TYPE_UNKNOWN = 0;
	static final int TYPE_ID = 1;
	static final int TYPE_EQUAL = 2;
	static final int TYPE_QUOTES = 3;
	static final int TYPE_STRING = 4;
	
	int type = TYPE_UNKNOWN;
	String data = "";

}
