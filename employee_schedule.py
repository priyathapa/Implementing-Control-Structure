import random

days = [ "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]

shifts = ["morning", "afternoon", "evening"]

MAX_DAYS_PER_WEEK = 5
MIN_EMPLOYEES_PER_SHIFT = 2

# 1. INPUT AND STORAGE

employees = {}

num_employees = int(input("Enter number of employees: "))

for i in range(num_employees):

    name = input(f"\nEnter employee {i + 1} name: ").strip()

    employees[name] = {}

    for day in days:

        print(f"\nEnter {name}'s shift preferences for {day}.")
        print("Use morning, afternoon, evening.")

        first = input("First preference: ").lower().strip()

        while first not in shifts:
            first = input("Invalid. Enter first preference again: ").lower().strip()

        second = input("Second preference: ").lower().strip()

        while second not in shifts or second == first:
            second = input("Invalid or duplicate. Enter second preference again: ").lower().strip()

        third = input("Third preference: ").lower().strip()

        while third not in shifts or third == first or third == second:
            third = input("Invalid or duplicate. Enter third preference again: ").lower().strip()

        employees[name][day] = [first, second, third]


# 2. CREATE EMPTY SCHEDULE

weekly_schedule = {}

for day in days:
    weekly_schedule[day] = {}

    for shift in shifts:
        weekly_schedule[day][shift] = []


days_worked = {}

for employee in employees:
    days_worked[employee] = 0


# 3. HELPER FUNCTIONS

def is_employee_working_that_day(employee, day):
    for shift in shifts:
        if employee in weekly_schedule[day][shift]:
            return True
    return False


def can_assign(employee, day):
    if days_worked[employee] >= MAX_DAYS_PER_WEEK:
        return False

    if is_employee_working_that_day(employee, day):
        return False

    return True


def assign_employee(employee, day, shift):
    weekly_schedule[day][shift].append(employee)
    days_worked[employee] += 1


def get_preference_rank(employee, day, shift):
    preferences = employees[employee][day]
    return preferences.index(shift)


# 4. SCHEDULING LOGIC

for day in days:
    for shift in shifts:

        while len(weekly_schedule[day][shift]) < MIN_EMPLOYEES_PER_SHIFT:

            available_employees = []

            for employee in employees:
                if can_assign(employee, day):
                    available_employees.append(employee)

            if len(available_employees) == 0:
                print(f"\nWarning: Not enough available employees for {day} {shift}.")
                break

            available_employees.sort(
                key=lambda employee: (
                    get_preference_rank(employee, day, shift),
                    days_worked[employee]
                )
            )

            selected_employee = available_employees[0]
            assign_employee(selected_employee, day, shift)


# 5. CONFLICT RESOLUTION

for day in days:
    for shift in shifts:

        if len(weekly_schedule[day][shift]) < MIN_EMPLOYEES_PER_SHIFT:

            for employee in employees:

                if can_assign(employee, day):
                    assign_employee(employee, day, shift)

                    if len(weekly_schedule[day][shift]) == MIN_EMPLOYEES_PER_SHIFT:
                        break


# 6. OUTPUT FINAL SCHEDULE

print("\n------------------------")
print("FINAL WEEKLY EMPLOYEE SCHEDULE")
print("------------------------")

for day in days:
    print(f"\n{day}")
    print("-" * len(day))

    for shift in shifts:
        assigned_employees = weekly_schedule[day][shift]

        if len(assigned_employees) > 0:
            employee_list = ", ".join(assigned_employees)
        else:
            employee_list = "No employees assigned"

        print(f"{shift.capitalize()}: {employee_list}")


# 7. DAYS WORKED SUMMARY

print("\n------------------------")
print("DAYS WORKED SUMMARY")
print("------------------------")

for employee, count in days_worked.items():
    print(f"{employee}: {count} days")
