import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SimpleMqttClient implements MqttCallback {

    MqttClient myClient;
    MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://mpd.lan:1883";
    static final String M2MIO_DOMAIN = "laumio";
    static final String M2MIO_STUFF = "Laumio_D454DB";
    static final String M2MIO_THING = "json";
    static final String M2MIO_USERNAME = "";
    static final String M2MIO_PASSWORD_MD5 = "";

    // the following two flags control whether this example is a publisher, a subscriber or both
    static final Boolean subscriber = true;
    static final Boolean publisher = true;

    /**
     *
     * connectionLost
     * This callback is invoked upon losing the MQTT connection.
     *
     */
    @Override
    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!");
        // code to reconnect to the broker would go here if desired
    }

    /**
     *
     * deliveryComplete
     * This callback is invoked when a message published by this client
     * is successfully received by the broker.
     *
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    public void deliveryComplete(MqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    /**
     *
     * messageArrived
     * This callback is invoked when a message is received on a subscribed topic.
     *
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
    }


    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic.getName());
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");

    }

    // publish message
    public void sendMsg(MqttTopic topic, String message){

        int pubQoS = 0;
        MqttMessage mqtt_msg = new MqttMessage(message.getBytes());
        mqtt_msg.setQos(pubQoS);
        mqtt_msg.setRetained(false);

        // Publish the message
        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(mqtt_msg);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * lightOff
     * The main functionnality is to light off a lamp
     */

    public void lightOff(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[0,0,0]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     *
     * fill
     * The main functionnality is to change the color of the lamp
     *
     * @param client
     * @param lampe
     * @param r
     * @param g
     * @param b
     */

    public void fill(MqttClient client, String lampe, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * lightUp
     * The main functionnality is to light up a lamp
     */
    public void lightUp(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[123,123,123]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * setRing
     * The main functionnality is to change a ring's color
     */
    public void setRing(MqttClient client, String lampe,int idRing, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        if(idRing<0 || idRing>2)
        {
            idRing = 0;
        }
        String myMessage = "{\'command\' : \'set_ring\', \'led\' : "+idRing +"\'rgb\' :["+r+","+g+","+b+"]}";

        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * colorWipe
     * The main functionnality is to start a animation of progressive change of color for a defined time
     */
    public void colorWipe(MqttClient client, String lampe, int r, int g, int b,int duration){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_ring\', \'duration\' : "+duration +"\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }


    /**
     * setPixel
     * The main functionnality is to set a pixel of a lamp
     */
    public void setPixel(MqttClient client, String lampe, int pixelId, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_pixel\', \'led\':"+pixelId+",\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * setColumn
     * The main functionnality is to set a ring of a lamp
     */
    public void setColumn(MqttClient client, String lampe, int ringId, int r, int g, int b){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'set_column\', \'ring\':"+ringId+",\'rgb\' :["+r+","+g+","+b+"]}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    /**
     * animateRainbow
     * The main functionnality is to set a raimbow animation
     */
    public void animateRainbow(MqttClient client, String lampe){
        String str_topic = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'animate_rainbow\'}";
        MqttTopic topic = client.getTopic(str_topic);
        // Publish the message
        sendMsg(topic, myMessage);
    }

    public void animation(MqttClient client, String lampe)
    {


        try {
            this.lightOff(client, lampe);
            sleep(1000);
            this.fill(client, lampe, 255, 0,0);
            sleep(1000);
            this.fill(client, lampe, 0, 255,0);
            sleep(1000);
            this.fill(client, lampe, 0, 0,255);
            sleep(1000);
            this.animateRainbow(client, lampe);
            sleep(1000);
            this.setRing(client, lampe, 0,255,255, 255);
            sleep(1000);
            this.setRing(client, lampe, 1,255,255, 255);
            sleep(1000);
            this.setRing(client, lampe, 2,255,255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 0, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 1, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 2, 255, 255, 255);
            sleep(1000);
            this.setColumn(client, lampe, 3, 255, 255, 255);
            sleep(1000);
            this.animateRainbow(client, lampe);
            sleep(1000);
            this.fill(client, lampe, 255, 0,0);
            sleep(1000);
            this.fill(client, lampe, 0, 255,0);
            sleep(1000);
            this.fill(client, lampe, 0, 0,255);
            sleep(1000);
            this.lightOff(client, lampe);
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * MAIN
     *
     */
    public static void main(String[] args) {
        SimpleMqttClient smc = new SimpleMqttClient();
        smc.runClient();
    }

    /**
     *
     * runClient
     * The main functionality of this simple example.
     * Create a MQTT client, connect to broker, pub/sub, disconnect.
     *
     */
    public void runClient() {
        // setup MQTT Client
        String clientID = M2MIO_THING;
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        //connOpt.setUserName(M2MIO_USERNAME);
        //connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        // Connect to Broker
        try {
            myClient = new MqttClient(BROKER_URL, clientID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Connected to " + BROKER_URL);

        // setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>

        /*String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        String myTopic2 = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + "fill 0 0 255";
        MqttTopic topic = myClient.getTopic(myTopic);

        // subscribe to topic if subscriber
        if (subscriber) {
            try {
                int subQoS = 0;
                myClient.subscribe(myTopic, subQoS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        


        this.animation(myClient, "Laumio_D454DB");

        // disconnect
        try {
            // wait to ensure subscribed messages are delivered
            if (subscriber) {
                sleep(5000);
            }
            myClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
