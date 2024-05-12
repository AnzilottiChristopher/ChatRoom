import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ClientHandler implements Runnable
{
    //TCP Data
    private Socket socket;
    private DataInputStream in = null;
    private DataOutputStream out = null;


    //Data
    private static Queue<String> queue = new LinkedList<>();
    private String userName;
    private boolean sentName;


    public ClientHandler(Socket socket)
    {
        this.socket = socket;
        sentName = false;
        try
        {
            in = new DataInputStream(this.socket.getInputStream());
            out = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //Queue Manipulation
    public synchronized void enqueue(String messageIn)
    {
        queue.add(messageIn);
    }
    public synchronized boolean peek()
    {
        if (queue.peek() != null)
        {
            return true;
        }
        return false;
        //queue.peek();
    }
    public synchronized void dequeue()
    {
        queue.remove();
    }

    public String getMessage()
    {
        return queue.peek();
    }

    //Username
    public synchronized String getUserName()
    {
        return userName;
    }

    public void sendMessage(String outMessage)
    {
        try
        {
            out.writeUTF(outMessage);
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
                if (!sentName)
                {
                    userName = in.readUTF();
                    sentName = true;
                    //System.out.println(userName);
                    System.out.println(sentName);
                }
                else if(sentName)
                {
                    String message = in.readUTF();
                    //System.out.println("Message received: " + message);
                    enqueue(message);
                    //System.out.println(message);
                }

            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
