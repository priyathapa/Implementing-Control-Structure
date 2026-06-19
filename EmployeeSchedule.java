import java.util.*;

public class EmployeeSchedule {

    static String[] days = {"Monday", "Tuesday", "Wednesday","Thursday", "Friday", "Saturday", "Sunday"};

    static String[] shifts = { "morning", "afternoon", "evening" };

    static final int MAX_DAYS_PER_WEEK = 5;
    static final int MIN_EMPLOYEES_PER_SHIFT = 2;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Map<String, Map<String, List<String>>> employees = new HashMap<>();

        Map<String, Map<String, List<String>>> weeklySchedule = new HashMap<>();

        Map<String, Integer> daysWorked = new HashMap<>();


        // CREATE EMPTY SCHEDULE

        for (String day : days) {

            Map<String, List<String>> shiftMap = new HashMap<>();

            for (String shift : shifts) {
                shiftMap.put(shift, new ArrayList<>());
            }

            weeklySchedule.put(day, shiftMap);
        }


        // INPUT AND STORAGE

        System.out.print("Enter number of employees: ");

        int numEmployees = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numEmployees; i++) {

            System.out.print("\nEnter employee " + (i + 1) + " name: ");

            String name = scanner.nextLine().trim();

            Map<String, List<String>> preferences = new HashMap<>();

            for (String day : days) {

                System.out.println("\nEnter " + name +
                        "'s shift preferences for " + day);

                String first;
                String second;
                String third;

                while (true) {

                    System.out.print("First preference: ");

                    first = scanner.nextLine()
                            .toLowerCase()
                            .trim();

                    if (isValidShift(first)) {
                        break;
                    }

                    System.out.println("Invalid shift.");
                }

                while (true) {

                    System.out.print("Second preference: ");

                    second = scanner.nextLine()
                            .toLowerCase()
                            .trim();

                    if (isValidShift(second)
                            && !second.equals(first)) {
                        break;
                    }

                    System.out.println("Invalid or duplicate.");
                }

                while (true) {

                    System.out.print("Third preference: ");

                    third = scanner.nextLine()
                            .toLowerCase()
                            .trim();

                    if (isValidShift(third)
                            && !third.equals(first)
                            && !third.equals(second)) {
                        break;
                    }

                    System.out.println("Invalid or duplicate.");
                }

                preferences.put(
                        day,
                        Arrays.asList(first, second, third)
                );
            }

            employees.put(name, preferences);

            daysWorked.put(name, 0);
        }


        // SCHEDULING LOGIC

        for (String day : days) {

            for (String shift : shifts) {

                while (weeklySchedule
                        .get(day)
                        .get(shift)
                        .size() < MIN_EMPLOYEES_PER_SHIFT) {

                    List<String> availableEmployees =
                            new ArrayList<>();

                    for (String employee :
                            employees.keySet()) {

                        if (canAssign(
                                employee,
                                day,
                                weeklySchedule,
                                daysWorked)) {

                            availableEmployees
                                    .add(employee);
                        }
                    }

                    if (availableEmployees.isEmpty()) {

                        System.out.println(
                                "\nWarning: Not enough "
                                        + "employees for "
                                        + day + " "
                                        + shift
                        );

                        break;
                    }

                   availableEmployees.sort(
                        Comparator
                                .comparingInt((String employee) ->
                                getRank(
                                        employees,
                                        employee,
                                        day,
                                        shift
                                )
                        )
                        .thenComparingInt(
                                 employee -> daysWorked.get(employee)
                        )
                );

                    String selected =
                            availableEmployees.get(0);

                    weeklySchedule
                            .get(day)
                            .get(shift)
                            .add(selected);

                    daysWorked.put(
                            selected,
                            daysWorked.get(selected) + 1
                    );
                }
            }
        }


        // OUTPUT

        System.out.println(
                "\n------------------------"
        );

        System.out.println(
                "FINAL WEEKLY EMPLOYEE SCHEDULE"
        );

        System.out.println(
                "------------------------"
        );

        for (String day : days) {

            System.out.println(
                    "\n" + day
            );

            for (String shift : shifts) {

                System.out.println(
                        capitalize(shift)
                                + ": "
                                + String.join(
                                ", ",
                                weeklySchedule
                                        .get(day)
                                        .get(shift)
                        )
                );
            }
        }


        // DAYS WORKED SUMMARY

        System.out.println(
                "\n------------------------"
        );

        System.out.println(
                "DAYS WORKED SUMMARY"
        );

        System.out.println(
                "------------------------"
        );

        for (String employee :
                daysWorked.keySet()) {

            System.out.println(
                    employee
                            + ": "
                            + daysWorked.get(employee)
                            + " days"
            );
        }

        scanner.close();
    }


    static boolean isValidShift(
            String shift) {

        return shift.equals("morning")
                || shift.equals("afternoon")
                || shift.equals("evening");
    }


    static boolean canAssign(
            String employee,
            String day,

            Map<String,
                    Map<String,
                            List<String>>> schedule,

            Map<String,
                    Integer> daysWorked) {

        if (daysWorked.get(employee)
                >= MAX_DAYS_PER_WEEK) {

            return false;
        }

        for (String shift : shifts) {

            if (schedule.get(day)
                    .get(shift)
                    .contains(employee)) {

                return false;
            }
        }

        return true;
    }


    static int getRank(

            Map<String,
                    Map<String,
                            List<String>>> employees,

            String employee,

            String day,

            String shift) {

        return employees
                .get(employee)
                .get(day)
                .indexOf(shift);
    }


    static String capitalize(
            String word) {

        return word.substring(0, 1)
                .toUpperCase()

                + word.substring(1);
    }
}
