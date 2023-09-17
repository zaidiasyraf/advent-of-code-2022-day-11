package org.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    private static final String INPUT = """
Monkey 0:
  Starting items: 54, 89, 94
  Operation: new = old * 7
  Test: divisible by 17
    If true: throw to monkey 5
    If false: throw to monkey 3

Monkey 1:
  Starting items: 66, 71
  Operation: new = old + 4
  Test: divisible by 3
    If true: throw to monkey 0
    If false: throw to monkey 3

Monkey 2:
  Starting items: 76, 55, 80, 55, 55, 96, 78
  Operation: new = old + 2
  Test: divisible by 5
    If true: throw to monkey 7
    If false: throw to monkey 4

Monkey 3:
  Starting items: 93, 69, 76, 66, 89, 54, 59, 94
  Operation: new = old + 7
  Test: divisible by 7
    If true: throw to monkey 5
    If false: throw to monkey 2

Monkey 4:
  Starting items: 80, 54, 58, 75, 99
  Operation: new = old * 17
  Test: divisible by 11
    If true: throw to monkey 1
    If false: throw to monkey 6

Monkey 5:
  Starting items: 69, 70, 85, 83
  Operation: new = old + 8
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 7

Monkey 6:
  Starting items: 89
  Operation: new = old + 6
  Test: divisible by 2
    If true: throw to monkey 0
    If false: throw to monkey 1

Monkey 7:
  Starting items: 62, 80, 58, 57, 93, 56
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 6
    If false: throw to monkey 4
""";

    public static void main(String[] args) {
        MonkeyOperation monkeyOperation = new MonkeyOperation(INPUT);

        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeyOperation.getMonkeys()) {
                monkeyOperation.inspect(monkeyOperation.getMonkeys().indexOf(monkey));
            }
        }

        monkeyOperation.monkeys.sort(Comparator.comparing(Monkey::getInspectCount).reversed());
        int total = monkeyOperation.getMonkeys().get(0).getInspectCount() * monkeyOperation.getMonkeys().get(1).getInspectCount();
        System.out.println("Answer part 1 : " + total);

        // todo : solve part 2, issue is test input for round 20 is different with result
        // probably due to int max
        MonkeyOperation monkeyOperation2 = new MonkeyOperation(INPUT, false);
        for (int i = 0; i < 10000; i++) {
//            if (i == 1000) {
//                int k = 0;
//            }
//            System.out.println("For round " + i + ", monkey 0 has " + monkeyOperation2.getMonkeys().get(0).getInspectCount() + ", monkey 1 has " + monkeyOperation2.getMonkeys().get(1).getInspectCount() + ", monkey 2 has " + monkeyOperation2.getMonkeys().get(2).getInspectCount() + ", monkey 3 has " + monkeyOperation2.getMonkeys().get(3).getInspectCount());
            for (Monkey monkey : monkeyOperation2.getMonkeys()) {
                monkeyOperation2.inspect(monkeyOperation2.getMonkeys().indexOf(monkey));
            }

        }

        monkeyOperation2.monkeys.sort(Comparator.comparing(Monkey::getInspectCount).reversed());
//        int total2 = monkeyOperation2.getMonkeys().get(0).getInspectCount() * monkeyOperation2.getMonkeys().get(1).getInspectCount();
        BigInteger total2 = BigInteger.valueOf(monkeyOperation2.getMonkeys().get(0).getInspectCount()).multiply(BigInteger.valueOf(monkeyOperation2.getMonkeys().get(1).getInspectCount()));
        System.out.println("Answer part 2 : " + total2);
    }

     static class MonkeyOperation{
        private  List<Monkey> monkeys = new ArrayList<>();
        private boolean shouldDivideBy3 = true;

        public void addMonkey(Monkey monkey) {
            monkeys.add(monkey);
        }

         public List<Monkey> getMonkeys() {
             return monkeys;
         }

         public  void inspect(int index) {
            Monkey monkey = monkeys.get(index);
            for (long item : monkey.getItems()) {
                monkey.addInspectCount();
                long afterOperate = monkey.doOperation(item);
                if (shouldDivideBy3) {
                    afterOperate = afterOperate / 3;
                    if (monkey.isDividedBy(afterOperate)) {
                        monkeys
                                .get(monkey.getTrueThenPassTo())
                                .addItem(afterOperate);
                    } else {
                        monkeys
                                .get(monkey.getFalseThenPastTo())
                                .addItem(afterOperate);
                    }
                } else {
                    afterOperate %= 9699690;
                    if (monkey.isDividedBy(afterOperate)) {
                        monkeys
                                .get(monkey.getTrueThenPassTo())
                                .addItem(afterOperate);
                    } else {
                        monkeys
                                .get(monkey.getFalseThenPastTo())
                                .addItem(afterOperate);
                    }
                }

            }
            monkey.clearItems();
        }

        public MonkeyOperation (String input) {
            this(input, true);
        }

        public MonkeyOperation (String input, boolean shouldDivide) {
            String[] lines = input.split(System.lineSeparator());
            Monkey currentMonkey = null;
            for (String line : lines) {
                if (line.startsWith("Monkey")) {
                    currentMonkey = new Monkey();
                } else if (line.contains("Starting items:")) {
                    String parse = line.substring(line.indexOf(":") + 1);
                    String[] items = parse.split(",");
                    for (String item : items) {
                        item = item.replaceAll("\\s", "");
                        currentMonkey.addItem(Long.parseLong(item));
                    }
                } else if (line.contains("Operation:")) {
                    String parse = line.substring(line.indexOf("=") + 2);
                    currentMonkey.setOperation(parse);
                } else if (line.contains("Test: divisible by")) {
                    String parse = line.substring(line.indexOf("by") + 3);
                    currentMonkey.setDivideBy(Integer.parseInt(parse));
                } else if (line.contains("If true: throw to")) {
                    String parse = line.substring(line.indexOf("monkey") + 7);
                    currentMonkey.setTrueThenPassTo(Integer.parseInt(parse));
                } else if (line.contains("If false: throw to")) {
                    String parse = line.substring(line.indexOf("monkey") + 7);
                    currentMonkey.setFalseThenPastTo(Integer.parseInt(parse));
                } else {
                    monkeys.add(currentMonkey);
                }
            }
            monkeys.add(currentMonkey);
            shouldDivideBy3 = shouldDivide;
        }

    }


    static class Monkey{
        private List<Long> items = new ArrayList<>();
        // Example:old * 7
        private String operation;
        private int divideBy;
        private int trueThenPassTo;
        private int falseThenPastTo;
        private int inspectCount = 0;

        public Monkey() {
        }

        public void setOperation(final String operation) {
            this.operation = operation;
        }

        public void setDivideBy(final int divideBy) {
            this.divideBy = divideBy;
        }

        public void setTrueThenPassTo(final int trueThenPassTo) {
            this.trueThenPassTo = trueThenPassTo;
        }

        public void setFalseThenPastTo(final int falseThenPastTo) {
            this.falseThenPastTo = falseThenPastTo;
        }

        public List<Long> getItems() {
            return items;
        }

        public void addItem(Long item) {
            items.add(item);
        }

        public void removeItem(Integer item) {
            items.remove(item);
        }

        public int getTrueThenPassTo() {
            return trueThenPassTo;
        }

        public int getFalseThenPastTo() {
            return falseThenPastTo;
        }

        public Long doOperation(Long item) {
            String statement = operation.replace("old", item.toString());
            String[] units = statement.split(" ");
            switch (units[1]) {
                case "+":
                    return Long.parseLong(units[0]) + Long.parseLong(units[2]);
                case "*":
                    return Long.parseLong(units[0]) * Long.parseLong(units[2]);
            }
            throw new UnsupportedOperationException("Operation not supported");
        }

        public boolean isDividedBy(long num) {
            return num % divideBy == 0;
        }

        public void addInspectCount() {
            inspectCount = inspectCount + 1;
        }

        public int getInspectCount() {
            return inspectCount;
        }

        public void clearItems() {
            items.clear();
        }

    }

}