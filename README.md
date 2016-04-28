# Routing Rabbit
Routing! RabbitMQ Java

In the previous tutorial we built a simple logging system. We were able to broadcast log messages to many receivers.

In this tutorial we're going to add a feature to it - we're going to make it possible to subscribe only to a subset of the messages. For example, we will be able to direct only critical error messages to the log file (to save disk space), while still being able to print all of the log messages on the console.


Putting it all together

<<<<<<< HEAD
* You can compile both of these with just the RabbitMQ java client and receiver on the classpath:

 `$ javac -cp rabbitmq-client.jar EmitLogDirect.java `
=======
* You can compile both of these with just the RabbitMQ java sender and receiver on the classpath

Sender
 `$ javac -cp rabbitmq-client.jar EmitLogDirect.java `
Receive
>>>>>>> 0b4a0c693da54b66a6c5e3a6535a39bc9627e0ea
 `$ javac -cp rabbitmq-client.jar ReceiveLogsDirect.java`

* To run them, you'll need rabbitmq-client.jar and its dependencies on the classpath. In a terminal, run the sender:


 `java -cp .:commons-io-1.2.jar:commons-cli-1.1.jar:rabbitmq-client.jar EmitLogDirect`

* then, run the receiver:


 `java -cp .:commons-io-1.2.jar:commons-cli-1.1.jar:rabbitmq-client.jar ReceiveLogsDirect`

Nota: Incluir el identificador en ambos casos al momento de ejecutar el sender y el receiver para identificar la cola.

