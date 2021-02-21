import edu.umass.cs.msocket.FlowPath;
import edu.umass.cs.msocket.MSocket;
import edu.umass.cs.msocket.mobility.MobilityManagerClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class PingClient {


    private static final int    LOCAL_PORT = 5454;
    private static final String LOCALHOST  = "127.0.0.1";

    private static DecimalFormat df = new DecimalFormat("0.00##");

    private static final int TOTAL_ROUND = 1000;

    private static int numOfBytes = 10*1024;

    public static void main(String[] args) {
        String serverIPOrName = null;
        int numRound = TOTAL_ROUND;

        if (args.length == 0)
            serverIPOrName = LOCALHOST;
        else
            serverIPOrName = args[0];

        int serverPort = LOCAL_PORT;

        if (System.getProperty("total") != null) {
            numOfBytes = Integer.parseInt(System.getProperty("total"));
        }

        if (System.getProperty("round") != null) {
            numRound = Integer.parseInt(System.getProperty("round"));
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

                int numSent = numOfBytes;
                System.out.println("[Client:] To read " + numSent + " bytes data from input stream...");


                byte[] b = new byte[numSent];

                ByteBuffer dbuf = ByteBuffer.allocate(4);
                dbuf.putInt(numSent);
                byte[] bytes = dbuf.array();

                int numRead;
                int totalRead = 0;

                long start = System.currentTimeMillis();

                os.write(bytes);
                do {
                    numRead = is.read(b);
                    if (numRead >= 0)
                        totalRead += numRead;

                } while (totalRead < numSent);

                long elapsed = System.currentTimeMillis() - start;
                System.out.println("[Latency:] " + elapsed  + " ms");
                System.out.println("[Thruput:] " + df.format(numOfBytes/1000.0/elapsed ) + " KB/s");

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
