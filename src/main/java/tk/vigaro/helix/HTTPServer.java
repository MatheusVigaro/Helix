package tk.vigaro.helix;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class HTTPServer {
    HttpServer server;
    HTTPServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(43433), 0);
        server.createContext("/webhook", new GitHubWebHook());
        server.start();
    }

    private class GitHubWebHook implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (!httpExchange.getRequestHeaders().containsKey("X-GitHub-Event")) {return;}
            StringBuilder builder = new StringBuilder();
            BufferedReader stream = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(), "UTF-8"));
            String input;
            while ((input = stream.readLine()) != null) {
                builder.append(input);
            }
            JSONObject json = new JSONObject(builder.toString());
            System.out.println(json);
            JSONArray commits = json.getJSONArray("commits");
            for (int i = 0;i < commits.length();i++) {
                JSONObject commit = commits.getJSONObject(i);
                String message = commit.getString("message");
                String author = commit.getJSONObject("author").getString("name");
                String url = commit.getString("url");

            }
        }
    }
}
