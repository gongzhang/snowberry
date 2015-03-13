package fatcat.snowberry.tag;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;

import fatcat.snowberry.core.International;

/**
 * 支持多文件操作的标签任务包。
 * （与{@linkplain TagTask}类功能相同，但此类能支持多文件操作）
 * 
 * <p>标签任务包，可以向多个<code>IMemberModel</code>追加多个“addTag”、“removeTag”命令，并一次性执行。</p>
 * 
 * @author 张弓
 *
 */
public class MultiFileTask {
	
	private final HashMap<IFile, TagTask> taskMap;
	
	/**
	 * 创建一个空任务包。
	 */
	public MultiFileTask() {
		taskMap = new HashMap<IFile, TagTask>();
	}
	
	private TagTask getTagTask(IMemberModel model) {
		TagTask task = taskMap.get(model.getResource());
		if (task == null) {
			task = new TagTask();
			taskMap.put(model.getResource(), task);
		}
		return task;
	}
	
	/**
	 * 添加标签。
	 * @param model 要添加标签的成员
	 * @param tag 要添加的标签
	 */
	public void addTag(IMemberModel model, ITag tag) {
		getTagTask(model).addTag(model, tag);
	}
	
	/**
	 * 删除标签。
	 * @param model 要删除标签的成员
	 * @param filter 指明删除何种标签
	 */
	public void removeTag(IMemberModel model, ITagFilter filter) {
		getTagTask(model).removeTag(model, filter);
	}
	
	/**
	 * 执行这个任务包中的任务。
	 * 
	 * <p><strong>对于一个示例，此方法只应被调用一次。</strong></p>
	 * 
	 * @throws SourceEditingException
	 */
	public void execute() throws SourceEditingException {
		StringBuffer exceptions = new StringBuffer();
		for (IFile file : taskMap.keySet()) {
			try {
				taskMap.get(file).execute();
			} catch (SourceEditingException e) {
				exceptions.append(e.getMessage());
				exceptions.append("; "); //$NON-NLS-1$
			}
		}
		if (exceptions.length() != 0) {
			throw new SourceEditingException(International.FileOperationFailed + exceptions.toString());
		}
	}

}
