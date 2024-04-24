
// Import required Java classes
import java.io.File;
import java.io.IOException;
import java.util.*;

// This class represents a sliding puzzle solver
public class SlidingPuzzles {
    private int numberOfRows;    // Number of rows and columns in the grid
    private int numOfColumns;
    private char[][] grid;     // 2D array to store the grid/map
    private int startRow;
    private int finishRow;     // Start and finish coordinates
    private int startCol;
    private int finishCol;
    private final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};       // Array to store directions (up, down, left, right)

    public SlidingPuzzles(String fileName) {
        try {
            if (!fileName.equals("")) {
                loadMap(fileName);
                addStartAndFinish();
                displayMap();
                List<int[]> shortestPath = findShortestPath();
                if (shortestPath != null && !shortestPath.isEmpty()) {
                    printPath(shortestPath);
                    System.out.println();
                } else {
                    System.out.println("\nNo path found.");
                }
            }
        } catch (IOException e) {
            System.out.println("\nThe file does not exist. Please verify the file name and try again.");
        }
    }

    // Method to load the map from a file
    public void loadMap(String fileName) throws IOException {
        List<String> linesOfMap = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            linesOfMap.add(scanner.nextLine());
        }
        this.numberOfRows = linesOfMap.size();
        this.numOfColumns = linesOfMap.get(0).length();
        this.grid = new char[numberOfRows][numOfColumns];
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                grid[i][j] = linesOfMap.get(i).charAt(j);
            }
        }
    }

    // Method to display the map
    public void displayMap() {

        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    // Method to add start and finish coordinates to the grid
    private void addStartAndFinish() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                if (grid[i][j] == 'S') {
                    this.startRow = i;
                    this.startCol = j;
                } else if (grid[i][j] == 'F') {
                    this.finishRow = i;
                    this.finishCol = j;
                }
            }
        }
        System.out.println("\nSuccessfully Loaded the file data\n");
    }

    // Method to construct the path from start to finish
    private List<int[]> constructPath(int[][] parent) {
        List<int[]> path = new ArrayList<>();
        int currentRow = finishRow;
        int currentCol = finishCol;
        while (currentRow != startRow || currentCol != startCol) {
            path.add(0, new int[]{currentRow, currentCol});
            int index = parent[currentRow][currentCol];
            currentRow = index / numOfColumns;
            currentCol = index % numOfColumns;
        }
        path.add(0, new int[]{startRow, startCol});
        return path;
    }

    // Method to find the shortest path using BFS
    public List<int[]> findShortestPath() {
        boolean[][] visited = new boolean[numberOfRows][numOfColumns];
        int[][] parent = new int[numberOfRows][numOfColumns];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            if (row == finishRow && col == finishCol) {
                return constructPath(parent);
            }
            for (int[] direction : directions) {
                int newRow = row;
                int newCol = col;
                while (true) {
                    newRow += direction[0];
                    newCol += direction[1];
                    if (!Validation(newRow, newCol) || grid[newRow][newCol] == '0') {
                        break;
                    }
                    if (grid[newRow][newCol] == 'F') {
                        parent[newRow][newCol] = row * numOfColumns + col;
                        return constructPath(parent);
                    }
                }
                newRow -= direction[0];
                newCol -= direction[1];
                if (!visited[newRow][newCol]) {
                    queue.offer(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = row * numOfColumns + col;
                }
            }
        }
        return new ArrayList<>();
    }

    // Method to print the path
    public void printPath(List<int[]> path) {
        System.out.println("\nPath:\n");
        System.out.println("1. Starting at (" + (startCol + 1) + "," + (startRow + 1) + ")");
        int j = 0;
        for (int i = 1; i < path.size(); i++) {
            int[] currentCell = path.get(i);
            int[] previousCell = path.get(i - 1);
            int changeOfRow = currentCell[0] - previousCell[0];
            int changeOfColumn = currentCell[1] - previousCell[1];
            String change = "";
            if (changeOfColumn == 0 && changeOfRow < 0) {
                change = "Move up to ";
            } else if (changeOfRow > 0 && changeOfColumn == 0) {
                change = "Move down to ";
            } else if (changeOfRow == 0 && changeOfColumn > 0) {
                change = "Move right to ";
            } else if (changeOfRow == 0 && changeOfColumn < 0) {
                change = "Move left to ";
            }
            System.out.println((i + 1) + ". " + change + "(" + (currentCell[1] + 1) + "," + (currentCell[0] + 1) + ")");
            j = i;
        }
        System.out.println((j + 2) + ". Done!");
    }


    // Method to validate row and column indices
    private boolean Validation(int row, int col) {
        return row >= 0 && row < numberOfRows && col >= 0 && col < numOfColumns;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAvailable files in the project directory");
        System.out.println("\nbenchmark_series/maze10_1.txt");
        System.out.println("benchmark_series/maze20_1.txt");
        System.out.println("benchmark_series/maze30_1.txt");
        System.out.println("benchmark_series/puzzle_10.txt");
        System.out.println("benchmark_series/puzzle_20.txt");
        System.out.println("benchmark_series/puzzle_40.txt");
        System.out.println("benchmark_series/puzzle_80.txt");
        System.out.println("benchmark_series/puzzle_160.txt");

        System.out.print("\nPick One of above list: ");
        String fileName = scanner.next();

        SlidingPuzzles mapSolver = new SlidingPuzzles(fileName);
    }
}
