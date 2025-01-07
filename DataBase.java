package dispatchernode;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class DataBase {
    volatile private ArrayList<Process> Node0 = new ArrayList<>();
    volatile private ArrayList<Process> Node1 = new ArrayList<>();
    volatile private ArrayList<Process> Node2 = new ArrayList<>();
    private int ID; // Database ID
    volatile JTextArea textArea; // UI text area for logging

    public DataBase(int ID, JTextArea textArea) {
        this.ID = ID;
        this.textArea = textArea;
    }
    
    // Check if the specified node's process list is empty
    public boolean isEmpty(int NID) {
        switch (NID) {
            case 0: return Node0.isEmpty();
            case 1: return Node1.isEmpty();
            case 2: return Node2.isEmpty();
        } 
        return false;
    }
    
    // Add a process to the specified node's list
    public int addProcess(int NID, Process p) {
        Process temp = new Process(0, 0, 0); // Temporary process object
        switch (NID) {
            case 0:
                Node0.add(temp);
                Node0.get(Node0.size() - 1).setONode(p.getONode());
                Node0.get(Node0.size() - 1).setPID(p.getPID());
                Node0.get(Node0.size() - 1).setTime(p.getTime());
                break;
            case 1:
                Node1.add(temp);
                Node1.get(Node1.size() - 1).setONode(p.getONode());
                Node1.get(Node1.size() - 1).setPID(p.getPID());
                Node1.get(Node1.size() - 1).setTime(p.getTime());
                break;
            case 2:
                Node2.add(temp);
                Node2.get(Node2.size() - 1).setONode(p.getONode());
                Node2.get(Node2.size() - 1).setPID(p.getPID());
                Node2.get(Node2.size() - 1).setTime(p.getTime());
                break;
        }
        return 0;
    }
    
    // Remove a process by PID from the specified node
    public void removeProcess(int NID, int PID) {
        Process temp;
        int index = -1; // To hold the index of the process to remove
        switch (NID) {
            case 0:
                if (!Node0.isEmpty()) {
                    for (int i = 0; i < Node0.size(); i++) {
                        temp = Node0.get(i);
                        if (temp.getPID() == PID) index = i;
                    }
                    if (index != -1) Node0.remove(index);
                }
                break;
            case 1:
                if (!Node1.isEmpty()) {
                    for (int i = 0; i < Node1.size(); i++) {
                        temp = Node1.get(i);
                        if (temp.getPID() == PID) index = i;
                    }
                    if (index != -1) Node1.remove(index);
                }
                break;
            case 2:
                if (!Node2.isEmpty()) {
                    for (int i = 0; i < Node2.size(); i++) {
                        temp = Node2.get(i);
                        if (temp.getPID() == PID) index = i;
                    }
                    if (index != -1) Node2.remove(index);
                }
                break;
        }
    }
    
    // Remove a process at a specific index from the specified node
    public void removeProcessI(int NID, int index) {
        switch (NID) {
            case 0:
                if (!Node0.isEmpty()) {
                    Node0.remove(index);
                }
                break;
            case 1:
                if (!Node1.isEmpty()) {
                    Node1.remove(index);
                }
                break;
            case 2:
                if (!Node2.isEmpty()) {
                    Node2.remove(index);
                }
                break;
        }
    }
    
    // Calculate the total workload of a node
    public int workLoad(int NID) {
        int total = 0;
        switch (NID) {
            case 0:
                if (!Node0.isEmpty()) {
                    for (Process p : Node0) {
                        total += p.getTime();
                    }
                }
                break;
            case 1:
                if (!Node1.isEmpty()) {
                    for (Process p : Node1) {
                        total += p.getTime();
                    }
                }
                break;
            case 2:
                if (!Node2.isEmpty()) {
                    for (Process p : Node2) {
                        total += p.getTime();
                    }
                }
                break;
        }
        return total;
    }
    
    // Get the process with the shortest execution time from a node
    public Process shortestP(int NID) {
        int minTime = Integer.MAX_VALUE;
        int index = -1;
        switch (NID) {
            case 0:
                for (int i = 0; i < Node0.size(); i++) {
                    if (Node0.get(i).getTime() < minTime) {
                        minTime = Node0.get(i).getTime();
                        index = i;
                    }
                }
                if (index != -1) return Node0.get(index);
                break;
            case 1:
                for (int i = 0; i < Node1.size(); i++) {
                    if (Node1.get(i).getTime() < minTime) {
                        minTime = Node1.get(i).getTime();
                        index = i;
                    }
                }
                if (index != -1) return Node1.get(index);
                break;
            case 2:
                for (int i = 0; i < Node2.size(); i++) {
                    if (Node2.get(i).getTime() < minTime) {
                        minTime = Node2.get(i).getTime();
                        index = i;
                    }
                }
                if (index != -1) return Node2.get(index);
                break;
        }
        return null; // No process found
    } 
    
    // Get the index of the process with the shortest execution time
    public int shortestI(int NID) {
        int minTime = Integer.MAX_VALUE;
        int index = -1;
        switch (NID) {
            case 0:
                for (int i = 0; i < Node0.size(); i++) {
                    if (Node0.get(i).getTime() < minTime) {
                        minTime = Node0.get(i).getTime();
                        index = i;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < Node1.size(); i++) {
                    if (Node1.get(i).getTime() < minTime) {
                        minTime = Node1.get(i).getTime();
                        index = i;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < Node2.size(); i++) {
                    if (Node2.get(i).getTime() < minTime) {
                        minTime = Node2.get(i).getTime();
                        index = i;
                    }
                }
                break;
        }
        return index; // Return -1 if no process found
    } 
    
    // Clear all processes from a node
    public void cleanNode(int NID) {
        switch (NID) {
            case 0: Node0.clear(); break;
            case 1: Node1.clear(); break;
            case 2: Node2.clear(); break;
        }
    }
    
    // Get a formatted string of the processes in a node
    public String getNodeDB(int NID) {
        StringBuilder temp = new StringBuilder();
        switch (NID) {
            case 0:
                for (int i = 0; i < Node0.size(); i++) {
                    temp.append(Node0.get(i).getONode()).append("-")
                        .append(Node0.get(i).getPID()).append("-")
                        .append(Node0.get(i).getTime());
                    if (i != Node0.size() - 1) temp.append(";");
                }
                break;
            case 1:
                for (int i = 0; i < Node1.size(); i++) {
                    temp.append(Node1.get(i).getONode()).append("-")
                        .append(Node1.get(i).getPID()).append("-")
                        .append(Node1.get(i).getTime());
                    if (i != Node1.size() - 1) temp.append(";");
                }
                break;
            case 2:
                for (int i = 0; i < Node2.size(); i++) {
                    temp.append(Node2.get(i).getONode()).append("-")
                        .append(Node2.get(i).getPID()).append("-")
                        .append(Node2.get(i).getTime());
                    if (i != Node2.size() - 1) temp.append(";");
                }
                break;
        }
        return temp.toString();
    }
    
    // Print the processes in a node to the UI text area
    public void printDB(int NID) {
        textArea.setText(""); // Clear the text area
        String aux;
        switch (NID) {
            case 0:
                if (!Node0.isEmpty()) {
                    for (Process p : Node0) {
                        aux = "-Proceso: " + p.getPID() + " | Nodo de Origen: " + p.getONode() + " | Tiempo: " + p.getTime() + "\n";
                        textArea.append(aux); // Append to the text area
                    }
                } else {
                    textArea.append("Node0 is Empty\n");
                }
                break;
            case 1:
                if (!Node1.isEmpty()) {
                    for (Process p : Node1) {
                        aux = "-Proceso: " + p.getPID() + " | Nodo de Origen: " + p.getONode() + " | Tiempo: " + p.getTime() + "\n";
                        textArea.append(aux); // Append to the text area
                    }
                } else {
                    textArea.append("Node1 is Empty\n");
                }
                break;
            case 2:
                if (!Node2.isEmpty()) {
                    for (Process p : Node2) {
                        aux = "-Proceso: " + p.getPID() + " | Nodo de Origen: " + p.getONode() + " | Tiempo: " + p.getTime() + "\n";
                        textArea.append(aux); // Append to the text area
                    }
                } else {
                    textArea.append("Node2 is Empty\n");
                }
                break;
        }
    }
}
