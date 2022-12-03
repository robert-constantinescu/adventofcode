package day1;

import java.io.IOException;
import java.util.List;

import static commons.GetInput.getLocalInput;

public class Day1 {

    public static void main(String[] args) throws IOException {

        List<String> block = getLocalInput(1)
                .collectList()
                .block();


        int size = block.size();
        System.out.println(size);

        Integer biggestValue = 0;
        int temp = 0;
        for (String s : block) {
//            System.out.println("1 -> " + s);
            if ("".equals(s)) {
                if (temp >= biggestValue) {
                    biggestValue = temp;
                }
                temp = 0;
                continue;
            }
//            System.out.println("2 -> " + s);
            int calories = Integer.parseInt(s);
            temp += calories;
        }

        System.out.println("Most Calories " + biggestValue);
//
//        getLocalInput(1)
//                .map(bytes -> {
//                    try {
////                        System.out.println(bytes);
//                        return Files.writeString(
//                                Path.of("src/main/resources/input/result_day_1.txt"),
//                                bytes,
//                                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .collectList()
//                .block();
    }
}
