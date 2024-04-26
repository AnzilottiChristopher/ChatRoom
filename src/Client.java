import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable
{
    //TCP Data
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    //Client Data
    private String userName;

    public Client(String userName, String ip)
    {
        this.userName = userName;
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
        try
        {
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
}
