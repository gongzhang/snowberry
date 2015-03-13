package fatcat.snowberry.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.dp.DesignPatternCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagFilter;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.TagCore;

/**
 * 提供基本的搜索功能。
 * 
 * @author 张弓
 *
 */
public final class SearchCore {
	
	/**
	 * 搜索拥有指定标签的成员模型。
	 * @param results 监听搜索过程和结果的监听器
	 * @param filter 符合要求的标签过滤器
	 */
	public static void searchMember(final IResultListener<IMemberModel> results, final ITagFilter filter) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final IProjectModel[] projs = TagCore.getProjectModels();
				for (IProjectModel proj : projs) {
					final ITypeModel[] types = proj.getITypeModels();
					for (ITypeModel type : types) {
						if (searchTag(type)) {
							results.done(true);
							return;
						}
						final IMemberModel[] members = type.getMemberModels();
						for (IMemberModel member : members) {
							if (searchTag(member)) {
								results.done(true);
								return;
							}
						}
					}
				}
				results.done(false);
			}
			
			private boolean searchTag(IMemberModel model) {
				final ITag[] tags = model.getTags();
				for (ITag tag : tags) {
					if (filter.isAccepted(tag)) {
						return results.gotResult(model);
					}
				}
				return false;
			}
			
		}).start();
	}
	
	/**
	 * “AnyConnection”搜索。
	 * @param results 监听搜索过程和结果的监听器
	 * @param model 指定的类型模型
	 * @deprecated see {@link #anyConnection2(IResultListener, ITypeModel)}
	 */
	@Deprecated
	public static void anyConnection(final IResultListener<ITypeModel> results, final ITypeModel model) {
		final Label[] labels = Label.parseLabels(model);
		new Thread(new Runnable() {
			
			final class RefFinder extends ASTVisitor {
				
				final ITypeModel target;
				int result;
				ITypeModel searchTarget = null;
				
				RefFinder(ITypeModel target) {
					this.target = target;
					result = 0;
				}
				
				@Override
				public boolean visit(SimpleName node) {
					IBinding binding = node.resolveBinding();
					if (binding == null) return true;
					
					if (target.getJavaElement().equals(binding.getJavaElement())) {
						result++;
					}
					return true;
				}
				
				@Override
				public boolean equals(Object arg0) {
					if (arg0 == null) return false;
					if (arg0 instanceof RefFinder) {
						return searchTarget.equals(((RefFinder) arg0).searchTarget);
					} else {
						return false;
					}
				}
				
			}
			
			@Override
			public void run() {
				final IProjectModel[] projs = TagCore.getProjectModels();
				for (IProjectModel proj : projs) {
					final ITypeModel[] types = proj.getITypeModels();
					for (ITypeModel type : types) {
						if ((!type.equals(model)) && searchLabel(type)) {
							results.done(true);
							return;
						}
					}
				}
				for (IProjectModel proj : projs) {
					final ITypeModel[] types = proj.getITypeModels();
					for (ITypeModel type : types) {
						final IMemberModel[] members = type.getMemberModels();
						for (IMemberModel member : members) {
							if (searchLabel(member)) {
								results.done(true);
								return;
							}
						}
					}
				}
				
				final LinkedList<RefFinder> refResult = new LinkedList<RefFinder>();
				for (IProjectModel proj : projs) {
					final ITypeModel[] types = proj.getITypeModels();
					for (ITypeModel type : types) {
						if (!type.equals(model)) {
							RefFinder rst = searchRef(type);
							if (rst.result != 0 && !(refResult.contains(rst))) refResult.add(rst);
						}
					}
				}
				
				final RefFinder[] result = refResult.toArray(new RefFinder[0]);
				Arrays.sort(result, new Comparator<RefFinder>() {

					@Override
					public int compare(RefFinder o1, RefFinder o2) {
						return o1.result - o2.result;
					}
					
				});
				
				for (RefFinder ref : result) {
					if (results.gotResult(ref.searchTarget)) {
						results.done(true);
						return;
					}
				}
				
				results.done(false);
			}
			
			
			private boolean searchLabel(IMemberModel model) {
				final Label[] ls = Label.parseLabels(model);
				for (Label l1 : ls) {
					for (Label l2 : labels) {
						if (l2.equals(l1)) {
							return results.gotResult(model.getKind() == IMemberModel.TYPE ? ((ITypeModel) model) : (model.getOwnerType()));
						}
					}
				}
				return false;
			}
			
			private RefFinder searchRef(ITypeModel target) {
				RefFinder rst = new RefFinder(model);
				rst.searchTarget = target;
				target.getASTNode().accept(rst);
				return rst;
			}
			
		}).start();
	}
	
	/**
	 * “AnyConnection”搜索。
	 * @param results 监听搜索过程和结果的监听器
	 * @param target 目标模型
	 */
	public static void anyConnection2(final IResultListener<IConnection> results, final ITypeModel target) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final HashSet<Label> target_labels = new HashSet<Label>();
				Label[] labels = Label.parseLabels(target);
				for (Label label : labels) target_labels.add(label);
				
				final HashSet<DesignPattern> target_patterns = new HashSet<DesignPattern>();
				DesignPattern[] patterns = DesignPatternCore.getPatterns(target);
				for (DesignPattern pattern : patterns) target_patterns.add(pattern);
				
				final LinkedList<IConnection> rst = new LinkedList<IConnection>();
				final HashSet<ITypeModel> searched_models = new HashSet<ITypeModel>();
				final IProjectModel[] projs = TagCore.getProjectModels();
				for (IProjectModel proj : projs) {
					final ITypeModel[] types = proj.getITypeModels();
					for (ITypeModel type : types) {
						if ((!searched_models.contains(type)) && (!type.equals(target))) {
							// 如果找到有关联的类，就加入rst
							IConnection c = Connection.createConnection(target, type, target_labels, target_patterns);
							if (c != null) {
								rst.add(c);
							}
							searched_models.add(type);
						}
					}
				}
				
				// 结果排序
				IConnection[] connections = rst.toArray(new IConnection[0]);
				Arrays.sort(connections, new Comparator<IConnection>() {
					@Override
					public int compare(IConnection o1, IConnection o2) {
						final double rst = o1.getLambda() - o2.getLambda();
						return rst > 0.0 ? -1 : (rst < 0.0 ? 1 : 0);
					}
				});
				
				// 输出结果
				for (IConnection connection : connections) {
					if (results.gotResult(connection)) {
						results.done(true);
						return;
					}
				}
				
				results.done(false);
			}
			
		}).start();
	}
	
	public static IConnection createConnection(ITypeModel target, ITypeModel result) {
		final HashSet<Label> target_labels = new HashSet<Label>();
		Label[] labels = Label.parseLabels(target);
		for (Label label : labels) target_labels.add(label);
		
		final HashSet<DesignPattern> target_patterns = new HashSet<DesignPattern>();
		DesignPattern[] patterns = DesignPatternCore.getPatterns(target);
		for (DesignPattern pattern : patterns) target_patterns.add(pattern);
		
		return Connection.createConnection(target, result, target_labels, target_patterns);
	}

}
