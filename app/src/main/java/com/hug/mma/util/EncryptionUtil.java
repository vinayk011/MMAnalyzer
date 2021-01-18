package com.hug.mma.util;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Raviteja on 24-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class EncryptionUtil {
    private static final byte[] keyValue = new byte[]{'$', 'E', '*', 'S', '^', 'p', 'b', '#', 's', '8', 'H', '1', 'U', '0', 'N', '3'};

    public static String encrypt(String data) {
        if (data != null) {
            Cipher cipher = null;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec secretKey = new SecretKeySpec(keyValue, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(secretKey.getEncoded());
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT).replace("\n", "").replace("\r", "");
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
