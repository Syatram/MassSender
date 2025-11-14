import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Bodies {
    public static class ContinueBody {
        private String challengeId;
        private String challengeMetadata;
        private String challengeType;

        public ContinueBody(String challengeId, String challengeMetadata, String challengeType) {
            this.challengeId = challengeId;
            this.challengeMetadata = challengeMetadata;
            this.challengeType = challengeType;
        }
    }

    public static class TwoStepBody {
        final private int actionType;
        final private String challengeId;
        final private String code;

        public TwoStepBody(int actionType, String challengeId, String code) {
            this.actionType = actionType;
            this.challengeId = challengeId;
            this.code = code;
        }
    }

    public static class VerificationMetadata {

        private final String verificationToken;
        private final boolean rememberDevice;
        private final String challengeId;
        private final String actionType;

        public VerificationMetadata(String verificationToken, boolean rememberDevice, String challengeId, String actionType) {
            this.verificationToken = verificationToken;
            this.rememberDevice = rememberDevice;
            this.challengeId = challengeId;
            this.actionType = actionType;
        }
    }
}
