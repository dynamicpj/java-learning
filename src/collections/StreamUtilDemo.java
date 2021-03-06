package collections;

import java.util.*;

public class StreamUtilDemo {
	private List<String> sList = new ArrayList<String>();

	public StreamUtilDemo() {
		init();
	}

	private void init() {
		initStringList();
	}

	/**
	 * 初始化字符串列表
	 */
	private void initStringList() {
		sList.add("zzz1");
		sList.add("aaa2");
		sList.add("bbb2");
		sList.add("fff1");
		sList.add("fff2");
		sList.add("aaa1");
		sList.add("bbb1");
		sList.add("zzz2");
	}

	/**
	 * Filter接受一个predicate接口类型的变量，并将所有流对象中的元素进行过滤。该操作是一个中间操作，
	 * 因此它允许我们在返回结果的基础上再进行其他的流操作
	 * （forEach）。ForEach接受一个function接口类型的变量，用来执行对每一个元素的操作
	 * 。ForEach是一个中止操作。它不返回流，所以我们不能再调用其他的流操作
	 */
	public void useStreamFilter() {
		// stream()方法是Collection接口的一个默认方法
		// Stream<T> filter(Predicate<? super T>
		// predicate);filter方法参数是一个Predicate函数式接口并继续返回Stream接口
		// void forEach(Consumer<? super T> action);foreach方法参数是一个Consumer函数式接口
		// 解释:从字符串序列中过滤出以字符a开头的字符串并迭代打印输出
		sList.stream().filter((s) -> s.startsWith("a")).forEach(System.out::println);
	}

	/**
	 * Sorted是一个中间操作，能够返回一个排过序的流对象的视图。流对象中的元素会默认按照自然顺序进行排序，
	 * 除非你自己指定一个Comparator接口来改变排序规则.
	 * 
	 * <p>
	 * 一定要记住，sorted只是创建一个流对象排序的视图，而不会改变原来集合中元素的顺序。原来string集合中的元素顺序是没有改变的
	 */
	public void useStreamSort() {
		// Stream<T> sorted();返回Stream接口
		// 另外还有一个 Stream<T> sorted(Comparator<? super T>
		// comparator);带Comparator接口的参数
		sList.stream().filter((s) -> s.startsWith("a")).sorted().forEach(System.out::println);
		System.out.println("#####");
		// 输出原始集合元素，sorted只是创建排序视图，不影响原来集合顺序
		sList.stream().forEach(System.out::println);
	}

	/**
	 * map是一个对于流对象的中间操作，通过给定的方法，它能够把流对象中的每一个元素对应到另外一个对象上。
	 * 下面的例子就演示了如何把每个string都转换成大写的string.
	 * 不但如此，你还可以把每一种对象映射成为其他类型。对于带泛型结果的流对象，具体的类型还要由传递给map的泛型方法来决定。
	 */
	public void useStreamMap() {
		// <R> Stream<R> map(Function<? super T, ? extends R> mapper);
		// map方法参数为Function函数式接口(R_String,T_String).

		// 解释:将集合元素转为大写(每个元素映射到大写)->降序排序->迭代输出
		// 不影响原来集合
		sList.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a)).forEach(System.out::println);
	}
	
	  /**
     * 匹配操作有多种不同的类型，都是用来判断某一种规则是否与流对象相互吻合的。所有的匹配操作都是终结操作，只返回一个boolean类型的结果
     */
    public void useStreamMatch() {
        // boolean anyMatch(Predicate<? super T> predicate);参数为Predicate函数式接口
        // 解释:集合中是否有任一元素匹配以'a'开头
        boolean anyStartsWithA = sList.stream().anyMatch((s) -> s.startsWith("a"));
        System.out.println(anyStartsWithA);

        // boolean allMatch(Predicate<? super T> predicate);
        // 解释:集合中是否所有元素匹配以'a'开头
        boolean allStartsWithA = sList.stream().allMatch((s) -> s.startsWith("a"));
        System.out.println(allStartsWithA);

        // boolean noneMatch(Predicate<? super T> predicate);
        // 解释:集合中是否没有元素匹配以'd'开头
        boolean nonStartsWithD = sList.stream().noneMatch((s) -> s.startsWith("d"));
        System.out.println(nonStartsWithD);
    }
    
    /**
     * Count是一个终结操作，它的作用是返回一个数值，用来标识当前流对象中包含的元素数量
     */
    public void useStreamCount() {
        // long count();
        // 解释:返回集合中以'a'开头元素的数目
        long startsWithACount = sList.stream().filter((s) -> s.startsWith("a")).count();
        System.out.println(startsWithACount);
        System.out.println(sList.stream().count());
    }
    
    /**
     * 该操作是一个终结操作，它能够通过某一个方法，对元素进行削减操作。该操作的结果会放在一个Optional变量里返回。
     */
    public void useStreamReduce() {
        // Optional<T> reduce(BinaryOperator<T> accumulator);
        // @FunctionalInterface public interface BinaryOperator<T> extends
        // BiFunction<T,T,T> {

        // @FunctionalInterface public interface BiFunction<T, U, R> { R apply(T
        // t, U u);
		Optional<String> reduced = sList.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);

        // 解释:集合元素排序后->reduce(削减 )->将元素以#连接->生成Optional对象(其get方法返回#拼接后的值)
        reduced.ifPresent(System.out::println);
        System.out.println(reduced.get());
    }
    
    /**
     * 使用并行流
     * <p>
     * 流操作可以是顺序的，也可以是并行的。顺序操作通过单线程执行，而并行操作则通过多线程执行. 可使用并行流进行操作来提高运行效率
     */
    public static void useParallelStreams() {
        // 初始化一个字符串集合
        int max = 1000000;
        List<String> values = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            UUID uuid = UUID.randomUUID();
            values.add(uuid.toString());
        }

        // 使用顺序流排序

        long sequenceT0 = System.nanoTime();
        values.stream().sorted();
        long sequenceT1 = System.nanoTime();

        // 输出:sequential sort took: 51921 ms.
        System.out.format("sequential sort took: %d ms.", sequenceT1 - sequenceT0).println();

        // 使用并行流排序
        long parallelT0 = System.nanoTime();
        // default Stream<E> parallelStream() {
        // parallelStream为Collection接口的一个默认方法
        values.parallelStream().sorted();
        long parallelT1 = System.nanoTime();

        // 输出:parallel sort took: 21432 ms.
        System.out.format("parallel sort took: %d ms.", parallelT1 - parallelT0).println();

        // 从输出可以看出：并行排序快了一倍多
    }

	public static void main(String[] args) {
		StreamUtilDemo sDemo = new StreamUtilDemo();
//		sDemo.useStreamFilter();
//		System.out.println("----------");
//		sDemo.useStreamSort();
//		System.out.println("----------");
//		sDemo.useStreamMap();
//		System.out.println("----------");
//		sDemo.useStreamMatch();
//		System.out.println("----------");
//		sDemo.useStreamCount();
//		System.out.println("----------");
//		sDemo.useStreamReduce();
		
		useParallelStreams();
	}
}
