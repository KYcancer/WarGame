import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Game implements Serializable {

    private static final long serialVersionUID = -6466740727038499166L;             //シリアルバージョンUIDを設定
    public int win = 0;                                                                //勝利したかのチェック
    public int wonCard = 0;                                                            //獲得したカード
    private int count = 1;                                                             //何回戦かのカウント
    int lostCard = 0;                                                                   //取られたカード
    int board = 0;                                                                      //場のカード
    Map<Integer, String> myHandDeck = new HashMap<>();                                  //myHandDeckという自分の手札用のMapを作成
    HashMap<Integer, String> cpuHandDeck = new HashMap<>();                             //cpuHandDeckというcpuの手札用のMapを作成

    public void gamePlay() {

        int end = 0;                                                                    //最後のチェック
        String cpuHand;                                                                 //mapのvalue
        String myHand;                                                                  //mapのvalue
        int myCard;                                                                     //mapのkey
        int cpuCard;                                                                    //mapのkey
        SerializableTest st = new SerializableTest();                                   //SerializableTestクラスをインスタンス化
        DeckSet ds = new DeckSet();                                                     //DeckSetクラスをインスタンス化

        System.out.println("*******War Game*******");
        System.out.println("");

        if(myHandDeck.size() == 0) {
            ds.deckSet(myHandDeck);                                                      //deckSetメソッドでmyHandDeckにカードを格納
            ds.deckSet(cpuHandDeck);                                                     //deckSetメソッドでcpuHandDeckにカードを格納
        }



        do {

                System.out.println("### 第" + count + "回戦 ###");
                System.out.println("場に積まれた札:" + board + "枚");
                System.out.println("CPUの持ち札:" + cpuHandDeck.size() + "枚, 獲得した札:" + lostCard + "枚");
                System.out.println("あなたの持ち札:" + myHandDeck.size() + "枚, 獲得した札:" + wonCard + "枚");
                System.out.print("札を切りますか? (d:札を切る, q:中断) >");
                String select = new Scanner(System.in).nextLine();
                if(select.equals("d") || select.equals("D")) {                        //札を切るを選択した場合
                    while(true) {
                        cpuCard = new Random().nextInt(14);                           //ランダムにkeyを取り出してcpuCardに代入
                        cpuHand = cpuHandDeck.get(cpuCard);                           //そのkeyからvalueを取り出し
                        if(cpuHandDeck.containsKey(cpuCard)) {                        //そのkeyがmapの中にあれば無限ループから抜ける
                            break;                                                   //なければループ
                        }
                    }
                    System.out.println("CPUが切った札:[スペード" + cpuHand + "]");
                    board++;                                                          //場に1枚追加

                    while(true) {
                        myCard = new Random().nextInt(14);                           //ランダムにkeyを取り出してcpuCardに代入
                        myHand = myHandDeck.get(myCard);                              //そのkeyからvalueを取り出し
                        if(myHandDeck.containsKey(myCard)) {                         //そのkeyがmapの中にあれば無限ループから抜ける
                            break;                                                   //なければループ
                        }
                    }
                    System.out.println("あなたが切った札:[ダイア" + myHand + "]");
                    board++;                                                         //場に1枚追加

                    if(cpuCard < myCard) {                                           //keyの数字の大きさで比較、勝った場合
                        System.out.println("あなたが獲得しました!");
                        wonCard += board;                                            //場のカードを自分の獲得カードに追加
                        board = 0;                                                   //場のカードを0にする
                    }else if(cpuCard > myCard) {                                    //keyの数字の大きさで比較、負けた場合
                        System.out.println("CPUが獲得しました!");
                        lostCard += board;                                           //場のカードをcpuの獲得カードに追加
                        board = 0;                                                   //場のカードを0にする
                    }else {                                                         //keyの数字の大きさで比較、引き分けた場合
                        System.out.println("引き分けです。");
                    }
                    cpuHandDeck.remove(cpuCard);                                    //使用したkey,valueを削除
                    myHandDeck.remove(myCard);                                      //使用したkey,valueを削除
                    count++;                                                        //～回戦の値を増やす
                    System.out.println("");
                    if(myHandDeck.size() == 0) {                                    //手札がなくなった場合
                        end = 1;                                                   //最終チェックに1を代入してループから抜ける
                        break;
                    }
                }else if(select.equals("q") || select.equals("Q")) {               //中断を選択した場合
                    System.out.println("ゲームを中断します");
                    st.serializableOutput(this);                                   //このインスタンスをシリアライズする
                    System.exit(0);
                }else {                                                            //入力ミスした場合の表記
                    System.out.println("dまたはqの文字を入力してください");
                    System.out.println("");
                }

        }while(end == 0);                                                         //終了時の表記

        try {
            Files.delete(Paths.get(Main.object));                                //ゲームが終了したので中断ファイルが残っている場合削除
        } catch (IOException e1) {
        }

        System.out.println("### 最終結果 ###");
        System.out.println("CPUが獲得した札:" + lostCard + "枚");
        System.out.println("あなたが獲得した札:" + wonCard + "枚");
        if(wonCard > lostCard) {                                                   //勝った場合
            System.out.println("あなたが勝ちました、おめでとう！");
            win = 1;                                                               //winに1を代入

        }else if(wonCard < lostCard) {                                            //負けた場合
            System.out.println("CPUの勝ちです。");
        }else {                                                                   //引き分けの場合
            System.out.println("引き分けです。");
        }
    }
}
