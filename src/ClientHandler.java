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


    public ClientHandler(Socket socket)
    {
        this.socket = socket;
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
    public void checkQueue()
    {
        queue.peek();
    }

    public synchronized void dequeue()
    {
        queue.remove();
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
                String message = in.readUTF();
                //System.out.println(message);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
