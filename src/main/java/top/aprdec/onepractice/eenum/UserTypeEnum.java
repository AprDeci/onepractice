package top.aprdec.onepractice.eenum;

public enum UserTypeEnum {
    ADMIN(1),

    VIP(2),

    NORMAL(3);

    private Integer usertypecode;

     UserTypeEnum(Integer usertypecode) {
         this.usertypecode = usertypecode;
     }

    public Integer code() {
        return this.usertypecode;
    }

    public String strcode(){
        return this.usertypecode.toString();
    }


    @Override
    public String toString() {
        return strcode();
    }
    public static String  getUserTypeByCode(Integer code){
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.code().equals(code)) {
                return userTypeEnum.name();
            }
        }
        return "NORMAL";
    }
}
