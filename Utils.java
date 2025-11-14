import java.time.Duration;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTPGenerator;
import com.google.gson.Gson;

public class Utils {
    private static final Gson gson = new Gson();
    private static String payload;

    public static String getMetaDataChallengeId(String metadata) throws Exception {
        while (metadata.isEmpty()) {
            new SecretInfo(payload, totpNow());
            System.out.println("Empty meta data");
            Thread.sleep(10000);
        }
        byte[] decoded = Base64.getDecoder().decode(metadata);
        String decodedStr = new String(decoded, StandardCharsets.UTF_8);
        System.out.println(decodedStr);
        String metaDataChallengeId = decodedStr.substring(decodedStr.indexOf(":\"", decodedStr.indexOf("challengeId")) + 2, decodedStr.indexOf("\",", decodedStr.indexOf("challengeId")));

        System.out.println(metaDataChallengeId);
        return metaDataChallengeId;
    }

    public static String prepareMetaData(Bodies.VerificationMetadata verification) {
        byte[] verificationBytes = gson.toJson(verification).getBytes();
        return Base64.getEncoder().encodeToString(verificationBytes);
    }

    public static String rawMetaData(Bodies.VerificationMetadata verification) {
        System.out.println(gson.toJson(verification));
        return gson.toJson(verification);
    }

    /**
     * Code yoinked from <a href="https://github.com/BastiaanJansen/otp-java">...</a>
     * */
    public static String totpNow() {
        // Generate a secret (or use your own secret)
        byte[] secret = SecretInfo.getOTP().getBytes();

        TOTPGenerator totp = new TOTPGenerator.Builder(secret)
                .withHOTPGenerator(builder -> {
                    builder.withPasswordLength(6);
                    builder.withAlgorithm(HMACAlgorithm.SHA1); // SHA256 and SHA512 are also supported
                })
                .withPeriod(Duration.ofSeconds(30))
                .build();
        try {
            return totp.now();
        } catch (IllegalStateException e) {
            System.out.println(e);
            return "Error";
        }
    }

    public static ArrayList<String> extractPayloads(String input) {
        ArrayList<String> payloads = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{.*?]}");
        Matcher matcher = pattern.matcher(input);

        while(matcher.find()) {
            payloads.add(matcher.group());
        }

        return payloads;
    }

    public static String getPayload() {
        return payload;
    }

    public static void setPayload(String payload) {
        Utils.payload = payload;
    }
}
