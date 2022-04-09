/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.encrypt;

import com.gridnine.elsa.common.core.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@Component
public class DesCodec {

    private Cipher encryptCipher;
    private Cipher decryptCipher;

    @Autowired
    public void setEnvironment(Environment env) throws Exception {
        final String KEY = env.getProperty("elsa.encryptionKey", "1234345356");
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, getKey(KEY.getBytes()));
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, getKey(KEY.getBytes()));
    }

    private Key getKey(byte[] arrBTmp) {
        final byte[] arrB = new byte[8];

        var i = 0;
        while (i < arrBTmp.length && i < arrB.length) {
            arrB[i] = arrBTmp[i];
            i++;
        }
        return new SecretKeySpec(arrB, "DES");
    }

    public String encrypt(String strIn) {
        return Base64.getEncoder().encodeToString(encrypt(strIn.getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] encrypt(byte[] arrB) {
        return ExceptionUtils.wrapException(()-> encryptCipher.doFinal(arrB));
    }

    public byte[] decrypt(byte[] arrB){
        return ExceptionUtils.wrapException(()->decryptCipher.doFinal(arrB));
    }

    public String decrypt(String strIn){
        return new String(decrypt(Base64.getDecoder().decode(strIn)), StandardCharsets.UTF_8);
    }
}
