package ex2.fitxers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WelcomeServer {

    private static final int PORT = 8000;
    private static final String PUBLIC_DIRECTORY = "C:\\Users\\jiins8\\OneDrive\\Escritorio\\Documentacion";

    private ServerSocket serverSocket;

    public void iniciaServidor() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                Thread clientThread = new Thread(() -> {
                    try {
                        handleClient(socket);
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
        try {
            sendFileList(clientSocket);
            receiveAndSendFile(clientSocket);
        } finally {
            clientSocket.close();
        }
    }

    private void sendFileList(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        File directory = new File(PUBLIC_DIRECTORY);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        out.println(file.getName());
                    }
                }
            }
        }
        out.println("END_OF_LIST");
        out.flush();
    }

    private void receiveAndSendFile(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String fileName = in.readLine();
        if (fileName != null) {
            Path filePath = Paths.get(PUBLIC_DIRECTORY, fileName);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                byte[] fileBytes = Files.readAllBytes(filePath);
                out.println(fileBytes.length); // Send file size
                out.flush();
                clientSocket.getOutputStream().write(fileBytes, 0, fileBytes.length);
                clientSocket.getOutputStream().flush();
            }
        }
    }
}
