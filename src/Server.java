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
    private int numClients;


    //Client information
    private List<ClientHandler> clientHandlers;

    public Server(int port)
    {
        this.port = port;
        this.clientHandlers = new ArrayList<ClientHandler>();
        numClients = 0;

        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public synchronized boolean isEmpty()
    {
        if (clientHandlers.isEmpty())
        {
            //System.out.println("Server is empty");
            return false;
        }
        else return true;
    }

    public synchronized void addClient(ClientHandler clientHandler)
    {
        clientHandlers.add(clientHandler);
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
                    //System.out.println("In client not null");
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    service.execute(clientHandler);
                    //clientHandlers.add(clientHandler);
                    addClient(clientHandler);
                    numClients++;
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args)
    {
        Server server = new Server(5000);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(server);

        while(true)
        {
            if (server.isEmpty())
            {
                //System.out.println("Here are the client handlers");
                if (server.clientHandlers.get(0).peek())
                {
                    for(ClientHandler clientHandler : server.clientHandlers)
                    {
                        //System.out.println("Here we are");
//                        if(!clientHandler.getMessage().split(":")[0].equals(clientHandler.getUserName()))
//                        {
//                            //System.out.println(clientHandler.getUserName().split(":")[0]);
//                            clientHandler.sendMessage(clientHandler.getMessage());
//                        }

                        //Unsure if I like only one person's message popping up or both sender and receiver.
                        clientHandler.sendMessage(clientHandler.getMessage());
                    }
                    server.clientHandlers.get(0).dequeue();
                }
            }
        }
    }
}
