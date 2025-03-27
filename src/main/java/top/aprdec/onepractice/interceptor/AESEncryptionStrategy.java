package top.aprdec.onepractice.interceptor;

import com.easy.query.core.basic.extension.encryption.EncryptionStrategy;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.util.CryptoUtil;

@Component
public class AESEncryptionStrategy implements EncryptionStrategy {
    @Override
    public Object encrypt(Class<?> aClass, String s, Object o) {
        if(o == null) return null;
        return CryptoUtil.encrypt(o.toString());
    }

    @Override
    public Object decrypt(Class<?> aClass, String s, Object o) {
        if (o == null) return null;
        return CryptoUtil.decrypt(o.toString());
    }
}
