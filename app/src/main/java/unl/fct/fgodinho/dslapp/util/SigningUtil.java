package unl.fct.fgodinho.dslapp.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import android.util.Base64;

public class SigningUtil {

    public static byte[] sign(byte[] toSignBytes, byte[] privateKeyBytes) {

        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec ks = new PKCS8EncodedKeySpec(privateKeyBytes);
            RSAPrivateKey privKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);

            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privKey);
            sig.update(new String(toSignBytes).trim().getBytes());
            byte[] signature = sig.sign();

            return Base64.encode(signature, Base64.NO_WRAP);
        } catch (SignatureException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
