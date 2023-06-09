/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.kruskal2;

import java.util.*;

class Edge implements Comparable<Edge> {

    int source;
    int destination;
    int weight;
    boolean isUsed;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.isUsed = false;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
}

class Graph {

    int numVertices;
    List<Edge> edges;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination, int weight) {
        Edge edge = new Edge(source, destination, weight);
        edges.add(edge);
    }

    public List<Edge> findMinimumSpanningTree() {
        List<Edge> minimumSpanningTree = new ArrayList<>();

        // Élek rendezése súly szerint növekvő sorrendbe
        Collections.sort(edges);

        // Rangok és szülők inicializálása
        int[] ranks = new int[numVertices];
        int[] parents = new int[numVertices];
        Arrays.fill(ranks, 0);
        for (int i = 0; i < numVertices; i++) {
            parents[i] = i;
        }

        // Élek feszítőfához való hozzáadása
        for (Edge edge : edges) {
            int sourceParent = findParent(parents, edge.getSource());
            int destinationParent = findParent(parents, edge.getDestination());

            if (sourceParent != destinationParent) {
                minimumSpanningTree.add(edge);
                union(parents, ranks, sourceParent, destinationParent);
            }
        }

        return minimumSpanningTree;
    }

    private int findParent(int[] parents, int vertex) {
        if (parents[vertex] != vertex) {
            parents[vertex] = findParent(parents, parents[vertex]);
        }
        return parents[vertex];
    }

    private void union(int[] parents, int[] ranks, int vertex1, int vertex2) {
        if (ranks[vertex1] < ranks[vertex2]) {
            parents[vertex1] = vertex2;
        } else if (ranks[vertex1] > ranks[vertex2]) {
            parents[vertex2] = vertex1;
        } else {
            parents[vertex2] = vertex1;
            ranks[vertex1]++;
        }
    }

}

class Dijkstra {

    private final int INFINITY = Integer.MAX_VALUE;

    public void findShortestPath(List<Edge> edges, int numVertices, int source, int destination, List<Edge> usedEdges) {
        List<List<Edge>> adjacencyList = buildAdjacencyList(edges, numVertices);
        int[] distances = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] previous = new int[numVertices];

        Arrays.fill(distances, INFINITY);
        Arrays.fill(visited, false);
        Arrays.fill(previous, -1);

        distances[source] = 0;

        for (int i = 0; i < numVertices - 1; i++) {
            int minDistance = INFINITY;
            int minVertex = -1;

            for (int j = 0; j < numVertices; j++) {
                if (!visited[j] && distances[j] < minDistance) {
                    minDistance = distances[j];
                    minVertex = j;
                }
            }

            if (minVertex == -1) {
                break;
            }

            visited[minVertex] = true;

            for (Edge edge : adjacencyList.get(minVertex)) {
                int neighbor = edge.getDestination();
                int newDistance = distances[minVertex] + edge.getWeight();

                if (!visited[neighbor] && newDistance < distances[neighbor]) {
                    distances[neighbor] = newDistance;
                    previous[neighbor] = minVertex;
                }
            }
        }
        int currentVertex = destination;

        while (currentVertex != -1 && previous[currentVertex] != -1) {
            int previousVertex = previous[currentVertex];
            usedEdges.add(findEdge(edges, previousVertex, currentVertex));
            currentVertex = previousVertex;
        }

    }

    private List<List<Edge>> buildAdjacencyList(List<Edge> edges, int numVertices) {
        List<List<Edge>> adjacencyList = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (Edge edge : edges) {
            adjacencyList.get(edge.getSource()).add(edge);
            adjacencyList.get(edge.getDestination()).add(new Edge(edge.getDestination(), edge.getSource(), edge.getWeight()));
        }

        return adjacencyList;
    }

    private Edge findEdge(List<Edge> edges, int source, int destination) {
        int i = 0;
        for (Edge edge : edges) {
            if (edge.getSource() == source && edge.getDestination() == destination) {
                edge.setUsed(true);
                edges.set(i, edge);
                return edge;
            }
            i++;
        }
        return null;
    }
}

public class Kruskal2 {

    static int[][] matrix;

    static int[][] createMatrix(List<Edge> minimumSpanningTree, int numVertices) {
        int[][] matrix = new int[numVertices][numVertices];

        for (Edge edge : minimumSpanningTree) {
            matrix[edge.source][edge.destination] = edge.weight;
            matrix[edge.destination][edge.source] = edge.weight;
        }

        return matrix;
    }

    static void toMatrix(int numVertices) {

        // Mátrix kiíratása, így könnyebb ellenőrízni
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int numVertices = 6;
        Graph graph = new Graph(numVertices);
        Dijkstra dijkstra = new Dijkstra();
        Random random = new Random();
        int min = 1;
        int max = 10;

        //utak az egyetemi épületek között
        graph.addEdge(0, 1, random.nextInt(max - min + 1) + min);
        graph.addEdge(0, 2, random.nextInt(max - min + 1) + min);
        graph.addEdge(1, 2, random.nextInt(max - min + 1) + min);
        graph.addEdge(1, 3, random.nextInt(max - min + 1) + min);
        graph.addEdge(1, 4, random.nextInt(max - min + 1) + min);
        graph.addEdge(2, 3, random.nextInt(max - min + 1) + min);
        graph.addEdge(3, 4, random.nextInt(max - min + 1) + min);
        graph.addEdge(3, 5, random.nextInt(max - min + 1) + min);
        graph.addEdge(4, 5, random.nextInt(max - min + 1) + min);

        
        // Minimális költségű feszítőfa keresése
        List<Edge> minimumSpanningTree = graph.findMinimumSpanningTree();

        System.out.println("Minimális költségű feszítőfa élei:");
        for (Edge edge : minimumSpanningTree) {
            System.out.println(edge.getSource() + " - " + edge.getDestination() + " : " + edge.getWeight());
        }
        matrix = createMatrix(minimumSpanningTree, numVertices);
        toMatrix(numVertices);
        // Csúcsok közötti legrövidebb út meghatározása
        // Dijkstra algoritmussal végignézzük a Csúcsok közötti legrövidebb útakat, azokat az éleket amiket nem érintenek azokat elhagyjuk a feszítőfából
        List<Integer> selectedNodes = Arrays.asList(2, 3, 5); // egyetemi csúcsok
        List<Edge> usedEdges = new ArrayList<>();
        for (int i = 0; i < selectedNodes.size(); i++) {
            for (int j = 0; j < selectedNodes.size(); j++) {
                dijkstra.findShortestPath(minimumSpanningTree, numVertices, selectedNodes.get(i).intValue(), selectedNodes.get(j).intValue(), usedEdges);
            }
        }

        System.out.println("Kerékpárutak");
        int roadToPaint = 0;
        for (Edge edge : minimumSpanningTree) {
            if (edge.isUsed) {
                System.out.println(edge.source + " - " + edge.destination + ", távolság: " + edge.weight);
                roadToPaint += edge.weight;
            }
        }
        System.out.println("Ekkora hosszúságú kerékpár utat kell felfesteni: " + roadToPaint);
    }
}
