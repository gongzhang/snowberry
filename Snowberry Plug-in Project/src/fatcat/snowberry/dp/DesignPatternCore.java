package fatcat.snowberry.dp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.UUID;

import fatcat.snowberry.dp.schema.ISchema;
import fatcat.snowberry.dp.schema.ISchemaListener;
import fatcat.snowberry.dp.schema.Relationship;
import fatcat.snowberry.dp.schema.Role;
import fatcat.snowberry.dp.schema.internal.Schema;
import fatcat.snowberry.tag.IMemberModel;
import fatcat.snowberry.tag.IMemberModelListener;
import fatcat.snowberry.tag.IProjectModel;
import fatcat.snowberry.tag.IProjectModelListener;
import fatcat.snowberry.tag.ITag;
import fatcat.snowberry.tag.ITagListener;
import fatcat.snowberry.tag.ITypeModel;
import fatcat.snowberry.tag.ITypeModelListener;
import fatcat.snowberry.tag.TagCore;
import fatcat.snowberry.tag.internal.Tag;


public final class DesignPatternCore {
	
	private DesignPatternCore() {
	}
	
	public static void start() {
		new DPTagListener(); // 监听设计模式相关的标签变化
	}
	
	/**
	 * 取得当前所有已知的Schema。
	 * @return Schema数组
	 */
	public static ISchema[] getSchemas() {
		LinkedList<ISchema> rst = new LinkedList<ISchema>();
		for (UUID id : schemaMap.keySet()) {
			rst.add(schemaMap.get(id));
		}
		return rst.toArray(new ISchema[0]);
	}
	
	public static void addSchemaListener(ISchemaListener listener) {
		SCHEMA_LISTENERS.add(listener);
	}
	
	public static void removeSchemaListener(ISchemaListener listener) {
		SCHEMA_LISTENERS.remove(listener);
	}
	
	/**
	 * 取得指定的Schema。
	 * @param id 指定的{@code UUID}
	 * @return Schema或{@code null}
	 */
	public static ISchema getSchema(UUID id) {
		return schemaMap.get(id);
	}
	
	/**
	 * 取得指定的标签池。
	 * @param id 指定的{@code UUID}
	 * @return 标签池或{@code null}
	 */
	public static PatternTagPool getPool(UUID id) {
		return tagPoolMap.get(id);
	}
	
	/**
	 * 取得指定模型所参与的所有设计模式。
	 * @param model 指定的模型
	 * @return 设计模式数组
	 */
	public static DesignPattern[] getPatterns(IMemberModel model) {
		LinkedList<DesignPattern> rst = new LinkedList<DesignPattern>();
		PatternTagPool[] pools = getPools(model);
		for (PatternTagPool pool : pools) {
			DesignPattern pattern = DesignPattern.createFromPool(pool);
			if (pattern != null) {
				rst.add(pattern);
			}
		}
		return rst.toArray(new DesignPattern[0]);
	}
	
	/**
	 * 取得指定模型所参与的所有模式标签池。
	 * @param model 指定模型
	 * @return 模式标签池数组
	 */
	public static PatternTagPool[] getPools(IMemberModel model) {
		LinkedList<PatternTagPool> rst = new LinkedList<PatternTagPool>();
		ITag[] tags = model.getTags("pattern");
		for (ITag t : tags) {
			PatternTag tag = new PatternTag(t);
			UUID id = tag.getID();
			if (id != null) {
				PatternTagPool pool = tagPoolMap.get(id);
				if (pool != null) rst.add(pool);
			}
		}
		return rst.toArray(new PatternTagPool[0]);
	}
	
	/**
	 * 根据指定的标签，尽最大可能构造一个<code>ISchema</code>实例。
	 * <p>
	 * 不检查指定标签的标签名。标签应当包括非空的“id”属性和“name”属性，允许零长度的“comment”和“roles”属性。
	 * 其中“id”属性值应当为一个<code>UUID</code>的字符串值；
	 * 其中“roles”属性值应当包含由半角逗号（“,”）所分隔的多个角色名。
	 * 若不满足以上条件，返回<code>null</code>。
	 * </p><p>
	 * 对于标签中的其他属性，全部无条件地被认为是“关系描述”属性，它们的属性值将按照{@link Relationship}中约定的格式进行关系解读。
	 * </p>
	 * @param schemaTag 指定的标签
	 * @return <code>ISchema</code>实例或{@code null}
	 */
	public static final ISchema parseSchema(ITag schemaTag) {
		String id, name, comment, roles;
		id = schemaTag.getPropertyValue("id");
		name = schemaTag.getPropertyValue("name");
		comment = schemaTag.getPropertyValue("comment");
		comment = comment == null ? "" : comment;
		roles = schemaTag.getPropertyValue("roles");
		
		if (id == null || name == null || roles == null) {
			return null;
		}
		
		if (name.length() == 0) {
			return null;
		}
		
		UUID uuid = null;
		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {
			return null;
		}
		
		Schema schema = new Schema(uuid, name, comment);
		
		String[] names = roles.split(",");
		for (String n : names) {
			n = n.trim();
			if (n.length() > 0) {
				Role role = new Role(schema, n);
				schema.addRole(role);
			}
		}
		
		String[] propertyNames = schemaTag.getPropertyNames();
		for (String pn : propertyNames) {
			if (pn.equals("id") ||
				pn.equals("name") ||
				pn.equals("comment") ||
				pn.equals("roles")) {
				continue;
			}
			
			Relationship r = Relationship.parseRelationship(schemaTag.getPropertyValue(pn), schema);
			if (r != null) {
				schema.addRelationship(r);
			}
		}
		
		return schema;
	}
	
	public static DesignPattern[] getPatterns(final IProjectModel project) {
		final HashSet<DesignPattern> rst = new HashSet<DesignPattern>();
		for (UUID id : tagPoolMap.keySet()) {
			PatternTagPool pool = tagPoolMap.get(id);
			DesignPattern pattern = DesignPattern.createFromPool(pool);
			IMemberModel[] models = pattern.getModels();
			for (IMemberModel model : models) {
				if (model.getOwnerProject().equals(project)) {
					rst.add(pattern);
					break;
				}
			}
		}
		return rst.toArray(new DesignPattern[0]);
	}

	//// internal ////
	
	private static final HashMap<UUID, ISchema> schemaMap;
	private static final HashMap<UUID, PatternTagPool> tagPoolMap;
	
	private static final LinkedList<ISchemaListener> SCHEMA_LISTENERS;
	
	static {
		schemaMap = new HashMap<UUID, ISchema>();
		tagPoolMap = new HashMap<UUID, PatternTagPool>();
		SCHEMA_LISTENERS = new LinkedList<ISchemaListener>();

		// Default Schema Tag : Visitor
		final Tag tag_visitor = new Tag();
		tag_visitor.setName("schema");
		tag_visitor.addProperty("id", "e138a215-7cdd-44da-8c73-f364f50698ba");
		tag_visitor.addProperty("name", "Visitor");
		tag_visitor.addProperty("comment", "表示一个作用于某对象结构中的各元素的操作。它使你可以在不改变各元素的类的前提下定义作用于这些元素的新操作。");
		tag_visitor.addProperty("roles", "Visitor, Concrete Visitor, Element, Concrete Element, Object Structure");
		tag_visitor.addProperty("relationship-1", "Generalization, Visitor, Concrete Visitor");
		tag_visitor.addProperty("relationship-2", "Generalization, Element, Concrete Element");
		tag_visitor.addProperty("relationship-3", "Aggregation, Object Structure, Element, 元素");
		schemaTagAdded(tag_visitor);

		// Default Schema Tag : Prototype
		final Tag tag_prototype = new Tag();
		tag_prototype.setName("schema");
		tag_prototype.addProperty("id", "0a217090-3077-4baf-a0e0-6c4a8fe737f1");
		tag_prototype.addProperty("name", "Prototype");
		tag_prototype.addProperty("comment", "用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。");
		tag_prototype.addProperty("roles", "Prototype, Concrete Prototype, Client");
		tag_prototype.addProperty("relationship-1", "Generalization, Prototype, Concrete Prototype");
		tag_prototype.addProperty("relationship-2", "Dependency, Client, Prototype, 构建");
		schemaTagAdded(tag_prototype);

		// Default Schema Tag : Abstract Factory
		final Tag tag_abstract_factory = new Tag();
		tag_abstract_factory.setName("schema");
		tag_abstract_factory.addProperty("id", "47c352a4-efce-46d0-a2c4-d062e63be48d");
		tag_abstract_factory.addProperty("name", "Abstract Factory");
		tag_abstract_factory.addProperty("comment", "提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。");
		tag_abstract_factory.addProperty("roles", "Abstract Factory, Concrete Factory, Abstract Product, Concrete Product, Client");
		tag_abstract_factory.addProperty("relationship-1", "Generalization, Abstract Factory, Concrete Factory");
		tag_abstract_factory.addProperty("relationship-2", "Generalization, Abstract Product, Concrete Product");
		tag_abstract_factory.addProperty("relationship-3", "Dependency, Client, Abstract Factory, 使用");
		tag_abstract_factory.addProperty("relationship-4", "Dependency, Client, Abstract Product, 使用");
		schemaTagAdded(tag_abstract_factory);

		// Default Schema Tag : Mediator
		final Tag tag_mediator = new Tag();
		tag_mediator.setName("schema");
		tag_mediator.addProperty("id", "2546a82e-f22a-4bab-9f9c-55e1bfe8a400");
		tag_mediator.addProperty("name", "Mediator");
		tag_mediator.addProperty("comment", "用一个中介对象来封装一系列的对象交互。中介者使各对象不需要显式地相互引用，从而使其耦合松散，而且可以独立地改变它们之间的交互。");
		tag_mediator.addProperty("roles", "Mediator, Concrete Mediator, Colleague, Concrete Colleague");
		tag_mediator.addProperty("relationship-1", "Generalization, Mediator, Concrete Mediator");
		tag_mediator.addProperty("relationship-2", "Generalization, Colleague, Concrete Colleague");
		tag_mediator.addProperty("relationship-3", "Dependency, Concrete Mediator, Concrete Colleague, 使用");
		tag_mediator.addProperty("relationship-4", "Association, Colleague, Mediator, 中介者");
		schemaTagAdded(tag_mediator);

		// Default Schema Tag : State
		final Tag tag_state = new Tag();
		tag_state.setName("schema");
		tag_state.addProperty("id", "c256cf9f-ceb8-4463-a74f-0faf2242786b");
		tag_state.addProperty("name", "State");
		tag_state.addProperty("comment", "允许一个对象在其内部状态改变时改变它的行为。对象看起来似乎修改了它的类。");
		tag_state.addProperty("roles", "Context, State, Concrete State");
		tag_state.addProperty("relationship-1", "Generalization, State, Concrete State");
		tag_state.addProperty("relationship-2", "Aggregation, Context, State, 状态");
		schemaTagAdded(tag_state);

		// Default Schema Tag : Command
		final Tag tag_command = new Tag();
		tag_command.setName("schema");
		tag_command.addProperty("id", "e044b2e2-4b64-49ac-ba9f-d095173f0287");
		tag_command.addProperty("name", "Command");
		tag_command.addProperty("comment", "将一个请求封装为一个对象，从而使你可以用不同的请求对客户进行参数化；对请求排队或记录请求日志，以及支持可撤销的操作。");
		tag_command.addProperty("roles", "Command, Concrete Command, Client, Invoker, Receiver");
		tag_command.addProperty("relationship-1", "Generalization, Command, Concrete Command");
		tag_command.addProperty("relationship-2", "Aggregation, Invoker, Command");
		tag_command.addProperty("relationship-3", "Association, Concrete Command, Receiver, 接收者");
		tag_command.addProperty("relationship-4", "Dependency, Client, Receiver, 使用");
		schemaTagAdded(tag_command);

		// Default Schema Tag : Observer
		final Tag tag_observer = new Tag();
		tag_observer.setName("schema");
		tag_observer.addProperty("id", "5f5ab859-1158-4990-9d11-ece56ab96bca");
		tag_observer.addProperty("name", "Observer");
		tag_observer.addProperty("comment", "定义对象之间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。");
		tag_observer.addProperty("roles", "Subject, Observer, Concrete Subject, Concrete Observer");
		tag_observer.addProperty("relationship-1", "Generalization, Subject, Concrete Subject");
		tag_observer.addProperty("relationship-2", "Generalization, Observer, Concrete Observer");
		tag_observer.addProperty("relationship-3", "Association, Subject, Observer, 观察器");
		tag_observer.addProperty("relationship-4", "Association, Concrete Observer, Concrete Subject");
		schemaTagAdded(tag_observer);

		// Default Schema Tag : Strategy
		final Tag tag_strategy = new Tag();
		tag_strategy.setName("schema");
		tag_strategy.addProperty("id", "b32400e3-de51-40d8-aa1e-3b3f2949527e");
		tag_strategy.addProperty("name", "Strategy");
		tag_strategy.addProperty("comment", "定义一系列的算法，把它们一个个封装起来，并且使它们可以相互替换。本模式使得算法可独立于使用它的客户而变化。");
		tag_strategy.addProperty("roles", "Strategy, Concrete Strategy, Context");
		tag_strategy.addProperty("relationship-1", "Generalization, Strategy, Concrete Strategy");
		tag_strategy.addProperty("relationship-2", "Aggregation, Context, Strategy, 策略");
		schemaTagAdded(tag_strategy);

		// Default Schema Tag : Decorator
		final Tag tag_decorator = new Tag();
		tag_decorator.setName("schema");
		tag_decorator.addProperty("id", "e6dc5792-b1eb-414d-9e38-6e114b417139");
		tag_decorator.addProperty("name", "Decorator");
		tag_decorator.addProperty("comment", "动态地给一个对象添加一些额外的职责。就增加功能来说，Decorator模式相比生成子类更为灵活。");
		tag_decorator.addProperty("roles", "Component, Concrete Component, Decorator, Concrete Decorator");
		tag_decorator.addProperty("relationship-1", "Generalization, Component, Concrete Component");
		tag_decorator.addProperty("relationship-2", "Generalization, Decorator, Concrete Decorator");
		tag_decorator.addProperty("relationship-3", "Aggregation, Decorator, Component, 装饰部件");
		schemaTagAdded(tag_decorator);

		// Default Schema Tag : Proxy
		final Tag tag_proxy = new Tag();
		tag_proxy.setName("schema");
		tag_proxy.addProperty("id", "09e45e32-92cc-41dc-a875-f00a8a8edabe");
		tag_proxy.addProperty("name", "Proxy");
		tag_proxy.addProperty("comment", "为其他对象提供一种代理以控制对这个对象的访问。");
		tag_proxy.addProperty("roles", "Proxy, Subject, Real Subject, Client");
		tag_proxy.addProperty("relationship-1", "Generalization, Subject, Real Subject");
		tag_proxy.addProperty("relationship-2", "Generalization, Subject, Proxy");
		tag_proxy.addProperty("relationship-3", "Association, Proxy, Real Subject");
		tag_proxy.addProperty("relationship-4", "Dependency, Client, Proxy, 使用");
		schemaTagAdded(tag_proxy);

		// Default Schema Tag : Bridge
		final Tag tag_bridge = new Tag();
		tag_bridge.setName("schema");
		tag_bridge.addProperty("id", "a55e6771-e666-44b6-8cfb-ea3b33317c4a");
		tag_bridge.addProperty("name", "Bridge");
		tag_bridge.addProperty("comment", "将抽象部分与它的实现部分分离，使它们都可以独立地变化。");
		tag_bridge.addProperty("roles", "Abstraction, Refined Abstraction, Implementor, Concrete Implementor");
		tag_bridge.addProperty("relationship-1", "Generalization, Abstraction, Refined Abstraction");
		tag_bridge.addProperty("relationship-2", "Generalization, Implementor, Concrete Implementor");
		tag_bridge.addProperty("relationship-3", "Aggregation, Abstraction, Implementor, 实现者");
		schemaTagAdded(tag_bridge);

		// Default Schema Tag : Flyweight
		final Tag tag_flyweight = new Tag();
		tag_flyweight.setName("schema");
		tag_flyweight.addProperty("id", "7f30245d-d1a2-4139-84dd-3735f80a9d2a");
		tag_flyweight.addProperty("name", "Flyweight");
		tag_flyweight.addProperty("comment", "运用共享技术有效地支持大量细颗粒度的对象。");
		tag_flyweight.addProperty("roles", "Flyweight, Concrete Flyweight, Unshared Concrete Flyweight, Flyweight Factory, Client");
		tag_flyweight.addProperty("relationship-1", "Generalization, Flyweight, Concrete Flyweight");
		tag_flyweight.addProperty("relationship-2", "Generalization, Flyweight, Unshared Concrete Flyweight");
		tag_flyweight.addProperty("relationship-3", "Aggregation, Flyweight Factory, Flyweight");
		tag_flyweight.addProperty("relationship-4", "Dependency, Client, Flyweight Factory");
		tag_flyweight.addProperty("relationship-5", "Dependency, Client, Concrete Flyweight");
		tag_flyweight.addProperty("relationship-6", "Dependency, Client, Unshared Concrete Flyweight");
		schemaTagAdded(tag_flyweight);

		// Default Schema Tag : Iterator
		final Tag tag_iterator = new Tag();
		tag_iterator.setName("schema");
		tag_iterator.addProperty("id", "5d39d741-ae28-4290-9b62-928f5883b33c");
		tag_iterator.addProperty("name", "Iterator");
		tag_iterator.addProperty("comment", "提供一种方法顺序访问一个聚合对象中各个元素，而又不需暴露该对象的内部表示。");
		tag_iterator.addProperty("roles", "Iterator, Concrete Iterator, Aggregate, Concrete Aggregate");
		tag_iterator.addProperty("relationship-1", "Generalization, Aggregate, Concrete Aggregate");
		tag_iterator.addProperty("relationship-2", "Generalization, Iterator, Concrete Iterator");
		tag_iterator.addProperty("relationship-3", "Dependency, Concrete Iterator, Concrete Aggregate, 使用");
		tag_iterator.addProperty("relationship-4", "Dependency, Iterator, Aggregate, 遍历");
		schemaTagAdded(tag_iterator);

		// Default Schema Tag : Template Method
		final Tag tag_template_method = new Tag();
		tag_template_method.setName("schema");
		tag_template_method.addProperty("id", "207f4656-9c1c-4727-8a65-6ddb61bb372d");
		tag_template_method.addProperty("name", "Template Method");
		tag_template_method.addProperty("comment", "定义一个操作中的算法的骨架，而将这一些步骤延迟到子类中。Template Method使得子类可以不改变一个算法的结构即可重新定义该算法的某些特定步骤。");
		tag_template_method.addProperty("roles", "Abstract Class, Concrete Class");
		tag_template_method.addProperty("relationship-1", "Generalization, Abstract Class, Concrete Class");
		schemaTagAdded(tag_template_method);

		// Default Schema Tag : Interpreter
		final Tag tag_interpreter = new Tag();
		tag_interpreter.setName("schema");
		tag_interpreter.addProperty("id", "6705dfb5-c33f-41ae-b3e7-7375122dcd0f");
		tag_interpreter.addProperty("name", "Interpreter");
		tag_interpreter.addProperty("comment", "给定一个语言，定义它的文法的一种表示，并定义一个解释器，这个解释器使用该表示来解释语言中的句子。");
		tag_interpreter.addProperty("roles", "Abstract Expression, Terminal Expression, Nonterminal Expression, Context, Client");
		tag_interpreter.addProperty("relationship-1", "Generalization, Abstract Expression, Terminal Expression");
		tag_interpreter.addProperty("relationship-2", "Generalization, Abstract Expression, Nonterminal Expression");
		tag_interpreter.addProperty("relationship-3", "Aggregation, Nonterminal Expression, Abstract Expression");
		tag_interpreter.addProperty("relationship-4", "Dependency, Client, Context, 使用");
		tag_interpreter.addProperty("relationship-5", "Dependency, Client, Abstract Expression, 使用");
		schemaTagAdded(tag_interpreter);

		// Default Schema Tag : Chain of Responsibility
		final Tag tag_chain_of_responsibility = new Tag();
		tag_chain_of_responsibility.setName("schema");
		tag_chain_of_responsibility.addProperty("id", "7edf2edd-12a1-4a37-99fe-76b3c275182e");
		tag_chain_of_responsibility.addProperty("name", "Chain of Responsibility");
		tag_chain_of_responsibility.addProperty("comment", "使多个对象都具有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。");
		tag_chain_of_responsibility.addProperty("roles", "Handler, Concrete Handler, Client");
		tag_chain_of_responsibility.addProperty("relationship-1", "Generalization, Handler, Concrete Handler");
		tag_chain_of_responsibility.addProperty("relationship-2", "Dependency, Client, Handler");
		schemaTagAdded(tag_chain_of_responsibility);

		// Default Schema Tag : 三国杀模式
//		final Tag tag_三国杀模式 = new Tag();
//		tag_三国杀模式.setName("schema");
//		tag_三国杀模式.addProperty("id", "2d38803b-1aa4-4162-a7dd-ff8a688e7ddf");
//		tag_三国杀模式.addProperty("name", "三国杀模式");
//		tag_三国杀模式.addProperty("comment", "著名的三国杀模式，很好玩儿。");
//		tag_三国杀模式.addProperty("roles", "主公, 忠臣, 反贼, 内奸");
//		tag_三国杀模式.addProperty("relationship-1", "Dependency, 主公, 忠臣");
//		schemaTagAdded(tag_三国杀模式);

		// Default Schema Tag : Memento
		final Tag tag_memento = new Tag();
		tag_memento.setName("schema");
		tag_memento.addProperty("id", "4f9e75ad-8acb-40d9-8b3f-679bbf9ad117");
		tag_memento.addProperty("name", "Memento");
		tag_memento.addProperty("comment", "在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。这样以后就可以将该对象恢复到原先保存的状态。");
		tag_memento.addProperty("roles", "Memento, Originator, Caretaker");
		tag_memento.addProperty("relationship-1", "Aggregation, Caretaker, Memento, 保管");
		tag_memento.addProperty("relationship-2", "Dependency, Originator, Memento, 使用");
		schemaTagAdded(tag_memento);

		// Default Schema Tag : Factory Method
		final Tag tag_factory_method = new Tag();
		tag_factory_method.setName("schema");
		tag_factory_method.addProperty("id", "d4fe2998-29e0-4e56-bdf2-f3c6c02d41b8");
		tag_factory_method.addProperty("name", "Factory Method");
		tag_factory_method.addProperty("comment", "定义一个用于创建对象的接口，让子类决定实例化哪一个类。Factory Method使一个类的实例化延迟到其子类。");
		tag_factory_method.addProperty("roles", "Product, Concrete Product, Creator, Concrete Creator");
		tag_factory_method.addProperty("relationship-1", "Generalization, Product, Concrete Product");
		tag_factory_method.addProperty("relationship-2", "Generalization, Creator, Concrete Creator");
		tag_factory_method.addProperty("relationship-3", "Dependency, Concrete Creator, Concrete Product, 构建");
		schemaTagAdded(tag_factory_method);

		// Default Schema Tag : Adapter
		final Tag tag_adapter = new Tag();
		tag_adapter.setName("schema");
		tag_adapter.addProperty("id", "149485c5-5e8d-4888-9fba-47de40890332");
		tag_adapter.addProperty("name", "Adapter");
		tag_adapter.addProperty("comment", "将一个类的接口转换成客户希望的另外一个接口。Adapter模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。");
		tag_adapter.addProperty("roles", "Target, Client, Adaptee, Adapter");
		tag_adapter.addProperty("relationship-1", "Generalization, Target, Adapter");
		tag_adapter.addProperty("relationship-2", "Association, Adaptee, Adapter, 适配");
		tag_adapter.addProperty("relationship-3", "Dependency, Client, Target, 使用");
		tag_adapter.addProperty("relationship-4", "Dependency, Client, Adapter, 使用");
		schemaTagAdded(tag_adapter);

		// Default Schema Tag : Composite
		final Tag tag_composite = new Tag();
		tag_composite.setName("schema");
		tag_composite.addProperty("id", "8c0bf3c1-b37b-4d2c-9bfe-abaec6fbeeda");
		tag_composite.addProperty("name", "Composite");
		tag_composite.addProperty("comment", "将对象组合成树形结构以表示“部分-整体”的层次结构。Composite使得用户对单个对象和组合对象的使用具有一致性。");
		tag_composite.addProperty("roles", "Component, Leaf, Composite, Client");
		tag_composite.addProperty("relationship-1", "Generalization, Component, Leaf");
		tag_composite.addProperty("relationship-2", "Generalization, Component, Composite");
		tag_composite.addProperty("relationship-3", "Aggregation, Composite, Component, 子部件");
		tag_composite.addProperty("relationship-4", "Dependency, Client, Component, 使用");
		schemaTagAdded(tag_composite);

		// Default Schema Tag : Builder
		final Tag tag_builder = new Tag();
		tag_builder.setName("schema");
		tag_builder.addProperty("id", "3152aeca-eb66-432c-ac56-9b752e86de97");
		tag_builder.addProperty("name", "Builder");
		tag_builder.addProperty("comment", "将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。");
		tag_builder.addProperty("roles", "Builder, Concrete Builder, Director, Product");
		tag_builder.addProperty("relationship-1", "Generalization, Builder, Concrete Builder");
		tag_builder.addProperty("relationship-2", "Dependency, Concrete Builder, Product, 构建");
		tag_builder.addProperty("relationship-3", "Aggregation, Director, Builder, 生成器");
		schemaTagAdded(tag_builder);

		// Default Schema Tag : Singleton
		final Tag tag_singleton = new Tag();
		tag_singleton.setName("schema");
		tag_singleton.addProperty("id", "18240249-076b-40c1-8026-35ce512dac49");
		tag_singleton.addProperty("name", "Singleton");
		tag_singleton.addProperty("comment", "保证一个类仅有一个实例，并提供一个访问它的全局访问点。");
		tag_singleton.addProperty("roles", "Singleton");
		schemaTagAdded(tag_singleton);

		// Default Schema Tag : Facade
		final Tag tag_facade = new Tag();
		tag_facade.setName("schema");
		tag_facade.addProperty("id", "1ca4459e-1914-4534-b007-1ae323fe5077");
		tag_facade.addProperty("name", "Facade");
		tag_facade.addProperty("comment", "为子系统中的一组接口提供一个一致的界面。Facade模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。");
		tag_facade.addProperty("roles", "Facade, Subsystem Classes");
		tag_facade.addProperty("relationship-1", "Dependency, Facade, Subsystem Classes, 使用");
		schemaTagAdded(tag_facade);
	}
	
	static void schemaTagAdded(ITag t) {
		ISchema schema = parseSchema(t);
		if (schema != null) {
			if (schemaMap.get(schema.getID()) == null) {
				schemaMap.put(schema.getID(), schema);
				for (ISchemaListener listener : SCHEMA_LISTENERS) {
					listener.schemaAdded(schema);
				}
			}
		}
	}
	
	static void schemaTagRemoved(UUID id) {
		ISchema schema = schemaMap.remove(id);
		if (schema != null) {
			for (ISchemaListener listener : SCHEMA_LISTENERS) {
				listener.schemaRemoved(schema);
			}
		}
	}
	
	static void patternTagAdded(PatternTag t) {
		if (t.isLegal()) {
			UUID id = t.getID();
			PatternTagPool pool = tagPoolMap.get(id);
			if (pool == null) {
				pool = new PatternTagPool(id);
				tagPoolMap.put(id, pool);
			}
			pool.addTag(t);
		}
	}
	
	static void patternTagRemoved(PatternTag t) {
		if (t.isLegal()) {
			UUID id = t.getID();
			PatternTagPool pool = tagPoolMap.get(id);
			pool.removeTag(t);
			if (pool.size() == 0) {
				tagPoolMap.remove(id);
			}
		}
	}
	
}

final class DPTagListener implements IProjectModelListener, ITypeModelListener, IMemberModelListener, ITagListener {
	
	DPTagListener() {
		IProjectModel[] projectModels = TagCore.getProjectModels();
		for(IProjectModel projectModel: projectModels){
			projectModel.addTypeModelListener(this);
			ITypeModel[] typeModels = projectModel.getITypeModels();
			for(ITypeModel typeModel : typeModels){
				typeModel.addMemberModelListener(this);
				typeModel.addTagListener(this);
				tagsAdded(typeModel.getTags());
				IMemberModel[] memberModels = typeModel.getMemberModels();
				for(IMemberModel memberModel:memberModels){
					memberModel.addTagListener(this);
					tagsAdded(memberModel.getTags());
				}
			}
		}
		TagCore.addProjectModelListener(this);
	}

	@Override
	public void projectModelAdded(IProjectModel projectModel) {
		projectModel.addTypeModelListener(this);
		
		// cascade
		ITypeModel[] models = projectModel.getITypeModels();
		for (ITypeModel model : models) {
			typeModelAdded(model);
		}
	}

	@Override
	public void projectModelChanged(IProjectModel projectModel) {
	}

	@Override
	public void projectModelRemoved(IProjectModel projectModel) {
		projectModel.removeTypeModelListener(this);
		
		// cascade
		ITypeModel[] models = projectModel.getITypeModels();
		for (ITypeModel model : models) {
			typeModelRemoved(model);
		}
	}

	@Override
	public void typeModelAdded(ITypeModel typeModel) {
		typeModel.addMemberModelListener(this);
		typeModel.addTagListener(this);
		
		// cascade
		ITag[] tags = typeModel.getTags();
		tagsAdded(tags);
		IMemberModel[] models = typeModel.getMemberModels();
		for (IMemberModel model : models) {
			memberModelAdded(model);
		}
	}

	@Override
	public void typeModelChanged(ITypeModel typeModel) {
	}

	@Override
	public void typeModelRemoved(ITypeModel typeModel) {
		typeModel.removeMemberModelListener(this);
		typeModel.removeTagListener(this);
		
		// cascade
		ITag[] tags = typeModel.getTags();
		tagsRemoved(tags);
		IMemberModel[] models = typeModel.getMemberModels();
		for (IMemberModel model : models) {
			memberModelRemoved(model);
		}
	}

	@Override
	public void memberModelAdded(IMemberModel memberModel) {
		memberModel.addTagListener(this);
		// cascade
		ITag[] tags = memberModel.getTags();
		tagsAdded(tags);
	}

	@Override
	public void memberModelChanged(IMemberModel memberModel) {
	}

	@Override
	public void memberModelRemoved(IMemberModel memberModel) {
		memberModel.removeTagListener(this);
		// cascade
		ITag[] tags = memberModel.getTags();
		tagsRemoved(tags);
	}
	
	void tagsAdded(ITag[] tags) {
		for (ITag tag : tags) {
			tagAdded(tag, tag.getHostModel());
		}
	}
	
	void tagsRemoved(ITag[] tags) {
		for (ITag tag : tags) {
			tagRemoved(tag, tag.getHostModel());
		}
	}

	@Override
	public void tagAdded(ITag tag, IMemberModel memberModel) {
		if (tag.getName().equals("pattern")) {
			DesignPatternCore.patternTagAdded(new PatternTag(tag));
		} else if (tag.getName().equals("schema")) {
			DesignPatternCore.schemaTagAdded(tag);
		}
	}

	@Override
	public void tagRemoved(ITag tag, IMemberModel memberModel) {
		if (tag.getName().equals("pattern")) {
			DesignPatternCore.patternTagRemoved(new PatternTag(tag));
		} else if (tag.getName().equals("schema")) {
			String id = tag.getPropertyValue("id");
			if (id != null) {
				try {
					DesignPatternCore.schemaTagRemoved(UUID.fromString(id));
				} catch (Exception e) {
				}
			}
		}
	}
	
}
