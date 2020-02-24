package frc.lib.drivers;
import edu.wpi.first.wpiutil.net.PortForwarder;
/**
 * The purpose of this class is to enumerate all of the external devices that need to be port forwarded.
 *d
 * Documentation: https://docs.wpilib.org/en/latest/docs/networking/networking-utilities/portforwarding.html
 */public enum EForwardableConnections {
    /**
     * The visual feed, used to display what the camera sees
     */
    LIMELIGHT_CAMERA_FEED("10.38.47.11", 5800),
    /**
     *
     * The web URL view. Note that the {@link EForwardableConnections#LIMELIGHT_CAMERA_FEED} must be forwarded as well for
     * the camera to show up in this view.
     */
    LIMELIGHT_WEB_VIEW("10.38.47.11", 5801);
    private EForwardableConnections(String pIPAddress, int pPort) {
        mpIPAddress = pIPAddress;
        mPort = pPort;
    }
    private final int mPort;
    private final String mpIPAddress;
    public int getPort() {
        return mPort;
    }
    public String gepIPAddress() {
        return mpIPAddress;
    }
    /**
     * Helper method to
     * @param pExternalConnection
     */
    public static void addPortForwarding(EForwardableConnections pExternalConnection) {
        PortForwarder.add(pExternalConnection.getPort(),pExternalConnection.gepIPAddress(), pExternalConnection.getPort());
    }
}