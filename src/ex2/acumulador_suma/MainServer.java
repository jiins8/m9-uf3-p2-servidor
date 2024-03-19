package ex2.acumulador_suma;

public class MainServer {
    public static void main(String[] args) {
        var server = new WelcomeServer();
        server.inicaServei();
        server.tancaConnexio();
    }
}
