package ga.adriwalter.smartvending.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluethoothConnection {

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private AppCompatActivity mAppCompatActivity;
    private String mId;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    protected static String BluethoothMacAddress = Credential.BluethoothMacAddress; // Bluethooth Mac Address

    public BluethoothConnection(AppCompatActivity activity, String id) {
        mAppCompatActivity = activity;
        mId = id;
    }

    public void blueTooth() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if (myBluetooth == null) {
            // Show a message. that the device has no bluetooth adapter
            Toast.makeText(mAppCompatActivity, "Bluetooth is not available on this device", Toast.LENGTH_LONG).show();
        } else if (!myBluetooth.isEnabled()) {
            // Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mAppCompatActivity.startActivityForResult(turnBTon, 1);
        }

        Toast.makeText(mAppCompatActivity, "... Client connect Smart Vending...", Toast.LENGTH_LONG).show();

        // Set up a pointer to the remote node using it's BluethoothMacAddress.
        BluetoothDevice device = myBluetooth.getRemoteDevice(BluethoothMacAddress);

        // Two things are needed to make a connection:
        //  A MAC BluethoothMacAddress, which we got above.
        //  A Service ID or UUID.  In this case we are using the
        //  UUID for SPP.
        try {
            btSocket = device.createRfcommSocketToServiceRecord(myUUID);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        myBluetooth.cancelDiscovery();

        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Toast.makeText(mAppCompatActivity, "Fatal Error\", \"In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".", Toast.LENGTH_LONG).show();
            }
        }

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }

        turnOnRelay();

        // on Pause
        Toast.makeText(mAppCompatActivity, "...Succesfully...", Toast.LENGTH_LONG).show();

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    // Turn relay on
    private void turnOnRelay() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(mId.getBytes());
            } catch (IOException e) {
                msg("Did not connect with Vending Machine");
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast msg = Toast.makeText(mAppCompatActivity,
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
    }

    // fast way to call Toast
    private void msg(String s) {
        Toast.makeText(mAppCompatActivity, s, Toast.LENGTH_LONG).show();
    }
}