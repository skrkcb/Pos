package com.java.ex;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;






public class MainPos extends JFrame implements ActionListener  {

	Calendar cal = new GregorianCalendar();
	Date date = new Date(cal.getTimeInMillis());
	Time time = new Time(cal.getTimeInMillis());
	JButton cancelBtn = new JButton("선택취소");
	JButton allcancelBtn =  new JButton("새로고침");
	JButton resultBtn =  new JButton("결제");
	JButton recipeBtn = new JButton("뒤로가기");
	JButton insert = new JButton("등록");
	
	JLabel store = new JLabel("상품");
	JLabel many = new JLabel("수량");
	
	JTextField stuffTf = new JTextField();
	
	JPanel contentPane = new JPanel();
	JComboBox comboBox = new JComboBox();
	JTextField sale = new JTextField();
	
	
	JTable jt1;
	
	SaleStockDAO dao = new SaleStockDAO();
	/*
	* South 영역에 추가할 Componet들
	*/

public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				MainPos frame = new MainPos();

			} catch (Exception e) {
				System.out.println("문제 있다");
				e.printStackTrace();
			}
		}
	});
}

	/**
	* 화면구성 및 이벤트등록
	*/
public MainPos() throws SQLException {

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setContentPane(contentPane);
	setSize(770,660);
	setVisible(true);
	setLayout(null);
	
	store.setBounds(530, 23, 200, 70);
	store.setFont(new Font("맑은고딕", Font.BOLD, 15));
	contentPane.add(store);
	
	many.setBounds(530, 95, 200, 70);
	many.setFont(new Font("맑은고딕", Font.BOLD, 15));
	contentPane.add(many);
	
	cancelBtn.setBounds(530, 410, 100, 70);
	contentPane.add(cancelBtn);
	
	resultBtn.setBounds(631, 480, 100, 70);
	contentPane.add(resultBtn);
	
	allcancelBtn.setBounds(631, 410, 100, 70);
	contentPane.add(allcancelBtn);
	
	recipeBtn.setBounds(530, 480, 100, 70);
	contentPane.add(recipeBtn);
	
	stuffTf.setBounds(50,480,450,70);
	stuffTf.setFont(new Font("맑은고딕", Font.BOLD, 30));
	contentPane.add(stuffTf);
	
	sale.setBounds(570, 95, 160, 70);
	contentPane.add(sale);
	
	insert.setBounds(570, 167, 160, 70);
	contentPane.add(insert);
	
	
	
	DefaultTableModel model = new DefaultTableModel();
	    model.addColumn("id");
	    model.addColumn("메뉴");
	    model.addColumn("수량");
	    model.addColumn("가격");
	jt1 = new JTable(model);
	jt1.setBounds(25,20,500,500);
	jt1.setRowHeight(50);
	jt1.getTableHeader().setFont(new Font("맑은고딕", Font.BOLD, 15));
	contentPane.add(jt1);
	
	JScrollPane jsp = new JScrollPane(jt1);
	jsp.setBounds(25,20,500,500);
	contentPane.add(jsp);
	
	DefaultComboBoxModel combomodel = combo_model_update();
	comboBox = new JComboBox(combomodel);
	comboBox.setBounds(570, 23, 160, 70);
	
	contentPane.add(comboBox);
	
	
	
	
	insert.addActionListener(this);
	   allcancelBtn.addActionListener(this);
	   cancelBtn.addActionListener(this);
	   resultBtn.addActionListener(this);
	   recipeBtn.addActionListener(this);

}
public void actionPerformed(ActionEvent e) {
       Object obj = e.getSource();

       String item = comboBox.getSelectedItem().toString(); //필드에서 아이템 가져오기
       String qty = sale.getText(); //수량가져오기
       String stockprice = dao.getprice(item);//재고상품 가격
       String stock = dao.getStock(item);//상품재고 수량
       DefaultTableModel model = (DefaultTableModel) jt1.getModel();
       DefaultComboBoxModel combomodel = (DefaultComboBoxModel) comboBox.getModel();
     
       if (obj == insert) {


           int index = model.getRowCount() + 1;
           if (qty.equals("") || item.equals("")) {
               JOptionPane.showMessageDialog(null, "상품이나 수량을 입력해주세요", "경고!!", JOptionPane.WARNING_MESSAGE);
           } else if (Integer.parseInt(qty) > Integer.parseInt(stock)) {
               JOptionPane.showMessageDialog(null, "물품의 수량이 남은 재고수량 보다 많습니다.", "경고!!", JOptionPane.WARNING_MESSAGE);
           } else {
               int price = Integer.parseInt(qty) * Integer.parseInt(stockprice);

               Vector<String> in = makeInVector(new String[]{(String.valueOf(index)), item, qty, String.valueOf(price)});
               int total = 0;
               


               if (!isCheck(item, model)) {
                   JOptionPane.showMessageDialog(null, "이미 선택한 상품입니다.", "경고!!", JOptionPane.WARNING_MESSAGE);
               } else {
                   //중복검사후 jtable에 요소 추가
                   model.addRow(in);
               }

               //장바구니 총합구하기
               for (int i = 0; i < model.getRowCount(); i++) {
                   total = total + Integer.parseInt((String) model.getValueAt(i, 3));
                   stuffTf.setText(String.valueOf(total));
               }

           }
       }
       else if (obj == allcancelBtn) { //새로고침 버튼 클릭
           combomodel.removeAllElements();
           try {
               DefaultComboBoxModel defaultComboBoxModel = combo_model_update();        
               for (int i = 0; i < defaultComboBoxModel.getSize(); i++) {
                   String a = (String) defaultComboBoxModel.getElementAt(i);
                   combomodel.addElement(a);
               
                   
               }
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }

       }
       else if (obj == cancelBtn) {//취소버튼 클릭
           int result;
           result = JOptionPane.showConfirmDialog(null, "주문을 취소하시겠습니까?");
           //예를 눌렀을시
           if (result == 0) {
               clear();
           }
       } else if (obj == resultBtn) { //결제버튼 클릭
           if (stuffTf.getText().equals("") || stuffTf.getText().equals(null)) {
               JOptionPane.showMessageDialog(null, "상품을 선택해주세요", "경고!!", JOptionPane.WARNING_MESSAGE);
           } else {
               int total = Integer.parseInt(stuffTf.getText()); //지불해야할 금액
               int input; //지불한금액
               int res; //거스름돈
               int result;

               result = JOptionPane.showConfirmDialog(null, "결제 하시겠습니까?");
               //예를 눌렀을시
               if (result == 0) {
                   input = Integer.parseInt(JOptionPane.showInputDialog("총 금액은" + total + "입니다.\n얼마를 지불하시겠습니까?"));

                   if (input >= total) {
                       res = input - total;
                       JOptionPane.showMessageDialog(null, "지불하신 금액은" + input + "\n 상품의 합계는" + total + "\n 거스름돈은" + res + "입니다");
                       stock_update(model);
                   }
               } else {
                   JOptionPane.showMessageDialog(null, "결제를 취소하였습니다");

               }

           }
       }else {
        Choose cs= new Choose();
                dispose();
               
       }
   }

   //String을 vector로 변환
   private Vector<String> makeInVector(String[] array) {
       Vector<String> in = new Vector<>();
       for (String data : array) {
           in.add(data);
       }
       return in;
   }

   //Jtable의 모든 요소 삭제
   private void clear() {
       DefaultTableModel model = (DefaultTableModel) jt1.getModel();
       stuffTf.setText("");
       sale.setText("");
       int rows = model.getRowCount();
       for (int i = rows - 1; i >= 0; i--) {
           model.removeRow(i);
       }

   }

   //중복검사
   private boolean isCheck(String text, DefaultTableModel model) {
       boolean check = true;

       for (int i = 0; i < model.getRowCount(); i++) {
           if (text.equals(model.getValueAt(i, 1))) {
               check = false;
           }
       }
       return check;

   }

   //재고 업데이트
   private void stock_update(DefaultTableModel model) {
       String out_itemname = null;
       String out_itemstock = null;
       String total_stock;
       String itemname = null;
       String itemstock = null;
       String itemprice = null;
       String id = null;
     
       
       for (int i = 0; i < model.getRowCount(); i++) {
           out_itemname = (String) model.getValueAt(i, 1);
           out_itemstock = (String) model.getValueAt(i, 2);
           total_stock = dao.getStock(out_itemname);
           dao.updateStock(total_stock, out_itemstock, out_itemname);
       }
       for (int i = 0; i < model.getRowCount(); i++) {
           itemname = (String) model.getValueAt(i, 1);
           itemstock = (String) model.getValueAt(i, 2);
           itemprice = (String) model.getValueAt(i, 3);                  
           dao.insertStock(itemname, itemstock, itemprice, date, time, id);
       }
       clear();
       
   }
   


   //table 업데이트
   private DefaultComboBoxModel combo_model_update() throws SQLException {
       DefaultComboBoxModel combomodel = new DefaultComboBoxModel(dao.getItem());
       return combomodel;
   }
}
