package com.java.ex;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

//======================================== 재고창 메소드 시작 ==================================================

public class SaleStockDAO {

	Connection con;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;

	public SaleStockDAO() {
		try {

			Class.forName("org.mariadb.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3307/datadb", "root", "skrkcb");

		} catch (ClassNotFoundException e) {
			System.out.println(e + "road fail");
		} catch (SQLException e) {
			System.out.println(e + "connect fail");
		}
	}// 생성자

	//list에 상품 재고 추가 기능 메소드
	public int ListInsert(Stock stock1) {
		int result = 0;
		try {
			ps = con.prepareStatement("INSERT INTO item (`item_name`, `item_stock`, `item_price`) VALUES (?, ?, ?)");

			ps.setString(1, stock1.name.getText());
			ps.setInt(2, Integer.parseInt(stock1.stock.getText()));
			ps.setInt(3, Integer.parseInt(stock1.price.getText()));

			result = ps.executeUpdate(); // 실행 -> 저장

		} catch (SQLException e) {
			System.out.println(e + "ListInsert fail");
		}

		return result;

	}// userListInsert()

	// list의 모든 정보 조회
	public void SelectAll(DefaultTableModel t_model) {
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from item order by id");

			// DefaultTableModel에 있는 기존 데이터 지우기
			for (int i = 0; i < t_model.getRowCount();) {
				t_model.removeRow(0);
			}

			while (rs.next()) {
				Object data[] = { rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4) };

				t_model.addRow(data); // DefaultTableModel에 레코드 추가
			}

		} catch (SQLException e) {
			System.out.println(e + "SelectAll fail");
		}
	}// SelectAll()

	//상품 삭제
	public int Delete(String id) {
		int result = 0;
		try {
			ps = con.prepareStatement("DELETE FROM item WHERE (id = ?) ");
			ps.setString(1, id.trim());
			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e + "Delete fail");
		}

		return result;
	}// Delete()

	//상품 수정
	public int Update(Stock stock1) {
		int result = 0;
		String sql = "UPDATE item SET item_name=?, item_stock=? , item_price=? WHERE id=?";

		try {
			ps = con.prepareStatement(sql);
			ps.setString(4, stock1.id.getText().trim());
			ps.setString(1, stock1.name.getText());
			ps.setString(2, stock1.stock.getText());
			ps.setString(3, stock1.price.getText());

			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e + "Update fail");
		}
		return result;
	}

// 검색단어에 해당하는 레코드 검색하기 (like연산자를 사용하여 _, %를 사용할때는 PreparedStatemnet안된다. 반드시 Statement객체를 이용함)
	public void getSearch(DefaultTableModel dt, String fieldName, String word) {
		String sql = "SELECT * FROM item WHERE " + fieldName.trim() + " LIKE '%" + word.trim() + "%'";

		try {
			st = con.createStatement();
			rs = st.executeQuery(sql);

			// DefaultTableModel에 있는 기존 데이터 지우기
			for (int i = 0; i < dt.getRowCount();) {
				dt.removeRow(0);
			}

			while (rs.next()) {
				Object data[] = { rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4) };

				dt.addRow(data);
			}

		} catch (SQLException e) {
			System.out.println(e + "getSearch fail");
		}

	}

//============================================상품 판매창 메소드 시작===================================================

	//아이템 가저오기
	public Vector<String> getItem() throws SQLException {
		Vector<Item> dbitemlist = getAllItem();
		Vector<String> itemlist = new Vector<String>();
		for (Item item : dbitemlist) {
			itemlist.add(item.getItem_name());
		}
		return itemlist;
	}

	// 모든 상품 가저오기
	public Vector<Item> getAllItem() throws SQLException {
		Vector<Item> list = new Vector<>();
		String price = null;
		String sql = "select * from item";
		try {

			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				Item item = new Item();
				item.setId(rs.getInt("id"));
				item.setItem_name(rs.getString("item_name"));
				item.setItem_stock(rs.getInt("item_stock"));
				item.setItem_price(rs.getInt("item_price"));
				list.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	//상품 이름으로 가격 가져오기
	public String getprice(String item_name) {

		String price = null;
		String sql = "select item_price from item where item_name=? ";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, item_name);
			rs = ps.executeQuery();

			if (rs.next()) {
				price = Integer.toString(rs.getInt("item_price"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return price;
	}

	//수량가져오기
	public String getStock(String item_name) {
		String stock = null;
		String sql = "select item_stock from item where item_name=? ";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, item_name);
			rs = ps.executeQuery();

			if (rs.next()) {
				stock = Integer.toString(rs.getInt("item_stock"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();

		}
		return stock;
	}

	// 재고 업데이트
	public void updateStock(String total, String stock, String name) {

		String sql = "update item set item_stock = ? - ? where item_name = ?";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, total);
			ps.setString(2, stock);
			ps.setString(3, name);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertStock(String itemname, String itemstock, String itemprice, Date date, Time time, String id) {

		String sql = "INSERT INTO item1 (`item_name`, `item_stock`, `item_price`,`date`,`time`,`id`) VALUES (?, ?, ?,?,?,?)";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, itemname);
			ps.setString(2, itemstock);
			ps.setString(3, itemprice);
			ps.setDate(4, date);
			ps.setTime(5, time);
			ps.setString(6, id);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ===============================================영수증 창 시작=====================================================

	public void SelectAll2(DefaultTableModel c_model) {
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from item1 order by item_name");

			// DefaultTableModel에 있는 기존 데이터 지우기
			for (int i = 0; i < c_model.getRowCount();) {
				c_model.removeRow(0);
			}

			while (rs.next()) {
				Object data[] = { rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5),
						rs.getString(6) };

				c_model.addRow(data); // DefaultTableModel에 레코드 추가
			}

		} catch (SQLException e) {
			System.out.println(e + "SelectAll fail");
		}
	}// SelectAll()

	//상품 반품
	public int Delete2(String id) {
		int result = 0;
		try {
			ps = con.prepareStatement("DELETE FROM item1 WHERE (`id` = ?)");
			ps.setString(1, id);

			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e + "Delete fail");
		}

		return result;
	}// Delete()

	//재고 업데이트
	public void updateStock2(String total, String stock, String name) {

		String sql = "update item set item_stock = ? + ? where item_name = ?";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, total);
			ps.setString(2, stock);
			ps.setString(3, name);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}