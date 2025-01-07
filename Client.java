package dispatchernode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextArea;

public class Client {
    int ID; // Client ID
    JTextArea textArea; // UI text area for logging
    final String HOST = "192.168.0.135"; // Server host address
    final int PUERTO = 7000; // Server port
    DataInputStream in; // Input stream for reading messages
    DataOutputStream out; // Output stream for sending messages

    /**
     * Constructor to initialize the client.
     */
    Client(int ID, JTextArea textArea) {
        this.ID = ID;
        this.textArea = textArea;
    }
    
    /**
     * Sends a message to the server and logs the response.
     */
    public void sendMessage(String message) {
        try {
            // Establish connection to the server
            Socket sc = new Socket(HOST, PUERTO);
            textArea.append("[" + ID + "] Cliente Iniciado\n");
            
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            // Send message to the server
            out.writeUTF(message);

            // Receive and log the server's response
            String respuesta = in.readUTF();
            textArea.append(respuesta + "\n");

            sc.close(); // Close the socket

        } catch (IOException ex) {
            textArea.append("[" + ID + "] Error de conexión Cliente\n");
        }
    }
}
