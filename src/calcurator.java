import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class CalcFrame extends JFrame implements ActionListener{
	JLabel label = new JLabel(""); //計算結果を表示する
	JButton[] button = new JButton[20]; //ボタンを格納する配列
	JPanel panel = new JPanel(); //ボタンを乗せるパネル
	String str = ""; //数字を文字列として格納する変数
	String s; //計算する際、演算子を格納する
	double d1; //計算する際、数値を格納する
	double d2; //計算する際、数値を格納する
	double operand = 0; //文字列から数字に変換して格納する変数
	ArrayList<String> box = new ArrayList<>(); //数字等を文字列として先入れ後出しする箱
	ArrayList<String> subBox = new ArrayList<>(); //補助用の箱。演算子を一時保管する
	ArrayList<ArrayList> lists = new ArrayList<>();

	
	
	CalcFrame(){
		setTitle("電卓");
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
		//アクションリスナーを有効にする
		for(int i = 0; i <= 19; i++) {
			button[i].addActionListener(this);
		}
		//フレームを表示する
		setSize(350,400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i <= 19; i++) {
			if(e.getSource() == button[i]){
				//押されたボタンの数字等をラベルに表示する
				if(i != 16) { 
					label.setText(label.getText() + button[i].getText());
					System.out.println("押されたキーは" + button[i].getText());
				}
				//数字または小数点が押され続ける限り、それを文字列として格納する
				if(0 <= i && i <= 10) { 
					//文字列が小数点のみなら先頭に0を付ける
					if(str == ".") {
						str = 0 + str;
					}
					str = str + button[i].getText();
					//小数点は一回しか押せない
					if(i == 10) { 
						button[10].setEnabled(false);
					}
					System.out.println("strは" + str);
				}
				//イコールを押したら答えを表示する。すべての変数をリセットする
				else if(i == 11) {
					//計算する
					if(str != "") {//strが空でない必要がある
						box.add(str);
						box.add(subBox.get(subBox.size()-1));
						subBox.remove(subBox.size()-1);
						d1 = Double.parseDouble(box.get(box.size()-3));
						d2 = Double.parseDouble(box.get(box.size()-2));
						s = box.get(box.size()-1);
						box.remove(box.size()-1);
						box.remove(box.size()-1);
						box.remove(box.size()-1);
						System.out.println("d1は" + d1 + "、d2は" + d2 + "、sは" + s);
						operand = calc(d1, d2, s);
						if(subBox.size() > 0) {
							System.out.println("operandは" + operand);
							str = String.valueOf(operand);
							box.add(str);
							box.add(subBox.get(subBox.size()-1));
							d1 = Double.parseDouble(box.get(box.size()-3));
							d2 = Double.parseDouble(box.get(box.size()-2));
							s = box.get(box.size()-1);
							System.out.println("d1は" + d1 + "、d2は" + d2 + "、sは" + s);
							operand = calc(d1, d2, s);
						}
						//答えを表示
						label.setText(label.getText() + operand);
						System.out.println("答えは" + operand);
						//変数リセット
						str = "";
						operand = 0;
						box.clear();
						subBox.clear();
						button[10].setEnabled(true);
					}
				}
				//演算子が押されたら、文字列をboxへ格納し、文字列を初期化。演算子はsubBoxへ格納
				//subBoxへ格納された演算子が、直前にsubBoxへ格納された演算子より優先順位が低ければ、直前の演算子をboxへ格納
				//box内に演算子が格納されたとき、文字列を最後から3つ取り出して計算し、boxへ戻す
				else if(12 <= i && i <= 15) {
					if(str != "") {//strが空でない必要がある
						box.add(str);
						str = ""; //文字列初期化
						button[10].setEnabled(true); //小数点ボタン有効化
						subBox.add(button[i].getText());
						if(subBox.size() > 1) { //subBoxに二つ以上の演算子が格納されている場合
							//+または-を押した場合かつ直前の演算子が×または÷の場合
							if((i ==12 || i ==13) && (subBox.get(subBox.size()-2) == "×" || subBox.get(subBox.size()-2) == "÷")) {
								process();
								//subBoxに+または-の演算子が二つ並んだ場合
								if(subBox.size() > 1) {
									if((subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-") &&
										(subBox.get(subBox.size()-1) == "+" || subBox.get(subBox.size()-1) == "-")) {
										process();
									}
								}
							}
							//+または-を押した場合かつ直前の演算子が+または-の場合
							else if((i ==12 || i ==13) && (subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-")) {
								process();
							}
							//×または÷を押した場合かつ直前の演算子が×または÷の場合
							else if((i ==14 || i ==15) && (subBox.get(subBox.size()-2) == "×" || subBox.get(subBox.size()-2) == "÷")) {
								process();
								//subBoxに+または-の演算子が二つ並んだ場合
								if(subBox.size() > 1) {
									if((subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-") &&
										(subBox.get(subBox.size()-1) == "+" || subBox.get(subBox.size()-1) == "-")) {
										process();
									}
								}
							}
						}
					}
					
				}else if(i == 16) {
					str = "";
					s = ""; 
					d1 = 0; 
					d2 = 0; 
					operand = 0;
					box.clear();
					subBox.clear();
					button[10].setEnabled(true);
					label.setText("");
				}else if(i == 18) {
					
				}else if(i == 19) {
					
				}
			}	
		}
		if(box.size() > 0) {
			for(int j = 0; j < box.size(); j++) {
				System.out.println("boxの" + j + "番目は" + box.get(j));
			}
		}
		if(subBox.size() > 0) {
			for(int j = 0; j < subBox.size(); j++) {
				System.out.println("subBoxの" + j + "番目は" + subBox.get(j));
			}
		}		
	}
	
	
	public void process() {
		box.add(subBox.get(subBox.size()-2));
		subBox.remove(subBox.size()-2);
		d1 = Double.parseDouble(box.get(box.size()-3));
		d2 = Double.parseDouble(box.get(box.size()-2));
		s = box.get(box.size()-1);
		box.remove(box.size()-1);
		box.remove(box.size()-1);
		box.remove(box.size()-1);
		System.out.println("d1は" + d1 + "、d2は" + d2 + "、sは" + s);
		operand = calc(d1, d2, s);
		System.out.println("operandは" + operand);
		str = String.valueOf(operand);
		box.add(str);
		str = "";
	}
	
	public double calc(double d1, double d2, String s) {
		double d = 0;
		switch(s) {
		case "+":
			System.out.println("case+が実行されました");
			d =  d1 + d2;
			break;
		case "-":
			System.out.println("case-が実行されました");
			d = d1 - d2;
			break;
		case "×":
			System.out.println("case×が実行されました");
			d = d1 * d2;
			break;
		case "÷":
			System.out.println("case÷が実行されました");
			d = d1 / d2;
			break;
		}
		return d;
	}
	
}
public class calcurator {
	
	public static void main(String[] args) {
		new CalcFrame();
	}
}
