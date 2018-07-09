import java.util.*;

public class Main
{
	public static void main(String[] args)
	{
		try
		{
			Requests request = new Requests();
			RequestsQueue requestsQueue;
			Dispatcher dispatcher = new Dispatcher();
			Floor floor = new Floor();
			Elevator elevator = new Elevator();

			requestsQueue = request.readin();
			dispatcher.run(requestsQueue, floor, elevator);
		}
		catch (Exception E)
		{
			System.out.println("ERROR");
			System.out.println("# Unknown ERROR");
		}
	}
}
