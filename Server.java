package dispatchernode;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class LTDispatcher extends Thread {
    private int ID; // ID of the long-term dispatcher
    volatile DataBase db; // Database instance
    volatile SharedFlag newGenProcess; // Flag for new generated processes
    volatile SharedFlag newNodeProcess; // Flag for new node processes
    volatile SharedFlag working; // Flag to indicate if the dispatcher is currently working
    volatile Process GenProcess; // Process generated by the generator
    volatile Process NodeProcess; // Process received from another node
    volatile Process current; // Currently active process
    Client c; // Client for socket communication
    JTextArea textArea; // Text area for logging output

    // Constructor for LTDispatcher
    public LTDispatcher(int ID, DataBase db, SharedFlag newGenProcess, SharedFlag newNodeProcess,
                        SharedFlag working, Process GenProcess, Process NodeProcess, Process current,
                        Client c, JTextArea textArea) {
        this.ID = ID; // Set the dispatcher ID
        this.db = db; // Initialize database
        this.newGenProcess = newGenProcess; // Initialize flag for new generated processes
        this.newNodeProcess = newNodeProcess; // Initialize flag for new node processes
        this.working = working; // Initialize working flag
        this.GenProcess = GenProcess; // Initialize generated process
        this.NodeProcess = NodeProcess; // Initialize node process
        this.current = current; // Initialize current process
        this.c = c; // Initialize client
        this.textArea = textArea; // Initialize text area for logging
    }
    
    /**
     * Updates the database by sending a message containing the node's information.
     * 
     * Input: None.
     * Output: Sends a message to the client containing the node ID and its data from the database.
     */
    public void updateDatabase() {
        String msj = "0;" + ID + ";"; // Create message with ID
        msj = msj + db.getNodeDB(ID); // Append database information for the node
        synchronized (c) { // Synchronize to ensure thread safety
            c.sendMessage(msj); // Send the message to the client
        }
    }
    
    /**
     * The main execution method for the LTDispatcher thread.
     * 
     * Input: None.
     * Output: Continuously processes incoming node and generated processes, updates the database,
     *         and dispatches processes as needed. Logs activity to the text area.
     */
    public synchronized void run() {
        Process temp; // Temporary process holder
        int aux; // Auxiliary variable for load comparison
        int NID = 0; // Node ID for least loaded node

        while (true) { // Infinite loop for continuous operation
            if (newNodeProcess.isFlag()) { // Check if there's a new process from another node
                db.addProcess(ID, NodeProcess); // Add the received process to the database
                newNodeProcess.setFlag(false); // Reset the flag
                textArea.append("Proceso recibido de un Nodo\n"); // Log received process
            }

            if (newGenProcess.isFlag()) { // Check if there's a new generated process
                aux = Integer.MAX_VALUE; // Initialize with the maximum possible load
                for (int i = 0; i < 3; i++) { // Iterate through nodes to find the least loaded
                    if (db.workLoad(i) < aux) { // Compare workload
                        aux = db.workLoad(i); // Update the least workload
                        NID = i; // Store the node ID
                    }
                }

                // If the current node is the least loaded node
                if (NID == ID) {
                    db.addProcess(ID, GenProcess); // Add the process locally
                    updateDatabase(); // Update the database
                    textArea.append("Proceso Local recibido\n"); // Log local process receipt
                } else {
                    // If another node is less loaded, send the process there
                    if (GenProcess.getPID() != 0) { // Check if there is a valid process
                        c.sendMessage("1;" + NID + ";" + GenProcess.getONode() + ";" + GenProcess.getPID() + ";" + GenProcess.getTime());
                        textArea.append("Proceso enviado al Nodo: " + NID + "\n"); // Log process sent
                    }
                }
                newGenProcess.setFlag(false); // Reset the flag
            }
            
            // Process dispatching logic
            temp = db.shortestP(ID); // Get the next process to dispatch
            aux = db.shortestI(ID); // Get the shortest index
            if (!db.isEmpty(ID)) { // Check if there are processes in the database
                if (!working.isFlag()) { // If the dispatcher is not currently working
                    current.setONode(temp.getONode()); // Set current node details
                    current.setPID(temp.getPID());
                    current.setTime(temp.getTime());
                    working.setFlag(true); // Set working flag
                    db.removeProcessI(ID, aux); // Remove the dispatched process from the database
                }
            }

            db.printDB(ID); // Print the current state of the database
            try {
                sleep(1000); // Sleep for a second before the next iteration
            } catch (InterruptedException ex) {
                Logger.getLogger(LTDispatcher.class.getName()).log(Level.SEVERE, null, ex); // Log exceptions
            }
        }
    }
}
