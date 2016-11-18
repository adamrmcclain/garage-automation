package garage.lights;
import java.io.IOException;
import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

/**
 * Class that discovers all bluetooth devices in the neighbourhood
 * and displays their name and bluetooth address.
 */
public class BlueToothDiscovery implements DiscoveryListener {

    //object used for waiting
    private static Object lock = new Object();

    //vector containing the devices discovered
    private static Vector vecDevices = new Vector();

    private static String connectionURL;

    //main method of the application
    public static void getBluetoothList() throws IOException {

//create an instance of this class
        BlueToothDiscovery bluetoothDeviceDiscovery = new BlueToothDiscovery();

//display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

//find devices
        DiscoveryAgent agent = localDevice.getDiscoveryAgent();

        System.out.println("Starting device inquiry…");
        agent.startInquiry(DiscoveryAgent.GIAC, bluetoothDeviceDiscovery);

        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Device Inquiry Completed. ");

//print all devices in vecDevices
        int deviceCount = vecDevices.size();

        if (deviceCount <= 0) {
            System.out.println("No Devices Found .");
        } else {
            //print bluetooth device addresses and names in the format [ No. address (name) ]
            System.out.println("Bluetooth Devices: ");
            for (int i = 0; i < deviceCount; i++) {
                RemoteDevice remoteDevice = (RemoteDevice) vecDevices.elementAt(i);
                System.out.println((i + 1) + ". "
                        + remoteDevice.getBluetoothAddress() +
                        " (" + remoteDevice.getFriendlyName(true) + ")");


            }
        }
    }

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        if(!vecDevices.contains(btDevice)){
            vecDevices.addElement(btDevice);
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        if(servRecord!=null && servRecord.length>0){
            connectionURL = servRecord[0].getConnectionURL(0,false);
        }
        synchronized(lock){
            lock.notify();
        }
    }

    @Override
    public void serviceSearchCompleted(int transID, int respCode) {
        synchronized(lock){
            lock.notify();
        }
    }

    @Override
    public void inquiryCompleted(int discType) {
        synchronized(lock){
            lock.notify();
        }
    }
}