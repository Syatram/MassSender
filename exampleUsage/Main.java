import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Current UNIX Timestamp: " + System.currentTimeMillis());
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter payloads: ");
        String payloadsStr = scan.nextLine();

        List<String> payloads = Utils.extractPayloads(payloadsStr);

        for(String payload : payloads) {
            Utils.setPayload(payload);
            System.out.println("Current payload: " + payload);
            SecretInfo secretInfo = new SecretInfo(payload, Utils.totpNow());

            try(HttpClient httpClient = HttpClient.newHttpClient()) {
                if(secretInfo.getChallengeType() != null) {
                    HttpRequest postRequest = HttpRequest.newBuilder()
                            .uri(new URI("https://trades.roblox.com/v1/trades/send"))
                            .POST(HttpRequest.BodyPublishers.ofString(payload))
                            .headers("content-type", "application/json", "Cookie", ".ROBLOSECURITY=" + SecretInfo.getROBLOSECURITY(), "X-Csrf-Token", secretInfo.getXCSRF(),"rblx-challenge-metadata", secretInfo.getChallengeMetaData(), "rblx-challenge-id", secretInfo.getChallengeID(), "rblx-challenge-type", secretInfo.getChallengeType())
                            .build();
                    HttpResponse<String> postResponse =  httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
                    System.out.println("\u001b[30m" + postResponse.body());
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(10000);
        }
    }
}