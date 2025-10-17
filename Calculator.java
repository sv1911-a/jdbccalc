import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Calculator extends JFrame implements ActionListener {
    JTextField text = new JTextField();
    JTextArea historyArea = new JTextArea();
    String operator;
    double firstNum, secondNum, result;
    JButton[] numberButtons = new JButton[10];
    JButton addButton, subButton, mulButton, divButton, equButton, clrButton;
    String historyFile = "calc_history.txt";
    public Calculator() {
        setTitle("Calculator");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        // Dark mode colors
        Color bgColor = new Color(34,34,34);
        Color fgColor = new Color(230,230,230);
        Color buttonColor = new Color(64,64,64);
        getContentPane().setBackground(bgColor);
        // Text field
        text.setBounds(50, 25, 350, 50);
        text.setEditable(false);
        text.setBackground(new Color(50,50,50));
        text.setForeground(fgColor);
        text.setFont(new Font("Arial", Font.BOLD, 24));
        add(text);
        // History area
        historyArea.setBounds(50, 500, 380, 150);
        historyArea.setBackground(new Color(50,50,50));
        historyArea.setForeground(fgColor);
        historyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        historyArea.setEditable(false);
        add(historyArea);
        // Load previous history
        loadHistory();
        // Buttons
        for(int i=0;i<10;i++){
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setBackground(buttonColor);
            numberButtons[i].setForeground(fgColor);
            numberButtons[i].setFont(new Font("Arial", Font.BOLD, 20));
            numberButtons[i].addActionListener(this);
        }
        addButton = new JButton("+"); subButton = new JButton("-");
        mulButton = new JButton("*"); divButton = new JButton("/");
        equButton = new JButton("="); clrButton = new JButton("C");
        JButton[] funcButtons = {addButton, subButton, mulButton, divButton, equButton, clrButton};
        for(JButton btn : funcButtons){
            btn.setBackground(buttonColor);
            btn.setForeground(fgColor);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(this);
        }
        int startX=50, startY=100, buttonWidth=80, buttonHeight=50, gapX=20, gapY=20;
        // Number buttons 1-9
        int x=startX, y=startY;
        for(int i=1;i<=9;i++){
            numberButtons[i].setBounds(x,y,buttonWidth,buttonHeight);
            add(numberButtons[i]);
            x += buttonWidth + gapX;
            if(i % 3==0){ x=startX; y+=buttonHeight+gapY; }
        }
        // Zero button centered
        numberButtons[0].setBounds(startX + buttonWidth + gapX, y, buttonWidth, buttonHeight);
        add(numberButtons[0]);
        // Operator buttons on right
        addButton.setBounds(startX + 3*(buttonWidth + gapX), startY, buttonWidth, buttonHeight);
        subButton.setBounds(startX + 3*(buttonWidth + gapX), startY + buttonHeight + gapY, buttonWidth, buttonHeight);
        mulButton.setBounds(startX + 3*(buttonWidth + gapX), startY + 2*(buttonHeight + gapY), buttonWidth, buttonHeight);
        divButton.setBounds(startX + 3*(buttonWidth + gapX), startY + 3*(buttonHeight + gapY), buttonWidth, buttonHeight);
        // Equals button centered below numbers
        equButton.setBounds(startX + buttonWidth + gapX, y + buttonHeight + gapY, buttonWidth, buttonHeight);
        add(equButton);
        // Clear button below operators
        clrButton.setBounds(startX + 3*(buttonWidth + gapX), startY + 4*(buttonHeight + gapY), buttonWidth, buttonHeight);
        add(clrButton);
        add(addButton); add(subButton); add(mulButton); add(divButton);
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        for(int i=0;i<10;i++){
            if(e.getSource()==numberButtons[i]){ text.setText(text.getText()+i); }
        }
        if(e.getSource()==addButton){ firstNum=Double.parseDouble(text.getText()); operator="+"; text.setText(""); }
        if(e.getSource()==subButton){ firstNum=Double.parseDouble(text.getText()); operator="-"; text.setText(""); }
        if(e.getSource()==mulButton){ firstNum=Double.parseDouble(text.getText()); operator="*"; text.setText(""); }
        if(e.getSource()==divButton){ firstNum=Double.parseDouble(text.getText()); operator="/"; text.setText(""); }
        if(e.getSource()==equButton){
            secondNum=Double.parseDouble(text.getText());
            switch(operator){
                case "+": result=firstNum+secondNum; break;
                case "-": result=firstNum-secondNum; break;
                case "*": result=firstNum*secondNum; break;
                case "/": result=firstNum/secondNum; break;
            }
            String calculation = firstNum + " " + operator + " " + secondNum + " = " + result;
            text.setText(String.valueOf(result));
            // Append to local history file
            try (FileWriter fw = new FileWriter(historyFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                out.println(timestamp + " - " + calculation);
            } catch(IOException ex){ ex.printStackTrace(); }
            // Update history area
            historyArea.append(calculation + "\n");
            firstNum=result;
        }
        if(e.getSource()==clrButton){ text.setText(""); }
    }
    private void loadHistory(){
        try {
            File file = new File(historyFile);
            if(file.exists()){
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while((line = br.readLine()) != null){
                    historyArea.append(line + "\n");
                }
                br.close();
            }
        } catch(IOException ex){ ex.printStackTrace(); }
    }
    public static void main(String[] args){
        new Calculator();
    }
}
