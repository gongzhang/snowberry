package fatcat.snowberry.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.PlatformUI;

import fatcat.snowberry.core.International;
import fatcat.snowberry.tag.internal.MemberModel;
import fatcat.snowberry.tag.internal.Tag;
import fatcat.snowberry.tag.internal.TagParser;

/**
 * 标签任务包。（更推荐使用{@link MultiFileTask}类）
 * <p>
 * 可创建一个或多个增加标签、删除标签任务，并一次性执行。
 * </p>
 * <p>
 * 在执行顺序方面，只能保证同一个<code>IMemberModel</code>上的任务是按添加时的顺寻执行的。
 * </p>
 * <p>
 * 调用{@link #execute()}方法后任务才被执行，并直接写入源文件。
 * </p><p>
 * <strong>注意：</strong>
 * </p><p>
 * 1、一旦执行后，无论成功与否，任务包的剩余内容都是不确定的。
 * </p><p>
 * 2、一个<code>TagTask</code>对象只能接受<strong>来自同一文件</strong>的多个<code>IMemberModel</code>。
 * 如果有多个文件参与标签任务（或不确定是否来自同一文件），应当使用{@link MultiFileTask}类。
 * </p>
 * 
 * @author 张弓
 *
 */
public class TagTask {
	
	final HashMap<IMemberModel, LinkedList<BasicTagTask>> taskMap;
	private IFile file;
	
	/**
	 * 创建一个空的标签任务包。
	 */
	public TagTask() {
		taskMap = new HashMap<IMemberModel, LinkedList<BasicTagTask>>();
		file = null;
	}
	
	/**
	 * 追加一个任务：在指定模型上添加标签。
	 * <p>
	 * <strong>注意：</strong>
	 * </p><p>
	 * <code>model</code>必须和此<code>TagTask</code>对象中的其他模型来自同一文件。
	 * </p>
	 * @param model 添加标签的位置
	 * @param tag 要添加的标签
	 * @author 张弓
	 * @throws IllegalArgumentException <code>model</code>来自不同文件
	 */
	public void addTag(IMemberModel model, ITag tag) {
		if (file == null) {
			file = model.getResource();
		} else if (!file.equals(model.getResource())) {
			throw new IllegalArgumentException(International.IMemberModelMustBeFromSameIFile);
		}

		LinkedList<BasicTagTask> tasks = taskMap.get(model);
		if (tasks == null) {
			tasks = new LinkedList<BasicTagTask>();
			taskMap.put(model, tasks);
		}
		tasks.add(new BasicTagTask(tag));
	}
	
	/**
	 * 追加一个任务：删除指定模型上的指定标签。
	 * <p>
	 * <strong>注意：</strong>
	 * </p><p>
	 * <code>model</code>必须和此<code>TagTask</code>对象中的其他模型来自同一文件。
	 * </p>
	 * @param model 添加标签的位置
	 * @param filter 决定哪个/哪种标签要被删除
	 * @author 张弓
	 * @throws IllegalArgumentException <code>model</code>来自不同文件
	 */
	public void removeTag(IMemberModel model, ITagFilter filter) {
		if (file == null) {
			file = model.getResource();
		} else if (!file.equals(model.getResource())) {
			throw new IllegalArgumentException(International.IMemberModelMustBeFromSameIFile);
		}

		LinkedList<BasicTagTask> tasks = taskMap.get(model);
		if (tasks == null) {
			tasks = new LinkedList<BasicTagTask>();
			taskMap.put(model, tasks);
		}
		tasks.add(new BasicTagTask(filter));
	}
	
	/**
	 * 执行此任务包中的所有任务。
	 * 
	 * <p><strong>对于一个示例，此方法只应被调用一次。</strong></p>
	 * 
	 * @throws SourceEditingException
	 */
	public void execute() throws SourceEditingException {

		if (!PlatformUI.getWorkbench().saveAllEditors(true)) return;
		if (file == null) return;
		
		if (!file.exists()) throw new SourceEditingException(International.FileRemoved + file.getName());
		if (file.isReadOnly()) throw new SourceEditingException(International.FileReadOnly + file.getName());
		ICompilationUnit iCompilationUnit = JavaCore.createCompilationUnitFrom(file);
		if (iCompilationUnit == null) throw new SourceEditingException(International.CannotResolveJavaCode + file.getName());
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setResolveBindings(true);
		parser.setSource(iCompilationUnit);
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		IDocument fDocument = null;
		try {
			fDocument = new Document(iCompilationUnit.getSource());
		} catch (JavaModelException e1) {
			throw new SourceEditingException(International.CannotLoadFromFile + file.getName());
		}
		
		cu.recordModifications();
		
		try {
			cu.accept(new ASTEditor(this));
		} catch (Exception e) {
			throw new SourceEditingException(e.getMessage());
		}
		
		// rewrite
		
		TextEdit edit = null;
		try {
			edit = cu.rewrite(fDocument, JavaCore.getDefaultOptions());
			edit.apply(fDocument);
			String newSource = fDocument.get();
			iCompilationUnit.getBuffer().setContents(newSource);
			iCompilationUnit.getBuffer().save(null, true);
		} catch (Exception e) {
			throw new SourceEditingException(International.CannotSaveToFile + file.getName());
		}

	}

}

class ASTEditor extends ASTVisitor {
	
	final TagTask tagTask;
	
	ASTEditor(TagTask tagTask) {
		this.tagTask = tagTask;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		if (node.fragments().size() > 0) {
			IJavaElement resolved_element = ((VariableDeclarationFragment) node.fragments().get(0)).resolveBinding().getJavaElement();
			findTarget(resolved_element, node);
		}
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		findTarget(node.resolveBinding().getJavaElement(), node);
		return true;
	}
	
	@Override
	public boolean visit(TypeDeclaration node) {
		findTarget(node.resolveBinding().getJavaElement(), node);
		return true;
	}
	
	private void findTarget(IJavaElement resolved_element, BodyDeclaration node) {
		
		// 查找是否有此节点上的任务
		for (IMemberModel member_model : tagTask.taskMap.keySet()) {
			if (member_model.getJavaElement().equals(resolved_element)) {
				
				// 定位Javadoc（没有则创建）
				Javadoc jdoc = node.getJavadoc();
				if (jdoc == null) {
					jdoc = node.getAST().newJavadoc();
					((BodyDeclaration) node).setJavadoc(jdoc);
				}
				
				final AST ast = node.getAST();
				final LinkedList<BasicTagTask> tasks = tagTask.taskMap.get(member_model);
				
				// 依次执行任务
				doTask(tasks, jdoc, ast, member_model);
				
				// 删除这个模型上的任务
				tagTask.taskMap.remove(member_model);
				
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doTask(final LinkedList<BasicTagTask> tasks, final Javadoc jdoc, final AST ast, final IMemberModel model) {
		
		// 依次弹出任务
		while (tasks.size() > 0) {
			final BasicTagTask task = tasks.removeFirst();
			switch (task.type) {
			case BasicTagTask.ADD_TAG:
				
				// 添加标签
				{
					TagElement tag = ast.newTagElement();
					tag.setTagName("@" + TagCore.TAG_NAME); //$NON-NLS-1$
					String[] strs = TagParser.parseStrings(task.tag);
					TextElement text = tag.getAST().newTextElement();
					text.setText(strs[0]);
					tag.fragments().add(text);
					jdoc.tags().add(tag);
					for (int i = 1; i < strs.length; i++)
					{
						TagElement subTag = ast.newTagElement();
						TextElement subText = subTag.getAST().newTextElement();
						subText.setText(strs[i]);
						subTag.fragments().add(subText);
						jdoc.tags().add(subTag);
					};
				}
				
				break;

			case BasicTagTask.REMOVE_TAG:
				
				// 删除标签
				{
					@SuppressWarnings("rawtypes")
					List tags = new ArrayList();
					tags.addAll(jdoc.tags());
					for (Object tag : tags) {
						if (tag instanceof TagElement) {
							TagElement tagElement = (TagElement) tag;
							if (!("@" + TagCore.TAG_NAME).equals(tagElement.getTagName())) //$NON-NLS-1$
								continue;
							List<?> texts = tagElement.fragments();
							StringBuffer tagText = new StringBuffer();
							for (Object text : texts) {
								if (text instanceof TextElement) {
									tagText.append(((TextElement) text).getText());
									tagText.append(" "); //$NON-NLS-1$
								}
							}
							try {
								Tag internalTag = TagParser.parseTag(tagText.toString());
								if (task.filter.isAccepted(internalTag)) {
									tagElement.delete();
								}
							} catch (TagResolveException e) {
								MemberModel.log(model, e);
							}
						}
					}
				}
				
				break;
			}
			
		}
		
		
	}
	
}

class BasicTagTask {
	
	static final int ADD_TAG = 0;
	static final int REMOVE_TAG = 1;
	
	int type;
	ITag tag;
	ITagFilter filter;
	
	BasicTagTask(ITag tag) {
		type = ADD_TAG;
		this.tag = tag;
	}
	
	BasicTagTask(ITagFilter filter) {
		type = REMOVE_TAG;
		this.filter = filter;
	}
	
}
