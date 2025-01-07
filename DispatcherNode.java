package dispatchernode;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class DispatcherNode extends JFrame {
    
    int ID; // ID of the dispatcher node
    volatile DataBase db; // Database instance for the dispatcher node
    // Flags for process management
    volatile SharedFlag newGenProcess; // Flag for new generated processes
    volatile SharedFlag newNodeProcess; // Flag for new node processes
    volatile SharedFlag working; // Flag to indicate if a dispatcher is working
    volatile Process GenProcess; // Process for generating tasks
    volatile Process NodeProcess; // Process for managing node tasks
    public volatile Process current; // Currently active process
    Client c; // Client for socket communication
    Server s; // Server instance for handling connections
    LTDispatcher LTD; // Long-term dispatcher
    STDispatcher STD; // Short-term dispatcher
    ProcessGen pg; // Process generator instance
    // GUI components
    volatile JTextArea textArea; // Text area for short-term dispatcher logging
    volatile JTextArea textArea2; // Text area for long-term dispatcher logging
    volatile JTextArea textArea3; // Text area for server logging
    volatile JTextArea textArea4; // Text area for process queue
    JScrollPane scrollPane; // Scroll pane for text area 1
    JScrollPane scrollPane2; // Scroll pane for text area 2
    JScrollPane scrollPane3; // Scroll pane for text area 3
    JScrollPane scrollPane4; // Scroll pane for text area 4
    JLabel DLabel; // Label for short-term dispatcher
    JLabel DLabel2; // Label for long-term dispatcher
    JLabel DLabel3; // Label for server
    JLabel DLabel4; // Label for process queue
    
    // Constructor for DispatcherNode
    DispatcherNode(int ID) {
        setSize(1000, 800); // Set window size
        setResizable(false); // Disable resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit on close
        setLayout(null); // Use absolute positioning

        // Initialize labels
        DLabel = new JLabel("Despachador de corto plazo:");
        DLabel.setBounds(20, 5, 300, 20);
        DLabel2 = new JLabel("Despachador de largo plazo:");
        DLabel2.setBounds(20, 235, 300, 20);
        DLabel3 = new JLabel("Servidor:");
        DLabel3.setBounds(20, 465, 300, 20);
        DLabel4 = new JLabel("Cola de procesos:");
        DLabel4.setBounds(500, 5, 300, 20);
        
        // Initialize text areas with auto-scrolling caret
        textArea = new JTextArea(); // Short-term dispatcher log
        DefaultCaret caret1 = (DefaultCaret) textArea.getCaret();
        caret1.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        textArea2 = new JTextArea(); // Long-term dispatcher log
        DefaultCaret caret2 = (DefaultCaret) textArea2.getCaret();
        caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        textArea3 = new JTextArea(); // Server log
        DefaultCaret caret3 = (DefaultCaret) textArea3.getCaret();
        caret3.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        textArea4 = new JTextArea(); // Process queue log
        DefaultCaret caret4 = (DefaultCaret) textArea4.getCaret();
        caret4.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        // Initialize scroll panes for text areas
        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 30, 420, 200);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().add(textArea);
        
        scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(20, 260, 420, 200);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.getViewport().add(textArea2);
        
        scrollPane3 = new JScrollPane();
        scrollPane3.setBounds(20, 490, 420, 200);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane3.getViewport().add(textArea3);
        
        scrollPane4 = new JScrollPane();
        scrollPane4.setBounds(500, 30, 420, 660);
        scrollPane4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane4.getViewport().add(textArea4);
        
        // Add components to the frame
        add(DLabel);
        add(DLabel2);
        add(DLabel3);
        add(DLabel4);
        add(scrollPane);
        add(scrollPane2);
        add(scrollPane3);
        add(scrollPane4);
        
        this.ID = ID; // Assign ID to dispatcher node
        this.db = new DataBase(ID, textArea4); // Initialize database
        newGenProcess = new SharedFlag(); // Initialize flags
        newNodeProcess = new SharedFlag();
        working = new SharedFlag();
        GenProcess = new Process(); // Initialize processes
        NodeProcess = new Process();
        current = new Process();
        c = new Client(ID, textArea3); // Initialize client
        s = new Server(ID, db, newNodeProcess, NodeProcess, c, textArea3); // Initialize server
        LTD = new LTDispatcher(ID, db, newGenProcess, newNodeProcess, working, GenProcess, NodeProcess, current, c, textArea2); // Long-term dispatcher
        STD = new STDispatcher(ID, working, current, c, textArea); // Short-term dispatcher
        pg = new ProcessGen(ID, GenProcess, newGenProcess); // Process generator
        
        // Start threads
        STD.start();
        LTD.start();
        s.start();
        pg.start();
        
        setTitle("Nodo " + ID); // Set window title
        
        // Sample output for text areas (for debugging/testing)
        textArea.append("Despachando:  Proceso: " + 234 + " | Nodo Origen: " + 1 + " | Tiempo de trabajo: " + 3 + "\n");
        textArea.append("Terminado:  Proceso: " + 234 + " | Nodo Origen: " + 1 + " | Tiempo de trabajo: " + 3 + "\n");
        textArea.append("Despachando:  Proceso: " + 307 + " | Nodo Origen: " + 2 + " | Tiempo de trabajo: " + 6 + "\n");
        textArea.append("Terminado:  Proceso: " + 307 + " | Nodo Origen: " + 2 + " | Tiempo de trabajo: " + 6 + "\n");
        textArea.append("Despachando:  Proceso: " + 175 + " | Nodo Origen: " + 0 + " | Tiempo de trabajo: " + 2 + "\n");
        textArea.append("Terminado:  Proceso: " + 175 + " | Nodo Origen: " + 0 + " | Tiempo de trabajo: " + 2 + "\n");
        
        textArea2.append("Proceso recibido de un Nodo\n");
        textArea2.append("Proceso enviado al Nodo: " + 1 + "\n");
        textArea2.append("Proceso enviado al Nodo: " + 0 + "\n");
        textArea2.append("Proceso enviado al Nodo: " + 0 + "\n");
        textArea2.append("Proceso enviado al Nodo: " + 1 + "\n");
        textArea2.append("Proceso recibido de un Nodo\n");
        
        textArea3.append("[" + ID + "]" + "Nodo iniciado" + "\n");
        textArea3.append("[" + ID + "]" + "Cliente conectado" + "\n");
        
        textArea3.append("[" + ID + "]" + "Proceso recibido\n");
        textArea3.append("[" + ID + "]" + "Cliente conectado" + "\n");
        textArea3.append("[" + ID + "]" + "Actualizacion de la base de datos exitosa" + "\n");
        
        textArea3.append("[" + ID + "]" + "Cliente conectado" + "\n");
        textArea3.append("[" + ID + "]" + "Proceso recibido\n");
        
        textArea4.append("Proceso: " + 145 + " | Nodo Origen: " + 0 + " | Tiempo de trabajo: " + 3 + "\n");
        textArea4.append("Proceso: " + 180 + " | Nodo Origen: " + 0 + " | Tiempo de trabajo: " + 5 + "\n");
        textArea4.append("Proceso: " + 370 + " | Nodo Origen: " + 1 + " | Tiempo de trabajo: " + 6 + "\n");
    }
    
    public static void main(String[] args) {
        DispatcherNode dn = new DispatcherNode(1); // Create a new dispatcher node with ID 1
        dn.setVisible(true); // Make the window visible
    }
}
