package mqttController;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class CarConnect extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String EXTERNAL_MQTT_BROKER = "3.138.188.190";
    private static final String LOCALHOST = "3.138.188.190";
    private static final String MQTT_SERVER = "tcp://" + EXTERNAL_MQTT_BROKER + ":1883";
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final String SUCCESSFUL_CONNECTION = "Connected to MQTT broker";
    private static final String FAILED_CONNECTION = "Failed to connect to MQTT broker";
    private static final String LOST_CONNECTION = "Connection to MQTT broker lost";
    private static final int QOS = 1;

    Context context;

    private MqttClient mMqttClient;
    private boolean isConnected = false;

    public CarConnect(Context context) {
        this.context = context;
        mMqttClient = new MqttClient(context, MQTT_SERVER, TAG);
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

                    mMqttClient.subscribe(TURNING_TOPIC, QOS, null);
                    mMqttClient.subscribe(SPEED_TOPIC, QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, FAILED_CONNECTION);
                    feedbackMessage(FAILED_CONNECTION);
                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;
                    Log.w(TAG, LOST_CONNECTION);
                    feedbackMessage(LOST_CONNECTION);
                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
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

}