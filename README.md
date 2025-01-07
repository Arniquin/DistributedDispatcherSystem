# Distributed Dispatcher System

## Overview
This project simulates a distributed system where processes are dispatched between nodes (dispatchers). It includes both long-term and short-term dispatchers that handle the execution and dispatching of processes, utilizing a client-server communication model.

## Key Features
- **LTDispatcher**: Handles long-term process dispatching, deciding whether to process locally or forward tasks to other nodes based on workload.
- **STDispatcher**: Processes tasks assigned to it, simulating execution time and communicating completion back to the originating node.
- **SharedFlag**: Used for inter-thread synchronization to ensure that dispatchers only act when necessary.
- **DataBase**: Stores and manages node and process information, used for tracking tasks and their execution.
- **Process**: Represents a task with essential details like its ID, origin node, and execution time.
- **Client**: Manages socket communication between nodes for sending and receiving process data.

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/dispatcher-node.git
    ```

2. Compile the Java files:
    ```bash
    javac dispatchernode/*.java
    ```

3. Run the dispatcher nodes:
    - Start the long-term dispatcher (LTDispatcher):
      ```bash
      java dispatchernode.LTDispatcher
      ```

    - Start the short-term dispatcher (STDispatcher):
      ```bash
      java dispatchernode.STDispatcher
      ```

## Usage
- **Long-Term Dispatcher**: Receives new processes from the generator or other nodes. It compares workloads across nodes and either processes the task locally or forwards it to the least-loaded node.
- **Short-Term Dispatcher**: Processes the tasks assigned to it and logs the completion status, notifying the originating node when a task is finished.

## Communication
Nodes communicate via a `Client` class, which handles socket communication. Messages are sent with a predefined format:
- **Message Format**:
    - `"0;ID;NODE_DATA"`: Updates database with node information.
    - `"1;DEST_ID;ONODE;PID;TIME"`: Sends a new generated process to another node.
    - `"2;ONODE;PID"`: Notifies the originating node that a process is complete.

## Dependencies
- Java 8 or higher for compiling and running the project.

## Contributing
Feel free to fork the repository, open issues, or submit pull requests for improvements.

## License
This project is licensed under the MIT License.
