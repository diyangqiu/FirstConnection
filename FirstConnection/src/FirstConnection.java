import java.sql.*;
import java.util.Scanner;

public class FirstConnection {

	public static void getOrder(Integer orderID) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system", "password");

			Statement stat = con.createStatement();
			// execute the query
			ResultSet rs = stat
					.executeQuery("SELECT ORDER_TIMESTAMP, ORDER_TOTAL, CUST_LAST_NAME || ' ' || CUST_FIRST_NAME as NAME, "
							+ "CUST_CITY, CUST_STATE, CUST_STREET_ADDRESS1 || ' ' || CUST_STREET_ADDRESS2 as ADDRESS "
							+ "FROM testuser.DEMO_ORDERS "
							+ "JOIN testuser.DEMO_CUSTOMERS "
							+ "ON DEMO_ORDERS.CUSTOMER_ID = DEMO_CUSTOMERS.CUSTOMER_ID "
							+ "WHERE DEMO_ORDERS.ORDER_ID = "
							+ orderID.toString());

			if (rs.next()) {
				int total = 0;
				System.out.println(rs.getString("ORDER_TIMESTAMP")
						+ "\t\tOrder Number:" + orderID.toString());
				System.out.println(rs.getString("NAME"));
				System.out.println(rs.getString("ADDRESS"));
				System.out.println(rs.getString("CUST_CITY") + "\t"
						+ rs.getString("CUST_STATE"));
				System.out.println("---------------------------------------");
				System.out.println("Qty|\tDescription\t\t|Amount");
				ResultSet receipt = stat
						.executeQuery("SELECT DEMO_ORDER_ITEMS.ORDER_ID, QUANTITY, UNIT_PRICE*QUANTITY as Amount, PRODUCT_NAME "
								+ "FROM testuser.DEMO_ORDERS "
								+ "JOIN testuser.DEMO_ORDER_ITEMS "
								+ "ON DEMO_ORDER_ITEMS.ORDER_ID = DEMO_ORDERS.ORDER_ID "
								+ "JOIN testuser.DEMO_PRODUCT_INFO "
								+ "ON DEMO_ORDER_ITEMS.PRODUCT_ID = DEMO_PRODUCT_INFO.PRODUCT_ID "
								+ "WHERE DEMO_ORDER_ITEMS.ORDER_ID = "
								+ orderID.toString());
				while (receipt.next()) {
					total += Integer.parseInt(receipt.getString("Amount"));
					System.out.printf("%s%-18s%15s", 
							receipt.getString("QUANTITY")+"  |", 
							receipt.getString("PRODUCT_NAME"), 
							"|$" + receipt.getString("Amount"));
					
					System.out.println();
				}
				System.out.println();
				System.out.println("Total: $" + total);
			}

			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getOrderHistory(Integer custID) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system", "password");

			Statement stat = con.createStatement();
			// execute the query
			ResultSet rs = stat
					.executeQuery("SELECT ORDER_TIMESTAMP, order_total, cust_last_name || ' ' || cust_first_name as NAME, "
							+ "CUST_CITY, CUST_STATE, CUST_STREET_ADDRESS1 || ' ' || CUST_STREET_ADDRESS2 as ADDRESS "
							+ "FROM testuser.DEMO_ORDERS "
							+ "JOIN testuser.DEMO_CUSTOMERS "
							+ "ON DEMO_ORDERS.CUSTOMER_ID = DEMO_CUSTOMERS.CUSTOMER_ID "
							+ "WHERE DEMO_CUSTOMERS.CUSTOMER_ID = "
							+ custID.toString());

			while (rs.next()) {
				System.out.println(rs.getString("ORDER_TIMESTAMP"));
				System.out.println(rs.getString("NAME"));
				System.out.println(rs.getString("ADDRESS"));
				System.out.println(rs.getString("CUST_CITY") + "\t"
						+ rs.getString("CUST_STATE"));
				System.out.println("---------------------------------------");
				System.out.println("Date      |\tOrder Number\t|Total");
				ResultSet detail = stat
						.executeQuery("SELECT DEMO_ORDERS.ORDER_ID, ORDER_TOTAL, ORDER_TIMESTAMP FROM testuser.DEMO_ORDERS "
								+ "JOIN testuser.DEMO_CUSTOMERS "
								+ "ON DEMO_CUSTOMERS.CUSTOMER_ID = DEMO_ORDERS.CUSTOMER_ID  "
								+ "WHERE DEMO_ORDERS.CUSTOMER_ID = "
								+ custID.toString());
				while (detail.next()) {
					System.out.printf("%s%17s%10s", 
							detail.getDate("ORDER_TIMESTAMP")+ "|",
							detail.getString("ORDER_ID"), 
							"|$" + detail.getString("ORDER_TOTAL"));
					
					System.out.println();
				}
				System.out.println();
			}

			con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Please Enther The Function number you want to enter:")
		System.out.println("Select 1 for Order Number Selection, Select 2 for Order History Selection");
		Scanner sc = new Scanner;
		
		//getOrder(3);
		getOrderHistory(1);
	}

}
