package fr.pomp.adfuell.utils.munix;

import android.util.Base64;

import java.lang.System;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by munix on 07/04/16.
 */
public class AESCrypt {

    private final Cipher cipher;
    private final SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    /**
     * Constructor
     *
     * @param SEED_16_CHARACTER
     * @throws Exception
     */
    public AESCrypt( String SEED_16_CHARACTER ) throws Exception {
        MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
        digest.update( SEED_16_CHARACTER.getBytes( "UTF-8" ) );
        byte[] keyBytes = new byte[32];
        System.arraycopy( digest.digest(), 0, keyBytes, 0, keyBytes.length );

        cipher = Cipher.getInstance( "AES/CBC/PKCS7Padding" );
        key = new SecretKeySpec( keyBytes, "AES" );
        spec = getIV();
    }

    private AlgorithmParameterSpec getIV() {
        byte[] iv = {
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
        };
        IvParameterSpec ivParameterSpec;
        ivParameterSpec = new IvParameterSpec( iv );

        return ivParameterSpec;
    }

    /**
     * Encripta un texto
     *
     * @param plainText
     * @return
     * @throws Exception
     */
    public String encrypt( String plainText ) throws Exception {
        cipher.init( Cipher.ENCRYPT_MODE, key, spec );
        byte[] encrypted = cipher.doFinal( plainText.getBytes( "UTF-8" ) );
        String encryptedText = new String( Base64.encode( encrypted, Base64.DEFAULT ), "UTF-8" );

        return encryptedText;
    }

    /**
     * Desencripta un texto encriptado
     *
     * @param cryptedText
     * @return
     * @throws Exception
     */
    public String decrypt( String cryptedText ) throws Exception {
        cipher.init( Cipher.DECRYPT_MODE, key, spec );
        byte[] bytes = Base64.decode( cryptedText, Base64.DEFAULT );
        byte[] decrypted = cipher.doFinal( bytes );
        String decryptedText = new String( decrypted, "UTF-8" );

        return decryptedText;
    }
}
