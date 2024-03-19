package ex2.frases_del_dia;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class WelcomeServer {
    private static final String[] FRASES = {
            "Never give someone the opportunity to waste your time twice",
            "Dicipline + Focus + Action = Success",
            "Push yourself because no one else is going to do it for you",
            "Take the risk or lose the chance",
            "If you want it, work for it"
    };

    private int PORT = 7000;
    private ServerSocket serverSocket;
    private Random random = new Random();

    public void inicaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat: " + clientSocket);

                Thread clientThread = new Thread(() -> {
                    try {
                        enviarFrase(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarFrase(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        int index = random.nextInt(FRASES.length);
        out.println(FRASES[index]);
        out.close();
    }

    public void tancaConnexio() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
