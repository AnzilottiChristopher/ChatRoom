import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable
{
    //TCP Data
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    //Client Data
    private String userName;
    private boolean sentName;

    public Client(String userName, String ip)
    {
        this.userName = userName;
        sentName = false;
        try
        {
            socket = new Socket(ip, 5000);
            System.out.println("Connected");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (UnknownHostException e)
        {
            System.out.println("Failed Connection");
        } catch (IOException e)
        {
            System.out.println("Failed Connection");
        }
    }

    public void sendMessage(String messageOut)
    {
        if (!sentName)
        {
            try
            {
                out.writeUTF(userName);
                out.flush();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            sentName = true;
        }
        try
        {
            //System.out.println("In send message!");
            out.writeUTF(userName + ": " + messageOut);
            out.flush();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String messageIn = in.readUTF();
                System.out.println(messageIn);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter a username");
        Client client = new Client(sc.nextLine(), "LocalHost");
        client.sendMessage(client.userName);

        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(client);

        String messageOut;

        while (true)
        {
            messageOut = sc.nextLine();
            client.sendMessage(messageOut);
        }

    }
}
