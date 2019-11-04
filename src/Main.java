import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String INPUT_FILE = "D:\\CSV\\war_game_output.csv";       //CSV用のPathを設定
    private static final String OUTPUT_FILE = "D:\\CSV\\war_game_output.csv";
    static Path in = Paths.get(INPUT_FILE);
    static Path out = Paths.get(OUTPUT_FILE);
    public static final String object = "D:\\test\\14_1.dat";                       //中断ファイル用のPathを設定

    public static void csvRead() {                                                  //保存しているCSVファイルの読み込み、保存しているCSVファイルがなければ作成

        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(in);   //linesにCSVを読み込ませる
        } catch (NoSuchFileException e) {                                            //もしファイルがなければ
            ArrayList<String> lines = new ArrayList<String>();                       //linesというArrayListを作成
            lines.add("ゲーム回数,勝利回数,最大獲得カード枚数");                      //1行目にそれぞれの見出しを,区切りで格納
            lines.add("0,0,0");                                                       //2行目に0を,区切りで格納
            try {
                Files.write(out, lines, CREATE_NEW);                                //linesをCSVに書き込み
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
        	 e.printStackTrace();
        }
    }

    public static void csvWrite(int win, int wonCard) {                            //引数として渡されてきたカードを使用してCSVファイルに書き込む

        try {
            ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(in);   //CSVファイルをArrayListのlinesに格納
            List<String> split = new ArrayList<String>();                            //splitというListを作成
            split.addAll(Arrays.asList(lines.get(1).split(",")));                    //splitにlinesの数値部分を,を省いてそれぞれ格納

            int resultGame = Integer.parseInt(split.get(0));                       //resultGameにゲーム回数の値を数値に変えて代入
            int resultWin = Integer.parseInt(split.get(1));                        //resultWinに勝利回数の値を数値に変えて代入
            int resultGetCard = Integer.parseInt(split.get(2));                    //resultGetCardに最大獲得枚数の値を数値に変えて代入

            resultGame++;                                                             //ゲーム回数に+1
            if(win == 1) {                                                            //もしwinの値が1なら
                resultWin++;                                                          //勝利回数の値に+1
                if(resultGetCard < wonCard) {                                         //もし最大獲得枚数に格納されている値より今回獲得した枚数の方が多い場合
                    resultGetCard = wonCard;                                          //今回獲得した値を最大獲得枚数に代入
                }
                win = 0;                                                              //winの値をリセット
            }

	        split.set(0, String.valueOf(resultGame));                                //resultGameの値を文字列に変換してsplitの0番目に格納
	        split.set(1, String.valueOf(resultWin));                                 //resultWinの値を文字列に変換してsplitの1番目に格納
	        split.set(2, String.valueOf(resultGetCard));                             //resultGetCardの値を文字列に変換してsplitの2番目に格納

	        lines.set(1, String.join(",", split));                                    //splitの内容にそれぞれ,を追加してlinesに格納
	        Files.delete(Paths.get("D:\\CSV\\war_game_output.csv"));                 //今回読み込んだCSVの削除
	        Files.write(out, lines, CREATE_NEW);                                    //新たに今回の結果を加えた内容をCSVで書き込み
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static Game serializeRead() {                                            //中断したファイルが存在した場合、読み込み、もしくは新規ゲームの開始
	    SerializableTest st = new SerializableTest();                                //SerializableTestのシリアライズ化
	    Path filePath = Paths.get(object);                                          //前回のファイルが残っているか確認する為ファイルのパスを指定する
	    Game game = new Game();                                                      //Gameのインスタンス化
	    if(Files.exists(filePath)) {                                                 //ファイルの確認
	        while(true) {                                                            //ファイルが存在している場合の無限ループ
	            System.out.print("中断したゲームを再開しますか?[Yes/No]＞");
	            String select = new Scanner(System.in).nextLine();
	            if(select.equals("y") || select.equals("Y")) {                        //入力した値がyの場合
	                System.out.println("中断したゲームを再開します。");
	                game = st.serializableInput();                                    //デシリアライズ
	                System.out.println("");
	                break;
	            }else if(select.equals("n") || select.equals("N")) {                  //入力した値がnの場合
	            	System.out.println("新規でゲームを開始します。");
	                System.out.println("");
	                break;
	            }else {                                                               //入力した値がyでもnでもなかった場合
	                System.out.println("yまたはnの文字を入力してください。");
	                System.out.println("");
	            }
	        }
	    }
	    return game;                                                                  //Gameのインスタンスを返す
	}


	public static void main(String[] args) {

	    csvRead();
	    Game game = serializeRead();                                                 //gameにデシリアライズした内容を代入
	    game.gamePlay();                                                               //GameクラスのgamePlayメソッドを実行
	    csvWrite(game.win, game.wonCard);                                             //Gameのフィールドの勝利したかどうかのwinと獲得カード枚数のをwonCardを引数として渡す
	}
}