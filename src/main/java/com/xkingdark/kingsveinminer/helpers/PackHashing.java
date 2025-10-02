package com.xkingdark.kingsveinminer.helpers;

import net.minecraft.resource.ResourcePack;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.UUID;

public class PackHashing {
    public static byte[] computePackHash() {
        try (InputStream input = ResourcePack.class.getResourceAsStream("/resourcepacks/kings-vein-miner.zip")) {
            if (input == null)
                throw new IllegalStateException("Resource pack not found in JAR.");

            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1)
                sha1.update(buffer, 0, read);

            return sha1.digest();
        }
        catch (Exception e) {
            return null;
        }
    };

    public static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest)
            sb.append(String.format("%02x", b));

        return sb.toString();
    };

    public static UUID calculateUUID(byte[] sha1) {
        long msb = 0, lsb = 0;

        for (int i = 0; i < 8; i++)
            msb = (msb << 8) | (sha1[i] & 0xff);

        for (int i = 8; i < 16; i++)
            lsb = (lsb << 8) | (sha1[i] & 0xff);

        return new UUID(msb, lsb);
    };
};