import java.sql.*;
import java.util.Scanner;

public class DbConnection {
	
	private Connection connect() {
		Connection c = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:nbatrader.db");
			//System.out.println("Sqlite DB Connected\n");
		} catch (Exception e) {
			System.out.println(e);
		}
		return c;
	}

	public void allPlayers() {
		String sql = "SELECT * FROM players";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + " " + rs.getDouble("ppg") + "ppg "+ rs.getDouble("apg") + "apg "+ rs.getDouble("rpg") + "rpg");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mainMenu();
	}
	
	public void allTeams() {
		String sql = "SELECT * FROM team";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("team_name") + (" ") + rs.getString("record"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mainMenu();
	}
	
	public void allAgents() {
		String sql = "SELECT * FROM agents";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("agent_name"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mainMenu();
	}
	public void allContracts() {
		String sql = "SELECT * FROM contracts";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + (" ") + ("$") + rs.getDouble("contracts_million_per_year")+ ("M"));

			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mainMenu();
	}
	
	public void allTrades() {
		String sql = "SELECT * FROM trade";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getInt("trade_id") + (" ") + rs.getString("player_name"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		mainMenu();
	}
	
	public void mainMenu() {
		System.out.println("\nWelcome to the NBA Trader database");
		System.out.println(
				"\nTo view all the data in the database select from the options below.\nSelect 1 to view all the players.\n"
				+ "Select 2 to view all the teams.\n"
				+ "Select 3 to view all contracts.\n"
				+ "Select 4 to view all agents.\n"
				+ "Select 5 to view all Trades.\n"
				+ "Select 6 view queries.\n"
						
				);
		Scanner i = new Scanner(System.in);
		Integer t = i.nextInt();
		if (t == 1) {
			allPlayers();
		} else if(t == 2) {
			allTeams();
		}else if(t == 3) {
			allContracts();
		}else if(t == 4) {
			allAgents();
		}
		else if(t == 5) {
			allTrades();
		}else if(t == 6) {
			queryMenu();
		}
	}
	
	public void queryMenu() {
		System.out.println("\nThese are the list of the following available queries to be made.\n");
		System.out.println("Select 1 to display all the players on teams who are making over $30m/year."
				+ "\nSelect 2 to display the agents who have clients making over $20m.\n"
				+ "Select 3 to identify all the star players who were traded away by their team.\n"
				+ "Select 4 to identify the teams that have completed more than 1 trade.\n"
				+ "Select 5 to display the highest paid point guards and identify their playmaking abilities.\n"
				+ "Select 6 provides the average contract price for the Toronto Raptors. \n"
				+ "Select 7 returns the top earner listed and who the agent is that got them the contract.\n"
				+ "Select 8 displays the top 3 most dominant players around the rim in the database.\n"
				+ "Select 9 shows all the players the Houston Rockets have dealt with in transactions.\n"
				+ "Select 10 lists the amount of money the Houston Rockets alleviate in the trades they've made.\n"
				+ "Select 11 to return to the main menu.");
		Scanner m = new Scanner(System.in);
		Integer n = m.nextInt();
		if (n == 1) {
			queryOne();
		}else if (n == 2) {
			queryTwo();
		} else if (n == 3) {
			queryThree();
		} else if (n == 4) {
			queryFour();
		} else if(n == 5) {
			queryFive();
		} else if(n == 6) {
			querySix();
		} else if(n == 7) {
			querySeven();
		} else if (n == 8) {
			queryEight();
		} else if(n == 9) {
			queryNine();
		} else if (n == 10) {
			queryTen();
		}else if (n == 11) {
			mainMenu();
		}
	}
	
	
	public void queryOne() {
		String sql = "SELECT player_name FROM contracts WHERE contracts_million_per_year > 30";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name"));

			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryTwo() {
		String sql = "SELECT players.player_name, players.agent_name, contracts.contracts_million_per_year FROM players INNER JOIN contracts ON players.player_name = contracts.player_name WHERE contracts_million_per_year > 20 ORDER BY contracts_million_per_year desc";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("agent_name") + ("--") + rs.getString("player_name") + (" $") + rs.getDouble("contracts_million_per_year") + ("M"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		queryMenu();
	}
	
	public void queryThree() {
		String sql = "SELECT team.team_name, players.player_name, players.ppg, contracts.contracts_million_per_year FROM team INNER JOIN players ON team.team_id = players.team_id INNER JOIN contracts ON players.player_name = contracts.player_name WHERE players.traded = 1 AND (contracts_million_per_year > 25 OR ppg > 25)";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + ("--") + rs.getString("team_name") + ("--") + rs.getDouble("ppg")+ ("ppg $") + rs.getDouble("contracts_million_per_year") + ("M"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryFour() {
		String sql = "SELECT team_name, player_name FROM team JOIN players ON team.team_id = players.team_id WHERE team_trades > 1";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("team_name") + ("--") + rs.getString("player_name"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryFive() {
		String sql = "SELECT players.player_name, players.apg, contracts.contracts_million_per_year FROM players JOIN contracts ON contracts.player_name = players.player_name WHERE players.player_position = 'PG' ORDER BY contracts_million_per_year desc";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + ("--") + rs.getDouble("apg") + ("apg $") + rs.getDouble("contracts_million_per_year")+("M"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void querySix() {
		String sql = ("SELECT AVG(contracts_million_per_year) FROM contracts WHERE team_id = 1");
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("$"+ rs.getFloat(1) + "M");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
				
	}
	
	public void querySeven() {
		String sql = "SELECT players.player_name, players.agent_name, contracts.contracts_million_per_year FROM contracts JOIN players ON contracts.player_name = players.player_name JOIN agents ON players.player_id = agents.player_id ORDER BY contracts_million_per_year DESC LIMIT 1";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + ("--") + rs.getString("agent_name") + (" $") + rs.getDouble("contracts_million_per_year")+("M"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryEight() {
		String sql = "SELECT player_name, rpg FROM players ORDER BY rpg DESC LIMIT 3";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name") + ("--") + rs.getDouble("rpg") + ("rpg"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryNine() {
		String sql = "SELECT player_name FROM trade WHERE team_toid = 3 OR team_fromid = 3";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println(rs.getString("player_name"));
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public void queryTen() {
		String sql = "SELECT SUM(contracts.contracts_million_per_year) FROM contracts JOIN trade ON trade.player_name = contracts.player_name WHERE team_fromid = 3";
		try(Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			while(rs.next()) {
				System.out.println("$" + rs.getFloat(1) + "M");
			}
		}catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		queryMenu();
	}
	
	public static void main(String[] args) {
		DbConnection conn1 = new DbConnection();
		conn1.mainMenu();
		
	}
	
	
}