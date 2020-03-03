import view.PetFrame;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author Ayase
 * @date 2019/10/24-15:05
 */
public class VirtualPet {
    public static void main(String[] args) {
        int firstX = 1700;
        int firstY = 800;
        try {
            InputStreamReader reader=new InputStreamReader(new FileInputStream("data\\setting.dat"),"GBK");
            BufferedReader bfreader=new BufferedReader(reader);
            String line;
            int i = 1;
            //包含该行内容的字符串，不包含任何行终止符，如果已到达流末尾，则返回 null
            while((line=bfreader.readLine())!=null) {
                switch (i){
                    case 1 :firstX = Integer.parseInt(line);break;
                    case 2 :firstY = Integer.parseInt(line);break;
                    default:break;
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        new PetFrame(firstX,firstY);
    }
}
