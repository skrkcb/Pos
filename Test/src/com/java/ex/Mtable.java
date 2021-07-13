package com.java.ex;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Mtable extends JFrame implements ActionListener {
	JMenu m = new JMenu("관리");
	JMenuItem insert = new JMenuItem("입력");
	JMenuItem update = new JMenuItem("수정");
	JMenuItem delete = new JMenuItem("삭제");
	JMenuItem quit = new JMenuItem("종료");
	JMenuBar mb = new JMenuBar();

	String[] name = { "id", "상품", "재고", "가격" };

	DefaultTableModel dt = new DefaultTableModel(name, 0);
	JTable jt = new JTable(dt);
	JScrollPane jsp = new JScrollPane(jt);

	/*
	 * South 영역에 추가할 Componet들
	 */
	JPanel p = new JPanel();
	String[] comboName = { "  ALL  ", "  item_name  ", " item_stock ", " item_price " };

	JComboBox combo = new JComboBox(comboName);
	JTextField jtf = new JTextField(20);
	JButton serach = new JButton("검색");

	SaleStockDAO dao = new SaleStockDAO();

	/**
	 * 화면구성 및 이벤트등록
	 */
	public Mtable() {

		//메뉴아이템을 메뉴에 추가
		m.add(insert);
		m.add(update);
		m.add(delete);
		m.add(quit);
		//메뉴를 메뉴바에 추가
		mb.add(m);

		//윈도우에 메뉴바 세팅
		setJMenuBar(mb);

		// South영역
		p.setBackground(Color.yellow);
		p.add(combo);
		p.add(jtf);
		p.add(serach);

		add(jsp, "Center");
		add(p, "South");

		setSize(500, 400);
		setVisible(true);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 이벤트등록
		insert.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);
		quit.addActionListener(this);
		serach.addActionListener(this);

		// 모든레코드를 검색하여 DefaultTableModle에 올리기
		dao.SelectAll(dt);

		//첫번행 선택.
		if (dt.getRowCount() > 0)
			jt.setRowSelectionInterval(0, 0);

	}// 생성자끝

	/**
	 * main메소드 작성
	 */
	public static void main(String[] args) {
		new Mtable();
	}

	/**
	 * 가입/수정/삭제/검색기능을 담당하는 메소드
	 */

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insert) {// 가입 메뉴아이템 클릭
			new Stock(this, "입력");

		} else if (e.getSource() == update) {// 수정 메뉴아이템 클릭
			new Stock(this, "수정");

		} else if (e.getSource() == delete) {// 삭제 메뉴아이템 클릭
			// 현재 Jtable의 선택된 행과 열의 값을 얻어온다.
			int row = jt.getSelectedRow();

			Object obj = jt.getValueAt(row, 0);// 행 열에 해당하는 value

			if (dao.Delete(obj.toString()) > 0) {
				Stock.messageBox(this, "상품 삭제되었습니다.");

				//리스트 갱신
				dao.SelectAll(dt);
				if (dt.getRowCount() > 0)
					jt.setRowSelectionInterval(0, 0);

			} else {
				Stock.messageBox(this, "상품 삭제되지 않았습니다.");
			}

		} else if (e.getSource() == quit) {// 종료 메뉴아이템 클릭
			Choose ch = new Choose();
			dispose();

		} else if (e.getSource() == serach) {// 검색 버튼 클릭
			// JComboBox에 선택된 value 가져오기
			String fieldName = combo.getSelectedItem().toString();

			if (fieldName.trim().equals("ALL")) {// 전체검색
				dao.SelectAll(dt);
				if (dt.getRowCount() > 0)
					jt.setRowSelectionInterval(0, 0);
			} else {
				if (jtf.getText().trim().equals("")) {
					Stock.messageBox(this, "검색단어를 입력해주세요!");
					jtf.requestFocus();
				} else {// 검색어를 입력했을경우
					dao.getSearch(dt, fieldName, jtf.getText());
					if (dt.getRowCount() > 0)
						jt.setRowSelectionInterval(0, 0);
				}
			}
		}

	}
}