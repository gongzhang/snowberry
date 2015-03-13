package fatcat.snowberry.search;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

import fatcat.gui.snail.Container;
import fatcat.snowberry.diagram.ClassDiagram;
import fatcat.snowberry.diagram.ConnectionLine;
import fatcat.snowberry.diagram.Label;
import fatcat.snowberry.dp.DesignPattern;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.ITypeModel;

/**
 * 联系描述器。
 * @author 张弓
 */
public interface IConnection {
	
	public ITypeModel getTarget();
	
	public ITypeModel getResult();
	
	/**
	 * 取得搜索目标和结果直接的联系紧密程度。
	 * @return 大于0.0的浮点数
	 */
	public double getLambda();
	
	public int getReferencedCount();
	
	public Label[] getLabels();
	
	public DesignPattern[] getPatterns();
	
	public ConnectionLine createLine(Container owner, ClassDiagram src, ClassDiagram dst);
	
	public ConnectionLine getLine();
	
	public void setLambda(double lambda);
	
}

class Connection implements IConnection {
	
	final ITypeModel result, target;
	private int referenced_cnt = 0;
	private HashSet<Label> labels = new HashSet<Label>();
	private HashSet<DesignPattern> patterns = new HashSet<DesignPattern>();
	
	static IConnection createConnection(ITypeModel target, ITypeModel result, HashSet<Label> target_labels, HashSet<DesignPattern> target_patterns) {
		
		final Connection rst = new Connection(target, result);
		
		// 查找设计模式
		for (DesignPattern pattern : target_patterns) {
			if (pattern.getRole(result) != null) {
				rst.patterns.add(pattern);
			}
		}
		
		// 查找标签
		final Label[] result_labels = Label.parseLabels(result);
		for (Label label : result_labels) {
			if (containsLabel(target_labels, label)) rst.labels.add(label);
		}
		IMemberModel[] members = result.getMemberModels();
		for (IMemberModel member : members) {
			final Label[] member_labels = Label.parseLabels(member);
			for (Label label : member_labels) {
				if (containsLabel(target_labels, label)) rst.labels.add(label);
			}
		}
		
		
		// 查找引用
		final RefFinder finder = new RefFinder(target);
		result.getASTNode().accept(finder);
		
		// 导出结果
		if (rst.labels.size() > 0 ||
			finder.referenced_cnt != 0 ||
			rst.patterns.size() != 0) {
			rst.referenced_cnt =finder.referenced_cnt;
			return rst;
		} else {
			return null;
		}
	}
	
	private static boolean containsLabel(HashSet<Label> labels, Label label) {
		for (Label l : labels) {
			if (l.name.equals(label.name)) return true;
		}
		return false;
	}
	
	Connection(ITypeModel target, ITypeModel result) {
		this.result = result;
		this.target = target;
	}

	@Override
	public ITypeModel getResult() {
		return result;
	}

	@Override
	public ITypeModel getTarget() {
		return target;
	}
	
	private double man_lambda = -1.0;

	@Override
	public double getLambda() {
		if (man_lambda < 0.0)
			return labels.size() * 5.0 + referenced_cnt * 1.0 + patterns.size() * 1.0;
		else
			return man_lambda;
	}

	@Override
	public Label[] getLabels() {
		return labels.toArray(new Label[0]);
	}

	@Override
	public int getReferencedCount() {
		return referenced_cnt;
	}

	@Override
	public DesignPattern[] getPatterns() {
		return patterns.toArray(new DesignPattern[0]);
	}
	
	private ConnectionLine line;

	@Override
	public ConnectionLine createLine(Container owner, ClassDiagram src, ClassDiagram dst) {
		ConnectionLine line = new ConnectionLine(owner, src, dst, this);
		this.line = line;
		return line;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof IConnection) {
			return result.equals(((IConnection) obj).getResult());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return result.hashCode();
	}

	@Override
	public ConnectionLine getLine() {
		return line;
	}

	@Override
	public void setLambda(double lambda) {
		this.man_lambda = lambda;
	}
	
}

final class RefFinder extends ASTVisitor {
	
	final ITypeModel target;
	int referenced_cnt;
	ITypeModel searchTarget = null;
	
	RefFinder(ITypeModel target) {
		this.target = target;
		referenced_cnt = 0;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		if (binding == null) return true;
		
		if (target.getJavaElement().equals(binding.getJavaElement())) {
			referenced_cnt++;
		}
		return true;
	}
	
}
