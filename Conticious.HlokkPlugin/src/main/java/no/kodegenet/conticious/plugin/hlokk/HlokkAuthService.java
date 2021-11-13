package no.kodegenet.conticious.plugin.hlokk;

import no.kodegenet.conticious.plugin.hlokk.data.HlokkUser;
import no.kodegenet.conticious.plugin.hlokk.util.RandomString;

import java.util.*;

/**
 * Created by joachimhaagenskeie on 16/08/2017.
 */
public class HlokkAuthService {
    private static HlokkAuthService instance = null;
    private static Map<String, HlokkUser> tokenMap;
    private static long lastCheckTimestamp = 0;

    private HlokkAuthService() {
        tokenMap = new HashMap<>();
    }

    public static HlokkAuthService getInstance() {
        if (instance == null) {
            instance = new HlokkAuthService();
        }

        return instance;
    }

    public String generateTokenForUser(String username) {
        String newToken = new RandomString(6).nextString();

        tokenMap.put(newToken, new HlokkUser(newToken, username));

        purgeOldTokens();

        return newToken;
    }

    public HlokkUser authenticateToken(String token, String username) {
        HlokkUser hlokkUser = tokenMap.get(token);

        /**
         * Only return  user if token and username matches.
         */
        if (hlokkUser != null && hlokkUser.getUsername() != null && hlokkUser.getUsername().equals(username)) {
            tokenMap.remove(token);
            return hlokkUser;
        } else {
            return null;
        }
    }

    /**
     * Only check once every 5 minutes. If a token is older than 7 days, delete it
     */
    private void purgeOldTokens() {
        long now = new Date().getTime();
        List<String> tokensToDelete = new ArrayList<>();

        if (now - lastCheckTimestamp > 300000l) { //5 minutes
            for (String token : tokenMap.keySet()) {
                if (now - tokenMap.get(token).getGeneratedTimestamp() > 604800000l) { // 7 days
                    tokensToDelete.add(token);
                }
            }

            for (String token : tokensToDelete) {
                tokenMap.remove(token);
            }

            lastCheckTimestamp = now;
        }
    }

}
