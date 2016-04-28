import com.rabbitmq.client.*;
import java.io.IOException;

public class EmitLogDirect2 extends Thread implements Runnable{

  private static final String EXCHANGE_NAME = "direct_logs";
  private static ConnectionFactory factory;
  private static Connection connection;
  private static Channel channel;
  private static String queueName;
  private static EmitLogDirect2 INSTANCE = new EmitLogDirect2();

  @Override
  public void run(){
    int i = 0;
    if (i == 0){
      try{
        Thread.sleep(2000);
        enviaM("B","HolaD");
      }
      catch(Exception e){}
    }
    
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      }
    };

    try{
      factory = new ConnectionFactory();
      factory.setHost("localhost");
      connection = factory.newConnection();
      channel = connection.createChannel();
      channel.exchangeDeclare(EXCHANGE_NAME, "direct");
      queueName = channel.queueDeclare().getQueue();
      channel.queueBind(queueName, EXCHANGE_NAME, "BB");
      
      channel.basicConsume(queueName, true, consumer);

    }
    catch(Exception e){}
    
  }
  
  public static void enviaM (String id, String message) throws Exception {
    factory = new ConnectionFactory();
    factory.setHost("localhost");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "BB");

    channel.basicPublish(EXCHANGE_NAME, id, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + id + "':'" + message + "'");

    channel.close();
    connection.close();
  }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (EmitLogDirect2.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EmitLogDirect2();
                }
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that 
     * only one instance of this class is created. Singleton design pattern.
     * 
     * @return The instance of this class.
     */
    public static EmitLogDirect2 getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * Start this controller
     * 
     * @param args IP address of the event manager (on command line). 
     * If blank, it is assumed that the event manager is on the local machine.
     */
    public static void main(String args[]) {
        if(args[0] != null)
        EmitLogDirect2 sensor = EmitLogDirect2.getInstance();
        sensor.run();
    }

}