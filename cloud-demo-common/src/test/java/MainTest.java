import com.google.common.io.Files;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class MainTest {

    @Test
    public void splitTest1(){
        String jwtStr = "exp=1550495628, user_name=user, authorities=[ROLE_tgb, ROLE_yhn, ROLE_wsx, ROLE_ik, ROLE_qaz, ROLE_ujm, ROLE_ol, ROLE_edc, ROLE_p, ROLE_rfv], jti=b153b8ee-abc3-4f4e-9832-35241f8345fc, client_id=abc, scope=[select]";
        String pattern = ",(?=[^,]+=[^,]+)";
        String[] split = jwtStr.split(pattern);
        System.out.println(split.length);
        for (String s : split) {
            System.out.println(s);
        }
    }
    @Test
    public void splitTest2(){
        String jwtStr = "ROLE_tgb, ROLE_yhn, ROLE_wsx, ROLE_ik, ROLE_qaz, ROLE_ujm, ROLE_ol, ROLE_edc, ROLE_p, ROLE_rfv";
        String pattern = ",\\s{0,1}";
        String[] split = jwtStr.split(pattern);
        System.out.println(split.length);
        for (String s : split) {
            System.out.println(s);
        }
    }
    @Test
    public void modifyFile() throws IOException {

        try (BufferedReader bufferedReader = Files.newReader(new File("D:/fql(1).del"), Charset.forName("utf-8"));
             BufferedWriter bufferedWriter = Files.newWriter(new File("D:/fql-fix.sql"), Charset.forName("utf-8"));){
            String dataStr;
            while ((dataStr = bufferedReader.readLine()) !=null){
                String formatDataStr = formatDataStr(dataStr);
                bufferedWriter.write(formatDataStr,0,formatDataStr.length());
                bufferedWriter.newLine();
            }
            bufferedWriter.write("commit;");
        }

    }

    private String formatDataStr(String dataStr){
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = dataStr.split(",");
        if(split!=null && split.length>=12){
            stringBuilder.append("insert into tbl_asii_acct_data values (");
            for (int i = 0; i < 12; i++) {
                if(split[i].length() == 2){
                    stringBuilder.append("'',");
                    continue;
                }

                if(i==7 || i==8){
                    stringBuilder.append("to_date('")
                            .append(split[i].substring(1,split[i].length()-1)).append("','")
                            .append("YYYY-MM-DD HH24:MI:SS'),");
                }else{
                    stringBuilder.append("'")
                            .append(split[i].substring(1,split[i].length()-1))
                            .append("',");
                }
            }
            stringBuilder.setLength(stringBuilder.length()-1);
            stringBuilder.append(");");
        }
        return stringBuilder.toString();
    }

}
