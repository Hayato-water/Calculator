import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class CalcFrame extends JFrame implements ActionListener{
	JLabel label = new JLabel(""); //計算結果を表示する
	JButton[] button = new JButton[20]; //ボタンを格納する配列
	JPanel panel = new JPanel(); //ボタンを乗せるパネル
	String str = ""; //数字を文字列として格納する変数
	String s; //計算する際、演算子を格納する
	double d1; //計算する際、数値を格納する
	double d2; //計算する際、数値を格納する
	double operand = 0; //文字列から数字に変換して格納する変数
	int flag = 0; //「(」を押した後のみ「)」を押せるようにするためのフラグ
	ArrayList<String> box = new ArrayList<>(); //数字等を文字列として先入れ後出しする箱
	ArrayList<String> subBox = new ArrayList<>(); //補助用の箱。演算子を一時保管する。先入れ後出し
	//なお、便宜上、箱の後ろからn番目に格納された要素を、箱の上からn番目にあると表現している

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
		panel.setLayout(new GridLayout(5,4));
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
		label.setPreferredSize(new Dimension(Short.MAX_VALUE, 100));
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setHorizontalAlignment(JLabel.LEFT);
		System.out.println(label.getHorizontalAlignment());
		label.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 16));
		getContentPane().add(label);
		getContentPane().add(panel);
		//アクションリスナーを有効にする
		for(int i = 0; i <= 19; i++) {
			button[i].setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
			button[i].addActionListener(this);
		}
		//初めは、数字、「AC」「(」しか押せないようにしておく
		for(int i = 10; i <= 15; i++) {
			button[i].setEnabled(false);
		}
		button[17].setEnabled(false);
		button[19].setEnabled(false);
		//フレームを表示する
		setSize(350,400);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		//どのボタンが押されたか順番に確認する
		for(int i = 0; i <= 19; i++) {
			if(e.getSource() == button[i]){
				//「AC」以外のボタンを押したのであればラベルに表示する
				if(i != 16) { 
					label.setText(label.getText() + button[i].getText());
					System.out.println("押されたキーは" + button[i].getText());
				}
				//数字が押された場合
				if(0 <= i && i <= 9) { 
					//連続して数字が押された場合、それをひと続きの文字列（ひとつの数字）として格納する
					str = str + button[i].getText();
					System.out.println("strは" + str);
				}
				//小数点が押された場合
				else if(i == 10) { 
					//文字列に小数点を加える
					str = str + button[i].getText();
					System.out.println("strは" + str);
				}
				//「=」を押した場合。答えを表示する。すべての変数をリセットする
				else if(i == 11) {
					//strが空でない必要がある
					if(str != "") {
						//subBoxが空でない場合
						if(subBox.size() > 0) {
							//boxに文字列を格納
							box.add(str);
							//計算処理
							//引数「1」はsubBoxの上から1番目の演算子を使うことを指す
							process(1);
							//subBoxに演算子がまだ残っている場合、もう一度計算する
							if(subBox.size() > 0) {
								//計算処理
								process(1);
							}
							//operandが整数ならば、double型からint型へキャストし、整数部のみで答えを表示する
							//小数点以下を切り捨てたものが元の数値と同じであれば整数と判断できる
							if(Math.floor(operand) == operand) {
								//答えを表示
								label.setText(label.getText() + (int)operand);
								System.out.println("答えは" + (int)operand);
							}else {
								//答えを表示
								label.setText(label.getText() + operand);
								System.out.println("答えは" + operand);
							}
						}
						//subBoxが空（演算子がない）の場合。operandに格納されている数値がそのまま答えになる
						else {
							//operandが整数ならば、double型からint型へキャストし、整数部のみで答えを表示する
							//小数点以下を切り捨てたものが元の数値と同じであれば整数と判断できる
							if(Math.floor(operand) == operand) {
								//答えを表示
								label.setText(label.getText() + (int)operand);
								System.out.println("答えは" + (int)operand);
							}else {
								//答えを表示
								label.setText(label.getText() + operand);
								System.out.println("答えは" + operand);
							}
						}
						//変数リセット
						str = "";
						operand = 0;
						box.clear();
						subBox.clear();
						button[10].setEnabled(true);
					}
				}
				//演算子が押された場合。文字列をboxへ格納し、文字列を初期化。演算子はsubBoxへ格納
				//今subBoxへ格納した演算子と、ひとつ前にsubBoxへ格納した演算子の組み合わせにより場合分けし、計算処理↓を行う
				//ひとつ前の演算子をboxへ格納し、boxから要素を上から3つ取り出して計算し、boxへ戻す（processメソッド）
				else if(12 <= i && i <= 15) {
					if(str != "") {//strが空でない必要がある
						//文字列をboxに格納
						box.add(str);
						//文字列初期化
						str = ""; 
						//小数点ボタン有効化
						button[10].setEnabled(true);
						//押された演算子をsubBoxへ格納
						subBox.add(button[i].getText());
						//subBoxに2つ以上の演算子が格納されている場合
						if(subBox.size() > 1) { 
							//+または-を押した場合かつひとつ前の演算子が×または÷の場合
							if((i ==12 || i ==13) && (subBox.get(subBox.size()-2) == "×" || subBox.get(subBox.size()-2) == "÷")) {
								//+,-は×,÷より優先順位が低いため、processメソッドで計算処理を行う
								//引数「2」はsubBoxの上から2番目の演算子を使うことを指す
								process(2);
								//subBoxから×または÷の演算子が取り出されたことにより、subBoxに+または-の演算子が2つ並んだ場合
								if(subBox.size() > 1) {
									if((subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-") &&
										(subBox.get(subBox.size()-1) == "+" || subBox.get(subBox.size()-1) == "-")) {
										//計算処理
										process(2);
									}
								}
							}
							//+または-を押した場合かつひとつ前の演算子が+または-の場合
							else if((i ==12 || i ==13) && (subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-")) {
								//計算処理
								process(2);
							}
							//×または÷を押した場合かつ直前の演算子が×または÷の場合
							else if((i ==14 || i ==15) && (subBox.get(subBox.size()-2) == "×" || subBox.get(subBox.size()-2) == "÷")) {
								process(2);
								//subBoxに+または-の演算子が2つ並んだ場合
								if(subBox.size() > 1) {
									if((subBox.get(subBox.size()-2) == "+" || subBox.get(subBox.size()-2) == "-") &&
										(subBox.get(subBox.size()-1) == "+" || subBox.get(subBox.size()-1) == "-")) {
										//計算処理
										process(2);
									}
								}
							}
						}
					}
				}
				// 「AC」が押された場合。変数をリセットする
				else if(i == 16) {
					str = "";
					s = ""; 
					d1 = 0; 
					d2 = 0; 
					operand = 0;
					box.clear();
					subBox.clear();
					label.setText("");
				}
				//「%」が押された場合。strを100分の1にする
				else if(i == 17) {
					if(str != "") {
						operand = Double.parseDouble(str) * 0.01;
						str = String.valueOf(operand);
					}
				//「(」が押された場合。 subBoxに「(」を追加
				}else if(i == 18) {
					subBox.add(button[i].getText());
				//「)」が押された場合。
				//「)」は、カッコ内の数式における「=」と同じとみなせるので、「=」の時とほぼ同じ処理をする
				}else if(i == 19) {
					//strが空でない必要がある
					if(str != "") {
						box.add(str);
						//計算処理
						process(1);
						//processメソッドの最後でboxに格納してしまった要素を取り除く
						box.remove(box.size()-1);
						//strにoperandを格納する
						str = String.valueOf(operand);
						//subBoxの先頭が「(」でなければ、カッコ内の計算が終わっていないので、もう一度計算する
						if(subBox.get(subBox.size()-1) != "(") {
							box.add(str); 
							//計算処理
							process(1);
							//processメソッドの最後でboxに格納してしまった要素を取り除く
							box.remove(box.size()-1);
							//strにoperandを格納する
							str = String.valueOf(operand);
						} 
						//subBoxの「(」を消去
						subBox.remove(subBox.size()-1); 
						//小数点ボタン有効化
						button[10].setEnabled(true);
					}
				}
				
				///////////*以下、ボタンの有効化・無効化の処理*///////////
				
				//数字が押された場合
				if(0 <= i && i <= 9) { 
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//「(」「)」以外のボタンを有効化
					for(int j = 10; j <= 17; j++) {
						button[j].setEnabled(true);
					}
					//以下の条件の場合は「)」を有効化
					//「(」が押されている
					if(flag > 0) {
						//strが空でない
						if(str != "") {
							//subBoxの一番上に演算子が格納されている
							if(subBox.get(subBox.size()-1) == "+" || subBox.get(subBox.size()-1) == "-" || 
									subBox.get(subBox.size()-1) == "×" || subBox.get(subBox.size()-1) == "÷" ) {
								button[19].setEnabled(true);
							}
						}
					}
					//以下の条件の場合は「=」を無効化
					//「)」で閉じられていない
					if(flag != 0) {
						button[11].setEnabled(false);
					}
				}
				//小数点が押された場合
				else if(i == 10) { 
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//数字と「AC」を有効化
					for(int j = 0; j <= 9; j++) {
						button[j].setEnabled(true);
					}
					button[16].setEnabled(true);
				}
				//「=」を押した場合
				else if(i == 11) {
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//「AC」のみ有効化
					button[16].setEnabled(true);
				}
				//演算子が押された場合
				else if(12 <= i && i <= 15) {
					//いったんすべてのボタンを有効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(true);
					}
					//演算子、「=」「.」「%」「)」を無効化
					for(int j = 10; j <= 15; j++) {
						button[j].setEnabled(false);
					}
					button[17].setEnabled(false);
					button[19].setEnabled(false);
				}
				// 「AC」が押された場合
				else if(i == 16) {
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//数字、「AC」「(」のみ有効化
					for(int j = 0; j <= 9; j++) {
						button[j].setEnabled(true);
					}
					button[16].setEnabled(true);
					button[18].setEnabled(true);
				}
				//「%」が押された場合
				else if(i == 17) {
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//演算子、「=」「)」のみ有効化
					//なお、「)」を押せるかどうかは数字を押した際にコントロール済み
					for(int j = 11; j <= 15; j++) {
						button[j].setEnabled(true);
					}
					//以下の条件の際は「=」を無効化
					//「)」で閉じられていない
					if(flag != 0) {
						button[11].setEnabled(false);
					}
				}
				//「(」が押された場合
				else if(i == 18) {
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//数字、「AC」「(」のみ有効化
					for(int j = 0; j <= 9; j++) {
						button[j].setEnabled(true);
					}
					button[16].setEnabled(true);
					button[18].setEnabled(true);
					//「)」を押せるようにする
					flag++;
				//「)」が押された場合
				}else if(i == 19) {
					//いったんすべてのボタンを無効化
					for(int j = 0; j <= 19; j++) {
						button[j].setEnabled(false);
					}
					//演算子、「=」「%」「AC」「)」のみ有効化
					for(int j = 11; j <= 15; j++) {
						button[j].setEnabled(true);
					}
					button[16].setEnabled(true);
					button[17].setEnabled(true);
					button[19].setEnabled(true);
					//「)」で閉じきったら「)」を押せないようにする
					flag--;
					if(flag == 0) {
						button[19].setEnabled(false);
					}
					//以下の条件の際は「=」を押せない
					//「)」で閉じられていない
					if(flag != 0) {
						button[11].setEnabled(false);
					}
				}
			}	
		}
		//box、subBoxに格納されている要素を確認する
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
	
	//計算処理をするメソッド
	//引数xは、subBoxの上から何番目の演算子を使用するかを指定する
	public void process(int x) {
		//subBoxの上から2番目の演算子を取り出す
		box.add(subBox.get(subBox.size()-x));
		//取り出した要素を削除する
		subBox.remove(subBox.size()-x);
		//boxの上から3つの要素を取り出す
		d1 = Double.parseDouble(box.get(box.size()-3));
		d2 = Double.parseDouble(box.get(box.size()-2));
		s = box.get(box.size()-1);
		//取り出した要素を削除する
		box.remove(box.size()-1);
		box.remove(box.size()-1);
		box.remove(box.size()-1);
		System.out.println("d1は" + d1 + "、d2は" + d2 + "、sは" + s);
		//計算する
		operand = calc(d1, d2, s);
		System.out.println("operandは" + operand);
		str = String.valueOf(operand);
		box.add(str);
		str = "";
	}
	
	//実際に計算するメソッド
	public double calc(double d1, double d2, String s) {
		double d = 0;
		//演算子sによって場合分けする
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
public class Calcurator {
	
	public static void main(String[] args) {
		new CalcFrame();
	}
}