package day;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static commons.GetInput.getLines;
import static commons.GetInput.getLocalInput;

public class Day1 {

    public static void main(String[] args) throws IOException {
        int mostCalories = part1();
        System.out.println(mostCalories);
        int mostCaloriesTop3 = part2();
        System.out.println(mostCaloriesTop3);

    }

    public static int part1() throws IOException {
        List<String> block = getLocalInput(1)
                .collectList()
                .block();

        block = getLines(1)
                .collectList()
                .block();


        int size = block.size();
        System.out.println(size);

        Integer mostCalories = 0;
        int temp = 0;
        for (String s : block) {
//            System.out.println("1 -> " + s);
            if ("".equals(s)) {
                if (temp >= mostCalories) {
                    mostCalories = temp;
                }
                temp = 0;
                continue;
            }
//            System.out.println("2 -> " + s);
            int calories = Integer.parseInt(s);
            temp += calories;
        }

        return mostCalories;
    }

    public static int part2() throws IOException {
        Flux<String> lines = getLines(1);

        lines.takeWhile(line -> !line.isEmpty())
                .map(Integer::parseInt)
                .reduce(0, Day1::add)
                .map(result -> {
                    System.out.println(result);
                    return result;
                });

        List<String> block = lines.collectList().block();
        List<Integer> all = new ArrayList<>();
        int tmp = 0;
        for (String value : block) {
            if ("".equals(value)) {
                all.add(tmp);
                tmp = 0;
                continue;
            }
            int i = Integer.parseInt(value);
            tmp += i;
        }

//        all.sort(Comparator.reverseOrder());
        all.sort((x1, x2) -> x2.compareTo(x1));
        System.out.println(all);
        System.out.println(all.get(0));
        System.out.println(all.get(1));
        System.out.println(all.get(2));

        int total = all.get(0) + all.get(1) + all.get(2);
        return total;
    }

    public static int add(Integer x1, Integer x2) {
        return x1 + x2;
    }

}
