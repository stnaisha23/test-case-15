import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;

public class TestCase15 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input peta
        System.out.println("Masukkan gambar peta:");
        String[][] peta = inputPeta(scanner);

        int posisiAnneX = -1, posisiAnneY = -1;
        int posisiRumahX = -1, posisiRumahY = -1;
        for (int i = 0; i < peta.length; i++) {
            for (int j = 0; j < peta[i].length; j++) {
                if (peta[i][j].equals("^")) {
                    posisiAnneX = i;
                    posisiAnneY = j;
                } else if (peta[i][j].equals("*")) {
                    posisiRumahX = i;
                    posisiRumahY = j;
                }
            }
        }

        List<String> result = cariJalanTercepat(peta, posisiAnneX, posisiAnneY, posisiRumahX, posisiRumahY);

        System.out.println("\nOutput:");
        if (result == null) {
            System.out.println("Tidak ada jalan");
        } else {
            for (String res : result) {
                System.out.println(res);
            }
        }
    }

    public static String[][] inputPeta(Scanner scanner) {
        LinkedList<String> lines = new LinkedList<>();
        String line;
        while (!(line = scanner.nextLine()).equals("OK")) {
            lines.add(line);
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        String[][] peta = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            peta[i] = lines.get(i).split("");
        }

        return peta;
    }

    public static List<String> cariJalanTercepat(String[][] peta, int startX, int startY, int goalX, int goalY) {
        int rows = peta.length;
        int cols = peta[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows][cols];

        for (int[] row : parent) {
            Arrays.fill(row, -1);
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] dirNames = {"bawah", "atas", "kanan", "kiri"};

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        parent[startX][startY] = startX * cols + startY;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == goalX && y == goalY) {
                return reconstructPath(parent, startX, startY, goalX, goalY, directions, dirNames);
            }

            for (int i = 0; i < directions.length; i++) {
                int newX = x + directions[i][0];
                int newY = y + directions[i][1];

                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && !visited[newX][newY] && (peta[newX][newY].equals(" ") || peta[newX][newY].equals("*"))) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    parent[newX][newY] = x * cols + y;
                }
            }
        }
        return null;
    }

    private static List<String> reconstructPath(int[][] parent, int startX, int startY, int goalX, int goalY, int[][] directions, String[] dirNames) {
        List<String> result = new ArrayList<>();
        int cols = parent[0].length;

        Stack<int[]> stack = new Stack<>();
        int x = goalX;
        int y = goalY;

        while (x != startX || y != startY) {
            int prev = parent[x][y];
            int prevX = prev / cols;
            int prevY = prev % cols;

            for (int i = 0; i < directions.length; i++) {
                if (prevX == x + directions[i][0] && prevY == y + directions[i][1]) {
                    stack.push(new int[]{x, y, i});
                    break;
                }
            }
            x = prevX;
            y = prevY;
        }

        int[] lastStep = stack.pop();
        int currentDirection = lastStep[2];
        int stepCount = 1;

        while (!stack.isEmpty()) {
            int[] curr = stack.pop();
            if (curr[2] == currentDirection) {
                stepCount++;
            } else {
                result.add(stepCount + " " + dirNames[currentDirection]);
                currentDirection = curr[2];
                stepCount = 1;
            }
        }
        result.add(stepCount + " " + dirNames[currentDirection]);

        int totalLangkah = result.stream().mapToInt(s -> Integer.parseInt(s.split(" ")[0])).sum();
        result.add(totalLangkah + " langkah");

        return result;
    }
}
