package com.java.ex;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Stock extends JDialog implements ActionListener {

	JPanel pw = new JPanel(new GridLayout(4, 1));
	JPanel pc = new JPanel(new GridLayout(4, 1));
	JPanel ps = new JPanel();

	JLabel lable_Id = new JLabel("ID");
	JLabel lable_Name = new JLabel("상품");
	JLabel lable_Stock = new JLabel("제고");
	JLabel lable_Price = new JLabel("가격");

	JTextField id = new JTextField();
	JTextField name = new JTextField();
	JTextField stock = new JTextField();
	JTextField price = new JTextField();

	JButton confirm;
	JButton reset = new JButton("취소");

	Mtable me;

	SaleStockDAO dao = new SaleStockDAO();

	public Stock(Mtable me, String index) {

		this.me = me;
		if (index.equals("입력")) {
			confirm = new JButton(index);
			id.setEditable(false);
		} else {
			confirm = new JButton("수정");

			//text박스에 선택된 레코드의 정보 넣기
			int row = me.jt.getSelectedRow();// 선택된 행
			id.setText(me.jt.getValueAt(row, 0).toString());
			name.setText(me.jt.getValueAt(row, 1).toString());
			stock.setText(me.jt.getValueAt(row, 2).toString());
			price.setText(me.jt.getValueAt(row, 3).toString());

			//id text박스 비활성

			id.setEditable(false);

		}

		//Label추가부분
		pw.add(lable_Id);
		pw.add(lable_Name);// 이름
		pw.add(lable_Stock);// 나이
		pw.add(lable_Price);// 주소

		//TextField 추가
		pc.add(id);
		pc.add(name);
		pc.add(stock);
		pc.add(price);

		ps.add(confirm);
		ps.add(reset);

		add(pw, "West");
		add(pc, "Center");
		add(ps, "South");

		setSize(300, 250);
		setVisible(true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		//이벤트등록
		confirm.addActionListener(this); // 가입/수정 이벤트등록
		reset.addActionListener(this); // 취소 이벤트등록

	}// 생성자끝

	/**
	 * 가입/수정/삭제 기능에 대한 부분
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String btnLabel = e.getActionCommand();// 이벤트주체 대한 Label 가져오기

		if (btnLabel.equals("입력")) {
			if (dao.ListInsert(this) > 0) {// 가입성공
				messageBox(this, name.getText() + "입력완료");

				dispose();// JDialog 창닫기

				dao.SelectAll(me.dt);// 모든레코드가져와서 DefaultTableModel에 올리기

				if (me.dt.getRowCount() > 0)
					me.jt.setRowSelectionInterval(0, 0);// 첫번째 행 선택

			} else {// 가입실패
				messageBox(this, "가입되지 않았습니다.");
			}

		} else if (btnLabel.equals("수정")) {

			if (dao.Update(this) > 0) {
				messageBox(this, "수정완료되었습니다.");
				dispose();
				dao.SelectAll(me.dt);
				if (me.dt.getRowCount() > 0)
					me.jt.setRowSelectionInterval(0, 0);

			} else {
				messageBox(this, "수정되지 않았습니다.");
			}

		} else if (btnLabel.equals("취소")) {
			dispose();
		}

	}

	/**
	 * 메시지박스 띄워주는 메소드 작성
	 */
	public static void messageBox(Object obj, String message) {
		JOptionPane.showMessageDialog((Component) obj, message);
	}

}// 클래스끝
