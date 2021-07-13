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
	
	
	JButton jbRefundBtn = new JButton("��ǰ");
	JButton jbRefundBtn2 = new JButton("���ù�ǰ");
	JButton backBtn = new JButton("�ڷΰ���");
	JTextArea jta = new JTextArea();
	JTable jt;
	JTextArea jtf = new JTextArea();
	JPanel contentPane = new JPanel();
	
	SaleStockDAO dao = new SaleStockDAO();
	
	
	
	
	//ȭ�� ������ �̺�Ʈ
	public Receipt(){
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(contentPane);
		setSize(700,450);
		setVisible(true);
		setLayout(null);
			   
		
		 
		 DefaultTableModel model = new DefaultTableModel();	
	     model.addColumn("�޴�");
	     model.addColumn("����");
	     model.addColumn("����");
	     model.addColumn("��¥");
	     model.addColumn("�ð�");
	     model.addColumn("��ȣ");
	     
	     
		jt = new JTable(model);
		jt.setBounds(25,20,400,370);
		jt.setRowHeight(40);
		jt.getTableHeader().setFont(new Font("�������", Font.BOLD, 15));
		contentPane.add(jt);
		
		//���̺� ���� ���� ��������
		jt.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(jt.getModel());
		jt.setRowSorter(sorter);
		
		JScrollPane jsp = new JScrollPane(jt);
		jsp.setBounds(25,20,400,350);
		contentPane.add(jsp);
		

		jta.setBounds(450,20,200,200);
		jta.setFont(new Font("�������", Font.BOLD, 15));
		contentPane.add(jta);
		
		JScrollPane jsp1 = new JScrollPane(jta);
		jsp1.setBounds(450,20,200,200);
		contentPane.add(jsp1);
		
		jtf.setBounds( 450,230,100,60);		
		jtf.setFont(new Font("�������", Font.BOLD, 15));
		contentPane.add(jtf);
		
		
		

		jbRefundBtn.setBounds(550,230,100,60);
		contentPane.add(jbRefundBtn);
		
		jbRefundBtn2.setBounds(450,290,100,60);
		contentPane.add(jbRefundBtn2);

		backBtn.setBounds(550,290,100,60);
		contentPane.add(backBtn);

	
		
		dao.SelectAll2(model);
		
		backBtn.addActionListener(this); // �ڷΰ��� �̺�Ʈ ���
		jbRefundBtn.addActionListener(this); // ��ǰ �̺�Ʈ���
		jbRefundBtn2.addActionListener(this); // ���ù�ǰ �̺�Ʈ���
		
		//��ǰ ���� �ؽ�Ʈ Ŭ����
		jt.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				  if (e.getSource().equals(jt)) {
					  
						int row = jt.getSelectedRow();// ���õ� ��
					//������ ���� 
							jta.setText( "GS25" + "\n" + "������" + "\n" + "������ ��ȣ : " +  jt.getValueAt(row,5).toString() + "\n" +
									"��¥ : " +  jt.getValueAt(row,3).toString() + "/" +  jt.getValueAt(row,4).toString() + "\n" +
									"��ǰ�� : " + jt.getValueAt(row,0).toString()+ "\n"  + 
									"���� : " + jt.getValueAt(row,1).toString() + "\n"+
									"���� : " + jt.getValueAt(row,2).toString() + "\n");
							
							
							int total = 0;
							
							for (int i = 0; i < model.getRowCount(); i++) {
								total = total + Integer.parseInt(String.valueOf(model.getValueAt(i, 2)));
								jtf.setText("�Ǹ� �Ѿ�" + "\n" +String.valueOf(total));
							}
							
							
			        }
			      
				
			}
		});
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		DefaultTableModel model = (DefaultTableModel) jt.getModel();
		//��ǰ ��ư �׼�
		if(obj == jbRefundBtn) {
	
	          int row = jt.getSelectedRow();
		          String out_itemname = (String) model.getValueAt(row, 0);
		          String out_itemstock = (String) model.getValueAt(row, 1);
		       
		          
		          String total_stock = dao.getStock(out_itemname);
		          dao.updateStock2(total_stock, out_itemstock, out_itemname);
		 
						

			Object obj1 = jt.getValueAt(row, 4);// �� ���� �ش��ϴ� value
			Object obj2 = jt.getValueAt(row, 3);

			if (dao.Delete2(Time.valueOf((String) obj1),Date.valueOf((String) obj2) ) > 0) {
				
				
				 JOptionPane.showMessageDialog(this, "��ǰ ��ǰ�Ǿ����ϴ�.");

				
				dao.SelectAll2(model);
				if (model.getRowCount() > 0)
					jt.setRowSelectionInterval(0, 0);
				

			} else {
				 JOptionPane.showMessageDialog(this, "��ǰ ��ǰ���� �ʾҽ��ϴ�.");
			}
		}else if(obj == backBtn) {//��� ��ư �׼�
			Choose cs = new Choose();
			dispose();
		}else if(obj == jbRefundBtn2){
			 int row = jt.getSelectedRow();
	          String out_itemname = (String) model.getValueAt(row, 0);
	          String out_itemstock = (String) model.getValueAt(row, 1);
	          
	         
	          
	          String total_stock = dao.getStock(out_itemname);
	          dao.updateStock2(total_stock, out_itemstock, out_itemname);
	 
		Object obj1 = jt.getValueAt(row, 4);// �� ���� �ش��ϴ� value
		Object obj2 = jt.getValueAt(row, 3);

		Object obj5 = jt.getValueAt(row, 0);

		if (dao.Delete3(Time.valueOf((String) obj1),Date.valueOf((String) obj2), obj5.toString()) > 0) {
			
			
			 JOptionPane.showMessageDialog(this, "��ǰ ��ǰ�Ǿ����ϴ�.");

			
			dao.SelectAll2(model);
			if (model.getRowCount() > 0)
				jt.setRowSelectionInterval(0, 0);

		} else {
			 JOptionPane.showMessageDialog(this, "��ǰ ��ǰ���� �ʾҽ��ϴ�.");
		}
		}
			
	}

	 
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Receipt frame = new Receipt();
					
					
				} catch (Exception e) {
					System.out.println("���� �ִ�");
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
