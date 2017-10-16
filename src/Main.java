import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1337);
            System.out.println("Server wurde erfolgreich gestartet...");
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                AppointmentCalendar agent1 = (AppointmentCalendar) objectInputStream.readObject();
                AppointmentCalendar agent2 = (AppointmentCalendar) objectInputStream.readObject();
                int time = scanner.nextInt();
                ArrayList<int[]> available = available(agent1, agent2, time);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(available);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    static ArrayList<int[]> available(AppointmentCalendar agent1, AppointmentCalendar agent2, int time) {
        ArrayList<int[]> available = new ArrayList<>();
        for (int i = 0; i < agent1.free.length; i++) {
            for (int j = 0; j < agent1.free[0].length; j++) {
                if (j + time <= 10) {
                    boolean isAvailable = true;
                    for (int k = j; k < j + time; k++) {
                        if (!agent1.free[i][k] || !agent2.free[i][k]) {
                            isAvailable = false;
                            break;
                        }
                    }
                    if (isAvailable) {
                        int[] temp = {i, j};
                        available.add(temp);
                    }
                }
            }
        }
        return available;
    }
}
