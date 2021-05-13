package mqttController;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medcarapp.ManualControl;
import com.example.medcarapp.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CarConnect extends AppCompatActivity{
    private static final String TAG = "SmartcarMqttController";
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final String CAMERA_TOPIC = "Camera_Stream";
    private static final int QOS = 0;
    private static final int IMAGE_WIDTH = 160;
    private static final int IMAGE_HEIGHT = 120;
    private static final String SUCCESSFUL_CONNECTION = "Connected to MQTT broker";
    private static final String FAILED_CONNECTION = "Failed to connect to MQTT broker";
    private static final String LOST_CONNECTION = "Connection to MQTT broker lost";
    private static final String EXTERNAL_MQTT_BROKER = "tcp://3.138.188.190:1883";
    //private static final String LOCALHOST = "10.0.2.2";
    // private static String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
    private static String MQTT_SERVER = "tcp://10.0.2.2:1883";



    Context context;

    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private ImageView mCameraView;

    public CarConnect(Context context, ImageView mCameraView) {
        this.context = context;
        mMqttClient = new MqttClient(context, MQTT_SERVER, TAG);
        this.mCameraView = mCameraView;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    public void connectToMqttBroker(TextView connectionText) {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    Log.i(TAG, SUCCESSFUL_CONNECTION);
                    feedbackMessage(SUCCESSFUL_CONNECTION);
                    connectionText.setText("Connected");
                    connectionText.setTextColor(Color.parseColor("#32CD32"));

                    //mMqttClient.subscribe(TURNING_TOPIC, QOS, null);
                    //mMqttClient.subscribe(SPEED_TOPIC, QOS, null);
                    mMqttClient.subscribe(CAMERA_TOPIC, QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    mCameraView.setImageResource(R.drawable.intermission);

                    Log.e(TAG, FAILED_CONNECTION);
                    feedbackMessage(FAILED_CONNECTION);

                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {



                    mCameraView.setImageResource(R.drawable.intermission);

                    Log.w(TAG, LOST_CONNECTION);
                    feedbackMessage(LOST_CONNECTION);
                  
                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(CAMERA_TOPIC)) {
                        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                        final byte[] payload = message.getPayload();
                        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                        for (int ci = 0; ci < colors.length; ++ci) {
                            final byte r = payload[3 * ci];
                            final byte g = payload[3 * ci + 1];
                            final byte b = payload[3 * ci + 2];
                            colors[ci] = Color.rgb(r, g, b);
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

                        mCameraView.setImageBitmap(bm);
                    } else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void disconnect(IMqttActionListener disconnectionCallback) {
        mMqttClient.disconnect(disconnectionCallback);
    }

    public void subscribe(String topic, int qos, IMqttActionListener subscriptionCallback) {
        mMqttClient.subscribe(topic, qos, subscriptionCallback);
    }

    public void unsubscribe(String topic, IMqttActionListener unsubscriptionCallback) {
        mMqttClient.unsubscribe(topic, unsubscriptionCallback);
    }

   public void publish(String topic, String message, int qos, IMqttActionListener publishCallback){
        mMqttClient.publish(topic, message, qos, publishCallback);
    }


    public void feedbackMessage(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    public static void setMQTTServer(){
        MQTT_SERVER = EXTERNAL_MQTT_BROKER;
    }

}