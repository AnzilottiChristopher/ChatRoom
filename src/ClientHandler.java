import java.net.Socket;

public class ClientHandler implements Runnable
{
    //TCP Data
    private Socket socket;

    public ClientHandler(Socket socket)
    {
        this.socket = socket;

    }

    @Override
    public void run()
    {

    }
}
