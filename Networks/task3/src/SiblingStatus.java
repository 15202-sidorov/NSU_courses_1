import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;

//maybe make queue here for all messages

public class SiblingStatus {
    public SiblingStatus( InetSocketAddress inputAddress) {
        address = inputAddress;
        aliveStatus = 0;
        waitForAckStatus = 0;
        packetQueue = new SynchronousQueue<DatagramPacket>();
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void noPing() {
        aliveStatus++;
    }

    public void noAck() {
        waitForAckStatus++;
    }

    public void gotPing() {
        aliveStatus = 0;
    }

    public void gotAck() {
        waitForAckStatus = 0;
    }

    public boolean getPingStatus() {
        return aliveStatus < CRITICAL_NO_PING_VALUE;
    }

    public boolean getAckStatus() {
        return waitForAckStatus < CRITICAL_NO_ACK_VALUE;
    }

    public boolean isAvailable() {
        return ((aliveStatus == 0) && (waitForAckStatus == 0));
    }

    public void pushToPacketQueue( DatagramPacket packet ) throws InterruptedException {
        packetQueue.put(packet);
    }

    public DatagramPacket pullFromPacketQueue() throws InterruptedException {
        return packetQueue.take();
    }

    public boolean packetQueueIsEmpty() {
        return packetQueue.isEmpty();
    }

    private InetSocketAddress address;
    private Integer aliveStatus;
    private Integer waitForAckStatus;
    private SynchronousQueue<DatagramPacket> packetQueue;

    private final static Integer CRITICAL_NO_ACK_VALUE = 3;
    private final static Integer CRITICAL_NO_PING_VALUE = 3;
}
