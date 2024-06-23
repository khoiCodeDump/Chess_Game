# Chess_Game
Local Multiplayer chess game. The chess game follows peer to peer architecture and is a live game .

## Set up & Installation

Make sure you have Java SE 17 & JDK 17 installed. I am using eclipse to launch the project. Note this is a maven project so the dependencies may need to be installed in your machine.
```bash
git clone <repo-url>
```
Install the required maven dependency:
```bash
<dependency>
  		<groupId>org.apache.xmlgraphics</groupId>
  		<artifactId>batik-all</artifactId>
  		<version>1.17</version>
</dependency>
```
## Running the application
You can either run this in the command line or eclipse but I recommend running it in eclipse. From eclipse, you can get the command to run it in the command line in Run Configurations.
Follow the steps below to run the app:
1. Open the Client.java and Server.java. You can configure which port to send to in Client.java and to receive in Server.java.
2. First press play button with the Server.java file in focus. Playing this will make you the server.
3. Then press play button with the Client.java file in focus. 

https://github.com/khoiCodeDump/Chess_Game/assets/46497248/6c6a0acd-9887-4dd3-8527-25b8bd0af84e

