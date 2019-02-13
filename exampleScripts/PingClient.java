import edu.umass.cs.msocket.FlowPath;
import edu.umass.cs.msocket.MSocket;
import edu.umass.cs.msocket.mobility.MobilityManagerClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Random;

public class PingClient {


    private static final int    LOCAL_PORT = 5454;
    private static final String LOCALHOST  = "127.0.0.1";

    private static final int TOTAL_ROUND = 1000;

    private static final Random rand = new Random();


    public static void main(String[] args) {
        String serverIPOrName = null;
        int numRound = TOTAL_ROUND;

        if (args.length == 0)
            serverIPOrName = LOCALHOST;
        else
            serverIPOrName = args[0];
        int serverPort = LOCAL_PORT;

        if (args.length == 1) {
            serverIPOrName = args[0];
        }

        if (args.length == 2) {
            serverIPOrName = args[0];
            numRound = Integer.parseInt(args[1]);
        }

        try {
            MSocket ms = new MSocket(InetAddress.getByName(serverIPOrName), serverPort);

            OutputStream os = ms.getOutputStream();
            InputStream is = ms.getInputStream();

            // wait for 2 seconds for all connections
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int rd = 0;

            for (int i = 0; i < ms.getActiveFlowPaths().size(); i++) {
                FlowPath currfp = ms.getActiveFlowPaths().get(i);

                System.out.println("Flowpath id=" + currfp.getFlowPathId() + " local ip=" + currfp.getLocalEndpoint().toString());
            }

            while (rd < numRound) {

                int numRead = 10 * 1024;
                System.out.println("[Client:] To read " + numRead + " bytes data from input stream...");


                byte[] b = new byte[numRead];

                ByteBuffer dbuf = ByteBuffer.allocate(4);
                dbuf.putInt(numRead);
                byte[] bytes = dbuf.array();


                int totalRead = 0;
                int cnt = 0;

                long start = System.currentTimeMillis();

                os.write(bytes);
                do {
                    numRead = is.read(b);
                    if (numRead >= 0)
                        totalRead += numRead;
                    ++cnt;
                } while (totalRead < numRead && cnt < 100);

                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[Latency:] " + elapsed  + " ms");

                rd++;
            }

            os.write(-1);
            os.flush();

            ms.close();
            System.out.println("Socket closed");
            MobilityManagerClient.shutdownMobilityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
