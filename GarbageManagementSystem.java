import java.util.Scanner;

public class GarbageManagementSystem {

    // ===== System Data =====
    Location[] locationList;   // sari locations ki list
    int locationCount;         // kitni locations add ho chuki hain

    PriorityQueue pq;          // Max Heap based priority queue
    CleanHistoryStack history; // Cleaned locations ka record (Stack)

    // ===== Constructor =====
    GarbageManagementSystem(int capacity) {
        locationList = new Location[capacity];   // fixed size array
        pq = new PriorityQueue(capacity);        // heap ka size
        history = new CleanHistoryStack(capacity); // history stack
        locationCount = 0;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("\t Enter number of locations: ");
        int capacity = sc.nextInt();
        sc.nextLine(); // newline consume

        GarbageManagementSystem system = new GarbageManagementSystem(capacity);

        int choice;

        do {
            System.out.println("\n\t======================================");
            System.out.println("\t   GARBAGE MANAGEMENT SYSTEM MENU");
            System.out.println("\t======================================");
            System.out.println("\t 1. Add New Location");
            System.out.println("\t 2. Update Garbage Fill Level");
            System.out.println("\t 3. Show All Locations");
            System.out.println("\t 4. Show Next Location to Clean");
            System.out.println("\t 5. Clean a Location");
            System.out.println("\t 6. Show Cleaning History");
            System.out.println("\t 7. Exit System");
            System.out.print("\t Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // newline consume

            switch(choice) {

                // ===== Add Location =====
                case 1:
                    System.out.print("\n\t Enter location name: ");
                    String name = sc.nextLine();

                    System.out.print("\t Enter fill level (%): ");
                    float fillLevel = sc.nextFloat();

                    system.addLocation(name, fillLevel);
                    break;

                // ===== Update Fill Level =====
                case 2:
                    System.out.print("\n\t Enter location name to update: ");
                    String locName = sc.nextLine();

                    System.out.print("\t Enter new fill level (%): ");
                    float newLevel = sc.nextFloat();

                    system.updateFillLevel(locName, newLevel);
                    break;

                // ===== Display All Locations =====
                case 3:
                    system.displayAllLocations();
                    break;

                // ===== Next Location =====
                case 4:
                    Location next = system.getNextLocationToClean();
                    if(next != null)
                        System.out.println("\n\t Next location to clean: "
                                + next.name + " (" + next.fillLevel + "%)");
                    else
                        System.out.println("\n\t No locations available.");
                    break;

                // ===== Clean Location =====
                case 5:
                    system.cleanLocation();
                    break;

                // ===== History =====
                case 6:
                    System.out.println("\n\t ===== Cleaning History =====");
                    system.displayCleanHistory();
                    break;

                case 7:
                    System.out.println("\n\t System exited successfully!");
                    break;

                default:
                    System.out.println("\n\t Invalid choice! Please try again.");
            }

        } while(choice != 7);

        sc.close(); // Scanner band
    }

    // ===== Add Location =====
    // Duplicate check + array + heap insert
    void addLocation(String name, float fillLevel) {

        if (isDuplicateLocation(name)) {
            System.out.println("\n\t Location already exists!");
            return;
        }

        Location loc = new Location(name, fillLevel);
        locationList[locationCount++] = loc;
        pq.insert(loc);

        System.out.println("\n\t Location added successfully!");
    }

    // ===== Update Fill Level =====
    void updateFillLevel(String name, float newFillLevel) {

        if (isSystemEmpty()) {
            System.out.println("\n\t System is empty.");
            return;
        }

        for (int i = 0; i < locationCount; i++) {
            if (locationList[i].name.equals(name)) {

                locationList[i].fillLevel = newFillLevel;
                pq.updateFilllevelInQueue(name, newFillLevel);

                System.out.println("\n\t Fill level updated successfully!");
                return;
            }
        }

        System.out.println("\n\t Location not found.");
    }

    // ===== Get Next Location =====
    Location getNextLocationToClean() {
        if (pq.isEmpty()) return null;
        return pq.peek();
    }

    // ===== System Empty Check =====
    boolean isSystemEmpty() {
        return locationCount == 0;
    }

    // ===== All Clean Check =====
    boolean allLocationsCleaned() {
        for (int i = 0; i < locationCount; i++) {
            if (locationList[i].fillLevel > 0)
                return false;
        }
        return true;
    }

    // ===== Duplicate Check =====
    boolean isDuplicateLocation(String name) {
        for (int i = 0; i < locationCount; i++) {
            if (locationList[i].name.equals(name))
                return true;
        }
        return false;
    }

    // ===== Clean Location =====
    void cleanLocation() {

        if (pq.isEmpty()) {
            System.out.println("\n\t No location available to clean.");
            return;
        }

        if (allLocationsCleaned()) {
            System.out.println("\n\t All locations are already clean!");
            return;
        }

        Location cleaned = pq.removeMax(); // heapifyDown yahan hota hai
        cleaned.fillLevel = 0;
        history.push(cleaned);

        System.out.println("\n\t Location cleaned & added to history!");
    }

    // ===== Display History =====
    void displayCleanHistory() {
        if (history.isEmpty()) {
            System.out.println("\t No cleaning history found.");
            return;
        }
        history.display();
    }

    // ===== Display All Locations =====
    void displayAllLocations() {

        if (isSystemEmpty()) {
            System.out.println("\n\t No locations available.");
            return;
        }

        System.out.println("\n\t ===== All Locations Status =====");
        for (int i = 0; i < locationCount; i++) {
            System.out.println("\t " + locationList[i].name +
                    " --> Fill Level: " + locationList[i].fillLevel + "%");
        }
    }

    // ===== Location Class =====
    class Location {
        String name;
        float fillLevel;

        Location(String name, float fillLevel) {
            this.name = name;
            this.fillLevel = fillLevel;
        }
    }

    // ===== PRIORITY QUEUE (MAX HEAP) =====
    class PriorityQueue {

        Location[] heap;
        int size;

        PriorityQueue(int capacity) {
            heap = new Location[capacity];
            size = 0;
        }

        boolean isEmpty() {
            return size == 0;
        }

        // ===== Insert with Heapify Up =====
        void insert(Location loc) {
            heap[size] = loc;
            heapifyUp(size);
            size++;
        }

        // ===== Update Fill Level =====
        void updateFilllevelInQueue(String name, float newFillLevel) {

            for (int i = 0; i < size; i++) {
                if (heap[i].name.equals(name)) {

                    float oldFill = heap[i].fillLevel;
                    heap[i].fillLevel = newFillLevel;

                    // agar value barhi → heapifyUp
                    if (newFillLevel > oldFill)
                        heapifyUp(i);

                    // agar value kam hui → heapifyDown
                    else if (newFillLevel < oldFill)
                        heapifyDown(i);

                    return;
                }
            }
        }

        Location peek() {
            return heap[0];
        }

        // ===== Remove Max =====
        Location removeMax() {

            Location max = heap[0];
            heap[0] = heap[size - 1];
            size--;

            heapifyDown(0);
            return max;
        }

        // ===== Heapify Up =====
        void heapifyUp(int index) {

            while (index > 0) {

                int parent = (index - 1) / 2;

                if (heap[index].fillLevel > heap[parent].fillLevel) {
                    swap(index, parent);
                    index = parent;
                } else break;
            }
        }

        // ===== Heapify Down =====
        void heapifyDown(int index) {

            int largest = index;
            int left = 2 * index + 1;
            int right = 2 * index + 2;

            if (left < size &&
                heap[left].fillLevel > heap[largest].fillLevel)
                largest = left;

            if (right < size &&
                heap[right].fillLevel > heap[largest].fillLevel)
                largest = right;

            if (largest != index) {
                swap(index, largest);
                heapifyDown(largest);
            }
        }

        // ===== Swap =====
        void swap(int i, int j) {
            Location temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }
    }

    // ===== STACK (History) =====
    class CleanHistoryStack {

        Location[] stack;
        int top = -1;

        CleanHistoryStack(int capacity) {
            stack = new Location[capacity];
        }

        void push(Location loc) {
            stack[++top] = loc;
        }

        boolean isEmpty() {
            return top == -1;
        }

        void display() {
            for (int i = 0; i <= top; i++) {
                System.out.println("\t " + stack[i].name);
            }
        }
    }
}
