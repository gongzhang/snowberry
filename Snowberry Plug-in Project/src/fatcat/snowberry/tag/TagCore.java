package fatcat.snowberry.tag;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IType;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.internal.ProjectModel;

/**
 * 标签机制核心类。
 * <p>
 * 标签（Tag）为实现代码设计信息融合提供了基础，而所有标签操作均从此类入手。
 * </p>
 * <p>
 * 标签核心类将工作空间中的项目封装成了{@link IProjectModel}，调用{@link #getProjectModels()}方法可以取得当前所有的项目模型。
 * 核心类同时监控了项目的变更情况，可以通过{@link IProjectModelListener}监听这些变更。
 * </p>
 * @author 张弓
 *
 */
final public class TagCore {
	
	/**
	 * 用于识别Snowberry标签的Javadoc标签名。
	 */
	public static final String TAG_NAME = "tag"; //$NON-NLS-1$
	
	private static final ResouceChangeListener RESOUCE_CHANGE_LISTENER;
	private static final HashSet<ProjectModel> PROJECT_MODELS;
	private static final ArrayList<IProjectModelListener> PROJECT_MODEL_LISTENERS;
	
	static {
		RESOUCE_CHANGE_LISTENER = new ResouceChangeListener();
		PROJECT_MODELS = new HashSet<ProjectModel>();
		PROJECT_MODEL_LISTENERS = new ArrayList<IProjectModelListener>();
	}
	
	TagCore() {
	}
	
	public static void start() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(RESOUCE_CHANGE_LISTENER,
				IResourceChangeEvent.POST_CHANGE);
		init();
	}

	public static void stop() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(RESOUCE_CHANGE_LISTENER);
	}
	
	private static void init() {
		PROJECT_MODELS.clear();
		try {
			ResourcesPlugin.getWorkspace().getRoot().accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource.getType() == IResource.PROJECT) {
						ProjectModel projectModel = new ProjectModel((IProject) resource);
						PROJECT_MODELS.add(projectModel);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			SnowberryCore.log(Status.ERROR, International.ErrorWhenVisitResource, e);
		}
		ready_flag = true;
		SnowberryCore.log(Status.INFO, International.Started);
	}
	
	private static class ResouceChangeListener implements IResourceChangeListener, IResourceDeltaVisitor {

		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			
			if (!TagCore.isReady()) return; // bug fixed
			
			Job job = new Job(International.UpdateSnowberry) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						event.getDelta().accept(ResouceChangeListener.this);
					} catch (CoreException e) {
						SnowberryCore.log(Status.ERROR, International.ErrorWhenUpdateResource, e);
					} catch (NullPointerException e) {
						SnowberryCore.log(Status.ERROR, International.ErrorWhenUpdateResource, e);
					}
					monitor.done();
		            return Status.OK_STATUS;  
				}
			};
			job.schedule();
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta.getResource().getType() == IResource.PROJECT) {
				ProjectModel projectModel = (ProjectModel) TagCore.getProjectModel(delta.getResource());
				if (delta.getKind() == IResourceDelta.REMOVED) {
					PROJECT_MODELS.remove(projectModel);
					for (IProjectModelListener listener : PROJECT_MODEL_LISTENERS)
						listener.projectModelRemoved(projectModel);
				} else if (projectModel != null) {
					projectModel.notifyContentChanged(delta);
					for (IProjectModelListener listener : PROJECT_MODEL_LISTENERS)
						listener.projectModelChanged(projectModel);
				} else {
					projectModel = new ProjectModel((IProject) delta.getResource());
					PROJECT_MODELS.add(projectModel);
					for (IProjectModelListener listener : PROJECT_MODEL_LISTENERS)
						listener.projectModelAdded(projectModel);
				}
				return false;
			} else {
				return true;
			}
		}
		
	}
	
	/**
	 * 取得指定资源所在的工程模型。
	 * @param resource 指定的Eclipse资源
	 * @return 工程模型或{@code null}
	 */
	public static IProjectModel getProjectModel(IResource resource) {
		for (IProjectModel projectModel : PROJECT_MODELS) {
			if (projectModel.getResource().equals(resource.getProject())) {
				return projectModel;
			}
		}
		return null;
	}
	
	/**
	 * 取得当前所有的工程模型。
	 * @return 当前所有的工程模型
	 */
	public static IProjectModel[] getProjectModels() {
		return PROJECT_MODELS.toArray(new IProjectModel[0]);
	}
	
	/**
	 * 添加工程模型监听器。
	 * @param listener 监听器
	 */
	public static void addProjectModelListener(IProjectModelListener listener) {
		PROJECT_MODEL_LISTENERS.add(listener);
	}
	
	/**
	 * 移除工程模型监听器。
	 * @param listener 监听器
	 */
	public static void removeProjectModelListener(IProjectModelListener listener) {
		PROJECT_MODELS.remove(listener);
	}
	
	private static boolean ready_flag = false;
	
	/**
	 * 取得<code>TagCore</code>是否启动完成。
	 * @return <code>TagCore</code>是否启动完成
	 */
	public static boolean isReady() {
		return ready_flag;
	}
	
	/**
	 * 查找指定的<code>IJavaElement</code>所对应的类型模型。（优先在制定的工程中查找）
	 * <p>
	 * 若找不到相应的类型模型则返回<code>null</code>。
	 * </p>
	 * @param je 指定的{@code IJavaElement}
	 * @param cur_project 当前的工程
	 * @return 类型模型或{@code null}
	 */
	public static ITypeModel searchTypeModel(IType je, IProjectModel cur_project) {
		
		// 在当前项目中查找
		if (cur_project != null) {
			ITypeModel[] models = cur_project.getITypeModels();
			for (ITypeModel model : models) {
				if (model.getJavaElement().equals(je)) {
					return model;
				}
			}
		}
		
		// 在所有项目中查找
//		final String name = je.getFullyQualifiedName();
		IProjectModel[] projects = TagCore.getProjectModels();
		for (IProjectModel proj : projects) {
			if (proj.equals(cur_project)) continue; // 跳过当前项目
			ITypeModel[] models2 = proj.getITypeModels();
			for (ITypeModel model : models2) {
				if (je.equals(model.getJavaElement())) {
					return model;
				}
			}
		}
		
		return null;
	}

}
