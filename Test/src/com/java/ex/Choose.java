package com.java.ex;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Choose extends JFrame {
	JLabel gs = new JLabel("GS25");
	JButton PosBtn = new JButton("상품 판매");
	JButton StockBtn = new JButton("제고 관리");
	JButton ReciepeBtn = new JButton("영수증");
    JPanel contentPane = new JPanel();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Choose frame = new Choose();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	//화면구성및 이벤트
	public Choose(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(380, 250);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		gs.setBounds(160,
				0,100,100);
		gs.setFont(new Font("맑은고딕", Font.BOLD, 17));
		contentPane.add(gs);
		
		PosBtn.setBounds(20,80,100,100);
		contentPane.add(PosBtn);
		
		StockBtn.setBounds(130,80,100,100);
		contentPane.add(StockBtn);
		
		ReciepeBtn.setBounds(240,80,100,100);
		contentPane.add(ReciepeBtn);
		
		//상품판매 버튼 눌렀을떄 이동
		PosBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						MainPos frame = new MainPos();
						dispose();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					}
				});
		//제고관리 버튼 눌렀을떄 이동
		StockBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					Mtable frame = new Mtable();
					dispose();
					
					}
				});
		//영수증 버튼 눌렀을떄 이동
		ReciepeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					Receipt frame = new Receipt();
					dispose();
					
					}
				});
		setVisible(true);
			
		}
		
	
	

}
