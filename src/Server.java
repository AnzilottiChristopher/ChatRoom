import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable
{
    //Server information
    private int port;
    private ServerSocket serverSocket;


    //Client information
    private List<ClientHandler> clientHandlers;

    public Server(int port)
    {
        this.port = port;
        this.clientHandlers = new ArrayList<ClientHandler>();

        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run()
    {
        ExecutorService service = Executors.newCachedThreadPool();

        try
        {
            while(true)
            {
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                if (clientSocket != null)
                {
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    service.execute(clientHandler);
                    clientHandlers.add(clientHandler);
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
