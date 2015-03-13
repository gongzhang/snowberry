package fatcat.snowberry.tag.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import fatcat.snowberry.core.International;
import fatcat.snowberry.core.SnowberryCore;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagListener;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.SourceEditingException;
import fatcat.snowberry.tag.TagCore;
import fatcat.snowberry.tag.TagResolveException;


public abstract class MemberModel implements IMemberModel {
	
	private IProjectModel project;
	private IFile file;
	private final ITypeModel owner;
	
	private final ArrayList<Tag> tags;
	private final ArrayList<ITagListener> listeners;
	
	public MemberModel(ITypeModel owner, IProjectModel project, IFile file, BodyDeclaration node, IMember member) {
		tags = new ArrayList<Tag>();
		listeners = new ArrayList<ITagListener>();
		this.owner = owner;
		this.file = file;
		this.project = project;
		setASTNode(node);
		setJavaElement(member);
		initTags();
	}
	
	@Override
	public ITypeModel getOwnerType() {
		return owner;
	}
	
	@Override
	public ITag[] getTags() {
		return tags.toArray(new ITag[0]);
	}
	
	@Override
	public ITag[] getTags(String tagName) {
		LinkedList<ITag> rst = new LinkedList<ITag>();
		for (ITag t : tags) {
			if (t.getName().equals(tagName)) rst.add(t);
		}
		return rst.toArray(new ITag[0]);
	}
	
	@Override
	public void addTagListener(ITagListener listener) {
		listeners.add(listener);
	}
	
	public void removeTagListener(ITagListener listener) {
		listeners.remove(listener);
	}
	
	abstract public void setASTNode(BodyDeclaration node);
	
	abstract public void setJavaElement(IMember member);

	@Override
	public IProjectModel getOwnerProject() {
		return project;
	}

	@Override
	public IFile getResource() {
		return file;
	}
	
	abstract public void notifyContentChanged();
	
	private Tag[] getTagsFormJavadoc() {
		LinkedList<Tag> rst = new LinkedList<Tag>();
		Javadoc node = getASTNode().getJavadoc();
		if (node != null) {
			List<?> tags = (List<?>) node.tags();
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
						internalTag.setHostElement(this);
						rst.add(internalTag);
					} catch (TagResolveException e) {
						log(this, e);
					}
				}
			}
		}
		return rst.toArray(new Tag[0]);
	}
	
	public static void log(IMemberModel model, TagResolveException e) {
		StringBuffer suffix = new StringBuffer("(");
		suffix.append(model.getJavaElement().getJavaProject().getElementName());
		suffix.append(" - ");
		switch (model.getKind()) {
		case IMemberModel.TYPE:
			suffix.append(model.getJavaElement().getElementName());
			break;
			
		case IMemberModel.METHOD:
		case IMemberModel.FIELD:
			suffix.append(model.getOwnerType().getJavaElement().getElementName());
			suffix.append(".");
			suffix.append(model.getJavaElement().getElementName());
			break;

		default:
			suffix.append(model.getJavaElement().getElementName());
			break;
		}
		suffix.append(")");
		SnowberryCore.log(Status.WARNING, String.format("%s %s", International.WarningIllegalTag, suffix), e);
	}
	
	private void initTags() {
		Tag[] tags = getTagsFormJavadoc();
		for (Tag tag : tags)
			this.tags.add(tag);
	}
	
	void notifyJavadocChanged() {
		ITag[] oldTags = getTags();
		ArrayList<ITag> restedTags = new ArrayList<ITag>();
		LinkedList<ITag> tagsAdded = new LinkedList<ITag>();
		for (ITag t : oldTags)
			restedTags.add(t);
		
		Tag[] newTags = getTagsFormJavadoc();
		for (Tag tag : newTags) {
			Tag oldTag = find(tag);
			if (oldTag != null) {
				restedTags.remove(oldTag);
			} else {
				this.tags.add(tag);
				tagsAdded.add(tag);
			}
		}
		
		for (ITag t : restedTags) {
			this.tags.remove(t);
			for (ITagListener listener : listeners)
				listener.tagRemoved(t, this);
		}
		
		for (ITag t : tagsAdded) {
			for (ITagListener listener : listeners)
				listener.tagAdded(t, this);
		}
	}
	
	private Tag find(ITag tag) {
		for (Tag t : tags)
			if (t.equals(tag))
				return t;
		return null;
	}
	
	public void editSource(ASTVisitor astVisitor, boolean save_flag) throws SourceEditingException {
		IFile file = getResource();
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
			cu.accept(astVisitor);
		} catch (Exception e) {
			throw new SourceEditingException(International.UnknownError);
		}
		
		// rewrite
		
		TextEdit edit = null;
		try {
			edit = cu.rewrite(fDocument, JavaCore.getDefaultOptions());
			edit.apply(fDocument);
			String newSource = fDocument.get();
			iCompilationUnit.getBuffer().setContents(newSource);
			if (save_flag) {
				iCompilationUnit.getBuffer().save(null, true);
			}
		} catch (Exception e) {
			throw new SourceEditingException(International.CannotSaveToFile + file.getName());
		}
	}
	
	@Override
	public void addTag(final ITag t) throws SourceEditingException {
		addTag(t, true);
	}
	
	@Override
	public void addTag(final ITag t, boolean save_flag) throws SourceEditingException {
		
		final class TagAdder extends ASTVisitor {
			
			boolean done;
			
			TagAdder() {
				super(true);
				done = false;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public boolean visit(SimpleName node) {
				if (done) return false;
				if (node.resolveBinding().getJavaElement().equals(getJavaElement())) {
					ASTNode parentNode = node.getParent();
					while (!(parentNode instanceof BodyDeclaration))
						parentNode = parentNode.getParent();
					if (parentNode instanceof BodyDeclaration) {
						Javadoc jdoc = ((BodyDeclaration) parentNode).getJavadoc();
						TagElement tag = parentNode.getAST().newTagElement();
						tag.setTagName("@" + TagCore.TAG_NAME); //$NON-NLS-1$
						TextElement text = tag.getAST().newTextElement();
						text.setText(TagParser.parseString(t, true));
						tag.fragments().add(text);
						if (jdoc != null) {
							jdoc.tags().add(tag);
						} else {
							jdoc = parentNode.getAST().newJavadoc();
							jdoc.tags().add(tag);
							((BodyDeclaration) parentNode).setJavadoc(jdoc);
						}
						done = true;
					}
				}
				return false;
			}
		}
		
		TagAdder adder = new TagAdder();
		editSource(adder, save_flag);
		if (!adder.done) throw new SourceEditingException(International.CannotFindWhereToEmbedTag);
		
	}
	
	@Override
	public void removeTag(final ITag t) throws SourceEditingException {
		removeTag(t, true);
	}
	
	@Override
	public void removeTag(final ITag t, boolean save_flag) throws SourceEditingException {
	
		final class TagRemover extends ASTVisitor {
			
			boolean done;
			
			TagRemover() {
				super(true);
				done = false;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public boolean visit(SimpleName node) {
				if (done) return false;
				if (node.resolveBinding().getJavaElement().equals(getJavaElement())) {
					ASTNode parentNode = node.getParent();
					while (!(parentNode instanceof BodyDeclaration))
						parentNode = parentNode.getParent();
					if (parentNode instanceof BodyDeclaration) {
						Javadoc jdoc = ((BodyDeclaration) parentNode).getJavadoc();

						if (jdoc != null) {
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
										if (internalTag.equals(t)) {
											tagElement.delete();
										}
									} catch (TagResolveException e) {
										log(MemberModel.this, e);
									}
								}
							}
						}

						done = true;
					}
				}
				return false;
			}
			
		}
		
		TagRemover remover = new TagRemover();
		editSource(remover, save_flag);
		if (!remover.done) throw new SourceEditingException(International.CannotFindWhereToEmbedTag);
		
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof IMemberModel) {
			IMemberModel model = (IMemberModel) obj;
			return model.getJavaElement().equals(getJavaElement());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getJavaElement().getElementName().hashCode();
	}

}
