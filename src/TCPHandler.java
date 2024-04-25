import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPHandler implements Runnable
{
    //TCP Data
    private Socket socket;
    private DataInputStream in = null;
    private DataOutputStream out = null;


    public TCPHandler(Socket socket)
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

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                String message = in.readUTF();
                System.out.println(message);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
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
}
