package ca.jrvs.apps.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LambdaStreamImpTest {

    private LambdaStreamImp lsi;
    private ByteArrayOutputStream out;

    @BeforeEach
    void setUp() {
        lsi = new LambdaStreamImp();
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @Test
    void createStrStream() {
        Stream<String> stream = lsi.createStrStream("a", "b", "c");
        List<String> result = stream.collect(Collectors.toList());

        List<String> test = new ArrayList<>();
        test.add("a");
        test.add("b");
        test.add("c");

        assertEquals(test, result);
    }

    @Test
    void toUpperCase() {
        List<String> result = lsi.toUpperCase("a", "b", "c")
                .collect(Collectors.toList());

        List<String> test = new ArrayList<>();
        test.add("A");
        test.add("B");
        test.add("C");

        assertEquals(test, result);
    }

    @Test
    void filter() {
        Stream<String> stream = Stream.of("apple", "banana", "cherry");
        List<String> result = lsi.filter(stream, "a").collect(Collectors.toList());

        List<String> test = new ArrayList<>();
        test.add("cherry");

        assertEquals(test, result);
    }

    @Test
    void createIntStream() {
        int[] arr = {1, 2, 3};
        List<Integer> result = lsi.createIntStream(arr).boxed().collect(Collectors.toList());

        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);

        assertEquals(test, result);
    }

    @Test
    void testCreateIntStream() {
        List<Integer> result = lsi.createIntStream(1, 4).boxed().collect(Collectors.toList());

        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);

        assertEquals(test, result);
    }

    @Test
    void squareRootIntStream() {
        List<Double> result = lsi.squareRootIntStream(
                IntStream.of(1, 4, 9)
        ).boxed().collect(Collectors.toList());

        List<Double> test = new ArrayList<>();
        test.add(1.0);
        test.add(2.0);
        test.add(3.0);

        assertEquals(test, result);
    }

    @Test
    void getOdd() {
        List<Integer> result = lsi.getOdd(
                IntStream.rangeClosed(1, 6)
        ).boxed().collect(Collectors.toList());

        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(3);
        test.add(5);

        assertEquals(test, result);
    }

    @Test
    void getLambdaPrinter() {
        Consumer<String> printer = lsi.getLambdaPrinter("start>", "<end");
        printer.accept("Hello");

        assertEquals("start>Hello<end\n", out.toString());
    }

    @Test
    void printMessages() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        String[] messages = {"a", "b", "c"};
        lsi.printMessages(messages, lsi.getLambdaPrinter("msg:", "!"));

        String expected =
                "msg:a!\n" +
                        "msg:b!\n" +
                        "msg:c!\n";

        assertEquals(expected, out.toString());
    }

    @Test
    void printOdd() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        lsi.printOdd(
                lsi.createIntStream(0, 5),
                lsi.getLambdaPrinter("odd number:", "!")
        );

        String expected =
                "odd number:1!\n" +
                        "odd number:3!\n" +
                        "odd number:5!\n";

        assertEquals(expected, out.toString());
    }

    @Test
    void flatNestedInt() {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);

        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);

        Stream<List<Integer>> input = Stream.of(
                list1,
                list2
        );

        List<Integer> result = lsi.flatNestedInt(input).collect(Collectors.toList());

        List<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(4);
        test.add(9);
        test.add(16);

        assertEquals(test, result);
    }
}