import java.io.*;
import java.net.Socket;

public class Gestor implements Runnable{
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    public Gestor(Socket socket) throws IOException {
        this.socket = socket;
        System.out.println("Creat nou client ");
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    @Override
    public void run() {
        System.out.println("Atenent al client ");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            writer.write("Ets el client amb adreça " + this.socket.getRemoteSocketAddress());
            writer.newLine();
            writer.flush();
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        } catch (IOException e) {
            System.out.println("Error escrivint al client ");
        }
        System.out.println("FI atenent al client ");
    }
}
