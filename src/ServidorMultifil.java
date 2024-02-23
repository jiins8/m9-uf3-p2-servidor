import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorMultifil {
    private ServerSocket serverSocket;
    private int PORT = 65000;

    public void inicia() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Iniciat servidor al port "
                + serverSocket.getLocalPort());
        while (true){
            Socket socket = serverSocket.accept();
            try {
                Gestor gestor = new Gestor(socket);
                new Thread(gestor).start();
            }catch (IOException e){
                System.out.println("Error creant gestor pel client ");
            }
        }
    }
}
