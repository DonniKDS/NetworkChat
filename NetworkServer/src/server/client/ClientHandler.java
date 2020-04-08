package server.client;

import client.Command;
import client.CommandType;
import client.command.AuthCommand;
import client.command.BroadcastMessageCommand;
import client.command.PrivateMessageCommand;
import server.NetworkServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {

    private final NetworkServer networkServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
    }

    public void run() {
        doHandle(clientSocket);
    }

    private void doHandle(Socket socket) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());


            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    System.out.println("Соединение с клиентом " + nickname + " было закрыто!");
                } finally {
                    closeConnection();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            networkServer.unsubscribe(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    System.out.println("Received 'END' command");
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommand commandData = (PrivateMessageCommand) command.getData();
                    String receiver = commandData.getReceiver();
                    String message = commandData.getMessage();
                    networkServer.sendMessage(receiver, Command.messageCommand(nickname, message));
                    break;
                }
                case BROADCAST_MESSAGE: {
                    BroadcastMessageCommand commandData = (BroadcastMessageCommand) command.getData();
                    String message = commandData.getMessage();
                    networkServer.broadcastMessage(Command.messageCommand(nickname, message), this);
                    break;
                }
                default:
                    System.err.println("Unknown type of command : " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void authentication() throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            try {
                Thread.sleep(120000);
                clientSocket.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                executorService.shutdown();
                boolean successfulAuth = processAuthCommand(command);
                if (successfulAuth){
                    return;
                }
            } else {
                System.err.println("Unknown type of command for auth process: " + command.getType());
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        String login = commandData.getLogin();
        String password = commandData.getPassword();
        String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        if (username == null) {
            Command authErrorCommand = Command.authErrorCommand("Отсутствует учетная запись по данному логину и паролю!");
            sendMessage(authErrorCommand);
            return false;
        }
        else if (networkServer.isNicknameBusy(username)) {
            Command authErrorCommand = Command.authErrorCommand("Данный пользователь уже авторизован!");
            sendMessage(authErrorCommand);
            return false;
        }
        else {
            nickname = username;
            String message = nickname + " зашел в чат!";
            networkServer.broadcastMessage(Command.messageCommand(null, message), this);
            commandData.setUsername(nickname);
            sendMessage(command);
            networkServer.subscribe(this);
            return true;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    public String getNickname() {
        return nickname;
    }
}
