package top.aprdec.onepractice.commmon.constant;

public class RedisKeyConstant {

    public static final String LOCK_USER_REGISTER = "onepractice-user-service:lock:user-register:";

    public static final String CAPTCHA_EMAIL = "onepractice:captcha-service:email:";

    public static final String USER_RECORD = "onepractice:record:user:";

    public static final String USER_RECORD_SORTED_SET = "onepractice:user:record:sorted:";

    public static final String PAPER_VOTE_KEY = "onepractice:paper:stats:";

//   用户收藏单词表
    public static final String USER_SAVED_WORD_LIST = "onepractice:user:savedword:";
}
