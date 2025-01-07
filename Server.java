package dispatchernode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {
    private int port; // Port on which the server listens
    private DataBase database; // Shared database for managing nodes and processes
    private boolean running; // Flag to control server execution

    public Server(int port, DataBase database) {
        this.port = port; // Initialize server port
        this.database = database; // Reference shared database
        this.running = true; // Set server to running state
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + port);

            while (running) {
                // Wait for client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle client in a separate thread
                new Thread(new ClientHandler(clientSocket, database)).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Error in server", ex);
        }
    }

    public void stopServer() {
        running = false; // Stop server loop
        System.out.println("Server stopped.");
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataBase database;

        public ClientHandler(Socket clientSocket, DataBase database) {
            this.clientSocket = clientSocket;
            this.database = database;
        }

        @Override
        public void run() {
            try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)
            ) {
                String message;
                while ((message = reader.readLine()) != null) {
                    System.out.println("Received: " + message);
                    processMessage(message, writer);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Error in client handler", ex);
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, "Error closing client socket", ex);
                }
            }
        }

        private void processMessage(String message, PrintWriter writer) {
            String[] parts = message.split(";");
            switch (parts[0]) {
                case "0": // Update node information
                    int nodeID = Integer.parseInt(parts[1]);
                    String nodeData = parts[2];
                    database.updateNode(nodeID, nodeData);
                    writer.println("Node updated: " + nodeID);
                    break;

                case "1": // Receive a new process
                    int destID = Integer.parseInt(parts[1]);
                    int originNode = Integer.parseInt(parts[2]);
                    int processID = Integer.parseInt(parts[3]);
                    int processTime = Integer.parseInt(parts[4]);
                    Process process = new Process(processID, originNode, processTime);
                    database.addProcess(destID, process);
                    writer.println("Process added: " + processID + " to Node " + destID);
                    break;

                case "2": // Process completion notification
                    int completedOrigin = Integer.parseInt(parts[1]);
                    int completedPID = Integer.parseInt(parts[2]);
                    database.markProcessComplete(completedOrigin, completedPID);
                    writer.println("Process completed: " + completedPID);
                    break;

                default:
                    writer.println("Unknown command: " + message);
                    break;
            }
        }
    }
}
