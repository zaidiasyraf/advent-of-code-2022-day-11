package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    private static final String INPUT = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
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
            for (Monkey monkey : monkeyOperation2.getMonkeys()) {
                monkeyOperation2.inspect(monkeyOperation2.getMonkeys().indexOf(monkey));
            }
            if (i == 0 || i == 19 || i == 999 || i == 1999 || i == 2999) {
                int j = 0;
            }
        }

        monkeyOperation2.monkeys.sort(Comparator.comparing(Monkey::getInspectCount).reversed());
        int total2 = monkeyOperation2.getMonkeys().get(0).getInspectCount() * monkeyOperation2.getMonkeys().get(1).getInspectCount();
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
            for (int item : monkey.getItems()) {
                monkey.addInspectCount();
                int afterOperate = monkey.doOperation(item);
                int dividedBy3 = afterOperate;
                if (shouldDivideBy3) {
                    dividedBy3 = afterOperate / 3;
                }
                if (monkey.isDividedBy(dividedBy3)) {
                    monkeys.get(monkey.getTrueThenPassTo()).addItem(dividedBy3);
                } else {
                    monkeys.get(monkey.getFalseThenPastTo()).addItem(dividedBy3);
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
                        currentMonkey.addItem(Integer.parseInt(item));
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
        private List<Integer> items = new ArrayList<>();
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

        public List<Integer> getItems() {
            return items;
        }

        public void addItem(Integer item) {
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

        public Integer doOperation(Integer item) {
            String statement = operation.replace("old", item.toString());
            String[] units = statement.split(" ");
            switch (units[1]) {
                case "+":
                    return Integer.parseInt(units[0]) + Integer.parseInt(units[2]);
                case "*":
                    return Integer.parseInt(units[0]) * Integer.parseInt(units[2]);
            }
            throw new UnsupportedOperationException("Operation not supported");
        }

        public boolean isDividedBy(int num) {
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