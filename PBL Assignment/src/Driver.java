import java.util.Scanner;
import java.util.ArrayList;

public class Driver 
{
	private Scanner input;
	private Bank bank;
	private ArrayList<Player> players;
	private ArrayList<Player> highScore;
	
	
	public Driver() throws Exception {
		bank = new Bank();
		input = new Scanner(System.in);
		runMenu();		
	}
	
	public static void main (String args[]) throws Exception
	{
		Driver app = new Driver();
		
	}
	
	private int mainMenu() {
		System.out.println(" ___________________________________________\n" + 
                "|                                           |\n" + 
                "|  Welcome to the Shares and Stocks!        |\n" +
                "|___________________________________________|\n");

		System.out.println("What would you like to do?");
		System.out.println("  1) Start game");
		System.out.println("  2) Previous game final score");
		System.out.println("  0) Exit");
		System.out.print("==>> ");
		int option = input.nextInt();
		return option;
	}



	private void runMenu() throws Exception {
		int option = mainMenu();
		while (option != 0) {
			// clear the terminal window
			System.out.println("\f");

			switch (option) {
			case 1:
				addPlayer();
				break;
			case 2:
				lastScore();
				break;

			default:
				System.out.println("Invalid option entered: " + option);
				break;
			}

			
			System.out.println("\nPress any key to continue...");
			input.nextLine();
			input.nextLine(); 

			
			System.out.println("\f");
			option = mainMenu();
		}
		
		System.out.println("Exiting... bye");
		System.exit(0);

	}
	
	private int roundMenu()
	{
		System.out.println("  1) Buy shares");
		System.out.println("  2) Sell shares");
		System.out.println("  3) Check your current balance and stocks");
		System.out.println("  4) Check stock prices and available stocks");
		System.out.println("  5) Check your card for this round");
		System.out.println("  6) Take loan");
		System.out.println("  7) Repay loan (if currently on loan)");
		System.out.println("  0) End turn");
		int option = input.nextInt();
		return option;
	}
	
	/*
	 * Basic version of the main round method which will be called at the start of the game and run until game is over
	 * Not sure on specifics yet, just doing it to get things working and working through things in my head.
	 * -Chris
	*/
	public void mainRound()
	{
		int roundNumber = 1;
		while(roundNumber < 13)
		{
			System.out.println("Now beginning round number " + roundNumber);
			players = bank.getPlayers();
			for (Player currentsPlayer : players) {
				bank.generateCardIndex(currentsPlayer);
				bank.removeCard(currentsPlayer);
			}
			for(Player currentPlayer : players)
			{
				System.out.println( currentPlayer.getUserName() + "'s turn");
				
				if(currentPlayer.getLoanStatus())//THIS IS NEW, also need validation in here 
				{
					currentPlayer.setBalanceDown(20);
				}
				
				int option = -1;
				bank.generateCardIndex(currentPlayer);//gives each player an index
				
				while(option != 0)
				{
					System.out.println("\f");
					option = roundMenu();
					
					switch(option)
					{
						case 1:
							chooseStock("buy", currentPlayer);
						break;
						case 2:
							chooseStock("sell", currentPlayer);
						break;
						case 3:
							System.out.println("\nYour current balance is £" + currentPlayer.getBalance());
							System.out.println("\nYour current Motors stock: " + currentPlayer.getStocks("Motors"));
							System.out.println("Your current Shipping stock: " + currentPlayer.getStocks("Shipping"));
							System.out.println("Your current Steel stock: " + currentPlayer.getStocks("Steel"));
							System.out.println("Your current Stores stock: " + currentPlayer.getStocks("Stores"));
							System.out.println("\nPress any key to continue...");
							input.nextLine();
							input.nextLine();
						break;
						case 4:
							System.out.println("\nMotors current stock price: £" + bank.getStockPrice("Motors"));
							System.out.println("Shipping current stock price: £" + bank.getStockPrice("Shipping"));
							System.out.println("Steel current stock price: £" + bank.getStockPrice("Steel"));
							System.out.println("Stores current stock price: £" + bank.getStockPrice("Stores"));
							System.out.println("\nMotors current stock amount: " + bank.getStockAmount("Motors"));
							System.out.println("Shipping current stock amount: " + bank.getStockAmount("Shipping"));
							System.out.println("Steel current stock amount: " + bank.getStockAmount("Steel"));
							System.out.println("Stores current stock amount: " + bank.getStockAmount("Stores"));
							System.out.println("\nPress any key to continue...");
							input.nextLine();
							input.nextLine();
						break;
						case 5://This is currently printing out on card per user.*Dave
							System.out.println(bank.generateCard(currentPlayer));
							System.out.println("\nPress any key to continue..");
							input.nextLine();//SOME NEW SHIT AROUND HERE DOWN TO THE BREAK OF CASE 7
							input.nextLine();
						break;
						case 6:
							currentPlayer.setLoanStatus(true);
							currentPlayer.setBalanceUp(80);
						break;
						case 7:
							//Needs validation like a motherfucker
							currentPlayer.setBalanceDown(100);
							currentPlayer.setLoanStatus(false);
						break;
					}
				}
			}
			roundNumber ++;
			bank.resetArray();//new//
		}
	}

	public void addPlayer() 
	{
		System.out.println("Please enter the amount of players");
		int playerAmount = input.nextInt();
		int counter = 1;
  
		if(playerAmount >=3 && playerAmount <=6 )
		{ 
			while (counter <= playerAmount)
			{
				System.out.println("Please enter player number "  +counter+ "'s name");
				String name = input.next();
				bank.add(new Player(name));
				counter ++;
			}	
		}
		else
		{	
			System.out.println("Please enter a value between 3 and 6.");
		}	  
		mainRound();
	}
	
	private int chooseStockMenu(String buyOrSell)
	{
		System.out.println("What type of stock would you like to " + buyOrSell + "?");
		System.out.println("  1) Motors");
		System.out.println("  2) Shipping");
		System.out.println("  3) Steel");
		System.out.println("  4) Stores");
		System.out.println("  0) Cancel");
		int option = input.nextInt();
		return option;
	}
	
	/*
	 * Think we're going to need a lot of validation for this stuff like checking whether or not the player/bank has
	 * the appropriate stocks/funds to carry out the action.
	 * -Chris
	 */
	public void chooseStock(String buyOrSell, Player currentPlayer)
	{
		if(buyOrSell == "buy")
		{
			int option = chooseStockMenu(buyOrSell);
			String stockType = "";
			while(option < 0 && option >= 5 )
			{
				System.out.println("Invalid option entered. Please enter a valid option between 0 and 4");
				option = chooseStockMenu(buyOrSell);
			}
			if(option != 0)
			{
				switch(option)
				{
					case 1: 
						stockType = "Motors";
					break;
					case 2:
						stockType = "Shipping";
					break;
					case 3:
						stockType = "Steel";
					break;
					case 4:
						stockType = "Stores";
					break;
				}
				chooseAmount(stockType, buyOrSell, currentPlayer);
			}
		}
		else if(buyOrSell == "sell")
		{
			int option = chooseStockMenu(buyOrSell);
			String stockType = "";
			while(option < 0 && option >= 5 )
			{
				System.out.println("Invalid option entered. Please enter a valid option between 0 and 4");
				option = chooseStockMenu(buyOrSell);
			}
			if(option != 0)
			{
				switch(option)
				{
					case 1: 
						stockType = "Motors";
					break;
					case 2:
						stockType = "Shipping";
					break;
					case 3:
						stockType = "Steel";
					break;
					case 4:
						stockType = "Stores";
					break;
				}
				chooseAmount(stockType, buyOrSell, currentPlayer);
			}
		}
		
	}
	
	private int chooseAmountMenu(String buyOrSell)
	{
		//May want to add a 0 option here to allow the user to back out, we'll see.
		System.out.println("How many stocks would you like to " + buyOrSell + "?");
		System.out.println("  1) One stock");
		System.out.println("  2) Two stocks");
		System.out.println("  3) Three stocks");
		System.out.println("  4) Four stocks");
		System.out.println("  5) Five stocks");
		System.out.println("  6) Ten stocks");
		System.out.println("  7) Fifteen stocks");
		System.out.println("  8) Twenty stocks");
		System.out.println("  9) Twenty-Five stocks");
		int option = input.nextInt();
		return option;
		
	}
	
	public void chooseAmount(String stockType, String buyOrSell, Player currentPlayer)
	{
		int option = chooseAmountMenu(buyOrSell);
		while(option < 1 && option > 9)
		{
			System.out.println("Invalid option entered. Please enter a valid option between 1 and 9");
			option = chooseAmountMenu(buyOrSell);
		}
		if(buyOrSell == "buy")
		{
			switch(option)
			{
				case 1:
					buyStock(stockType, 1, currentPlayer);
				break;
				case 2:
					buyStock(stockType, 2, currentPlayer);
				break;
				case 3:
					buyStock(stockType, 3, currentPlayer);
				break;
				case 4:
					buyStock(stockType, 4, currentPlayer);
				break;
				case 5:
					buyStock(stockType, 5, currentPlayer);
				break;
				case 6:
					buyStock(stockType, 10, currentPlayer);
				break;
				case 7:
					buyStock(stockType, 15, currentPlayer);
				break;
				case 8:
					buyStock(stockType, 20, currentPlayer);	
				break;
				case 9:
					buyStock(stockType, 25, currentPlayer);
				break;
			}
		}
		else if(buyOrSell == "sell")
		{
			switch(option)
			{
				case 1:
					sellStock(stockType, 1, currentPlayer);
				break;
				case 2:
					sellStock(stockType, 2, currentPlayer);
				break;
				case 3:
					sellStock(stockType, 3, currentPlayer);
				break;
				case 4:
					sellStock(stockType, 4, currentPlayer);
				break;
				case 5:
					sellStock(stockType, 5, currentPlayer);
				break;
				case 6:
					sellStock(stockType, 10, currentPlayer);
				break;
				case 7:
					sellStock(stockType, 15, currentPlayer);
				break;
				case 8:
					sellStock(stockType, 20, currentPlayer);	
				break;
				case 9:
					sellStock(stockType, 25, currentPlayer);
				break;
			}
		}
	}
	
	/*
	 * The method that will be called whenever a player is buying stocks
	 * Still not 100% finished or anything but it's a basic version at least
	 * Need to add a way to remove stocks from the bank still
	 * No validation yet either
	 * -Chris
	 */
	public void buyStock(String stockType, int amount, Player currentPlayer)
	{
		if(stockType == "Motors")
		{
			bank.setStockAmount("Motors", -amount);
			currentPlayer.setStocksUp("Motors", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceDown(bank.getStockPrice("Motors"));
				counter ++;
			}
		}
		else if(stockType == "Shipping")
		{
			bank.setStockAmount("Shipping", -amount);
			currentPlayer.setStocksUp("Shipping", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceDown(bank.getStockPrice("Shipping"));
				counter ++;
			}
		}
		else if(stockType == "Steel")
		{
			bank.setStockAmount("Steel", -amount);
			currentPlayer.setStocksUp("Steel", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceDown(bank.getStockPrice("Steel"));
				counter ++;
			}
		}
		else if(stockType == "Stores")
		{
			bank.setStockAmount("Stores", -amount);
			currentPlayer.setStocksUp("Stores", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceDown(bank.getStockPrice("Stores"));
				counter ++;
			}
		}
	}
	
	/*
	 * The method that will be called whenever a player is selling stocks
	 * Still not 100% finished or anything but it's a basic version at least
	 * Need to add a way to add stocks back to the bank still
	 * No validation yet either 
	 * -Chris
	 */
	public void sellStock(String stockType, int amount, Player currentPlayer)
	{
		if(stockType == "Motors")
		{
			bank.setStockAmount("Motors", amount);
			currentPlayer.setStocksDown("Motors", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceUp(bank.getStockPrice("Motors"));
				counter ++;
			}
		}
		else if(stockType == "Shipping")
		{
			bank.setStockAmount("Shipping", amount);
			currentPlayer.setStocksDown("Shipping", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceUp(bank.getStockPrice("Shipping"));
				counter ++;
			}
		}
		else if(stockType == "Steel")
		{
			bank.setStockAmount("Steel", amount);
			currentPlayer.setStocksDown("Steel", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceUp(bank.getStockPrice("Steel"));
				counter ++;
			}
		}
		else if(stockType == "Stores")
		{
			bank.setStockAmount("Stores", amount);
			currentPlayer.setStocksDown("Stores", amount);
			int counter = 0;
			while(counter < amount)
			{
				currentPlayer.setBalanceUp(bank.getStockPrice("Stores"));
				counter ++;
			}
		}
	}
	
	public void lastScore() throws Exception
	{
		bank.load();
		
		highScore = new ArrayList<Player>();
	
		for(int i=0; i<players.size() ; i++)
		{
			if(highScore.size() == 0 || players.get(i).getBalance() > highScore.get(0).getBalance() )
			{
				highScore.add(0, players.get(i));
			}
			else if(players.get(i).getBalance() > highScore.get(1).getBalance())
			{
				highScore.add(1, players.get(i));
			}
			else if(players.get(i).getBalance() > highScore.get(2).getBalance())
			{
				highScore.add(2, players.get(i));
			}
			else if(players.get(i).getBalance() > highScore.get(3).getBalance())
			{
				highScore.add(3, players.get(i));
			}
			else if(players.get(i).getBalance() > highScore.get(4).getBalance())
			{
				highScore.add(4, players.get(i));
			}
			else
			{
				highScore.add(5, players.get(i));
			}
		}
		System.out.println("Here are the end scores of the last game: ");
		System.out.println("--------------------------------------------");
		System.out.println("Postion: Name:	Money");
		System.out.println("1		" + highScore.get(0).getUserName() +"	 " + highScore.get(0).getBalance() );
		System.out.println("2		" + highScore.get(1).getUserName() +"	 " + highScore.get(0).getBalance());
		System.out.println("3		" + highScore.get(2).getUserName() +"	 " + highScore.get(0).getBalance());
		System.out.println("4		" + highScore.get(3).getUserName() +"	 " + highScore.get(0).getBalance());
		System.out.println("5		" + highScore.get(4).getUserName() +"	 " + highScore.get(0).getBalance());
		System.out.println("6		" + highScore.get(5).getUserName() +"	 " + highScore.get(0).getBalance());
		System.out.println("");
		System.out.println("/nPress any key to continue...");
		
		input.nextLine();
		input.nextLine(); 

		
		System.out.println("\f");
		players.clear();
		runMenu();
	}
 	
}
