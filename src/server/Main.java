package server;

public class Main {

    public static void main(String[] args) {

        if (args.length == 2) {

			// variable initialization system to port to use and maximum number of clients
            int portNumber = 0;
            int maxClients = 0;

            try {
				//convert input string to integer and assign to port
                portNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Port number must be an integer.");
                System.exit(1);
            }

            try {
				//convert input string to integer and assign to maxClients
                maxClients = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Maximum number of clients must be an integer.");
                System.exit(1);
            }

			//instantiate server objects using specified port and maxClients
            Server mainServer = new Server(portNumber, maxClients);
            mainServer.runServer();
        } else {
            System.err.println("Usage: <port number> <max clients>");
            System.exit(1);
        }
    }
}
