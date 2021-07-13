package com.java.ex;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DBJoin extends JFrame implements MouseListener {

	private JPanel contentPane;
	private JLabel lblJoin;
	private JButton joinBtn;
	private JButton sameBtn;
	private JButton cancleBtn;
	private JTextField tfUsername;
	private JTextField tfPassword;
	private JTextField tfName;
	private JTextField tfBirth;
	private JTextField tfPhone;
	
	//메인메소드
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DBJoin frame = new DBJoin();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//화면 구성 과 이벤트
	public DBJoin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(490, 490);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		lblJoin = new JLabel("회원가입");
		Font f1 = new Font("돋움", Font.BOLD, 20); 
		lblJoin.setFont(f1); 
		lblJoin.setBounds(159, 41, 101, 20);
		contentPane.add(lblJoin);
		
		JLabel lblPassword = new JLabel("password");
		lblPassword.setBounds(69, 163, 69, 20);
		contentPane.add(lblPassword);
		
		JLabel lblUsername = new JLabel("username");
		lblUsername.setBounds(69, 113, 69, 20);
		contentPane.add(lblUsername);
		
		JLabel lblName = new JLabel("name");
		lblName.setBounds(69, 210, 69, 20);
		contentPane.add(lblName);
		
		JLabel lblBirth = new JLabel("birth");
		lblBirth.setBounds(69, 257, 69, 20);
		contentPane.add(lblBirth);
		
		JLabel lblPhone = new JLabel("phone");
		lblPhone.setBounds(69, 304, 69, 20);
		contentPane.add(lblPhone);
		
		tfUsername = new JTextField();
		tfUsername.setColumns(10);
		tfUsername.setBounds(159, 106, 186, 35);
		contentPane.add(tfUsername);
		tfUsername.addMouseListener(this);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(159, 156, 186, 35);
		contentPane.add(tfPassword);
		tfPassword.addMouseListener(this);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(159, 203, 186, 35);
		contentPane.add(tfName);
		tfName.addMouseListener(this);
		
		tfBirth = new JTextField("ex)980907", 6);
		tfBirth.setColumns(10);
		tfBirth.setBounds(159, 250, 186, 35);
		contentPane.add(tfBirth);
		tfBirth.addMouseListener(this);
		
		tfPhone = new JTextField();
		tfPhone.setColumns(10);
		tfPhone.setBounds(159, 297, 186, 35);
		contentPane.add(tfPhone);
		tfPhone.addMouseListener(this);
		
		sameBtn = new JButton("중복확인");
		sameBtn.setBounds(350, 106, 100, 35);
		contentPane.add(sameBtn);
		sameBtn.addMouseListener(this);
		
		joinBtn = new JButton("가입");
		joinBtn.setBounds(190, 340, 60, 35);
		contentPane.add(joinBtn);
		joinBtn.addMouseListener(this);
		
		
		
		cancleBtn = new JButton("취소");
		cancleBtn.setBounds(252, 340, 60, 35);
		contentPane.add(cancleBtn);
		cancleBtn.addMouseListener(this);
		
		//취소 버튼 클릭시
		cancleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LoginUI ui = new LoginUI();
				dispose();
				dbClose();
			}
		});
		
		
		setVisible(true);
	
	}
	
	Statement stmt = null;
    ResultSet rs = null;
    String url = "jdbc:mariadb://localhost:3307/datadb"; 
    String sql = null;
    Properties info = null;
    Connection cnn = null;
  
    @Override
    public void mouseClicked(MouseEvent e) {
 
        //마우스 클릭시 공백 만들기
        if (e.getSource().equals(tfUsername)) {
            tfUsername.setText("");
        } else if (e.getSource().equals(tfPassword)) {
            tfPassword.setText("");
        } else if (e.getSource().equals(tfName)) {
            tfName.setText("");
        } else if (e.getSource().equals(tfBirth)) {
            tfBirth.setText("");
        } else if (e.getSource().equals(tfPhone)) {
        	tfPhone.setText("");
        }
 
        try {
            Class.forName("org.mariadb.jdbc.Driver"); 
            info = new Properties();
            info.setProperty("user", "root");
            info.setProperty("password", "skrkcb");
            cnn = DriverManager.getConnection(url, info);
            stmt = cnn.createStatement();
 
            if (e.getSource().equals(sameBtn)) {
                sql = "select * from joinDB where id='" + tfUsername.getText() + "'";
                rs = stmt.executeQuery(sql);
 
                if (rs.next() == true || (tfUsername.getText().isEmpty()) == true) {
                	JOptionPane.showMessageDialog(null, "해당 ID는 사용이 불가능합니다. 다시 작성해주세요.");
                } else {
                	JOptionPane.showMessageDialog(null, "사용 가능한 ID 입니다.");
                }
            }
 
            // 가입 버튼
            if (e.getSource().equals(joinBtn)) {
                sql = "select * from joinDB where ID='" + tfUsername.getText() + "'";
 
                rs = stmt.executeQuery(sql); 
 
                if (rs.next() == true) { 
                	JOptionPane.showMessageDialog(null, "ID Check가 필요합니다.");
 
                } else if ((tfUsername.getText().isEmpty()) == true || (tfPassword.getText().isEmpty()) == true
                        || (tfName.getText().isEmpty()) || (tfBirth.getText().isEmpty()) || (tfPhone.getText().isEmpty())) {        
                	JOptionPane.showMessageDialog(null, "비어있는 칸이 존재합니다.");
                } else if ((tfBirth.getText().length()) != 6) {
                	JOptionPane.showMessageDialog(null, "생년월일 서식이 잘못되었습니다.");    
                } else {
 
                	sql = "insert into joinDB values ('" + tfUsername.getText() + "','" + tfPassword.getText() + "','"
							+ tfName.getText() + "','" + tfBirth.getText() + "','" + tfPhone.getText() + "')";
                    stmt.executeUpdate(sql);
                    JOptionPane.showMessageDialog(null, "축하합니다.가입 되셨습니다.");
                    LoginUI frame = new LoginUI();
                    dispose();
                    dbClose();
                }
            }
        } catch (Exception ee) {
            System.out.println("문제있음");
            ee.printStackTrace();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    public void dbClose() {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (cnn != null)
                cnn.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    
    
 



}
