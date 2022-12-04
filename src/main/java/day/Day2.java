package day;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static commons.GetInput.getLines;

public class Day2 {

//    char rockA = 'A'; char paperB = 'B'; char scissorsC = 'C';
//    char rockY = 'X'; char paperX = 'Y'; char scissorsZ = 'Z';

//            1                 2                   3
//    char rockA = 'A'; char paperB = 'B'; char scissorsC = 'C';
//            0                3                  6
//    char iLose = 'X'; char draw = 'Y';   char iWin = 'Z';



    public static List<List<String>> resultsMatrixPart1 = List.of(
            List.of("you", "tie", "opp"),
            List.of("opp", "tie", "you"),
            List.of("tie", "opp", "you")
    );

    public static List<List<String>> myHandPart2 = List.of(
            List.of("C", "A", "B"),
            List.of("A", "B", "C"),
            List.of("B", "C", "A")
    );
    public static Map<String, Integer> oppOptionsAndScores = Map.of(
            "A", 1,
            "B", 2,
            "C", 3);
    public static Map<String, Integer> myOptionsAndScores = Map.of(
            "X", 1,
            "Y", 2,
            "Z", 3);

    public static Map<String, Integer> expectedResultScore = Map.of(
            "X", 0,
            "Y", 3,
            "Z", 6);

    public static Map<String, Integer> scores = Map.of(
            "opp", 0,
            "tie", 3,
            "you", 6
            );


    public static void main(String[] args) throws IOException {
        Integer block = getLines(2)
                .map(line -> {
                    String[] hand = line.split(" ");
                    int handScore = playPart1(hand);
                    return handScore;
                })
                .reduce(Integer::sum)
                .block();

        System.out.println(block);

        Integer part2 = getLines(2)
                .map(line -> {
                    String[] hand = line.split(" ");
                    int handScore = playPart2(hand);
                    return handScore;
                })
                .reduce(Integer::sum)
                .block();

        System.out.println("Part2:" + part2);
    }

    static int playPart1(String[] hand) {
        Integer oppScore = oppOptionsAndScores.get(hand[0]);
        int oppIndex = oppScore - 1;

        Integer myScore = myOptionsAndScores.get(hand[1]);
        int myIndex = myScore - 1;

        String result = resultsMatrixPart1.get(oppIndex).get(myIndex);
        Integer handScore = scores.get(result);

        return myScore + handScore;
    }


    static int playPart2(String[] hand) {


//    A-X       0   +    3
//    rockA - loseX => scissorsC
//    A-Y       3    +   1
//    rockA - drawY => rockA
//    A-Z       6    +   2
//    rockA - winZ => paperB
//    C, A, B

//    B-X          0   +  1
//    paperB - loseX => rockA
//    B-Y          3   +   2
//    paperB - drawY => paperB
//    B-Z          6   +   3
//    paperB - winZ => scissorsC
//    A, B, C

//    C-X             0   +    2
//    scissorsC - loseX => paperB
//    C-Y             3   +    3
//    scissorsC - drawY => scissorsC
//    C-Z              6   +   1
//    scissorsC - winZ => rockA
//    B, C,A

        Integer oppScore = oppOptionsAndScores.get(hand[0]);
        int oppIndex = oppScore - 1;

        Integer expectedScore = expectedResultScore.get(hand[1]);
        Integer expectedResultIndex = myOptionsAndScores.get(hand[1]) - 1;

        String myHand = myHandPart2.get(oppIndex).get(expectedResultIndex);
        Integer handScore = oppOptionsAndScores.get(myHand);

        return expectedScore + handScore;
    }

}

