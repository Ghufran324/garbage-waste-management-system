# 🗑️ Garbage Waste Management System - Java DSA Project

A real-world urban management application designed to efficiently handle city waste cleanup based on priority levels. This project demonstrates practical applications of Data Structures in **Java** by manually implementing core concepts without relying on built-in libraries.

---

## 📌 Problem Statement
In urban waste management, cleaning all city locations simultaneously is impossible due to limited manpower and resources. This system solves the problem by prioritizing areas with higher garbage fill levels and maintaining a log of recently serviced locations.

---

## 🛠️ Data Structures Implemented (Manual Implementation)

Instead of using standard Java built-in collection libraries (`PriorityQueue`, `Stack`), all data structures were built **manually from scratch** to demonstrate core DSA fundamentals:

* **Array:** Serves as the base data storage structure to hold location records (Location Name & Fill Level).
* **Priority Queue (Max Heap):** Maintains cleaning priority. The location with the highest garbage fill level stays at the root.
  * **Heapify Up:** Re-organizes the heap when new locations are added or fill levels increase.
  * **Heapify Down:** Restores the heap order after the highest priority location is cleaned and removed.
* **Stack (LIFO):** Stores a log of cleaned locations to easily track and display recent cleanup history.

---

## 📁 Repository Contents
* `Garbage Waste Management System / Source Files` - Java source code implementing custom DSA classes.
* `Garbage Waste Management System Project Report.pdf` - Complete detailed project documentation.

---

## 🚀 How to Run Locally

```bash
# 1. Clone this repository
git clone [https://github.com/Ghufran324/garbage-waste-management-system.git](https://github.com/Ghufran324/garbage-waste-management-system.git)

# 2. Compile Java files
javac Main.java

# 3. Run the application
java Main
