package ex2.endivina_num;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WelcomeServer {
    private static int clientCount = 0;
    private static final Object lock = new Object();
    private int PORT = 6000;
    private ServerSocket serverSocket;
    private Map<Integer, Integer> accumulatedSumMap = new HashMap<>();

    public void startService() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started...");

            while (true) {
                Socket socket = serverSocket.accept();
                int clientNumber = assignClientNumber();
                System.out.println("Client " + clientNumber + " connected.");
                accumulatedSumMap.put(clientNumber, 0);
                new Thread(new ClientHandler(socket, clientNumber)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int assignClientNumber() {
        synchronized (lock) {
            clientCount++;
            return clientCount;
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private int clientNumber;

        public ClientHandler(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

                writer.write("Welcome, you are the client number " + clientNumber);
                writer.newLine();
                writer.flush();

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) {
                        break;
                    }
                    try {
                        int number = Integer.parseInt(line);
                        synchronized (lock) {
                            int accumulatedSum = accumulatedSumMap.get(clientNumber);
                            accumulatedSum += number;
                            accumulatedSumMap.put(clientNumber, accumulatedSum);
                            writer.write("Accumulated Sum: " + accumulatedSum);
                            writer.newLine();
                            writer.flush();
                        }
                    } catch (NumberFormatException e) {
                        writer.write("Invalid input, please enter a number.");
                        writer.newLine();
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (lock) {
                    accumulatedSumMap.remove(clientNumber);
                }
            }
        }
    }
}
