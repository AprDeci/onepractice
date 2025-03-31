package top.aprdec.onepractice.util;

import org.springframework.stereotype.Component;
import top.aprdec.onepractice.eenum.PaperTypeEnum;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.entity.proxy.PaperDOProxy;

import java.util.HashMap;
import java.util.Map;


@Component
public class PaperUtil {
    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("CET-4", "英语四级");
        TYPE_MAP.put("CET-6", "英语六级");
        // 可以继续添加其他类型
    }

    public static String getFullpapername(String name,String year,String month,String type) {;
        String oname = TYPE_MAP.get(type);
        StringBuilder sb = new StringBuilder();
        sb.append(sb.append(year).append("年")
                .append(month)).append("月")
                .append(oname).append(name);

        return sb.toString();
    }

    public static int[] getPartQuestionCount(String type){
        if(type.equals(PaperTypeEnum.CET4.getType())){
            return new int[]{1,20,30,1};
        } else if (type.equals(PaperTypeEnum.CET6.getType())){
            return new int[]{1,20,30,1};
        }else {
            return null;
        }
    }

    public static Long getPartCount(String type){
        if(type.equals(PaperTypeEnum.CET4.getType())){
            return 4L;
        } else if (type.equals(PaperTypeEnum.CET6.getType())){
            return 4L;
        }else {
            return null;
        }
    }
}
