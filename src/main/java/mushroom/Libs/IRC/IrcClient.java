package mushroom.Libs.IRC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrcClient {
    public class IRCMessage {
        public String nick;
        public String tag;
        public String host;
        public String message;
    }

    private Thread t = null;

    private final String host;
    private final int port;
    private final String nick;
    private final String channel;
    private final Runnable onConnect;
    private final Runnable onDisconnect;
    private final Runnable onJoin;
    private final Consumer<IRCMessage> onMessage;
    private final Consumer<String> onLine;
    private final Consumer<Exception> onException;

    private Socket socket;
    private BufferedWriter writer;
    private boolean shouldKill;

    private final Pattern onMessagePattern;

    public IrcClient(String host, int port, String nick, String channel, Runnable onConnect, Runnable onDisconnect, Runnable onJoin, Consumer<IRCMessage> onMessage, Consumer<String> onLine, Consumer<Exception> onException) {
        this.host = host;
        this.port = port;
        this.nick = nick;
        this.channel = channel;
        this.onConnect = onConnect;
        this.onDisconnect = onDisconnect;
        this.onJoin = onJoin;
        this.onMessage = onMessage;
        this.onLine = onLine;
        this.onException = onException;

        onMessagePattern = Pattern.compile("^:(.+?)!(.+?)@(.+?) PRIVMSG " + channel + " :(.+?)$");
    }

    private ConcurrentLinkedQueue<String> writeQueue = new ConcurrentLinkedQueue<>();

    public void start() {
        shouldKill = false;
        writeQueue.clear();
        t = new Thread(() -> {
            try {
                // Connect directly to the IRC server.
                socket = new Socket(host, port);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Log on to the server.
                writer.write("NICK " + nick + "\r\n");
                writer.write("USER " + nick + " 0 * :" + nick + "\r\n");
                writer.flush();

                // Read lines from the server until it tells us we have connected.
                String line;
                while(!shouldKill && (line = reader.readLine()) != null) {
                    if (line.toLowerCase().startsWith("ping ")) {
                        // We must respond to PINGs to avoid being disconnected.
                        writer.write("PONG " + line.substring(5) + "\r\n");
                        writer.flush();
                    }else if(line.contains("004")) {
                        onConnect.run();
                        break;
                    }else if(line.contains("433")) {
                        throw new Exception("Nick is already in use");
                    }
                }

                // Join the channel.
                writer.write("JOIN " + channel + "\r\n");
                writer.flush();

                // Keep reading lines from the server.
                while(!shouldKill) {
                    if(reader.ready() && (line = reader.readLine()) != null) {
                        if (line.toLowerCase().startsWith("ping ")) {
                            // We must respond to PINGs to avoid being disconnected.
                            writer.write("PONG " + line.substring(5) + "\r\n");
                            writer.flush();
                        }else if(line.startsWith(":" + nick + "!~" + nick + "@") && line.endsWith(" JOIN :" + channel)) {
                            onJoin.run();
                        }else {
                            Matcher m = onMessagePattern.matcher(line);
                            if(m.matches()) {
                                IRCMessage ms = new IRCMessage();
                                ms.nick = m.group(1);
                                ms.tag = m.group(2);
                                ms.host = m.group(3);
                                ms.message = m.group(4);
                                onMessage.accept(ms);
                            }else {
                                onLine.accept(line);
                            }
                        }
                    }else {
                        boolean isEmpty = writeQueue.isEmpty();
                        while(!writeQueue.isEmpty()) {
                            String l = writeQueue.poll();
                            writer.write(l);
                        }
                        if(!isEmpty) writer.flush();
                    }
                }

                onDisconnect.run();
            } catch (Exception ex) {
                onException.accept(ex);
            }
        });
        t.start();
    }

    // TODO Disconnecting works POORLY
    public void stop() {
        if(t != null) {
            try {
                write("QUIT");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            shouldKill = true;
            t.stop();
            t = null;
        }
    }

    public void write(String line) {
        writeQueue.add(line + "\r\n");
    }

    public void sendMessage(String message) {
        write("PRIVMSG " + channel + " :" + message);
    }
}