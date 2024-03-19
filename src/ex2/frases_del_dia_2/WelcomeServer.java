package ex2.frases_del_dia_2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class WelcomeServer {

    private static final String[] PHRASES = {
            "Never give someone the opportunity to waste your time twice",
            "Discipline + Focus + Action = Success",
            "Push yourself because no one else is going to do it for you",
            "Take the risk or lose the chance",
            "If you want it, work for it"
    };

    private static Map<String, List<String>> clientHistory = new HashMap<>();

    private int PORT = 8000;
    private ServerSocket serverSocket;

    public void inicaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket);

                Thread clientThread = new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) throws IOException {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String clientAddress = clientSocket.getInetAddress().getHostAddress();

            // Retrieve client's history or create new if it doesn't exist
            List<String> history = clientHistory.getOrDefault(clientAddress, new ArrayList<>());

            // Select a phrase that hasn't been sent to the client yet
            String newPhrase = selectNewPhrase(history);

            // Send the new phrase to the client
            out.println(newPhrase);

            // Add the new phrase to the client's history
            history.add(newPhrase);
            clientHistory.put(clientAddress, history);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String selectNewPhrase(List<String> history) {
        List<String> remainingPhrases = new ArrayList<>();
        for (String phrase : PHRASES) {
            if (!history.contains(phrase)) {
                remainingPhrases.add(phrase);
            }
        }
        if (remainingPhrases.isEmpty()) {
            return "No new phrases available";
        } else {
            Random random = new Random();
            return remainingPhrases.get(random.nextInt(remainingPhrases.size()));
        }
    }
}
