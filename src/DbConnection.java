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
				System.out.println(rs.getString("player_name"));
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
		System.out.println("\nSelect 1 to display all the players on teams who are making over $30m/year."
				+ "\nSelect 2 to display the agents who have clients making over $20m.\n"
				+ "\nSelect 3 to identify all the star players being paid $25m/year.\n");
		Scanner m = new Scanner(System.in);
		Integer n = m.nextInt();
		if (n == 1) {
			queryOne();
		}else if (n == 2) {
			queryTwo();
		} else if (n == 3) {
			queryThree();
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
	
	public static void main(String[] args) {
		DbConnection conn1 = new DbConnection();
		conn1.mainMenu();
		
	}
	
	
}