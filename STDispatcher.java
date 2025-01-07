package dispatchernode;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class STDispatcher extends Thread {
    int ID; // ID of the dispatcher
    volatile SharedFlag working; // Flag to indicate if dispatcher is working
    volatile Process current; // Current process to dispatch
    Client c; // Client for communication
    JTextArea textArea; // UI text area for logging

    public STDispatcher(int ID, SharedFlag working, Process current, Client c, JTextArea textArea) {
        this.ID = ID; // Initialize dispatcher ID
        this.working = working; // Initialize shared working flag
        this.current = current; // Initialize current process
        this.c = c; // Initialize client
        this.textArea = textArea; // Initialize text area for logging
    }
    
    // Run method executed when thread starts
    public synchronized void run() {
        int aux; // Auxiliary variable for process time
        while (true) {
            // Check if the dispatcher is working
            if (working.isFlag()) {
                if (current.getPID() != 0) { // Proceed if there is a current process
                    textArea.append("Despachando:  Proceso: " + current.getPID() + 
                                    " | Nodo Origen: " + current.getONode() + 
                                    " | Tiempo de trabajo: " + current.getTime() + "\n");
                    aux = current.getTime(); // Get process time

                    try {
                        sleep(aux * 1000); // Simulate processing time
                    } catch (InterruptedException ex) {
                        Logger.getLogger(STDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // Check if the process originated from this dispatcher
                    if (current.getONode() == ID) {
                        textArea.append("Terminado:        Proceso: " + current.getPID() + 
                                        " | Nodo Origen: " + current.getONode() + 
                                        " | Tiempo de trabajo: " + current.getTime() + "\n");
                    } else {
                        textArea.append("Terminado:        Proceso: " + current.getPID() + 
                                        " | Nodo Origen: " + current.getONode() + 
                                        " | Tiempo de trabajo: " + current.getTime() + "\n");
                        c.sendMessage("2;" + current.getONode() + ";" + current.getPID()); // Notify other node
                    }
                }
                
                working.setFlag(false); // Reset working flag
            }
        }
    }
}
