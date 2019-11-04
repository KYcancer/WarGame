import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SerializableTest {

    private static final String DATA_FILE = "d:\\test\\14_1.dat";                                             //中断ファイル保存先のPathを指定

    public void serializableOutput(Game game) {                                                                 //中断ファイル書き出し

        try (
            ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(Paths.get(DATA_FILE)));) {  //指定したPathでObjectOutputStreamをインスタンス化

            os.writeObject(game);                                                                                //引数で渡されてきたgameインスタンスをシリアライズ
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Game serializableInput() {                                                                           //中断ファイル読み込み
        Game game = null;
        try (
            ObjectInputStream is = new ObjectInputStream(Files.newInputStream(Paths.get(DATA_FILE)));) {     //指定したPathでObjectInputStreamをインスタンス化

            game = (Game) is.readObject();                                                                       //指定されたPathからgameインスタンスをデシリアライズ

        } catch (IOException | ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return game;                                                                                            //読み込んだgameインスタンスを返す

    }
}