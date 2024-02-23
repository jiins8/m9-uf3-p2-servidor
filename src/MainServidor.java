import java.io.IOException;

public class MainServidor {
    public static void main(String[] args) {
        ServidorMultifil server = new ServidorMultifil();
        try {
            server.inicia();
        } catch (IOException e) {
            System.out.println("Error iniciant el servidor");
        }
    }

}
