import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

class PowerLine {
    String cityA;
    String cityB;

    public PowerLine(String cityA, String cityB) {
        this.cityA = cityA;
        this.cityB = cityB;
    }
}

// Students can define new classes here

public class PowerGrid {
    int numCities;
    int numLines;
    String[] cityNames;
    PowerLine[] powerLines;

    int[] discoveryTime;
    int[] lowValue; // make a different class for graph
    boolean[] visited;
    int[] low;
    // ArrayList<Integer>[] al;
    ArrayList<ArrayList<Integer>> al = new ArrayList<ArrayList<Integer>>();
    ArrayList<PowerLine> ar;
    // Node root=null;
    int time = 0;
    HashMap<Integer, Integer> map2 = new HashMap<Integer, Integer>();
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    ArrayList<Integer> list = new ArrayList<Integer>();

    // Students can define private variables here

    public PowerGrid(String filename) throws Exception {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        numCities = Integer.parseInt(br.readLine());
        numLines = Integer.parseInt(br.readLine());
        cityNames = new String[numCities];
        for (int i = 0; i < numCities; i++) {
            cityNames[i] = br.readLine();
        }
        powerLines = new PowerLine[numLines];
        for (int i = 0; i < numLines; i++) {
            String[] line = br.readLine().split(" ");
            powerLines[i] = new PowerLine(line[0], line[1]);
        }

        // TO be completed by students

        for (int i = 0; i < numCities; i++) {
            al.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < powerLines.length; i++) {
            System.out.println(powerLines[i].cityA + " " + powerLines[i].cityB + " " + i);
        }
        discoveryTime = new int[numCities];
        low = new int[numCities];
        lowValue = new int[numCities];
        visited = new boolean[numCities];
        // makegraph(cityNames, powerLines, map);
        for (int i = 0; i < cityNames.length; i++) {
            String c = cityNames[i];
            map.put(c, i);

        }
        // ArrayList<Integer>[] al = new ArrayList[numCities];
        // for (int i = 0; i < numCities; i++) {
        //     al[i] = new ArrayList<Integer>();
        // }
        for (int j = 0; j < powerLines.length; j++) {

            String c1 = powerLines[j].cityA;
            String c2 = powerLines[j].cityB;
            int a = map.get(c1);
            int b = map.get(c2);
            // System.out.println(a+" a");
            // System.out.println(b+" b" + " "+ a+" a"+ " " + " "+ c1+" "+c2+' '+ j + "
            // aaa");
            al.get(a).add(b);
            al.get(b).add(a);

            // System.out.println(a+" "+b);
            // System.out.println(c1+" "+c2);
            // System.out.println(map.get(c1)+" "+map.get(c2));
        }
        DFS(0, -1);
        System.out.println(list);
        // root=convert(al);
        // for (int i = 0; i < numCities; i++) {
        // System.out.println(low[i] + " " + dist[i] + " " + "low");
        // }

        // TO be completed by students
    }

    private void DFS(int u, int parent) {
        visited[u] = true;
        discoveryTime[u] = lowValue[u] = ++time;
        for (int v : al.get(u)) {
            if (!visited[v]) {
                DFS(v, u);
                lowValue[u] = Math.min(lowValue[u], lowValue[v]);

                // check if the edge u-v is a bridge
                if (discoveryTime[u] < lowValue[v]) {
                    System.out.println(cityNames[u] + " " + cityNames[v] + " is a bridge");
                    list.add(u);
                    list.add(v);

                }
            } else if (v != parent) {
                lowValue[u] = Math.min(lowValue[u], discoveryTime[v]);
            }
        }
        // System.out.println(ar + " " + "ar");

    }

    private int[] shortestPath(int source, int destination) {
        // Initialize visited array to keep track of visited nodes
        boolean[] visited = new boolean[al.size()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }

        // Initialize distance array to keep track of shortest distance from source
        int[] dist = new int[al.size()];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = -1;
        }
        dist[source] = 0;

        // Initialize parent array to keep track of parent of each node in shortest path
        int[] parent = new int[numCities];
        for (int i = 0; i < numCities; i++) {
            parent[i] = -1;
        }

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(source);
        visited[source] = true;

        while (!queue.isEmpty()) {
            // Dequeue a vertex from queue
            int node = queue.poll();

            // If destination is found, backtrack to find the shortest path
            if (node == destination) {
                ArrayList<Integer> path = new ArrayList<Integer>();
                int temp = destination;
                while (temp != -1) {
                    path.add(temp);
                    temp = parent[temp];
                }
                int[] result = new int[path.size()];
                for (int i = 0; i < path.size(); i++) {
                    result[i] = path.get(path.size() - i - 1);
                }
                return result;
            }

            // Get all adjacent vertices of the dequeued vertex node
            ArrayList<Integer> neighbors = al.get(node);
            for (int i = 0; i < neighbors.size(); i++) {
                int neighbor = neighbors.get(i);
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    dist[neighbor] = dist[node] + 1;
                    parent[neighbor] = node;
                }
            }
        }

        // If destination is not reachable from source
        return new int[] { -1 };
    }

    public ArrayList<PowerLine> criticalLines() {
        /*
         * Implement an efficient algorithm to compute the critical transmission lines
         * in the power grid.
         
         * Expected running time: O(m + n), where n is the number of cities and m is the
         * number of transmission lines.
         */

        System.out.println(list.size());
        ArrayList<PowerLine> ar1 = new ArrayList<PowerLine>();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));

            // System.out.println("hhhh");
        }
        for (int i = 0; i < list.size(); i++) {
            int a = list.get(i);
            int b = list.get(i + 1);
            String c1 = cityNames[a];
            String c2 = cityNames[b];
            PowerLine p = new PowerLine(c1, c2);
            ar1.add(p);
            i++;
        }
        // for (int i = 0; i < powerLines.length; i++) {
        for (int i = 0; i < ar1.size(); i++) {
            System.out.println(ar1.get(i).cityA + " " + ar1.get(i).cityB + " " + "derfvcvfev");
        }
        return ar1;
    }

    public void preprocessImportantLines() {
        /*
         * Implement an efficient algorithm to preprocess the power grid and build
         * required data structures which you will use for the numImportantLines()
         * method. This function is called once before calling the numImportantLines()
         * method. You might want to define new classes and data structures for this
         * method.
         
         * Expected running time: O(n * logn), where n is the number of cities.
         */

        ar = criticalLines();

        for (int i = 0; i < numCities; i++) {
            map2.put(i, -1);
        }
        for (int i = 0; i < ar.size(); i++) {
            String c1 = ar.get(i).cityA;
            String c2 = ar.get(i).cityB;
            int a = map.get(c1);
            int b = map.get(c2);
            map2.put(a, b);
            map2.put(b, a);
        }

        return;
    }

    public int numImportantLines(String cityA, String cityB) {
        /*
         * Implement an efficient algorithm to compute the number of important
         * transmission lines between two cities. Calls to numImportantLines will be
         * made only after calling the preprocessImportantLines() method once.
         
         * Expected running time: O(logn), where n is the number of cities.
         */
        int count = 0;
        int a = map.get(cityA);
        int b = map.get(cityB);
        int x;
        int y;
        // ArrayList<Integer> ar1 =new ArrayList<Integer>(null);
        // ArrayList<Integer>[] al=new ArrayList[numCities];
        // for (int i = 0; i < numCities; i++) {
        // al[i]=new ArrayList<Integer>();
        // }
        // makegraph(cityNames, powerLines, map, al);
        System.out.println(al.get(0));
        int[] arr = shortestPath(a, b);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i] + " " + "arr");
        }
        System.out.println(a + " " + b + " " + "ab");

        for (int i = 0; i < arr.length; i++) {
            int m1 = arr[i];
            if (map2.get(m1) != -1) {
                int m2 = map2.get(m1);
                if (i + 1 < arr.length) {
                    if (m2 == arr[i + 1]) {
                        count++;
                    }
                }
            }

        }
        // printTree(root);
        // System.out.println(root.data);
        // System.out.println(root.left.data);
        // // System.out.println(root.right.data);
        // System.out.println(root.left.left.data);
        // System.out.println(root.left.right.data);
        // int count=0;

        // ArrayList<Node> ar=shortestPath(root, a, b);
        // for (int i = 0; i < ar.size(); i++) {
        // int m1=ar.get(i).data;
        // if (map2.get(m1) != -1) {
        // int m2=map2.get(m1);
        // if(i+1<ar.size())
        // {
        // if (m2==ar.get(i+1).data) {
        // count++;
        // }
        // }
        // }

        // }

        return count;
    }

    // public static void main(String[] args) {
    //     try {
    //         PowerGrid powerGrid = new PowerGrid("/home/mayank/Videos/Q1/input.txt");

    //         // System.out.println(powerGrid.numCriticalLines());
    //         ArrayList<PowerLine> criticalLines = powerGrid.criticalLines();
    //         for (PowerLine powerLine : criticalLines) {
    //             System.out.println(powerLine.cityA + " " + powerLine.cityB);
    //         }
    //         powerGrid.preprocessImportantLines();
    //         System.out.println(powerGrid.numImportantLines(powerGrid.cityNames[0], powerGrid.cityNames[2]));
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}