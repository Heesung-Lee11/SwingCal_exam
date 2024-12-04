import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.util.ArrayList;

public class Calculator extends JFrame{
	//계산에 쓰일 변수들 선언 
	private final int width= 500;	//계산기 창의 너비 상수
	private final int height= 470;	//계산기 창의 높이 상수
	static JLabel expression;		//표현식 출력 라벨
	static JLabel result;			//결과값 출력 라벨

	private String num="";
	private String prev_operation = "";
	private ArrayList<String> equation = new ArrayList<String>();
	
	//계산기 버튼에 쓰일 레이아웃
	private GridBagLayout grid = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	private Color darkColor = new Color(80, 82, 85);		//자주 쓰이는 색깔 정의
	TitledBorder tB = new TitledBorder(new LineBorder(darkColor, 1));
	//버튼추가할때 쓰이는 배열
	String button_names[] = {"C", "±","%", "÷", "7", "8", "9", "x","4", "5", "6", "-", "1", "2", "3","+","0", ".", "="};
	JButton buttons[] = new JButton[button_names.length];
	//계산기클래스 생성자
	public Calculator() {
		
		setLayout(null); //레이아웃배치를 자유롭게 하겠다
		
		JPanel resultPanel = new JPanel();	//표현식과 결과를 보여주는 Panel
		
		resultPanel.setLayout(null);	//라벨2개를 크기를 지정해서 하기위해 자유 배치 레이아웃
		resultPanel.setBackground(darkColor);
		resultPanel.setForeground(Color.white);
		resultPanel.setBounds(0, 0, width, 120);
		
		expression = new JLabel("");
		result= new JLabel("0");

		expression.setHorizontalAlignment(SwingConstants.RIGHT);  //정렬
		expression.setFont(new Font("Dialog", Font.PLAIN, 20)); // 글씨체
		expression.setForeground(Color.white);
	    expression.setBounds(0, 0, width-20, 50); // 위치,크기설정
	    expression.setBorder(new LineBorder(Color.gray, 0));	//테두리설정
				
	    result.setHorizontalAlignment(SwingConstants.RIGHT);  
	    result.setFont(new Font("Dialog", Font.PLAIN, 40));
	    result.setForeground(Color.white);
	    result.setBounds(0, 50, width-20, 70); 
	    result.setBorder(new LineBorder(Color.gray, 0));

	    resultPanel.add(expression);	//위에 정의해놓은 라벨 2개 Panel에 넣기
		resultPanel.add(result);
	    
		JPanel buttonPanel = new JPanel(); //계산기 버튼부분을 구현하는 Panel
	    
		// 버튼 레이어 셋팅
	    buttonPanel.setLayout(grid);
	    buttonPanel.setBounds(0, 120, width, 324); // x:0, y:120 위치 width x 324 크기
	    buttonPanel.setBackground(darkColor);
	    
	    gbc.fill = GridBagConstraints.BOTH; // 수평,수직 꽉채움
	    gbc.weightx = 1.0; // x축 비율 1
	    gbc.weighty = 1.0;// y축 비율 1
	  
	    //makeFrame을 사용할 때 인덱스 쓰기 위해 선언
	    int x=0;
	    int y=0;
	    
	    //buttons[i]배열에 있는 각각의 텍스트대로 버튼을 생성하고, 글씨체, 색깔 설정해주고, 버튼눌렀을때 액션리스너 추가해줌
	    for(int i = 0; i<button_names.length; i++) {
	        buttons[i] = new JButton(button_names[i]);
	        buttons[i].setFont(new Font("Dialog", Font.BOLD, 20));
	        buttons[i].setForeground(Color.white);
	        buttons[i].addActionListener(new ButtonClickListener());
	        
	        // 각각의 버튼 색 설정
	        if(button_names[i].matches("[÷+=x-]")) {
	          buttons[i].setBackground(new Color(255, 159, 9));
	        }else if(button_names[i].matches("[C±%]")) {
	          buttons[i].setBackground(new Color(97, 99, 102));
	        }else {
	          buttons[i].setBackground(new Color(123, 125, 127));
	        }
	  			
	        // 격자 형태 생성 ====== gbc 설정으로 각각 버튼의 크기 할당
	        if(button_names[i] == "0") {
	          makeFrame(buttons[i], x, y, 2, 1);
	          x++;
	        }else {
	          makeFrame(buttons[i], x, y, 1, 1);
	        }
	  			
	        x++;
	        if(x > 3) {
	          x = 0;
	          y++;
	        }
	        // ====== ======
	  			
	        buttons[i].setBorder(tB);	 //테두리설정
	        buttonPanel.add(buttons[i]);
	        buttons[i].setOpaque(true);	 //지정한 배경색을 쓰기위해서 필요
	    }
	    
	    add(resultPanel);	//계산기 JFrame 안에 JPanel 위 아래 2개 배치
	    add(buttonPanel);
	    
	    setTitle("계산기");
	    setVisible(true);
	    setSize(width, height);
	    setBackground(Color.DARK_GRAY);
	    setLocationRelativeTo(null); // 화면 가운데에 띄우기
	    setResizable(false); // 사이즈조절 불가
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫을때 실행중인 프로그램 도 종료
	  }	    
	
	//버튼 클릭했을때 액션 리스너
	class ButtonClickListener implements ActionListener {
    	
		//실제 수행하는 메소드 재정의
        @Override
        public void actionPerformed(ActionEvent e) {
            String operation=e.getActionCommand();
            
            if(operation.equals("C")) {
            	expression.setText("");
            	result.setText("0");
            }            
            else if(operation.equals("=")) {     
            	String resultd=Double.toString(calculate(expression.getText()));
            	result.setText(resultd);
            	expression.setText("");
            	num="";
            }	
            else if(operation.equals("+") || operation.equals("-") || operation.equals("x") || operation.equals("÷")) {
            	if(expression.getText().equals("") && operation.equals("-")) {
            		expression.setText(expression.getText()+e.getActionCommand());
            	}
            	else if (!expression.getText().equals("") && !prev_operation.equals("+") && !prev_operation.equals("-") && !prev_operation.equals("x") && !prev_operation.equals("÷")) {
            		expression.setText(expression.getText()+e.getActionCommand());
            	}
            }
            else {
        		expression.setText(expression.getText()+e.getActionCommand());
            }
            prev_operation=e.getActionCommand();
        }
    }
	
	//계산식 숫자,연산자 나뉘어 ArrayList에 저장하는 메소드
	private void fullTextParsing(String inputText) {
		equation.clear(); //비우기
		
		//for문으로 arraylist 각각의 항목에 접근
		for(int i=0;i<inputText.length();i++) {
			char ch= inputText.charAt(i);
			
			if(ch=='-' || ch=='+' || ch=='x' || ch=='÷') {
				equation.add(num); //연산기호이전 숫자묶음 배열에 넣음
				num="";
				equation.add(String.valueOf(ch)); //연산기호 배열에추가
			}
			else {
				num=num+ch;	//숫자 계속 추가
			}
		}
		equation.add(num); //마지막 수
	}
	
	public double calculate(String inputText) {
		fullTextParsing(inputText);
		
		double prev = 0;
		double current=0;
		
		String mode="";
		
		for(int i=0;i<equation.size();i++) {
			String s=equation.get(i);
			
			if(s.equals("+")) {
				mode="add";
			}
			else if(s.equals("-")) {
				mode="sub";
			}
			else if(s.equals("x")) {
				mode="mul";
			}
			else if(s.equals("÷")) {
				mode="div";
			}
			else {
				if((mode.equals("mul") || mode.equals("div")) && !s.equals("+") && !s.equals("-") && !s.equals("x") && !s.equals("÷")) {
					Double one =  Double.parseDouble(equation.get(i-2));
					Double two = Double.parseDouble(equation.get(i));
					Double result=0.0;
					
					if (mode.equals("mul")) {
						result=one*two;
					}
					else if (mode.equals("div")) {
						result= one/two;
					}
					
					equation.add(i+1,Double.toString(result));
					
					for (int j=0;j<3;j++) equation.remove(i-2);
				}
			}	
		}
		
		for(String s: equation) {
			if(s.equals("+")) {
				mode="add";
			}
			else if(s.equals("-")) {
				mode="sub";
			}
			else {
				current=Double.parseDouble(s);
				
				if (mode.equals("add")) {
					prev += current;
				}
				else if(mode.equals("sub")) {
					prev -= current;
				}
				else prev = current;
			}
			prev =  Math.round(prev*100000) / 100000.0;
		}
		return prev;
	}
	
	//GridBagConstraints 구성요소 설정함수
	public void makeFrame(JButton button, int x , int y, int w, int h) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
	    	    
	    grid.setConstraints(button, gbc);
	  }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calculator calc = new Calculator();	//계산기 실행
	}

}
