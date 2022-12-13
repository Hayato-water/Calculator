import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class CalcFrame extends JFrame implements ActionListener{
	JLabel label = new JLabel("0"); //計算結果を表示する
	JButton[] button = new JButton[20]; //ボタンを格納する配列
	JPanel panel = new JPanel(); //ボタンを乗せるパネル

	CalcFrame(){
		setTitle("メモ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//各種ボタンの作成
		for(int i = 0; i <= 9; i++) {
			button[i] = new JButton("" + i + "");
		}
		button[10] = new JButton(".");
		button[11] = new JButton("=");
		button[12] = new JButton("+");
		button[13] = new JButton("-");
		button[14] = new JButton("×");
		button[15] = new JButton("÷");
		button[16] = new JButton("AC");
		button[17] = new JButton("%");
		button[18] = new JButton("(");
		button[19] = new JButton(")");
		//パネルを作成し、ボタンをパネルに乗せる
		panel.setLayout(new GridLayout(5,4)); //ボタンの順番、横に4回のち下の列へ
		panel.add(button[16]);
		panel.add(button[18]);
		panel.add(button[19]);
		panel.add(button[17]);
		panel.add(button[7]);
		panel.add(button[8]);
		panel.add(button[9]);
		panel.add(button[15]);
		panel.add(button[4]);
		panel.add(button[5]);
		panel.add(button[6]);
		panel.add(button[14]);
		panel.add(button[1]);
		panel.add(button[2]);
		panel.add(button[3]);
		panel.add(button[13]);
		panel.add(button[0]);
		panel.add(button[10]);
		panel.add(button[11]);
		panel.add(button[12]);
		//コンテナをボックスレイアウトとし、ラベルとパネルをコンテナに乗せる。
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		label.setPreferredSize(new Dimension(350, 100));
		getContentPane().add(label);
		getContentPane().add(panel);
		//フレームを表示する
		setSize(350,400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		
		
	}
	
}
public class calcurator {
	
	public static void main(String[] args) {
		new CalcFrame();
	}
}
