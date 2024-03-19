package ex2.acumulador_suma;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class WelcomeServer {

    private int PORT = 6000;
    private ServerSocket serverSocket;
    private int contadorClientes = 0;
    private Map<Integer, Integer> sumaAcumuladaMap = new HashMap<>();

    public void inicaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat...");

            while (true) {
                Socket socket = serverSocket.accept();
                contadorClientes++;
                System.out.println("Client " + contadorClientes + " connectat.");
                sumaAcumuladaMap.put(contadorClientes, 0); // Initialize sum for new client
                new Thread(new ConnectionHandler(socket, contadorClientes)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tancaConnexio() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ConnectionHandler implements Runnable {
        private Socket socket;
        private int clientNumber;

        public ConnectionHandler(Socket socket, int clientNumber) {
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

                String line;
                while ((line = reader.readLine()) != null) {
                    int numero = Integer.parseInt(line);
                    int sumaAcumulada = sumaAcumuladaMap.get(clientNumber);
                    sumaAcumulada += numero;
                    sumaAcumuladaMap.put(clientNumber, sumaAcumulada);

                    writer.write("Client " + clientNumber + " - Suma acumulada: " + sumaAcumulada);
                    writer.newLine();
                    writer.flush();
                }

                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
