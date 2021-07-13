package com.java.ex;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.Time;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Receipt extends JFrame  implements ActionListener  {
	
	
	JButton jbRefundBtn = new JButton("반품");
	JButton jbRefundBtn2 = new JButton("선택반품");
	JButton backBtn = new JButton("뒤로가기");
	JTextArea jta = new JTextArea();
	JTable jt;
	JTextArea jtf = new JTextArea();
	JPanel contentPane = new JPanel();
	
	SaleStockDAO dao = new SaleStockDAO();
	
	
	
	
	//화면 구성및 이벤트
	public Receipt(){
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		setSize(700,450);
		setVisible(true);
		setLayout(null);
			   
		
		 
		 DefaultTableModel model = new DefaultTableModel();	
	     model.addColumn("메뉴");
	     model.addColumn("수량");
	     model.addColumn("가격");
	     model.addColumn("날짜");
	     model.addColumn("시간");
	     model.addColumn("번호");
	     
	     
		jt = new JTable(model);
		jt.setBounds(25,20,400,370);
		jt.setRowHeight(40);
		jt.getTableHeader().setFont(new Font("맑은고딕", Font.BOLD, 15));
		contentPane.add(jt);
		
		//테이블 오름 차순 내림차순
		jt.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jt.getModel());
		jt.setRowSorter(sorter);
		
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setBounds(25,20,400,350);
		contentPane.add(jsp);
		

		jta.setBounds(450,20,200,200);
		jta.setFont(new Font("맑은고딕", Font.BOLD, 15));
		contentPane.add(jta);
		
		JScrollPane jsp1 = new JScrollPane(jta);
		jsp1.setBounds(450,20,200,200);
		contentPane.add(jsp1);
		
		jtf.setBounds( 450,230,100,60);		
		jtf.setFont(new Font("맑은고딕", Font.BOLD, 15));
		contentPane.add(jtf);
		
		
		

		jbRefundBtn.setBounds(550,230,100,60);
		contentPane.add(jbRefundBtn);
		
		jbRefundBtn2.setBounds(450,290,100,60);
		contentPane.add(jbRefundBtn2);

		backBtn.setBounds(550,290,100,60);
		contentPane.add(backBtn);

	
		
		dao.SelectAll2(model);
		
		backBtn.addActionListener(this); // 뒤로가기 이벤트 등록
		jbRefundBtn.addActionListener(this); // 반품 이벤트등록
		jbRefundBtn2.addActionListener(this); // 선택반품 이벤트등록
		
		//상품 수량 텍스트 클릭시
		jt.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				  if (e.getSource().equals(jt)) {
					  
						int row = jt.getSelectedRow();// 선택된 행
					//영수증 정보 
							jta.setText( "GS25" + "\n" + "영수증" + "\n" + "영수증 번호 : " +  jt.getValueAt(row,5).toString() + "\n" +
									"날짜 : " +  jt.getValueAt(row,3).toString() + "/" +  jt.getValueAt(row,4).toString() + "\n" +
									"상품명 : " + jt.getValueAt(row,0).toString()+ "\n"  + 
									"수량 : " + jt.getValueAt(row,1).toString() + "\n"+
									"가격 : " + jt.getValueAt(row,2).toString() + "\n");
							
							
							int total = 0;
							
							for (int i = 0; i < model.getRowCount(); i++) {
								total = total + Integer.parseInt(String.valueOf(model.getValueAt(i, 2)));
								jtf.setText("판매 총액" + "\n" +String.valueOf(total));
							}
							
							
			        }
			      
				
			}
		});
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		DefaultTableModel model = (DefaultTableModel) jt.getModel();
		//반품 버튼 액션
		if(obj == jbRefundBtn) {
	
	          int row = jt.getSelectedRow();
		          String out_itemname = (String) model.getValueAt(row, 0);
		          String out_itemstock = (String) model.getValueAt(row, 1);
		       
		          
		          String total_stock = dao.getStock(out_itemname);
		          dao.updateStock2(total_stock, out_itemstock, out_itemname);
		 
						

			Object obj1 = jt.getValueAt(row, 4);// 행 열에 해당하는 value
			Object obj2 = jt.getValueAt(row, 3);

			if (dao.Delete2(Time.valueOf((String) obj1),Date.valueOf((String) obj2) ) > 0) {
				
				
				 JOptionPane.showMessageDialog(this, "상품 반품되었습니다.");

				
				dao.SelectAll2(model);
				if (model.getRowCount() > 0)
					jt.setRowSelectionInterval(0, 0);
				

			} else {
				 JOptionPane.showMessageDialog(this, "상품 반품되지 않았습니다.");
			}
		}else if(obj == backBtn) {//취소 버튼 액션
			Choose cs = new Choose();
			dispose();
		}else if(obj == jbRefundBtn2){
			 int row = jt.getSelectedRow();
	          String out_itemname = (String) model.getValueAt(row, 0);
	          String out_itemstock = (String) model.getValueAt(row, 1);
	          
	         
	          
	          String total_stock = dao.getStock(out_itemname);
	          dao.updateStock2(total_stock, out_itemstock, out_itemname);
	 
		Object obj1 = jt.getValueAt(row, 4);// 행 열에 해당하는 value
		Object obj2 = jt.getValueAt(row, 3);

		Object obj5 = jt.getValueAt(row, 0);

		if (dao.Delete3(Time.valueOf((String) obj1),Date.valueOf((String) obj2), obj5.toString()) > 0) {
			
			
			 JOptionPane.showMessageDialog(this, "상품 반품되었습니다.");

			
			dao.SelectAll2(model);
			if (model.getRowCount() > 0)
				jt.setRowSelectionInterval(0, 0);

		} else {
			 JOptionPane.showMessageDialog(this, "상품 반품되지 않았습니다.");
		}
		}
			
	}

	 
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Receipt frame = new Receipt();
					
					
				} catch (Exception e) {
					System.out.println("문제 있다");
					e.printStackTrace();
				}
			}
		});
	}
	
	  
	private void time(DefaultTableModel model) {
		
		Time time = null;
		Date date = null;
		

	
		for (int i = 0; i < model.getRowCount(); i++) {

			
			time = (Time) model.getValueAt(i, 5);
			date = (Date) model.getValueAt(i, 4);
			dao.Delete2(time,date);
		}

		
	}

	
	
}
